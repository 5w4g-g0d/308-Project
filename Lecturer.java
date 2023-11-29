import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Lecturer extends User {
    String[] module;
    
    public Lecturer(String[] data, DBConnect x){
        db        = x;
        id        = Integer.parseInt(data[0]);
        username  = data[1];
        ManagedBy = Integer.parseInt(data[4]);
        email     = data[5];
        password  = data[2];
        firstName = data[6];
        lastName  = data[7];
        gender    = data[8];
        dob       = data[9];
        userType  = data[3];
        
        module = db.read("SELECT Module_Id FROM Module WHERE Lecturer_Id = " + id + ";");
    }

    public String[] getModule(){
        if(module.length == 0){
            return null;
        }
        return module;
    }

    public void viewModule(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Module");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750,200);

        if (module.length == 0) {
            JOptionPane.showMessageDialog(null, "No modules found for this lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[][] rowData = new String[1][6];
        String[] result = db.read("SELECT Module_Name, Course_Id, Module_Description, Semester, Credits FROM Module WHERE Module_Id = " + module[0] + ";");
        rowData[0][0] = module[0];
        rowData[0][1] = result[0];
        rowData[0][2] = result[1];
        rowData[0][3] = result[2];
        rowData[0][4] = result[3];
        rowData[0][5] = result[4];

        String[] columnNames = {"Module Id", "Module Name", "Course Id", "Module Description", "Semester", "Credits"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void updateModuleName(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Update Name");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update");
        submit.setBounds(140,200,120,50);

        JLabel name = new JLabel("Module Name");
        name.setBounds(50,10,300,30);

        JTextField namef = new JTextField();
        namef.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(name);
        panel.add(namef);

        frame.add(panel);

        submit.addActionListener(e -> updateModuleName2(namef.getText()));

    }

    public void updateModuleName2(String name){
        Driver d = new Driver();

        boolean error = d.valid_length(name,"module name",30);
        if (error) {
            return;
        } else {
            db.write("UPDATE Module SET Module_Name = '" + name + "' WHERE Module_Id = " + module[0] + ";");
            JOptionPane.showMessageDialog(null, "Module updated.", "Complete", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void updateModuleDescription(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Update Description");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update");
        submit.setBounds(140,200,120,50);

        JLabel description = new JLabel("Module Description");
        description.setBounds(50,10,300,30);

        JTextField descriptionf = new JTextField();
        descriptionf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(description);
        panel.add(descriptionf);

        frame.add(panel);

        submit.addActionListener(e -> updateModuleDescription2(descriptionf.getText()));

    }

    public void updateModuleDescription2(String description){
        Driver d = new Driver();

        boolean error = d.valid_length(description,"module description",250);
        if (error) {
            return;
        } else {
            db.write("UPDATE Module SET Module_Description = '" + description + "' WHERE Module_Id = " + module[0] + ";");
            JOptionPane.showMessageDialog(null, "Module updated.", "Complete", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void updateModuleSemester(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Update Semester");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update");
        submit.setBounds(140,200,120,50);

        JLabel semester = new JLabel("Module Semester");
        semester.setBounds(50,10,300,30);

        JRadioButton semester1B = new JRadioButton();
        semester1B.setText("Semester 1");
        semester1B.setBounds(50,40,100,30);
        JRadioButton semester2B = new JRadioButton();
        semester2B.setText("Semester 2");
        semester2B.setBounds(150,40,100,30);

        panel.add(submit);
        panel.add(semester);
        panel.add(semester1B);
        panel.add(semester2B);

        frame.add(panel);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(semester1B);
        BG1.add(semester2B);

        submit.addActionListener(e -> updateModuleSemester2(semester1B, semester2B));

    }

    public void updateModuleSemester2(JRadioButton s1, JRadioButton s2){
        int semester;
        if (s1.isSelected()) {
            semester = 1;
        } else if (s2.isSelected()) {
            semester = 2;
        } else {
            JOptionPane.showMessageDialog(null, "You haven't selected an option", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        db.write("UPDATE Module SET Semester = '" + semester + "' WHERE Module_Id = " + module[0] + ";");
        JOptionPane.showMessageDialog(null, "Module updated.", "Complete", JOptionPane.PLAIN_MESSAGE);
    }

    public void updateModuleCredits(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Update Credits");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update");
        submit.setBounds(140,200,120,50);

        JLabel credits = new JLabel("Module Credits");
        credits.setBounds(50,10,300,30);

        JRadioButton credits10 = new JRadioButton();
        credits10.setText("10 Credits");
        credits10.setBounds(50,40,100,30);
        JRadioButton credits20 = new JRadioButton();
        credits20.setText("20 Credits");
        credits20.setBounds(150,40,100,30);

        panel.add(submit);
        panel.add(credits);
        panel.add(credits10);
        panel.add(credits20);

        frame.add(panel);

        ButtonGroup BG1 = new ButtonGroup();
        BG1.add(credits10);
        BG1.add(credits20);

        submit.addActionListener(e -> updateModuleCredits2(credits10, credits20));

    }

    public void updateModuleCredits2(JRadioButton c10, JRadioButton c20){
        int credits;
        if (c10.isSelected()) {
            credits = 10;
        } else if (c20.isSelected()) {
            credits = 20;
        } else {
            JOptionPane.showMessageDialog(null, "You haven't selected an option", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        db.write("UPDATE Module SET Credits = '" + credits + "' WHERE Module_Id = " + module[0] + ";");
        JOptionPane.showMessageDialog(null, "Module updated.", "Complete", JOptionPane.PLAIN_MESSAGE);
    }

    public void uploadNotes1(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Update Name");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,500);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Upload");
        submit.setBounds(140,400,120,50);

        JLabel description = new JLabel("Note Description");
        description.setBounds(50,10,300,30);
        JLabel data = new JLabel("Note Data");
        data.setBounds(50,90,300,30);

        JTextField descriptionf = new JTextField();
        descriptionf.setBounds(50,40,300,30);
        JTextField dataf = new JTextField();
        dataf.setBounds(50,120,300,200);

        panel.add(submit);
        panel.add(description);
        panel.add(data);
        panel.add(descriptionf);
        panel.add(dataf);

        frame.add(panel);

        submit.addActionListener(e -> uploadNotes2(descriptionf.getText(), data.getText()));
    }
    public void uploadNotes2(String desc, String content){
        Driver d = new Driver();

        boolean error = d.valid_length(desc,"module description",200);
        if (error) {
            return;
        }
        db.write("INSERT INTO Notes (Module_Id, Description, Data) VALUES (" + module[0] + ", '" + desc + "', '"+ content +"');");
        JOptionPane.showMessageDialog(null, "Notes uploaded.", "Notes Uploaded", JOptionPane.PLAIN_MESSAGE);
    }

    public void viewStudents() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Students in Module");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750,400);

        String[] result = db.read("SELECT Student_Id FROM Student_Module WHERE Module_Id = " + module[0] + ";");
        String[][] rowData = new String[result.length][6];
        for (int i = 0; i < result.length; i++) {
            String[] result2 = db.read("SELECT First_Name, Surname FROM User WHERE User_Id = " + result[i] + ";");
            String[] result3 = db.read("SELECT Exam_Mark, Lab_Mark, Decision FROM Student_Module WHERE Student_Id = " + result[i] + ";");
            rowData[i][0] = result[i];
            rowData[i][1] = result2[0];
            rowData[i][2] = result2[1];
            rowData[i][3] = result3[0];
            rowData[i][4] = result3[1];
            rowData[i][5] = result3[2];
        }

        String[] columnNames = {"Student Id", "First Name", "Surname", "Exam Mark", "Lab Mark", "Decision"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    public void setMarks(){
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Students in Module");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,500);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Update");
        submit.setBounds(140,300,120,50);

        JLabel id = new JLabel("Student Id");
        id.setBounds(50,10,300,30);
        JLabel exam = new JLabel("Exam Mark");
        exam.setBounds(50,90,300,30);
        JLabel lab = new JLabel("Lab Mark");
        lab.setBounds(50,180,300,30);

        JTextField idf = new JTextField();
        idf.setBounds(50,40,300,30);
        JTextField examf = new JTextField();
        examf.setBounds(50,120,300,30);
        JTextField labf = new JTextField();
        labf.setBounds(50,210,300,30);

        panel.add(id);
        panel.add(idf);
        panel.add(exam);
        panel.add(examf);
        panel.add(lab);
        panel.add(labf);
        panel.add(submit);

        frame.add(panel);

        submit.addActionListener(e -> {
            Integer studentId = Integer.parseInt(idf.getText());
            Integer examMark = Integer.parseInt(examf.getText());
            Integer labMark = Integer.parseInt(labf.getText());
            try {
                setExamMark(studentId, examMark, Integer.parseInt(module[0]));
                setLabMark(studentId, labMark, Integer.parseInt(module[0]));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for id and mark", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    //ALL methods pertaining to marks will need to be adjusted for Lab and Exam marks.
    public void setExamMark(Integer student, Integer mark, Integer module){
        String[] exists = db.read("SELECT Student_Id FROM Student_Module WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        if(exists.length == 0) {
            JOptionPane.showMessageDialog(null, "That student is not in your module", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mark > 100 || mark < 0){
            JOptionPane.showMessageDialog(null, "That mark is invalid", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        db.write("UPDATE Student_Module SET Exam_Mark = " + mark + " WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student '"+student+"' has been given a '"+mark+"' for the module '"+module+"' exam.", "Mark Assigned", JOptionPane.PLAIN_MESSAGE);
    }

    public void setLabMark(Integer student, Integer mark, Integer module){
        String[] exists = db.read("SELECT Student_Id FROM Student_Module WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        if(exists.length == 0) {
            JOptionPane.showMessageDialog(null, "That student is not in your module", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mark > 100 || mark < 0){
            JOptionPane.showMessageDialog(null, "That mark is invalid", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        db.write("UPDATE Student_Module SET Lab_Mark = " + mark + " WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student '"+student+"' has been given a '"+mark+"' for the module '"+module+"' lab.", "Mark Assigned", JOptionPane.PLAIN_MESSAGE);
    }

}
