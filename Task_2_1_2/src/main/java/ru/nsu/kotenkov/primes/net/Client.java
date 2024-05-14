package ru.nsu.kotenkov.primes.net;


import ru.nsu.kotenkov.primes.calculus.LinearChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOERR IN CLIENT" + e);
        }
    }

    public boolean start() {
        try {
            String myPart = in.readLine();
            System.out.println(myPart);

            boolean myRes = LinearChecker.check(new int[]{Integer.parseInt(myPart)});
        } catch (IOException e) {
            System.err.println("IOERR IN CLIENT WHILE PROCESSING ITS PART: " + e);
        }

        return false;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
