package modelos3d.texturas;

import com.sun.j3d.utils.geometry.Sphere;
import modelos3d.Apariencia;

public class EsferaConTextura extends Sphere {

  public EsferaConTextura(float radio, String archivo) {
    super(radio, GENERATE_TEXTURE_COORDS | GENERATE_NORMALS, 150);
    cargarTextura(archivo);
  }

  public final void cargarTextura(String archivo) {
    Apariencia ap = new Apariencia();
    ap.asignarTextura(archivo);
    setAppearance(ap);
  }
}
