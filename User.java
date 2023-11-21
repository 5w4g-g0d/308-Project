public abstract class User {
    protected DBConnect db;
    protected Integer id;
    protected String username;
    protected Integer ManagedBy;
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String gender;
    protected String dob;

    public void updatePass(String newPass){
        int check = db.write("UPDATE User SET password = '" + newPass + "' WHERE User_id = " + id + ";");
        if(check == 0) {
            password = newPass;
            System.out.println("Password updated to " + password);
        }
        return;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public Integer getID(){
        return id;
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String getGender(){
        return gender;
    }

    public String getDOB(){
        return dob;
    }
}
