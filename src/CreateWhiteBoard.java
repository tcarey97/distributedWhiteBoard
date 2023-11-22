//// Full name: Thomas Carey
//// Student ID: 831811

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class CreateWhiteBoard {

    private final String serverAddress;
    private final int port;
    private final String userName;
    private WhiteBoard managerWhiteBoard;

    public static void main(String[] args) {

        //command-line arguments
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String userName = args[2];

        CreateWhiteBoard createWhiteBoard = new CreateWhiteBoard(serverAddress
                , port, userName);

        createWhiteBoard.setUpServer();


    }

    public CreateWhiteBoard(String serverAddress, int port, String userName) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.userName = userName;
    }

    private void setUpServer() {
        ServerSocket listeningSocket = null;
        Socket clientSocket = null;

        try {
            //Create a server socket listening on requested port
            listeningSocket = new ServerSocket(port);

            System.out.println("Manager Whiteboard created");

            managerWhiteBoard = new WhiteBoard();
            managerWhiteBoard.fileButton.show();

            managerWhiteBoard.listModel.addElement("Manager: " + this.userName);


            int clientNumber = 0; //counter to keep track of the number of clients

            //Listen for incoming connections forever
            while (true)
            {
                System.out.println("Whiteboard manager listening on port " + this.port + " for a connection");

                //Accept an incoming client connection request
                clientSocket = listeningSocket.accept(); //This method will block until a connection request is received


                String clientDetails = "Client " + clientNumber + " connection accepted: " + "\n" +
                        "   Remote Port: " + clientSocket.getPort() + "\n" +
                        "   Remote Hostname: " + clientSocket.getInetAddress().getHostName();

                System.out.println(clientDetails);

                clientNumber++;

                NewThread aNewThread = new NewThread(clientSocket, this.managerWhiteBoard);

            }
        }
        catch (SocketException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {

            try
            {
                // close the server socket
                listeningSocket.close();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
