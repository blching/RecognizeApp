import java.sql.*;
import java.util.Scanner;
import java.util.Properties;

public class RecognizeApp {
    public static Connection conn = null;
    public static ResultSet rs = null;
    public static   Statement stmt = null;

    static void intitalize() {
        /*
            java -cp .:/Users/brandonching/Desktop/School/CS157a/mysql-connector-j-8.2.0.jar
        */
        System.out.println("Welcome to Honor World!");
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/test?" +
                "user=root&password=root");
            stmt = conn.createStatement();

             System.out.println("Here are our five recent honor recipents:");

            rs = stmt.executeQuery("SELECT * FROM recentrecipents");

            while (rs.next()) {
                String fName = rs.getString("fName");
                String lName = rs.getString("fName");

                System.out.println(fName + " " + lName);
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    static void mainStep() throws SQLException {
        System.out.println("What do you want to do?");

        
    }

    public static void main(String[] args) {
        //Print Welcome statement & initilize connection to sql databse
        intitalize();
      
        try {
            mainStep();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        

    
    }
}

