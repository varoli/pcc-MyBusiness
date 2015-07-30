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
	
	/** Obtiene el valor del atributo especificado, este atributo pertenece a una etiqueta especificada. <b>Se recomienda utilizar
	 * este código solo para obtener los datos de la factura</b> <i>PARA OBTENER LOS ARTICULOS DE LA FACTURA, UTILIZAR EL MÉTODO
	 * "colectarDatosXml(...)</i>".
	 * @param documento El documento con toda la información del archivo xml
	 * @param etiqueta La etiqueta a usar para obtener el atributo deseado
	 * @param atributo El atributo que se desea obtener, de la etiqueta especificada
	 * @return El contenido del atributo que pertenece a la etiqueta especificada en los correspondientes parametros del método
	 */
	public String obtenerValorAtributo(Document documento, String etiqueta, String atributo){
		NodeList tag = documento.getElementsByTagName(etiqueta);
		NamedNodeMap atributos = tag.item(0).getAttributes();
		return atributos.getNamedItem(atributo).getTextContent();
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
