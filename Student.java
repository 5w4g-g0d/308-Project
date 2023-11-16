public class Student extends User {
    String[] modules;
    
    public Student(String[] data, DBConnect x){
        db = x;
        id = Integer.parseInt(data[0]);
        username = data[1];
        ManagedBy = Integer.parseInt(data[3]); //With the exception of login(), skip data[2], userType can be determined by class type.
        email = data[4];
        password = data[5];
        firstName = data[6];
        lastName = data[7];
        gender = data[8];
        dob = data[9];

        modules = db.read("SELECT Module_Id FROM Student_Module WHERE Student_Id = " + id + ";");
    }

    public String[] getModules(){
        return modules;
    }

    public String getResult(String x){
        String[] data = db.read("SELECT Exam_mark, Lab_mark FROM Student_Module WHERE Student_Id = " + id + " AND Module_Id = " + x + ";");
        if(data == null || data.length == 0){
            return "No results found for that module.";
        }else{
            return data[0] + ", " + data[1];
        }
    }

    public String getDecision(String x){
        String[] data = db.read("SELECT Decision FROM Student_Module WHERE Student_Id = " + id + " AND Module_Id = " + x + ";");
        if(data == null || data.length == 0){
            return "No decision found for that module.";
        }else if(data.length > 1){
            return "Error: Multiple results returned. Please contact an administrator.";
        }else{
            return data[0];
        }
    }
}
