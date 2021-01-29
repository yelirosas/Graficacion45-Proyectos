package modelos3d.texturas;

import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class Planeta3D extends TransformGroup {
  
  private final float semiEjeMayor;
  private final float semiEjeMenor;
  private final String textura;
  private final float radio;
  private final long tiempo;
  
  public Planeta3D(String textura, float radio, float semiEjeMayor, 
    float semiEjeMenor, long tiempo) {
    this.semiEjeMayor = semiEjeMayor;
    this.semiEjeMenor = semiEjeMenor;
    this.textura = textura;
    this.radio = radio;
    this.tiempo = tiempo;
    addPlaneta();
  }

  private void addPlaneta() {
    this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    EsferaConTextura planeta = new EsferaConTextura(getRadio(), textura);
    Material mat = new Material();
    mat.setShininess(3f);
    planeta.getAppearance().setMaterial(mat);
    Transform3D t3d = new Transform3D();
    t3d.setTranslation(new Vector3d(getSemiEjeMayor() / 2, 0, 0));
    setTransform(t3d);
    addChild(planeta);
  }

  public float getSemiEjeMayor() {
    return semiEjeMayor;
  }

  public float getSemiEjeMenor() {
    return semiEjeMenor;
  }

  public float getRadio() {
    return radio;
  }

  public long getTiempo() {
    return tiempo;
  }
  
}
