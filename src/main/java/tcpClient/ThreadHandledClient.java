package tcpClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by methma on 8/3/17.
 */
public class ThreadHandledClient {

    public static final int SERVICE_PORT = 9898;
    public static final String SERVER_ADDRESS = "localhost";


    public static void main(String[] args) throws Exception {

        Socket socket = new Socket(SERVER_ADDRESS, SERVICE_PORT);

        getMessageFromServer(socket);

        //Send the message to the server
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        System.out.println("\n Enter your lower case text for capitalize bellow :");

        Scanner sc=new Scanner(System.in);
        String request=sc.next();

        sendMessageToServer(request,bw);
        getMessageFromServer(socket);

    }


    public static void getMessageFromServer(Socket socket){
        //Get the return message from the server
        InputStream is = null;
        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String message = null;
        try {
            message = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Message received from the server : " +message);
    }

    public static void sendMessageToServer(String request, BufferedWriter bw){
        String sendMessage = request + "\n";
        try {
            bw.write(sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Message sent to the server : "+sendMessage);
    }

}
