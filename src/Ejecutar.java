import java.awt.*;
import javax.swing.*;
import procesos.*;

/**
 * Clase principal, para poder ejecutar el programa es necesaria �sta clase
 * @author Javier Bur�n Guti�rrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth V�squez Rojas (liz_02277@hotmail.com)
 */
public class Ejecutar {

	public static void main(String[] args) {
		JDialog dialogoEspera = new JDialog();
		JPanel panelImagen = new JPanel();
		panelImagen.setBackground(new Color(0, 0, 0, 0));
		JPanel panelTexto = new JPanel();
		panelImagen.add(new JLabel(new ImageIcon(dialogoEspera.getClass().getResource("/img/loading.gif")), JLabel.CENTER));
		panelTexto.add(new JLabel("Cargando programa ... Porfavor espere ...", JLabel.CENTER));
		dialogoEspera.add(BorderLayout.NORTH, new JLabel(new ImageIcon(dialogoEspera.getClass().getResource("/img/logo-pcc.jpg"))));
		dialogoEspera.add(BorderLayout.CENTER, panelImagen);
		dialogoEspera.add(BorderLayout.SOUTH, panelTexto);
		dialogoEspera.setSize(300, 200);
		dialogoEspera.setLocationRelativeTo(null);
		dialogoEspera.setUndecorated(true);
		dialogoEspera.getRootPane().setOpaque(false);
		dialogoEspera.getContentPane().setBackground(new Color(255, 255, 255));
		dialogoEspera.setBackground(new Color(255, 255, 255));
		dialogoEspera.setVisible(true);
		ManejadorBd manejadorBD = new ManejadorBd();
		HerramientaArchivo herramientaArchivo = new HerramientaArchivo();
		if(manejadorBD.conectarBd(herramientaArchivo.leerArchivoTexto("cBD.data"))){
			String fileRelacionAttrxml= herramientaArchivo.leerArchivoTexto("relacionAtributos.data").trim();
			String[] rowRelacionAttrXml= fileRelacionAttrxml.split(";");
			String[][] relacionAtributosXml= new String[rowRelacionAttrXml.length][];
			for(int i=0; i<rowRelacionAttrXml.length; i++){
				relacionAtributosXml[i]= rowRelacionAttrXml[i].split("=");
			}
			dialogoEspera.dispose();
			dialogoEspera = null;
			new ControlProyecto(manejadorBD, herramientaArchivo, relacionAtributosXml);
		}else{
			JOptionPane.showMessageDialog(null, "No fue posible entrar al sistema, contacte al t�cnico", "Problema", JOptionPane.WARNING_MESSAGE);
			System.exit(-1);
		}
	}

}
