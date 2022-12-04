package main;

import data.ClackData;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * The ClackServer class is a blueprint for a ClackServer object that contains information about the
 * port number that clients connect to, a boolean representing whether the server needs to be
 * closed or not, and ClackData objects representing data sent to and received from the client. The
 * server class does not need to know the host name (as the server program runs on its own computer),
 * it just needs to know what port the clients connect to. In our application, all clients will connect
 * to a single port.
 *
 * @author Michael Laing
 */
public class ClackServer {
    private static final int DEFAULT_PORT = 7000;  // The default port number
    private static final String DEFAULT_KEY = "TIME";  // The default key for encryption and decryption
    private int port;  // An integer representing the port number on the server connected to
    private boolean closeConnection;  // A boolean representing whether the connection is closed or not
    private ArrayList<ServerSideClientIO> serverSideClientIOList;
    private ClackData dataToSendToClient;
    private ClackData dataToReceiveFromClient;


    /**
     * The constructor that sets the port number.
     * Should set dataToReceiveFromClient and dataToSendToClient as null.
     *
     * @param port an int representing the port number on the server connected to
     */
    public ClackServer(int port) {
        if (port < 1024) {
            throw new IllegalArgumentException("The port cannot be lesser than 1024.");
        }

        this.serverSideClientIOList = new ArrayList<ServerSideClientIO>();
        this.port = port;
        this.closeConnection = false;
        this.dataToReceiveFromClient = null;
        this.dataToSendToClient = null;
    }

    /**
     * The default constructor that sets the port to the default port number 7000.
     * The default port number should be set up as constant (e.g., DEFAULT_PORT).
     * This constructor should call another constructor.
     */
    public ClackServer() {
        this(DEFAULT_PORT);
        this.serverSideClientIOList = new ArrayList<ServerSideClientIO>();
    }

    /**
     * Starts the server to get connections from the client and echo the client's data.
     * Does not return anything.
     * Must catch all relevant exceptions separately and
     * print out messages to standard error for each exception.
     * Workflow:
     * 1. Initializes the socket to accept incoming socket connections;
     * 2. Initializes this.inFromClient and this.outToClient;
     * 3. While the connection is still open:
     * (a) Receives the data from the client using receiveData(),
     * (b) Assigns this.dataToSendToClient to be the same as this.dataToReceiveFromClient,
     * (c) Sends the data back to the client using sendData();
     * 4. Closes all I/O related resources.
     */
    public void start() {
        // See the following API docs for all exceptions caught:
        // https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html#ServerSocket(int)
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html#ObjectInputStream(java.io.InputStream)
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html#ObjectOutputStream(java.io.OutputStream)
        try {
            ServerSocket sskt = new ServerSocket(this.port);
            System.out.println("Server is running on port " + this.port);  // For debugging purposes
            while(!this.closeConnection) {
                Socket clientSocket = sskt.accept();
                System.out.println("Client has been connected.");  // For debugging purposes
                ServerSideClientIO SSCIO = new ServerSideClientIO(this, clientSocket);
                this.serverSideClientIOList.add(SSCIO);
                Thread thread = new Thread(SSCIO);
                thread.start();
            }
            sskt.close();

        } catch (StreamCorruptedException sce) {
            System.err.println("StreamCorruptedException thrown in start(): " + sce.getMessage());

        }  catch (IOException ioe) {
            System.err.println("IOException thrown in start(): " + ioe.getMessage());

        } catch (SecurityException se) {
            System.err.println("SecurityException thrown in start(): " + se.getMessage());

        } catch (IllegalArgumentException iae) {
            System.err.println("IllegalArgumentException thrown in start(): " + iae.getMessage());
        }
    }

    /**
     * sends data to all clients connected to the server.
     * @param dataToBroadcastToClients
     */
    public synchronized void broadcast(ClackData dataToBroadcastToClients){
        for(ServerSideClientIO SSCIO : this.serverSideClientIOList){
            SSCIO.setDataToSendToClient(dataToBroadcastToClients);
            SSCIO.sendData();
        }
    }

    /**
     * removes a clientIO from the arraylist.
     * @param SSCIO
     */
    public synchronized void remove(ServerSideClientIO SSCIO){
        this.serverSideClientIOList.remove(SSCIO);
    }

    /**
     * Returns the port.
     *
     * @return this.port.
     */
    public int getPort() {
        return this.port;
    }

    public synchronized ArrayList<String> getUserList(){
        ArrayList<String> userList = new ArrayList<String>();
        for(ServerSideClientIO SSCIO : serverSideClientIOList){
            userList.add(SSCIO.getUserName());
        }
        return userList;
    }

    @Override
    public int hashCode() {
        // The following is only one of many possible implementations to generate the hash code.
        // See the hashCode() method in other classes for some different implementations.
        // It is okay to select only some of the instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        return Objects.hash(this.port, this.closeConnection, this.dataToReceiveFromClient, this.dataToSendToClient);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClackServer)) {
            return false;
        }

        // Casts other to be a ClackServer to access its instance variables.
        ClackServer otherClackServer = (ClackServer) other;

        // Compares the selected instance variables of both ClackServer objects that determine uniqueness.
        // It is okay to select only some of the instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.port == otherClackServer.port
                && this.closeConnection == otherClackServer.closeConnection
                && Objects.equals(this.dataToReceiveFromClient, otherClackServer.dataToReceiveFromClient)
                && Objects.equals(this.dataToSendToClient, otherClackServer.dataToSendToClient);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables.
        return "This instance of ClackServer has the following properties:\n"
                + "Port number: " + this.port + "\n"
                + "Connection status: " + (this.closeConnection ? "Closed" : "Open") + "\n"
                + "Data to receive from the client: " + this.dataToReceiveFromClient + "\n"
                + "Data to send to the client: " + this.dataToSendToClient + "\n";
    }

    /**
     * The main method in ClackServer that uses command line arguments to
     * create a new ClackServer object, and starts the ClackServer object.
     * The command line arguments are assumed to be always valid.
     * The command line arguments are handled as follows:
     * (a) No arguments: ClackServer()
     * (b) A single argument [portnumber]: ClackServer(portnumber)
     *
     * @param args the parameters to set up ClackClient
     */
    public static void main(String[] args) {
        ClackServer clackServer;

        if (args.length == 0) {
            clackServer = new ClackServer();

        } else {
            int port = Integer.parseInt(args[0]);
            clackServer = new ClackServer(port);
        }

        clackServer.start();
    }
}
