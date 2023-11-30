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

    //closes the startup panel to begin login
    public void closePanel1(JPanel panel1){
        panel1.setVisible(false);
        login1();
    }

    //closes the student, lecturer or manager frame and goes to startup
    public void closeFrame(JFrame frame){
        frame.setVisible(false);
        frame.dispose();
        startup();
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

        //passes the values the user has entered to login2() so they can be processed
        submit.addActionListener(e -> login2(usernamefield.getText(), passwordfield.getText(), panel2));

    }

    public void login2(String username, String password, JPanel panel){
        //checks that the password is correct for that username
        String[] result = db.read("SELECT * FROM User WHERE username = '" + username + "' AND password = '" + password + "';");
        if(result.length == 0){
            JOptionPane.showMessageDialog(null, "Username and Password combination not found. Re-enter your details.", "Error", JOptionPane.ERROR_MESSAGE);
            login1();
            return;
        }

        //checks if account is inactive or unregistered
        if(Objects.equals(result[11], "0")){
            JOptionPane.showMessageDialog(null, "This account has not yet been approved for registration. Please contact your manager.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(Objects.equals(result[10], "0")){
            JOptionPane.showMessageDialog(null, "This account has been deactivated. Please contact your manager.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String i = result[3];
        panel.setVisible(false);
        frame1.setVisible(false);
        frame1.dispose();
        switch(i) {
            case "Student":
                //opens the student frame
                JOptionPane.showMessageDialog(null, "You have successfully logged in", "Student", JOptionPane.PLAIN_MESSAGE);
                Student s = new Student(result, db);
                student(s);
                return;
            case "Lecturer":
                //opens the lecturer frame
                JOptionPane.showMessageDialog(null, "You have successfully logged in", "Lecturer", JOptionPane.PLAIN_MESSAGE);
                Lecturer l = new Lecturer(result, db);
                lecturer(l);
                return;
            case "Manager":
                //opens the manager frame
                JOptionPane.showMessageDialog(null, "You have successfully logged in", "Manager", JOptionPane.PLAIN_MESSAGE);
                Manager m = new Manager(result, db);
                manager(m);
                return;
        }
    }

    public void student(Student s){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame jstudent = new JFrame();

        JPanel panel = new JPanel();

        jstudent.setTitle("Student");
        jstudent.setIconImage(image.getImage());
        jstudent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jstudent.setSize(400,450);
        jstudent.setResizable(false);

        panel.setVisible(true);
        panel.setLayout(new GridLayout(6,1));


        //if button is clicked lets the user update their password
        JButton changePass = new JButton();
        changePass.addActionListener(e -> s.updatePass(panel, jstudent));
        changePass.setText("Change Password");
        changePass.setFocusable(false);

        //if button is clicked lets the user view information about their modules
        JButton viewMod = new JButton();
        viewMod.addActionListener(e -> s.viewModules());
        viewMod.setText("View Modules");
        viewMod.setFocusable(false);

        //if button is clicked lets the user view information about their course
        JButton viewCourse = new JButton();
        viewCourse.addActionListener(e -> s.viewCourse());
        viewCourse.setText("View Course Details");
        viewCourse.setFocusable(false);

        //if button is clicked lets the user view note information
        JButton viewNotes = new JButton();
        viewNotes.addActionListener(e -> s.viewNoteInfo());
        viewNotes.setText("View Note Information");
        viewNotes.setFocusable(false);

        //if button is clicked lets the user get select notes to download
        JButton getNotes = new JButton();
        getNotes.addActionListener(e -> s.getNotes1());
        getNotes.setText("Download Notes");
        getNotes.setFocusable(false);

        //if button is clicked closes the student frame and logs the user out, bringing them back to the startup page
        JButton logout = new JButton();
        logout.addActionListener(e -> closeFrame(jstudent));
        logout.setText("Logout");
        logout.setFocusable(false);

        panel.add(changePass);
        panel.add(viewMod);
        panel.add(viewCourse);
        panel.add(viewNotes);
        panel.add(getNotes);
        panel.add(logout);

        jstudent.add(panel);

        jstudent.setVisible(true);
    }

    public void lecturer(Lecturer l){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame jlecturer = new JFrame();

        JPanel panel = new JPanel();

        jlecturer.setTitle("Lecturer");
        jlecturer.setIconImage(image.getImage());
        jlecturer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jlecturer.setSize(400,750);
        jlecturer.setResizable(false);

        //if button is clicked lets the user update their password
        JButton changePass = new JButton();
        changePass.addActionListener(e -> l.updatePass(panel, jlecturer));
        changePass.setText("Change Password");
        changePass.setFocusable(false);

        //if button is clicked lets the user view the module they are assigned to
        JButton viewMod = new JButton();
        viewMod.addActionListener(e -> l.viewModule());
        viewMod.setText("View Module Details");
        viewMod.setFocusable(false);

        //if button is clicked lets the user update the name of their module
        JButton updateName = new JButton();
        updateName.addActionListener(e -> l.updateModuleName());
        updateName.setText("Update Module Name");
        updateName.setFocusable(false);

        //if button is clicked lets the user update the description of their module
        JButton updateDescription = new JButton();
        updateDescription.addActionListener(e -> l.updateModuleDescription());
        updateDescription.setText("Update Module Description");
        updateDescription.setFocusable(false);

        //if button is clicked lets the user update the semester in which their module takes place
        JButton updateSemester = new JButton();
        updateSemester.addActionListener(e -> l.updateModuleSemester());
        updateSemester.setText("Update Module Semester");
        updateSemester.setFocusable(false);

        //if button is clicked lets the user update the credits that their module is worth
        JButton updateCredits = new JButton();
        updateCredits.addActionListener(e -> l.updateModuleCredits());
        updateCredits.setText("Update Module Credits");
        updateCredits.setFocusable(false);

        //if button is clicked lets the user upload lab and lecture marks for their module
        JButton uploadNotes = new JButton();
        uploadNotes.addActionListener(e -> l.uploadNotes1());
        uploadNotes.setText("Uplaod Notes");
        uploadNotes.setFocusable(false);

        //if button is clicked lets the user view the students that are enrolled in their module
        JButton viewStudents = new JButton();
        viewStudents.addActionListener(e -> l.viewStudents());
        viewStudents.setText("View Students in Module");
        viewStudents.setFocusable(false);

        //if button is clicked lets the user set the mark for students in their module
        JButton changeMarks = new JButton();
        changeMarks.addActionListener(e -> l.setMarks());
        changeMarks.setText("Set Student Marks");
        changeMarks.setFocusable(false);

        //if button is clicked closes the lecturer frame and logs the user out, bringing them back to the startup page
        JButton logout = new JButton();
        logout.addActionListener(e -> closeFrame(jlecturer));
        logout.setText("Logout");
        logout.setFocusable(false);

        panel.setVisible(true);
        panel.setLayout(new GridLayout(10,1));

        panel.add(changePass);
        panel.add(viewMod);
        panel.add(updateName);
        panel.add(updateDescription);
        panel.add(updateSemester);
        panel.add(updateCredits);
        panel.add(uploadNotes);
        panel.add(viewStudents);
        panel.add(changeMarks);
        panel.add(logout);

        jlecturer.add(panel);

        jlecturer.setVisible(true);
    }

    public void manager(Manager m){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame jmanager = new JFrame();

        JPanel panel = new JPanel();

        jmanager.setTitle("Manager");
        jmanager.setIconImage(image.getImage());
        jmanager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jmanager.setSize(400,375);
        jmanager.setResizable(false);

        //if button is clicked lets the user update their password
        JButton changePass = new JButton();
        changePass.addActionListener(e -> m.updatePass(panel, jmanager));
        changePass.setText("Change Password");
        changePass.setFocusable(false);

        //if button is clicked takes the user to the account management menu
        JButton accountM = new JButton();
        accountM.addActionListener(e -> accountManagement(panel, jmanager, m));
        accountM.setText("Account Management");
        accountM.setFocusable(false);

        //if button is clicked takes the user to the module/course management menu
        JButton moduleM = new JButton();
        moduleM.addActionListener(e -> moduleManagement(panel, jmanager, m));
        moduleM.setText("Module/Course Management");
        moduleM.setFocusable(false);

        //if button is clicked takes the user to the business rule management menu
        JButton bRules = new JButton();
        bRules.addActionListener(e -> businessRules(panel, jmanager, m));
        bRules.setText("Business Rule Management");
        bRules.setFocusable(false);

        //if button is clicked closes the manager frame and logs the user out, bringing them back to the startup page
        JButton logout = new JButton();
        logout.addActionListener(e -> closeFrame(jmanager));
        logout.setText("Logout");
        logout.setFocusable(false);

        panel.setVisible(true);
        panel.setLayout(new GridLayout(5,1));

        panel.add(changePass);
        panel.add(accountM);
        panel.add(moduleM);
        panel.add(bRules);
        panel.add(logout);

        jmanager.add(panel);

        jmanager.setVisible(true);
    }

    public void accountManagement(JPanel panel1, JFrame frame, Manager m){
        panel1.setVisible(false);

        JPanel panel2 = new JPanel();

        frame.setSize(400,600);

        //if button is clicked lets the user view the workflow of users who have signed up
        JButton viewWF = new JButton();
        viewWF.addActionListener(e -> m.viewWorkflow());
        viewWF.setText("View sign up workflow");
        viewWF.setFocusable(false);

        //if button is clicked lets the user approve users that have signed up
        JButton approveU = new JButton();
        approveU.addActionListener(e -> m.approveUser());
        approveU.setText("Approve User");
        approveU.setFocusable(false);

        //if button is clicked lets the user remove users from the signup workflow that they believe have invalid details
        JButton removeU = new JButton();
        removeU.addActionListener(e -> m.removeFromWorkflow());
        removeU.setText("Remove User From Workflow");
        removeU.setFocusable(false);

        //if button is clicked lets the user view the users that they manage
        JButton viewU = new JButton();
        viewU.addActionListener(e -> m.viewUsers());
        viewU.setText("View Users That I Manage");
        viewU.setFocusable(false);

        //if button is clicked lets the user activate a user
        JButton activateU = new JButton();
        activateU.addActionListener(e -> m.activateUser());
        activateU.setText("Activate User");
        activateU.setFocusable(false);

        //if button is clicked lets the user deactivate a user
        JButton deactivateU = new JButton();
        deactivateU.addActionListener(e -> m.deactivateUser());
        deactivateU.setText("Deactivate User");
        deactivateU.setFocusable(false);

        //if button is clicked lets the user reset another users password
        JButton resetP = new JButton();
        resetP.addActionListener(e -> m.passReset());
        resetP.setText("Reset User Password");
        resetP.setFocusable(false);

        //if button is clicked takes the user back to the main menu
        JButton back = new JButton();
        back.addActionListener(e -> closePanelM(frame, m));
        back.setText("Back");
        back.setFocusable(false);

        panel2.setVisible(true);
        panel2.setLayout(new GridLayout(8,1));

        panel2.add(viewWF);
        panel2.add(approveU);
        panel2.add(removeU);
        panel2.add(viewU);
        panel2.add(activateU);
        panel2.add(deactivateU);
        panel2.add(resetP);
        panel2.add(back);

        frame.add(panel2);
    }

    public void moduleManagement(JPanel panel1, JFrame frame, Manager m) {
        panel1.setVisible(false);

        JPanel panel2 = new JPanel();

        frame.setSize(400, 750);

        //if button is clicked lets the user view all modules
        JButton viewM = new JButton();
        viewM.addActionListener(e -> m.viewModule());
        viewM.setText("View Modules");
        viewM.setFocusable(false);

        //if button is clicked lets the user view all courses
        JButton viewC = new JButton();
        viewC.addActionListener(e -> m.viewCourse());
        viewC.setText("View Courses");
        viewC.setFocusable(false);

        //if button is clicked lets the user add a new module
        JButton addM = new JButton();
        addM.addActionListener(e -> m.addModule());
        addM.setText("Add Module");
        addM.setFocusable(false);

        //if button is clicked lets the user add a new course
        JButton addC = new JButton();
        addC.addActionListener(e -> m.addCourse());
        addC.setText("Add Course");
        addC.setFocusable(false);

        //if button is clicked lets the user update details of a course
        JButton updateC = new JButton();
        updateC.addActionListener(e -> m.updateCourse());
        updateC.setText("Update Course");
        updateC.setFocusable(false);

        //if button is clicked lets the user assign a lecturer to a module
        JButton assignM = new JButton();
        assignM.addActionListener(e -> m.assignModule());
        assignM.setText("Assign Module to Lecturer");
        assignM.setFocusable(false);

        //if button is clicked lets the user enroll a student int a course
        JButton enroll = new JButton();
        enroll.addActionListener(e -> m.enrollStudent());
        enroll.setText("Enroll Student");
        enroll.setFocusable(false);

        //if button is clicked lets the user get results of students they manage
        JButton results = new JButton();
        results.addActionListener(e -> m.getResults());
        results.setText("View Managed Students Results");
        results.setFocusable(false);

        //if button is clicked lets the user set a decision for a user
        JButton decision = new JButton();
        decision.addActionListener(e -> m.setDecision());
        decision.setText("Issue Student Decision");
        decision.setFocusable(false);

        //if button is clicked takes the user back to the main menu
        JButton back = new JButton();
        back.addActionListener(e -> closePanelM(frame, m));
        back.setText("Back");
        back.setFocusable(false);

        panel2.setVisible(true);
        panel2.setLayout(new GridLayout(10,1));

        panel2.add(viewM);
        panel2.add(viewC);
        panel2.add(addM);
        panel2.add(addC);
        panel2.add(updateC);
        panel2.add(assignM);
        panel2.add(enroll);
        panel2.add(results);
        panel2.add(decision);
        panel2.add(back);

        frame.add(panel2);
    }

    public void businessRules(JPanel panel1, JFrame frame, Manager m){
        panel1.setVisible(false);

        JPanel panel2 = new JPanel();

        frame.setSize(400, 450);

        //if button is clicked lets the user view all business rules
        JButton viewRules = new JButton();
        viewRules.addActionListener(e -> m.viewBusinessRules());
        viewRules.setText("View Business Rules");
        viewRules.setFocusable(false);

        //if button is clicked lets the user add a business rules to a course
        JButton addCR = new JButton();
        addCR.addActionListener(e -> m.addRuleCourse());
        addCR.setText("Add Course Business Rule");
        addCR.setFocusable(false);

        //if button is clicked lets the user remove a business rules to a course
        JButton remCR = new JButton();
        remCR.addActionListener(e -> m.remRuleCourse());
        remCR.setText("Remove Course Business Rule");
        remCR.setFocusable(false);

        //if button is clicked lets the user add a business rules to a module
        JButton addMR = new JButton();
        addMR.addActionListener(e -> m.addRuleModule());
        addMR.setText("Add Module Business Rule");
        addMR.setFocusable(false);

        //if button is clicked lets the user remove a business rules to a module
        JButton remMR = new JButton();
        remMR.addActionListener(e -> m.remRuleModule());
        remMR.setText("Remove Module Business Rule");
        remMR.setFocusable(false);

        //if button is clicked takes the user back to the main menu
        JButton back = new JButton();
        back.addActionListener(e -> closePanelM(frame, m));
        back.setText("Back");
        back.setFocusable(false);

        panel2.setVisible(true);
        panel2.setLayout(new GridLayout(6,1));

        panel2.add(viewRules);
        panel2.add(addCR);
        panel2.add(remCR);
        panel2.add(addMR);
        panel2.add(remMR);
        panel2.add(back);

        frame.add(panel2);

    }

    //used to take a manager back to the main menu when they click the back button
    public void closePanelM(JFrame frame, Manager m){
        frame.setVisible(false);
        frame.dispose();
        manager(m);
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

        //when the submit button is clicked pass the values in the form to register2 to be processed
        submit.addActionListener(e -> register2(usernamef.getText(), passwordf.getText(), password2f.getText(), getUsertype(studentButton, lecturerButton, managerButton), emailf.getText(), fnamef.getText(), snamef.getText(), getGender(maleButton, femaleButton, nbButton, otherButton), dobf.getText(), getQualification(phdButton, mscButton, doctorateButton), jregister));

    }

    //gets what user type radio button was selected in the register form
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

    //gets what gender radio button was selected in the register form
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

    //gets what qualification radio button was selected in the register form
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
        String[] details = new String[9];

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
            details[8] = qualification;
        } else {
            details[8] = "N/A";
        }
        db.write("INSERT INTO User (Username,Password,User_Type,Email,First_Name,Surname,Gender,DOB, Qualification, Active,Registered) VALUES ('"+details[0]+"','"+details[1]+"','"+details[2]+"','"+details[3]+"','"+details[4]+"','"+details[5]+"','"+details[6]+"','"+details[7]+"','"+details[8]+"',0,0);");
        JOptionPane.showMessageDialog(null, "You have successfully registered, a manager will approve your account shortly", "Success", JOptionPane.PLAIN_MESSAGE);
        jregister.setVisible(false);
        jregister.dispose();
    }

    //checks that the length of a string is within the database constraints
    public boolean valid_length(String x,String y,int l){
        boolean error = false;
        if(x.length() > l){
            JOptionPane.showMessageDialog(null, "Your " + y + " is over the maximum length of " + l +" characters", "Error", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        return error;
    }

    //checks if an email is in valid email format
    public static boolean valid_email(String email){ 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        return pat.matcher(email).matches(); 
    }

    //checks if a date if in the valid formate
    public static boolean valid_dob_format(String dob){
        String regex = "^([0-9]{4})-(1[0-2]"
                + "|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";

        Pattern pat = Pattern.compile(regex);
        return pat.matcher(dob).matches();
    }

    //finds out of a year is a leap year for date of birth validation
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

    public static void startup() {
        Driver d = new Driver();

        JPanel panel1 = new JPanel();

        ImageIcon image = new ImageIcon("logo.jpg");

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

    public static void main(String[] args){
        startup();
    }
}
