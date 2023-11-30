import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.exit;

public class DBConnect {
    private static Connection mysqlConn = null;
    //this block will run only once when class is loaded into memory
    static
    {
        String url = "jdbc:mysql:// ";
        String dbName ="";
        String user = "" ;
        String password = "";
        try {// Reading the connection parameters from “config.txt” file
            Scanner sc = new Scanner(new File("DBconnect.txt"));
            if (sc.hasNextLine()) url = url + sc.nextLine()+"/";
            if (sc.hasNextLine()) dbName = sc.nextLine();
            if (sc.hasNextLine()) user = sc.nextLine();
            if (sc.hasNextLine()) password = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading config.txt");
            exit(-1);
        }
        try {Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConn = DriverManager.getConnection(url+dbName, user,
                    password);
            System.out.println("MySQL Db Connection is successful");}
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();}
    }
    public static Connection getMysqlConnection()
    {return mysqlConn;}

    public String[] read(String statement){
        if(mysqlConn != null){
            try{
                Statement stmt = mysqlConn.createStatement();
                ResultSet rs = stmt.executeQuery(statement);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                List<String> results = new ArrayList<String>();
                while(rs.next()){
                    for(int i=1; i<=columnsNumber; i++){
                        results.add(rs.getString(i));
                    }
                }
                String[] temp = new String[results.size()];
                return results.toArray(temp);
            }
            catch(SQLException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public int write(String statement){
        if(mysqlConn != null){
            try{
                Statement stmt = mysqlConn.createStatement();
                stmt.executeUpdate(statement);
                return 1;
            }
            catch(SQLException e){
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
}