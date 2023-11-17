public class Manager extends User{
    Integer manages;
    
    public Manager(String[] data, DBConnect x){
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

        manages = Integer.parseInt(db.read("SELECT User_Id FROM User WHERE Managed_By_Id = " + id + ";")[0]);
    }

    public String[] DisplayQueue(){
        String[] result = db.read("SELECT User_Id FROM User WHERE Registered == 0;");
        return result;
    }

    public void ApproveUser(Integer x){
        db.write("UPDATE User SET Registered = " + 1 + " WHERE User_Id = " + x + ";");

        String[] registered = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Registered = 1" );
        if (registered != null){
            System.out.println("User has been successfully registered.");
        } 
    }

    public void Activate(Integer x){
        db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
        return;
    }

    public void Deactivate(Integer x){
        db.write("UPDATE User SET Active = " + 0 + " WHERE User_Id = " + x + ";");
        return;
    }

    public void passReset(String newPass, Integer x){
        db.write("UPDATE User SET Password = '" + newPass + "' WHERE Student_Id = " + x + ";");
        return;
    }

    public void assignModule(Integer lecturer, Integer module){
        db.write("UPDATE Module SET Lecturer_Id = " + lecturer + " WHERE Module_Id = " + module + ";");
        return;
    }

    public void setDecision(Integer student, Integer module, String decision){
        db.write("UPDATE Student_Module SET Decision = '" + decision + "' WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        return;
    }

    public void toggleRule(String rulename){
        db.write("UPDATE Business_Rules SET Boolean = NOT Boolean WHERE Rule_Name = '" + rulename + "';");
        return;
    }

    public void newCourse(Integer id, String name, String desc){
        db.write("INSERT INTO Course VALUES (" + id + ", '" + name + "', '" + desc + "');");
        return;
    }

    public void newModule(Integer id, String name, String desc){
        db.write("INSERT INTO Module VALUES (" + id + ", '" + name + "', '" + desc + "');");
        return;
    }

    public void assignCourse(Integer module, Integer course){
        db.write("UPDATE Module SET Course_Id = " + course + " WHERE Module_Id = " + module + ";");
        return;
    }

    public String[] getCourseDetails(Integer course){
        String[] results = db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
        return results;
    }

    public String[] getModuleDetails(Integer module){
        String[] results = db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
        return results;
    }

    public void updateDesc(String desc, Integer x){
        db.write("UPDATE Course SET Course_Description = '" + desc + "' WHERE Course_Id = " + x + ";");
        return;
    }
}
