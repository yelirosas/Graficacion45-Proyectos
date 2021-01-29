package Unidad2;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class PizarraOriginal extends javax.swing.JFrame {

    static final int LINEA = 0;
    static final int TRIANGULO = 1;
    static final int CUADRADO = 2;
    static final int CIRCULO = 3;

    // Ancho y alto de la pizarra 
    static final int ANCHO = 640;
    static final int ALTO = 480;

    Raster raster;

    Point p1, p2, p3, p4;
    boolean bP1 = false, bP2 = false, bP3 = false, bP4 = false;
    int figura = LINEA;

    //paneles que se utilizaran 
    
    JPanel panelRaster;
    JPanel panelControles;
    JPanel panelFiguras;
    JPanel panelTransformaciones;
    JScrollPane scrollFiguras;

    JList listFiguras;
    DefaultListModel listModel;
    ArrayList<Figura> aFiguras; //contenedor de figuras 

    //botones que se utilizaran 
    
    JButton btnColor;
    JToggleButton rbLinea;
    JToggleButton rbTriang;
    JToggleButton rbCirculo;
    JToggleButton rbCuadrado;

    JButton btnTrasladar;
    JButton btnEscalar;
    JButton btnRotar;

    JTextField campoTX;
    JTextField campoTY;
    JTextField campoEX;
    JTextField campoEY;
    JTextField campoR;

    Matrix matrix;

    ButtonGroup bg;

    Color color;
    JColorChooser colorChooser;
    JButton btnGuardarRast;
    JButton btnGuardarVect;
    JButton btnAbrirVect;
    JButton btnBorrar;

    public static void main(String[] args) {

    }
}
