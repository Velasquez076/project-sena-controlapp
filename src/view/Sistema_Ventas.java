package view;

import javax.swing.JOptionPane;
import model_entity.Cliente;
import DAO.ClienteDao;
import DAO.ProductoDao;
import DAO.ProveedorDao;
import DAO.VentaDao;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import model_entity.DatosEmpresa;
import model_entity.DetalleVenta;
import model_entity.Eventos;
import model_entity.Login;
import model_entity.Producto;
import model_entity.Proveedor;
import model_entity.Venta;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import reports.Excel;
import reports.Graficos;

/**
 *
 * @author julian076
 */
public class Sistema_Ventas extends javax.swing.JFrame {

    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    // instancia de cliente
    Cliente cliente = new Cliente();
    ClienteDao clienteDao = new ClienteDao();

    //instancia de proveedor
    Proveedor proveedor = new Proveedor();
    ProveedorDao proveedorDao = new ProveedorDao();

    //instancia de productos
    Producto producto = new Producto();
    ProductoDao productoDao = new ProductoDao();

    //instancia de las clases venta
    Venta venta = new Venta();
    VentaDao ventaDao = new VentaDao();

    //instacia de clase DetalleVenta
    DetalleVenta detalle = new DetalleVenta();

    //instancia de la clase datos de la empresa
    DatosEmpresa empresa = new DatosEmpresa();

    //instancia de la clase Eventos
    Eventos validar = new Eventos();
    Login user = new Login();
    login vendedor = new login();

    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp = new DefaultTableModel();

    int item;
    double totalPagar = 0.00;

    /**
     * Creates new form Sistema_Ventas
     */
    public Sistema_Ventas() {
        initComponents();

    }

    public Sistema_Ventas(Login privilegios) {
        initComponents();
        this.setLocationRelativeTo(null);
        jTabbedPane1.setSelectedIndex(1);
        AutoCompleteDecorator.decorate(cbxProveedorProducto);
        productoDao.consultarProveedor(cbxProveedorProducto);
        labelTotalAPagar.setText(String.format("%.2f", totalPagar));
        ocultarCajasTexto();

        if (privilegios.getRol().equals("Auxiliar Caja") || privilegios.getRol().equals("Auxiliar Bodega")) {
            btnProveedores.setVisible(false);
            btnConfig.setVisible(false);
            btnSalir.setVisible(false);

        } else {

        }
    }

    //Metodo para recorrer y mostrar los clientes en la tabla
    public void listarClientes() {
        List<Cliente> listarCliente = clienteDao.listarCliente();
        modelo = (DefaultTableModel) tablaClientes.getModel();
        Object[] obj = new Object[6];
        for (int i = 0; i < listarCliente.size(); i++) {
            obj[0] = listarCliente.get(i).getId();
            obj[1] = listarCliente.get(i).getCedula_cliente();
            obj[2] = listarCliente.get(i).getNombre_cliente();
            obj[3] = listarCliente.get(i).getEmail_cliente();
            obj[4] = listarCliente.get(i).getDireccion_cliente();
            obj[5] = listarCliente.get(i).getTelefono_cliente();

            modelo.addRow(obj);
        }

        tablaClientes.setModel(modelo);
    }

