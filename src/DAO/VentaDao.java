package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import conexion_BD.Conexion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model_entity.DetalleVenta;
import model_entity.Venta;

/**
 *
 * @author julian076
 */
public class VentaDao {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Conexion conexion = new Conexion();

    public int registrarVenta(Venta venta) {
        int resp = 0;
        String sql = "INSERT INTO ventas (cliente_venta, vendedor, total_venta, fecha_venta) VALUES (?, ?, ?, ?)";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, venta.getClienteVenta());
            ps.setString(2, venta.getVendedor());
            ps.setDouble(3, venta.getTotalVenta());
            ps.setString(4, venta.getFecha());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return resp;
    }

    public int registrarDetalleVenta(DetalleVenta detalle) {
        int resp = 0;
        String sql = "INSERT INTO detalle_venta (cod_producto, cantidad, precio_venta, id_venta) VALUES (?, ?, ?, ?)";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, detalle.getCodigoProducto());
            ps.setInt(2, detalle.getCantidad());
            ps.setDouble(3, detalle.getPrecioVenta());
            ps.setInt(4, detalle.getId());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return resp;
    }

    public int idVenta() {

        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public boolean actualizarStock(int cantidad, String codigo ){
        String sql = "UPDATE productos SET stock_productos = ? WHERE codigo_producto = ?";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setString(2, codigo);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public List listarVentas(){
        
        List<Venta> listaVentas = new ArrayList();
        String sql = "SELECT * FROM ventas";
        
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Venta sell = new Venta();
                sell.setId(rs.getInt("id"));
                sell.setClienteVenta(rs.getString("cliente_venta"));
                sell.setVendedor(rs.getString("vendedor"));
                sell.setTotalVenta(rs.getDouble("total_venta"));
                
                listaVentas.add(sell);
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaVentas;
    }

}
