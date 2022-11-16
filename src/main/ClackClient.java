package main;

import data.ClackData;
import data.FileClackData;
import data.MessageClackData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The ClackClient class represents the client user. A ClackClient object contains the username,
 * host name of the server connected to, port number connected to, and a boolean designating
 * whether the connection is open or not. The ClackClient object will also have two ClackData
 * objects representing data sent to the server and data received from the server.
 *
 * @author Ngaio Hawkins & Michael Laing
 */
public class ClackClient {
    private static final int DEFAULT_PORT = 7000;  // The default port number
    private static final String DEFAULT_KEY = "TIME";  // The default key for encryption and decryption
    private String userName;  // A string representing the name of the client
    private String hostName;  // A string representing the name of the computer representing the server
    private int port;  // An integer representing the port number on the server connected to
    private boolean closeConnection;  // A boolean representing whether the connection is closed or not
    private ClackData dataToSendToServer;  // A ClackData object representing the data sent to the server
    private ClackData dataToReceiveFromServer;  // A ClackData object representing the data received from the server
    private Scanner inFromStd;  // A Scanner object representing the standard input
    private ObjectInputStream inFromServer;
    private ObjectOutputStream outToServer;

    /**
     * The constructor to set up the username, host name, and port.
     * The connection should be set to be open (closeConnection = false).
     * Should set dataToSendToServer and dataToReceiveFromServer as null.
     *
     * @param userName a string representing the username of the client
     * @param hostName a string representing the host name of the server
     * @param port     an int representing the port number on the server connected to
     */
    public ClackClient(String userName, String hostName, int port) throws IllegalArgumentException {
        if (userName == null) {
            throw new IllegalArgumentException("The username cannot be null.");
        }
        if (hostName == null) {
            throw new IllegalArgumentException("The host name cannot be null.");
        }
        if (port < 1024) {
            throw new IllegalArgumentException("The port cannot be lesser than 1024.");
        }

        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
        this.closeConnection = false;
        this.dataToSendToServer = null;
        this.dataToReceiveFromServer = null;
        this.inFromServer = null;
        this. outToServer = null;
    }

    /**
     * The constructor to set up the port to the default port number 7000.
     * The default port number should be set up as constant (e.g., DEFAULT_PORT).
     * This constructor should call another constructor.
     *
     * @param userName a string representing the username of the client
     * @param hostName a string representing the host name of the server
     */
    public ClackClient(String userName, String hostName) throws IllegalArgumentException {
        this(userName, hostName, DEFAULT_PORT);
    }

    /**
     * The constructor that sets the host name to be "localhost"
     * (i.e., the server and the client programs run on the same computer).
     * This constructor should call another constructor.
     *
     * @param userName a string representing the username of the client
     */
    public ClackClient(String userName) throws IllegalArgumentException {
        this(userName, "localhost");
    }

    /**
     * The default constructor that sets anonymous user.
     * Should call another constructor.
     */
    public ClackClient() throws IllegalArgumentException {
        this("Anon");
    }

    /**
     * Starts the client's communication with the server.
     * Does not return anything.
     * Workflow:
     * 1. Initializes this.inFromStd to be a Scanner object that can be used in readClientData()
     * 2. While the connection is still running:
     *    (a) Reads the client's data using readClientData()
     *    (b) Sets this.dataToReceiveFromServer to be the same as this.dataToSendToServer
     *    (c) Prints the data using printData()
     * 3. Closes this.inFromStd.
     */
    public void start() throws IOException, ClassNotFoundException {
            this.inFromStd = new Scanner(System.in);
            Socket s = new Socket(this.getHostName(), this.getPort());
            this.outToServer = new ObjectOutputStream(s.getOutputStream());
            this.inFromServer = new ObjectInputStream(s.getInputStream());

            while (!this.closeConnection) {
                readClientData();
                sendData();
                receiveData();
                printData();
            }

            this.outToServer.close();
            this.inFromServer.close();
            this.inFromStd.close();
    }

