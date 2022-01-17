
package model_entity;

/**
 *
 * @author julian076
 */
public class DetalleVenta {
    
    private int id;
    private String codigoProducto;
    private int cantidad;
    private double precioVenta;
    private int idVenta;           
    
    
    public DetalleVenta(){
        
    }

    public DetalleVenta(int id, String codigoProducto, int cantidad, double precioVenta, int idVenta) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.idVenta = idVenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
    
    
}
