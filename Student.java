import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Student extends User {
    String[] modules;
    public String[][] testData;

    public Student(String[] data, DBConnect x){
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

        modules = db.read("SELECT Module_Id FROM Student_Module WHERE Student_Id = " + id + ";");
    }

    public void viewModules() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Modules");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000,400);

        String[][] rowData = new String[modules.length][8];
        for (int i = 0; i < modules.length; i++) {
            String[] result = db.read("SELECT Module_Name, Module_Description, Semester, Credits FROM Module WHERE Module_Id = " + modules[i] + ";");
            String[] result2 = db.read("SELECT Exam_Mark, Lab_Mark, Decision FROM Student_Module WHERE Module_Id = " + modules[i] + ";");
            rowData[i][0] = modules[i];
            rowData[i][1] = result[0];
            rowData[i][2] = result[1];
            rowData[i][3] = result[2];
            rowData[i][4] = result[3];
            rowData[i][5] = result2[0];
            rowData[i][6] = result2[1];
            rowData[i][7] = result2[2];

            this.testData = rowData;

        }

        String[] columnNames = {"Module Id", "Module Name", "Module Description", "Semester", "Credits", "Exam Mark", "Lab Mark", "Decision"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        this.testData = rowData;
    }

    public void viewCourse() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Course Details");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(625,200);

        String[][] rowData = new String[1][5];
        String[] courseid = db.read("SELECT Course_Id FROM Module WHERE Module_Id = " + modules[0] + ";");
        String[] result = db.read("SELECT Course_Id, Course_Name, Course_Type, Course_Description, Department_Id FROM Course WHERE Course_Id = " + courseid[0] + ";");
        String[] result2 = db.read("SELECT Department_Name FROM Department WHERE Department_Id = " + result[4] + ";");
        rowData[0][0] = result[0];
        rowData[0][1] = result[1];
        rowData[0][2] = result[2];
        rowData[0][3] = result[3];
        rowData[0][4] = result2[0];

        String[] columnNames = {"Course Id", "Course Name", "Course Type", "Course Description", "Department Name"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        this.testData = rowData;
    }

    public void viewNoteInfo() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("View Note Information");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(375,400);

        String[] courseid = db.read("SELECT Course_Id FROM Module WHERE Module_Id = " + modules[0] + ";");
        String[] notes = db.read("SELECT Notes.Note_Id FROM Notes JOIN Module ON Notes.Module_Id = Module.Module_Id JOIN Course ON Module.Course_Id = Course.Course_Id WHERE Course.Course_Id = " + courseid[0] + ";");

        String[][] rowData = new String[notes.length][3];
        for (int i = 0; i < notes.length; i++) {
            String[] result = db.read("SELECT Module_Id, Description FROM Notes WHERE Note_Id = " + notes[i] + ";");
            String[] result2 = db.read("SELECT Module_Name FROM Module WHERE Module_Id = " + result[0] + ";");
            rowData[i][0] = notes[i];
            rowData[i][1] = result2[0];
            rowData[i][2] = result[1];
        }

        String[] columnNames = {"Note Id", "Module Name", "Description"};
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        this.testData = rowData;

    }

    public void getNotes1() {
        ImageIcon image = new ImageIcon("logo.jpg");

        JFrame frame = new JFrame();
        frame.setTitle("Download Notes");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();

        panel.setLayout(null);
        panel.setVisible(true);
        frame.setVisible(true);

        JButton submit = new JButton();
        submit.setText("Download");
        submit.setBounds(140,200,120,50);

        JLabel noteId = new JLabel("Note Id");
        noteId.setBounds(50,10,300,30);

        JTextField noteIdf = new JTextField();
        noteIdf.setBounds(50,40,300,30);

        panel.add(submit);
        panel.add(noteId);
        panel.add(noteIdf);

        frame.add(panel);

        submit.addActionListener(e -> {
            String moduleIdText = noteIdf.getText();
            try {
                int moduleId = Integer.parseInt(moduleIdText);
                getNotes2(moduleId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid note ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error fetching notes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public File[] getNotes2(Integer x) throws IOException{
        String[] data = db.read("SELECT Description, Data FROM Notes WHERE Note_Id = " + x + ";");
        if(data.length == 0){
            JOptionPane.showMessageDialog(null, "No notes found for that module.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }else{
            File[] files = new File[data.length/2];
            for(int i = 0; i < data.length; i+=2){
                BufferedWriter writer = new BufferedWriter(new FileWriter(data[i] + ".txt"));
                writer.write(data[i+1]);
                writer.close();
                files[i/2] = new File(data[i] + ".txt");
            }
            JOptionPane.showMessageDialog(null, "Notes Downloaded!", "Complete", JOptionPane.PLAIN_MESSAGE);
            return files;
        }
    }
}
