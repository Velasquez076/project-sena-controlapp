
package model_entity;

/**
 *
 * @author julian076
 */
public class Cliente {
    
    private int id;
    private String cedulaCliente;
    private String nombreCliente;
    private String emailCliente;
    private String direccionCliente;
    private String telefonoCliente;

    public Cliente() {
        
    }

    public Cliente(int id, String cedula_cliente, String nombre_cliente, String email_cliente, String direccion_cliente, String telefono_cliente) {
        this.id = id;
        this.cedulaCliente = cedula_cliente;
        this.nombreCliente = nombre_cliente;
        this.emailCliente = email_cliente;
        this.direccionCliente = direccion_cliente;
        this.telefonoCliente = telefono_cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula_cliente() {
        return cedulaCliente;
    }

    public void setCedula_cliente(String cedula_cliente) {
        this.cedulaCliente = cedula_cliente;
    }

    public String getNombre_cliente() {
        return nombreCliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombreCliente = nombre_cliente;
    }

    public String getEmail_cliente() {
        return emailCliente;
    }

    public void setEmail_cliente(String email_cliente) {
        this.emailCliente = email_cliente;
    }

    public String getDireccion_cliente() {
        return direccionCliente;
    }

    public void setDireccion_cliente(String direccion_cliente) {
        this.direccionCliente = direccion_cliente;
    }

    public String getTelefono_cliente() {
        return telefonoCliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefonoCliente = telefono_cliente;
    }
    
    
    
    
}
