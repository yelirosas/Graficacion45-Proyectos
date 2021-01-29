/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class MyPanel extends JPanel{
    Raster raster;
    Dimension preTamaño;
    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }       

    public MyPanel(Raster _raster) {
        preTamaño = new Dimension(_raster.anchura,_raster.altura);
        this.setSize(preTamaño);
        this.raster = _raster;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return preTamaño;
    }
    
    public void setRaster(Raster _raster){
        this.raster = _raster;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        Image output =  raster.toImage(this);
        g.drawImage(output, 0, 0, this);
    }
}
