# Java Chat Application

A simple **client-server** chat application using **Java socket programming**. Multiple clients can connect to the server, and messages are broadcast in real-time.

## Features
- **Multi-client support** with separate threads for each client.
- **Broadcast messaging** to all clients except the sender.
- **Automatic client disconnection handling**.

## How It Works

### 1. **Server**
- Listens for client connection requests on a specific port.
- Creates a new thread for each client connection using the `ClientHandler` class.

### 2. **ClientHandler**
- Manages communication between the server and each client.
- **Broadcasts messages** from one client to all other clients.
- Handles client disconnection and resource cleanup.

### 3. **Client**
- Sends and receives messages.
- Each client runs on its own thread, with messages broadcast to all connected clients.
- Sending messages is handled by the main thread of the client object. 
- listening to messages is handled by a new separate thread.

## Code Structure

- **Server.java**: Initializes `ServerSocket`, accepts connections, and manages threads.
- **ClientHandler.java**: Handles client communication and broadcasting messages.
- **Client.java**: Sends messages and listens for incoming messages.

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Muhammed-Amer/GroupChat.git
   cd GroupChat/src
   ```
2. Compile the server and client:

    ```bash
    javac Server.java
    javac Client.java
    ```

3. Start the server:

    ```bash
    java Server
    ```

4. In another terminal, run the client:
   you can make a terminal to each client.
    ```bash
    java Client
    ```