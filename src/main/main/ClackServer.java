package main;

import data.ClackData;
import java.util.Objects;
import java.io.*;
import java.net.*;

import static java.lang.Integer.parseInt;


/**
 * The ClackServer class is a blueprint for a ClackServer object that contains information about the
 * port number that clients connect to, a boolean representing whether the server needs to be
 * closed or not, and ClackData objects representing data sent to and received from the client. The
 * server class does not need to know the host name (as the server program runs on its own computer),
 * it just needs to know what port the clients connect to. In our application, all clients will connect
 * to a single port.
 *
 * @author Ngaio Hawkins
 */
public class ClackServer {
    private static final int DEFAULT_PORT = 7000;  // The default port number
    private int port;  // An integer representing the port number on the server connected to
    private boolean closeConnection;  // A boolean representing whether the connection is closed or not
    private ClackData dataToReceiveFromClient;  // A ClackData object representing the data received from the client
    private ClackData dataToSendToClient;  // A ClackData object representing the data sent to client
    private ObjectInputStream inFromClient; // ObjectInputStream to receive information from client
    private ObjectOutputStream outToClient; // ObjectOutputStream to send information to client

    /**
     * The constructor that sets the port number.
     * Should set dataToReceiveFromClient and dataToSendToClient as null.
     *
     * @param port an int representing the port number on the server connected to
     */
    public ClackServer(int port) throws IllegalArgumentException {
        this.port = port;
        this.closeConnection = false;
        this.dataToReceiveFromClient = null;
        this.dataToSendToClient = null;
        this.inFromClient = null;
        this.outToClient = null;
    }

    /**
     * The default constructor that sets the port to the default port number 7000.
     * The default port number should be set up as constant (e.g., DEFAULT_PORT).
     * This constructor should call another constructor.
     */
    public ClackServer() {
        this(DEFAULT_PORT);
    }

    /**
     * Starts the server.
     * Does not return anything.
     * Gets connections from the client and echoes client data.
     */
    public void start() {
        try {
            ServerSocket sskt = new ServerSocket(DEFAULT_PORT);
            Socket clientSkt = sskt.accept();
            this.inFromClient = new ObjectInputStream(clientSkt.getInputStream());
            this.outToClient = new ObjectOutputStream(clientSkt.getOutputStream());

            while (!this.closeConnection) {
               receiveData();
                this.dataToSendToClient = this.dataToReceiveFromClient;
               sendData();
            }

            sskt.close();
            clientSkt.close();
            inFromClient.close();
            outToClient.close();
        } catch (IOException ioe) {
            System.err.println("io exception occured");
        }

    }

    /**
     * Receives data from client.
     * Does not return anything.
     */
    public void receiveData() {
        ClackData line;
       // while (!(this.closeConnection)) {
            try {
                line = (ClackData)this.inFromClient.readObject();
                this.dataToReceiveFromClient = line;
            } catch (IOException ioe) {
                System.err.println("io exception occured");
            } catch (ClassNotFoundException cnfe) {
                System.err.println("class not found exception occured");
            }
       // }
    }

    /**
     * Sends data to client.
     * Does not return anything.
     */
    public void sendData() {
      //  while (!(this.closeConnection)) {
            try {
                outToClient.writeObject(dataToSendToClient);
                outToClient.flush();
            } catch (IOException ioe) {
                System.err.println("io exception occured");
            }
     //   }
    }

    /**
     * Returns the port.
     *
     * @return this.port.
     */
    public int getPort() {
        return this.port;
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

    public static void main(String[] args) {
        ClackServer server=null;
        if(args.length==0){
            server = new ClackServer();
        } else if(args.length==1){
            int port = parseInt(args[0]);
        } else {
            System.out.println("Error: too many arguments");
            System.exit(1);
        }
        server.start();
    }
}
