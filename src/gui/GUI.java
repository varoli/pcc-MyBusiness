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
	
	public void setArchivoActual(JLabel archivoActual) {
		this.archivoActual = archivoActual;
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

	public String getDatosFactura() {
		return datosFactura.getText();
	}

	public void setDatosFactura(String datosFactura) {
		this.datosFactura.setText(datosFactura);
	}
	
	public JList getListaArchivosXml(){
		return this.listaArchivosXml;
	}
	
	public void hacerEnabledListaArchivosXml(boolean enabled){
		listaArchivosXml.setEnabled(enabled);
	}

	public GUI(){	
		add(BorderLayout.NORTH, panelCambiarCarpeta());
		rutaCarpetaXml.setEditable(false);
		String[] listaXml = {""};
		crearListaScroll(listaXml);
		crearTablaArticulos(null);
		add(BorderLayout.SOUTH, panelSur());
		inicializarVentana();
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
		datosFactura = new JLabel("prueba");
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
		listaArchivosXml = null;
		listaArchivosXml = new JList(listaXml);
		listaArchivosXml.addListSelectionListener(this);
		if(listaXml[0] == ""){
			listaArchivosXml.setPreferredSize(new Dimension(getPreferredSize().width / 4, getPreferredSize().height));
			listaArchivosXml.setEnabled(false);
		}else{
			listaArchivosXml.setEnabled(true);
		}
		add(BorderLayout.EAST, new JScrollPane(listaArchivosXml));
	}
	
	private void crearTablaArticulos(Object[][] articulos){
		String[] TitulosColumnas = {"Artículo","Descripción","Unidad","Cantidad","Precio","Importe", "Elegir"};
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
	
	public void actualizarTablaArticulos(Object[][] articulos){
		remove(tablaArticulos.getParent().getParent());
		crearTablaArticulos(articulos);
		revalidate();
	}
	
	public void actualizarListaArchivosXml(String[] listaXml){
		remove(listaArchivosXml.getParent().getParent());
		crearListaScroll(listaXml);
		revalidate();
	}
	
	private void inicializarVentana(){
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
