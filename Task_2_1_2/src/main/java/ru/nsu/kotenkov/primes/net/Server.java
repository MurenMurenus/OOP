package ru.nsu.kotenkov.primes.net;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


public class Server {
    private ServerSocket serverSocket;
    private final Socket[] clientSocket;
    private final PrintWriter[] out;
    private final BufferedReader[] in;
    private final int numOfClients;

    public Server(int port, int numOfClients) {
        clientSocket = new Socket[numOfClients];
        out = new PrintWriter[numOfClients];
        in = new BufferedReader[numOfClients];
        this.numOfClients = numOfClients;

        try {
            System.out.println("Receiving connections");
            serverSocket = new ServerSocket(port);
            for (int i = 0; i < numOfClients; i++) {
                clientSocket[i] = serverSocket.accept();
                out[i] = new PrintWriter(clientSocket[i].getOutputStream(), true);
                in[i] = new BufferedReader(new InputStreamReader(clientSocket[i].getInputStream()));
                System.out.println("+ client");
            }
            System.out.println("All clients have connected successfully");
        } catch (IOException e) {
            System.err.println("ERR IN INIT SERVER: " + e);
        }

    }

    public boolean start(int[] numbers) {
        int batchSize = Math.floorDiv(numbers.length, numOfClients) + 1;
        for (int i = 0; i < numOfClients; i++) {
            if (i == numOfClients - 1) {
                out[i].println(Arrays.toString(
                                Arrays.copyOfRange(numbers,
                                        i * batchSize,
                                        numbers.length)
                        )
                );
            } else {
                out[i].println(Arrays.toString(
                                Arrays.copyOfRange(numbers,
                                        i * batchSize,
                                        (i + 1) * batchSize)
                        )
                );
            }

        }

        System.out.println("All parts are sent, waiting for results");
        try {
            for (int i = 0; i < numOfClients; i++) {
                boolean res = Boolean.parseBoolean(in[i].readLine());
                System.out.println("+ result: " + res);
                if (res) {
                    stop();
                    return true;
                }
            }

            stop();
            return false;
        } catch (IOException e) {
            System.err.println("ERR WHILE WAITING FOR THE RES: " + e);
        }

        return false;
    }

    public void stop() throws IOException {
        for (int i = 0; i < numOfClients; i++) {
            in[i].close();
            out[i].close();
            clientSocket[i].close();
        }

        serverSocket.close();
    }
}
