
package Unidad2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Pizarraa extends javax.swing.JFrame {

  static final int LINEA = 0;
  static final int TRIANGULO = 1;
  static final int CUADRADO = 2;
  static final int CIRCULO = 3;

  static final int ANCHO = 1366; //640 ,  1366
  static final int ALTO = 1000; //480  ,   768

  Raster Raster;

  Point Punto1, Punto2, Punto3, Punto4;
  boolean bP1 = false, bP2 = false, bP3 = false, bP4 = false;
  int figura = LINEA;

  JList ListaFigura;
  DefaultListModel ListaModelo;
  ArrayList<Figura> aFiguras; //contenedor de figuras 

  Matrix Matriz;

  ButtonGroup botongrupo;

  Color Color;

  JColorChooser EscogerColor;

  public Pizarraa() {
    initComponents();

    Matriz = new Matrix();

    Punto1 = new Point();
    Punto2 = new Point();
    Punto3 = new Point();
    Punto4 = new Point();

    bP1 = false;
    bP2 = false;
    bP3 = false;
    bP4 = false;

    Raster = new Raster(ANCHO, ALTO);

    PanelRaster = new MyPanel(Raster);
    PanelRaster.setFocusable(true);
    this.PanelRaster.setBackground(Color.WHITE);
    this.add(PanelRaster, BorderLayout.CENTER);

    ListaFigura = new JList();
    ListaFigura.setModel(new DefaultListModel());
    ListaModelo = (DefaultListModel) ListaFigura.getModel();
    //
    DesplazarFigura.setViewportView(ListaFigura);
    DesplazarFigura.setPreferredSize(new Dimension(150, 125)); 

    //
    panelControles.add(DesplazarFigura);

    Color = Color.black;

    botongrupo = new ButtonGroup();

    rbLinea.setSelected(true);
    botongrupo.add(rbLinea);
    botongrupo.add(rbTriang);
    botongrupo.add(rbCirculo);
    botongrupo.add(rbCuadrado);

    aFiguras = new ArrayList<Figura>();

    this.PanelRaster.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        jPanel1MouseClicked(evt);
        System.out.println("click");
      }
    });

    this.btnTrasladar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Figura figura = aFiguras.get(ListaFigura.getSelectedIndex());

        String tipoTransf = ((JToggleButton) e.getSource()).getName();
        Point[] puntos = getPuntosFigura(figura);
        ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
        dibujarFigura(list_pprima, figura);
      }
    });

    this.btnEscalar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Figura figura = aFiguras.get(ListaFigura.getSelectedIndex());
        String tipoTransf = ((JToggleButton) e.getSource()).getName();
        Point[] puntos = getPuntosFigura(figura);
        ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
        dibujarFigura(list_pprima, figura);
      }
    });

    this.btnRotar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Figura figura = aFiguras.get(ListaFigura.getSelectedIndex());
        String tipoTransf = ((JToggleButton) e.getSource()).getName();
        Point[] puntos = getPuntosFigura(figura);
        ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
        dibujarFigura(list_pprima, figura);
      }
    });

    this.btnColor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color = JColorChooser.showDialog(null, "Seleccione un color", Color);
        btnColor.setBackground(Color);
      }
    });

    this.btnGuardarVect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        guardarVectores();
      }
    });

    this.btnAbrirVect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        abrirVectores();
      }
    });

    this.btnBorrar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        borrarFiguras();
      }
    });

    this.setVisible(true);
    this.pack();
  }

  public void dibujarFigura(ArrayList<double[]> listpprima, Figura figura) {
    String linea;
    Point p1, p2, p3, p4;
    if (figura instanceof Linea) {
      double[] pp1 = listpprima.get(0);
      double[] pp2 = listpprima.get(1);
      p1 = new Point((int) pp1[0], (int) pp1[1]);
      p2 = new Point((int) pp2[0], (int) pp2[1]);
      this.dibujarLinea(p1, p2, Color);

      Linea l = new Linea(p1, p2, Color);

      linea = String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n", l.Punto1.getX(), l.Punto1.getY(),
        l.Punto2.getX(), l.Punto2.getY(),
        l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    if (figura instanceof Triangulo) {
      double[] pp1 = listpprima.get(0);
      double[] pp2 = listpprima.get(1);
      double[] pp3 = listpprima.get(2);
      p1 = new Point((int) pp1[0], (int) pp1[1]);
      p2 = new Point((int) pp2[0], (int) pp2[1]);
      p3 = new Point((int) pp3[0], (int) pp3[1]);
      this.dibujarTriangulo(p1, p2, p3, Color);

      Triangulo l = new Triangulo(p1, p2, p3, Color);

      linea = String.format("T,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.p1.getX(), l.p1.getY(),
        l.p2.getX(), l.p2.getY(), l.p3.getX(), l.p3.getY(),
        Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    if (figura instanceof Circulo) {
      double[] pp1 = listpprima.get(0);
      double[] pp2 = listpprima.get(1);
      p1 = new Point((int) pp1[0], (int) pp1[1]);
      p2 = new Point((int) pp2[0], (int) pp2[1]);
      this.dibujarCirculoRelleno(p1, p2);

      Circulo l = new Circulo(p1, p2, Color);

      linea = String.format("C,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
        l.punto2.getX(), l.punto2.getY(),
        l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    if (figura instanceof Rectangulo) {
      double[] pp1 = listpprima.get(0);
      double[] pp2 = listpprima.get(1);
      double[] pp3 = listpprima.get(2);
      double[] pp4 = listpprima.get(3);
      p1 = new Point((int) pp1[0], (int) pp1[1]);
      p2 = new Point((int) pp2[0], (int) pp2[1]);
      p3 = new Point((int) pp3[0], (int) pp3[1]);
      p4 = new Point((int) pp4[0], (int) pp4[1]);
      this.dibujarCuadrado(p1, p2, p3, p4, Color);

      Rectangulo l = new Rectangulo(p1, p2, p3, p4, Color);

      linea = String.format("R,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
        l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
        l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    this.repaint();
  }

  public ArrayList<double[]> aplicarTransformacion(String tipoTrans, Point[] puntos) {

    Matriz.loadIdentity();

    switch (tipoTrans) {
      case "trasladar":
        int dx = Integer.parseInt(campoTX.getText());
        int dy = Integer.parseInt(campoTY.getText());
        Matriz.traslacion(dx, dy);
        break;
      case "escalar":
        int ex = Integer.parseInt(campoEX.getText());
        int ey = Integer.parseInt(campoEY.getText());
        Matriz.escalarXY(ex, ey);

        break;
      case "rotar":
        int r = Integer.parseInt(campoR.getText());
        Matriz.rotacion(r);

    }

    ArrayList<double[]> listPPrima = new ArrayList<>();
    for (int i = 0; i < puntos.length; i++) {

      double[] punto = {puntos[i].x, puntos[i].y, 1};
      double[] pprima = Matriz.pprima(punto);
      listPPrima.add(pprima);
    }

    ArrayList<double[]> listAuxPPrima = new ArrayList<>();
    if (tipoTrans.equals("rotar")) {
      double[] p1 = {puntos[0].x - listPPrima.get(0)[0], puntos[0].y - listPPrima.get(0)[1], 1};
      Matriz.loadIdentity();
      Matriz.traslacion((int) p1[0], (int) p1[1]);
      for (int i = 0; i < listPPrima.size(); i++) {
        double[] punto = {listPPrima.get(i)[0], listPPrima.get(i)[1], 1};
        double[] pprima = Matriz.pprima(punto);
        listAuxPPrima.add(pprima);
      }
      return listAuxPPrima;
    }

    return listPPrima;

  }

  public Point[] getPuntosFigura(Figura figura) {
    Point[] puntos = null;
    if (figura instanceof Linea) {
      Linea l = (Linea) figura;
      puntos = new Point[2];
      puntos[0] = l.Punto1;
      puntos[1] = l.Punto2;
      
    }

    if (figura instanceof Triangulo) {
      Triangulo l = (Triangulo) figura;
      puntos = new Point[3];
      puntos[0] = l.p1;
      puntos[1] = l.p2;
      puntos[2] = l.p3;
      
    }

    if (figura instanceof Circulo) {
      Circulo l = (Circulo) figura;
      puntos = new Point[2];
      puntos[0] = l.punto1;
      puntos[1] = l.punto2;
      
    }

    if (figura instanceof Rectangulo) {
      Rectangulo l = (Rectangulo) figura;
      puntos = new Point[4];
      puntos[0] = l.punto1;
      puntos[1] = l.punto2;
      puntos[2] = l.punto3;
      puntos[3] = l.punto4;
    }
    return puntos;
  }

  public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }
    
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    return bimage;
  }

  public static Color hex2Rgb(String colorStr) {
    return new Color(
      Integer.valueOf(colorStr.substring(1, 3), 16),
      Integer.valueOf(colorStr.substring(3, 5), 16),
      Integer.valueOf(colorStr.substring(5, 7), 16));
  }

  public void guardarImagen() {

    BufferedImage img = toBufferedImage(Raster.toImage(this));
    try {
      File outputfile = new File("saved.png");
      ImageIO.write(img, "png", outputfile);
    } catch (IOException e) {

    }
  }

  public void abrirVectores() {
    this.aFiguras.clear();
    
    JFileChooser chooser = new JFileChooser();

    FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
    chooser.setFileFilter(filter);
    
    String nombreArchivo = "";
    int returnVal = chooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      nombreArchivo = chooser.getSelectedFile().getPath();

      try {
        FileReader fr = new FileReader(nombreArchivo);
        BufferedReader br = new BufferedReader(fr);
        String linea = "";

        while ((linea = br.readLine()) != null) {
          if (linea.startsWith("L")) {
            String[] puntos = linea.split(",");
            Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
            Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
            Color color = Color.decode("#" + puntos[5].substring(2));

            this.aFiguras.add(new Linea(p1, p2, color));
            this.ListaModelo.addElement(linea);
          }

          if (linea.startsWith("T")) {
            String[] puntos = linea.split(",");
            Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
            Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
            Point p3 = new Point(Integer.parseInt(puntos[5]), Integer.parseInt(puntos[6]));
            Color color = Color.decode("#" + puntos[7].substring(2));

            this.aFiguras.add(new Triangulo(p1, p2, p3, color));
            this.ListaModelo.addElement(linea);
          }

          if (linea.startsWith("R")) {
            String[] puntos = linea.split(",");
            Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
            Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
            Point p3 = new Point(Integer.parseInt(puntos[5]), Integer.parseInt(puntos[6]));
            Point p4 = new Point(Integer.parseInt(puntos[7]), Integer.parseInt(puntos[8]));
            Color color = Color.decode("#" + puntos[9].substring(2));

            this.aFiguras.add(new Rectangulo(p1, p2, p3, p4, color));
            this.ListaModelo.addElement(linea);
          }

          if (linea.startsWith("C")) {
            String[] puntos = linea.split(",");
            Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
            Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
            Color color = Color.decode("#" + puntos[5].substring(2));

            this.aFiguras.add(new Circulo(p1, p2, color));
            this.ListaModelo.addElement(linea);
          }
        }

        br.close();
      } catch (IOException ex) {
        Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    this.redibujar();
  }

  public void redibujar() {
    Iterator itr = aFiguras.iterator();
    int i = 0;
    while (itr.hasNext()) {
      Figura f = (Figura) itr.next();

      if (f instanceof Linea) {
        Linea l = (Linea) f;
        this.dibujarLinea(l.Punto1, l.Punto2, l.Color);
      }

      if (f instanceof Triangulo) {
        Triangulo t = (Triangulo) f;
        this.dibujarTriangulo(t.p1, t.p2, t.p3, t.Color);
      }

      if (f instanceof Rectangulo) {
        Rectangulo c = (Rectangulo) f;
        this.dibujarCuadrado(c.punto1, c.punto2, c.punto3, c.punto4, c.Color);
      }

      if (f instanceof Circulo) {
        Circulo c = (Circulo) f;
        this.dibujarCirculoRelleno(c.punto1, c.punto2);
      }
    }
  }

  public void guardarVectores() {
    JFileChooser chooser = new JFileChooser();

    FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
    chooser.setFileFilter(filter);
    String rutaGuardar = "";
    int returnVal = chooser.showSaveDialog(this);

    FileWriter fw = null;
    String linea = "";

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      rutaGuardar = chooser.getSelectedFile().getPath();

      if (!rutaGuardar.contains(".txt")) {
        rutaGuardar = chooser.getSelectedFile().getPath() + ".txt";
      } else {
          
      }

      try {
        fw = new FileWriter(rutaGuardar);
        for (int i = 0; i < aFiguras.size(); i++) {
          Figura f = aFiguras.get(i);

          if (f instanceof Linea) {
            Linea l = (Linea) f;

            linea = String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n", l.Punto1.getX(), l.Punto1.getY(),
              l.Punto2.getX(), l.Punto2.getY(),
              l.Color.getRGB());
          }

          if (f instanceof Triangulo) {
            Triangulo t = (Triangulo) f;
            linea = String.format("T,%d,%d,%d,%d,%d,%d,%x\n", t.p1.x, t.p1.y,
              t.p2.x, t.p2.y,
              t.p3.x, t.p3.y,
              t.Color.getRGB());
          }

          if (f instanceof Circulo) {
            Circulo l = (Circulo) f;
            linea = String.format("C,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(), l.punto2.getX(), l.punto2.getY(), l.Color.getRGB());

          }

          if (f instanceof Rectangulo) {
            Rectangulo l = (Rectangulo) f;
            linea = String.format("R,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
              l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
              l.Color.getRGB());
          }

          fw.write(linea);
        }
      } catch (IOException ex) {
        Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        try {
          fw.close();
        } catch (IOException ex) {
          Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

  }

  public void clear() {
    int s = Raster.size();
    for (int i = 0; i < s; i++) {
      Raster.Pixel[i] ^= 0x00ffffff;
    }
    repaint();
    return;
  }

  public void lineaSimple(int x0, int y0, int x1, int y1, Color color) {
    int pix = color.getRGB();
    int dx = x1 - x0;
    int dy = y1 - y0;

    Raster.setPixel(pix, x0, y0);

    if (dx != 0) {
      float m = (float) dy / (float) dx;
      float b = y0 - m * x0;

      dx = (x1 > x0) ? 1 : -1;

      while (x0 != x1) {
        x0 += dx;
        y0 = Math.round(m * x0 + b);
        Raster.setPixel(pix, x0, y0);
      }
    }
  }

  public void lineaMejorada(int x0, int y0, int x1, int y1, Color color) {
    int pix = color.getRGB();
    int dx = x1 - x0;
    int dy = y1 - y0;
    Raster.setPixel(pix, x0, y0);
    if (Math.abs(dx) > Math.abs(dy)) {     // inclinacion < 1
      float m = (float) dy / (float) dx; // calcular inclinacion
      float b = y0 - m * x0;
      dx = (dx < 0) ? -1 : 1;
      while (x0 != x1) {
        x0 += dx;
        Raster.setPixel(pix, x0, Math.round(m * x0 + b));
      }
    } else {
      if (dy != 0) {
        float m = (float) dx / (float) dy;
        float b = x0 - m * y0;
        dy = (dy < 0) ? -1 : 1;
        while (y0 != y1) {
          y0 += dy;
          Raster.setPixel(pix, Math.round(m * y0 + b), y0);
        }
      }
    }
  }

  public void lineFast(int x0, int y0, int x1, int y1, Color color) {
    int pix = color.getRGB();
    int dy = y1 - y0;
    int dx = x1 - x0;
    int stepx, stepy;
    if (dy < 0) {
      dy = -dy;
      stepy = -Raster.anchura;
    } else {
      stepy = Raster.anchura;
    }
    if (dx < 0) {
      dx = -dx;
      stepx = -1;
    } else {
      stepx = 1;
    }
    dy <<= 1;
    dx <<= 1;
    y0 *= Raster.anchura;
    y1 *= Raster.anchura;
    Raster.Pixel[x0 + y0] = pix;
    if (dx > dy) {
      int fraction = dy - (dx >> 1);
      while (x0 != x1) {
        if (fraction >= 0) {
          y0 += stepy;
          fraction -= dx;
        }
        x0 += stepx;
        fraction += dy;
        Raster.Pixel[x0 + y0] = pix;
      }
    } else {
      int fraction = dx - (dy >> 1);
      while (y0 != y1) {
        if (fraction >= 0) {
          x0 += stepx;
          fraction -= dy;
        }
        y0 += stepy;
        fraction += dx;
        Raster.Pixel[x0 + y0] = pix;
      }
    }
  }

  private void dibujarLinea(Point _p1, Point _p2, Color color) {
    long inicio = 0, fin = 0;
   
    lineFast(_p1.x, _p1.y, _p2.x, _p2.y, color);
    
    this.repaint();
  }

  private void dibujarTriangulo(Point p1, Point p2, Point p3, Color c) {
    Vertex2D v1 = new Vertex2D(p1.x, p1.y, c.getRGB());
    Vertex2D v2 = new Vertex2D(p2.x, p2.y, c.getRGB());
    Vertex2D v3 = new Vertex2D(p3.x, p3.y, c.getRGB());

    Triangulo tri = new Triangulo(v1, v2, v3, c);

    tri.dibujar(Raster);

    this.repaint();
  }

  public void dibujarCuadrado(Point p1, Point p2, Color c) {
    Punto3 = new Point(p2.x, p1.y);
    Punto4 = new Point(p1.x, p2.y);
    lineFast(p1.x, p1.y, Punto3.x, Punto3.y, c);
    lineFast(Punto3.x, Punto3.y, p2.x, p2.y, c);
    lineFast(p2.x, p2.y, Punto4.x, Punto4.y, c);
    lineFast(Punto4.x, Punto4.y, p1.x, p1.y, c);
    this.repaint();
  }

  public void dibujarCuadrado(Point p1, Point p2, Point p3, Point p4, Color c) {
    lineFast(p1.x, p1.y, p3.x, p3.y, c);
    lineFast(p3.x, p3.y, p2.x, p2.y, c);
    lineFast(p2.x, p2.y, p4.x, p4.y, c);
    lineFast(p4.x, p4.y, p1.x, p1.y, c);
    this.repaint();
  }

  public void dibujarCirculoRelleno(Point p1, Point p2) {
    double dX = Math.pow((Math.abs(p2.x - p1.x)), 2);
    double dY = Math.pow((Math.abs(p2.y - p1.y)), 2);
    int d = (int) (Math.sqrt(dX + dY));
    int radius = d / 2;
    int multiplier = 1;
    int midPointX = (p1.x + p2.x) / 2;
    int midPointY = (p1.y + p2.y) / 2;

    midPointX /= multiplier;
    midPointY /= multiplier;
    radius /= multiplier;
    int x = 0;
    int y = radius;
    d = 1 - radius;

    drawSymmetricPoints(midPointX, midPointY, x, y, Color);
    while (x < y) {
      if (d < 0) {
        d = d + 2 * x + 3;
        x++;
      } else {
        d = d + 2 * (x - y) + 5;
        x++;
        y--;
      }
      drawSymmetricPoints(midPointX, midPointY, x, y, Color);
    }
  }

  private void drawSymmetricPoints(int mx, int my, int x, int y, Color color) {
    int c = color.getRGB();
    Raster.setPixel(c, (mx + x), (my - y));
    Raster.setPixel(c, (mx + x), (my + y));
    Raster.setPixel(c, (mx - x), (my + y));
    Raster.setPixel(c, (mx - x), (my - y));
    Raster.setPixel(c, (mx + y), (my - x));
    Raster.setPixel(c, (mx + y), (my + x));
    Raster.setPixel(c, (mx - y), (my + x));
    Raster.setPixel(c, (mx - y), (my - x));
  }

  private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {
    if (rbLinea.isSelected()) {
      figura = LINEA;
    }
    if (rbTriang.isSelected()) {
      figura = TRIANGULO;
    }
    if (rbCuadrado.isSelected()) {
      figura = CUADRADO;
    }
    if (rbCirculo.isSelected()) {
      figura = CIRCULO;
    }

    if (bP1 && bP2 && !bP3) {
      Punto3.x = evt.getX();
      Punto3.y = evt.getY();
      bP3 = true;

      dibujarLinea(Punto3, Punto3, Color);
    }

    if (bP1 && !bP2) {
      Punto2.x = evt.getX();
      Punto2.y = evt.getY();
      bP2 = true;

      dibujarLinea(Punto2, Punto2, Color);
    }

    if (!bP1) {
      Punto1.x = evt.getX();
      Punto1.y = evt.getY();
      bP1 = true;

      dibujarLinea(Punto1, Punto1, Color);
    }

    if (figura == LINEA && bP1 && bP2) {
      dibujarLinea(Punto1, Punto2, Color);
      bP1 = false;
      bP2 = false;
      bP3 = false;
      bP4 = false;
      Linea l = new Linea(Punto1, Punto2, Color);

      String linea = String.format("Linea,%.0f,%.0f,%.0f,%.0f,%x\n", l.Punto1.getX(), l.Punto1.getY(),
        l.Punto2.getX(), l.Punto2.getY(),
        l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    if (figura == TRIANGULO && bP1 && bP2 && bP3) {
      dibujarTriangulo(Punto1, Punto2, Punto3, Color);
      bP1 = false;
      bP2 = false;
      bP3 = false;
      bP4 = false;
      Triangulo l = new Triangulo(Punto1, Punto2, Punto3, Color);
      String linea = String.format("Triangulo,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.p1.getX(), l.p1.getY(),
        l.p2.getX(), l.p2.getY(), l.p3.getX(), l.p3.getY(),
        Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
    }

    if (figura == CUADRADO && bP1 && bP2) {
      Rectangulo l = new Rectangulo(Punto1, Punto2, Color);

      String linea = String.format("Rectangulo,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
        l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
        l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
      dibujarCuadrado(Punto1, Punto2, Color);
      bP1 = false;
      bP2 = false;
      bP3 = false;
      bP4 = false;
    }

    if (figura == CIRCULO && bP1 && bP2) {
      Circulo l = new Circulo(Punto1, Punto2, Color);
     
      String linea = String.format("Circulo,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(), l.punto2.getX(), l.punto2.getY(), l.Color.getRGB());

      aFiguras.add(l);
      ListaModelo.addElement(linea);
      dibujarCirculoRelleno(Punto1, Punto2);
      bP1 = false;
      bP2 = false;
      bP3 = false;
      bP4 = false;
    }
  }

  public void jPanel1KeyReleased(KeyEvent ke) {

    if (ke.getKeyCode() == KeyEvent.VK_T) {
      this.figura = 1;
    }

    if (ke.getKeyCode() == KeyEvent.VK_L) {
      this.figura = 0;
    }
  }

  public void borrarFiguras() {
    this.aFiguras.clear();
    this.ListaModelo.clear();
    this.Raster.Pixel = new int[Raster.Pixel.length];
    PanelRaster.repaint();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panelFiguras = new javax.swing.JPanel();
    jPanel3 = new javax.swing.JPanel();
    jLabel6 = new javax.swing.JLabel();
    rbLinea = new javax.swing.JToggleButton();
    rbTriang = new javax.swing.JToggleButton();
    rbCirculo = new javax.swing.JToggleButton();
    rbCuadrado = new javax.swing.JToggleButton();
    jPanel4 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    campoTX = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    campoTY = new javax.swing.JTextField();
    btnTrasladar = new javax.swing.JToggleButton();
    jPanel5 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    campoEX = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    campoEY = new javax.swing.JTextField();
    btnEscalar = new javax.swing.JToggleButton();
    jPanel6 = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    campoR = new javax.swing.JTextField();
    btnRotar = new javax.swing.JToggleButton();
    panelControles = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    btnGuardarVect = new javax.swing.JButton();
    btnAbrirVect = new javax.swing.JButton();
    btnBorrar = new javax.swing.JButton();
    btnColor = new javax.swing.JButton();
    DesplazarFigura = new javax.swing.JScrollPane();
    PanelRaster = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    panelFiguras.setPreferredSize(new java.awt.Dimension(120, 400));
    panelFiguras.setLayout(new java.awt.GridLayout(4, 1));

    jPanel3.setLayout(new java.awt.GridLayout(5, 1));

    jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel6.setText("Figuras: ");
    jLabel6.setToolTipText("");
    jPanel3.add(jLabel6);

    rbLinea.setText("Linea");
    jPanel3.add(rbLinea);

    rbTriang.setText("Triangulo");
    jPanel3.add(rbTriang);

    rbCirculo.setText("Circulo");
    rbCirculo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rbCirculoActionPerformed(evt);
      }
    });
    jPanel3.add(rbCirculo);

    rbCuadrado.setText("Rectangulo");
    jPanel3.add(rbCuadrado);

    panelFiguras.add(jPanel3);

    jPanel4.setLayout(new java.awt.GridLayout(5, 1));

    jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("X:");
    jPanel4.add(jLabel1);

    campoTX.setColumns(5);
    jPanel4.add(campoTX);

    jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("Y:");
    jPanel4.add(jLabel2);

    campoTY.setColumns(5);
    jPanel4.add(campoTY);

    btnTrasladar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
    btnTrasladar.setText("Traslacion");
    btnTrasladar.setName("trasladar"); // NOI18N
    btnTrasladar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnTrasladarActionPerformed(evt);
      }
    });
    jPanel4.add(btnTrasladar);

    panelFiguras.add(jPanel4);

    jPanel5.setLayout(new java.awt.GridLayout(5, 1));

    jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel3.setText("X: ");
    jPanel5.add(jLabel3);

    campoEX.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        campoEXActionPerformed(evt);
      }
    });
    jPanel5.add(campoEX);

    jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Y:");
    jPanel5.add(jLabel4);
    jPanel5.add(campoEY);

    btnEscalar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
    btnEscalar.setText("Escalar");
    btnEscalar.setName("escalar"); // NOI18N
    btnEscalar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEscalarActionPerformed(evt);
      }
    });
    jPanel5.add(btnEscalar);

    panelFiguras.add(jPanel5);

    jPanel6.setLayout(new java.awt.GridLayout(3, 1));

    jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Angulo");
    jPanel6.add(jLabel5);
    jPanel6.add(campoR);

    btnRotar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
    btnRotar.setText("Rotacion");
    btnRotar.setName("rotar"); // NOI18N
    jPanel6.add(btnRotar);

    panelFiguras.add(jPanel6);

    getContentPane().add(panelFiguras, java.awt.BorderLayout.WEST);

    panelControles.setPreferredSize(new java.awt.Dimension(120, 115));
    panelControles.setLayout(new java.awt.BorderLayout());

    jPanel1.setLayout(new java.awt.GridLayout(2, 1));

    jPanel2.setLayout(new java.awt.GridLayout(4, 1));

    btnGuardarVect.setText("Guardar Vector");
    btnGuardarVect.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    btnGuardarVect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGuardarVectActionPerformed(evt);
      }
    });
    jPanel2.add(btnGuardarVect);

    btnAbrirVect.setText("Abrir Vector");
    btnAbrirVect.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jPanel2.add(btnAbrirVect);

    btnBorrar.setText("Borrar");
    btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jPanel2.add(btnBorrar);

    btnColor.setText("Color");
    btnColor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jPanel2.add(btnColor);

    jPanel1.add(jPanel2);

    DesplazarFigura.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    DesplazarFigura.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    DesplazarFigura.setAlignmentY(2.0F);
    DesplazarFigura.setPreferredSize(new java.awt.Dimension(100, 80));
    jPanel1.add(DesplazarFigura);

    panelControles.add(jPanel1, java.awt.BorderLayout.NORTH);

    getContentPane().add(panelControles, java.awt.BorderLayout.EAST);

    PanelRaster.setPreferredSize(new java.awt.Dimension(640, 55));
    PanelRaster.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    getContentPane().add(PanelRaster, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void btnEscalarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscalarActionPerformed

    }//GEN-LAST:event_btnEscalarActionPerformed

    private void btnTrasladarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrasladarActionPerformed
      // TODO add your handling code here:
    }//GEN-LAST:event_btnTrasladarActionPerformed

    private void campoEXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEXActionPerformed
      // TODO add your handling code here:
    }//GEN-LAST:event_campoEXActionPerformed

    private void rbCirculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCirculoActionPerformed
      // TODO add your handling code here:
    }//GEN-LAST:event_rbCirculoActionPerformed

    private void btnGuardarVectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVectActionPerformed
      // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarVectActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Pizarraa().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JScrollPane DesplazarFigura;
  private javax.swing.JPanel PanelRaster;
  private javax.swing.JButton btnAbrirVect;
  private javax.swing.JButton btnBorrar;
  private javax.swing.JButton btnColor;
  private javax.swing.JToggleButton btnEscalar;
  private javax.swing.JButton btnGuardarVect;
  private javax.swing.JToggleButton btnRotar;
  private javax.swing.JToggleButton btnTrasladar;
  private javax.swing.JTextField campoEX;
  private javax.swing.JTextField campoEY;
  private javax.swing.JTextField campoR;
  private javax.swing.JTextField campoTX;
  private javax.swing.JTextField campoTY;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JPanel jPanel6;
  private javax.swing.JPanel panelControles;
  private javax.swing.JPanel panelFiguras;
  private javax.swing.JToggleButton rbCirculo;
  private javax.swing.JToggleButton rbCuadrado;
  private javax.swing.JToggleButton rbLinea;
  private javax.swing.JToggleButton rbTriang;
  // End of variables declaration//GEN-END:variables
}
