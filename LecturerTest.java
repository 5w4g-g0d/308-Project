import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LecturerTest {

    @Test
    void getModule() {
        DBConnect db = new DBConnect();
        String statement = "INSERT INTO `User`(`User_Id`, `Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('17', 'Test','Test','Lecturer', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',1 ,1)";
        db.write(statement);
        statement = "UPDATE Module SET `Lecturer_Id` = '17' WHERE Module_ID = '946'";
        db.write(statement);
        String[] data = {"17", "Test", "Test", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        String[] expectedModuleID = {"946"};
        String[] actualModule = lecturer.getModule();
        assertArrayEquals(expectedModuleID, actualModule);
        statement = "UPDATE Module SET `Lecturer_Id` = '19' WHERE Module_ID = '946'";
        db.write(statement);
        statement = "DELETE FROM `User` WHERE User_Id = '17'";
        db.write(statement);
    }

    @Test
    void viewModule() {
        DBConnect db = new DBConnect();
        String statement = "INSERT INTO `User`(`User_Id`, `Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('17', 'Test','Test','Lecturer', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',1 ,1)";
        db.write(statement);
        statement = "UPDATE Module SET `Lecturer_Id` = '17' WHERE Module_ID = '946'";
        db.write(statement);
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.viewModule();
        statement = "UPDATE Module SET `Lecturer_Id` = '19' WHERE Module_ID = '946'";
        db.write(statement);
        statement = "DELETE FROM `User` WHERE User_Id = '17'";
        db.write(statement);
    }

    @Test
    void updateModuleName2() {
        DBConnect db = new DBConnect();
        String statement = "INSERT INTO `User`(`User_Id`, `Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('17', 'Test','Test','Lecturer', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',1 ,1)";
        db.write(statement);
        statement = "UPDATE Module SET `Lecturer_Id` = '17' WHERE Module_ID = '946'";
        db.write(statement);
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.updateModuleName2("Test ModuleName");
        statement = "UPDATE Module SET `Lecturer_Id` = '19', `Module_Name` = 'CS312' WHERE Module_ID = '946'";
        db.write(statement);
        statement = "DELETE FROM `User` WHERE User_Id = '17'";
        db.write(statement);
    }

    @Test
    void updateModuleDescription2() {
        DBConnect db = new DBConnect();
        String statement = "INSERT INTO `User`(`User_Id`, `Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('17', 'Test','Test','Lecturer', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',1 ,1)";
        db.write(statement);
        statement = "UPDATE Module SET `Lecturer_Id` = '17' WHERE Module_ID = '946'";
        db.write(statement);
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.updateModuleDescription2("TestDesc");
        statement = "UPDATE Module SET `Lecturer_Id` = '19', `Module_Description` = 'i hate web' WHERE Module_ID = '946'";
        db.write(statement);
        statement = "DELETE FROM `User` WHERE User_Id = '17'";
        db.write(statement);

    }


    @Test
    void uploadNotes2() {
        DBConnect db = new DBConnect();
        String statement = "INSERT INTO `User`(`User_Id`, `Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('17', 'Test','Test','Lecturer', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',1 ,1)";
        db.write(statement);
        statement = "UPDATE Module SET `Lecturer_Id` = '17' WHERE Module_ID = '946'";
        db.write(statement);
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.uploadNotes2("TestDesc", "This Is A Test xx");
        statement = "UPDATE Module SET `Lecturer_Id` = '19', `Module_Description` = 'i hate web' WHERE Module_ID = '946'";
        db.write(statement);
        statement = "DELETE FROM `Notes` WHERE Module_ID = '946' AND `Description` = 'TestDesc'";
        db.write(statement);
        statement = "DELETE FROM `User` WHERE User_Id = '17'";
        db.write(statement);

    }

}
