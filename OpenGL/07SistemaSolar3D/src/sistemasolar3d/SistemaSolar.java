package sistemasolar3d;

import java.awt.Color;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3f;
import modelos3d.Luces;
import modelos3d.texturas.Planeta3D;

public class SistemaSolar extends BranchGroup {

  public SistemaSolar() {
    addChild(Luces.luzPuntual(Color.WHITE, new Point3f(0, 0, 0), new Point3f(1f, 0, 0)));
    addChild(new Planeta3D("sol.jpg", 0.08f, 0, 0, 50));
    addChild(new Planeta3D("tierra.jpg", 0.02f, 0.8f, 0.7f, 50));
    addChild(new Planeta3D("marte.jpg", 0.017f, 0.91f, 1f, 60));
    addChild(new Planeta3D("jupiter.jpg", 0.06f, 1.4f, 1.2f, 100));
  }

}
