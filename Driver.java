import java.util.Scanner;

public class Driver {
    DBConnect db;
    Scanner in;

    public Driver(){
        db = new DBConnect();
        in = new Scanner(System.in);
    }

    public void login(String username, String password){
        String[] result = db.read("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';");
        if(result == null || result.length == 0){
            System.out.println("Error: Username or Password is incorrect.");
            //in future, option to create new user. See: Register.java
            return;
        }else if(result.length > 1){
            System.out.println("Error: Multiple results returned. Please contact an administrator.");
            return;
        }else{
            String i = result[2];
            switch(i) {
                case "Student":
                    Student s = new Student(result, db);
                    break;
                case "Lecturer":
                    Lecturer l = new Lecturer(result, db);
                    break;
                case "Manager":
                    Manager m = new Manager(result, db);
                    break;
            }
        }
    }
    
    
    public static void main(String[] args){
        Driver d = new Driver();
        d.login(d.in.nextLine(), d.in.nextLine());
        d.in.close();
    }
}
