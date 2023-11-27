import java.io.File;

import javax.swing.JOptionPane;

public class Lecturer extends User {
    String[] modules;
    
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
        
        modules = db.read("SELECT Module_Id FROM Student_Module WHERE Student_Id = " + id + ";");
    }

    public String[] getModule(){
        if(modules.length == 0){
            return null;
        }
        return modules;
    }

    //ALL methods pertaining to marks will need to be adjusted for Lab and Exam marks.
    public void setExamMark(Integer student, Integer mark, Integer module){
        db.write("UPDATE Student_Module SET Exam_Mark = " + mark + " WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student '"+student+"' has been given a '"+mark+"' for the module '"+module+"' exam.", "Mark Assigned", JOptionPane.PLAIN_MESSAGE);
    }

    public void setLabMark(Integer student, Integer mark, Integer module){
        db.write("UPDATE Student_Module SET Lab_Mark = " + mark + " WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student '"+student+"' has been given a '"+mark+"' for the module '"+module+"' lab.", "Mark Assigned", JOptionPane.PLAIN_MESSAGE);
    }

    public void updateModule(String msg, Integer module){
        db.write("UPDATE Module SET Module_Description = '" + msg + "' WHERE Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Module '"+module+"' updated.", "Module Updated", JOptionPane.PLAIN_MESSAGE);
    }

    /*public void uploadNotes(String type, String content, Integer module){
        generate a .txt containing 'content' and save it to the given module. Type should flag whether the file is for lecture or lab notes
    }*/

    public String[] getStudents(Integer module){
        return db.read("SELECT Student_Id FROM Student_Module WHERE Module_Id = " + module + ";");
    }
}
