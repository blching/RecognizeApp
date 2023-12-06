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
            /* conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/test?" +
                "user=root&password=root");
            stmt = conn.createStatement(); */

            System.out.println("Here are our five recent honor recipents:");

            //rs = stmt.executeQuery("SELECT * FROM RECENTRECIPIENTS");
            openSQL("SELECT * FROM RECENTRECIPIENTS");

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
        } finally {
            closeSQL();
        }
    }

    static void openSQL(String command) {
            try {
                conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/test?" +
                    "user=root&password=root");

                stmt = conn.createStatement();
                rs = stmt.executeQuery(command);

            } catch (SQLException ex) {
                // TODO Auto-generated catch block
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
    }

    static void closeSQL() {
            if (rs != null) {
                /* try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore */
                rs = null; 

            }
            if (stmt != null) {
                /* try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore */
                stmt = null;
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
            openSQL("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
            //rs = stmt.executeQuery("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");

            while (rs.next()) {
                honorLvl = rs.getInt("honorLevel");
                honorLvl++;
                stmt.executeUpdate("UPDATE RECIPIENTS SET honorLevel = '" + honorLvl + "' WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
            }

        } catch (SQLException ex) {
            //System.out.println("SQLException: " + ex.getMessage());
            //System.out.println("SQLState: " + ex.getSQLState());
            //System.out.println("VendorError: " + ex.getErrorCode());        
        } finally {
            closeSQL();
        }

        openSQL("SELECT honorLevel FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
        try {
            while (rs.next()) {
                System.out.println("New Honor Level of: " + rs.getInt("honorLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSQL();
        }

        mainStep();

    }

    static void gift() {

    }

    static void checkHonor() {
        System.out.println("--------------");
        System.out.println("(Enter -Q to return to main menu)");
        System.out.println("Please enter first name of user you want to check Honor Level");

        String fName = scan.nextLine();

        if (fName.equals("-q") || fName.equals("-Q")) {
            mainStep();
        } else {
            System.out.println("Please enter the last name of who you want to check Honor Level");
            String lName = scan.nextLine();

            try {
                System.out.println("SELECT honorLevel FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "'");

                openSQL("SELECT honorLevel FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");

                while (rs.next()) {
                    int lvl = rs.getInt("honorLevel");
                    System.out.println(fName + " " + lName + ": " + Integer.toString(lvl));
                }
            } catch (SQLException e) {
                sqlError(e);
            } finally {
                closeSQL();
            }
            mainStep();
        }
        

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
        
        mainStep();
    }

    static void displayAll() {
        System.out.println("--------------");
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            openSQL("SELECT * FROM RECIPIENTS");

            //rs = stmt.executeQuery("SELECT * FROM RECIPIENTS");

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
        } finally {
            closeSQL();
        }
    }

    static void displayRecent() {
        System.out.println("--------------");
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            //rs = stmt.executeQuery("SELECT * FROM RECENTRECIPIENTS");
            openSQL("SELECT * FROM RECENTRECIPIENTS");

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
        } finally {
            closeSQL();
        }
    }

    static void end() {
        System.out.println("See you soon!");
        System.exit(0);
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

