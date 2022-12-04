package main;

/**
*ClientSideServerListener is a class threaded by the ClackClient to receive and print data from the server.
* @author Michael Laing
 */
public class ClientSideServerListener implements Runnable {
    private ClackClient client;

    /**
     * Constructor that takes a ClackClient,
     * @param client
     */
    public ClientSideServerListener(ClackClient client){
        this.client=client;
    }

    /**
     * run override that runs a while loop receiving and printing data from the server
     * until the connection is closed.
     */
    public void run(){
        while(!client.getCloseConnection()){
            client.receiveData();
            client.printData();
        }
    }
}
