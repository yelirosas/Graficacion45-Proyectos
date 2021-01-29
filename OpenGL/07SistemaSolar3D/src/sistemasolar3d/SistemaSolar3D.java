package sistemasolar3d;

import javax.swing.JFrame;

public class SistemaSolar3D {

  public static void main(String[] args) {
    SistemaSolar sistema = new SistemaSolar();
    PanelSistemaSolar panel = new PanelSistemaSolar(sistema);
    OyenteSistemaSolar oyente = new OyenteSistemaSolar(sistema, panel);
    panel.addEventos(oyente);
    
    JFrame f = new JFrame("Sistema Solar con OpenGL");
    f.setSize(800, 600);
    f.setLocationRelativeTo(f);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(panel);
    f.setVisible(true);
  }

}
