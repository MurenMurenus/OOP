package groovyscripts


import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


def config = new GroovyClassLoader().parseClass(
        new File(this.class.getResource("/groovyscripts/config.groovy").getFile())
).newInstance()


def evaluate(Set groups, String lab) {
    println '--------------------'
    println lab + ':'

    def results = new HashMap()
    for (groupDirectory in groups) {
        def groupResults = new HashMap()
        def connector = GradleConnector.newConnector()

        groupPath = new File("./repos/" + groupDirectory)
        studentsSubDirectories = groupPath.list(new FilenameFilter() {
            @Override
            boolean accept(File current, String name) {
                return new File(current, name).isDirectory()
            }
        })

        println '----------'
        println 'Students in group ' + groupDirectory + ':'
        if (studentsSubDirectories == null) {
            println 'No repositories for this group'
            continue
        }
        println studentsSubDirectories
        println 'Testing:'

        for (student in studentsSubDirectories) {
            println student
            def studentResults = [
                    build: '-',
                    javadoc: '-',
                    test: '-'
            ]

            def fullLabPath = './repos/' + groupDirectory + '/' + student + '/' + lab
            studentResults['path'] = fullLabPath

            connector.forProjectDirectory(new File(fullLabPath))
            def connection = connector.connect()

            build = connection.newBuild()

            // BUILD
            try {
                build.forTasks('build')
                        .addArguments('-x', 'test')
                        .run()
                studentResults['build'] = '+'
            } catch (Exception e) {
                println "Building " + fullLabPath + " failed " + e
            }

            // TESTS
            build = connection.newBuild()
            try {
                build.forTasks('test')
                        .run()
            } catch (Exception e) {
                println "Testing of " + fullLabPath + " resulted in exception " + e
            }

            try {
                link = fullLabPath + '/build/reports/tests/test/index.html'
                testSummary = new File(link)
                document = Jsoup.parse(testSummary)

                value = document.getElementById("successRate").select("div.percent").first().text()
                println 'TESTS COMPLETED ' + value

                studentResults['test'] = value
                studentResults['summaryHTML'] = document.getElementById("content").outerHtml()
            } catch (Exception e) {
                println "Getting results of tests " + fullLabPath + " resulted in exception " + e
            }

            // DOCS
            try {
//                build = connection.newBuild()
//                println build.addJvmArguments('')
                build.forTasks('javadoc')
                        .run()
                studentResults['javadoc'] = '+'
            } catch (Exception e) {
                println "Javadoc for " + fullLabPath + " failed " + e
            }

            if (studentResults['javadoc'] == '+') {
                try {
                    link = fullLabPath + '/build/docs/javadoc/allpackages-index.html'
                    testSummary = new File(link)
                    document = Jsoup.parse(testSummary)

                    //                studentResults['javadocHTML'] = document.select("div.summary-table two-column-summary").outerHtml()
                    studentResults['javadocHTML'] = document.select("div.col-first").outerHtml()  // for index-all
                    println 'javadocs stolen'
                    println studentResults['javadocHTML']
                } catch (Exception e) {
                    println "Cannot steal " + fullLabPath + " failed " + e
                }
            }

            if (groupDirectory in groupResults.keySet()) {
                groupResults[student] add studentResults
            } else {
                groupResults[student] = studentResults
            }
            connection.close()
        }

        results[groupDirectory] = groupResults
    }

    return results
}

def shell = new GroovyShell()
source = new GroovyCodeSource(this.class.getResource("/groovyscripts/cloning.groovy"))
shell.run(source, Collections.singletonList(""))

def allLabResults = new HashMap()
for (lab in config.tasks) {
    def labResults = evaluate(config.groups.keySet(), lab)
    allLabResults[lab] = labResults
}

println '----------'
println '----------'
//println allLabResults

return allLabResults
