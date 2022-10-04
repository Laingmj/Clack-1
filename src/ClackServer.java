package main;

public class ClackServer {
    //data
    int port;
    boolean closeConnection;
    ClackData dataToReceiveFromClient;
    ClackData dataToSendToClient;

    //constructors
    ClackServer (int port) {
        this.port = port;
        dataToReceiveFromClient = null;
        dataToSendToClient = null;
    }

    ClackServer() {
        this(7000);
    }

    //functions
    public void start() {}

    public void sendData() {}

    public void receiveData() {}

    public int getPort() {
        return port;
    }

    public int hashCode() {
        return this.dataToReceiveFromClient.hashCode() * this.dataToSendToClient.hashCode() * this.port;
    }

    public String toString() {
        return "the port is: " + port + '\n' +
                "the data sent to the client: " + this.dataToSendToClient + '\n' +
                "the data received from the client: " + this.dataToReceiveFromClient;
    }

    public boolean equals(Object other) {
        ClackServer otherServer = (ClackServer) other;
        return (this.port == otherServer.port) && (this.dataToSendToClient == otherServer.dataToSendToClient) &&
                (this.dataToReceiveFromClient == otherServer.dataToReceiveFromClient);
    }
}
