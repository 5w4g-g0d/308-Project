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
            System.out.println("Error: Username and Password combination not found. Enter 'Y' to create a new user.");
            //TODO: option to create new user.
            return;
        }else if(result.length > 1){
            System.out.println("Error: Multiple results returned. Please contact an administrator.");
            return;
        }else if(result[10] == "0"){
            System.out.println("Error: Account is not active.");
            return;
        }
        else if(result[11] == "0"){
            System.out.println("Error: Account is not registered.");
            return;
        }else{
            String i = result[2];
            Boolean running = true;
            switch(i) {
                case "Student":
                    Student s = new Student(result, db);
                    while(running){
                        System.out.println("What would you like to do?");
                        System.out.println("1. Change Password");
                        System.out.println("2. View your modules");
                        System.out.println("3. View Results");
                        System.out.println("4. View Decision");
                        System.out.println("5. View Notes");
                        System.out.println("0. Exit");
                        String choice = in.nextLine();
                        switch(choice){
                            case "1":
                                System.out.println("Enter new password: ");    
                                s.updatePass(in.nextLine());
                                break;
                            case "2":
                                String[] modules = s.getModules();
                                for(int j = 0; j < modules.length; j++){
                                    System.out.println(modules[j]);
                                }
                                break;
                            case "3":
                                System.out.println("Enter Module code: ");
                                String j = in.nextLine();
                                System.out.println(s.getResult(j));
                                break;
                            case "4":
                                System.out.println("Enter Module code: ");
                                String k = in.nextLine();
                                System.out.println(s.getDecision(k));
                                break;
                            case "5":
                                System.out.println("Enter Module code: ");
                                String l = in.nextLine();
                                System.out.println(db.read("SELECT Lab_notes FROM Module WHERE Module_Id = " + l + ";"));
                                System.out.println(db.read("SELECT Lecture_notes FROM Module WHERE Module_Id = " + l + ";"));
                                return;
                            case "0":
                                running = false;
                                break;
                            default:
                                System.out.println("Invalid input.");
                                break;
                        }
                    }
                    break;
                case "Lecturer":
                    Lecturer l = new Lecturer(result, db);
                    //TODO: Lecturer menu
                    break;
                case "Manager":
                    Manager m = new Manager(result, db);
                    //TODO: Manager menu
                    break;
            }
        }
    }
    
    
    public static void main(String[] args){
        Driver d = new Driver();
        System.out.println("Enter username: ");
        String x = d.in.nextLine();
        System.out.println("Enter password: ");
        String y = d.in.nextLine();
        d.login(x, y);
        d.in.close();
    }
}
