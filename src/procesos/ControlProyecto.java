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
	private Dato datoXml;
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public ControlProyecto(){
		super();
		herramientaArchivo = new HerramientaArchivo();
		setRutaCarpetaXml(herramientaArchivo.leerArchivoTexto("foldSel.data"));
		if(getRutaCarpetaXml() != null){
			mostrarArchivosXmlDisponible();
		}
	}
	
	private void mostrarArchivosXmlDisponible() {
		hacerEnabledListaArchivosXml(true);
		actualizarListaArchivosXml(herramientaArchivo.obtenerNombresArchivosXml(getRutaCarpetaXml()));	
	}
	
	public void eventoBotonElegirCarpeta(){
		String rutaCarpeta = herramientaArchivo.elegirRutaCarpetaXml(getRutaCarpetaXml());
		if(rutaCarpeta != null){
			setRutaCarpetaXml(rutaCarpeta);
			mostrarArchivosXmlDisponible();
			actualizarTablaArticulos(null);
			hacerEnabledBtnProcesar(false);
			setArchivoActual("Para activar el boton procesar, elija un archivo xml de la parte derecha");
			herramientaArchivo.escribirArchivoTexto("foldSel.data", rutaCarpeta);
			herramientaArchivo.crearCarpeta(getRutaCarpetaXml() + "\\procesados\\");
		}
	}
	
	public void eventoBotonProcesar(){
		ManejadorBd mnBd = new ManejadorBd();
		int indexColumnaFinal = getTablaArticulos().getColumnCount() - 1;
		Object[][] datoXML= datoXml.getArticulos();
		TableModel contenidoTablaArticulo = getTablaArticulos().getModel();
		quitarArchivoDeLista(getListaArchivosXml().getSelectedIndex());
		for(int i=0; i<getTablaArticulos().getRowCount(); i++){
			if(Boolean.parseBoolean(contenidoTablaArticulo.getValueAt(i, indexColumnaFinal).toString()))
				datoXML[i][indexColumnaFinal] = contenidoTablaArticulo.getValueAt(i, indexColumnaFinal).toString();
		}
		datoXml.setArticulos(datoXML);
		mnBd.actualizarBd(datoXml);
		herramientaArchivo.moverArchivo(getRutaCarpetaXml() + getListaArchivosXml().getSelectedValue().toString(), 
				getRutaCarpetaXml() + "procesados\\" + getListaArchivosXml().getSelectedValue().toString());
		hacerEnabledListaArchivosXml(false);
		actualizarTablaArticulos(null);
		mostrarArchivosXmlDisponible();
	}

	private void actualizarContenidoGUI(String nombreArchivoXml){
		String labelDatosFactura = "<html><head><style>table{margin:10px;} td{font-size:9px; padding:3px; text-align:center;} .brd-right{border-right:3px solid #7a8a99;}</style></head><body><table>"
				+ "<tr align='center'><td colspan='3'> EMISOR: " + datoXml.getNombreEmisorFactura() + "</td></tr>"
				+ "<tr><td class='brd-right'> RFC: "+ datoXml.getRfcEmisorFactura() +"</td><td class='brd-right'> FOLIO FISCAL: " + datoXml.getFolioFiscal() + "</td><td> FOLIO FACTURA: " + datoXml.getFolioFactura() + " </td></tr>"
				+ "<tr><td colspan='3'> FECHA: " + datoXml.getFechaFactura() + "</td></tr>"
				+ "</table></body></html>";
		setArchivoActual(getRutaCarpetaXml() + nombreArchivoXml);
		hacerEnabledBtnProcesar(true);
		setDatosFactura(labelDatosFactura);
		actualizarTablaArticulos(datoXml.getArticulos());
	}
	
	public void eventoJListArchivoXml(String nombreArchivoXml){
		ManejadorXml mnXml = new ManejadorXml();
		datoXml = null;
		datoXml = new Dato();
		Document datosXml = mnXml.leerXML(new File(getRutaCarpetaXml() + nombreArchivoXml));
		datoXml.setNombreEmisorFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Emisor", "nombre"));
		datoXml.setRfcEmisorFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Emisor", "rfc"));
		datoXml.setFolioFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Comprobante", "folio"));
		datoXml.setFechaFactura(mnXml.obtenerValorAtributo(datosXml, "cfdi:Comprobante", "fecha"));
		datoXml.setFolioFiscal(mnXml.obtenerValorAtributo(datosXml, "tfd:TimbreFiscalDigital", "UUID"));
		datoXml.setArticulos(mnXml.colectarDatosXml(datosXml, 7));
		actualizarContenidoGUI(nombreArchivoXml);
	}
	
	private void acercaDe(){
		//String cadena = "Javier Burón Gutiérrez\nLizeth Vásquez Rojas";
		String cadena = "Desarrollado por:   Estancias profesionales 2015 \n        Lic. Javier Burón Gutiérrez\n        Lic. Lizeth Vásquez Rojas \n\n Dirigido por:    PCC\n       Ing. Ricardo Martinez Velazquez\n\n Versión 1.0";
		JOptionPane.showMessageDialog(null, cadena, "STOCKTAKING HELP", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(getBtnElegirCarpeta()))
			eventoBotonElegirCarpeta();
		else if(arg0.getSource().equals(getBtnProcesar()))
			eventoBotonProcesar();
		else if(arg0.getSource().equals(getSalirMenuItem()))
			System.exit(0);
		else if(arg0.getSource().equals(getGuiaRapidaMenuItem()))
			System.out.println("Guia Rápida");
		else if(arg0.getSource().equals(getAboutMenuItem()))
			acercaDe();
	}

	public void valueChanged(ListSelectionEvent e) {
		if((e.getSource() == getListaArchivosXml()) && !e.getValueIsAdjusting()){
			eventoJListArchivoXml(getListaArchivosXml().getSelectedValue().toString());
		}
	}
	
}
