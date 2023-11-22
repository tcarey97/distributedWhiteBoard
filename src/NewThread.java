// Full name: Thomas Carey
// Student ID: 831811

import javafx.scene.Parent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class NewThread extends Thread{
    private String peerUsername;
    private WhiteBoard managerWhiteBoard;
    private Socket clientSocket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    public NewThread(Socket aClientSocket, WhiteBoard managerWhiteBoard) throws IOException {
        this.clientSocket = aClientSocket;
        this.managerWhiteBoard = managerWhiteBoard;

//        Get the input/output streams for reading/writing data from/to the socket
        this.outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inFromClient = new ObjectInputStream(clientSocket.getInputStream());

        this.peerUsername = (String) readFromClient();

        boolean managerResponseToRequest = managerWhiteBoard.approvePeer(this.peerUsername);

        checkUserName();

        if (managerResponseToRequest){
            System.out.println("request accepted");
            writeToClient("request accepted @@@" + peerUsername);
            //create a new thread per connection

        } else {
            System.out.println("request declined");
            writeToClient("request declined");
            return;
        }


        managerWhiteBoard.addUserName(peerUsername);

        managerWhiteBoard.userListPrint.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedUser = (String) managerWhiteBoard.userListPrint.getSelectedValue();

                String managerUserName = (String) managerWhiteBoard.listModel.get(0);
                if (selectedUser == managerUserName){
                    return;
                }

                if (selectedUser != null){
                    if (managerWhiteBoard.kickUser(selectedUser)){
                        managerWhiteBoard.listModel.removeElement(selectedUser);

                        managerWhiteBoard.userListPrint.revalidate();
                        managerWhiteBoard.userListPrint.repaint();

                    }
                }
            }
        });


        writeToClient(managerWhiteBoard.listModel);

        this.start();

    }

    private void checkUserName() {
        if (managerWhiteBoard.listModel.contains(this.peerUsername)){
            this.peerUsername = this.peerUsername + managerWhiteBoard.listModel.size();
        }
    }

    @Override
    public void run(){

        try {

            while (true) {

                writeToClient(managerWhiteBoard.drawnShapesList);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                writeToClient(managerWhiteBoard.listModel);

                readOperations();

            }

        } catch (RuntimeException r){
            managerWhiteBoard.peerExit(this.peerUsername);

        }
        // close the client connection
        finally {
            managerWhiteBoard.removeUserName(this.peerUsername);

            managerWhiteBoard.userListPrint.revalidate();
            managerWhiteBoard.userListPrint.repaint();

            try{;
                clientSocket.close();
                inFromClient.close();
                outToClient.close();

            } catch(IOException e){
            }

        }


    }

    private void readOperations() throws RuntimeException{

        try {
            clientSocket.setSoTimeout(500);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        ParentShape shape = (ParentShape) readFromClient();

        if(shape == null){
            return;
        }

        managerWhiteBoard.addShape(shape);

    }

    private void writeToClient(Object input){
        // Send the input Object to the server by writing to the socket output stream
        try {

            outToClient.reset();
            outToClient.writeObject(input);
            outToClient.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    private Object readFromClient() throws RuntimeException{
        Object readObject = null;

        try {
            readObject = inFromClient.readObject();

        } catch (SocketTimeoutException e) {
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return readObject;
    }


}
