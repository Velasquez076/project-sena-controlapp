
package model_entity;
/**
 *
 * @author julian076
 */
public class Producto {
    
    private int id;
    private String codigoProducto;
    private String descripcionProducto;
    private int stockProductos;
    private double precioProducto;
    private String proveedorProducto;
    
    public Producto(){
        
    }

    public Producto(int id, String codigoProducto, String descripcion, int stock, double precio, String proveedor) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.descripcionProducto = descripcion;
        this.stockProductos = stock;
        this.precioProducto = precio;
        this.proveedorProducto = proveedor;
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

    public String getDescripcion() {
        return descripcionProducto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcionProducto = descripcion;
    }

    public int getStock() {
        return stockProductos;
    }

    public void setStock(int stock) {
        this.stockProductos = stock;
    }

    public double getPrecio() {
        return precioProducto;
    }

    public void setPrecio(double precio) {
        this.precioProducto = precio;
    }

    public String getProveedor() {
        return proveedorProducto;
    }

    public void setProveedor(String proveedor) {
        this.proveedorProducto = proveedor;
    }
    
    
}
