import javax.swing.JOptionPane;

public class Student extends User {
    String[] modules;
    
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

        modules = db.read("SELECT Module_Id FROM Student_Module WHERE Student_Id = " + id + ";");
    }

    public String[] getModules(){
        return modules;
    }

    public String getResult(Integer x){
        String[] data = db.read("SELECT Exam_mark, Lab_mark FROM Student_Module WHERE Student_Id = " + id + " AND Module_Id = " + x + ";");
        if(data.length == 0){
            JOptionPane.showMessageDialog(null, "No result found for that module.", "Error", JOptionPane.ERROR_MESSAGE);
            return "Error";
        }else{
            return data[0] + ", " + data[1];
        }
    }

    public String getDecision(Integer x){
        String[] data = db.read("SELECT Decision FROM Student_Module WHERE Student_Id = " + id + " AND Module_Id = " + x + ";");
        if(data == null || data.length == 0){
            JOptionPane.showMessageDialog(null, "No decision yet for that module.", "No decision", JOptionPane.PLAIN_MESSAGE);
            return "None";
        }else if(data.length > 1){
            JOptionPane.showMessageDialog(null, "Multiple results found. Please contact an administrator", "Error", JOptionPane.ERROR_MESSAGE);
            return "Err_Flag";
        }else{
            return data[0];
        }
    }
}
