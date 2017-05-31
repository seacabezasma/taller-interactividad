/**
 * Created by Sergio on 29/05/2017.
 * some functions where taken and modified from:
 * proscene's luxor example
 * proscene's flock example
 *Each example code where created by Jean Pierre Charalambos
 */
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PGraphics3D;
import jogamp.opengl.*;
import processing.core.PApplet;
import remixlab.dandelion.geom.Quat;
import remixlab.dandelion.geom.Vec;
import remixlab.proscene.*;

public class Car {

    public PApplet parent;
    public PVector pos, vel, acc, lastAcc;
    public Quat q;

    public Car(PApplet p, PVector v)
    {
        parent = p;
        pos = v;
        vel = new PVector(0,0, 0);
        acc = new PVector((float)0.1, (float) 0, (float) 0);
        lastAcc = new PVector((float)0.1, (float) 0, (float) 0);
    }

    public void drawCar()
    {
        parent.pushStyle();
        //parent.rotateX(parent.PI/2);
        q = Quat.multiply(new Quat( new Vec(0, 1, 0), PApplet.atan2(-vel.z, vel.x)),
                new Quat( new Vec(0, 0, 1), PApplet.asin(vel.y / vel.mag())) );

        parent.pushMatrix();
        // Multiply matrix to get in the frame coordinate system.
        parent.popMatrix();
        parent.popStyle();
    }

    void move() {
        vel.add(acc); // add acceleration to velocity
        pos.add(vel); // add velocity to position
        vel.limit(8); // make sure the velocity vector magnitude does not
        acc.limit((float) 2);
        //acc.mult(0); // reset acceleration
    }
}