    public void limpiarTabla() {

        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i--;
        }
    }

    //Metodo para recorrer y mostrar los proveedores en la tabla
    public void listarProveedor() {
        List<Proveedor> listarProveedor = proveedorDao.listarProveedor();
        modelo = (DefaultTableModel) tablaProveedores.getModel();
        Object[] obj = new Object[6];
        for (int i = 0; i < listarProveedor.size(); i++) {
            obj[0] = listarProveedor.get(i).getId();
            obj[1] = listarProveedor.get(i).getNitProveedor();
            obj[2] = listarProveedor.get(i).getNombreProveedor();
            obj[3] = listarProveedor.get(i).getDireccionProveedor();
            obj[4] = listarProveedor.get(i).getCiudadProveedor();
            obj[5] = listarProveedor.get(i).getTelefonoProveedor();

            modelo.addRow(obj);
        }

        tablaProveedores.setModel(modelo);
    }

    public void listarProductos() {
        List<Producto> listarProducto = productoDao.listarProductos();
        modelo = (DefaultTableModel) tablaProductos.getModel();
        Object[] obj = new Object[6];
        for (int i = 0; i < listarProducto.size(); i++) {
            obj[0] = listarProducto.get(i).getId();
            obj[1] = listarProducto.get(i).getCodigoProducto();
            obj[2] = listarProducto.get(i).getDescripcion();
            obj[3] = listarProducto.get(i).getStock();
            obj[4] = listarProducto.get(i).getPrecio();
            obj[5] = listarProducto.get(i).getProveedor();

            modelo.addRow(obj);
        }

        tablaProductos.setModel(modelo);
    }

    public void listarDatosEmpresa() {
        empresa = productoDao.consultarDatos();
        txtIdDatosEmpresa.setText("" + empresa.getId());
        txtNitEmpresa.setText("" + empresa.getNit());
        txtNombreEmpresa.setText("" + empresa.getNombreEmpresa());
        txtTelefonoEmpresa.setText("" + empresa.getTelefono());
        txtDireccionEmpresa.setText("" + empresa.getDireccion());
    }

    public void listarVentas() {
        List<Venta> listarVenta = ventaDao.listarVentas();
        modelo = (DefaultTableModel) tablaConsolidadoVentas.getModel();
        Object[] obj = new Object[4];
        for (int i = 0; i < listarVenta.size(); i++) {
            obj[0] = listarVenta.get(i).getId();
            obj[1] = listarVenta.get(i).getClienteVenta();
            obj[2] = listarVenta.get(i).getVendedor();
            obj[3] = listarVenta.get(i).getTotalVenta();

            modelo.addRow(obj);
        }

        tablaConsolidadoVentas.setModel(modelo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnProveedores = new javax.swing.JButton();
        btnProductos = new javax.swing.JButton();
        btnNuevaVenta = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnClientes1 = new javax.swing.JButton();
        btnRegistrousuarios1 = new javax.swing.JButton();
        labelVendedor = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtNitProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtCiudadProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();
        btnNuevoProveedor = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        btnGuardarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCodigoVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponible = new javax.swing.JTextField();
        btnEliminarVenta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVenta = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtCedulaClienteVenta = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtNombreClienteVenta = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        labelTotalAPagar = new javax.swing.JLabel();
        txtIdVenta = new javax.swing.JTextField();
        txtTelefonoClienteVenta = new javax.swing.JTextField();
        txtDireccionClienteVenta = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtNitEmpresa = new javax.swing.JTextField();
        txtNombreEmpresa = new javax.swing.JTextField();
        txtTelefonoEmpresa = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtDireccionEmpresa = new javax.swing.JTextField();
        btnActualizarDatosEmp = new javax.swing.JButton();
        txtIdDatosEmpresa = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtCantidadProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        btnNuevoProducto = new javax.swing.JButton();
        btnGuardarProducto = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        cbxProveedorProducto = new javax.swing.JComboBox<>();
        btnExcelProductos = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaConsolidadoVentas = new javax.swing.JTable();
        btnPdfVentas = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        txtIdConsolidadoVentas = new javax.swing.JTextField();
        btnGraficar = new javax.swing.JButton();
        miDate = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtCedulaCliente = new javax.swing.JTextField();
        txtNombresCliente = new javax.swing.JTextField();
        txtEmailCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        btnNuevoCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnGuardarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setBackground(new java.awt.Color(153, 0, 0));
        btnSalir.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setSelected(true);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 0, 70, 50));

        btnProveedores.setBackground(new java.awt.Color(153, 0, 0));
        btnProveedores.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnProveedores.setForeground(new java.awt.Color(255, 255, 255));
        btnProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/proveedor.png"))); // NOI18N
        btnProveedores.setText("Proveedores");
        btnProveedores.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnProveedores.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProveedores.setSelected(true);
        btnProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedoresActionPerformed(evt);
            }
        });
        jPanel1.add(btnProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 110, 50));

        btnProductos.setBackground(new java.awt.Color(153, 0, 0));
        btnProductos.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnProductos.setForeground(new java.awt.Color(255, 255, 255));
        btnProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/producto.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProductos.setSelected(true);
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });
        jPanel1.add(btnProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 110, 50));

        btnNuevaVenta.setBackground(new java.awt.Color(153, 0, 0));
        btnNuevaVenta.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnNuevaVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Nventa.png"))); // NOI18N
        btnNuevaVenta.setText("Nueva Venta");
        btnNuevaVenta.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnNuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevaVenta.setSelected(true);
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });
        jPanel1.add(btnNuevaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 140, 50));

        btnConfig.setBackground(new java.awt.Color(153, 0, 0));
        btnConfig.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnConfig.setForeground(new java.awt.Color(255, 255, 255));
        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/config.png"))); // NOI18N
        btnConfig.setText("Config");
        btnConfig.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnConfig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfig.setSelected(true);
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });
        jPanel1.add(btnConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 100, 50));

        btnVentas.setBackground(new java.awt.Color(153, 0, 0));
        btnVentas.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnVentas.setForeground(new java.awt.Color(255, 255, 255));
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/compras.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVentas.setSelected(true);
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });
        jPanel1.add(btnVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 0, 100, 50));

        btnClientes1.setBackground(new java.awt.Color(153, 0, 0));
        btnClientes1.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnClientes1.setForeground(new java.awt.Color(255, 255, 255));
        btnClientes1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Clientes.png"))); // NOI18N
        btnClientes1.setText("Clientes");
        btnClientes1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnClientes1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClientes1.setSelected(true);
        btnClientes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientes1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnClientes1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 110, 50));

        btnRegistrousuarios1.setBackground(new java.awt.Color(153, 0, 0));
        btnRegistrousuarios1.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnRegistrousuarios1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrousuarios1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Clientes.png"))); // NOI18N
        btnRegistrousuarios1.setText("Reg.Usuarios");
        btnRegistrousuarios1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        btnRegistrousuarios1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrousuarios1.setSelected(true);
        btnRegistrousuarios1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrousuarios1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrousuarios1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 110, 50));

        labelVendedor.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        labelVendedor.setForeground(new java.awt.Color(255, 255, 255));
        labelVendedor.setText("root");
        jPanel1.add(labelVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 10, -1, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 980, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 740, 140));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/app.png"))); // NOI18N
        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 140));

        jLabel27.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel27.setText("Registro Proveedores");

        jLabel28.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel28.setText("Nit:*");

        jLabel29.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel29.setText("Nombre:*");

        jLabel30.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel30.setText("Dirección:*");

        jLabel31.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel31.setText("Ciudad:*");

        jLabel32.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel32.setText("Teléfono:*");

        txtNitProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNitProveedorKeyTyped(evt);
            }
        });

        txtNombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProveedorKeyTyped(evt);
            }
        });

        txtTelefonoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyTyped(evt);
            }
        });

        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nit", "Nombre", "Direccion", "Ciudad", "Teléfono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProveedoresMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaProveedores);
        if (tablaProveedores.getColumnModel().getColumnCount() > 0) {
            tablaProveedores.getColumnModel().getColumn(0).setPreferredWidth(6);
            tablaProveedores.getColumnModel().getColumn(1).setPreferredWidth(30);
            tablaProveedores.getColumnModel().getColumn(2).setPreferredWidth(90);
            tablaProveedores.getColumnModel().getColumn(3).setPreferredWidth(90);
            tablaProveedores.getColumnModel().getColumn(4).setPreferredWidth(40);
            tablaProveedores.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        btnNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo.png"))); // NOI18N
        btnNuevoProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        btnEditarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Actualizar (2).png"))); // NOI18N
        btnEditarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnGuardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel27)
                .addGap(32, 32, 32)
                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel30)
                                            .addComponent(jLabel29)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel31))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(jLabel32)))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefonoProveedor)
                                    .addComponent(txtNombreProveedor)
                                    .addComponent(txtDireccionProveedor)
                                    .addComponent(txtCiudadProveedor))))
                        .addGap(12, 12, 12)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 643, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtNitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtCiudadProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarProveedor)
                            .addComponent(btnNuevoProveedor)
                            .addComponent(btnEditarProveedor)
                            .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        jTabbedPane1.addTab("", jPanel4);

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel4.setText("Código Prod.");

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel6.setText("Descripción del Prod.");

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel7.setText("Cantidad");

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel8.setText("Precio Unitario");

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel9.setText("Stock");

        txtCodigoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyPressed(evt);
            }
        });

        txtDescripcionVenta.setEditable(false);

        txtCantidadVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadVentaActionPerformed(evt);
            }
        });
        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });

        txtStockDisponible.setEditable(false);

        btnEliminarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnEliminarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        tablaVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tablaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código Prod.", "Descripción", "Cantidad", "Precio Unitario", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaVenta);
        if (tablaVenta.getColumnModel().getColumnCount() > 0) {
            tablaVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            tablaVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablaVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            tablaVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            tablaVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel10.setText("Cedula Cliente");

        txtCedulaClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaClienteVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaClienteVentaKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel11.setText("Nombre Cliente");

        txtNombreClienteVenta.setEditable(false);
        txtNombreClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteVentaActionPerformed(evt);
            }
        });
        txtNombreClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteVentaKeyTyped(evt);
            }
        });

        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/print.png"))); // NOI18N
        btnGenerarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/money.png"))); // NOI18N
        jLabel12.setText("TOTAL A PAGAR: $");

        labelTotalAPagar.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        labelTotalAPagar.setText("____________");

        txtTelefonoClienteVenta.setEditable(false);

        txtDireccionClienteVenta.setEditable(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtCedulaClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(22, 22, 22)
                                .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(26, 26, 26)
                                        .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(labelTotalAPagar))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(101, 101, 101))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEliminarVenta)))
                                .addGap(18, 18, 18)
                                .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 55, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel7))
                                    .addComponent(btnEliminarVenta)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerarVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCedulaClienteVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                            .addComponent(txtNombreClienteVenta)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(labelTotalAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("", jPanel6);

        jLabel39.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel39.setText("Informacion De La Empresa");

        jLabel41.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel41.setText("Nit:*");

        jLabel42.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel42.setText("Nombre / Compañia:*");

        jLabel43.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel43.setText("Teléfono:*");

        txtNitEmpresa.setEditable(false);
        txtNitEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNitEmpresaKeyTyped(evt);
            }
        });

        txtNombreEmpresa.setEditable(false);
        txtNombreEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreEmpresaKeyTyped(evt);
            }
        });

        txtTelefonoEmpresa.setEditable(false);
        txtTelefonoEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoEmpresaKeyTyped(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel44.setText("Dirección:*");

        txtDireccionEmpresa.setEditable(false);

        btnActualizarDatosEmp.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        btnActualizarDatosEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Actualizar (2).png"))); // NOI18N
        btnActualizarDatosEmp.setText("ACTUALIZAR INFORMACIÓN");
        btnActualizarDatosEmp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarDatosEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosEmpActionPerformed(evt);
            }
        });

        txtIdDatosEmpresa.setEditable(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addContainerGap(656, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtNitEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)
                        .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(214, 214, 214)
                .addComponent(jLabel43)
                .addGap(97, 97, 97))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarDatosEmp)
                .addGap(317, 317, 317))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(txtIdDatosEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(346, 346, 346))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(420, 420, 420))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(45, 45, 45)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNitEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(txtIdDatosEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)))
                .addComponent(btnActualizarDatosEmp)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel8);

        jLabel33.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel33.setText("Registro Productos");

        jLabel34.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel34.setText("Código:*");

        jLabel35.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel35.setText("Descripción:*");

        jLabel36.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel36.setText("Cantidad/Stock:*");

        jLabel37.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel37.setText("Proveedor:*");

        jLabel38.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel38.setText("Precio:*");

        txtDescripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionProductoKeyTyped(evt);
            }
        });

        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });

        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Descripción", "Stock", "Precio", "Proveedor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductosMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablaProductos);
        if (tablaProductos.getColumnModel().getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(8);
            tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(40);
            tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(90);
            tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(20);
            tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(40);
            tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        btnNuevoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo.png"))); // NOI18N
        btnNuevoProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProductoActionPerformed(evt);
            }
        });

        btnGuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnEditarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Actualizar (2).png"))); // NOI18N
        btnEditarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        cbxProveedorProducto.setEditable(true);
        cbxProveedorProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        btnExcelProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnExcelProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProductosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel33)
                        .addGap(71, 71, 71)
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(75, 75, 75)
                                        .addComponent(jLabel34))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabel35))
                                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel36)))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cbxProveedorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtCantidadProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnNuevoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(btnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(btnEditarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(25, 25, 25)
                                        .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnExcelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel35)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel36)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbxProveedorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(btnNuevoProducto))
                            .addComponent(btnGuardarProducto)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEditarProducto)
                                    .addComponent(btnEliminarProducto))
                                .addGap(14, 14, 14)
                                .addComponent(btnExcelProductos)))
                        .addContainerGap(57, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(44, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("", jPanel5);

        tablaConsolidadoVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cliente", "Vendedor", "$Total "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaConsolidadoVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaConsolidadoVentasMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tablaConsolidadoVentas);
        if (tablaConsolidadoVentas.getColumnModel().getColumnCount() > 0) {
            tablaConsolidadoVentas.getColumnModel().getColumn(0).setPreferredWidth(17);
            tablaConsolidadoVentas.getColumnModel().getColumn(1).setPreferredWidth(70);
            tablaConsolidadoVentas.getColumnModel().getColumn(2).setPreferredWidth(70);
            tablaConsolidadoVentas.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        btnPdfVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf.png"))); // NOI18N
        btnPdfVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel40.setText("Reporte De Ventas");

        btnGraficar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/torta.png"))); // NOI18N
        btnGraficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficarActionPerformed(evt);
            }
        });

        jLabel13.setText("Seleccionar*");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(btnPdfVentas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(miDate, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(26, 26, 26)
                        .addComponent(btnGraficar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(txtIdConsolidadoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel40)
                .addGap(21, 21, 21)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnPdfVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGraficar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(miDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(txtIdConsolidadoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel7);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel21.setText("Registro Clientes");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 12, -1, -1));

        jLabel22.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel22.setText("Cedula:*");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel23.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel23.setText("Dirección:*");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, -1));

        jLabel24.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel24.setText("E-mail:*");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel25.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel25.setText("Nombres:*");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, -1, -1));

        jLabel26.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel26.setText("Teléfono:*");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

        txtCedulaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaClienteKeyTyped(evt);
            }
        });
        jPanel3.add(txtCedulaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 196, -1));

        txtNombresCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombresClienteKeyTyped(evt);
            }
        });
        jPanel3.add(txtNombresCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 196, -1));
        jPanel3.add(txtEmailCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 196, -1));
        jPanel3.add(txtDireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 196, -1));

        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyTyped(evt);
            }
        });
        jPanel3.add(txtTelefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 196, -1));

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cedula", "Nombres", "E-mail", "Dirección", "Teléfono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaClientes);
        if (tablaClientes.getColumnModel().getColumnCount() > 0) {
            tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(2);
            tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(25);
            tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(90);
            tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(100);
            tablaClientes.getColumnModel().getColumn(5).setPreferredWidth(20);
        }

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 73, 630, 298));

        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nuevo.png"))); // NOI18N
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });
        jPanel3.add(btnNuevoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 48, -1));

        btnEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Actualizar (2).png"))); // NOI18N
        btnEditarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });
        jPanel3.add(btnEditarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 49, -1));

        btnGuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/GuardarTodo.png"))); // NOI18N
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });
        jPanel3.add(btnGuardarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 50, -1));

        btnEliminarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 50, -1));
        jPanel3.add(txtIdCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 18, 14, -1));

        jTabbedPane1.addTab("", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 930, 440));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 50, 240));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background.png"))); // NOI18N
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 50, 220));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro de Cerrar Sesión?");

        if (pregunta == 0) {
            JOptionPane.showMessageDialog(null, "Sesión Finalizada");
            dispose();
            login inicio = new login();
            inicio.setVisible(true);
        }


    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        txtCodigoProducto.requestFocus();
        limpiarTabla();
        listarProductos();
        jTabbedPane1.setSelectedIndex(3);
        btnGuardarProducto.setVisible(true);
        btnNuevoProducto.setVisible(true);
        limpiarCamposProductos();
    }//GEN-LAST:event_btnProductosActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(4);
        limpiarTabla();
        listarVentas();
    }//GEN-LAST:event_btnVentasActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
        btnActualizarDatosEmp.setVisible(false);
        listarDatosEmpresa();
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedoresActionPerformed
        txtNitProveedor.requestFocus();
        limpiarTabla();
        listarProveedor();
        jTabbedPane1.setSelectedIndex(0);
        btnNuevoProveedor.setVisible(true);
        btnGuardarProveedor.setVisible(true);
        txtNitProveedor.requestFocus();
        limpiarCamposProveedor();
    }//GEN-LAST:event_btnProveedoresActionPerformed

    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        txtCodigoVenta.requestFocus();
        limpiarTabla();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    private void btnGraficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficarActionPerformed
        // TODO add your handling code here:
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(miDate.getDate());
        Graficos.graficar(fechaReporte);
    }//GEN-LAST:event_btnGraficarActionPerformed

    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfVentasActionPerformed
         // TODO add your handling code here:        
        if (!"".equals(tablaConsolidadoVentas.getRowCount())) {
            try {
                int id = Integer.parseInt(txtIdConsolidadoVentas.getText());
                File file = new File("src/pdf/venta" + id + ".pdf");
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(null, "Seleccione una fila!");
                Logger.getLogger(Sistema_Ventas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_btnPdfVentasActionPerformed

    private void tablaConsolidadoVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConsolidadoVentasMouseClicked
        // TODO add your handling code here:
        int fila = tablaConsolidadoVentas.rowAtPoint(evt.getPoint());
        txtIdConsolidadoVentas.setText(tablaConsolidadoVentas.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_tablaConsolidadoVentasMouseClicked

    private void btnExcelProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelProductosActionPerformed
        int pregunta = JOptionPane.showConfirmDialog(null, "¿Desea Descargar el Reporte?");
        if (pregunta == 0) {
            Excel.reporte();
        }
    }//GEN-LAST:event_btnExcelProductosActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        if ("".equals(txtIdProducto.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        }
        if (!"".equals(txtIdProducto.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de Eliminar este Producto?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdProducto.getText());
                productoDao.eliminarProducto(id);
                JOptionPane.showMessageDialog(null, "¡Ha Sido Eliminado Con Éxito!");
                limpiarTabla();
                limpiarCamposProductos();
                listarProductos();
                btnGuardarProducto.setVisible(true);
                btnNuevoProducto.setVisible(true);

            }
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductoActionPerformed
        if ("".equals(txtIdProducto.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        } else {
            if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText())
                    || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText()) || !"".equals(cbxProveedorProducto.getSelectedItem())) {

                if (!"".equals(txtIdProducto.getText())) {
                    if ("".equals(txtCodigoProducto.getText()) || "".equals(txtDescripcionProducto.getText())
                            || "".equals(txtCantidadProducto.getText()) || "".equals(txtPrecioProducto.getText()) || "".equals(cbxProveedorProducto.getSelectedItem())) {
                        JOptionPane.showMessageDialog(null, "* ¡No Deben Haber Campos Vacios!");

                    } else {
                        int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro de Actualizar este Producto?");
                        if (pregunta == 0) {
                            int id = Integer.parseInt(txtIdProducto.getText());
                            producto.setCodigoProducto(txtCodigoProducto.getText());
                            producto.setDescripcion(txtDescripcionProducto.getText());
                            producto.setStock(Integer.parseInt(txtCantidadProducto.getText()));
                            producto.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));
                            producto.setProveedor(cbxProveedorProducto.getSelectedItem().toString());
                            producto.setId(Integer.parseInt(txtIdProducto.getText()));
                            productoDao.editarProducto(producto);
                            JOptionPane.showMessageDialog(null, "¡Ha Sido Actualizado Con Éxito!");
                            limpiarTabla();
                            limpiarCamposProductos();
                            listarProductos();
                            btnNuevoProducto.setVisible(true);
                            btnGuardarProducto.setVisible(true);

                        }

                    }

                }

            }

        }
    }//GEN-LAST:event_btnEditarProductoActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText())
                || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText()) || !"".equals(cbxProveedorProducto.getSelectedItem())) {

            producto.setCodigoProducto(txtCodigoProducto.getText());
            producto.setDescripcion(txtDescripcionProducto.getText());
            producto.setStock(Integer.parseInt(txtCantidadProducto.getText()));
            producto.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));
            producto.setProveedor(cbxProveedorProducto.getSelectedItem().toString());

            productoDao.registrarProducto(producto);

            JOptionPane.showMessageDialog(null, "Producto Registrado exitosamente!");
            limpiarTabla();
            limpiarCamposProductos();
            listarProductos();

        } else {
            JOptionPane.showMessageDialog(null, "¡Validar los Campos, Estan Vacios!");
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void btnNuevoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProductoActionPerformed
        // TODO add your handling code here:
        limpiarTabla();
        limpiarCamposProductos();
        listarProductos();
    }//GEN-LAST:event_btnNuevoProductoActionPerformed

    private void tablaProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMouseClicked
        int fila = tablaProductos.rowAtPoint(evt.getPoint());
        txtIdProducto.setText(tablaProductos.getValueAt(fila, 0).toString());
        txtCodigoProducto.setText(tablaProductos.getValueAt(fila, 1).toString());
        txtDescripcionProducto.setText(tablaProductos.getValueAt(fila, 2).toString());
        txtCantidadProducto.setText(tablaProductos.getValueAt(fila, 3).toString());
        txtPrecioProducto.setText(tablaProductos.getValueAt(fila, 4).toString());
        cbxProveedorProducto.setSelectedItem(tablaProductos.getValueAt(fila, 5).toString());
        btnNuevoProducto.setVisible(false);
        btnGuardarProducto.setVisible(false);
    }//GEN-LAST:event_tablaProductosMouseClicked

    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoKeyTyped
        // TODO add your handling code here:
        validar.soloDecimal(evt, txtPrecioProducto);
    }//GEN-LAST:event_txtPrecioProductoKeyTyped

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    private void txtDescripcionProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionProductoKeyTyped
        // TODO add your handling code here:
        validar.soloTexto(evt);
    }//GEN-LAST:event_txtDescripcionProductoKeyTyped

    private void btnActualizarDatosEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosEmpActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtNitEmpresa.getText()) || !"".equals(txtNombreEmpresa.getText())
                || !"".equals(txtDireccionEmpresa.getText()) || !"".equals(txtTelefonoEmpresa.getText())) {

            if (!"".equals(txtIdDatosEmpresa.getText())) {
                if ("".equals(txtNitEmpresa.getText()) || "".equals(txtNombreEmpresa.getText())
                        || "".equals(txtDireccionEmpresa.getText()) || "".equals(txtTelefonoEmpresa.getText())) {
                    JOptionPane.showMessageDialog(null, "* ¡No Deben Haber Campos Vacios!");

                } else {
                    int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro de Actualizar Datos de la Empresa?");
                    if (pregunta == 0) {
                        int id = Integer.parseInt(txtIdDatosEmpresa.getText());
                        empresa.setNit(txtNitEmpresa.getText());
                        empresa.setNombreEmpresa(txtNombreEmpresa.getText());
                        empresa.setDireccion(txtDireccionEmpresa.getText());
                        empresa.setTelefono(txtTelefonoEmpresa.getText());
                        empresa.setId(Integer.parseInt(txtIdDatosEmpresa.getText()));
                        productoDao.editarDatosEmpresa(empresa);
                        JOptionPane.showMessageDialog(null, "¡Los Datos Han Sido Actualizado Con Éxito!");
                        listarDatosEmpresa();
                    }

                }

            }

        }
    }//GEN-LAST:event_btnActualizarDatosEmpActionPerformed

    private void txtTelefonoEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoEmpresaKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtTelefonoEmpresaKeyTyped

    private void txtNombreEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreEmpresaKeyTyped
        // TODO add your handling code here:
        validar.soloTexto(evt);
    }//GEN-LAST:event_txtNombreEmpresaKeyTyped

    private void txtNitEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitEmpresaKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtNitEmpresaKeyTyped

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        // TODO add your handling code here:
        if (tablaVenta.getRowCount() > 0) {
            if (!"".equals(txtNombreClienteVenta.getText())) {
                int pregunta = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Realizar Venta?");
                if (pregunta == 0) {
                    JOptionPane.showMessageDialog(null, "Venta Realizada, Gracias por su Compra!");
                    registrarVenta();
                    registrarDetalle();
                    actualizarStock();
                    pdf();
                    limpiarTablaVenta();
                    totalPagar();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Debes Buscar un Cliente:");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay Productos en la Venta");
        }
    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void txtNombreClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteVentaKeyTyped
        // TODO add your handling code here:
        validar.soloTexto(evt);
    }//GEN-LAST:event_txtNombreClienteVentaKeyTyped

    private void txtNombreClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteVentaActionPerformed

    private void txtCedulaClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaClienteVentaKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtCedulaClienteVentaKeyTyped

    private void txtCedulaClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaClienteVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtCedulaClienteVenta.getText())) {
                String cedula = txtCedulaClienteVenta.getText();
                cliente = clienteDao.buscarCliente(cedula);
                if (cliente.getNombre_cliente() != null) {
                    txtNombreClienteVenta.setText("" + cliente.getNombre_cliente());
                    txtTelefonoClienteVenta.setText("" + cliente.getTelefono_cliente());
                    txtDireccionClienteVenta.setText("" + cliente.getDireccion_cliente());
                    btnGenerarVenta.requestFocus();
                } else {
                    txtCedulaClienteVenta.setText("");
                    txtNombreClienteVenta.setText("");
                    JOptionPane.showMessageDialog(null, "El cliente NO existe!");
                    txtCedulaClienteVenta.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txtCedulaClienteVentaKeyPressed

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        // TODO add your handling code here:
        if (tablaVenta.getRowCount() > 0) {
            modelo = (DefaultTableModel) tablaVenta.getModel();
            modelo.removeRow(tablaVenta.getSelectedRow());
            totalPagar();
            txtCodigoVenta.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "No hay Productos para sacar de la Venta!");
        }
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtCantidadVenta.getText())) {
                String codigo = txtCodigoVenta.getText();
                String descripcion = txtDescripcionVenta.getText();
                int cantidad = Integer.parseInt(txtCantidadVenta.getText());
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                int stock = Integer.parseInt(txtStockDisponible.getText());

                double totalVenta = cantidad * precio;
                if (stock >= cantidad) {
                    item += 1;
                    DefaultTableModel tmp = (DefaultTableModel) tablaVenta.getModel();
                    for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                        if (tablaVenta.getValueAt(i, 1).equals(txtDescripcionVenta.getText())) {
                            JOptionPane.showMessageDialog(null, "El producto ya esta registrado.");
                            limpiarCamposVenta();
                            txtCodigoVenta.requestFocus();
                            return;
                        }
                    }
                    ArrayList lista = new ArrayList();
                    lista.add(item);
                    lista.add(codigo);
                    lista.add(descripcion);
                    lista.add(cantidad);
                    lista.add(precio);
                    lista.add(totalVenta);
                    //agregar lista a la tabla
                    Object[] obj = new Object[5];
                    obj[0] = lista.get(1);
                    obj[1] = lista.get(2);
                    obj[2] = lista.get(3);
                    obj[3] = lista.get(4);
                    obj[4] = lista.get(5);

                    tmp.addRow(obj);
                    tablaVenta.setModel(tmp);
                    totalPagar();
                    limpiarCamposVenta();
                    txtCodigoVenta.requestFocus();

                } else {
                    JOptionPane.showMessageDialog(null, "Stock No Disponible!");
                    txtCantidadVenta.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad!");
            }
        }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed

    private void txtCantidadVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadVentaActionPerformed

    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!"".equals(txtCodigoVenta.getText())) {

                String codigo = txtCodigoVenta.getText();

                producto = productoDao.consultarProductos(codigo);

                if (producto.getDescripcion() != null) {
                    txtDescripcionVenta.setText("" + producto.getDescripcion());
                    txtPrecioVenta.setText("" + producto.getPrecio());
                    txtStockDisponible.setText("" + producto.getStock());
                    txtCantidadVenta.requestFocus();

                } else {
                    JOptionPane.showMessageDialog(null, "Código de producto no existe!");
                    limpiarCamposVenta();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Código del Producto!");
                txtCodigoVenta.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCodigoVentaKeyPressed

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        }
        if (!"".equals(txtIdProveedor.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de Eliminar este Proveedor?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdProveedor.getText());
                proveedorDao.eliminarProveedor(id);
                JOptionPane.showMessageDialog(null, "¡Ha Sido Eliminado Con Éxito!");
                limpiarTabla();
                limpiarCamposProveedor();
                listarProveedor();

            }
        }
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    //PROVEEDORES  - METODOS CRUD
    private void btnGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProveedorActionPerformed
        if (!"".equals(txtNitProveedor.getText()) || !"".equals(txtNombreProveedor.getText())
                || !"".equals(txtDireccionProveedor.getText()) || !"".equals(txtCiudadProveedor.getText()) || !"".equals(txtTelefonoProveedor.getText())) {

            proveedor.setNitProveedor(txtNitProveedor.getText());
            proveedor.setNombreProveedor(txtNombreProveedor.getText());
            proveedor.setDireccionProveedor(txtDireccionProveedor.getText());
            proveedor.setCiudadProveedor(txtCiudadProveedor.getText());
            proveedor.setTelefonoProveedor(txtTelefonoProveedor.getText());

            proveedorDao.registrarProveedor(proveedor);

            JOptionPane.showMessageDialog(null, "Proveedor Registrado exitosamente!");
            limpiarTabla();
            limpiarCamposProveedor();
            listarProveedor();

        } else {
            JOptionPane.showMessageDialog(null, "¡Validar los Campos, Estan Vacios!");
        }
    }//GEN-LAST:event_btnGuardarProveedorActionPerformed

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        } else {
            if (!"".equals(txtNitProveedor.getText()) || !"".equals(txtNombreProveedor.getText())
                    || !"".equals(txtDireccionProveedor.getText()) || !"".equals(txtCiudadProveedor.getText()) || !"".equals(txtTelefonoProveedor.getText())) {

                if (!"".equals(txtIdProveedor.getText())) {
                    if ("".equals(txtNitProveedor.getText()) || "".equals(txtNombreProveedor.getText())
                            || "".equals(txtDireccionProveedor.getText()) || "".equals(txtCiudadProveedor.getText()) || "".equals(txtTelefonoProveedor.getText())) {
                        JOptionPane.showMessageDialog(null, "* ¡No Deben Haber Campos Vacios!");

                    } else {
                        int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro de Actualizar este Proveedor?");
                        if (pregunta == 0) {
                            int id = Integer.parseInt(txtIdProveedor.getText());
                            proveedor.setNitProveedor(txtNitProveedor.getText());
                            proveedor.setNombreProveedor(txtNombreProveedor.getText());
                            proveedor.setDireccionProveedor(txtDireccionProveedor.getText());
                            proveedor.setCiudadProveedor(txtCiudadProveedor.getText());
                            proveedor.setTelefonoProveedor(txtTelefonoProveedor.getText());
                            proveedor.setId(Integer.parseInt(txtIdProveedor.getText()));
                            proveedorDao.editarProveedor(proveedor);
                            JOptionPane.showMessageDialog(null, "¡Ha Sido Actualizado Con Éxito!");
                            limpiarTabla();
                            limpiarCamposProveedor();
                            listarProveedor();
                            btnNuevoProveedor.setVisible(true);
                            btnGuardarProveedor.setVisible(true);

                        }

                    }

                }

            }

        }
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorActionPerformed
        txtNitProveedor.requestFocus();
        limpiarTabla();
        limpiarCamposProveedor();
        listarProveedor();
    }//GEN-LAST:event_btnNuevoProveedorActionPerformed

    private void tablaProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedoresMouseClicked
        int fila = tablaProveedores.rowAtPoint(evt.getPoint());
        txtIdProveedor.setText(tablaProveedores.getValueAt(fila, 0).toString());
        txtNitProveedor.setText(tablaProveedores.getValueAt(fila, 1).toString());
        txtNombreProveedor.setText(tablaProveedores.getValueAt(fila, 2).toString());
        txtDireccionProveedor.setText(tablaProveedores.getValueAt(fila, 3).toString());
        txtCiudadProveedor.setText(tablaProveedores.getValueAt(fila, 4).toString());
        txtTelefonoProveedor.setText(tablaProveedores.getValueAt(fila, 5).toString());
        btnNuevoProveedor.setVisible(false);
        btnGuardarProveedor.setVisible(false);
    }//GEN-LAST:event_tablaProveedoresMouseClicked

    private void txtTelefonoProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtTelefonoProveedorKeyTyped

    private void txtNombreProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProveedorKeyTyped
        // TODO add your handling code here:
        validar.soloTexto(evt);
    }//GEN-LAST:event_txtNombreProveedorKeyTyped

    private void txtNitProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitProveedorKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtNitProveedorKeyTyped

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        }
        if (!"".equals(txtIdCliente.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de Eliminar este Cliente?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdCliente.getText());
                clienteDao.eliminarCliente(id);
                JOptionPane.showMessageDialog(null, "¡Ha Sido Eliminado Con Éxito!");
                limpiarTabla();
                limpiarCamposClientes();
                listarClientes();
                btnGuardarCliente.setVisible(true);
                btnNuevoCliente.setVisible(true);

            }
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed

        if (!"".equals(txtCedulaCliente.getText()) || !"".equals(txtNombresCliente.getText())
                || !"".equals(txtEmailCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtTelefonoCliente.getText())) {

            cliente.setCedula_cliente(txtCedulaCliente.getText());
            cliente.setNombre_cliente(txtNombresCliente.getText());
            cliente.setEmail_cliente(txtEmailCliente.getText());
            cliente.setDireccion_cliente(txtDireccionCliente.getText());
            cliente.setTelefono_cliente(txtTelefonoCliente.getText());

            clienteDao.registrarCliente(cliente);

            JOptionPane.showMessageDialog(null, "Cliente Registrado exitosamente!");
            limpiarTabla();
            limpiarCamposClientes();
            listarClientes();

        } else {
            JOptionPane.showMessageDialog(null, "Validar los Campos, Estan Vacios!");
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    //Boton que realiza la pregunta y envio de informacion a actualizar al DAO
    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        if ("".equals(txtIdCliente.getText())) {
            JOptionPane.showMessageDialog(null, "¡Seleccione Una Fila!");
        } else {

            if (!"".equals(txtCedulaCliente.getText()) || !"".equals(txtNombresCliente.getText())
                    || !"".equals(txtEmailCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtTelefonoCliente.getText())) {

                if (!"".equals(txtIdCliente.getText())) {
                    if ("".equals(txtCedulaCliente.getText()) || "".equals(txtNombresCliente.getText())
                            || "".equals(txtEmailCliente.getText()) || "".equals(txtDireccionCliente.getText()) || "".equals(txtTelefonoCliente.getText())) {
                        JOptionPane.showMessageDialog(null, "* ¡No Deben Haber Campos Vacios!");

                    } else {
                        int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro de Actualizar este Cliente?");
                        if (pregunta == 0) {
                            int id = Integer.parseInt(txtIdCliente.getText());
                            cliente.setCedula_cliente(txtCedulaCliente.getText());
                            cliente.setNombre_cliente(txtNombresCliente.getText());
                            cliente.setEmail_cliente(txtEmailCliente.getText());
                            cliente.setDireccion_cliente(txtDireccionCliente.getText());
                            cliente.setTelefono_cliente(txtTelefonoCliente.getText());
                            cliente.setId(Integer.parseInt(txtIdCliente.getText()));
                            clienteDao.editarCliente(cliente);
                            JOptionPane.showMessageDialog(null, "¡Ha Sido Actualizado Con Éxito!");
                            limpiarTabla();
                            limpiarCamposClientes();
                            listarClientes();
                            btnNuevoCliente.setVisible(true);
                            btnGuardarCliente.setVisible(true);

                        }

                    }

                }

            }

        }
    }//GEN-LAST:event_btnEditarClienteActionPerformed

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        txtCedulaCliente.requestFocus();
        limpiarTabla();
        limpiarCamposClientes();
        listarClientes();
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        int fila = tablaClientes.rowAtPoint(evt.getPoint());
        txtIdCliente.setText(tablaClientes.getValueAt(fila, 0).toString());
        txtCedulaCliente.setText(tablaClientes.getValueAt(fila, 1).toString());
        txtNombresCliente.setText(tablaClientes.getValueAt(fila, 2).toString());
        txtEmailCliente.setText(tablaClientes.getValueAt(fila, 3).toString());
        txtDireccionCliente.setText(tablaClientes.getValueAt(fila, 4).toString());
        txtTelefonoCliente.setText(tablaClientes.getValueAt(fila, 5).toString());
        btnGuardarCliente.setVisible(false);
        btnNuevoCliente.setVisible(false);
    }//GEN-LAST:event_tablaClientesMouseClicked

    private void txtTelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtTelefonoClienteKeyTyped

    private void txtNombresClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresClienteKeyTyped
        // TODO add your handling code here:
        validar.soloTexto(evt);
    }//GEN-LAST:event_txtNombresClienteKeyTyped

    private void txtCedulaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaClienteKeyTyped
        // TODO add your handling code here:
        validar.soloNumeros(evt);
    }//GEN-LAST:event_txtCedulaClienteKeyTyped

    private void btnClientes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientes1ActionPerformed
        // TODO add your handling code here:
        txtCedulaCliente.requestFocus();
        limpiarTabla();
        listarClientes();
        jTabbedPane1.setSelectedIndex(5);
        btnGuardarCliente.setVisible(true);
        btnNuevoCliente.setVisible(true);
        txtCedulaCliente.requestFocus();
        limpiarCamposClientes();
    }//GEN-LAST:event_btnClientes1ActionPerformed

    private void btnRegistrousuarios1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrousuarios1ActionPerformed
        // TODO add your handling code here:
        RegistroUsuarios user = new RegistroUsuarios();
        user.setVisible(true);
    }//GEN-LAST:event_btnRegistrousuarios1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema_Ventas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema_Ventas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema_Ventas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema_Ventas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema_Ventas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarDatosEmp;
    private javax.swing.JButton btnClientes1;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnExcelProductos;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficar;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnGuardarProveedor;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoProducto;
    private javax.swing.JButton btnNuevoProveedor;
    private javax.swing.JButton btnPdfVentas;
    private javax.swing.JButton btnProductos;
    private javax.swing.JButton btnProveedores;
    private javax.swing.JButton btnRegistrousuarios1;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVentas;
    private javax.swing.JComboBox<String> cbxProveedorProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelTotalAPagar;
    private javax.swing.JLabel labelVendedor;
    private com.toedter.calendar.JDateChooser miDate;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaConsolidadoVentas;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaProveedores;
    private javax.swing.JTable tablaVenta;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCedulaCliente;
    private javax.swing.JTextField txtCedulaClienteVenta;
    private javax.swing.JTextField txtCiudadProveedor;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCodigoVenta;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDireccionClienteVenta;
    private javax.swing.JTextField txtDireccionEmpresa;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JTextField txtEmailCliente;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdConsolidadoVentas;
    private javax.swing.JTextField txtIdDatosEmpresa;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtNitEmpresa;
    private javax.swing.JTextField txtNitProveedor;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreEmpresa;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNombresCliente;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStockDisponible;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoClienteVenta;
    private javax.swing.JTextField txtTelefonoEmpresa;
    private javax.swing.JTextField txtTelefonoProveedor;
    // End of variables declaration//GEN-END:variables
