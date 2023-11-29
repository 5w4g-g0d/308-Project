import javax.swing.*;
import java.util.Objects;

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

    protected String userType;

    public void updatePass(JPanel panel1, JFrame frame) {
        panel1.setVisible(false);

        JPanel panel2 = new JPanel();

        frame.setSize(400,400);
        panel2.setLayout(null);
        panel2.setVisible(true);

        JLabel password = new JLabel("Password:");
        password.setBounds(50,10,300,30);
        JLabel password2 = new JLabel("Confirm password:");
        password2.setBounds(50,90,300,30);

        JPasswordField passwordf = new JPasswordField();
        passwordf.setBounds(50,40,300,30);
        JPasswordField password2f = new JPasswordField();
        password2f.setBounds(50,120,300,30);

        JButton submit = new JButton();
        submit.setText("Change Password");
        submit.setBounds(120,200,160,50);
        submit.addActionListener(e -> updatePassword2(passwordf.getText(), password2f.getText(), getType(), frame));

        frame.add(panel2);
        panel2.add(password);
        panel2.add(password2);
        panel2.add(passwordf);
        panel2.add(password2f);
        panel2.add(submit);
    }

    public void updatePassword2(String p1, String p2, String type, JFrame frame) {
        Driver d = new Driver();
        if(!Objects.equals(p1, p2)){
            JOptionPane.showMessageDialog(null, "Your passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            int check = db.write("UPDATE User SET password = '" + p1 + "' WHERE User_id = " + id + ";");
            if(check == 0) {
                password = p1;
                JOptionPane.showMessageDialog(null, "Password Updated", "Success", JOptionPane.PLAIN_MESSAGE);
            }
            String[] result = db.read("SELECT * FROM User WHERE User_Id = " + id + ";");
            if (Objects.equals(type, "Student")) {
                frame.setVisible(false);
                frame.dispose();
                Student s = new Student(result, db);
                d.student(s);
                return;
            } else if (Objects.equals(type, "Lecturer")) {
                frame.setVisible(false);
                frame.dispose();
                Lecturer l = new Lecturer(result, db);
                d.lecturer(l);
                return;
            } else {
                frame.setVisible(false);
                frame.dispose();
                Manager m = new Manager(result, db);
                d.manager(m);
                return;
            }
        }
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

    public String getType() {return userType;}
}
