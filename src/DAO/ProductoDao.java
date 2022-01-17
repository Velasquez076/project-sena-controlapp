package DAO;

import conexion_BD.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import model_entity.DatosEmpresa;
import model_entity.Producto;

/**
 *
 * @author julian076
 */
public class ProductoDao {

    Conexion conexion = new Conexion();
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarProducto(Producto product) {
        String sql = "INSERT INTO productos (codigo_producto, descripcion_producto, stock_productos, precio_producto, proveedor_producto) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);

            //se envian datos 
            ps.setString(1, product.getCodigoProducto());
            ps.setString(2, product.getDescripcion());
            ps.setInt(3, product.getStock());
            ps.setDouble(4, product.getPrecio());
            ps.setString(5, product.getProveedor());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void consultarProveedor(JComboBox suplier) {
        String sql = "SELECT nombre_proveedor FROM proveedores";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                suplier.addItem(rs.getString("nombre_proveedor"));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public List listarProductos() {
        List<Producto> listaProduct = new ArrayList();
        String sql = "SELECT * FROM productos";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto suplier = new Producto();
                suplier.setId(rs.getInt("id"));
                suplier.setCodigoProducto(rs.getString("codigo_producto"));
                suplier.setDescripcion(rs.getString("descripcion_producto"));
                suplier.setStock(Integer.parseInt(rs.getString("stock_productos")));
                suplier.setPrecio(Double.parseDouble(rs.getString("precio_producto")));
                suplier.setProveedor(rs.getString("proveedor_producto"));
                listaProduct.add(suplier);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaProduct;
    }

    public boolean eliminarProducto(int id) {

        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                conn.close();

            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

    }

    public boolean editarProducto(Producto product) {

        String sql = "UPDATE productos SET codigo_producto = ?, descripcion_producto = ?, stock_productos = ?, precio_producto = ?, proveedor_producto = ? WHERE id = ?";

        try {
            ps = conn.prepareStatement(sql);

            ps.setString(1, product.getCodigoProducto());
            ps.setString(2, product.getDescripcion());
            ps.setInt(3, product.getStock());
            ps.setDouble(4, product.getPrecio());
            ps.setString(5, product.getProveedor());
            ps.setInt(6, product.getId());
            ps.execute();

            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {

                System.out.println(e.toString());
            }
        }

    }

    public Producto consultarProductos(String codigo) {
        Producto product = new Producto();
        String sql = "SELECT * FROM productos WHERE codigo_producto = ?";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                product.setDescripcion(rs.getString("descripcion_producto"));
                product.setPrecio(rs.getDouble("precio_producto"));
                product.setStock(rs.getInt("stock_productos"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return product;
    }
    
    public DatosEmpresa consultarDatos( ) {
        DatosEmpresa datos = new DatosEmpresa();
        String sql = "SELECT * FROM config";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                datos.setId(rs.getInt("id"));
                datos.setNit(rs.getString("nit_empresa"));
                datos.setNombreEmpresa(rs.getString("nombre_empresa"));
                datos.setTelefono(rs.getString("telefono_empresa"));
                datos.setDireccion(rs.getString("direccion_empresa"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return datos;
    }
    
     public boolean editarDatosEmpresa(DatosEmpresa datos) {

        String sql = "UPDATE config SET nombre_empresa = ?, nit_empresa = ?, telefono_empresa = ?, direccion_empresa = ?,  WHERE id = ?";

        try {
            ps = conn.prepareStatement(sql);

            ps.setString(1, datos.getNombreEmpresa());
            ps.setString(2, datos.getNit());
            ps.setString(3, datos.getTelefono());
            ps.setString(4, datos.getDireccion());
            ps.setInt(5, datos.getId());
            ps.execute();
            return true;
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {

                System.out.println(e.toString());
            }
        }

    }
   

}
