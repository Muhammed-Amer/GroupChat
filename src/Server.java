import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // A Server Socket is to wait for incoming client connection request
    // After the server accepts the connection it will create a socket object
    // connection established between client and server
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    // keeps our server running
    public void startServer() {
        try{
            // whenever a client request to connect, the server creates a new socket object
            while(!serverSocket.isClosed()) {
                // whenever a client connects a socket object is returned
                Socket socket = serverSocket.accept();
                System.out.println("A new client is Connected!");

                // when a class implements the runnable interface,
                // the objects of the class will be executed by a separate thread
                // each object of ClientHandler will be responsible for communicating with a client
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // when an error occurs we need to shut down our serverSocket
    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // our server will be listening to clients that are making a connection to this port number
        ServerSocket serverSocket = new ServerSocket(1237);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
