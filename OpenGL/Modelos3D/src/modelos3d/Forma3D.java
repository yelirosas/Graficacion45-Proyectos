package modelos3d;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public abstract class Forma3D extends Shape3D {

    protected Point3f[] v;
    protected Color3f[] c;
    protected Point3f[] vC;
    protected Color3f[] cC;

    public Forma3D() {
        setAppearance(new Appearance());
        crearColores();
    }

    public final void addComponentes(Color color) {
        asignarVertices();
        if (color == null) {
            asignarColores();
        } else {
            asignarColores(color);
        }
        crearForma();
    }

    public final void crearColores() {
        c = new Color3f[]{
            new Color3f(Color.RED), new Color3f(Color.GREEN),
            new Color3f(Color.BLUE), new Color3f(Color.CYAN),
            new Color3f(Color.MAGENTA), new Color3f(Color.YELLOW),
            new Color3f(Color.DARK_GRAY), new Color3f(Color.GRAY),
            new Color3f(Color.LIGHT_GRAY), new Color3f(Color.ORANGE),
            new Color3f(Color.PINK), new Color3f(Color.BLACK)
        };
    }

    public final void asignarColores(Color color) {
        cC = new Color3f[vC.length];
        for (int i = 0; i < cC.length; i++) {
            cC[i] = new Color3f(color);
        }
    }

    public abstract void asignarVertices();

    public abstract void asignarColores();

    public abstract void crearForma();
}
