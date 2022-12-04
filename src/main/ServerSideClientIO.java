package main;

import data.ClackData;
import data.MessageClackData;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
/*
*ServerSideClientIO is a class used by ClackServer to manage several clients. Each time a new client connects,
* a ServerSideClientIO is created and added to an ArrayList to manage it.
*
* @author Michael Laing
 */
public class ServerSideClientIO implements Runnable {
    private boolean closeConnection;
    private ClackData dataToReceiveFromClient;
    private ClackData dataToSendToClient;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private ClackServer server;
    private Socket clientSocket;
    private String userName;
    private static final String DEFAULT_KEY = "TIME";

    /**
     * Constructor that takes a ClackServer and a Socket
     * @param server
     * @param clientSocket
     */
    public ServerSideClientIO(ClackServer server, Socket clientSocket){
        this.server=server;
        this.clientSocket = clientSocket;
    }

    /**
     * run override that receives data from the client.
     * If the client chooses listusers, it will list the users to the calling client only,
     * Otherwise, the data will be broadcast to all clients connected to the server.
     */
    public void run(){
        try {
            //ServerSocket sskt = new ServerSocket(server.getPort());
            this.inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            this.outToClient = new ObjectOutputStream(clientSocket.getOutputStream());

            while (!this.closeConnection) {
                receiveData();
                if (this.dataToReceiveFromClient.getType()==ClackData.CONSTANT_LISTUSERS){
                    sendListUsers();
                } else {
                    server.broadcast(dataToReceiveFromClient);
                }
            }

            //sskt.close();
            clientSocket.close();
            inFromClient.close();
            outToClient.close();
        } catch (IOException ioe) {
            System.err.println("io exception occured");
        }
    }

    /**
     * receives data from the client and sets dataToReceiveFromClient
     */
    public void receiveData(){
        try {
            this.dataToReceiveFromClient = (ClackData) this.inFromClient.readObject();
            this.userName=this.dataToReceiveFromClient.getUserName();

            // Determines if the connection is to be closed.
            if (this.dataToReceiveFromClient.getType() == ClackData.CONSTANT_LOGOUT) {
                this.closeConnection = true;
                this.server.remove(this);
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException thrown in receiveData(): " + cnfe.getMessage());

        } catch (InvalidClassException ice) {
            System.err.println("InvalidClassException thrown in receiveData(): " + ice.getMessage());

        } catch (StreamCorruptedException sce) {
            System.err.println("StreamCorruptedException thrown in receiveData(): " + sce.getMessage());

        } catch (OptionalDataException ode) {
            System.err.println("OptionalDataException thrown in receiveData(): " + ode.getMessage());

        } catch (IOException ioe) {
            System.err.println("IOException thrown in receiveData(): " + ioe.getMessage());
        }
    }

    /**
     * sends data to the client.
     */
    public void sendData(){
        try {
            this.outToClient.writeObject(this.dataToSendToClient);

        } catch (InvalidClassException ice) {
            System.err.println("InvalidClassException thrown in sendData(): " + ice.getMessage());

        } catch (NotSerializableException nse) {
            System.err.println("NotSerializableException thrown in sendData(): " + nse.getMessage());

        } catch (IOException ioe) {
            System.err.println("IOException thrown in sendData(): " + ioe.getMessage());
        }
    }

    /**
     * lists all non-anonymous users connected to the server to the client.
     */
    public void sendListUsers(){
        ArrayList<String> userList = server.getUserList();
        String message = "";
        Iterator it = userList.iterator();

        while(it.hasNext()){
            message = message + it.next() + "\n";
        }
        setDataToSendToClient(new MessageClackData(getUserName(), message, DEFAULT_KEY, ClackData.CONSTANT_SENDMESSAGE));
        sendData();
    }

    /**
     * simple mutator for dataToSendToClient
     * @param dataToSendToClient
     */
    public void setDataToSendToClient(ClackData dataToSendToClient){
        this.dataToSendToClient=dataToSendToClient;
    }

    /**
     * simple getter for userName
     * @return
     */
    public String getUserName(){
        return this.userName;
    }
}
