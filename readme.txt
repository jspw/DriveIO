Project Name:   File Server System

Created by : 

                Mohammad Akhlaqur Rahman
                Reg No: 2017831011
                Department of Software Engineering, SUST


                Mehedi Hasan Shifat
                Reg No: 2017831017
                Department of Software Engineering, SUST


Submitted to
    
                Dr. Farida Chowdhury    
                Associate Professor
                Department of Computer Science, SUST


Description : 

                This is a sever-client file management system for storing and sharing files between clients. 
The system is divided into two parts : 
-   the client module and 
-   the server module. 
The server is where all the files of the system are stored and the client is a user who connects to the server.

How To Use :

                First of all, the server needs to be started. The server has an ip address and a port number which will be displayed on the server user interface.For the clients,In order  to connect to the server, they will need to input this ip address and port no. 
After starting the server, we can see the files that are present in the server’s directory. We can then upload new files to the server and also delete existing files from the server. The clients use the ip address and port number of the server to connect with it. After connecting to the server, a client can view the files that are already present on the server as well as upload new files to the server. If the client right clicks on an existing file in the server, he will get a popup menu with the option to download and delete the file. When downloading a file, the client will be prompted to select a location where the file will be downloaded.

How System Works (Technically) : 

                    We are using socket to create server at the server on a port. Then The client side connect to the server using the port and ip address.In server side there is a default directory where all files are stored.
                    
How File Upload works : 
                    
                    When server loads the files of directory are loaded with a File Manager.When client send files first of all it sends the length of name of the file in byes then it send the file name in byes.The client then send the file content. On the other hand the server receive the file name in bytes and then it take a space of that length from the server file system. Then the server accept the file name byte and file contents. After that server write a the file in the system. Thus a file is uploaded in the server.

How File Download Works : 

                    When client select a file from the server for download, it mainly select a name of the file .Then client send the file name and it's length as byte to the server and server receives it. Then the server find the file in the storage and send the file name ,file name length , file content  as byte to the client and the client receives it.

How File Delete Works : 

                    When client select a a file to delete it send the name of the file and the server find the file in storage. Then the server delete the file from server storage. 











