package modelos3d;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Fondo3D extends Background {
  private Color colorFondo;

  public Fondo3D(Color color) {
    setFondo(color);
    aplicarBounds();
  }
  
  public Fondo3D(File archivo) {
    setFondo(archivo);
    aplicarBounds();
  }

  private void aplicarBounds(){
    this.setCapability(Background.ALLOW_COLOR_WRITE);
    this.setCapability(Background.ALLOW_IMAGE_WRITE);
    Bounds bounds = new BoundingSphere();
    setApplicationBounds(bounds);    
  }
    
  public final void setFondo(Color color) {
    setColor(new Color3f(color));
    if(this.getImage()!=null){
      this.setImage(null);
    }
    colorFondo = color;
  }

  public final void setFondo(File archivo) {
    try {
      BufferedImage imagen = ImageIO.read(archivo);
      ImageComponent2D imagen2D = 
        new ImageComponent2D(ImageComponent2D.FORMAT_RGB, imagen);
      setImage(imagen2D);
    } catch (IOException ex) {
      System.out.println("Error " + ex);
    }
  }
    
  
  public Color getColorFondo(){
    return colorFondo;
  }
}


