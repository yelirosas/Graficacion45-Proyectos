package modelos3d;

import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;

public abstract class Panel3D extends JPanel {

  private SimpleUniverse universo;
  private BranchGroup ramaContenido;

  public Panel3D() {
    setLayout(new BorderLayout());
    addUniverso();
  }

  public final void addUniverso() {
    // Obtenemos la configuracion predeteminada de la interfaz
    GraphicsConfigTemplate3D gct3D = new GraphicsConfigTemplate3D();
    gct3D.setSceneAntialiasing(GraphicsConfigTemplate3D.REQUIRED);
    GraphicsConfiguration gc = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gct3D);

    Canvas3D cv = new Canvas3D(gc);
    add(cv, "Center");
    // SimpleUniverse define los elementos basicos de una escena 3D 
    // y lo asocia al plano 2D
    universo = new SimpleUniverse(cv);
    // Define la ubicación predeterminada de la vista
    // Mueve el centro (0,0,0) a una distancia de 2.
    universo.getViewingPlatform().setNominalViewingTransform();
    creaRamaContenido();
  }

  private void creaRamaContenido() {
    //try {
      ramaContenido = new BranchGroup();
      ramaContenido.addChild(addNodoPrincipal());
      //URL imagen = getClass().getResource("/imagenes/paisaje.jpg");
      //File archivo = new File(imagen.toURI());
      Fondo3D fondo = new Fondo3D(Color.BLACK);
      ramaContenido.addChild(fondo); // Asociar el fondo al universo
      universo.addBranchGraph(ramaContenido);
    //} catch (URISyntaxException ex) {
      //System.out.println("Error de archivo " + ex);
      //System.exit(-1);
    //}

  }

  public abstract Node addNodoPrincipal();

  /**
   * @param orbitBehavior the orbitBehavior to set
   */
  public void setOrbitBehavior(boolean orbitBehavior) {
    // Se obtiene el plano 2D y la vista 3D
    Canvas3D cv = universo.getCanvas();
    ViewingPlatform vp = universo.getViewingPlatform();
    if (orbitBehavior) {
      // Se ajusta el objeto OrbitBehavior
      OrbitBehavior orbita
        = new OrbitBehavior(cv, OrbitBehavior.REVERSE_ALL);
      orbita.setSchedulingBounds(new BoundingSphere());
      vp.setViewPlatformBehavior(orbita);
    } else {
      vp.setViewPlatformBehavior(null);
    }
  }
}

//  public final void addUniverso() {
//    // Obtenemos la configuracion predeteminada de la interfaz
//    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
//    Canvas3D cv = new Canvas3D(gc);
//    add(cv, "Center");
//    // SimpleUniverse define los elementos basicos de una escena 3D 
//    // y lo asocia al plano 2D
//    universo = new SimpleUniverse(cv);
//    // Define la ubicación predeterminada de la vista
//    // Mueve el centro (0,0,0) a una distancia de 2.
//    universo.getViewingPlatform().setNominalViewingTransform();
//    creaRamaContenido();
//  }