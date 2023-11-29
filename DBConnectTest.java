

import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBConnectTest {
    private DBConnect dbConnect;

    @BeforeEach
    void setUp() {
        dbConnect = new DBConnect();
    }

    @Test
    void testGetMysqlConnection() {
        assertNotNull(dbConnect.getMysqlConnection());
    }

    @Test
    void testRead() {
        String statement = "SELECT * FROM User";
        String[] result = dbConnect.read(statement);
        assertNotNull(result);
    }

    @Test
    void testWrite() {
        String statement = "INSERT INTO `User`(`Username`, `Password`, `User_Type`, `Email`, `First_Name`, `Surname`, `DOB`, `Qualification`, `Active`, `Registered`) VALUES ('Test','Test','Student', 'test@test.com','Test','Test' ,'1111-11-11', 'N/A',0 ,0)";
        int rowsAffected = dbConnect.write(statement);
        assertEquals(1, rowsAffected);

    }



}