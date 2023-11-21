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

    public void login(){
        System.out.println("Enter your username: ");
        String username = in.nextLine();
        System.out.println("Enter your password: ");
        String password = in.nextLine();
        String[] result = db.read("SELECT * FROM User WHERE username = '" + username + "' AND password = '" + password + "';");
        while(result.length == 0){
            System.out.println("Username and Password combination not found. Re-enter your details.");
            System.out.println("Enter your username: ");
            username = in.nextLine();
            System.out.println("Enter your password: ");
            password = in.nextLine();
            result = db.read("SELECT * FROM User WHERE username = '" + username + "' AND password = '" + password + "';");
        }

        //check that account is inactive or unregistered before logging in.
        if(Objects.equals(result[10], "0")){
            System.out.println("This account has been deactivated. Please contact your manager.");
            return;
        }
        if(Objects.equals(result[11], "0")){
            System.out.println("This account has not yet been approved for registration. Please contact your manager.");
            return;
        }

        String i = result[3];
        boolean running = true;
        switch(i) {
            case "Student":
                Student s = new Student(result, db);
                System.out.println("\n\nLogin Success! Welcome...");
                while(running){
                    System.out.println("\n\nWhat would you like to do?");
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
                            return;
                        case "0":
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid input.");
                            break;
                    }
                }
            return;
            case "Lecturer":
                Lecturer l = new Lecturer(result, db);
                System.out.println("\n\nLogin Success! Welcome...");
                while(running) {
                    System.out.println("\n\nWhat would you like to do?");
                    System.out.println("1. Change Password");
                    System.out.println("2. View my module");
                    System.out.println("3. Return a student's mark");
                    System.out.println("4. Update my module");
                    System.out.println("5. Upload notes");
                    System.out.println("6. View my students");
                    System.out.println("0. Exit");
                    String choice = in.nextLine();

                    switch (choice) {
                        case "1":
                            System.out.println("Enter new password: ");
                            l.updatePass(in.nextLine());
                            break;

                        case "2":
                            System.out.println(l.getModule());
                            break;

                        case "3":
                            System.out.println("Enter a student's ID: ");
                            Integer id = in.nextInt();
                            in.nextLine();
                            System.out.println("Enter their mark: ");
                            Integer score = in.nextInt();
                            in.nextLine();
                            l.setMark(id, score);
                            break;

                        case "4":
                            System.out.println("Enter the new description: ");
                            String desc = in.nextLine();
                            l.updateModule(desc);
                            break;

                        case "5":
                            System.out.println("NOT WORKING RN");
                            /*System.out.println("Enter the notes type: ");
                            String type = in.nextLine();
                            l.uploadNotes(type, new File("testNotes.txt"));*/
                            break;

                        case "6":
                            String[] res = l.getStudents();
                            for (String re : res) {
                                System.out.println(re);
                            }

                        case "0":
                            running = false;
                            break;

                        default:
                            System.out.println("Invalid input.");
                            break;
                    }
                }
            break;
            case "Manager":
                Manager m = new Manager(result, db);
                Integer inp;
                System.out.println("\n\nLogin Success! Welcome...");
                while(running) {
                    System.out.println("\n\nWhat would you like to do?");
                    System.out.println("1. Change Password");
                    System.out.println("2. Account management menu >>");
                    System.out.println("3. Module management menu >>");
                    System.out.println("4. Business rules menu >>");
                    System.out.println("0. Exit");
                    String choice = in.nextLine();

                    switch(choice){
                        case "1":
                            System.out.println("Enter new password: ");
                            m.updatePass(in.nextLine());
                        break;

                        case "2":
                            boolean menuRunning = true;
                            while(menuRunning) {
                                System.out.println("\n\nAccount management menu");
                                System.out.println("1. View accounts awaiting registration");
                                System.out.println("2. Approve a new account");
                                System.out.println("3. Toggle account activity");
                                System.out.println("4. Reset a password");
                                System.out.println("5. Set a student's decision");
                                System.out.println("0. Return");
                                String menuChoice = in.nextLine();

                                switch (menuChoice) {
                                    case "1":
                                        m.DisplayQueue();
                                        break;

                                    case "2":
                                        System.out.print("Enter target user ID: ");
                                        inp = in.nextInt();
                                        in.nextLine();
                                        m.ApproveUser(inp);
                                        break;

                                    case "3":
                                        System.out.print("Enter target user ID: ");
                                        inp = in.nextInt();
                                        in.nextLine();
                                        System.out.print("Enter '1' to activate, or '0' to deactivate: ");
                                        String toggle = in.nextLine();
                                        if(Objects.equals(toggle, "1")){
                                            m.activate(inp);
                                        }else if(Objects.equals(toggle, "2")){
                                            m.deactivate(inp);
                                        }

                                    case "0":
                                        menuRunning = false;
                                        break;

                                    default:
                                        System.out.println("Invalid input. Try again.");
                                }
                            }
                            break;
                        case "0":
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid input. Please try again.");
                            break;
                    }
                }
            break;
        }
    }

    public void register(){
        String[] details = new String[8];
        String temp;
        String temp2;

        System.out.println("Please enter your username:");
        temp = in.nextLine();
        temp = valid_length(temp,"username",20);
        String[] existingUser = db.read("SELECT Username FROM User WHERE Username = '" + temp + "';");
        if(existingUser.length != 0){
            while(Objects.equals(temp, existingUser[0])){
                System.out.println("That username is already taken, please enter another username:");
                existingUser = db.read("SELECT Username FROM User WHERE Username = '" + temp + "';");
                temp = valid_length(temp,"username",20);
            }
        }
        details[0] = temp;

        System.out.println("Please enter your password:");
        temp= in.nextLine();
        temp = valid_length(temp,"password",25);
        System.out.println("Please confirm your password:");
        temp2= in.nextLine();
        while(!Objects.equals(temp, temp2)){
            System.out.println("Your passwords do not match, please re enter your password:");
            temp = in.nextLine();
            temp = valid_length(temp,"password",25);
            System.out.println("Please confirm your password:");
            temp2 = in.nextLine();
        }
        details[1] = temp;

        System.out.println("What type of user are you(student,lecturer,manager):");
        temp = in.nextLine();
        while(!Objects.equals(temp, "student") && !Objects.equals(temp, "lecturer") && !Objects.equals(temp, "manager")){
            System.out.println("Please enter one of the specified types of student, lecturer and manager:");
            temp = in.nextLine();
        }
        details[2] = temp;

        System.out.println("Please enter your email:");
        temp= in.nextLine();
        temp = valid_length(temp,"email", 50);
        boolean verified = valid_email(temp);
        while(!verified){
            System.out.println("That email is invalid, please enter a valid email");
            temp = in.nextLine();
            temp = valid_length(temp,"email", 50);
            verified = valid_email(temp);
        }
        details[3] = temp;


        System.out.println("Please enter your first name:");
        temp = in.nextLine();
        temp = valid_length(temp,"first name",20);
        details[4] = temp;

        System.out.println("Please enter your surname:");
        temp = in.nextLine();
        temp = valid_length(temp,"surname",25);
        details[5] = temp;

        System.out.println("Please enter your gender(Male,Female,Non-Binary,Other):");
        temp = in.nextLine();
        while(!Objects.equals(temp, "Male") && !Objects.equals(temp, "Female") && !Objects.equals(temp, "Non-Binary") && !Objects.equals(temp, "Other")){
            System.out.println("Please enter one of the specified genders of Male, Female, Non-Binary and Other:");
            temp = in.nextLine();
        }
        details[6] = temp;


        System.out.println("Please enter your date of birth(in the format 'yyyy-mm-dd'):");
        boolean valid = false;
        while(!valid){
            temp = in.nextLine();
            temp = valid_dob_format(temp);
            String[] arrOfDOB = temp.split("-", 5);
            int Y = Integer.parseInt(arrOfDOB[0]);
            int M = Integer.parseInt(arrOfDOB[1]);
            int D = Integer.parseInt(arrOfDOB[2]);
            boolean leap = is_leap_year(Y);
            if(((M == 4 || M == 6 || M == 9 || M == 11) && D == 31) || (M == 2 && ((!leap && D > 28) || (leap && D >29)))){
                System.out.println("That date does not exist, please enter a valid date");
            }else{
                valid = true;
            }
        }
        details[7] = temp;

        db.write("INSERT INTO User (Username,Password,User_Type,Email,First_Name,Surname,Gender,DOB,Active,Registered) VALUES ('"+details[0]+"','"+details[1]+"','"+details[2]+"','"+details[3]+"','"+details[4]+"','"+details[5]+"','"+details[6]+"','"+details[7]+"',0,0);");
        if(details[2].equals("lecturer")){
            //more qualifications to be added, only 2 for test purposes rn
            System.out.println("Please enter your primary qualification");
            String Q = in.nextLine();
            while(!Objects.equals(Q, "PhD") && !Objects.equals(Q, "MSc")){
                System.out.println("That is not a valid qualification, please enter a valid qualification eg PhD or MSc");
                Q = in.nextLine();
            }
            String ID = db.read("SELECT User_Id FROM User WHERE Username = '" + details[0] + "';")[0];
            db.write("UPDATE Module (Lecturer_Qualification) VALUES (" + Q + ") WHERE Lecturer_Id == '" + ID + "';");
        }
    }

    public String valid_length(String x,String y,int l){
        while(x.length() > l){
            System.out.println("Your " + y + " is over the maximum length of " + l +" characters, please re-enter:");
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
            Matcher matcher = pattern.matcher(d);
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
        boolean is_leap_year;
        if (year % 4 == 0) { 
            is_leap_year = true; 
            if (year % 100 == 0) {
                is_leap_year = year % 400 == 0;
            } 
        }else{is_leap_year = false;}
        return is_leap_year;
    } 
    
    
    public static void main(String[] args){
        Driver d = new Driver();
        System.out.println("Select an option to begin: \n 1: Login to an existing account. \n 2: Register a new account.");
        int i = d.in.nextInt(); //input validation here PLEASE.
        d.in.nextLine(); //clears input after use of nextInt()
        switch(i){
            case 1:
                d.login();
                break;
            case 2:
                d.register();
                break;
            case 0:
                d.in.close();
                return;
        }
        d.in.close();
    }
}
