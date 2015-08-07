package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public abstract class GUI extends JFrame implements ActionListener, ListSelectionListener{
	private JTextField rutaCarpetaXml = new JTextField();
	private JLabel datosFactura = new JLabel();
	private JLabel archivoActual;
	private JTable tablaArticulos;
	private JList listaArchivosXml;
	private JButton btnElegirCarpeta;
	private JButton btnProcesar;
	private JMenuBar menuBarr;
	private JMenu menuArchivo, menuAyuda;
	private JMenuItem salirMenuItem, guiaRapidaMenuItem, aboutMenuItem;
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public JMenuItem getSalirMenuItem() {
		return salirMenuItem;
	}

	public JMenuItem getGuiaRapidaMenuItem() {
		return guiaRapidaMenuItem;
	}

	public JMenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	public void setArchivoActual(String archivoActual) {
		this.archivoActual.setText(archivoActual);
	}
	
	public JTable getTablaArticulos() {
		return tablaArticulos;
	}
	
	public JButton getBtnProcesar() {
		return btnProcesar;
	}

	public JButton getBtnElegirCarpeta() {
		return btnElegirCarpeta;
	}

	public String getRutaCarpetaXml() {
		return rutaCarpetaXml.getText();
	}

	public void setRutaCarpetaXml(String rutaCarpetaXml) {
		this.rutaCarpetaXml.setText(rutaCarpetaXml);
	}

	public void setDatosFactura(String datosFactura) {
		this.datosFactura.setText(datosFactura);
	}
	
	public JList getListaArchivosXml(){
		return this.listaArchivosXml;
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public GUI(){	
		barraMenu();
		add(BorderLayout.NORTH, panelCambiarCarpeta());
		rutaCarpetaXml.setEditable(false);
		crearListaScroll(null);
		crearTablaArticulos(null);
		add(BorderLayout.SOUTH, panelSur());
		inicializarVentana();
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	protected void hacerEnabledListaArchivosXml(boolean enabled){
		listaArchivosXml.setEnabled(enabled);
	}
	
	protected void hacerEnabledBtnProcesar(boolean enabled){
		btnProcesar.setEnabled(enabled);
	}
	
	private void barraMenu(){
		menuBarr = new JMenuBar();
		menuArchivo = new JMenu("Archivo");
		menuAyuda = new JMenu("Ayuda");
		salirMenuItem = new JMenuItem("Salir");
		guiaRapidaMenuItem = new JMenuItem("Guia rápida");
		aboutMenuItem = new JMenuItem("Acerca de");
		
		salirMenuItem.addActionListener(this);
		guiaRapidaMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		
		menuArchivo.add(salirMenuItem);
		menuAyuda.add(guiaRapidaMenuItem);
		menuAyuda.add(aboutMenuItem);
		
		menuBarr.add(menuArchivo);
		menuBarr.add(menuAyuda);
		
		setJMenuBar(menuBarr);
	}
	
	private JPanel panelCambiarCarpeta(){
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panelCarpeta = new JPanel(new FlowLayout());
		btnElegirCarpeta = new JButton("Elejir carpeta");
		btnElegirCarpeta.addActionListener(this);
		rutaCarpetaXml.setPreferredSize(new Dimension(300, 30));
		panelCarpeta.add(rutaCarpetaXml);
		panelCarpeta.add(btnElegirCarpeta);
		panel.add(BorderLayout.NORTH, panelCarpeta);
		datosFactura = new JLabel("prueba", JLabel.CENTER);
		panel.add(BorderLayout.CENTER, datosFactura);
		return panel;
	}
	
	private JPanel panelSur(){
		JPanel panel = new JPanel(new BorderLayout());
		btnProcesar = new JButton("Procesar");
		btnProcesar.addActionListener(this);
		btnProcesar.setEnabled(false);
		archivoActual = new JLabel("Para activar el boton procesar, elija un archivo xml de la parte derecha");
		panel.add(BorderLayout.CENTER, archivoActual);
		panel.add(BorderLayout.EAST, btnProcesar);
		return panel;
	}
	
	private void crearListaScroll(String[] listaXml){
		if(listaXml == null)
			listaXml = new String[1];
		
		listaArchivosXml = null;
		listaArchivosXml = new JList(listaXml);
		listaArchivosXml.addListSelectionListener(this);
		
		if(listaXml == null){
			listaArchivosXml.setPreferredSize(new Dimension(getPreferredSize().width / 4, getPreferredSize().height));
			listaArchivosXml.setEnabled(false);
		} else {
			listaArchivosXml.setEnabled(true);
		}
		
		add(BorderLayout.EAST, new JScrollPane(listaArchivosXml));
	}
	
	private void crearTablaArticulos(Object[][] articulos){
		String[] TitulosColumnas = {"Artículo","Descripción","Unidad","impuesto","Cantidad","Precio","Importe", "Elegir"};
		DefaultTableModel dtm= new DefaultTableModel(articulos, TitulosColumnas);
		tablaArticulos = null;
		tablaArticulos = new JTable(dtm){
			public Class getColumnClass(int column){
            	if(column == (TitulosColumnas.length-1)){
                	return Boolean.class;
                }
                return String.class;
            }
		};
		tablaArticulos.setPreferredScrollableViewportSize(tablaArticulos.getPreferredSize());
		add(BorderLayout.CENTER, new JScrollPane(tablaArticulos));
	}
	
	protected void actualizarTablaArticulos(Object[][] articulos){
		remove(tablaArticulos.getParent().getParent());
		crearTablaArticulos(articulos);
		revalidate();
	}
	
	protected void actualizarListaArchivosXml(String[] listaXml){
		remove(listaArchivosXml.getParent().getParent());
		crearListaScroll(listaXml);
		revalidate();
	}
	
	private void inicializarVentana(){
		setTitle("STOCKTAKING HELP - V 1.0");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
