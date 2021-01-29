/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.Color;
import java.awt.Point;

//Calculo de Aristas para el triangulo

public class Triangulo extends Figura {
    protected Vertex2D v[];
    protected int Color_Int;
    Color Color;
    Point p1, p2, p3;
    
    public Triangulo(Point p1, Point p2, Point p3, Color c)
    {
        this.p1 = new Point(p1.x, p1.y);
        this.p2 = new Point(p2.x, p2.y);
        this.p3 = new Point(p3.x, p3.y);
        this.Color = c;
    }
    
    public Triangulo(Vertex2D v0, Vertex2D v1, Vertex2D v2, Color _color)
    {
        Color = _color;        
        Color_Int = _color.getRGB();
        
        v = new Vertex2D[3];
        v[0] = v0;
        v[1] = v1;
        v[2] = v2;
        
        int a = ((v0.argb >> 24) & 255) + ((v1.argb >> 24) & 255) + ((v2.argb >> 24) & 255);
        int r = ((v0.argb >> 16) & 255) + ((v1.argb >> 16) & 255) + ((v2.argb >> 16) & 255);
        int g = ((v0.argb >> 8) & 255) + ((v1.argb >> 8) & 255) + ((v2.argb >> 8) & 255);
        int b = (v0.argb & 255) + (v1.argb & 255) + (v2.argb & 255);
        
        a = (a + a + 3) / 6;
        r = (r + r + 3) / 6;
        g = (g + g + 3) / 6;
        b = (b + b + 3) / 6;
        
        Color_Int = (a << 24) | (r << 16) | (g << 8) | b;
    }

    
    protected EdgeEqn edge[];
    protected int area;
    protected int xMinimo, xMaximo, yMinimo, yMaximo;
    private static byte sort[][] = {
        {0, 1}, {1, 2}, {0, 2}, {2, 0}, {2, 1}, {1, 0}
    };

    
    public void dibujar(Raster r)
    {
        if (!inicializarTriangulo(r)) return;
        
        int x, y;  
        int A0 = edge[0].A;
        int A1 = edge[1].A;
        int A2 = edge[2].A;
        
        int B0 = edge[0].B;
        int B1 = edge[1].B;
        int B2 = edge[2].B;
        
        int t0 = A0*xMinimo + B0*yMinimo + edge[0].C;
        int t1 = A1*xMinimo + B1*yMinimo + edge[1].C;
        int t2 = A2*xMinimo + B2*yMinimo + edge[2].C;
        
        yMinimo *= r.anchura;
        yMaximo *= r.anchura;
                
        for (y = yMinimo; y <= yMaximo; y += r.anchura) {
	        int e0 = t0;
	        int e1 = t1;
	        int e2 = t2;
	        int xflag = 0;
	        for (x = xMinimo; x <= xMaximo; x++) {
	            if ((e0|e1|e2) >= 0) {      
		            r.Pixel[y+x] = Color_Int;
		            xflag++;
	            } else if (xflag != 0) break;
	            e0 += A0;
	            e1 += A1;
	            e2 += A2;
	        }
	        t0 += B0;
	        t1 += B1;
	        t2 += B2;
        }
        
    }
    
protected boolean inicializarTriangulo(Raster r)
    {
        if (edge == null) edge = new EdgeEqn[3];
        
        /*
            Calculo de las 3 aristas
        */
        edge[0] = new EdgeEqn(v[0], v[1]);
        edge[1] = new EdgeEqn(v[1], v[2]);
        edge[2] = new EdgeEqn(v[2], v[0]);
        
        /*
            Truco #1: 
            Oriente los bordes de modo que el 
            interior del triángulo quede dentro 
            de sus semiespacios positivos.
        */
        area = edge[0].C + edge[1].C + edge[2].C;
        if (area == 0) return false;                // degenerar el triangulo
        if (area < 0) {
            edge[0].flip();
            edge[1].flip();
            edge[2].flip();
            area = -area;
        }
        
        /*
            Truc0 #2: calcular el recuadro que encierra el triangulo
        */
        int xflag = edge[0].flag + 2*edge[1].flag + 4*edge[2].flag;
        int yflag = (xflag >> 3) - 1;
        xflag = (xflag & 7) - 1;
        
        xMinimo = (int) (v[sort[xflag][0]].x);
        xMaximo = (int) (v[sort[xflag][1]].x + 1);
        yMinimo = (int) (v[sort[yflag][1]].y);
        yMaximo = (int) (v[sort[yflag][0]].y + 1);
        
        /*
            recortar el espacio del triangulo
        */
        xMinimo = (xMinimo < 0) ? 0 : xMinimo;
        xMaximo = (xMaximo >= r.anchura) ? r.anchura - 1 : xMaximo;
        yMinimo = (yMinimo < 0) ? 0 : yMinimo;
        yMaximo = (yMaximo >= r.altura) ? r.altura - 1 : yMaximo;
        return true;
}
}
