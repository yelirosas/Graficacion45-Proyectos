

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.*;  
import static com.jogamp.opengl.GL2.*; 
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.GL2ES2.GL_ONE_MINUS_CONSTANT_ALPHA;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class JoglBase extends GLCanvas implements GLEventListener, KeyListener {
 

    private static String TITLE = "Unidad 3 y 4 OpenGL";  
    private static final int CANVAS_WIDTH = 1000;  
    private static final int CANVAS_HEIGHT = 900; 
    private static final int FPS = 60; 
    private static final float factInc = 8.0f; 
    private float fovy = 45.0f;

    private GLU glu;
    private GLUT glut;

     Texture hexagono;
    
    float rotacion = 0.0f;
    float despl = 0.0f;
    float despX =-5.0f;
    float despZ = 0.0f;
    private float angleCube = 0;
    private float speedCube = 10f;
    
    float lightX = 1f;
    float lightY = 1f;
    float lightZ = 1f;
    float dLight = 0.05f;

    final float ambient[] = {0.2f, 0.2f, 0.2f, 1.0f};
    final float position[] = {lightX, lightY, lightZ, 1.0f};
    final float mat_diffuse[] = {0.6f, 0.6f, 0.6f, 1.0f};
    final float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
    final float mat_shininess[] = {50.0f};
    final float[] colorBlack = {0.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorWhite = {1.0f, 1.0f, 1.0f, 1.0f};
    final float[] negro = {0.0f, 0.0f, 0.0f, 0.0f};
    final float[] colorDarkGray = {0.2f, 0.2f, 0.2f, 1.0f};
    final float[] colorRed = {1.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorGreen = {0.0f, 1.0f, 0.0f};
    final float[] colorBlue = {0.0f, 0.0f, 1.0f};
    final float[] colorYellow = {1.0f, 1.0f, 0.0f, 1.0f};
    final float[] colorLightYellow = {.5f, .5f, 0.0f, 1.0f};
    final float[] colorMagenta = {1.0f, 0.0f, 1.0f}; // magenta};

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               
                GLCanvas canvas = new JoglBase();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                
                final JFrame frame = new JFrame(); 
                JPanel panel1 = new JPanel();

                FlowLayout fl = new FlowLayout();
                frame.setLayout(fl);

                panel1.add(canvas);
                frame.getContentPane().add(panel1);

                frame.addKeyListener((KeyListener) canvas);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) {
                                    animator.stop();
                                }
                                System.exit(0);
                            }
                        }.start();
                    }
                });

                frame.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent ev) {
                        Component c = (Component) ev.getSource();
                   
                        Dimension newSize = c.getSize();
                        panel1.setSize(newSize);
                        canvas.setSize(newSize);
                    }
                });

                frame.setTitle("Animaciones OpenGL");
                frame.pack();
                frame.setVisible(true);
                animator.start(); 
            }
        });
    }

   
    public JoglBase() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }
    
     Texture loadTexture(String imageFile) {
        Texture text1 = null;
        try {
            BufferedImage buffImage = ImageIO.read(new File(imageFile));
            text1 = AWTTextureIO.newTexture(GLProfile.getDefault(), buffImage, false);
        } catch (IOException ioe) {
            System.out.println("Problema al cargar el archivo " + imageFile);
        }
        return text1;
    }
     
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();     
        glu = new GLU();                        
        glut = new GLUT();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
        gl.glClearDepth(1.0f);      
        gl.glEnable(GL_DEPTH_TEST); 
        gl.glDepthFunc(GL_LEQUAL);  
  
       this.hexagono = this.loadTexture("src/imagenes/domino.jpg");
  
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        
        gl.glLightModelfv(GL2.GL_3D_COLOR,
                this.ambient, 0);


        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_LIGHT3);

        gl.glEnable(GL2.GL_LIGHT2);
     
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, colorMagenta, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, colorBlue, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, colorGreen, 0);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, colorMagenta, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, colorBlue, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, colorGreen, 0);

        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_CONSTANT_ATTENUATION, 0.5f);

        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, negro, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, negro, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, negro, 0);
        
        gl.glLightf(GL2.GL_LIGHT2, GL2.GL_CONSTANT_ATTENUATION, 0.8f);

        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, colorWhite, 0);
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_DIFFUSE, colorWhite, 0);
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPECULAR, colorWhite, 0);
        
        gl.glLightf(GL2.GL_LIGHT3, GL2.GL_CONSTANT_ATTENUATION, 0.3f);

        gl.glLightfv(GL2.GL_LIGHT4, GL2.GL_SPECULAR, colorWhite, 0);
        
        gl.glLightf(GL2.GL_LIGHT4, GL2.GL_CONSTANT_ATTENUATION, 0.3f);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);

    }

   
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  

        if (height == 0) {
            height = 1;   
        }
        float aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);

        
        gl.glMatrixMode(GL_PROJECTION);  
        gl.glLoadIdentity();   
        glu.gluPerspective(fovy, aspect, 0.1, 50.0); 

        
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); 
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();  
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.70f, 0.82f, 0.75f,0.0f);
        this.hexagono.bind(gl);
        this.hexagono.enable(gl);
        gl.glLoadIdentity();
        glu.gluLookAt(15.0f, 2.0f, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glTranslatef(this.despX, 0.0f, this.despZ);
        gl.glRotatef(this.rotacion, 1.0f, -1.0f, 0.0f);
        
        hexagono(gl);
        this.hexagono.disable(gl);
        gl.glEnd();

        gl.glTranslatef(-0f, -0f, 0);
gl.glRotatef(this.rotacion, 1.0f, 1.0f, 1.0f); 

        gl.glTranslatef(2f, -2f, 0);
        gl.glBegin(GL_QUADS);

        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(3.0f, 1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f); 
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(3.0f, 1.0f, 1.0f);

        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(3.0f, -1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(3.0f, -1.0f, -1.0f);

        gl.glColor3f(2.0f, 3.0f, 1.0f); 
        gl.glVertex3f(3.0f, 1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f); 
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glColor3f(2.0f, 3.0f, 1.0f); 
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(3.0f, -1.0f, 1.0f);

        gl.glColor3f(2.0f, 3.0f, 1.0f); 
        gl.glVertex3f(3.0f, -1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f); 
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(3.0f, 1.0f, -1.0f);

        gl.glColor3f(2.0f, 3.0f, 1.0f); 
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f); 
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glColor3f(2.0f, 3.0f, 1.0f); 
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(3.0f, 1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f); 
        gl.glVertex3f(3.0f, 1.0f, 1.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f); 
        gl.glVertex3f(3.0f, -1.0f, 1.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f); 
        gl.glVertex3f(3.0f, -1.0f, -1.0f);
        gl.glEnd(); 
        gl.glEnd();

        this.rotacion += 3.0f;
        if (this.rotacion > 360) {
            this.rotacion = 0;
        }

        System.out.printf("Rotacion %f \n", this.rotacion);
        gl.glFlush();

    }
    
    private void hexagono(GL2 gl){

        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(2.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(1.0f, -1.5f, 0.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1.0f, -1.5f, 0.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(2.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(1.0f, 1.5f, 0.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1.0f, 1.5f, 0.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(1.0f, -1.5f, 1.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1.0f, -1.5f, 1.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(1.0f, 1.5f, 1.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1.0f, 1.5f, 1.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(-2.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(-1.0f, -1.5f, 0.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1.0f, -1.5f, 1.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glVertex3f(-1.0f, 1.5f, 1.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(-1.0f, 1.5f, 0.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(1.0f, 1.5f, 0.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(1.0f, 1.5f, 1.0f);

        gl.glTexCoord2f(0, 1f);
        gl.glColor3f(1.0f, 0.5f, 0.0f);
        gl.glVertex3f(1.0f, -1.5f, 0.0f);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(1.0f, -1.5f, 1.0f);
        gl.glTexCoord2f(1f, 0);
        gl.glVertex3f(2.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(2.0f, 0.0f, 0.0f);

        gl.glEnd();
    }
    
    public void fondo(GL2 gl) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();

        System.out.println("codigo presionado = " + codigo);

        switch (codigo) {
            case KeyEvent.VK_LEFT:
                this.despX -= 0.2f;
                break;
            case KeyEvent.VK_RIGHT:
                this.despX += 0.2f;
                break;
            case KeyEvent.VK_DOWN:
                this.despZ += 0.2f;
                break;
            case KeyEvent.VK_UP:
                this.despZ -= 0.2f;
                break;
            case KeyEvent.VK_R:
                this.rotacion += 5.0f;
                break;
        }
        System.out.println("despX =" + this.despX + " - " + "despZ =" + this.despZ);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
