
package model_entity;

/**
 *
 * @author julian076
 */
public class Venta {
    
    private int id;
    private String clienteVenta;
    private String vendedor;
    private double totalVenta;
    private String fecha;
    
    public Venta(){
        
    }

    public Venta(int id, String clienteVenta, String vendedor, double totalVenta, String fecha) {
        this.id = id;
        this.clienteVenta = clienteVenta;
        this.vendedor = vendedor;
        this.totalVenta = totalVenta;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClienteVenta() {
        return clienteVenta;
    }

    public void setClienteVenta(String clienteVenta) {
        this.clienteVenta = clienteVenta;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
    
}
