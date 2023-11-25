import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver {
    DBConnect db;
    static JFrame frame1 = new JFrame();
    Scanner in;

    public Driver(){
        db = new DBConnect();
        in = new Scanner(System.in);
    }


    public void closePanel1(JPanel panel1){
        panel1.setVisible(false);
        login1();
    }

    public void login1(){
        JPanel panel2 = new JPanel();
        panel2.setLayout(null);
        frame1.add(panel2);

        JButton submit = new JButton();
        submit.setText("Login");
        submit.setBounds(140,200,120,50);

        JLabel labelu = new JLabel("Username");
        labelu.setBounds(50,10,300,30);
        JLabel labelp = new JLabel("Password");
        labelp.setBounds(50,90,300,30);

        JTextField usernamefield = new JTextField();
        usernamefield.setBounds(50,40,300,30);
        JPasswordField passwordfield = new JPasswordField();
        passwordfield.setBounds(50,120,300,30);

        panel2.add(submit);
        panel2.add(usernamefield);
        panel2.add(passwordfield);
        panel2.add(labelu);
        panel2.add(labelp);
        panel2.setVisible(true);

        submit.addActionListener(e -> login2(usernamefield.getText(), passwordfield.getText()));

    }

    public void login2(String username, String password){

        String[] result = db.read("SELECT * FROM User WHERE username = '" + username + "' AND password = '" + password + "';");
        if(result.length == 0){
            JOptionPane.showMessageDialog(null, "Username and Password combination not found. Re-enter your details.", "Error", JOptionPane.ERROR_MESSAGE);
            login1();
            return;
        }

        //check that account is inactive or unregistered before logging in.
        if(Objects.equals(result[11], "0")){
            JOptionPane.showMessageDialog(null, "This account has not yet been approved for registration. Please contact your manager.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(Objects.equals(result[10], "0")){
            JOptionPane.showMessageDialog(null, "This account has been deactivated. Please contact your manager.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String i = result[3];
        frame1.setVisible(false);
        frame1.dispose();
        boolean running = true;
        switch(i) {
            case "Student":
                Student s = new Student(result, db);
                student(s);
                return;
            case "Lecturer":
                Lecturer l = new Lecturer(result, db);
                lecturer(l);
                return;
            case "Manager":
                Manager m = new Manager(result, db);
                manager(m);
                return;
        }
    }

    public void student(Student s){
        JOptionPane.showMessageDialog(null, "You have successfully logged in", "Student", JOptionPane.PLAIN_MESSAGE);
        JFrame jstudent = new JFrame();
        jstudent.setTitle("Student");
        jstudent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jstudent.setSize(400,400);
        jstudent.setVisible(true);

            /*System.out.println("\n\nWhat would you like to do?");
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
        }*/
    }

    public void lecturer(Lecturer l){
        JOptionPane.showMessageDialog(null, "You have successfully logged in", "Lecturer", JOptionPane.PLAIN_MESSAGE);
        JFrame jlecturer = new JFrame();
        jlecturer.setTitle("Lecturer");
        jlecturer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jlecturer.setSize(400,400);
        jlecturer.setVisible(true);

        /*System.out.println("\n\nLogin Success! Welcome...");
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
                            System.out.println("Enter the notes type: ");
                            String type = in.nextLine();
                            l.uploadNotes(type, new File("testNotes.txt"));
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
        }*/

    }

    public void manager(Manager m){
        JOptionPane.showMessageDialog(null, "You have successfully logged in", "Manager", JOptionPane.PLAIN_MESSAGE);
        JFrame jmanager = new JFrame();
        jmanager.setTitle("Manager");
        jmanager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jmanager.setSize(400,400);
        jmanager.setVisible(true);


        /*Integer inp;
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
        } */
    }

    public void register1() {
        frame1.setVisible(false);
        frame1.dispose();
        ImageIcon image = new ImageIcon("mongey.jpg");
        JFrame jregister = new JFrame();
        jregister.setTitle("Register");
        jregister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jregister.setLayout(null);
        jregister.setSize(500, 900);
        jregister.setIconImage(image.getImage());
        jregister.setResizable(false);
        jregister.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Register");
        submit.setBounds(190, 740, 120, 50);

        JLabel text = new JLabel("Please enter the following details about yourself:");
        text.setBounds(100,10,300,30);
        JLabel username = new JLabel("Username:");
        username.setBounds(100, 30, 300, 30);
        JLabel password = new JLabel("Password:");
        password.setBounds(100, 100, 300, 30);
        JLabel password2 = new JLabel("Confirm password:");
        password2.setBounds(100, 170, 300, 30);
        JLabel usertype = new JLabel("User type:");
        usertype.setBounds(100, 240, 300, 30);
        JLabel email = new JLabel("Email:");
        email.setBounds(100, 310, 300, 30);
        JLabel fname = new JLabel("First name:");
        fname.setBounds(100, 380, 300, 30);
        JLabel sname = new JLabel("Surname:");
        sname.setBounds(100, 450, 300, 30);
        JLabel gender = new JLabel("Gender:");
        gender.setBounds(100, 520, 300, 30);
        JLabel dob = new JLabel("Date of birth(in format 'yyyy-mm-dd'):");
        dob.setBounds(100, 590, 300, 30);
        JLabel qualification = new JLabel("Qualification(this will only be processed if you are a lecturer):");
        qualification.setBounds(100, 660, 400, 30);

        JTextField usernamef = new JTextField();
        usernamef.setBounds(100, 60, 300, 30);
        JPasswordField passwordf = new JPasswordField();
        passwordf.setBounds(100, 130, 300, 30);
        JPasswordField password2f = new JPasswordField();
        password2f.setBounds(100, 200, 300, 30);
        JRadioButton studentButton = new JRadioButton();
        studentButton.setText("Student");
        studentButton.setBounds(100,270,100,30);
        JRadioButton lecturerButton = new JRadioButton();
        lecturerButton.setText("Lecturer");
        lecturerButton.setBounds(200,270,100,30);
        JRadioButton managerButton = new JRadioButton();
        managerButton.setText("Manager");
        managerButton.setBounds(300,270,100,30);
        JTextField emailf = new JTextField();
        emailf.setBounds(100, 340, 300, 30);
        JTextField fnamef = new JTextField();
        fnamef.setBounds(100, 410, 300, 30);
        JTextField snamef = new JTextField();
        snamef.setBounds(100, 480, 300, 30);
        JRadioButton maleButton = new JRadioButton();
        maleButton.setText("Male");
        maleButton.setBounds(100,550,100,30);
        JRadioButton femaleButton = new JRadioButton();
        femaleButton.setText("Female");
        femaleButton.setBounds(200,550,100,30);
        JRadioButton nbButton = new JRadioButton();
        nbButton.setText("Non-Binary");
        nbButton.setBounds(300,550,100,30);
        JRadioButton otherButton = new JRadioButton();
        otherButton.setText("Other");
        otherButton.setBounds(400,550,100,30);
        JTextField dobf = new JTextField();
        dobf.setBounds(100, 620, 300, 30);
        JRadioButton phdButton = new JRadioButton();
        phdButton.setText("PhD");
        phdButton.setBounds(100,690,100,30);
        JRadioButton mscButton = new JRadioButton();
        mscButton.setText("MSc");
        mscButton.setBounds(200,690,100,30);
        JRadioButton doctorateButton = new JRadioButton();
        doctorateButton.setText("Doctorate");
        doctorateButton.setBounds(300,690,100,30);

        jregister.add(submit);
        jregister.add(text);
        jregister.add(username);
        jregister.add(password);
        jregister.add(usertype);
        jregister.add(password2);
        jregister.add(email);
        jregister.add(fname);
        jregister.add(sname);
        jregister.add(gender);
        jregister.add(dob);
        jregister.add(qualification);

        jregister.add(usernamef);
        jregister.add(passwordf);
        jregister.add(password2f);
        jregister.add(studentButton);
        jregister.add(lecturerButton);
        jregister.add(managerButton);
        jregister.add(emailf);
        jregister.add(fnamef);
        jregister.add(snamef);
        jregister.add(maleButton);
        jregister.add(femaleButton);
        jregister.add(nbButton);
        jregister.add(otherButton);
        jregister.add(dobf);
        jregister.add(phdButton);
        jregister.add(mscButton);
        jregister.add(doctorateButton);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(studentButton);
        BG1.add(lecturerButton);
        BG1.add(managerButton);

        ButtonGroup BG2 = new ButtonGroup();
        BG2.add(maleButton);
        BG2.add(femaleButton);
        BG2.add(nbButton);
        BG2.add(otherButton);

        ButtonGroup BG3 = new ButtonGroup();
        BG3.add(phdButton);
        BG3.add(mscButton);
        BG3.add(doctorateButton);

        submit.addActionListener(e -> register2(usernamef.getText(), passwordf.getText(), password2f.getText(), getUsertype(studentButton, lecturerButton, managerButton), emailf.getText(), fnamef.getText(), snamef.getText(), getGender(maleButton, femaleButton, nbButton, otherButton), dobf.getText(), getQualification(phdButton, mscButton, doctorateButton), jregister));

    }

    public String getUsertype(JRadioButton studentButton, JRadioButton lecturerButton, JRadioButton managerButton) {
        String usertype = "error";
        if (studentButton.isSelected()) {
            usertype = "Student";
        } else if (lecturerButton.isSelected()) {
            usertype = "Lecturer";
        } else if (managerButton.isSelected()){
            usertype = "Manager";
        }
        return usertype;
    }

    public String getGender(JRadioButton maleButton, JRadioButton femaleButton, JRadioButton nbButton, JRadioButton otherButton) {
        String gender = "error";
        if (maleButton.isSelected()) {
            gender = "Male";
        } else if (femaleButton.isSelected()) {
            gender = "Female";
        } else if (nbButton.isSelected()){
            gender = "Non-Binary";
        } else if (otherButton.isSelected()) {
            gender = "Other";
        }
        return gender;
    }

    public String getQualification(JRadioButton phdButton, JRadioButton mscButton, JRadioButton doctorateButton) {
        String qualification = "error";
        if (phdButton.isSelected()) {
            qualification = "PhD";
        } else if (mscButton.isSelected()) {
            qualification = "MSc";
        } else if (doctorateButton.isSelected()){
            qualification = "Doctorate";
        }
        return qualification;
    }

    public void register2(String username, String password, String password2, String usertype, String email, String fname, String sname, String gender, String dob, String qualification, JFrame jregister){
        String[] details = new String[8];
        String temp;
        String temp2;

        boolean error = valid_length(username,"username",20);
        if (error) {
            return;
        }
        String[] existingUser = db.read("SELECT Username FROM User WHERE Username = '" + username + "';");
        if(existingUser.length != 0){
            JOptionPane.showMessageDialog(null, "That username is already taken", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        details[0] = username;

        error = valid_length(password,"password",25);
        if (error) {
            return;
        }
        if(!Objects.equals(password, password2)){
            System.out.println("Your passwords do not match, please re enter your password:");
            JOptionPane.showMessageDialog(null, "Your passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        details[1] = password;

        if (usertype == "error"){
            JOptionPane.showMessageDialog(null, "You have not selected your user type", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        details[2] = usertype;

        error = valid_length(email,"email", 50);
        if (error) {
            return;
        }
        error = valid_email(email);
        if (!error) {
            JOptionPane.showMessageDialog(null, "Your email is invalid", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        details[3] = email;

        error = valid_length(fname,"first name",20);
        if (error) {
            return;
        }
        details[4] = fname;

        error = valid_length(sname,"surname",25);
        if (error) {
            return;
        }
        details[5] = sname;

        if (gender == "error"){
            JOptionPane.showMessageDialog(null, "You have not selected your gender", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        details[6] = gender;

        error = valid_dob_format(dob);
        if (!error) {
            JOptionPane.showMessageDialog(null, "Your dob is formatted incorrectely", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] arrOfDOB = dob.split("-", 5);
        int Y = Integer.parseInt(arrOfDOB[0]);
        int M = Integer.parseInt(arrOfDOB[1]);
        int D = Integer.parseInt(arrOfDOB[2]);
        boolean leap = is_leap_year(Y);
        if(((M == 4 || M == 6 || M == 9 || M == 11) && D == 31) || (M == 2 && ((!leap && D > 28) || (leap && D >29)))){
            JOptionPane.showMessageDialog(null, "The date of birth you have entered does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        details[7] = dob;
        if(details[2].equals("Lecturer")){
            if (qualification == "error"){
                JOptionPane.showMessageDialog(null, "You have not selected your qualification", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.write("INSERT INTO User (Username,Password,User_Type,Email,First_Name,Surname,Gender,DOB,Active,Registered) VALUES ('"+details[0]+"','"+details[1]+"','"+details[2]+"','"+details[3]+"','"+details[4]+"','"+details[5]+"','"+details[6]+"','"+details[7]+"',0,0);");
            String ID = db.read("SELECT User_Id FROM User WHERE Username = '" + details[0] + "';")[0];
            db.write("UPDATE Module (Lecturer_Qualification) VALUES (" + qualification + ") WHERE Lecturer_Id == '" + ID + "';");
        } else {
            db.write("INSERT INTO User (Username,Password,User_Type,Email,First_Name,Surname,Gender,DOB,Active,Registered) VALUES ('"+details[0]+"','"+details[1]+"','"+details[2]+"','"+details[3]+"','"+details[4]+"','"+details[5]+"','"+details[6]+"','"+details[7]+"',0,0);");
        }
        JOptionPane.showMessageDialog(null, "You have successfully registered, a manager will approve your account shortly", "Success", JOptionPane.PLAIN_MESSAGE);
        jregister.setVisible(false);
        jregister.dispose();
    }

    public boolean valid_length(String x,String y,int l){
        boolean error = false;
        if(x.length() > l){
            JOptionPane.showMessageDialog(null, "Your " + y + " is over the maximum length of " + l +" characters", "Error", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        return error;
    }

    
    public static boolean valid_email(String email){ 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        return pat.matcher(email).matches(); 
    }

    public static boolean valid_dob_format(String dob){
        String regex = "^([0-9]{4})-(1[0-2]"
                + "|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";

        Pattern pat = Pattern.compile(regex);
        return pat.matcher(dob).matches();
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

        JPanel panel1 = new JPanel();

        ImageIcon image = new ImageIcon("mongey.jpg");

        JButton Lbutton = new JButton();
        //if login button is clicked start the login function
        Lbutton.addActionListener(e -> d.closePanel1(panel1));
        Lbutton.setText("Login");
        Lbutton.setFocusable(false);

        JButton Rbutton = new JButton();
        //if register button is clicked start the register function
        Rbutton.addActionListener(e -> d.register1());
        Rbutton.setText("Register");
        Rbutton.setFocusable(false);


        frame1.setTitle("USMS");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(400,400);
        frame1.setIconImage(image.getImage());
        frame1.add(panel1);
        frame1.setResizable(false);

        panel1.add(Lbutton);
        panel1.add(Rbutton);
        panel1.setLayout(new GridLayout(2,1));

        frame1.setVisible(true);
        panel1.setVisible(true);

        d.in.close();
    }
}
