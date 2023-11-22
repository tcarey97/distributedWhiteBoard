// Full name: Thomas Carey
// Student ID: 831811

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.ArrayList;
public class JoinWhiteBoard {

    private Socket socket = null;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
//    private String userInput = "";
    private WhiteBoard peerWhiteBoard;
    private final String serverAddress;
    private final int port;
    private String userName;
    private boolean readShapes = true;


    public static void main(String[] args) {

        //command-line arguments
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String userName = args[2];

        JoinWhiteBoard joinWhiteBoard = new JoinWhiteBoard(serverAddress
                , port, userName);

        joinWhiteBoard.connectToSocket();

    }

    public JoinWhiteBoard(String serverAddress, int port, String userName){
        this.serverAddress = serverAddress;
        this.port = port;
        this.userName = userName;

    }

    private void connectToSocket() {
        try
        {
            socket = new Socket(serverAddress, port);

            // Get the input/output streams for reading/writing data from/to the socket
            outObject = new ObjectOutputStream(socket.getOutputStream());
            inObject = new ObjectInputStream(socket.getInputStream());

            sendUserName();

            String request = (String) readFromServer();
            if (request.contains("request accepted")){
                String [] stringArray = request.split("@@@");
                this.userName = stringArray[1];
                startWhiteBoard();

            } else {
                System.out.println("The manager denied your request to access the Whiteboard");
            }
        } catch (RuntimeException e){
            System.out.println("The manager closed the Whiteboard");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally {

            if (peerWhiteBoard != null && peerWhiteBoard.managerExit() == true){
                peerWhiteBoard.dispose();
            };

            System.out.println("Goodbye");

            // Close the socket
            try
            {
                socket.close();
                outObject.close();
                inObject.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void startWhiteBoard() throws RuntimeException {
        this.peerWhiteBoard = new WhiteBoard();
        this.peerWhiteBoard.fileButton.hide();
        this.peerWhiteBoard.setTitle("Peer WhiteBoard");

        DefaultListModel existingUsers = (DefaultListModel) readFromServer();
        lookForDiscrepancyUserList(peerWhiteBoard.listModel, existingUsers);


        //remove shapes
//        peerWhiteBoard.removeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                if (peerWhiteBoard.selectedComponent != null){
//                    peerWhiteBoard.whiteBoardSpace.remove(peerWhiteBoard.selectedComponent);
//                    peerWhiteBoard.whiteBoardSpace.revalidate();
//                    peerWhiteBoard.whiteBoardSpace.repaint();
//
//                    try {
//                        writeToServer(peerWhiteBoard.selectedComponent);
//                    } catch (SocketException ex) {
//                        throw new RuntimeException(ex);
//                    }
//
//                    /////REMOVING FROM OWN DRAWN SHAPES MENU
//                    peerWhiteBoard.drawnShapesList.remove(peerWhiteBoard.selectedComponent);
//                    peerWhiteBoard.drawnShapesMenu.removeItem(peerWhiteBoard.selectedShapeString);
//
//                    if (peerWhiteBoard.drawnShapesList.size() == 0){
//                        peerWhiteBoard.selectedComponent = null;
//                    }
//                }
//
//            }
//        });


        try {

            socket.setSoTimeout(500);

            while(true){

                writeOperations();

                Object receivedObject = readFromServer();

                if (receivedObject == null){
                    continue;
                }

                if (readShapes){
                    readShapes = false;
                    try{
                        ArrayList <ParentShape> updatedShapes = (ArrayList<ParentShape>) receivedObject;
                        lookForDiscrepancy(peerWhiteBoard.drawnShapesList, updatedShapes);
//                        System.out.println("received shapes");
                    }
                    catch (ClassCastException e){
//                        System.out.println("could not read shapes");

                    }

                } else {
                    readShapes = true;
                    try {
                        DefaultListModel updatedUserList = (DefaultListModel) receivedObject;
                        lookForDiscrepancyUserList(peerWhiteBoard.listModel, updatedUserList);
//                        System.out.println("received userlist");
                    } catch (ClassCastException g){
//                        System.out.println("could not read userlist");

                    }
                }

            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    private void lookForDiscrepancyUserList(DefaultListModel existingUserList, DefaultListModel receivedUserList) {

        for (int i = 0; i<receivedUserList.size(); i++){
            Object receivedUser = receivedUserList.get(i);
            if (!existingUserList.contains(receivedUser)){
                peerWhiteBoard.listModel.addElement(receivedUser);
            }
        }

        for (int i = 0; i<existingUserList.size(); i++){
            Object existingUser = existingUserList.get(i);
            if (!receivedUserList.contains(existingUser)){

                if (existingUser.equals(this.userName)){
                    System.out.println("You have been kicked by the manager");
                    System.out.println("Goodbye");
                    System.exit(0);
                }
                peerWhiteBoard.listModel.removeElement(existingUser);
            }
        }

    }

    private void writeOperations() throws SocketException {

        if (peerWhiteBoard.addedComponent == true){

            peerWhiteBoard.addedComponent = false;

            int addedComponentIndex = peerWhiteBoard.drawnShapesList.size() - 1;
            ParentShape addedComponent = peerWhiteBoard.drawnShapesList.get(addedComponentIndex);
            writeToServer(addedComponent);

        }
    }

    private void sendUserName() throws SocketException {
        writeToServer(this.userName);
    }

    private void writeToServer(Object input) throws SocketException{
        // Send the input Object to the server by writing to the socket output stream
        try {
            outObject.writeObject(input);
            outObject.flush();

        } catch (SocketException e) {
            throw new SocketException("Server disconnected the connection.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Object readFromServer() throws RuntimeException{

        try {
            Object response = inObject.readObject();
            return response;

        } catch (SocketTimeoutException e){
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void lookForDiscrepancy(ArrayList<ParentShape> existingShapesList, ArrayList<ParentShape> receivedShapesList){

        for (ParentShape receivedShape : receivedShapesList){
            if(!existingShapesList.contains(receivedShape)){
                peerWhiteBoard.addShape(receivedShape);
            }

        }

    }



}
