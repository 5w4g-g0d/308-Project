import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LecturerTest {

    @Test
    void getModule() {
        DBConnect db = new DBConnect(); 
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        String[] expectedModuleID = {"945"};
        String[] actualModule = lecturer.getModule();
        assertArrayEquals(expectedModuleID, actualModule);
    }

    @Test
    void viewModule() {
        DBConnect db = new DBConnect(); 
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.viewModule();
    }

    @Test
    void updateModuleName2() {
        DBConnect db = new DBConnect(); 
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.updateModuleName2("Test ModuleName");
    }

    @Test
    void updateModuleDescription2() {
        DBConnect db = new DBConnect(); 
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.updateModuleDescription2("TestDesc");

    }


    @Test
    void uploadNotes2() {
        DBConnect db = new DBConnect(); 
        String[] data = {"17", "username", "password", "Lecturer", "1", "email", "John", "Doe", "Male", "1990-01-01"};
        Lecturer lecturer = new Lecturer(data, db);
        lecturer.uploadNotes2("TestDesc", "This Is A Test xx");

    }

}
