import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class    Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                String response=null;
                String operation=null;
                ClientHandler clientHandler = new ClientHandler(socket,operation, response);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(33333);
        Server server = new Server(serverSocket);
        server.startServer();
        System.out.println("server iniciado");
    }
}
