/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hulululu.drive.hulululudrive;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author jspw
 */
public class Client extends javax.swing.JFrame {

    private Socket socket;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static boolean serverOn = false;
    String validIpPattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    final File[] fileToSend = new File[1];
    DefaultListModel fileListModel;
    ArrayList<String> fileNameList = new ArrayList<String>();
    private JPopupMenu popupOptions;
    private JMenuItem deleteMenuItem;
    private JMenuItem downloadMenuItem;
    int selectedFileToDownload;
    int selectedFileToDelete;
    File[] downloadDirectory = new File[1];

    /**
     * Creates new form Client
     */
    public Client() {
        initComponents();
        fileListModel = new DefaultListModel();
        popupOptions = new JPopupMenu("A");
        downloadMenuItem = new JMenuItem("Download");
        deleteMenuItem = new JMenuItem("Delete");
        popupOptions.add(downloadMenuItem);
        popupOptions.add(deleteMenuItem);

        downloadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {

                    //download file
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose A Folder");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                        JOptionPane.showMessageDialog(Client.this, "Yo Bitch", "Success", JOptionPane.DEFAULT_OPTION);
                        //             Get the selected file.               
                        downloadDirectory[0] = fileChooser.getSelectedFile();
                        //             Change the text of the java swing label to have the file name.      

                        System.out.println(downloadDirectory[0].getName());
                    }
                    //sending file name to get full file
                    out = new ObjectOutputStream(socket.getOutputStream());
                    DataModel data = new DataModel();
                    data.setStatus("new");
                    data.setName("download");
                    out.writeObject(data);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    // Get the name of the file you want to send and store it in filename.
                    String fileName = fileNameList.get(selectedFileToDownload);
                    // Convert the name of the file into an array of bytes to be sent to the server.
                    byte[] fileNameBytes = fileName.getBytes();

                    dataOutputStream.writeInt(fileNameBytes.length);
                    // Send the file name.
                    dataOutputStream.write(fileNameBytes);

                    // get file from server
                    // Stream to receive data from the client through the socket.
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    // Read the size of the file name so know when to stop reading.
                    int fileNameLength = dataInputStream.readInt();
                    // If the file exists
                    if (fileNameLength > 0) {
                        // Byte array to hold name of file.
                        byte[] fileNameBytesx = new byte[fileNameLength];
                        // Read from the input stream into the byte array.
                        dataInputStream.readFully(fileNameBytesx, 0, fileNameBytesx.length);
                        // Create the file name from the byte array.
                        String fileNamex = new String(fileNameBytesx);
                        // Read how much data to expect for the actual content of the file.
                        int fileContentLength = dataInputStream.readInt();
                        // If the file exists.
                        if (fileContentLength > 0) {
                            // Array to hold the file data.
                            byte[] fileContentBytes = new byte[fileContentLength];
                            // Read from the input stream into the fileContentBytes array.
                            dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                            // Panel to hold the picture and file name.

//                                        sendFileName.getText("File Downloaded Successfully");
                            System.out.println("File Downloaded");

                            //save file in storage
                            String storageDir = createStorageDir();
                            File file = new File(downloadDirectory[0].getAbsolutePath() + '/' + fileName);
                            Path path = Paths.get(file.getAbsolutePath());
                            try {
                                Files.write(path, fileContentBytes);
                                sendFileName.setText(fileName + " saved in " + downloadDirectory[0].getAbsolutePath());
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(Client.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    /// delete file

//                    /send type of action
                    out = new ObjectOutputStream(socket.getOutputStream());
                    DataModel data = new DataModel();
                    data.setStatus("new");
                    data.setName("delete");
                    out.writeObject(data);

                    // send file name with length to delete
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    // Get the name of the file you want to send and store it in filename.
                    String fileName = fileNameList.get(selectedFileToDelete);
                    // Convert the name of the file into an array of bytes to be sent to the server.
                    byte[] fileNameBytes = fileName.getBytes();

                    dataOutputStream.writeInt(fileNameBytes.length);
                    // Send the file name.
                    dataOutputStream.write(fileNameBytes);

                    /// file name list from server
                    ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
                    Object object = objectInput.readObject();
                    fileNameList = (ArrayList<String>) object;

                    loadFiles();
                    fileList.repaint();
                    
                    sendFileName.setText("File Deleted Successfully");

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    public static String createStorageDir() {
        String currentDir = Path.of("").toAbsolutePath().toString();
        System.out.println(currentDir);
        String newDir = currentDir + "/local";
        File theDir = new File(newDir);
        if (!theDir.exists()) {
            theDir.mkdirs();
        };
        return newDir;
    }

    void loadFiles() {
        fileListModel.clear();
        for (int i = 0; i < fileNameList.size(); i++) {
            fileListModel.addElement(fileNameList.get(i));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        serverIpLabel = new javax.swing.JLabel();
        serverIpTextField = new javax.swing.JTextField();
        serverPortLabel = new javax.swing.JLabel();
        serverPortTextField = new javax.swing.JTextField();
        connectServerbutton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        title = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        fileUploadProgressbar = new javax.swing.JProgressBar();
        sendFileName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        chooseFileButton = new javax.swing.JButton();
        fileSendButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        disconnectButton = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );

        serverIpLabel.setFont(new java.awt.Font("Amiri", 0, 14)); // NOI18N
        serverIpLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        serverIpLabel.setText("IP Address : ");
        serverIpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        serverIpTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverIpTextFieldActionPerformed(evt);
            }
        });

        serverPortLabel.setFont(new java.awt.Font("Amiri", 0, 14)); // NOI18N
        serverPortLabel.setText("Port : ");

        serverPortTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverPortTextFieldActionPerformed(evt);
            }
        });

