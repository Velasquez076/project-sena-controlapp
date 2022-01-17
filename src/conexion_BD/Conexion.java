
package conexion_BD;

/**
 *
 * @author julian076
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection conn;
    
    public Connection getConnection(){
        
        try {
            String myDB = "jdbc:mysql://localhost:3306/sistema_controlapp_sena?serverTimezone=UTC";
            conn = DriverManager.getConnection(myDB,"root","Velasquez7632*");
            return conn;
        } catch (SQLException e) {
            
            System.out.println(e.toString());
            
        }
        return null;
    }
    
}
