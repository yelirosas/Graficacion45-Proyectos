package modelos3d;

import java.awt.Color;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Luces {

  public static Light luzDireccional(Color c, Vector3f v) {
    DirectionalLight l = new DirectionalLight(new Color3f(c), v);
    l.setInfluencingBounds(new BoundingSphere());
    return l;
  }

  public static Light luzAmbiental(Color c) {
    AmbientLight l = new AmbientLight(new Color3f(c));
    l.setInfluencingBounds(new BoundingSphere());
    return l;
  }

  public static Light luzPuntual(Color c, Point3f p, Point3f at) {
    PointLight l = new PointLight(new Color3f(c), p, at);
    l.setInfluencingBounds(new BoundingSphere());
    return l;
  }

  public static Light luzSpot(Color c, Point3f p, Point3f at,
    Vector3f dir, float ang, float con) {
    SpotLight l = new SpotLight(new Color3f(c), p, at, dir, ang, con);
    l.setInfluencingBounds(new BoundingSphere());
    return l;
  }
}
