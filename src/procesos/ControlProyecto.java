package procesos;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.w3c.dom.*;
import gui.*;

public class ControlProyecto extends GUI{
	private HerramientaArchivo herramientaArchivo;
	private ManejadorBd manejadorBD;
	private Dato datoXml;
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public ControlProyecto(ManejadorBd manejadorBD, HerramientaArchivo herramientaArchivo){
		super();
		this.herramientaArchivo = herramientaArchivo;
		this.manejadorBD = manejadorBD;
		setRutaCarpetaXml(herramientaArchivo.leerArchivoTexto("foldSel.data"));
		setDatosFactura(datosFactura("Ninguno", "Ninguno", "Ninguno", "Ninguno", "Ninguno"));
		
		if(getRutaCarpetaXml() != null){
			mostrarArchivosXmlDisponible();
		}
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	private void mostrarArchivosXmlDisponible() {
		hacerEnabledListaArchivosXml(true);
		actualizarListaArchivosXml(herramientaArchivo.obtenerNombresArchivosXml(getRutaCarpetaXml()));	
	}
	
	private void eventoBotonElegirCarpeta(){
		String rutaCarpeta = herramientaArchivo.elegirRutaCarpetaXml(getRutaCarpetaXml());
		if(rutaCarpeta != null){
			setRutaCarpetaXml(rutaCarpeta);
			mostrarArchivosXmlDisponible();
			actualizarTablaArticulos(null);
			hacerEnabledBtnProcesar(false);
			setArchivoActual("Para activar el boton procesar, elija un archivo xml de la parte derecha");
			herramientaArchivo.escribirArchivoTexto("foldSel.data", rutaCarpeta);
			herramientaArchivo.crearCarpeta(getRutaCarpetaXml() + "procesados\\");
			herramientaArchivo.hacerInvisibleCarpeta(getRutaCarpetaXml() + "procesados");
		}
	}
	
	private void eventoBotonProcesar(){
		int indexColumnaFinal = getTablaArticulos().getColumnCount() - 1;
		Object[][] datoXML= datoXml.getArticulos();
		TableModel contenidoTablaArticulo = getTablaArticulos().getModel();
		getBtnProcesar().setEnabled(false);
		setArchivoActual("Para activar el boton procesar, elija un archivo xml de la parte derecha");
		for(int i=0; i<getTablaArticulos().getRowCount(); i++){
			if(!Boolean.parseBoolean(contenidoTablaArticulo.getValueAt(i, indexColumnaFinal).toString()))
				datoXML[i][indexColumnaFinal] = contenidoTablaArticulo.getValueAt(i, indexColumnaFinal).toString();
		}
		datoXml.setArticulos(datoXML);
		manejadorBD.actualizarBd(datoXml);
		herramientaArchivo.moverArchivo(getRutaCarpetaXml() + getListaArchivosXml().getSelectedValue().toString(), 
				getRutaCarpetaXml() + "procesados\\" + getListaArchivosXml().getSelectedValue().toString());
		//hacerEnabledListaArchivosXml(false);
		actualizarTablaArticulos(null);
		mostrarArchivosXmlDisponible();
		setDatosFactura("Factura procesada");
	}

	private void actualizarContenidoGUI(String nombreArchivoXml){
		setArchivoActual(getRutaCarpetaXml() + nombreArchivoXml);
		hacerEnabledBtnProcesar(true);
		setDatosFactura(datosFactura(datoXml.getNombreEmisorFactura(), datoXml.getRfcEmisorFactura(), datoXml.getFolioFiscal(), datoXml.getFolioFactura(), datoXml.getFechaFactura()));
		actualizarTablaArticulos(datoXml.getArticulos());
	}
	
	private void eventoJListArchivoXml(String nombreArchivoXml){
		ManejadorXml mnXml = new ManejadorXml();
		datoXml = null;
		datoXml = new Dato();
		Document datosXml = mnXml.leerXML(new File(getRutaCarpetaXml() + nombreArchivoXml));
		datoXml.setNombreEmisorFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Emisor", "nombre"));
		datoXml.setRfcEmisorFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Emisor", "rfc"));
		datoXml.setFolioFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Comprobante", "folio"));
		datoXml.setFechaFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Comprobante", "fecha"));
		datoXml.setFolioFiscal(mnXml.obtenerValorAtributo(datosXml, "tfd:TimbreFiscalDigital", "UUID"));
		datoXml.setArticulos(mnXml.colectarDatosXml(datosXml, 8)); //Arreglo de articulos
		actualizarContenidoGUI(nombreArchivoXml);
	}
	
	private String datosFactura(String nombreEmisorFactura, String rfcEmisorFactura, String folioFiscal, String folioFactura, String fechaFactura){
		return "<html><head><style>table{margin:10px;} td{font-size:9px; padding:3px; text-align:center;} .brd-right{border-right:3px solid #7a8a99;}</style></head><body><table>"
				+ "<tr align='center'><td colspan='3'> EMISOR: <i> " + nombreEmisorFactura + "</i></td></tr>"
				+ "<tr><td class='brd-right'> RFC: <i>"+ rfcEmisorFactura +"</i></td><td class='brd-right'> FOLIO FISCAL: <i>" + folioFiscal + "</i></td><td> FOLIO FACTURA: <i>" + folioFactura + "</i> </td></tr>"
				+ "<tr><td colspan='3'> FECHA: <i>" + fechaFactura + "</i></td></tr>"
				+ "</table></body></html>";
	}
	
	private void verArchivosProcesados() {
		Runtime r= Runtime.getRuntime();
		Process p= null;
		try{
			p= r.exec("explorer.exe " + getRutaCarpetaXml() + "procesados");
		}catch (Exception e) {
			System.out.println("Error al intentar ver la carpeta con los archivos procesados, consulte al técnico");
		}
	}
	
	private void acercaDe(){
		//String cadena = "Javier Burón Gutiérrez\nLizeth Vásquez Rojas";
		String cadena = "Desarrollado por:   Estancias profesionales 2015 \n        Lic. Javier Burón Gutiérrez\n        Lic. Lizeth Vásquez Rojas \n\n Dirigido por:    PCC\n       Ing. Ricardo Martínez Velázquez\n\n Versión 1.0";
		JOptionPane.showMessageDialog(null, cadena, "STOCKTAKING HELP", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	/* Eventos en click en los componentes botones y menu */
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(getBtnElegirCarpeta()))
			eventoBotonElegirCarpeta();
		else if(arg0.getSource().equals(getBtnProcesar()))
			eventoBotonProcesar();
		else if(arg0.getSource().equals(getSalirMenuItem()))
			System.exit(0);
		else if(arg0.getSource().equals(getArchProcesadosMenuItem()))
			verArchivosProcesados();
		else if(arg0.getSource().equals(getGuiaRapidaMenuItem()))
			System.out.println("Guia Rápida");
		else if(arg0.getSource().equals(getAboutMenuItem()))
			acercaDe();
	}
	
	/* Evento en lista de archivos xml */
	public void valueChanged(ListSelectionEvent e) {
		if((e.getSource() == getListaArchivosXml()) && !e.getValueIsAdjusting()){
			eventoJListArchivoXml(getListaArchivosXml().getSelectedValue().toString());
		}
	}
	
	/* eventos de la ventana */
	@Override
	public void windowClosed(WindowEvent e){}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		manejadorBD.cerrarConexion();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}
