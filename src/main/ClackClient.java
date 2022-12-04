package main;

import data.ClackData;
import data.FileClackData;
import data.MessageClackData;

import java.io.*;
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
 * @author Michael Laing
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
    private ObjectInputStream inFromServer;  // An ObjectInputStream to receive information from the server
    private ObjectOutputStream outToServer;  // An ObjectOutputStream to send information to the server

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
        this.outToServer = null;
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
     * Must catch all relevant exceptions separately and
     * print out messages to standard error for each exception.
     * Workflow:
     * 1. Initializes the scanner and all I/O related resources;
     * 2. While the connection is still open:
     * (a) Reads the client's data using readClientData(),
     * (b) Sends the data out to the server using sendData(),
     * (c) Receives the data from the server using receiveData(),
     * (d) Prints the data using printData();
     * 3. Closes the scanner and all I/O related resources.
     */
    public void start() {
        // See the following API docs for all exceptions caught:
        // https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html#Socket(java.lang.String,%20int)
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html#ObjectInputStream(java.io.InputStream)
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html#ObjectOutputStream(java.io.OutputStream)
        try {
            this.inFromStd = new Scanner(System.in);
            Socket skt = new Socket(this.hostName, this.port);

            // To correctly read and write from the network, the output stream
            // has to be initialized before the input stream.
            this.outToServer = new ObjectOutputStream(skt.getOutputStream());
            this.inFromServer = new ObjectInputStream(skt.getInputStream());
            ClientSideServerListener listener = new ClientSideServerListener(this);
            Thread thread = new Thread(listener);
            thread.start();
            while (!this.closeConnection) {
                readClientData();
                sendData();

                // When DONE, no need to further receive or print the data.
//                if (this.closeConnection) {
//                    break;
//                }

//                receiveData();
//                printData();
            }
            // make sure listener thread is finished before closing socket streams.
            thread.join();
            this.inFromServer.close();
            this.outToServer.close();
            skt.close();
            this.inFromStd.close();

        } catch (UnknownHostException uhe) {
            System.err.println("UnknownHostException thrown in start(): " + uhe.getMessage());

        } catch (StreamCorruptedException sce) {
            System.err.println("StreamCorruptedException thrown in start(): " + sce.getMessage());

        } catch (IOException ioe) {
            System.err.println("IOException thrown in start(): " + ioe.getMessage());

        } catch (SecurityException se) {
            System.err.println("SecurityException thrown in start(): " + se.getMessage());

        } catch (IllegalArgumentException iae) {
            System.err.println("IllegalArgumentException thrown in start(): " + iae.getMessage());
        } catch (InterruptedException ie){
            System.err.println("Listening was Interrupted.");
        }
    }

    /**
     * Gets an input from the user through standard input, represented by this.inFromStd,
     * and then initializes this.dataToSendToServer based on the following input:
     * (a) DONE: Closes the connection,
     * (b) SENDFILE filename: Initializes this.dataToSendToServer as FileClackData
     * and attempts to read the given file,
     * (c) LISTUSERS: Does nothing for now; eventually, will return a list of users,
     * (d) Anything else: Initializes this.dataToSendToServer as MessageClackData,
     * with the given input.
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
            this.dataToSendToServer = new MessageClackData(this.userName, "listusers", DEFAULT_KEY,
                    ClackData.CONSTANT_LISTUSERS);

        } else {
            String message = nextToken + this.inFromStd.nextLine();
            this.dataToSendToServer = new MessageClackData(this.userName, message, DEFAULT_KEY,
                    ClackData.CONSTANT_SENDMESSAGE);
        }
    }

    /**
     * Sends data to server.
     * Does not return anything.
     * Must catch all relevant exceptions separately and
     * print out messages to standard error for each exception.
     */
    public void sendData() {
        // See the following API docs for all exceptions caught:
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html#writeObject(java.lang.Object)
        try {
            this.outToServer.writeObject(this.dataToSendToServer);

        } catch (InvalidClassException ice) {
            System.err.println("InvalidClassException thrown in sendData(): " + ice.getMessage());

        } catch (NotSerializableException nse) {
            System.err.println("NotSerializableException thrown in sendData(): " + nse.getMessage());

        } catch (IOException ioe) {
            System.err.println("IOException thrown in sendData(): " + ioe.getMessage());
        }
    }

    /**
     * Receives data from the server.
     * Does not return anything.
     * Must catch all relevant exceptions separately and
     * print out messages to standard error for each exception.
     */
    public void receiveData() {
        // See the following API docs for all exceptions caught:
        // https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html#readObject()
        try {
            this.dataToReceiveFromServer = (ClackData) this.inFromServer.readObject();

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

    public boolean getCloseConnection(){
        return this.closeConnection;
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

    /**
     * The main method in ClackClient that uses command line arguments to
     * create a new ClackClient object, and starts the ClackClient object.
     * The command line arguments are assumed to be always valid.
     * The command line arguments are handled as follows:
     * (a) No arguments: ClackClient()
     * (b) A single argument:
     * (i) [username]: ClackClient(username)
     * (ii) [username]@[hostname]: ClackClient(username, hostname)
     * (iii) [username]@[hostname]:[portnumber]: ClackClient(username, hostname, portnumber)
     *
     * @param args the parameters to set up ClackClient
     */
    public static void main(String[] args) {
        ClackClient clackClient;

        if (args.length == 0) {
            clackClient = new ClackClient();

        } else {
            Scanner scanner = new Scanner(args[0]);
            scanner.useDelimiter("[@:]");
            String userName = scanner.next();

            if (!scanner.hasNext()) {
                clackClient = new ClackClient(userName);

            } else {
                String hostName = scanner.next();

                if (!scanner.hasNext()) {
                    clackClient = new ClackClient(userName, hostName);

                } else {
                    int port = scanner.nextInt();
                    clackClient = new ClackClient(userName, hostName, port);
                }
            }

            scanner.close();
        }

        clackClient.start();
    }
}
