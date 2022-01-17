
package model_entity;

/**
 *
 * @author julian076
 */
public class Proveedor {
    
    private int id;
    private String nitProveedor;
    private String nombreProveedor;
    private String direccionProveedor;
    private String ciudadProveedor;
    private String telefonoProveedor;

    public Proveedor() {
        
    }

    public Proveedor(int id, String nitProveedor, String nombreProveedor, String direccionProveedor, String ciudadProveedor, String telefonoProveedor) {
        this.id = id;
        this.nitProveedor = nitProveedor;
        this.nombreProveedor = nombreProveedor;
        this.direccionProveedor = direccionProveedor;
        this.ciudadProveedor = ciudadProveedor;
        this.telefonoProveedor = telefonoProveedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getCiudadProveedor() {
        return ciudadProveedor;
    }

    public void setCiudadProveedor(String ciudadProveedor) {
        this.ciudadProveedor = ciudadProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }
    
    
    
    
}
