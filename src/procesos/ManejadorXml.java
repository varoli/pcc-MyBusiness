package procesos;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class ManejadorXml {
	
	/**
	 * Lee el contenido del archivo xml y lo almacena en un objeto tipo Document para ser retornado
	 * @param archivo La instancia del archivo a procesar
	 * @return El contenido del archivo xml, el tipo de retorno es un objeto Document
	 */
	public Document leerXML(File archivo){
		Document documento = null;
		if(!archivo.exists()){
			return null;
		}
		
		try{
			documento = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(archivo);
			documento.getDocumentElement().normalize();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return documento;
	}
	
	public Object[][] colectarDatosXml(Document documento, int numAtributos){
		NodeList concepto = documento.getElementsByTagName("cfdi:Concepto");
		Object[][] datos = new Object[concepto.getLength()][numAtributos];
		for(int i=0; i<concepto.getLength(); i++){
			NamedNodeMap comprobante = concepto.item(i).getAttributes();
			datos[i][0] = comprobante.getNamedItem("noIdentificacion").getTextContent();
			datos[i][1] = comprobante.getNamedItem("descripcion").getTextContent();
			datos[i][2] = comprobante.getNamedItem("unidad").getTextContent();
			datos[i][3] = comprobante.getNamedItem("cantidad").getTextContent();
			datos[i][4] = comprobante.getNamedItem("valorUnitario").getTextContent();
			datos[i][5] = comprobante.getNamedItem("importe").getTextContent();
			datos[i][6] = new Boolean(true);
		}
		return datos;
	}
}
