package procesos;
import java.sql.*;

/**
 * Encargada de llevar la comunicación con la base de datos.
 * @author Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth Vásquez Rojas (liz_02277@hotmail.com)
 */

public class ManejadorBd {
	private Dato datoXml;
	private Connection con;
	private String[][] relacionAtributosXml;
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	/**
	 * Crea una conexión a la base de datos
	 * @return 
	 */
	public boolean conectarBd(String connectionUrl){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);
			return true;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Cierra la conexión a la base de datos
	 */
	public void cerrarConexion(){
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Este es el método principal que llamarán para guardar los datos XML dentro de la BD
	 * @param datoXml contenido xml, este contenido es el que se guardará a la BD
	 * @param relacionAtributosXml Contiene un arreglo de la relacion entre los campos de la tabla, bd y xml
	 */
	public void actualizarBd(Dato datoXml, String[][] relacionAtributosXml) {
		this.datoXml = datoXml;
		this.relacionAtributosXml= relacionAtributosXml;
		int ultimaColumnaAtributos = datoXml.getArticulos()[0].length - 1;
		for(int i=0; i<datoXml.getArticulos().length; i++)
			if(Boolean.parseBoolean(datoXml.getArticulos()[i][ultimaColumnaAtributos].toString()))
				procesarArticulo(datoXml.getArticulos()[i]);
	}
	
	/**
	 * Realiza una acción en la BD.
	 * @param sql La cadena con la consulta a realizar en la base de datos.
	 * @return Respuesta de la BD, este resultado es retornado de tipo ResultSet.
	 */
	private ResultSet consultarBD(String sql){
		try {
			return con.createStatement().executeQuery(sql);
			/*while (rs.next()){  
               System.out.println(rs.getString(2));  
            }*/
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}  
		return null; 
	}
	
	/**
	 * Procesa un articulo para ser guardado en la BD
	 * @param rowArticulo Contenido del articulo
	 */
	private void procesarArticulo(Object[] rowArticulo){
		int numeroColumnas= relacionAtributosXml.length; //total de columnas de la tabla
		String articulo= "", existencia= "", almacen= ""; //campos que se guardarán en la tabla, si es que ya existiera el registro del artículo
		String tblProdsColumnName="";
		String tblProdsColumnVal= "";
		String tblexistenciaAlmacenColumnName="";
		String tblExistenciaAlmacenColumnVal= "";
		String[][] datoPreparado= new String[numeroColumnas][2];
		
		for(int i=0; i<numeroColumnas; i++){ // buscar articulo
			datoPreparado[i]= prepararDatoEntradaBd(rowArticulo[i].toString(), relacionAtributosXml[i][0]);
			if(datoPreparado[i][0].compareToIgnoreCase("articulo") == 0){
				articulo= datoPreparado[i][1];
			}else if(datoPreparado[i][0].compareToIgnoreCase("existencia") == 0){
				existencia= datoPreparado[i][1];
			}else if(datoPreparado[i][0].compareToIgnoreCase("almacen") == 0){
				almacen= datoPreparado[i][1];
			}
		}
		
		if(investigarArticulo(articulo, "existenciaalmacen")){ //si el articulo existe en la bd, sumar la nueva cantidad(existencia), tabla de la bd existenciaalmacen
			System.out.println(articulo + "[Ya ][Existe ]");
			consultarBD("UPDATE existenciaalmacen "
					+ "SET existencia=(SELECT existencia "
										+ "FROM existenciaalmacen "
										+ "WHERE (almacen= " + almacen + ") AND (articulo=" + articulo + ")) + " + existencia
					+ "WHERE (almacen=" + almacen + ") AND (articulo=" + articulo + ")");
		}else{ //si el articulo no existe, crear el nuevo registro en las tablas prods, existenciaalmacen
			for(int i=0; i<numeroColumnas; i++){ //for y switch son para obtener la relacion nombre columna y valor a guardar en la bd, tablas existenciaalmacen, prods
				switch(datoPreparado[i][0].toLowerCase()){
					case "existencia":
					case "almacen":
						if(!tblexistenciaAlmacenColumnName.isEmpty()){
							tblexistenciaAlmacenColumnName+= ", ";
							tblExistenciaAlmacenColumnVal+= ", ";
						}
						tblexistenciaAlmacenColumnName+= datoPreparado[i][0]; //columna en la tabla a guardar el valor
						tblExistenciaAlmacenColumnVal+= datoPreparado[i][1]; //valor a guardar en tabla
					break;
					case "articulo":
						if(!tblexistenciaAlmacenColumnName.isEmpty()){
							tblexistenciaAlmacenColumnName+= ", ";
							tblExistenciaAlmacenColumnVal+= ", ";
						}
						tblexistenciaAlmacenColumnName+= datoPreparado[i][0]; //columna en la tabla a guardar el valor
						tblExistenciaAlmacenColumnVal+= datoPreparado[i][1]; //valor a guardar en tabla
					default :
						if(!tblProdsColumnName.isEmpty()){
							tblProdsColumnName+= ", ";
							tblProdsColumnVal+= ", ";
						}
						tblProdsColumnName+= datoPreparado[i][0]; //columna en la tabla a guardar el valor
						tblProdsColumnVal+= datoPreparado[i][1]; //valor a guardar en tabla
				} //Termina switch
			} //Termina for
			
			consultarBD("insert into prods(LINEA, MARCA, FABRICANTE, "+ tblProdsColumnName+ ") values(N'SYS', N'SYS', N'SYS', "+ tblProdsColumnVal+ ")");
			consultarBD("insert into existenciaalmacen("+ tblexistenciaAlmacenColumnName+ ") values("+ tblExistenciaAlmacenColumnVal+ ")");
		} //termina else de investigar si el articulo existe en la bd
		
		actualizarExistenciaProd(articulo); //buscar una mejor posición para esta linea de código
	}
	
	/** Prepara dato con N'cadena' para almacenarlo en bd
	 * @param cellRowArticulo articulo que será preparado con N'cadena', si fuera necesario
	 * @param valRelacionXml tipo y nombre de la tabla bd 
	 * @return arreglo con 2 datos. posicion 0=tabla bd y posicion 1=valor a almacenar en bd
	 */
	private String[] prepararDatoEntradaBd(String cellRowArticulo, String valRelacionXml){
		String[] datoPreparado= new String[2];
		String[] datosValRelacionXml= valRelacionXml.split(":");
		datoPreparado[0]= datosValRelacionXml[0];
		if(datosValRelacionXml[1].trim().compareToIgnoreCase("cadena") == 0)
			datoPreparado[1]= "N'"+ cellRowArticulo+ "'";
		else
			datoPreparado[1]= cellRowArticulo;
		return datoPreparado;
	}
	
	/**
	 * Investiga si existe o no el articulo dentro de la tabla.
	 * @param articulo Cadena de identificación del articulo.
	 * @param tabla Nombre de la tabla en la bd, la que será alterada.
	 * @return True si el articulo existe en la tabla, False si el articulo no existe en la tabla.
	 */
	private boolean investigarArticulo(String articulo, String tabla){
		try {
			ResultSet rs = consultarBD("SELECT count(articulo) FROM " + tabla + " WHERE articulo=N'" + articulo + "'"); //pienso falta en wehere un and almacen
			while (rs.next()){  
				if(Integer.parseInt(rs.getString(1)) > 0)
					return true;
					//actualizarDatos(1, rowArticulo); //PIENSO DEBERIA HABER VALIDACION TAMBIEN PARA SI EXISTE EN PRODS Y NO EN
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Actualiza los articulos en las tablas correspondientes de la BD.
	 * @param almacen Número de almacen donde actualizar los articulos optenidos.
	 * @param rowArticulo Lista de articulos a actualizar.
	 */
	private void actualizarDatos(int almacen, Object[] rowArticulo) {
		String articulo = "N'" + rowArticulo[0].toString() + "'";
		String descripcion = rowArticulo[1].toString();
		String unidad = rowArticulo[2].toString();
		float cantidad = Float.parseFloat(rowArticulo[3].toString()); //VER no me convence creo debe ser integer
		float precio = Float.parseFloat(rowArticulo[4].toString()); //PRECIO1
		float importe = Float.parseFloat(rowArticulo[5].toString()); //COSTO_U
		
		//Tabla prods
		String sqlProds = "UPDATE prods SET DESCRIP= " + descripcion + ", PRECIO1=" + precio + ", COSTO_U=" + importe + ", UNIDAD=" + unidad + "WHERE ARTICULO=" + articulo;
		consultarBD(sqlProds);
		
		//Tabla existenciaalmacen
		String sqlExistenciaAlmacen = "UPDATE existenciaalmacen SET existencia = (SELECT existencia FROM existenciaalmacen WHERE (ALMACEN=" + almacen + ") AND (articulo=N'" + articulo + "')) + " + cantidad
				+ "WHERE (almacen=1) AND (articulo=" + articulo + ")";
		consultarBD(sqlExistenciaAlmacen);
	}
	
	/**
	 * Actualiza el campo existencia de un articulo en la tabla "prods"
	 * @param articulo Nombre del articulo que será actualizada su existencia en la tabla "prods"
	 */
	private void actualizarExistenciaProd(String articulo){
		String sql = "UPDATE prods SET prods.EXISTENCIA= (SELECT SUM(existenciaalmacen.existencia) FROM existenciaalmacen WHERE articulo= " + articulo + ") WHERE prods.ARTICULO= " + articulo;
		consultarBD(sql);
	}
}
