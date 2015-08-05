package procesos;

/**
 * Contiene solo los atributos más representativos a utilizar del xml, cada atributo cuenta con su respectivo método set & get.
 * @author Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth Vásquez Rojas (liz_02277@hotmail.com)
 */
public class Dato {
	private String nombreEmisorFactura;
	private String rfcEmisorFactura;
	private String folioFactura;
	private Object[][] articulos;
	private String fechaFactura;
	private String folioFiscal;
	private String impuesto;
	
	//Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
	//Lizeth Vásquez Rojas (liz_02277@hotmail.com)
	
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