private void limpiarCamposClientes() {

        txtIdCliente.setText("");
        txtCedulaCliente.setText("");
        txtNombresCliente.setText("");
        txtEmailCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");

    }

    private void limpiarCamposProveedor() {
        txtIdProveedor.setText("");
        txtNitProveedor.setText("");
        txtNombreProveedor.setText("");
        txtDireccionProveedor.setText("");
        txtCiudadProveedor.setText("");
        txtTelefonoProveedor.setText("");
    }

    private void limpiarCamposProductos() {
        txtIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtDescripcionProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        cbxProveedorProducto.setSelectedItem("");
    }

    private void limpiarCamposVenta() {
        txtCodigoVenta.setText("");
        txtDescripcionVenta.setText("");
        txtCantidadVenta.setText("");
        txtPrecioVenta.setText("");
        txtStockDisponible.setText("");
        txtIdVenta.setText("");
    }

    private void totalPagar() {
        totalPagar = 0.00;
        int fila = tablaVenta.getRowCount();

        for (int i = 0; i < fila; i++) {
            double calcular = Double.parseDouble(String.valueOf(tablaVenta.getModel().getValueAt(i, 4)));
            if (fila >= 0) {
                totalPagar = totalPagar + calcular;
            } else {
                totalPagar = 0.00;
            }

        }
        labelTotalAPagar.setText(String.format("%.2f", totalPagar));

    }

    private void registrarVenta() {
        String cliente = txtNombreClienteVenta.getText();
        String vendedor = labelVendedor.getText();
        double totalVenta = totalPagar;
        venta.setClienteVenta(cliente);
        venta.setVendedor(vendedor);
        venta.setTotalVenta(totalVenta);
        venta.setFecha(fechaActual);
        ventaDao.registrarVenta(venta);

    }

    private void ocultarCajasTexto() {
        txtIdCliente.setVisible(false);
        txtIdProveedor.setVisible(false);
        txtIdProducto.setVisible(false);
        txtIdVenta.setVisible(false);
        txtTelefonoClienteVenta.setVisible(false);
        txtDireccionClienteVenta.setVisible(false);
        txtIdDatosEmpresa.setVisible(false);
        txtIdConsolidadoVentas.setVisible(false);
    }

    private void registrarDetalle() {
        int id = ventaDao.idVenta();
        for (int i = 0; i < tablaVenta.getRowCount(); i++) {
            String codigo = tablaVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tablaVenta.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(tablaVenta.getValueAt(i, 3).toString());

            detalle.setCodigoProducto(codigo);
            detalle.setCantidad(cantidad);
            detalle.setPrecioVenta(precio);
            detalle.setId(id);
            ventaDao.registrarDetalleVenta(detalle);
        }
    }

    private void actualizarStock() {
        for (int i = 0; i < tablaVenta.getRowCount(); i++) {
            String codigo = tablaVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tablaVenta.getValueAt(i, 2).toString());

            producto = productoDao.consultarProductos(codigo);

            int stockActual = producto.getStock() - cantidad;

            ventaDao.actualizarStock(stockActual, codigo);
        }
    }

    private void limpiarTablaVenta() {
        tmp = (DefaultTableModel) tablaVenta.getModel();
        int fila = tablaVenta.getRowCount();

        for (int i = 0; i < fila; i++) {
            tmp.removeRow(0);

        }

        txtNombreClienteVenta.setText("");
        txtCedulaClienteVenta.setText("");
        txtDireccionClienteVenta.setText("");
        txtTelefonoClienteVenta.setText("");
    }

    private void pdf() {
        try {
            int id = ventaDao.idVenta();
            FileOutputStream pdf;
            File miFile = new File("src/pdf/venta" + id + ".pdf");
            pdf = new FileOutputStream(miFile);
            Document miDocumento = new Document();
            PdfWriter.getInstance(miDocumento, pdf);
            miDocumento.open();
            Image img = Image.getInstance("src/img/app.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Factura: " + id + "\n" + "Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};

            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            String nit = txtNitEmpresa.getText();
            String nombre = txtNombreEmpresa.getText();
            String telefono = txtTelefonoEmpresa.getText();
            String direccion = txtDireccionEmpresa.getText();

            encabezado.addCell("");
            encabezado.addCell("Nit: " + nit + "\nNombre: " + nombre + "\nTeléfono: " + telefono + "\nDirección: " + direccion);
            encabezado.addCell(fecha);
            miDocumento.add(encabezado);

            Paragraph cliente = new Paragraph();
            cliente.add(Chunk.NEWLINE);
            cliente.add("Datos del Cliente: " + "\n\n");
            miDocumento.add(cliente);

            PdfPTable tablaCliente = new PdfPTable(4);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.getDefaultCell().setBorder(0);
            float[] columnaCliente = new float[]{20f, 50f, 30f, 40f};

            tablaCliente.setWidths(columnaCliente);
            tablaCliente.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cliente1 = new PdfPCell(new Phrase("N° Cedula", negrita));
            PdfPCell cliente2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cliente3 = new PdfPCell(new Phrase("Teléfono", negrita));
            PdfPCell cliente4 = new PdfPCell(new Phrase("Dirección", negrita));

            cliente1.setBorder(0);
            cliente2.setBorder(0);
            cliente3.setBorder(0);
            cliente4.setBorder(0);

            tablaCliente.addCell(cliente1);
            tablaCliente.addCell(cliente2);
            tablaCliente.addCell(cliente3);
            tablaCliente.addCell(cliente4);

            tablaCliente.addCell(txtCedulaClienteVenta.getText());
            tablaCliente.addCell(txtNombreClienteVenta.getText());
            tablaCliente.addCell(txtTelefonoClienteVenta.getText());
            tablaCliente.addCell(txtDireccionClienteVenta.getText());
            cliente.add("\n\n");

            miDocumento.add(tablaCliente);

            //Productos             
            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.getDefaultCell().setBorder(0);
            float[] columnaProductos = new float[]{10f, 50f, 15f, 20f};

            tablaProductos.setWidths(columnaProductos);
            tablaProductos.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell product1 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell product2 = new PdfPCell(new Phrase("Descripción", negrita));
            PdfPCell product3 = new PdfPCell(new Phrase("Precio Unit.", negrita));
            PdfPCell product4 = new PdfPCell(new Phrase("Valor Total", negrita));

            product1.setBorder(0);
            product2.setBorder(0);
            product3.setBorder(0);
            product4.setBorder(0);

            //background de los encbaezados
            product1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            product2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            product3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            product4.setBackgroundColor(BaseColor.LIGHT_GRAY);

            tablaProductos.addCell(product1);
            tablaProductos.addCell(product2);
            tablaProductos.addCell(product3);
            tablaProductos.addCell(product4);

            //for para recorrer la tabla de ventas y obtener los productos registrados
            for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                String producto = tablaVenta.getValueAt(i, 1).toString();
                String cantidad = tablaVenta.getValueAt(i, 2).toString();
                String precioUnitario = tablaVenta.getValueAt(i, 3).toString();
                String total = tablaVenta.getValueAt(i, 4).toString();

                // se agregan valores a las celdas
                tablaProductos.addCell(cantidad);
                tablaProductos.addCell(producto);
                tablaProductos.addCell(precioUnitario);
                tablaProductos.addCell(total);

            }

            miDocumento.add(tablaProductos);

            //parrafo de informacion de total a pagar            
            Paragraph totalPdf = new Paragraph();
            totalPdf.add(Chunk.NEWLINE);
            totalPdf.add("Total a Pagar: $" + totalPagar);
            totalPdf.setAlignment(Element.ALIGN_RIGHT);
            miDocumento.add(totalPdf);

            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelacion y Firma\n\n");
            firma.add("-----------------------------------------------------------------------");
            firma.setAlignment(Element.ALIGN_CENTER);
            miDocumento.add(firma);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("¡Gracias por su compra y preferencia!");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            miDocumento.add(mensaje);

            miDocumento.close();
            pdf.close();
            Desktop.getDesktop().open(miFile);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

}
