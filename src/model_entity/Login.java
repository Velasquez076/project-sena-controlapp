
package model_entity;

/**
 *
 * @author julian076
 */
public class Login {
    
    private int id;    
    private String cedula_usuario;    
    private String email_usuario;    
    private String nombres_usuario;    
    private String usuario;    
    private String rol;
    private String password;
    
    public Login(){
        
    }

    public Login(int id, String cedula_usuario, String email_usuario, String nombres_usuario, String usuario, String password, String rol) {
        this.id = id;
        this.cedula_usuario = cedula_usuario;
        this.email_usuario = email_usuario;
        this.nombres_usuario = nombres_usuario;
        this.usuario = usuario;
        this.rol = rol;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula_usuario() {
        return cedula_usuario;
    }

    public void setCedula_usuario(String cedula_usuario) {
        this.cedula_usuario = cedula_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getNombres_usuario() {
        return nombres_usuario;
    }

    public void setNombres_usuario(String nombres_usuario) {
        this.nombres_usuario = nombres_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
    
    
    
}
