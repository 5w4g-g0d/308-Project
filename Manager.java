public class Manager extends User{
    Integer manages;
    
    public Manager(String[] data, DBConnect x){
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

        manages = Integer.parseInt(db.read("SELECT User_Id FROM User WHERE Managed_By_Id = " + id + ";")[0]);
    }

    public void DisplayQueue(){
        String[] res = db.read("SELECT User_Id FROM User WHERE Registered = '0';");
        for(String re: res){
            System.out.println(re);
        }
    }

    public void ApproveUser(Integer x){
        String[] registered = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Registered = 1" );
        if (registered.length == 0){
            System.out.println("User has been successfully registered.");
            db.write("UPDATE User SET Registered = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Managed_By_Id = "+ id +" WHERE User_Id = "+ x + ";");
        }else{
            System.out.println("That account has already been registered.");
        }
    }

    public void activate(Integer x){
        db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
    }

    public void deactivate(Integer x){
        db.write("UPDATE User SET Active = " + 0 + " WHERE User_Id = " + x + ";");
    }

    public void passReset(String newPass, Integer x){
        db.write("UPDATE User SET Password = '" + newPass + "' WHERE Student_Id = " + x + ";");
    }

    public void assignModule(Integer lecturer, Integer module){
        db.write("UPDATE Module SET Lecturer_Id = " + lecturer + " WHERE Module_Id = " + module + ";");
    }

    public void setDecision(Integer student, Integer module, String decision){
        db.write("UPDATE Student_Module SET Decision = '" + decision + "' WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
    }

    public void toggleRule(String rulename){
        db.write("UPDATE Business_Rules SET Boolean = NOT Boolean WHERE Rule_Name = '" + rulename + "';");
    }

    public void newCourse(Integer id, String name, String desc){
        db.write("INSERT INTO Course VALUES (" + id + ", '" + name + "', '" + desc + "');");
    }

    public void newModule(Integer id, String name, String desc){
        db.write("INSERT INTO Module VALUES (" + id + ", '" + name + "', '" + desc + "');");
    }

    public void assignCourse(Integer module, Integer course){
        db.write("UPDATE Module SET Course_Id = " + course + " WHERE Module_Id = " + module + ";");
    }

    public String[] getCourseDetails(Integer course){
        return db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
    }

    public String[] getModuleDetails(Integer module){
        return db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
    }

    public void updateDesc(String desc, Integer x){
        db.write("UPDATE Course SET Course_Description = '" + desc + "' WHERE Course_Id = " + x + ";");
    }
}
