
package DAO;

/**
 *
 * @author julian076
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model_entity.Cliente;
import conexion_BD.Conexion;

public class ClienteDao {
    
    Conexion conexion = new Conexion();
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
   
    public boolean registrarCliente(Cliente client){
        String sql = "INSERT INTO clientes (cedula_cliente, nombre_cliente, email_cliente, direccion_cliente, telefono_cliente) "
                + "VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            
            //se envian datos 
            ps.setString(1, client.getCedula_cliente());
            ps.setString(2, client.getNombre_cliente());
            ps.setString(3, client.getEmail_cliente());
            ps.setString(4, client.getDireccion_cliente());
            ps.setString(5, client.getTelefono_cliente());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        
    }
    
    public List listarCliente(){
        
        List<Cliente> listaCl = new ArrayList();
        String sql = "SELECT * FROM clientes";
        
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Cliente client = new Cliente();
                client.setId(rs.getInt("id"));
                client.setCedula_cliente(rs.getString("cedula_cliente"));
                client.setNombre_cliente(rs.getString("nombre_cliente"));
                client.setEmail_cliente(rs.getString("email_cliente"));
                client.setDireccion_cliente(rs.getString("direccion_cliente"));
                client.setTelefono_cliente(rs.getString("telefono_cliente"));
                listaCl.add(client);
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaCl;
    }
    
    public boolean eliminarCliente(int id){
        
        String sql = "DELETE FROM clientes WHERE id = ?";
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
    
    public boolean editarCliente(Cliente client){
        
        String sql = "UPDATE clientes SET cedula_cliente = ?, nombre_cliente = ?, email_cliente = ?, direccion_cliente = ?, telefono_cliente = ? WHERE id = ?";
        
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setString(1,client.getCedula_cliente());
            ps.setString(2,client.getNombre_cliente());
            ps.setString(3,client.getEmail_cliente());
            ps.setString(4,client.getDireccion_cliente());
            ps.setString(5,client.getTelefono_cliente());
            ps.setInt(6,client.getId());
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
    
    public Cliente buscarCliente(String cedula){
        Cliente client = new Cliente();
        String sql = "SELECT *  FROM clientes WHERE cedula_cliente = ?";
        
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();
            if(rs.next()){
                client.setNombre_cliente(rs.getString("nombre_cliente"));
                client.setTelefono_cliente(rs.getString("telefono_cliente"));
                client.setDireccion_cliente(rs.getString("direccion_cliente"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return client;
    }
    
}
