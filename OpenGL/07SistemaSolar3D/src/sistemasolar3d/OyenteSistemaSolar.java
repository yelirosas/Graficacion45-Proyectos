package sistemasolar3d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import modelos3d.texturas.Planeta3D;

public class OyenteSistemaSolar implements ActionListener {

  private final SistemaSolar sistema;
  private final PanelSistemaSolar panel;
  private final ArrayList<HiloMovimiento> hilos;
  
  public OyenteSistemaSolar(SistemaSolar sistema, PanelSistemaSolar panel) {
    this.sistema = sistema;
    this.panel = panel;
    Enumeration en = sistema.getAllChildren();
    hilos = new ArrayList();
    while (en.hasMoreElements()) {
      Node n = (Node) en.nextElement();
      if (n instanceof Planeta3D) {
        hilos.add(new HiloMovimiento((Planeta3D) n));
      }
    }
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!hilos.get(0).isAlive()) {
      for (Thread hilo : hilos) {
        hilo.start();
      }
    }
  }
  
  class HiloMovimiento extends Thread {
    private Planeta3D planeta;
    
    private HiloMovimiento(Planeta3D planeta) {
      this.planeta = planeta;
    }
    
    @Override
    public void run() {
      int anguloT = 0;
      int anguloR = 0;
      double rMayor = planeta.getSemiEjeMayor() / 2;
      double rMenor = planeta.getSemiEjeMenor() / 2;
      Transform3D t3d = new Transform3D();
      Matrix4f matrizT = new Matrix4f();
      Matrix4f matrizR = new Matrix4f();
      
      for (;;) {
        double radT = Math.toRadians(anguloT);
        float x = (float)(rMayor * Math.cos(radT));
        float z = (float)(rMayor * Math.sin(radT));
        t3d.setTranslation(new Vector3d(x, 0, z));
        matrizT.setIdentity();
        matrizT.setM03(x);
        matrizT.setM13(0);
        matrizT.setM23(z);
        
        double radR = Math.toRadians(anguloR);
        float seno = (float)Math.sin(radR);
        float coseno = (float) Math.cos(radR);
        
        matrizR.setIdentity();
        matrizR.setM00(coseno);
        matrizR.setM22(coseno);
        matrizR.setM02(seno);
        matrizR.setM20(-seno);
        
        matrizT.mul(matrizR);
        t3d.set(matrizT);
        planeta.setTransform(t3d);
        try {
          sleep(planeta.getTiempo());
        } catch (InterruptedException e) {
          System.out.println("Error " + e);
          System.exit(0);
        }
        anguloT += 1;
        anguloR += 10;
      }
    }
    
  }
  
}
