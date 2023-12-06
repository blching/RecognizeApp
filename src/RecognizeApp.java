import java.sql.*;
import java.util.Scanner;
import java.util.Properties;

public class RecognizeApp {
    public static Connection conn = null;
    public static ResultSet rs = null;
    public static Statement stmt = null;
    static Scanner scan;


    static void intitalize() {
        /*
            java -cp .:/Users/brandonching/Desktop/School/CS157a/mysql-connector-j-8.2.0.jar RecognizeApp
        */
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        System.out.println("Welcome to Honor World!");
        scan = new Scanner(System.in);
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/test?" +
                "user=root&password=root");
            stmt = conn.createStatement();

             System.out.println("Here are our five recent honor recipents:");

            rs = stmt.executeQuery("SELECT * FROM RECENTRECIPIENTS");

            int i = 0;
            while (rs.next()) {
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");

                System.out.println("(" + Character.toUpperCase(alphabet[i]) + ") " + fName + " " + lName);
                i++;
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    static void honor() {
        System.out.println("--------------");

        System.out.println("(Enter -Q to return to main menu)");
        System.out.println("Please enter the first name of who you want to Honor");
        String fName = scan.nextLine();

        if (fName.equals("-q") || fName.equals("-Q")) {
            mainStep();
        } else {
            System.out.println("Please enter the last name of who you want to Honor");
        }
        String lName = scan.nextLine();
        
        //System.out.println("SELECT honorLevel FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
        try {
            int honorLvl = 0;
            while (rs.next()) {
                rs = stmt.executeQuery("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
                honorLvl = rs.getInt("honorLevel");
                honorLvl++;
                System.out.println("New Honor Level: " + honorLvl);
                stmt.executeUpdate("UPDATE RECIPIENTS SET honorLevel = '" + honorLvl + "' WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());        
        }

        mainStep();

    }

    static void gift() {

    }

    static void checkHonor() {

    }

    static void checkGift() {

    }

    static void mainStep() {
        System.out.println("--------------");

        Scanner scan = new Scanner(System.in);
        System.out.println("What do you want to do?");

        //Display All users
        System.out.println("(0) Display Recent Recipients");
        System.out.println("(1) Display all Recipients");
        System.out.println("(2) Honor a Recipient");
        System.out.println("(3) Gift a Recipient");
        //Remove/Edit gifts
        System.out.println("(5) Check Honor Levels");
        System.out.println("(6) Check Gift");
        System.out.println("(7) Quit");

        int answer = scan.nextInt();

        if (answer == 0) displayRecent();
        if (answer == 1) displayAll();
        if (answer == 2) honor();
        if (answer == 3) gift();
        //Add Remove/edit gift
        if (answer == 5) checkHonor();
        if (answer == 6) checkGift();
        if (answer == 7) end();

    }

    static void displayAll() {
        System.out.println("--------------");
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            rs = stmt.executeQuery("SELECT * FROM RECIPIENTS");

            int i = 0;
            while (rs.next()) {
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");

                System.out.println("(" + Character.toUpperCase(alphabet[i]) + ") " + fName + " " + lName);
                i++;
            }

            mainStep();
        } catch (SQLException e) {
            sqlError(e);
        }
    }

    static void displayRecent() {
        System.out.println("--------------");
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            rs = stmt.executeQuery("SELECT * FROM RECENTRECIPIENTS");

            int i = 0;
            while (rs.next()) {
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");

                System.out.println("(" + Character.toUpperCase(alphabet[i]) + ") " + fName + " " + lName);
                i++;
            }

            mainStep();
        } catch (SQLException e) {
            sqlError(e);
        }
    }

    static void end() {
        System.out.println("See you soon!");
    }

    static void sqlError(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    public static void main(String[] args) {
        //Print Welcome statement & initilize connection to sql databse
        intitalize();

        mainStep();

        

    
    }
}

