import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            System.out.println("Error: Username and Password combination not found. Enter '1' to create a new user.");
            int One = in.nextInt();
            if(One == 1) {
                register();
            }
        }else if(result.length > 1){
            System.out.println("Error: Multiple results returned. Please contact an administrator.");
        }else if(Objects.equals(result[10], "0")){
            System.out.println("Error: Account is not active.");
        }else if(Objects.equals(result[11], "0")){
            System.out.println("Error: Account is not registered.");
        }else{
            String i = result[2];
            boolean running = true;
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
                                for (String module : modules) {
                                    System.out.println(module);
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
                                System.out.println(Arrays.toString(db.read("SELECT Lab_notes FROM Module WHERE Module_Id = " + l + ";")));
                                System.out.println(Arrays.toString(db.read("SELECT Lecture_notes FROM Module WHERE Module_Id = " + l + ";")));
                                break;
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
                    //Lecturer l = new Lecturer(result, db);
                    //TODO: Lecturer menu
                    break;
                case "Manager":
                    //Manager m = new Manager(result, db);
                    //TODO: Manager menu
                    break;
            }
        }
    }

    public void register(){
        System.out.println("Please enter your username:");
        String U = in.nextLine();
        U = valid_length(U,"username",20);
        String[] VU = db.read("SELECT Username FROM User WHERE Username = '" + U + "';");
        String VU1 = VU[0];
        while(Objects.equals(U, VU1)){
            System.out.println("That username is already taken, please enter another username:");
            VU = db.read("SELECT Username FROM User WHERE Username = '" + U + "';");
            VU1 = VU[0];
            U = valid_length(U,"username",20);
        }

        System.out.println("Please enter your password:");
        String P = in.nextLine();
        P = valid_length(P,"password",25);
        System.out.println("Please confirm your password:");
        String VP = in.nextLine();
        while(!Objects.equals(P, VP)){
            System.out.println("Your passwords do not match, please re enter your password:");
            P = in.nextLine();
            P = valid_length(P,"password",25);
            System.out.println("Please confirm your password:");
            VP = in.nextLine();
        }

        System.out.println("Please enter your first name:");
        String FN = in.nextLine();
        FN = valid_length(FN,"first name",20);
        System.out.println("Please enter your surname:");
        String SN = in.nextLine();
        SN = valid_length(SN,"surname",25);
        System.out.println("Please enter your gender(Male,Female,Non-Binary,Other):");
        String G = in.nextLine();
        while(!Objects.equals(G, "Male") && !Objects.equals(G, "Female") && !Objects.equals(G, "Non-Binary") && !Objects.equals(G, "Other")){
            System.out.println("Please enter one of the specified genders of Male, Female, Non-Binary and Other:");
            G = in.nextLine();
        }
        
        System.out.println("Please enter your email:");
        String E = in.nextLine();
        E = valid_length(E,"email", 50);
        boolean VE = valid_email(E);
        while(!VE){
            System.out.println("That email is invalid, please enter a valid email");
            E = in.nextLine();
            E = valid_length(E,"email", 50);
            VE = valid_email(E);
        }
        
        System.out.println("Please enter your date of birth(in the format 'yyyy-mm-dd'):");
        boolean VDOB = false;
        String DOB = "error_prevention";
        while(!VDOB) {
            DOB = in.nextLine();
            DOB = valid_dob_format(DOB);
            String[] arrOfDOB = DOB.split("-", 5);
            int Y = Integer.parseInt(arrOfDOB[0]);
            int M = Integer.parseInt(arrOfDOB[1]);
            int D = Integer.parseInt(arrOfDOB[2]);
            boolean VY = is_leap_year(Y);
            if(((M == 4 || M == 6 || M == 9 || M == 11) && D == 31) || (M == 2 && ((!VY && D > 28) || (VY && D >29)))) {
                System.out.println("That date does not exist, please enter a valid date");
            } else {
                VDOB = true;
            }
        }
        
        System.out.println("What type of user are you(student,lecturer,manager):");
        String UT = in.nextLine();
        while(!Objects.equals(UT, "student") && !Objects.equals(UT, "lecturer") && !Objects.equals(UT, "manager")){
            System.out.println("Please enter one of the specified types of student, lecturer and manager:");
            UT = in.nextLine();
        }

        db.write("INSERT INTO User (Username,User_Type,Email,Password,First_Name,Surname,Gender,DOB,Active,Registered) VALUES ("+ U + ","+ UT + ","+ E + "," + P + "," + FN + "," + SN + "," + G + "," + DOB + ",0,0);");
        if(UT.equals("lecturer")){
            //more qualifications to be added, only 2 for test purposes rn
            System.out.println("Please enter your primary qualification");
            String Q = in.nextLine();
            while(!Objects.equals(Q, "PhD") && !Objects.equals(Q, "MSc")){
                System.out.println("That is not a valid qualification, please enter a valid qualification eg PhD or MSc");
                Q = in.nextLine();
            }
            String ID = db.read("SELECT User_Id FROM User WHERE Username = '" + U + "';")[0];
            db.write("INSERT INTO Module (Lecturer_Qualification) VALUES (" + Q + ") WHERE Lecturer_Id = '" + ID + "';");
        }
    }

    public String valid_length(String x,String y,int l){
        while(x.length() > l){
            System.out.println("Your " + y + " is over the maximum length of " + l +" ,please re enter:");
            x = in.nextLine();
        }
        return x;
    }

    
    public static boolean valid_email(String email){ 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        return pat.matcher(email).matches(); 
    } 

    public String valid_dob_format(String d){ 
        boolean valid_dob_format = false;
        while(!valid_dob_format){
            String regex = "^([0-9]{4})-(1[0-2]"
                       + "|0[1-9])-(3[01]|[12][0-9]|0[1-9])$"; 
            Pattern pattern = Pattern.compile(regex); 
            Matcher matcher = pattern.matcher((CharSequence)d);
            if(matcher.matches()){
                valid_dob_format = true;
            } else {
                System.out.println("That is not a valid date or format, please try again.");
                d = in.nextLine();
            }
        }
        return d; 
    } 

    public static boolean is_leap_year(int year){
        boolean is_leap_year = year % 4 == 0;
        if (year % 100 == 0 && year % 400 == 0){
            is_leap_year = true;
        }
        return is_leap_year;
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
