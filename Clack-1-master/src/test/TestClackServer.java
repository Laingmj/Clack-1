package test;

import main.ClackServer;

/**
 * You don't have to have a Javadoc for a test class.
 * You should test all implemented functions in the data classes,
 * whether they are in the superclass or in the subclasses.
 */
public class TestClackServer {
    public static void main(String[] args) {
        // Tests all three different constructors of ClackServer.
        ClackServer clackServer1 = new ClackServer(12345);
        ClackServer clackServer2 = new ClackServer(7000);
        ClackServer clackServer3 = new ClackServer();
        ClackServer clackServer4 = new ClackServer(-99);

        // Tests int getPort().
        System.out.println("clackServer1 getPort(): " + clackServer1.getPort());
        System.out.println("clackServer2 getPort(): " + clackServer2.getPort());
        System.out.println("clackServer3 getPort(): " + clackServer3.getPort());
        System.out.println("clackServer4 getPort(): " + clackServer4.getPort());
        System.out.println();

        // Tests int hashCode().
        System.out.println("clackServer1 hashCode(): " + clackServer1.hashCode());
        System.out.println("clackServer2 hashCode(): " + clackServer2.hashCode());
        System.out.println("clackServer3 hashCode(): " + clackServer3.hashCode());
        System.out.println("clackServer4 hashCode(): " + clackServer4.hashCode());
        System.out.println();

        // Tests boolean equals(Object other).
        System.out.println("clackServer1 equals null: " + clackServer1.equals(null));
        System.out.println("clackServer1 equals clackServer1: " + clackServer1.equals(clackServer1));
        System.out.println("clackServer1 equals clackServer2: " + clackServer1.equals(clackServer2));
        System.out.println("clackServer2 equals clackServer1: " + clackServer2.equals(clackServer1));
        System.out.println("clackServer1 equals clackServer3: " + clackServer1.equals(clackServer3));
        System.out.println("clackServer3 equals clackServer1: " + clackServer3.equals(clackServer1));
        System.out.println("clackServer1 equals clackServer4: " + clackServer1.equals(clackServer4));
        System.out.println();

        // Tests String toString().
        System.out.println("clackServer1 toString():\n" + clackServer1);
        System.out.println("clackServer2 toString():\n" + clackServer2);
        System.out.println("clackServer3 toString():\n" + clackServer3);
        System.out.println("clackServer4 toString():\n" + clackServer4);
    }
}
