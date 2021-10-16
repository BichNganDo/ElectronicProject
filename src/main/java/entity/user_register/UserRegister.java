package entity.user_register;

public class UserRegister {

    private int id;
    private String name;
    private String email;
    private String password;
    private String re_password;

    public UserRegister() {
    }

    public UserRegister(int id, String name, String email, String password, String re_password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.re_password = re_password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

}
