package modelos3d;

import com.sun.j3d.utils.image.TextureLoader;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import static javax.media.j3d.PolygonAttributes.*;

public class Apariencia extends Appearance {

  public void setApariencia(Appearance ap) {
    this.setPolygonAttributes(ap.getPolygonAttributes());
    this.setPointAttributes(ap.getPointAttributes());
    this.setLineAttributes(ap.getLineAttributes());
    this.setColoringAttributes(ap.getColoringAttributes());
    this.setTransparencyAttributes(ap.getTransparencyAttributes());
    this.setRenderingAttributes(ap.getRenderingAttributes());
  }
  
  private void dibujarPoligonos(int atributo) {
    PolygonAttributes pa = new PolygonAttributes();
    pa.setPolygonMode(atributo);  // CARA, LINEA O PUNTO
    setPolygonAttributes(pa);
  }

  public void dibujarCaras() {
    dibujarPoligonos(POLYGON_FILL);
  }

  public void dibujarLineas(int grosor, int tipo) {
    dibujarPoligonos(POLYGON_LINE);
    LineAttributes la = new LineAttributes();
    la.setLineWidth(grosor);
    la.setLinePattern(tipo);
    setLineAttributes(la);
  }

  public void dibujarPuntos(int tamaño) {
    dibujarPoligonos(POLYGON_POINT);
    PointAttributes pa = new PointAttributes();
    pa.setPointSize(tamaño);
    setPointAttributes(pa);
  }

  public void dibujarLineas() {
    dibujarLineas(2, LineAttributes.PATTERN_SOLID);
  }

  public void dibujarPuntos() {
    dibujarPuntos(4);
  }

  // COLORES
  public void ignorarColorVertices(boolean bandera) {
    RenderingAttributes ra = new RenderingAttributes();
    ra.setIgnoreVertexColors(bandera);
    setRenderingAttributes(ra);
  }

  public void dibujarGouraud() {
    ColoringAttributes ca = new ColoringAttributes();
    ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
    setColoringAttributes(ca);
    ignorarColorVertices(false);
 }

  public void dibujarPlano() {
    ColoringAttributes ca = new ColoringAttributes();
    ca.setShadeModel(ColoringAttributes.SHADE_FLAT);
    setColoringAttributes(ca);
    ignorarColorVertices(false);
  }

  public void dibujarSimple(Color color) {
    ColoringAttributes ca = new ColoringAttributes();
    ca.setColor(new Color3f(color));
    setColoringAttributes(ca);
    ignorarColorVertices(true);
  }
  
  // TRANSPARENCIAS
  public void definirTransparencia(float valor) {
    TransparencyAttributes ta = new TransparencyAttributes(
      TransparencyAttributes.BLENDED, valor
    );
    setTransparencyAttributes(ta);
  }
  
  // CARGAR TEXTURA
  public final void asignarTextura(String archivo) {
    String nombre = 
      getClass().getResource("/imagenes/" + archivo).getFile();
    Texture textura = new TextureLoader(nombre, null).getTexture();
    setTexture(textura);
  }

}
