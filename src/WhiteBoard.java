// Full name: Thomas Carey
// Student ID: 831811

import org.jdom.input.SAXBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class WhiteBoard extends JFrame{

    private javax.swing.JPanel JPanel;
    protected JPanel whiteBoardSpace;
    protected JComboBox drawnShapesMenu;
    private JButton addButton;
    private JLabel selectLabel;
    private JComboBox addShapeMenu;
    private JComboBox colourMenu;
    private JTextField userInput;
    private JLabel inputTextLabel;
    public JList userListPrint;
    private JLabel colourLabel;
    public JButton fileButton;

    /// My own attributes
    File selectedFile = null;
    private ParentShape selectedComponent;
    private int xPress;
    private int yPress;
    private int xRelease;
    private int yRelease;

    public ArrayList<ParentShape> drawnShapesList = new ArrayList<ParentShape>();
    protected String selectedShapeString = null;
    private int textCount = 0;
    private int lineCount = 0;
    private int circleCount = 0;
    private int rectangleCount = 0;
    private int ovalCount = 0;
    public boolean addedComponent = false;
    private boolean userAdded = false;
    private boolean userRemoved = false;
    public DefaultListModel listModel = new DefaultListModel();
    JPopupMenu filePopUp = new JPopupMenu("File");
    JMenuItem newFile = new JMenuItem("New");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem saveAs = new JMenuItem("Save As");


    public WhiteBoard (){

        setContentPane(JPanel);

        setTitle("Manager Whiteboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        setMinimumSize(new Dimension(800,500));

        userListPrint.setModel(listModel);

        filePopUp.add(newFile);
        filePopUp.add(open);
        filePopUp.add(save);
        filePopUp.add(saveAs);

        whiteBoardSpace.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                xPress = e.getX();
                yPress = e.getY();

            }
        });

        whiteBoardSpace.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                xRelease = e.getX();
                yRelease = e.getY();

                createNewShape();
            }
        });

        drawnShapesMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedShapeString = (String) drawnShapesMenu.getSelectedItem();

                for (ParentShape i : drawnShapesList){
                    if (selectedShapeString.equals(i.getName())){
                        selectedComponent = i;
                    }
                }

            }
        });

        addShapeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addShapeMenu.getSelectedItem().equals("Text field")){
                    inputTextLabel.show();
                    userInput.show();
                } else {
                    inputTextLabel.hide();
                    userInput.hide();
                }

            }
        });


        userListPrint.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                userAdded = true;
            }
        });
        userListPrint.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                super.componentRemoved(e);
                userRemoved = true;
            }
        });

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filePopUp.show(fileButton, xPress, yPress);

            }
        });

        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WhiteBoard();
                JOptionPane approvePeerPopup = new JOptionPane();
                int selectedOption = approvePeerPopup.showConfirmDialog(null, "This new Whiteboard is not collaborative. You can draw and save your progress, but please open the file" +
                                " in the original manager window if you wish it to be shared",
                        "Warning", JOptionPane.WARNING_MESSAGE);

            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane approvePeerPopup = new JOptionPane();
                int selectedOption = approvePeerPopup.showConfirmDialog(null,
                        "Opening will override your current Whiteboard. Please confirm with Yes to proceed.",
                        "Warning", JOptionPane.YES_NO_OPTION);


                if (selectedOption == approvePeerPopup.YES_OPTION){
                    SAXBuilder builder = new SAXBuilder();
                    try {
                        JFileChooser chooser = new JFileChooser();

                        int returnVal = chooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            selectedFile = chooser.getSelectedFile();
                            if (selectedFile.canRead() && selectedFile.exists()) {
                                runFile(selectedFile);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectedFile != null){
                    ObjectOutputStream outStream = null;
                    try {
                        outStream = new ObjectOutputStream(new FileOutputStream(selectedFile));
                        outStream.writeObject(drawnShapesList);
                        outStream.flush();
                        outStream.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                } else {
                    JOptionPane approvePeerPopup = new JOptionPane();
                    int selectedOption = approvePeerPopup.showConfirmDialog(null, "You have not picked a file to save to. Please proceed with Save As first.",
                            "Cannot save", JOptionPane.WARNING_MESSAGE);
                }


            }
        });

        saveAs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser();

                int returnVal = chooser.showSaveDialog(null);
                try {
                    if (returnVal == JFileChooser.APPROVE_OPTION) {

                        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()));
                        outStream.writeObject(drawnShapesList);
                        outStream.flush();
                        outStream.close();

                       selectedFile = chooser.getSelectedFile();

                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



    }

    private void runFile(File selectedFile) {

        ObjectInputStream inStream = null;
        try {
            inStream = new ObjectInputStream(new FileInputStream(selectedFile));
        } catch (IOException e) {
            errorPopUp();
        }

        try {
            ArrayList<ParentShape> readInShapes = (ArrayList<ParentShape>) inStream.readObject();
            for (ParentShape shape : readInShapes){
                addShape(shape);
            }
        } catch (IOException e) {
            errorPopUp();
        } catch (ClassNotFoundException e) {
            errorPopUp();
        }

        try {
            inStream.close();
        } catch (IOException e) {
            errorPopUp();
        }

    }

    private void errorPopUp(){
        JOptionPane approvePeerPopup = new JOptionPane();
        int selectedOption = approvePeerPopup.showConfirmDialog(null, "There was an error opening the file",
                "Cannot open", JOptionPane.WARNING_MESSAGE);
    }

    private synchronized void createNewShape() {

        String selectedColour = colourMenu.getSelectedItem().toString();

        if (addShapeMenu.getSelectedItem().equals("Text field") && !userInput.getText().equals("")){
            Text text = new Text(userInput.getText(), xPress, yPress, selectedColour);
            textCount += 1;
            text.setName("Text field: \"" + userInput.getText() + "\"");

            addedComponent = true;
            drawnShapesList.add(text);

            whiteBoardSpace.add(text);
            whiteBoardSpace.revalidate();
            whiteBoardSpace.repaint();


            drawnShapesMenu.addItem(text.getName());

            return;
        }

        if (xRelease-xPress > 10){
            if (addShapeMenu.getSelectedItem().equals("Line")){
                Line line = new Line(xPress, yPress, xRelease, yRelease, selectedColour);
                lineCount += 1;
                line.setName("Line #" + lineCount);

                addedComponent = true;
                drawnShapesList.add(line);

                whiteBoardSpace.add(line);
                whiteBoardSpace.revalidate();
                whiteBoardSpace.repaint();


                drawnShapesMenu.addItem(line.getName());

            } else if (addShapeMenu.getSelectedItem().equals("Rectangle")){

                Rectangle rectangle = new Rectangle(xPress, yPress, xRelease, yRelease, selectedColour);
                rectangleCount += 1;
                rectangle.setName("Rectangle #" + rectangleCount);

                addedComponent = true;
                drawnShapesList.add(rectangle);

                whiteBoardSpace.add(rectangle);
                whiteBoardSpace.revalidate();
                whiteBoardSpace.repaint();


                drawnShapesMenu.addItem(rectangle.getName());

            } else if (addShapeMenu.getSelectedItem().equals("Circle")){

                Circle circle = new Circle(xPress, yPress, xRelease, yRelease, selectedColour);
                circleCount += 1;
                circle.setName("Circle #" + circleCount);

                addedComponent = true;
                drawnShapesList.add(circle);

                whiteBoardSpace.add(circle);
                whiteBoardSpace.revalidate();
                whiteBoardSpace.repaint();

                drawnShapesMenu.addItem(circle.getName());

            } else if (addShapeMenu.getSelectedItem().equals("Oval")){

                Oval oval = new Oval(xPress, yPress, xRelease, yRelease, selectedColour);
                ovalCount += 1;
                oval.setName("Oval #" + ovalCount);

                addedComponent = true;
                drawnShapesList.add(oval);

                whiteBoardSpace.add(oval);
                whiteBoardSpace.revalidate();
                whiteBoardSpace.repaint();

                drawnShapesMenu.addItem(oval.getName());

            }
        }


    }




    public boolean approvePeer(String requestUsername){

        JOptionPane approvePeerPopup = new JOptionPane();
        int selectedOption = approvePeerPopup.showConfirmDialog(null,
                "\"" + requestUsername + "\" wants to join the Whiteboard",
                "Approve Peer", JOptionPane.YES_NO_OPTION);


        if (selectedOption == approvePeerPopup.YES_OPTION){
            return true;
        } else{
            return false;
        }

    }

    public boolean kickUser(String kickedUserName){

        JOptionPane approvePeerPopup = new JOptionPane();
        int selectedOption = approvePeerPopup.showConfirmDialog(null,
                "Do you want to kick \"" + kickedUserName + "\" from the shared Whiteboard",
                "Kick user", JOptionPane.YES_NO_OPTION);


        if (selectedOption == approvePeerPopup.YES_OPTION){
            return true;
        } else{
            return false;
        }

    }

    public boolean managerExit(){

        JOptionPane approvePeerPopup = new JOptionPane();
        int selectedOption = approvePeerPopup.showConfirmDialog(null, "The manager left the Whiteboard. You are unable to collaborate",
                "Whiteboard interrupted", JOptionPane.WARNING_MESSAGE);


        if (selectedOption == approvePeerPopup.OK_OPTION){
            return true;
        }

        return false;
    }

    public void peerExit(String peerName){

        JOptionPane approvePeerPopup = new JOptionPane();
        int selectedOption = approvePeerPopup.showConfirmDialog(null, peerName + " quit the Whiteboard.",
                "Notification", JOptionPane.WARNING_MESSAGE);

    }

    public synchronized void addShape(ParentShape shape){
        whiteBoardSpace.add(shape);
        whiteBoardSpace.revalidate();
        whiteBoardSpace.repaint();

        drawnShapesList.add(shape);
        drawnShapesMenu.addItem(shape.getName());

    }

    public synchronized void addUserName(String userName){
        listModel.addElement(userName);
    }

    public synchronized void removeUserName(String userName){
        listModel.removeElement(userName);
    }

}










