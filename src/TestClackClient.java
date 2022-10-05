public class TestClackClient {
    public static void main(String[] args) {
        //constructor and accessor tests
        main.ClackClient test1 = new main.ClackClient("testuser", "testhost", 100);
        System.out.println("user name is: " + test1.getUserName());
        System.out.println("host name is: " + test1.getHostName());
        System.out.println("port is: " + test1.getPort());

        main.ClackClient test2 = new main.ClackClient("testuser", "testhost");
        System.out.println(test2);

        main.ClackClient test3 = new main.ClackClient("testuser");
        System.out.println(test3);

        main.ClackClient test4 = new main.ClackClient();
        System.out.println(test4);

        //hashCode and equals tests
        main.ClackClient test5 = test1;
        if (test.equals(test2)) {
            System.out.println("The two objects are equal" + '\n' +
                               "Hashcode of test1: " + test1.hashCode() + '\n' +
                               "Hashcode of test2: " + test5.hashCode());
        }
        else {
            System.out.println("The two objects are not equal" + '\n' +
                    "Hashcode of test1: " + test1.hashCode() + '\n' +
                    "Hashcode of test2: " + test5.hashCode());
        }

        //toString test
        System.out.println(test1);
    }
}
