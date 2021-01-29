/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class Raster {
    
    int anchura;
    int altura;
    int[] Pixel;
    
    public Raster(int w, int h) {
        anchura = w;
        altura = h;
        Pixel = new int[w*h];
    }
    
    /**
     *  Este constructor crea un Raster inicializado con 
     *  el contenido de una imagen
    */
    public Raster(Image img){

      try {
          
           PixelGrabber grabber = new PixelGrabber(img, 0, 0, -1, -1, true);

           if (grabber.grabPixels()) {
               anchura = grabber.getWidth();
               altura = grabber.getHeight();
               Pixel = (int []) grabber.getPixels();
            }

       } catch (InterruptedException e) {

       }

    }    
    
    /* Retorna el número de pixeles   */
    public final int size( ){  
          return Pixel.length;
    }	

    /* Rellena el objeto Raster con un color solido */
    public final void fill(Color c){
        int s = size();
        int rgb = c.getRGB();
        for (int i = 0; i < s; i++)
             Pixel[i] = rgb;
    }

    /* Convierte la imagen rasterizada a un objeto Image */
    public final Image toImage(Component root){
          return root.createImage(new MemoryImageSource(anchura, altura, Pixel, 0, anchura));
    }    
    
    /*  Obtiene un color desde un Raster */
    public final Color getColor(int x, int y){
           return new Color(Pixel[y*anchura+x]);
    }

    /*  Establece un Pixel en un valor dado  */
    public final boolean setPixel(int pix, int x, int y){
          Pixel[y*anchura+x] = pix;
          return true;
    }

    /* Establece un Pixel en un color dado */

    public final boolean setColor(Color c, int x, int y){
          Pixel[y*anchura+x] = c.getRGB();
          return true;
    } 
}
