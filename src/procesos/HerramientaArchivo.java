package procesos;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Contiene métodos necesarios para el manejo de archivos en el sistema operativo
 * @author Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth Vásquez Rojas (liz_02277@hotmail.com)
 */
public class HerramientaArchivo {
	
	/** Crea una carpeta en el directortio y con el nombre recibido en el argumento del método
	 * @param nuevaCarpeta Ruta y nombre de la carpeta a crear. Ejemplo C:\\carpeta
	 */
	public void crearCarpeta(String nuevaCarpeta){
		File procesados = new File(nuevaCarpeta);
		if(!procesados.exists() && !procesados.mkdir()){
			System.out.println("No fue posible crear el directorio " + nuevaCarpeta);
		}
	}
	
	/** Hacer invisible la carpeta especificada en el argumento 
	 * @param rutaCarpeta Ruta de la carpeta a hacer invisible. Ejemplo C:\\carpeta
	 */
	public void hacerInvisibleCarpeta(String rutaCarpeta){
		try{
			Runtime.getRuntime().exec("attrib +H \"" + rutaCarpeta + "\"");
		}catch(Exception e){
			System.out.println("No es posible hacer invisible a: " + rutaCarpeta);
		}
	}
	
	/** Mueve un archivo de la posición acutal a un nuevo destino
	 * @param origen Ruta actual del archivo a mover
	 * @param destino Nueva ruta del archivo a mover
	 * @return True si se pudo mover el archivo, false en caso contrario
	 */
	public boolean moverArchivo(String origen, String destino){
		try{      
	         File archivoOrigen = new File(origen);
	         File archivoDestino = new File(destino);
	         if(archivoOrigen.renameTo(archivoDestino))
	        	 return true;
	         else
	        	 return false;
	      }catch(Exception e){
	         System.out.println(e.getMessage());
	      }
		return false;
	}
	
	/** Obtiene una lista de nombre de archivos con la extencion XML.
	 * @param rutaCarpeta Ruta de la carpeta que buscará todos los archiovos xml
	 * @return Lista de archivos XML encontrados
	 */
	public String[] obtenerNombresArchivosXml(String rutaCarpeta){
		File f = new File(rutaCarpeta);
		if(f.exists()){ //Comprobando que la carpeta especificada, exista o sea accecible
			File[] archivos = f.listFiles();
			if(archivos.length > 0){
				ArrayList<String> nombresXML = new ArrayList<String>();
				for(int i=0; i<archivos.length; i++){
					String fileXML = archivos[i].getName();
					if(validarExtencionXML(fileXML)){
						nombresXML.add(fileXML); //Almacenando los nombres de los xml
					}
				}
				String[] archivosXml = new String[nombresXML.size()];
				nombresXML.toArray(archivosXml);
				return archivosXml;
			}
		}
		System.out.println("La carpeta espesificada no existe");
		return null;
	}
	
	/**
	 * Valida que la cadena recibida sea el nombre de un archivo xml.
	 * @param archivo nombre y extención del archivo encontrado en la carpeta.
	 * @return "Verdadero" encaso de que el archivo sea un xml,
	 *         "Falso" en el caso contrario.
	 */
	private boolean validarExtencionXML(String archivo) {
		try{
			String extencion = "";
			extencion = archivo.substring(archivo.lastIndexOf("."));
			if(extencion.compareToIgnoreCase(".xml") == 0){
				return true;
			}
		}catch(Exception e){}
		return false;
	}

	/** Leer el contenido de un archivo de texto.
	 * @param archivo Ruta del archivo a leer. ejemplo c:\\archivo.data
	 * @return Contenido del archivo leido
	 */
	public String leerArchivoTexto(String archivo){
		String contenido = "";
		String linea="";
		try {
			BufferedReader b = new BufferedReader(new FileReader(archivo));
			
			while((linea=b.readLine()) != null){
				contenido+= linea;
			}
	        b.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        return contenido;
	}
	
	/** Excribir algo en un archivo de texto
	 * @param archivo Ruta del archivo a leer. ejemplo c:\\archivo.data
	 * @param contenido Texto a guardar en el archivo
	 */
	public void escribirArchivoTexto(String archivo, String contenido){
		File file = new File(archivo);
		try{
			BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(file));
			PrintWriter wr = new PrintWriter(bufferWrite);  
			wr.write(contenido);
			wr.close();
			bufferWrite.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	/** JFilechooser, cuadro de dialogo para elegír la carpeta que contenga los archivos xml
	 * @param ruta Dirección para situar el dialogo en una ruta para buscar. Ejemplo c:\\carpeta
	 * @return Ruta elegida, en ésta ruta se buscarán los archivos XML
	 */
	public String elegirRutaCarpetaXml(String ruta){
		JFileChooser fc = new JFileChooser(ruta);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		if(fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile().getPath()+ "\\";
		}
		return null;
	}
}
