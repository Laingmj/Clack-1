package data;

import java.util.Date;

/**
 * Class ClackData is a superclass that represents the data sent between the client and the
 * server. An object of type ClackData consists of the username of the client user, the date
 * and time at which the data was sent and the data itself, which can either be a message
 * (MessageClackData) or the name and contents of a file (FileClackData). Note that ClackData
 * should not be instantiable.
 *
 * @author Michael Laing
 */
public abstract class ClackData {
    /**
     * For giving a listing of all users connected to this session.
     */
    public static final int CONSTANT_LISTUSERS = 0;

    /**
     * For logging out, i.e., close this client's connection.
     */
    public static final int CONSTANT_LOGOUT = 1;

    /**
     * For sending a message.
     */
    public static final int CONSTANT_SENDMESSAGE = 2;

    /**
     * For sending a file.
     */
    public static final int CONSTANT_SENDFILE = 3;

    /**
     * A string representing the name of the client user.
     */
    protected String userName;

    /**
     * An integer representing the kind of data exchanged between the client and the server.
     */
    protected int type;

    /**
     * A Date object representing the date and time when ClackData object is created.
     */
    protected Date date;

    /**
     * The constructor to set up the instance variable username and type.
     * The instance variable date should be created automatically here.
     *
     * @param userName a string representing the name of the client user
     * @param type     an int representing the data type
     */
    public ClackData(String userName, int type) {
        this.userName = userName;
        this.type = type;
        this.date = new Date();
    }

    /**
     * The constructor to create an anonymous user, whose name should be "Anon".
     * This constructor should call another constructor.
     *
     * @param type an int representing the data type
     */
    public ClackData(int type) {
        this("Anon", type);
    }

    /**
     * The default constructor.
     * This constructor should call another constructor.
     * type should get defaulted to CONSTANT_LOGOUT.
     */
    public ClackData() {
        this(CONSTANT_LOGOUT);
    }

    /**
     * Returns the type.
     *
     * @return this.type
     */
    public int getType() {
        return this.type;
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
     * Returns the date.
     *
     * @return this.date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * The abstract method to return the data contained in this class
     * (contents of instant message or contents of a file).
     *
     * @return data
     */
    public abstract String getData();

    public abstract String getData(String key);

    /**
     * Encrypts a string using a key using a Vigener shift.
     * @param input
     * @param key
     * @return an encrypted string
     */
    protected String encrypt( String input, String key ){

        //convert input to char array
        char cinput[] = input.toCharArray();
        int ilength = cinput.length;
        String upperKey = key.toUpperCase();
        //make new char arrays
        int shift[] = new int[ilength];
        char eInput[] = new char[ilength];

        //ints for for loop
        int i,j;

        //Building trueKey from inputted key
        for(i = 0,j = 0; i < ilength; i++, j++)
        {
            if(j == key.length())
            {
                j = 0;
            }
            shift[i] = upperKey.charAt(j)-'A';
        }

        //encryption
        for(i = 0; i < ilength; ++i) {
            if(cinput[i]>='A'&&cinput[i]<='Z'){
                eInput[i]=(char)(cinput[i] + shift[i]);
                if(eInput[i]>'Z'){
                    eInput[i]-=26;
                }
            }else if(cinput[i]>='a'&&cinput[i]<='z') {
                eInput[i] = (char) (cinput[i] + shift[i]);
                if (eInput[i] > 'z') {
                    eInput[i] -= 26;
                }
            }else{
                eInput[i]= cinput[i];
            }
        }

        //restringify
        String finput = new String(eInput);

        return finput;
    }

    /**
     * Takes a encrypted string and decrypts it.
     * @param input
     * @param key
     * @return a decrypted string
     */
    protected String decrypt( String input, String key ){

        //convert to char array
        char cinput[] =input.toCharArray();
        int ilength = cinput.length;
        String upperKey = key.toUpperCase();

        //new char arrays
        int shift[] = new int[ilength];
        char dinput[] = new char[ilength];

        //ints for loop
        int i,j;

        //Creates trueKey based on the inputted key.
        for(i = 0,j = 0; i < ilength; i++, j++)
        {
            if(j == key.length())
            {
                j = 0;
            }
            shift[i] = upperKey.charAt(j)-'A';
        }

        //decryption
        for(i = 0; i < ilength; ++i) {
            if (cinput[i] >= 'A' && cinput[i] <= 'Z') {
                dinput[i] = (char) (cinput[i] - shift[i]);
                if (dinput[i] < 'A') {
                    dinput[i] += 26;
                }
            } else if (cinput[i] >= 'a' && cinput[i] <= 'z') {
                dinput[i] = (char) (cinput[i] - shift[i]);
                if (dinput[i] < 'a') {
                    dinput[i] += 26;
                }
            } else {
                dinput[i] = cinput[i];
            }
        }

        //restringify
        String finput = new String(dinput);

        return finput;
    }
}
