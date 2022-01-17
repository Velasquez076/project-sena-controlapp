package reports;

import conexion_BD.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author julian076
 */
public class Graficos {

    public static void graficar(String fecha) {
        Connection conn;
        Conexion conexion = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        
        String sql = "SELECT total_venta FROM ventas WHERE fecha_venta = ?";

        try {
            conn = conexion.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            DefaultPieDataset dataSet = new DefaultPieDataset();
            while(rs.next()){
                dataSet.setValue(rs.getString("total_venta"), rs.getDouble("total_venta"));
            }
            JFreeChart jf = ChartFactory.createPieChart("Reporte de Venta", dataSet);
            ChartFrame f = new ChartFrame("Total de Ventas Diarias", jf);
            f.setSize(800, 400);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }
}
