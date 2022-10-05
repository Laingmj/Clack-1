package main;

public class ClackClient {
    //data
    String userName; //String representing name of the client
    String hostName; //String representing name of the computer representing the server
    int port; //integer representing port number on server connected to
    boolean closeConnection; //boolean representing whether connection is closed or not
    data.ClackData dataToSendToServer; //ClackData object representing data sent to server
    data.ClackData dataToReceiveFromServer; //ClackData object representing data received from the server

    //constructors
    public ClackClient(String userName, String hostName, int port) {
        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
        closeConnection = true;
        dataToSendToServer = null;
        dataToReceiveFromServer = null;
    }

    public ClackClient(String userName, String hostName) {
        this(userName, hostName, 7000);
    }

    public ClackClient(String userName) {
        this(userName, "localhost");
    }

    public ClackClient() {
        this("anonymous");
    }

    //functions
    public void start() {}

    public void readClientData() {}

    public void sendData() {}

    public void receiveData() {}

    public void printData() {}

    public String getUserName() {
        return userName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public int hashCode() {
        return this.userName.hashCode() * this.hostName.hashCode() * this.port * this.dataToSendToServer.hashCode() *
                this.dataToReceiveFromServer.hashCode();
    }

    public String toString() {
        return "the user name is: " + this.userName + '\n' +
                "the host name is: " + this.hostName + '\n' +
                "the port is: " + this.port + '\n' +
                "the connection: " + this.closeConnection + '\n' +
                "the data sent to the server: " + this.dataToSendToServer + '\n' +
                "the data received from the server: " + this.dataToReceiveFromServer;
    }

    public boolean equals(Object other) {
        ClackClient otherClient = (ClackClient) other;
        return (this.userName.equals(otherClient.userName)) && (this.hostName.equals(otherClient.hostName)) &&
                (this.port == otherClient.port) && (this.closeConnection == otherClient.closeConnection) &&
                (this.dataToSendToServer == otherClient.dataToSendToServer) &&
                (this.dataToReceiveFromServer == otherClient.dataToReceiveFromServer);
    }
}