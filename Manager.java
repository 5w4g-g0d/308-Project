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

    public void DisplayQueue(){
        //TODO: Print list of users within Register
        return;
    }

    public void ApproveUser(Integer x){
        //db.write User with ID x to database
        //If successful, print confirmation message
        //Remove User with ID x from register
    }

    public void Activate(Integer x){
        //db.write User with ID x to be active
        return;
    }

    public void Deactivate(Integer x){
        //db.write User with ID x to be inactive
        return;
    }

    public void passReset(String newPass, Integer x){
        db.write("UPDATE users SET password = '" + newPass + "' WHERE id = " + x + ";");
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

    public void getCourseDetails(Integer course){
        db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
        return;
    }

    public void getModuleDetails(Integer module){
        db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
        return;
    }

    public void updateCourse(String desc, Integer x){
        db.write("UPDATE Course SET Course_Description = '" + desc + "' WHERE Course_Id = " + x + ";");
        return;
    }
}
