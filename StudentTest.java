import java.io.IOException;


public class StudentTest {

    public static <File> void main(String[] args) {

        DBConnect db = new DBConnect();

        String[] studentData = {"1", "student1", "password123", "Student", "1", "student1@example.com",
                "John", "Doe", "Male", "2000-01-01"};

        Student student = new Student(studentData, db);


        student.viewModules();
        String[][] modulesData = student.testData;
        assert modulesData.length > 0 : "viewModules() failed to retrieve modules data";


        student.viewCourse();
        String[][] courseData = student.testData;
        assert courseData.length > 0 : "viewCourse() failed to retrieve course data";

        student.viewNoteInfo();
        String[][] noteInfoData = student.testData;
        assert noteInfoData.length > 0 : "viewNoteInfo() failed to retrieve note information";

        try {
            File[] files = (File[]) student.getNotes2(1);

            if (files != null && files.length > 0) {
                System.out.println("Notes Downloaded!");
            } else {
                System.out.println("No notes found for that module.");
            }
        } catch (IOException ex) {
            System.out.println("Error fetching notes: " + ex.getMessage());
        }


        System.out.println("All tests passed successfully.");
    }
}
