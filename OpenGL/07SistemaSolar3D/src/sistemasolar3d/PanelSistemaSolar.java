package sistemasolar3d;

import modelos3d.PanelForma3D;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelSistemaSolar extends JPanel {
  
  private JButton botonIniciar;
  private final SistemaSolar sistema;
  
  public PanelSistemaSolar(SistemaSolar sistema) {
    this.sistema = sistema;
    addComponentes();
  }

  public void addEventos(OyenteSistemaSolar oyente) {
    botonIniciar.addActionListener(oyente);
  }
  
  private void addComponentes() {
    JPanel panelNorte = new JPanel();
    this.setLayout(new BorderLayout());
    botonIniciar = new JButton("Iniciar Movimiento");
    panelNorte.add(botonIniciar);
    add(panelNorte, "North");
    add(new PanelForma3D(sistema), "Center");
  }
  
}
