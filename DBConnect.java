public class DBConnect {
    public String[] read(String statement){
        //Apply SELECT statement to Database. (Could construct here, or individually when read() is being called.)
        //Read into array EITHER several fields of one record, or one field of several records.
        //!!Catch SQL errors here. Return 1 in instance of error, 0 for instance of no results!!
        return null;
    } 

    public int write(String statement){
        //As above, apply INSERT or UPDATE statement to Databse.
        //!!Catch SQL errors here. Return 1 in instance of error!!
        return 0;
    }
}