package procesos;

/**
 * Contiene solo los atributos m�s representativos a utilizar del xml, cada atributo cuenta con su respectivo m�todo set & get.
 * @author Javier Bur�n Guti�rrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth V�squez Rojas (liz_02277@hotmail.com)
 */
public class Dato {
	private String nombreEmisorFactura;
	private String rfcEmisorFactura;
	private String folioFactura;
	private Object[][] articulos;
	private String fechaFactura;
	private String folioFiscal;
	private String impuesto;
	
	//Javier Bur�n Guti�rrez (javier_buron_gtz@outlook.com)
	//Lizeth V�squez Rojas (liz_02277@hotmail.com)
	
	public String getNombreEmisorFactura() {
		return nombreEmisorFactura;
	}
	public void setNombreEmisorFactura(String nombreEmisorFactura) {
		this.nombreEmisorFactura = nombreEmisorFactura;
	}
	public String getRfcEmisorFactura() {
		return rfcEmisorFactura;
	}
	public void setRfcEmisorFactura(String rfcEmisorFactura) {
		this.rfcEmisorFactura = rfcEmisorFactura;
	}
	public String getFolioFactura() {
		return folioFactura;
	}
	public void setFolioFactura(String folioFactura) {
		this.folioFactura = folioFactura;
	}
	public Object[][] getArticulos() {
		return articulos;
	}
	public void setArticulos(Object[][] objects) {
		this.articulos = objects;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getFolioFiscal() {
		return folioFiscal;
	}
	public void setFolioFiscal(String folioFiscal) {
		this.folioFiscal = folioFiscal;
	}
	public String getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}
}