import java.io.File;

public class Lecturer extends User {
    Integer module;
    
    public Lecturer(String[] data, DBConnect x){
        db = x;
        id = Integer.parseInt(data[0]);
        username = data[1];
        ManagedBy = Integer.parseInt(data[4]); //With the exception of login(), skip data[2], userType can be determined by class type.
        email = data[5];
        password = data[2];
        firstName = data[6];
        lastName = data[7];
        gender = data[8];
        dob = data[9];
        
        String[] result = db.read("SELECT Module_Id FROM Module WHERE Lecturer_Id = " + id + ";");
        if(result == null || result.length == 0){
            System.out.println("Error: No modules found for this lecturer.");
        }else if(result.length > 1){
            System.out.println("Error: Multiple results returned. Please contact an administrator.");
        }else{
            module = Integer.parseInt(result[0]);
        }
    }

    public Integer getModule(){
        return module;
    }

    //ALL methods pertaining to marks will need to be adjusted for Lab and Exam marks.
    public void setMark(Integer student, Integer mark){
        db.write("UPDATE Student_Module SET Total_Mark = " + mark + " WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
    }

    public void updateModule(String msg){
        db.write("UPDATE Module SET Module_Description = '" + msg + "' WHERE Module_Id = " + module + ";");
    }

    //NEEDS RESTRUCTURING
    /*public void uploadNotes(String type, File f){
        db.write("UPDATE Module SET " + type + " = '"+ f + "' WHERE Module_Id = " + module + ";");
    }*/

    public String[] getStudents(){
        return db.read("SELECT Student_Id FROM Student_Module WHERE Module_Id = " + module + ";");
    }
}
