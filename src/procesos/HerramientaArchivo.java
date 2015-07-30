package procesos;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class HerramientaArchivo {
	public void crearCarpeta(String nuevaCarpeta){
		File procesados = new File(nuevaCarpeta);
		if(!procesados.exists() && !procesados.mkdir()){
			System.out.println("No fue posible crear el directorio " + nuevaCarpeta);
		}
		hacerInvisibleCarpeta(nuevaCarpeta);
	}
	
	public void hacerInvisibleCarpeta(String rutaCarpeta){
		try{
			String carpeta = "c:\\xmls - copia\\procesados";
			//Runtime.getRuntime().exec("attrib +h " + carpeta);
			Runtime.getRuntime().exec("cmd.exe attrib +H " + carpeta+"pr");
			System.out.println("Echo: " + rutaCarpeta);
		}catch(Exception e){
			System.out.println("No es posible hacer invisible a: " + rutaCarpeta);
		}
	}
	
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

	public String leerArchivoTexto(String archivo){
		String contenido = "";
		try {
			BufferedReader b = new BufferedReader(new FileReader(archivo));
			contenido = b.readLine();
	        b.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        return contenido;
	}
	
	public void escribirArchivoTexto(String archivo, String contenido){
		File file = new File(archivo);
		try{
			BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(file));
			PrintWriter wr = new PrintWriter(bufferWrite);  
			wr.write(contenido);
			//wr.append(" - y aqui continua");
			wr.close();
			bufferWrite.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
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
