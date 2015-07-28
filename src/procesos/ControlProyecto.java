package procesos;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import gui.GUI;

public class ControlProyecto extends GUI{
	private HerramientaArchivo herramientaArchivo;
	private Dato[] datos;
	
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
			herramientaArchivo.escribirArchivoTexto("foldSel.data", rutaCarpeta);
			herramientaArchivo.crearCarpeta(getRutaCarpetaXml() + "\\procesados\\");
		}
	}
	
	public void eventoBotonProcesar(){
		//herramientaArchivo.moverArchivo("c:\\prueba\\pruebaEsto.xml", "c:\\prueba\\procesados\\pruebaEsto.xml");
		System.out.println("boton procesar");
	}
	
	public void eventoJListArchivoXml(String nombreArchivoXml){
		ManejadorXml mnXml = new ManejadorXml();
		Document datosXml = mnXml.leerXML(new File(getRutaCarpetaXml() + nombreArchivoXml));
		actualizarTablaArticulos(mnXml.colectarDatosXml(datosXml, 7));
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(getBtnElegirCarpeta()))
			eventoBotonElegirCarpeta();
		else if(arg0.getSource().equals(getBtnProcesar()))
			eventoBotonProcesar();
	}

	public void valueChanged(ListSelectionEvent e) {
		if((e.getSource() == getListaArchivosXml()) && !e.getValueIsAdjusting()){
			eventoJListArchivoXml(getListaArchivosXml().getSelectedValue().toString());
		}
	}
	
}
