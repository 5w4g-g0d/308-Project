import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class Manager extends User{
    String[] manages;
    
    public Manager(String[] data, DBConnect x){
        db        = x;
        id        = Integer.parseInt(data[0]);
        username  = data[1];
        ManagedBy = Integer.parseInt(data[4]); //With the exception of login(), skip data[2], userType can be determined by class type.
        email     = data[5];
        password  = data[2];
        firstName = data[6];
        lastName  = data[7];
        gender    = data[8];
        dob       = data[9];
        userType  = data[3];

        manages = db.read("SELECT User_Id FROM User WHERE Managed_By_Id = " + id + ";");
    }

    public void viewWorkflow(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Users not yet registered");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1125,400);

        String[] users = db.read("SELECT User_Id FROM User WHERE Registered = '0';");
        String[][] rowData = new String[users.length][9];
        for (int i = 0; i < users.length; i++) {
            String[] result = db.read("SELECT Username, User_Type, Email, First_Name, Surname, Gender, DOB, Qualification FROM User WHERE User_Id = " + users[i] + ";");
            rowData[i][0] = users[i];
            rowData[i][1] = result[0];
            rowData[i][2] = result[1];
            rowData[i][3] = result[2];
            rowData[i][4] = result[3];
            rowData[i][5] = result[4];
            rowData[i][6] = result[5];
            rowData[i][7] = result[6];
            rowData[i][8] = result[7];
        }

        String[] columnNames = {"User Id", "Username", "User Type", "Email", "First Name", "Surname", "Gender", "Date of Birth", "Qualification"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    public void approveUser(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Approve User");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Approve");
        submit.setBounds(140,200,120,50);

        JLabel userId = new JLabel("User Id");
        userId.setBounds(50,10,300,30);

        JTextField userIdf = new JTextField();
        userIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(userId);
        panel.add(userIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String userIdText = userIdf.getText();
            try {
                int user_Id = Integer.parseInt(userIdText);
                approveUser2(user_Id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void approveUser2(Integer x){
        String[] registered = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Registered = 1;" );
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That user Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (registered.length == 0){
            db.write("UPDATE User SET Registered = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Managed_By_Id = "+ id +" WHERE User_Id = "+ x + ";");
            JOptionPane.showMessageDialog(null, "This user has been registered, and activated. You have been assigned as their manager.", "User Registered", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "That user Id has already been registered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeFromWorkflow(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Remove user from workflow");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Remove");
        submit.setBounds(140,200,120,50);

        JLabel userId = new JLabel("User Id");
        userId.setBounds(50,10,300,30);

        JTextField userIdf = new JTextField();
        userIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(userId);
        panel.add(userIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String userIdText = userIdf.getText();
            try {
                int user_Id = Integer.parseInt(userIdText);
                removeFromWorkflow2(user_Id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void removeFromWorkflow2(Integer x){
        String[] registered = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Registered = 1;" );
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That user Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (registered.length == 0){
            db.write("DELETE FROM User WHERE User_Id = " + x + ";");
            JOptionPane.showMessageDialog(null, "This user has been removed from the signup workflow", "User Registered", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "That user Id has already been registered, if you no longer want their account active you can deactivate it", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void viewUsers(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("My Users");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250,400);

        String[][] rowData = new String[manages.length][10];
        for (int i = 0; i < manages.length; i++) {
            String[] result = db.read("SELECT Username, User_Type, Email, First_Name, Surname, Gender, DOB, Qualification, Active FROM User WHERE User_Id = " + manages[i] + ";");
            rowData[i][0] = manages[i];
            rowData[i][1] = result[0];
            rowData[i][2] = result[1];
            rowData[i][3] = result[2];
            rowData[i][4] = result[3];
            rowData[i][5] = result[4];
            rowData[i][6] = result[5];
            rowData[i][7] = result[6];
            rowData[i][8] = result[7];
            rowData[i][9] = result[8];
        }

        String[] columnNames = {"User Id", "Username", "User Type", "Email", "First Name", "Surname", "Gender", "Date of Birth", "Qualification", "Active"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    public void activateUser(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Activate User");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Activate");
        submit.setBounds(140,200,120,50);

        JLabel userId = new JLabel("User Id");
        userId.setBounds(50,10,300,30);

        JTextField userIdf = new JTextField();
        userIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(userId);
        panel.add(userIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String userIdText = userIdf.getText();
            try {
                int user_Id = Integer.parseInt(userIdText);
                activateUser2(user_Id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
    public void activateUser2(Integer x){
        String[] active = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Active = 1;" );
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That user Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (active.length == 0) {
            db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
            JOptionPane.showMessageDialog(null, "User Id: '" + x + "' has been activated.", "Account Activated", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "That user Id is already activated", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void deactivateUser(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Deactivate User");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Deactivate");
        submit.setBounds(140,200,120,50);

        JLabel userId = new JLabel("User Id");
        userId.setBounds(50,10,300,30);

        JTextField userIdf = new JTextField();
        userIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(userId);
        panel.add(userIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String userIdText = userIdf.getText();
            try {
                int user_Id = Integer.parseInt(userIdText);
                deactivateUser2(user_Id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
    public void deactivateUser2(Integer x){
        String[] active = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Active = 0;" );
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That user Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (active.length == 0) {
            db.write("UPDATE User SET Active = " + 0 + " WHERE User_Id = " + x + ";");
            JOptionPane.showMessageDialog(null, "User Id: '"+x+"' has been deactivated.", "Account Deactivated", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "That user Id is already deactivated", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void passReset(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Reset Password");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Reset");
        submit.setBounds(140,200,120,50);

        JLabel userId = new JLabel("User Id");
        userId.setBounds(50,10,300,30);

        JTextField userIdf = new JTextField();
        userIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(userId);
        panel.add(userIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String userIdText = userIdf.getText();
            try {
                int user_Id = Integer.parseInt(userIdText);
                passReset2(user_Id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void passReset2(Integer x){
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That user Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        db.write("UPDATE User SET Password = 'temporaryPassword23' WHERE User_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "The user's password has been reset to \"temporaryPassword23\".", "Password changed", JOptionPane.PLAIN_MESSAGE);
    }

    public void viewModule(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Modules");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750,200);

        String[] modules =  db.read("SELECT Module_Id FROM Module;");
        String[][] rowData = new String[modules.length][7];
        for (int i = 0; i < modules.length; i++) {
            String[] result =  db.read("SELECT * FROM Module WHERE Module_Id = " + modules[i] + ";");
            rowData[i][0] = result[0];
            rowData[i][1] = result[1];
            rowData[i][2] = result[2];
            rowData[i][3] = result[3];
            rowData[i][4] = result[4];
            rowData[i][5] = result[5];
            rowData[i][6] = result[6];
        }
        String[] columnNames = {"Module Id", "Module Name", "Course Id", "Module Description", "Semester", "Credits", "Lecturer Id"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void viewCourse(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Courses");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(625,200);

        String[] courses =  db.read("SELECT Course_Id FROM Course;");
        String[][] rowData = new String[courses.length][5];
        for (int i = 0; i < courses.length; i++) {
            String[] result =  db.read("SELECT * FROM Course WHERE Course_Id = " + courses[i] + ";");
            rowData[i][0] = result[0];
            rowData[i][1] = result[1];
            rowData[i][2] = result[2];
            rowData[i][3] = result[3];
            rowData[i][4] = result[4];
        }
        String[] columnNames = {"Course Id", "Course Name", "Course Type", "Department Id", "Course Description"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void assignModule(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Assign Module");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Assign");
        submit.setBounds(140,200,120,50);

        JLabel lid = new JLabel("Lecturer Id");
        lid.setBounds(50,10,300,30);
        JLabel mid = new JLabel("Module id");
        mid.setBounds(50,90,300,30);

        JTextField lidf = new JTextField();
        lidf.setBounds(50,40,300,30);
        JTextField midf = new JTextField();
        midf.setBounds(50,120,300,30);

        panel.add(lid);
        panel.add(lidf);
        panel.add(mid);
        panel.add(midf);
        panel.add(submit);

        frame.add(panel);

        submit.addActionListener(e -> {
            Integer lecturerId = Integer.parseInt(lidf.getText());
            Integer moduleId = Integer.parseInt(midf.getText());
            try {
                assignModule2(lecturerId, moduleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for both ids", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public void assignModule2(Integer lecturer, Integer module){
        String[] lecturers = db.read("SELECT Lecturer_Id FROM Module WHERE Module_Id = '"+module+"';");
        String[] lexists = db.read("SELECT * FROM User WHERE User_Id = " + lecturer + ";");
        if (lexists.length == 0){
            JOptionPane.showMessageDialog(null, "That lecturer Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] mexists = db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
        if (mexists.length == 0){
            JOptionPane.showMessageDialog(null, "That module Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(lecturers.length == 0){
            db.write("UPDATE Module SET Lecturer_Id = " + lecturer + " WHERE Module_Id = " + module + ";");
            JOptionPane.showMessageDialog(null, "Lecturer Id: '"+lecturer+"' has been assigned to Module: '"+module+"'.", "Lecturer Assigned", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "That module already has a lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addModule(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Add Module");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,500);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Add Module");
        submit.setBounds(140,400,120,50);

        JLabel name = new JLabel("Module Name");
        name.setBounds(50,10,300,30);
        JLabel description = new JLabel("Module description");
        description.setBounds(50,90,300,30);
        JLabel credits = new JLabel("Credits");
        credits.setBounds(50,170,300,30);
        JLabel id = new JLabel("Course Id");
        id.setBounds(50,250,300,30);


        JTextField namef = new JTextField();
        namef.setBounds(50,40,300,30);
        JTextField descriptionf = new JTextField();
        descriptionf.setBounds(50,120,300,30);
        JRadioButton credits10 = new JRadioButton();
        credits10.setText("10 Credits");
        credits10.setBounds(50,200,100,30);
        JRadioButton credits20 = new JRadioButton();
        credits20.setText("20 Credits");
        credits20.setBounds(150,200,100,30);
        JTextField idf = new JTextField();
        idf.setBounds(50,280,300,30);

        panel.add(submit);
        panel.add(name);
        panel.add(namef);
        panel.add(description);
        panel.add(descriptionf);
        panel.add(credits10);
        panel.add(credits20);
        panel.add(id);
        panel.add(idf);

        frame.add(panel);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(credits10);
        BG1.add(credits20);

        submit.addActionListener(e -> {
            String courseIdText = idf.getText();
            try {
                int courseId = Integer.parseInt(courseIdText);
                addModule2(namef.getText(), descriptionf.getText(), credits10, credits20, courseId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid course ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void addModule2(String name, String desc, JRadioButton c10, JRadioButton c20, int id){
        Driver d = new Driver();
        String[] exists = db.read("SELECT * FROM Course WHERE Course_Id = " + id + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That course Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        exists = db.read("SELECT Module_Name FROM Module WHERE Module_Name = '" + name + "';");
        if (exists.length != 0){
            JOptionPane.showMessageDialog(null, "That module name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean error = d.valid_length(name,"module name",30);
        if (error) {
            return;
        }

        error = d.valid_length(desc,"module description",200);
        if (error) {
            return;
        }
        if (c10.isSelected()) {
            db.write("INSERT INTO Module (Module_Name, Module_Description, Credits) VALUES ('" + name + "', '" + desc + "', 10);");
            JOptionPane.showMessageDialog(null, "Module '"+name+"' has been added.", "Module Added", JOptionPane.PLAIN_MESSAGE);
            String[] module = db.read("SELECT Module_Id FROM Module WHERE Module_Name = '" + name + "';");
            assignCourse(Integer.parseInt(module[0]), id);
        } else if (c20.isSelected()) {
            db.write("INSERT INTO Module (Module_Name, Module_Description, Credits) VALUES ('" + name + "', '" + desc + "', 20);");
            JOptionPane.showMessageDialog(null, "Module '"+name+"' has been added.", "Module Added", JOptionPane.PLAIN_MESSAGE);
            String[] module = db.read("SELECT Module_Id FROM Module WHERE Module_Name = '" + name + "';");
            assignCourse(Integer.parseInt(module[0]), id);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a value for credits", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void assignCourse(Integer module, Integer course){
        db.write("UPDATE Module SET Course_Id = " + course + " WHERE Module_Id = " + module + ";");
        String[] rules = db.read("SELECT Rule_Id FROM Course_Rules WHERE Course_Id = '"+course+"';");
        for(int i = 0; i < rules.length; i++){
            db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ('"+module+"', '"+rules[i]+"');");
        }
        JOptionPane.showMessageDialog(null, "Module '"+module+"' has been assigned to Course '"+course+"'.", "Course Added", JOptionPane.PLAIN_MESSAGE);
    }

    public void addCourse(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Add Course");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,500);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Add Course");
        submit.setBounds(140,400,120,50);

        JLabel name = new JLabel("Course Name");
        name.setBounds(50,10,300,30);
        JLabel description = new JLabel("Course description");
        description.setBounds(50,90,300,30);
        JLabel id = new JLabel("Department Id");
        id.setBounds(50,170,300,30);
        JLabel type = new JLabel("Course type");
        type.setBounds(50,250,300,30);

        JTextField namef = new JTextField();
        namef.setBounds(50,40,300,30);
        JTextField descriptionf = new JTextField();
        descriptionf.setBounds(50,120,300,30);
        JTextField idf = new JTextField();
        idf.setBounds(50,200,300,30);
        JRadioButton under = new JRadioButton();
        under.setText("Undergraduate");
        under.setBounds(50,280,125,30);
        JRadioButton post = new JRadioButton();
        post.setText("Postgraduate");
        post.setBounds(200,280,125,30);

        panel.add(submit);
        panel.add(name);
        panel.add(namef);
        panel.add(description);
        panel.add(descriptionf);
        panel.add(id);
        panel.add(idf);
        panel.add(type);
        panel.add(under);
        panel.add(post);

        frame.add(panel);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(under);
        BG1.add(post);

        submit.addActionListener(e -> {
            String deptIdText = idf.getText();
            try {
                int deptId = Integer.parseInt(deptIdText);
                addCourse2(namef.getText(), descriptionf.getText(), deptId, under, post);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid department ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void addCourse2(String name, String desc, int id, JRadioButton under, JRadioButton post){
        Driver d = new Driver();

        String[] exists = db.read("SELECT * FROM Department WHERE Department_Id = " + id + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That department Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean error = d.valid_length(name,"course name",30);
        if (error) {
            return;
        }

        error = d.valid_length(desc,"course description",200);
        if (error) {
            return;
        }
        if (under.isSelected()) {
            db.write("INSERT INTO Course (Course_Name, Course_Description, Department_Id, Course_Type) VALUES ('" + name + "', '" + desc + "', " + id + ", 'Undergraduate');");
            JOptionPane.showMessageDialog(null, "Course '"+name+"' has been added.", "Course Added", JOptionPane.PLAIN_MESSAGE);
        } else if (post.isSelected()) {
            db.write("INSERT INTO Course (Course_Name, Course_Description, Department_Id, Course_Type) VALUES ('" + name + "', '" + desc + "', " + id + ", 'Postgraduate');");
            JOptionPane.showMessageDialog(null, "Course '"+name+"' has been added.", "Course Added", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Please select an option for the course type", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCourse(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("update Course");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update Course");
        submit.setBounds(140,300,120,50);

        JLabel id = new JLabel("Course Id");
        id.setBounds(50,10,300,30);
        JLabel description = new JLabel("Course description");
        description.setBounds(50,90,300,30);

        JTextField idf = new JTextField();
        idf.setBounds(50,40,300,30);
        JTextField descriptionf = new JTextField();
        descriptionf.setBounds(50,120,300,30);

        panel.add(submit);
        panel.add(id);
        panel.add(idf);
        panel.add(description);
        panel.add(descriptionf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String courseIdText = idf.getText();
            try {
                int courseId = Integer.parseInt(courseIdText);
                updateCourse2(descriptionf.getText(), courseId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid department ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void updateCourse2(String desc, Integer x){
        Driver d = new Driver();

        String[] exists = db.read("SELECT * FROM Course WHERE Course_Id = " + x + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That course Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean error = d.valid_length(desc,"course description",200);
        if (error) {
            return;
        }
        db.write("UPDATE Course SET Course_Description = '" + desc + "' WHERE Course_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "Course '"+x+"' has been updated'.", "Description Updated", JOptionPane.PLAIN_MESSAGE);
    }

    public void enrollStudent(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Enroll Student");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Enroll");
        submit.setBounds(140,200,120,50);

        JLabel sid = new JLabel("Student Id");
        sid.setBounds(50,10,300,30);
        JLabel cid = new JLabel("Course id");
        cid.setBounds(50,90,300,30);

        JTextField sidf = new JTextField();
        sidf.setBounds(50,40,300,30);
        JTextField cidf = new JTextField();
        cidf.setBounds(50,120,300,30);

        panel.add(sid);
        panel.add(sidf);
        panel.add(cid);
        panel.add(cidf);
        panel.add(submit);

        frame.add(panel);

        submit.addActionListener(e -> {
            Integer studentId = Integer.parseInt(sidf.getText());
            Integer courseId = Integer.parseInt(cidf.getText());
            try {
                enrollStudent2(studentId, courseId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for both ids", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void enrollStudent2(Integer student, Integer course){
        String[] exists = db.read("SELECT * FROM User WHERE User_Id = " + student + " AND User_type = 'Student';");
        if(exists.length == 0) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Student Id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
        if (exists.length == 0){
            JOptionPane.showMessageDialog(null, "That course Id does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //loop for each module in a course
        String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = "+ course+ ";");
        for(int i = 0; i < modules.length; i++){
            //find existing attempts for this module, if any
            Integer atts = 0;
            String[] attempts = db.read("SELECT Student_Attempt_No FROM Student_Module WHERE Student_Id = "+student+" AND Module_Id = "+modules[i]+";");
            
            //what rules apply to this module?
            String[] rules = db.read("SELECT Rule_Id FROM Module_Rules WHERE Module_Id = '"+modules[i]+"';");
            Boolean attemptsRule = false;
            Boolean attendanceRule = false;
            for(int j = 0;  j < rules.length; j++){
                if(Integer.parseInt(rules[j]) == 1){
                    attemptsRule = true;
                }else if(Integer.parseInt(rules[j]) == 2){
                    attendanceRule = true;
                }
            }

            //check if module is full (when rule is applied)
            if(attendanceRule){
                String[] students = db.read("SELECT Student_Id FROM Student_Module WHERE Module_Id = '"+modules[i]+"';");
                if(students.length > 29){
                    JOptionPane.showMessageDialog(null, "Module "+modules[i]+" is full. This student cannot be enrolled.", "Error", JOptionPane.ERROR_MESSAGE);
                    //N.B. if a student fails this check, and is not enrolled, they have been enrolled into any modules before, but not any after.
                    return;
                }
            }
            
            //Update attempts, where it is allowed
            if(attempts.length == 0){
                atts = 1;
            }else if(Integer.parseInt(attempts[0]) == 2 && attemptsRule){
                JOptionPane.showMessageDialog(null, "This student has exceeded the maximum allowed attempts for module "+modules[i]+".", "Error", JOptionPane.ERROR_MESSAGE);
                //N.B. if a student fails this check, and is not enrolled, they have been enrolled into any modules before, but not any after.
                return;
            }else{
                atts = Integer.parseInt(attempts[0]) + 1;
            }
            //add student to this module
            exists = db.read("SELECT * FROM Student_Module WHERE Module_Id = " + modules[i] + " AND Student_Id = " + student + ";");
            if (exists.length == 0){
                db.write("INSERT INTO Student_Module (Module_Id, Student_Id, Student_Attempt_No) VALUES (" + modules[i] + ", " + student + ", " + atts + ");");
            } else {
                db.write("UPDATE Student_Module SET Student_Attempt_No = " + atts + ",Exam_Mark = NULL , Lab_Mark = NULL, Decision = NULL WHERE  Module_Id = " + modules[i] + " AND Student_Id = " + student + ";");
            }
        }
        //Success message, only when student is enrolled into EVERY module for a course.
        String[] courseName = db.read("SELECT Course_Name FROM Course WHERE Course_Id = '"+course+"';");
        JOptionPane.showMessageDialog(null, "Success!\n Student "+student+"has been added to modules in course"+courseName[0]+".", "Success", JOptionPane.PLAIN_MESSAGE);
    }

    public void getResults(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Student Results");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750,400);

        String[] students = db.read("SELECT sm.Student_Id FROM Student_Module sm INNER JOIN User u ON sm.Student_Id = u.User_Id WHERE u.Managed_By_Id = " + id + ";");
        String[][] rowData = new String[students.length][6];
        for (int i = 0; i < students.length; i++) {
            String[] result = db.read("SELECT * FROM Student_Module WHERE Student_Id = " + students[i] + ";");
            rowData[i][0] = result[0];
            rowData[i][1] = result[1];
            rowData[i][2] = result[2];
            rowData[i][3] = result[3];
            rowData[i][4] = result[4];
            rowData[i][5] = result[5];
        }

        String[] columnNames = {"Module Id", "Student Id", "Student Attempt No", "Exam Mark", "Lab Mark", "Decision"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    public void setDecision(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Student Decision");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Set Decision");
        submit.setBounds(140,300,120,50);

        JLabel sid = new JLabel("Student Id");
        sid.setBounds(50,10,300,30);
        JLabel mid = new JLabel("Module Id");
        mid.setBounds(50,90,300,30);
        JLabel dec = new JLabel("Decision");
        dec.setBounds(50,170,300,30);

        JTextField sidf = new JTextField();
        sidf.setBounds(50,40,300,30);
        JTextField midf = new JTextField();
        midf.setBounds(50,120,300,30);
        JRadioButton award = new JRadioButton();
        award.setText("Award");
        award.setBounds(50,200,100,30);
        JRadioButton resit = new JRadioButton();
        resit.setText("Resit");
        resit.setBounds(150,200,100,30);
        JRadioButton withdraw = new JRadioButton();
        withdraw.setText("Withdraw");
        withdraw.setBounds(250,200,100,30);

        panel.add(submit);
        panel.add(sid);
        panel.add(sidf);
        panel.add(mid);
        panel.add(midf);
        panel.add(dec);
        panel.add(award);
        panel.add(resit);
        panel.add(withdraw);

        frame.add(panel);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(award);
        BG1.add(resit);
        BG1.add(withdraw);

        submit.addActionListener(e -> {
            int studentId = Integer.parseInt(sidf.getText());
            int moduleId = Integer.parseInt(midf.getText());
            try {
                setDecision2(studentId, moduleId, award, resit, withdraw);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid ids, the student must have marks for this module", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
        

    public void setDecision2(Integer student, Integer module, JRadioButton a, JRadioButton r, JRadioButton w){
        String[] exists = db.read("SELECT Student_Id FROM Student_Module WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        if(exists.length == 0) {
            JOptionPane.showMessageDialog(null, "Please enter a valid id combination", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String decision;
        if(a.isSelected()) {
            decision = "award";
        } else if(r.isSelected()) {
            decision = "resit";
        } else if(w.isSelected()){
            decision = "withdraw";
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a decision", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] rules = db.read("SELECT Rule_Id FROM Module_Rules WHERE Module_Id = " + module + ";");
        boolean PassRule = false;
        for(String rule:rules){
            if(Integer.parseInt(rule) == 3){
                PassRule = true;
            }
        }
        String[] marks = db.read("SELECT Exam_Mark, Lab_Mark FROM Student_Module WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        Integer totalMark = (Integer.parseInt(marks[0]) + Integer.parseInt(marks[1]));
        if(decision.equals("award")){
            if(totalMark < 100){
                JOptionPane.showMessageDialog(null, "This student has not achieved the required mark to be awarded this module.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }else if(totalMark < 80 && PassRule){
                JOptionPane.showMessageDialog(null, "This student has not achieved the required mark to be awarded this module.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        db.write("UPDATE Student_Module SET Decision = '" + decision + "' WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student Id: " + student + " has been given the decision: '" + decision + "' for module: " + module + ".", "Decision Made", JOptionPane.PLAIN_MESSAGE);
    }

    public void viewBusinessRules(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Business Rules");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        String[] rules = db.read("SELECT Id FROM Business_Rules;");
        String[][] rowData = new String[rules.length][2];
        for (int i = 0; i < rules.length; i++) {
            String[] result = db.read("SELECT * FROM Business_Rules WHERE Id = " + rules[i] + ";");
            rowData[i][0] = result[0];
            rowData[i][1] = result[1];
        }

        String[] columnNames = {"Rule Id", "Description"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void addRuleCourse() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Add Course Rule");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Add");
        submit.setBounds(140,200,120,50);

        JLabel cId = new JLabel("Course Id");
        cId.setBounds(50,10,300,30);
        JLabel rId = new JLabel("Rule Id");
        rId.setBounds(50,90,300,30);

        JTextField cIdf = new JTextField();
        cIdf.setBounds(50,40,300,30);
        JTextField rIdf = new JTextField();
        rIdf.setBounds(50,120,300,30);


        panel.add(submit);
        panel.add(cId);
        panel.add(cIdf);
        panel.add(rId);
        panel.add(rIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String cIdText = cIdf.getText();
            String rIdText = rIdf.getText();
            try {
                int courseId = Integer.parseInt(cIdText);
                int ruleId = Integer.parseInt(rIdText);
                addRuleCourse2(courseId, ruleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid ids.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void addRuleCourse2(Integer course, Integer rule){
        String[] exists = db.read("SELECT Course_Id FROM Course WHERE Course_Id = "+course+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Course does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Id FROM Business_Rules WHERE Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Business Rule does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Course_Id FROM Course_Rules WHERE Course_Id = "+course+" AND Rule_Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Rule "+rule+" is already applied to Course "+course+".", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("INSERT INTO Course_Rules (Course_Id, Rule_Id) VALUES ("+course+", "+rule+");");
            String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = "+course+";");
            for(String module: modules){
                String[] moduleExists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = "+module+" AND Rule_Id = "+rule+";");
                if(moduleExists.length != 0){
                    JOptionPane.showMessageDialog(null, "Rule "+rule+" is already applied to Module "+module+".", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ("+module+", "+rule+");");
                }
            }
            JOptionPane.showMessageDialog(null, "Course '"+course+"' has been given rule '"+rule+"'.", "Course Rule Added", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void remRuleCourse() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Remove Course Rule");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Remove");
        submit.setBounds(140,200,120,50);

        JLabel cId = new JLabel("Course Id");
        cId.setBounds(50,10,300,30);
        JLabel rId = new JLabel("Rule Id");
        rId.setBounds(50,90,300,30);

        JTextField cIdf = new JTextField();
        cIdf.setBounds(50,40,300,30);
        JTextField rIdf = new JTextField();
        rIdf.setBounds(50,120,300,30);


        panel.add(submit);
        panel.add(cId);
        panel.add(cIdf);
        panel.add(rId);
        panel.add(rIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String cIdText = cIdf.getText();
            String rIdText = rIdf.getText();
            try {
                int courseId = Integer.parseInt(cIdText);
                int ruleId = Integer.parseInt(rIdText);
                remRuleCourse2(courseId, ruleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid ids.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void remRuleCourse2(Integer course, Integer rule){
        String[] exists = db.read("SELECT Course_Id FROM Course WHERE Course_Id = "+course+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Course does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Id FROM Business_Rules WHERE Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Business Rule does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Course_Id FROM Course_Rules WHERE Course_Id = "+course+" AND Rule_Id = "+rule+";");
        if(exists.length == 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is not applied to Course '"+course+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("DELETE FROM Course_Rules WHERE Course_Id = "+course+" AND Rule_Id = "+rule+";");
            String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = "+course+";");
            for(String module: modules){
                db.write("DELETE FROM Module_Rules WHERE Module_Id = "+module+" AND Rule_Id = "+rule+";");
            }
            JOptionPane.showMessageDialog(null, "Course '"+course+"' no longer has Rule '"+rule+"'.", "Course Rule Removed", JOptionPane.PLAIN_MESSAGE);
        }
    }


    public void addRuleModule() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Add Module Rule");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Add");
        submit.setBounds(140,200,120,50);

        JLabel cId = new JLabel("Module Id");
        cId.setBounds(50,10,300,30);
        JLabel rId = new JLabel("Rule Id");
        rId.setBounds(50,90,300,30);

        JTextField cIdf = new JTextField();
        cIdf.setBounds(50,40,300,30);
        JTextField rIdf = new JTextField();
        rIdf.setBounds(50,120,300,30);


        panel.add(submit);
        panel.add(cId);
        panel.add(cIdf);
        panel.add(rId);
        panel.add(rIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String cIdText = cIdf.getText();
            String rIdText = rIdf.getText();
            try {
                int courseId = Integer.parseInt(cIdText);
                int ruleId = Integer.parseInt(rIdText);
                addRuleModule2(courseId, ruleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid ids.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }


    public void addRuleModule2(Integer module, Integer rule){
        String[] exists = db.read("SELECT Module_Id FROM Module WHERE Module_Id = "+module+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Module does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Id FROM Business_Rules WHERE Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Business Rule does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = "+module+" AND Rule_Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is already applied to Module '"+module+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ("+module+", "+rule+");");
            JOptionPane.showMessageDialog(null, "Module '"+module+"' has been given rule '"+rule+"'.", "Module Rule Added", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void remRuleModule() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Remove Module Rule");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Remove");
        submit.setBounds(140,200,120,50);

        JLabel cId = new JLabel("Module Id");
        cId.setBounds(50,10,300,30);
        JLabel rId = new JLabel("Rule Id");
        rId.setBounds(50,90,300,30);

        JTextField cIdf = new JTextField();
        cIdf.setBounds(50,40,300,30);
        JTextField rIdf = new JTextField();
        rIdf.setBounds(50,120,300,30);


        panel.add(submit);
        panel.add(cId);
        panel.add(cIdf);
        panel.add(rId);
        panel.add(rIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String cIdText = cIdf.getText();
            String rIdText = rIdf.getText();
            try {
                int courseId = Integer.parseInt(cIdText);
                int ruleId = Integer.parseInt(rIdText);
                remRuleModule2(courseId, ruleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid ids.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void remRuleModule2(Integer module, Integer rule){
        String[] exists = db.read("SELECT Module_Id FROM Module WHERE Module_Id = "+module+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Module does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Id FROM Business_Rules WHERE Id = "+rule+";");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Business Rule does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        exists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = "+module+" AND Rule_Id = "+rule+";");
        if(exists.length == 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is not applied to Module '"+module+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("DELETE FROM Module_Rules WHERE Module_Id = "+module+" AND Rule_Id = "+rule+";");
            JOptionPane.showMessageDialog(null, "Module "+module+" no longer has Rule "+rule+".", "Course Rule Removed", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void newCourse(Integer id, String name, String desc){
        db.write("INSERT INTO Course VALUES (" + id + ", '" + name + "', '" + desc + "');");
        JOptionPane.showMessageDialog(null, "Course '"+name+"' has been added.", "Course Added", JOptionPane.PLAIN_MESSAGE);
    }

    public String[] getCourseDetails(Integer course){
        return db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
    }

    public String[] getModuleDetails(Integer module){
        return db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
    }
}
