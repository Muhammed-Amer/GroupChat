import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    // keep track of all clients
    // whenever a client sends a message, we can loop through all clients and send the message to each client
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();

            if (this.clientUserName == null || this.clientUserName.trim().isEmpty()) {
                throw new IOException("Invalid username received");
            }

            clientHandlers.add(this);
            broadCastMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
         String messageFromClient;

         while(socket.isConnected()) {
             try {
                 messageFromClient = bufferedReader.readLine();
                 if (messageFromClient == null) {
                     // Client has disconnected
                     break;
                 }
                 broadCastMessage(messageFromClient);
             } catch (IOException e) {
//                 closeEveryThing(socket, bufferedReader, bufferedWriter);
                 break;
             }
         }
        closeEveryThing(socket, bufferedReader, bufferedWriter);
    }

    // something wrong here
    public void broadCastMessage(String messageToSend) {
        if(messageToSend == null) {
            return;
        }
        for(ClientHandler clientHandler: clientHandlers) {
            try {
                if(!clientHandler.clientUserName.equals(clientUserName)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEveryThing(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadCastMessage("SERVER: " + clientUserName + " has left the chat!");
    }

    public void closeEveryThing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}
