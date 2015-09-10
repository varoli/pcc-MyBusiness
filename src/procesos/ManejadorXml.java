package procesos;

import java.io.File;
import java.security.KeyStore.Entry.Attribute;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 * Contiene m�todos necesarios para el manejo del contenido del archivo XML
 * @author Javier Bur�n Guti�rrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth V�squez Rojas (liz_02277@hotmail.com)
 */
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
	 * este c�digo solo para obtener los datos de la factura</b> <i>PARA OBTENER LOS ARTICULOS DE LA FACTURA, UTILIZAR EL M�TODO
	 * "colectarDatosXml(...)</i>".
	 * @param documento El documento con toda la informaci�n del archivo xml
	 * @param etiqueta La etiqueta a usar para obtener el atributo deseado
	 * @param atributo El atributo que se desea obtener, de la etiqueta especificada
	 * @return El contenido del atributo que pertenece a la etiqueta especificada en los correspondientes parametros del m�todo
	 */
	public String obtenerValorAtributo(Document documento, String etiqueta, String atributo){
		NodeList tag = documento.getElementsByTagName(etiqueta);
		NamedNodeMap atributos = tag.item(0).getAttributes();
		return atributos.getNamedItem(atributo).getTextContent();
	}
	
	/** Recolecta los articulos que ser�n mostrados en la GUI del apartado tabla de articulos
	 * @param documento Contenido del archivo xml
	 * @param numAtributos N�mero de columnas que tiene la tabla de art�culos en la clase GUI
	 * @return Lista de productos a mostrar en la tabla de art�culos en la clase GUI
	 */
	public Object[][] colectarDatosXml(Document documento, String[][] relacionAtributosXml){
		try{
			int numAtributos= relacionAtributosXml.length;
			String attributo="";
			NodeList concepto = documento.getElementsByTagName("cfdi:Concepto");
			Object[][] datos = new Object[concepto.getLength()][numAtributos + 1];
			for(int i=0; i<concepto.getLength(); i++){
				NamedNodeMap comprobante = concepto.item(i).getAttributes();
				for(int j=0; j<numAtributos; j++){
					attributo= relacionAtributosXml[j][2];
					if(attributo.indexOf("[") != -1){
						attributo= attributo.replace("[", "");
						attributo= attributo.replace("]", "");
						datos[i][j] = attributo.trim();
					}else{
						datos[i][j] = comprobante.getNamedItem(attributo).getTextContent().trim();
					}
				}
				datos[i][numAtributos] = new Boolean(true); //valor para la columna elegir, columna en la tabla de art�culos en la clase GUI
			}
			return datos;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error del sistema, porfavor consulte al t�cnico. problema con atributos xml: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