        connectServerbutton.setFont(new java.awt.Font("Cantarell Extra Bold", 0, 13)); // NOI18N
        connectServerbutton.setText("Connet");
        connectServerbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectServerbuttonActionPerformed(evt);
            }
        });

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("Please Connect To A Server");

        jLabel1.setFont(new java.awt.Font("Amiri", 0, 13)); // NOI18N
        jLabel1.setText("User : ");

        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(serverIpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(serverPortLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(connectServerbutton))
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectServerbutton)
                    .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverPortLabel)
                    .addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverIpLabel)
                    .addComponent(jLabel1)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        title.setFont(new java.awt.Font("LM Mono Caps 10", 1, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Client");

        sendFileName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sendFileName.setText("Choose a file to send");

        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileListMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(fileList);

        chooseFileButton.setFont(new java.awt.Font("Cantarell Extra Bold", 0, 13)); // NOI18N
        chooseFileButton.setText("Choose File");
        chooseFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFileButtonActionPerformed(evt);
            }
        });

        fileSendButton.setFont(new java.awt.Font("Cantarell Extra Bold", 0, 13)); // NOI18N
        fileSendButton.setText("Send");
        fileSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chooseFileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fileSendButton)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseFileButton)
                    .addComponent(fileSendButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sendFileName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fileUploadProgressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(236, 236, 236))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(fileUploadProgressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("File");

        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });
        jMenu1.add(disconnectButton);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serverIpTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverIpTextFieldActionPerformed
        // TODO add your handling code here:
        //        if (!validIpPattern.matches(serverIpTextField.getText())) {
        //            JOptionPane.showMessageDialog(client.this, "Please Enter A Valid IP Address!", "Error", JOptionPane.ERROR_MESSAGE);
        //            serverIpTextField.setText("");
        //        }
    }//GEN-LAST:event_serverIpTextFieldActionPerformed

    private void serverPortTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverPortTextFieldActionPerformed
        // TODO add your handling code here:
        try {
            int x = Integer.parseInt(serverPortTextField.getText());
        } catch (NumberFormatException nfe) {
            serverPortTextField.setText("");
            JOptionPane.showMessageDialog(Client.this, "A Valid Port Should Contain Numbers Only!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_serverPortTextFieldActionPerformed

    private void connectServerbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectServerbuttonActionPerformed
        // TODO add your handling code here:
        String serverIp = serverIpTextField.getText();
        int port;
        if (serverIp.isEmpty()) {
            JOptionPane.showMessageDialog(Client.this, "Please Enter Server IP Address!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (serverPortTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(Client.this, "Please Enter Port!", "Error", JOptionPane.ERROR_MESSAGE);
        } //        else if (!validIpPattern.matches(serverIp)) {
        //            JOptionPane.showMessageDialog(client.this, "Please Enter A Valid IP Address!", "Error", JOptionPane.ERROR_MESSAGE);
        //        }
        else {
            
            try {
                port = Integer.parseInt(serverPortTextField.getText());
                socket = new Socket(serverIp, port);
                connectServerbutton.setEnabled(false);
                out = new ObjectOutputStream(socket.getOutputStream());
                DataModel data = new DataModel();
                data.setStatus("new");
                data.setName(username.getText());
                out.writeObject(data);
                out.flush();
                statusLabel.setText("Connected To Server!");

                //accept array of file names from server
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
                Object object = objectInput.readObject();
                fileNameList = (ArrayList<String>) object;
                System.out.println(fileNameList);

                loadFiles();
                fileList.setModel(fileListModel);

            } catch (Exception error) {
                JOptionPane.showMessageDialog(Client.this, error, "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, error);
            }

        }

    }//GEN-LAST:event_connectServerbuttonActionPerformed

    private void chooseFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFileButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose A File To Upload");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //            JOptionPane.showMessageDialog(client.this, "Yo Bitch", "Success", JOptionPane.DEFAULT_OPTION);
            //             Get the selected file.
            fileToSend[0] = fileChooser.getSelectedFile();
            //             Change the text of the java swing label to have the file name.
            System.out.println(fileToSend[0].getName());

            sendFileName.setText("The Selected  file to send : " + fileToSend[0].getName());
        }
    }//GEN-LAST:event_chooseFileButtonActionPerformed

    private void fileSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSendButtonActionPerformed
        // TODO add your handling code here:
        if (fileToSend[0] == null) {
            sendFileName.setText("Please choose a file to send first!");
            // If a file has been selected then do the following.
        } else {
            try {

                /// send title upload file
                out = new ObjectOutputStream(socket.getOutputStream());
                DataModel data = new DataModel();
                data.setStatus("new");
                data.setName("upload");
                out.writeObject(data);

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
                sendFileName.setText("File Sent");

                //// file name list from server
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
                Object object = objectInput.readObject();
                fileNameList = (ArrayList<String>) object;

                loadFiles();
                //                fileList.revalidate();
                fileList.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(Client.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(Client.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_fileSendButtonActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameActionPerformed

    private void fileListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileListMouseReleased
        // TODO add your handling code here:
//        popupOptions.show(introLabel, evt.getX() + 50, evt.getY() + 50);
        System.out.println("sdasdas");

        if (SwingUtilities.isRightMouseButton(evt)) {
            System.out.println("isPopupTrigger");
            int index = fileList.locationToIndex(evt.getPoint());
            Rectangle pathBounds = fileList.getCellBounds(index, index);

            if (pathBounds != null && pathBounds.contains(evt.getX(), evt.getY())) {
                System.out.println("pathBounds");
                fileList.setSelectedIndex((fileList.locationToIndex(evt.getPoint())));
                popupOptions.show(fileList, evt.getX(), evt.getY());
                selectedFileToDownload = fileList.locationToIndex(evt.getPoint());
                selectedFileToDelete = fileList.locationToIndex(evt.getPoint());
            }
        }
    }//GEN-LAST:event_fileListMouseReleased

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        // TODO add your handling code here:
        disconnectButton.setEnabled(false);
        connectServerbutton.setEnabled(true);
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(Client.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_disconnectButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseFileButton;
    private javax.swing.JButton connectServerbutton;
    private javax.swing.JMenuItem disconnectButton;
    private javax.swing.JList<String> fileList;
    private javax.swing.JButton fileSendButton;
    private javax.swing.JProgressBar fileUploadProgressbar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel sendFileName;
    private javax.swing.JLabel serverIpLabel;
    private javax.swing.JTextField serverIpTextField;
    private javax.swing.JLabel serverPortLabel;
    private javax.swing.JTextField serverPortTextField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel title;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
