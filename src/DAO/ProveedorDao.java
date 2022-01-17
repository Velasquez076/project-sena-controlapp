package DAO;

import conexion_BD.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model_entity.Proveedor;

/**
 *
 * @author julian076
 */
public class ProveedorDao {

    Conexion conexion = new Conexion();
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarProveedor(Proveedor suplier) {
        String sql = "INSERT INTO proveedores (nit_proveedor,  nombre_proveedor, direccion_proveedor, ciudad_proveedor, telefono_proveedor)"
                + "VALUES(?, ?, ?, ?, ?)";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);

            //enviar datos de los campos.
            ps.setString(1, suplier.getNitProveedor());
            ps.setString(2, suplier.getNombreProveedor());
            ps.setString(3, suplier.getDireccionProveedor());
            ps.setString(4, suplier.getCiudadProveedor());
            ps.setString(5, suplier.getTelefonoProveedor());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public List listarProveedor(){
        
        List <Proveedor> listaProveedor = new ArrayList();
        String sql = "SELECT * FROM proveedores";
        
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            // se recorre la lista con un while
            while(rs.next()){
                Proveedor suplier = new Proveedor();
                suplier.setId(rs.getInt("id"));
                suplier.setNitProveedor(rs.getString("nit_proveedor"));
                suplier.setNombreProveedor(rs.getString("nombre_proveedor"));
                suplier.setDireccionProveedor(rs.getString("direccion_proveedor"));
                suplier.setCiudadProveedor(rs.getString("ciudad_proveedor"));
                suplier.setTelefonoProveedor(rs.getString("telefono_proveedor"));                
                listaProveedor.add(suplier);
                
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaProveedor;
    }
    
     public boolean eliminarProveedor(int id){
        
        String sql = "DELETE FROM proveedores WHERE id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                conn.close();                
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        
    }
     
     public boolean editarProveedor(Proveedor suplier){
        
        String sql = "UPDATE proveedores SET nit_proveedor = ?, nombre_proveedor = ?, direccion_proveedor = ?, ciudad_proveedor = ?, telefono_proveedor = ? WHERE id = ?";
        
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setString(1,suplier.getNitProveedor());
            ps.setString(2,suplier.getNombreProveedor());
            ps.setString(3,suplier.getDireccionProveedor());
            ps.setString(4,suplier.getCiudadProveedor());
            ps.setString(5,suplier.getTelefonoProveedor());
            ps.setInt(6,suplier.getId());
            ps.execute();            
            return true;
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {                
                System.out.println(e.toString());
            }
        }
        
    }
}
