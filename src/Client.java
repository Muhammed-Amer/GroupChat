import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) throws IOException {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }
    }

    // the function works in the main thread of the client
    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                if(!messageToSend.isEmpty()) {
                    bufferedWriter.write(userName + ": " + messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }
    }

    // explicitly making a new thread for reading messages (the thread that starts the client)
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while(socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();

                        if (messageFromGroupChat == null || messageFromGroupChat.equals("SERVER_SHUTDOWN")) {
                            System.out.println("SERVER: SERVER CLOSED!");
                            closeEveryThing(socket, bufferedReader, bufferedWriter);
                            System.exit(0);
                            break;
                        }
                        System.out.println(messageFromGroupChat);
                    } catch (IOException e) {
                        closeEveryThing(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEveryThing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                // closing a socket will also close it's out/input stream
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your user name for the group Chat: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 1237);
        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();
    }
}