    /**
     * Gets an input from the user through standard input, represented by this.inFromStd,
     * and then initializes this.dataToSendToServer based on the following input:
     * (a) DONE:              Closes the connection
     * (b) SENDFILE filename: Initializes this.dataToSendToServer as FileClackData
     *                        and attempts to read the given file
     * (c) LISTUSERS:         Does nothing for now; eventually, will return a list of users
     * (d) Anything else:     Initializes this.dataToSendToServer as MessageClackData
     *                        with the given input
     * this.dataToSendToServer should be encrypted using the default key.
     * Does not return anything.
     */
    public void readClientData() {
        String nextToken = this.inFromStd.next();

        if (nextToken.equals("DONE")) {
            this.closeConnection = true;
            this.dataToSendToServer = new MessageClackData(this.userName, nextToken, DEFAULT_KEY,
                    ClackData.CONSTANT_LOGOUT);

        } else if (nextToken.equals("SENDFILE")) {
            String filename = this.inFromStd.next();
            this.dataToSendToServer = new FileClackData(this.userName, filename, ClackData.CONSTANT_SENDFILE);

            try {
                ((FileClackData) this.dataToSendToServer).readFileContents(DEFAULT_KEY);
            } catch (IOException ioe) {
                System.err.println("IOException occurs when reading a file: " + ioe.getMessage());
                this.dataToSendToServer = null;
            }

        } else if (nextToken.equals("LISTUSERS")) {
            // Does nothing for now. Eventually, this will return a list of users.
            // For Part 2, do not test LISTUSERS; otherwise, it may generate an error.

        } else {
            String message = nextToken + this.inFromStd.nextLine();
            this.dataToSendToServer = new MessageClackData(this.userName, message, DEFAULT_KEY,
                    ClackData.CONSTANT_SENDMESSAGE);
        }
    }

    /**
     * Sends data to server.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void sendData() throws IOException {
        outToServer.writeObject(this.dataToSendToServer);
    }

    /**
     * Receives data from the server.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void receiveData() throws IOException, ClassNotFoundException {
        this.dataToReceiveFromServer = (ClackData) this.inFromServer.readObject();
    }

    /**
     * Prints out the received data (the contents in the this.dataToReceiveFromServer)
     * to the client's standard output. this.dataToReceiveFromServer should be decrypted
     * using the default key.
     * Should output something meaningful to the client seeing the data.
     */
    public void printData() {
        if (this.dataToReceiveFromServer != null) {
            System.out.println("From: " + this.dataToReceiveFromServer.getUserName());
            System.out.println("Date: " + this.dataToReceiveFromServer.getDate());
            System.out.println("Data: " + this.dataToReceiveFromServer.getData(DEFAULT_KEY));
            System.out.println();
        }
    }

    /**
     * Returns the username.
     *
     * @return this.userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the host name.
     *
     * @return this.hostName
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * Returns the port.
     *
     * @return this.port
     */
    public int getPort() {
        return this.port;
    }

    @Override
    public int hashCode() {
        // The following is only one of many possible implementations to generate the hash code.
        // See the hashCode() method in other classes for some different implementations.

        int result = 23;

        // It is okay to select only some of the instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        result = 31 * result + Objects.hashCode(this.userName);
        result = 31 * result + Objects.hashCode(this.hostName);
        result = 31 * result + this.port;
        result = 31 * result + Objects.hashCode(this.closeConnection);
        result = 31 * result + Objects.hashCode(this.dataToSendToServer);
        result = 31 * result + Objects.hashCode(this.dataToReceiveFromServer);

        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClackClient)) {
            return false;
        }

        // Casts other to be a ClackClient to access its instance variables.
        ClackClient otherClackClient = (ClackClient) other;

        // Compares all comparable instance variables of both ClackClient objects that determine uniqueness.
        // It is okay to select only some of the instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.userName.equals(otherClackClient.userName) &&
                this.hostName.equals(otherClackClient.hostName) &&
                this.port == otherClackClient.port &&
                this.closeConnection == otherClackClient.closeConnection &&
                Objects.equals(this.dataToSendToServer, otherClackClient.dataToSendToServer) &&
                Objects.equals(this.dataToReceiveFromServer, otherClackClient.dataToReceiveFromServer);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables.
        return "This instance of ClackClient has the following properties:\n"
                + "Username: " + this.userName + "\n"
                + "Host name: " + this.hostName + "\n"
                + "Port number: " + this.port + "\n"
                + "Connection status: " + (this.closeConnection ? "Closed" : "Open") + "\n"
                + "Data to send to the server: " + this.dataToSendToServer + "\n"
                + "Data to receive from the server: " + this.dataToReceiveFromServer + "\n";
    }
}
