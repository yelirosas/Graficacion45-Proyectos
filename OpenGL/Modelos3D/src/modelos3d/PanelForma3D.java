package modelos3d;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;

public class PanelForma3D extends Panel3D{

  private final BranchGroup forma;
  
  public PanelForma3D(BranchGroup forma) {
    this.forma = forma;
    this.addUniverso();
    this.setOrbitBehavior(true);
  }
  
  @Override
  public Node addNodoPrincipal() {
    BranchGroup bg = new BranchGroup();
    bg.addChild(forma);
    return bg;
  }
  
}
