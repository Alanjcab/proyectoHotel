package hotel.model;

public class Client {
    private int idClient;
    private String name;
    private String lastName;
    private String dni;
    private String phone;
    private String email;
    private boolean active;

    public  Client(String name, String lastName, String dni, String phone, String email){
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.phone = phone;
        this.email = email;
        this.active = true;
    }

    public Client(int idClient,String name, String lastName, String dni, String phone, String email, boolean active){
        //reutilizo los parametros del constructor anterior
        this( name, lastName, dni, phone, email);
        this.idClient = idClient;
        this.active = active;
    }

    //getters para acceder a los atributos

    public int getIdClient(){
        return  idClient;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public boolean getActive(){
        return active;
    }

    //setter de los atributos

    public void setIdClient(int idClient){
        this.idClient = idClient;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
