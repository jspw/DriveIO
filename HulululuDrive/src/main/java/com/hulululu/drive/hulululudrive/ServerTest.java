/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hulululu.drive.hulululudrive;

import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.System.in;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import javax.xml.crypto.Data;

/**
 *
 * @author jspw
 */
public class ServerTest extends javax.swing.JFrame {

    private ServerSocket serverSocket;
    static private Socket socket;
    private ObjectOutputStream dataOut;
    private ObjectInputStream dataIn;
    private JPopupMenu popupOptions;
    File[] files;
    ArrayList<String> fileNameList = new ArrayList<String>();
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    static OutputStream os = null;
    DefaultListModel fileListModel;
    static boolean serverOn = false;

    /**
     * Creates new form Server
     */
    public ServerTest() {
        initComponents();
        popupOptions = new JPopupMenu("Delete");
        JMenuItem delete = new JMenuItem("Delete");
        popupOptions.add(delete);
        try {
            String serverIp = InetAddress.getLocalHost().getHostAddress();
            serverIpTextField.setText(serverIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        terminateServerButton.setEnabled(false);

        fileListModel = new DefaultListModel();
        loadFiles();
//        fileList.setModel(fileListModel);
        initFileManager();

    }

    void initFileManager() {
        FileManager fileManager = new FileManager();
        fileManJframe.setContentPane(fileManager.getGui());
        fileManager.showRootFile();
//        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }

    void loadFiles() {
        fileListModel.clear();
        fileNameList.clear();
        String storageDir = createStorageDir();
        File folder = new File(storageDir);
        files = folder.listFiles();
//        System.out.println(files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                fileNameList.add(files[i].getName());
                fileListModel.addElement(files[i].getName());
            }
        }
    }

    public static String createStorageDir() {
        String currentDir = Path.of("").toAbsolutePath().toString();
        System.out.println(currentDir);
        String newDir = currentDir + "/drive";
        File theDir = new File(newDir);
        if (!theDir.exists()) {
            theDir.mkdirs();
        };
        return newDir;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        serverIpLabel = new javax.swing.JLabel();
        serverIpTextField = new javax.swing.JTextField();
        serverPortLabel = new javax.swing.JLabel();
        serverPortTextField = new javax.swing.JTextField();
        createServerbutton = new javax.swing.JButton();
        introLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        receivedFileStatus = new javax.swing.JLabel();
        fileManJframe = new javax.swing.JInternalFrame();
        jMenuBar1 = new javax.swing.JMenuBar();
        serverTernimateButton = new javax.swing.JMenu();
        terminateServerButton = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        setExtendedState(6);

        title.setFont(new java.awt.Font("LM Mono Caps 10", 1, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Server");

        serverIpLabel.setFont(new java.awt.Font("Amiri", 0, 14)); // NOI18N
        serverIpLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        serverIpLabel.setText("IP Address : ");
        serverIpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        serverIpTextField.setEditable(false);
        serverIpTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverIpTextFieldActionPerformed(evt);
            }
        });

        serverPortLabel.setFont(new java.awt.Font("Amiri", 0, 14)); // NOI18N
        serverPortLabel.setText("Port : ");

        serverPortTextField.setText("5000");
        serverPortTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverPortTextFieldActionPerformed(evt);
            }
        });

        createServerbutton.setFont(new java.awt.Font("Cantarell Extra Bold", 0, 13)); // NOI18N
        createServerbutton.setText("Create Server");
        createServerbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createServerbuttonActionPerformed(evt);
            }
        });

        introLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        introLabel.setText("You haven't created server yet");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(serverIpLabel)
                .addGap(31, 31, 31)
                .addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(serverPortLabel)
                .addGap(18, 18, 18)
                .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createServerbutton)
                .addGap(27, 27, 27))
            .addComponent(introLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverIpLabel)
                    .addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverPortLabel)
                    .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createServerbutton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(introLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        receivedFileStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        receivedFileStatus.setText("Latest File");

        fileManJframe.setVisible(true);

        javax.swing.GroupLayout fileManJframeLayout = new javax.swing.GroupLayout(fileManJframe.getContentPane());
        fileManJframe.getContentPane().setLayout(fileManJframeLayout);
        fileManJframeLayout.setHorizontalGroup(
            fileManJframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        fileManJframeLayout.setVerticalGroup(
            fileManJframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 263, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receivedFileStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
            .addComponent(fileManJframe)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(receivedFileStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileManJframe)
                .addContainerGap())
        );

        serverTernimateButton.setText("Server");

        terminateServerButton.setText("Terminate Server");
        terminateServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminateServerButtonActionPerformed(evt);
            }
        });
        serverTernimateButton.add(terminateServerButton);

        jMenuBar1.add(serverTernimateButton);

        jMenu2.setText("Tools");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serverIpTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverIpTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverIpTextFieldActionPerformed

    private void serverPortTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverPortTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverPortTextFieldActionPerformed

    private void createServerbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createServerbuttonActionPerformed
        // TODO add your handling code here:
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int port = Integer.parseInt(serverPortTextField.getText());
                    serverSocket = new ServerSocket(port);
                    introLabel.setText("Server Creating at : " + serverIpTextField.getText() + " on port : " + port);
                    //                Socket s = serverSocket.accept();
                    introLabel.setText("Congrats server is created at : " + serverIpTextField.getText() + " on port : " + port);
                    createServerbutton.setEnabled(false);
                    terminateServerButton.setEnabled(true);
                    serverOn = true;
                    //                    socket = serverSocket.accept();
                    socket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    DataModel data = (DataModel) in.readObject();
                    String name = data.getName();
                    introLabel.setText("New client  " + name + "  has been connected ...\n");

                    //sending file list for the first time
                    ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                    objectOutput.writeObject(fileNameList);

                    while (true) {

                        try {

                            ObjectInputStream sss = new ObjectInputStream(socket.getInputStream());
                            DataModel datax = (DataModel) sss.readObject();
                            String type = datax.getName();
                            System.out.println(type);

                            if (type.equals("upload")) {
                                // Stream to receive data from the client through the socket.
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                // Read the size of the file name so know when to stop reading.
                                int fileNameLength = dataInputStream.readInt();
                                // If the file exists
                                if (fileNameLength > 0) {
                                    // Byte array to hold name of file.
                                    byte[] fileNameBytes = new byte[fileNameLength];
                                    // Read from the input stream into the byte array.
                                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                                    // Create the file name from the byte array.
                                    String fileName = new String(fileNameBytes);
                                    // Read how much data to expect for the actual content of the file.
                                    int fileContentLength = dataInputStream.readInt();
                                    // If the file exists.
                                    if (fileContentLength > 0) {
                                        // Array to hold the file data.
                                        byte[] fileContentBytes = new byte[fileContentLength];
                                        // Read from the input stream into the fileContentBytes array.
                                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                                        // Panel to hold the picture and file name.

                                        receivedFileStatus.setText(fileName);

                                        //save file in storage
                                        String storageDir = createStorageDir();
                                        File file = new File(storageDir + "/" + fileName);
                                        Path path = Paths.get(file.getAbsolutePath());
                                        try {
                                            Files.write(path, fileContentBytes);
                                        } catch (IOException ex) {
                                            JOptionPane.showMessageDialog(ServerTest.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        loadFiles();
//                                        fileList.revalidate();
//                                        fileList.repaint();
                                        ObjectOutputStream x = new ObjectOutputStream(socket.getOutputStream());
                                        x.writeObject(fileNameList);
//                                        fileManJframe.revalidate();
                                        initFileManager();
                                        fileManJframe.repaint();

                                    }
                                }
                            } else if (type.equals("download")) {

                                //get file name for sending to client
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                // Read the size of the file name so know when to stop reading.
                                int fileNameLength = dataInputStream.readInt();

                                if (fileNameLength > 0) {
                                    // Byte array to hold name of file.
                                    byte[] fileNameBytesx = new byte[fileNameLength];
                                    // Read from the input stream into the byte array.
                                    dataInputStream.readFully(fileNameBytesx, 0, fileNameBytesx.length);
                                    // Create the file name from the byte array.
                                    String fileNamex = new String(fileNameBytesx);

                                    System.out.println(fileNamex);

                                    final File[] fileToSend = new File[1];

                                    for (int i = 0; i < files.length; i++) {
                                        if (files[i].getName().equals(fileNamex)) {
                                            fileToSend[0] = files[i];
                                            break;
                                        }
                                    }

                                    // Create an input stream into the file you want to send.
                                    FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                                    // Create a socket connection to connect with the server.
                                    // Create an output stream to write to write to the server over the socket connection.
                                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                    // Get the name of the file you want to send and store it in filename.
                                    String fileName = fileToSend[0].getName();
                                    // Convert the name of the file into an array of bytes to be sent to the server.
                                    byte[] fileNameBytes = fileName.getBytes();
                                    // Create a byte array the size of the file so don't send too little or too much data to the server.
                                    byte[] fileBytes = new byte[(int) fileToSend[0].length()];
                                    // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server.
                                    fileInputStream.read(fileBytes);
                                    // Send the length of the name of the file so server knows when to stop reading.
                                    dataOutputStream.writeInt(fileNameBytes.length);
                                    // Send the file name.
                                    dataOutputStream.write(fileNameBytes);
                                    // Send the length of the byte array so the server knows when to stop reading.
                                    dataOutputStream.writeInt(fileBytes.length);
                                    // Send the actual file.
                                    dataOutputStream.write(fileBytes);

                                }

                            }

                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(ServerTest.this, e, "Error", JOptionPane.ERROR_MESSAGE);
                            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(ServerTest.this, error, "Error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, error);
                }
            }

            private Object getFileExtension(String fileName) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).start();
    }//GEN-LAST:event_createServerbuttonActionPerformed

    private void terminateServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminateServerButtonActionPerformed
        try {
            // TODO add your handling code here:
            serverSocket.close();
            introLabel.setText("Server Closed");
            createServerbutton.setEnabled(true);
            terminateServerButton.setEnabled(false);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ServerTest.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_terminateServerButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerTest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createServerbutton;
    private javax.swing.JInternalFrame fileManJframe;
    private javax.swing.JLabel introLabel;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel receivedFileStatus;
    private javax.swing.JLabel serverIpLabel;
    private javax.swing.JTextField serverIpTextField;
    private javax.swing.JLabel serverPortLabel;
    private javax.swing.JTextField serverPortTextField;
    private javax.swing.JMenu serverTernimateButton;
    private javax.swing.JMenuItem terminateServerButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
