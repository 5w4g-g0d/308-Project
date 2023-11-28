import javax.swing.JOptionPane;

public class Manager extends User{
    Integer manages;
    
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

        manages = Integer.parseInt(db.read("SELECT User_Id FROM User WHERE Managed_By_Id = " + id + ";")[0]);
    }

    public String[] DisplayQueue(){
        return db.read("SELECT User_Id FROM User WHERE Registered = '0';");
    }

    public void ApproveUser(Integer x){
        String[] registered = db.read("SELECT * FROM User WHERE User_Id = " + x + " AND Registered = 1" );
        if (registered.length == 0){
            db.write("UPDATE User SET Registered = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
            db.write("UPDATE User SET Managed_By_Id = "+ id +" WHERE User_Id = "+ x + ";");
            JOptionPane.showMessageDialog(null, "This user has been registered, and activated. You have been assigned as their manager.", "User Registered", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "That user Id has already been registered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activate(Integer x){
        db.write("UPDATE User SET Active = " + 1 + " WHERE User_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "User Id: '"+x+"' has been activated.", "Account Activated", JOptionPane.PLAIN_MESSAGE);
    }

    public void deactivate(Integer x){
        db.write("UPDATE User SET Active = " + 0 + " WHERE User_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "User Id: '"+x+"' has been deactivated.", "Account Deactivated", JOptionPane.PLAIN_MESSAGE);
    }

    public void enrollStudent(Integer student, Integer course){
        //loop for each module in a course
        String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = "+ course+ ";");
        for(int i = 0; i < modules.length; i++){
            //find existing attempts for this module, if any
            Integer atts = 0;
            String[] attempts = db.read("SELECT Student_Attempt_No FROM Student_Module WHERE Student_Id = '"+student+"'' AND Module_Id = '"+modules[i]+"';");
            
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
            if(attempts.length != 0){
                atts = 1;
            }else if(Integer.parseInt(attempts[0]) == 2 && attemptsRule){
                JOptionPane.showMessageDialog(null, "This student has exceeded the maximum allowed attempts for module "+modules[i]+".", "Error", JOptionPane.ERROR_MESSAGE);
                //N.B. if a student fails this check, and is not enrolled, they have been enrolled into any modules before, but not any after.
                return;
            }else{
                atts = Integer.parseInt(attempts[0]) + 1;
            }
            //add student to this module
            db.write("INSERT INTO Student_Module (Module_Id, Student_Id, Student_Attempt_No) VALUES ('"+modules[i]+"', '"+student+"', '"+atts+"');");
        }
        //Success message, only when student is enrolled into EVERY module for a course.
        String[] courseName = db.read("SELECT Course_Name FROM Course WHERE Course_Id = '"+course+"';");
        JOptionPane.showMessageDialog(null, "Success!\n Student "+student+"has been added to modules in course"+courseName+".", "Success", JOptionPane.PLAIN_MESSAGE);
    }

    public void passReset(Integer x){
        db.write("UPDATE User SET Password = 'temporaryPassword23' WHERE User_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "The user's password has been reset to \"temporaryPassword23\".", "Password changed", JOptionPane.PLAIN_MESSAGE);
    }

    public void assignModule(Integer lecturer, Integer module){
        String[] lecturers = db.read("SELECT Lecturer_Id FROM Module WHERE Module_Id = '"+module+"';");
        if(lecturers.length == 0){
            db.write("UPDATE Module SET Lecturer_Id = " + lecturer + " WHERE Module_Id = " + module + ";");
            JOptionPane.showMessageDialog(null, "Lecturer Id: '"+lecturer+"' has been assigned to Module: '"+module+"'.", "Lecturer Assigned", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "That module already has a lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        

    public void setDecision(Integer student, Integer module, String decision){
        String[] rules = db.read("SELECT Rule_Id FROM Module_Rules WHERE Module_Id = '"+module+"';");
        Boolean PassRule = false;
        for(String rule:rules){
            if(Integer.parseInt(rule) == 3){
                PassRule = true;
            }
        }
        String[] marks = db.read("SELECT Exam_Mark, Lab_Mark FROM Student_Module WHERE Student_Id = '"+student+"' AND Module_Id = '"+module+"';");
        Integer totalMark = (Integer.parseInt(marks[0]) + Integer.parseInt(marks[1]));
        if(decision == "Award"){
            if(totalMark < 80){
                JOptionPane.showMessageDialog(null, "This student has not achieved the required mark to be awarded this module.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }else if(totalMark < 100 && PassRule){
                JOptionPane.showMessageDialog(null, "This student has not achieved the required mark to be awarded this module.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        db.write("UPDATE Student_Module SET Decision = '" + decision + "' WHERE Student_Id = " + student + " AND Module_Id = " + module + ";");
        JOptionPane.showMessageDialog(null, "Student Id: '"+student+"' has been given the decision: '"+decision+"' for module: '"+module+"'.", "Decision Made", JOptionPane.PLAIN_MESSAGE);
    }

    public void addRuleCourse(Integer course, Integer rule){
        String[] exists = db.read("SELECT Course_Id FROM Course_Rules WHERE Course_Id = '"+course+"' AND Rule_Id = '"+rule+"';");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is already applied to Course '"+course+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("INSERT INTO Course_Rules (Course_Id, Rule_Id) VALUES ('"+course+"', '"+rule+"');");
            String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = '"+course+"';");
            for(String module: modules){
                String[] moduleExists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = '"+module+"' AND Rule_Id = '"+rule+"';");
                if(moduleExists.length != 0){
                    JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is already applied to Module '"+module+"'.", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ('"+module+"', '"+rule+"');");
                }
            }
            JOptionPane.showMessageDialog(null, "Course '"+course+"' has been given rule '"+rule+"'.", "Course Rule Added", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void remRuleCourse(Integer course, Integer rule){
        String[] exists = db.read("SELECT Course_Id FROM Course_Rules WHERE Course_Id = '"+course+"' AND Rule_Id = '"+rule+"';");
        if(exists.length == 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is not applied to Course '"+course+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("DELETE FROM Course_Rules WHERE Course_Id = '"+course+"' AND Rule_Id = '"+rule+"');");
            String[] modules = db.read("SELECT Module_Id FROM Module WHERE Course_Id = '"+course+"';");
            for(String module: modules){
                db.write("DELETE FROM Module_Rules WHERE Module_Id = '"+module+"' AND Rule_Id = '"+rule+"');");
            }
            JOptionPane.showMessageDialog(null, "Course '"+course+"' no longer has Rule '"+rule+"'.", "Course Rule Removed", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void addRuleModule(Integer module, Integer rule){
        String[] exists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = '"+module+"' AND Rule_Id = '"+rule+"';");
        if(exists.length != 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is already applied to Module '"+module+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ('"+module+"', '"+rule+"');");
            JOptionPane.showMessageDialog(null, "Module '"+module+"' has been given rule '"+rule+"'.", "Module Rule Added", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void remRuleModule(Integer module, Integer rule){
        String[] exists = db.read("SELECT Module_Id FROM Module_Rules WHERE Module_Id = '"+module+"' AND Rule_Id = '"+rule+"';");
        if(exists.length == 0){
            JOptionPane.showMessageDialog(null, "Rule '"+rule+"' is not applied to Module '"+module+"'.", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            db.write("DELETE FROM Module_Rules WHERE Module_Id = '"+module+"' AND Rule_Id = '"+rule+"');");
            JOptionPane.showMessageDialog(null, "Module '"+module+"' no longer has Rule '"+rule+"'.", "Course Rule Removed", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void newCourse(Integer id, String name, String desc){
        db.write("INSERT INTO Course VALUES (" + id + ", '" + name + "', '" + desc + "');");
        JOptionPane.showMessageDialog(null, "Course '"+name+"' has been added.", "Course Added", JOptionPane.PLAIN_MESSAGE);
    }

    public void newModule(Integer id, String name, String desc){
        db.write("INSERT INTO Module VALUES (" + id + ", '" + name + "', '" + desc + "');");
        JOptionPane.showMessageDialog(null, "Module '"+name+"' has been added.", "Module Added", JOptionPane.PLAIN_MESSAGE);
    }

    public void assignCourse(Integer module, Integer course){
        db.write("UPDATE Module SET Course_Id = " + course + " WHERE Module_Id = " + module + ";");
        String[] rules = db.read("SELECT Rule_Id FROM Course_Rules WHERE Course_Id = '"+course+"';");
        for(int i = 0; i < rules.length; i++){
            db.write("INSERT INTO Module_Rules (Module_Id, Rule_Id) VALUES ('"+module+"', '"+rules[i]+"');");
        }
        JOptionPane.showMessageDialog(null, "Module '"+module+"' has been assigned to Course '"+course+"'.", "Course Added", JOptionPane.PLAIN_MESSAGE);
    }

    public String[] getCourseDetails(Integer course){
        return db.read("SELECT * FROM Course WHERE Course_Id = " + course + ";");
    }

    public String[] getModuleDetails(Integer module){
        return db.read("SELECT * FROM Module WHERE Module_Id = " + module + ";");
    }

    public void updateDesc(String desc, Integer x){
        db.write("UPDATE Course SET Course_Description = '" + desc + "' WHERE Course_Id = " + x + ";");
        JOptionPane.showMessageDialog(null, "Course '"+x+"' has been updated'.", "Description Updated", JOptionPane.PLAIN_MESSAGE);
    }
}
