package model_entity;

/**
 *
 * @author julian076
 */
public class DatosEmpresa {

    private int id;
    private String nit;
    private String nombreEmpresa;
    private String telefono;
    private String direccion;

    public DatosEmpresa() {

    }

    public DatosEmpresa(int id, String nit, String nombreEmpresa, String telefono, String direccion) {
        this.id = id;
        this.nit = nit;
        this.nombreEmpresa = nombreEmpresa;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    

}
