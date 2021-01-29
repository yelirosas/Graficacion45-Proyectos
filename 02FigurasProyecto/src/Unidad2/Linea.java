/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.Color;
import java.awt.Point;

public class Linea extends Figura {
    Point Punto1;
    Point Punto2;

    Linea(Point _p1, Point _p2, Color _color) {
        Punto1 = new Point(_p1.x,_p1.y);
        Punto2 = new Point(_p2.x,_p2.y);
        Color  = _color;
}
}

