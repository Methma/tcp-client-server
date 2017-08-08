package tcpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by methma on 8/3/17.
 */
public class ThreadHandledServer {
    public static final int SERVICE_PORT = 9898;
    public static final int MAX_POOL_SIZE = 3;
    public static final int MIN_POOL_SIZE = 2;
    public static final int QUEUE_SIZE = 2;

    public static void main(String[] args) throws Exception {

        ExecutorService executorService;
        //Create a bounded queue
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);

        //Pass created queue with min max thread pool sizes to executor service
        executorService = new ThreadPoolExecutor(MIN_POOL_SIZE, MAX_POOL_SIZE,0L, TimeUnit.MILLISECONDS, queue);


        System.out.println("Thread handled server is running...");
        int clientNumber = 1;
        ServerSocket listener = new ServerSocket(SERVICE_PORT);
        try {
            while (true) {

                executorService.execute(new ThreadProcessor(listener.accept(),clientNumber++));

            }
        } finally {
            executorService.shutdown();
        }
    }

    private static class ThreadProcessor extends Thread {
        private Socket socket;
        private int clientNumber;

        public ThreadProcessor(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }


        public void run() {
            try {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");

                // Get messages from the client, line by line; return them capitalized
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    out.println(input.toUpperCase());
                }
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        private void log(String message) {
            System.out.println(message);
        }

    }
}
