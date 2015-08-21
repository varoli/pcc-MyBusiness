import java.awt.*;
import javax.swing.*;
import procesos.*;

/**
 * Clase principal, para poder ejecutar el programa es necesaria ésta clase
 * @author Javier Burón Gutiérrez (javier_buron_gtz@outlook.com)
 * 		<br> Lizeth Vásquez Rojas (liz_02277@hotmail.com)
 */
public class Ejecutar {

	public static void main(String[] args) {
		JDialog dialogoEspera = new JDialog();
		JPanel panelImaneg = new JPanel();
		panelImaneg.setBackground(new Color(0, 0, 0, 0));
		JPanel panelTexto = new JPanel();
		panelImaneg.add(new JLabel(new ImageIcon(dialogoEspera.getClass().getResource("/img/loading.gif")), JLabel.CENTER));
		panelTexto.add(new JLabel("Cargando programa ... Porfavor espere ...", JLabel.CENTER));
		dialogoEspera.add(BorderLayout.CENTER, panelImaneg);
		dialogoEspera.add(BorderLayout.SOUTH, panelTexto);
		dialogoEspera.setSize(300, 100);
		dialogoEspera.setLocationRelativeTo(null);
		dialogoEspera.setUndecorated(true);
		dialogoEspera.getRootPane().setOpaque(false);
		dialogoEspera.getContentPane().setBackground(new Color(0, 0, 0, 0));
		dialogoEspera.setBackground(new Color(0, 0, 0, 0));
		dialogoEspera.setVisible(true);
		ManejadorBd manejadorBD = new ManejadorBd();
		HerramientaArchivo herramientaArchivo = new HerramientaArchivo();
		if(manejadorBD.conectarBd(herramientaArchivo.leerArchivoTexto("cBD.data"))){
			dialogoEspera.dispose();
			dialogoEspera = null;
			new ControlProyecto(manejadorBD, herramientaArchivo);
		}else{
			JOptionPane.showMessageDialog(null, "No fue posible entrar al sistema, contacte al técnico", "Problema", JOptionPane.WARNING_MESSAGE);
			System.exit(-1);
		}
	}

}
