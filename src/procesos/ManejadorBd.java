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
	 */
	public void actualizarBd(Dato datoXml) {
		this.datoXml = datoXml;
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
		String sqlExistenciaAlmacen = "";
		String sqlProds = "";
		String articulo = "N'" + rowArticulo[0].toString() + "'";
		String descripcion = "N'" + rowArticulo[1].toString() + "'";
		String unidad = "N'" + rowArticulo[2].toString() + "'";
		String impuesto = "N'" + rowArticulo[3].toString() + "'"; //IVA
		float cantidad = Float.parseFloat(rowArticulo[4].toString());
		float precio = Float.parseFloat(rowArticulo[5].toString()); //PRECIO1
		float importe = Float.parseFloat(rowArticulo[6].toString()); //COSTO_U
		int almacen = 9; //ALMACEN en tabla existenciaalmacen
		boolean existenciaAlmacen = investigarArticulo(rowArticulo[0].toString(), "existenciaalmacen"); //rowArticulo[0].toString() --> articulo
		boolean prods = investigarArticulo(rowArticulo[0].toString(), "prods"); //rowArticulo[0].toString() --> articulo
		if(existenciaAlmacen && prods){
			sqlExistenciaAlmacen = "UPDATE existenciaalmacen SET existencia=(SELECT existencia FROM existenciaalmacen WHERE (almacen= " + almacen + ") AND (articulo=" + articulo + ")) + 1 WHERE (almacen=" + almacen + ") AND (articulo=" + articulo + ")";
		} else if(existenciaAlmacen && !prods){
			sqlExistenciaAlmacen = "UPDATE existenciaalmacen SET existencia=(SELECT existencia FROM existenciaalmacen WHERE (almacen= " + almacen + ") AND (articulo=" + articulo + ")) + 1 WHERE (almacen=" + almacen + ") AND (articulo=" + articulo + ")";
			sqlProds = "INSERT INTO prods(ARTICULO, DESCRIP, PRECIO1, COSTO_U, UNIDAD, IMPUESTO) VALUES(" 
					+ articulo + ", " + descripcion + ", " + precio + ", " + importe + ", " + unidad + ", " + impuesto + ")";
		} else if(!existenciaAlmacen && prods){
			sqlExistenciaAlmacen = "INSERT INTO existenciaalmacen VALUES(" + almacen + ", " + articulo + ", " + cantidad + ")";
		} else {
			sqlExistenciaAlmacen = "INSERT INTO existenciaalmacen VALUES(" + almacen + ", " + articulo + ", " + cantidad + ")";
			sqlProds = "INSERT INTO prods(ARTICULO, DESCRIP, PRECIO1, COSTO_U, UNIDAD, IMPUESTO) VALUES(" 
					+ articulo + ", " + descripcion + ", " + precio + ", " + importe + ", " + unidad + ", " + impuesto + ")";
		}
		consultarBD(sqlExistenciaAlmacen);
		consultarBD(sqlProds);
		
		actualizarExistenciaProd(articulo); //buscar una mejor posición para esta linea de código
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
