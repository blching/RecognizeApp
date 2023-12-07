import java.sql.*;
import java.util.Scanner;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
public class RecognizeApp {
    public static Connection conn = null;
    public static ResultSet rs = null;
    public static Statement stmt = null;
    static Scanner scan;

    public String userFName = "User";
    public String userLName = "Admin";
    public static int userID = 0;

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
        String pass = "";
        String user = "";
        String db = "";

            try (InputStream input = new FileInputStream("app.properties")) {
                Properties p = new Properties();
                p.load(input);

                pass = p.getProperty("db.pass");
                user = p.getProperty("db.user");
                db = p.getProperty("db.name");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(user);
            System.out.println(pass);
            System.out.println(db);

            try {
                /*conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/test?" +
                    "user=root&password=root");*/
                    
                conn = DriverManager.getConnection("jdbc:mysql://localhost/" + db +"?" +
                    "user="+user+"&password="+pass);

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
        String lName = "";

        if (fName.equals("-q") || fName.equals("-Q")) {
            mainStep();
        } else {
            System.out.println("Please enter the last name of who you want to Honor");
            lName = scan.nextLine();
        }
        
        //System.out.println("SELECT honorLevel FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
        try {
            int honorLvl = 0;
            openSQL("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");

            while (rs.next()) {
                honorLvl = rs.getInt("honorLevel");
                honorLvl++;
                stmt.executeUpdate("UPDATE RECIPIENTS SET honorLevel = '" + honorLvl + "' WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
            }

        } catch (SQLException ex) {
            //sqlError(ex);               
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

    static void checkMinimumHonor() {
        System.out.println("What minimum honor do you want to check for?");
        int min = scan.nextInt();

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        try {
            openSQL("call minLvl(" + Integer.toString(min) + ");");

            int honorLevel;
            String fName;
            String lName;
            int i = 0;

            while (rs.next()) {
                honorLevel = rs.getInt("honorLevel");
                fName = rs.getString("fName");
                lName = rs.getString("lName");
                
                System.out.println("(" + Character.toUpperCase(alphabet[i]) + ") " + fName + " " + lName + " | Honor Level: " + honorLevel);
                i++;
            }
        } catch (SQLException e) {
            sqlError(e);
        } finally {
            closeSQL();
        }
        mainStep();
    }
    
    static void gift() {
        System.out.println("--------------");
        System.out.println("Who would you like to gift? (First Name)");
        String fName = scan.nextLine();

        System.out.println("Who would you like to gift? (Last Name)");
        String lName = scan.nextLine();

        System.out.println("What is in this gift?");
        String descrip = scan.nextLine();

        System.out.println("How much does this cost?");
        String price = scan.nextLine();

        try {
            openSQL("SELECT id FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
            while (rs.next()) {
                int id = rs.getInt("id");
                String cmd = "INSERT INTO GIFTS(gifterID, recieverID, description, price) VALUES (\"" + userID + "\", \"" + id + "\", \"" + descrip + "\", \"" + price + "\");";
                //System.out.println(cmd);
                stmt.executeUpdate(cmd);
            }

        } catch (SQLException ex) {
            //sqlError(ex);               
        } finally {
            closeSQL();
        }

        mainStep();

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
        System.out.println("Who would you like to check gifts? (First Name)");
        String fName = scan.nextLine();

        System.out.println("Who would you like to check gifts? (Last Name)");
        String lName = scan.nextLine();

        System.out.println("Check gifts given or recieved? (Input 0 or 1)");
        int answer = scan.nextInt();
        int id = -1;

        if (answer == 0) {

            try {
                openSQL("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
                while (rs.next()) {
                    id = rs.getInt("id");
                }

            } catch (SQLException ex) {
                //sqlError(ex);               
            } finally {
                closeSQL();
            }

            try {
                openSQL("SELECT * FROM GIFTS WHERE gifterId = " + id + ";");
                while (rs.next()) {
                    String gifterID = rs.getString("gifterID");
                    String recieverID = rs.getString("reciverID");
                    String description = rs.getString("desc");
                    int price = rs.getInt("price");

                    System.out.println("Gifter ID: " + gifterID + " | RecieverID: " + recieverID + " | Description: " + description + " | Price = " + price);
                }

            } catch (SQLException ex) {
                //sqlError(ex);               
            } finally {
                closeSQL();
            }
        } else {
            try {
                openSQL("SELECT * FROM RECIPIENTS WHERE fName = '" + fName + "' AND lName = '" + lName + "';");
                while (rs.next()) {
                    id = rs.getInt("id");
                }

            } catch (SQLException ex) {
                //sqlError(ex);               
            } finally {
                closeSQL();
            }

            try {
                openSQL("SELECT * FROM GIFTS WHERE recieverID = " + id + ";");

                while (rs.next()) {
                    String gifterID = rs.getString("gifterID");
                    String recieverID = rs.getString("recieverID");
                    String description = rs.getString("description");
                    int price = rs.getInt("price");

                    System.out.println("Gifter ID: " + gifterID + " | RecieverID: " + recieverID + " | Description: " + description + " | Price = " + price);
                }

            } catch (SQLException ex) {
                sqlError(ex);               
            } finally {
                closeSQL();
            }
        }

        mainStep();
    }

    static void mainStep() {
        System.out.println("--------------");

        //Provides options for user
        Scanner scan = new Scanner(System.in);
        System.out.println("What do you want to do?");

        //Display All users
        System.out.println("(0) Display Recent Recipients");
        System.out.println("(1) Display all Recipients");
        System.out.println("(2) Honor a Recipient");
        System.out.println("(3) Gift a Recipient");
        System.out.println("(4) Check Honor Levels");
        System.out.println("(5) Check Minimum Honor Levels");
        System.out.println("(6) Check Gift");
        System.out.println("(7) Quit");

        int answer = scan.nextInt();

        //Utilizes the answer to execute functions done to the system
        if (answer == 0) displayRecent();
        if (answer == 1) displayAll();
        if (answer == 2) honor();
        if (answer == 3) gift();
        if (answer == 4) checkHonor();
        if (answer == 5) checkMinimumHonor();
        if (answer == 6) checkGift();
        if (answer == 7) end();
        
        mainStep();
    }

    static void displayAll() {
        System.out.println("--------------");
        try {
            char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
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

