package DAO;

/**
 *
 * @author julian076
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion_BD.Conexion;
import model_entity.Login;

public class LoginDao {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    Conexion conexion = new Conexion();

    public Login log(String usuario, String password) {

        Login user = new Login();
        String sql = "SELECT * FROM usuarios WHERE usuario =? AND password =?";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setCedula_usuario(rs.getString("cedula_usuario"));
                user.setEmail_usuario(rs.getString("email_usuario"));
                user.setNombres_usuario(rs.getString("nombres_usuario"));
                user.setUsuario(rs.getString("usuario"));
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return user;
    }

    public boolean registroUser(Login user) {
        String sql = "INSERT INTO usuarios(cedula_usuario, email_usuario, nombres_usuario, usuario, password, rol) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getCedula_usuario());
            ps.setString(2, user.getEmail_usuario());
            ps.setString(3, user.getNombres_usuario());
            ps.setString(4, user.getUsuario());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRol());

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }

    }
}
