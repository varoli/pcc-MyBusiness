package procesos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManejadorBd {
	private Dato datoXml;
	private Connection con;
	/*String connectionUrl = "jdbc:sqlserver://WINDOWS-PC\\SQLEXPRESS:1394;" +
			"databaseName=C:\\MyBusinessDatabase\\MyBusinessPOS2012.mdf;integratedSecurity=true;";*/
	private String dbURL = "jdbc:sqlserver://localhost;databaseName=prueba";
	private String user = "sa";
	private String pass = "d12345678";
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public static void main(String[] args){
		//new ManejadorBd();
		
		System.out.println("Antes de conexión");
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			/*String connectionUrl = "jdbc:sqlserver://127.0.0.1\\SQLEXPRESS;" +
			"databaseName=prueba;user=;password=;integratedSecurity=true";*/
			String connectionUrl = "jdbc:sqlserver://127.0.0.1\\SQLEXPRESS;" +
					"databaseName=prueba;integratedSecurity=true";
			Connection con = DriverManager.getConnection(connectionUrl);
			if(con != null){
				System.out.println("exito");
			}
		} catch (Exception e) {
			System.out.println("error: "+e.toString());
		}
		System.out.println("fin");
	}
	
	public ManejadorBd() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbURL, user, pass);
			//con = DriverManager.getConnection(connectionUrl);
			System.out.println("conexion exitosa");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
	public void actualizarBd(Dato datoXml) {
		this.datoXml = datoXml;
	}
	
	private boolean actualizarTablaProds(){
		return false;
	}
	private boolean actualizarTablaExistenciasAlmacen(){
		return false;
	}
}
