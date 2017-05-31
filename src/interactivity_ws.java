/**
 * Created by Sergio on 27/05/2017.
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
import org.gamecontrolplus.*;
import org.gamecontrolplus.gui.*;
import net.java.games.input.*;
import java.util.ArrayList;


public class interactivity_ws extends PApplet{

    public Scene scene;
    public PShape carShape;
    public InteractiveFrame frame;
    public Car sl;
    public Quat q;
    public ControlIO control;
    public ControlDevice pxn;
    public float globAngL, globAngR, globAng;
    public ArrayList<Float> angles = new ArrayList<>();

    public void settings()
    {
        size(700, 600, P3D);
    }

    public void setup()
    {
        scene = new Scene(this);
        //scene.setAxesVisualHint(false);
        //scene.setGridVisualHint(false);
        scene.setBoundingBox(new Vec(0, 0, 0), new Vec(1200, 500, 600));
        scene.showAll();

        carShape = loadShape("car.obj");
        frame = new InteractiveFrame(scene);

        control = ControlIO.getInstance(this);
        pxn = control.getMatchedDevice("pxn-controller");
        //pxn.getSlider("LT-RT").(this, "accelerate", ControlIO.SLIDER_TYPE);

        carShape.rotateZ(PApplet.PI/2);
        carShape.rotateY(PApplet.PI/2);
        carShape.rotateX(PApplet.PI/2);

        sl = new Car(this, new PVector(0, -60, 0));

        frame.setPosition(new Vec(sl.pos.x, sl.pos.y, sl.pos.z));
        frame.setTrackingEyeAzimuth(-PApplet.radians(90));
        frame.setTrackingEyeInclination(PApplet.radians(0));
        frame.setTrackingEyeDistance(scene.radius()/2);
        scene.setAvatar(frame);
        scene.startAnimation();
    }

    public void draw()
    {
        background(0);
        fill(50, 10, 100);
        box(1000, 30, 1000);
        accelerate();
        turn();
        sl.move();
        //sl.drawCar();
        frame.setPosition(new Vec(sl.pos.x, sl.pos.y, sl.pos.z));
        translate(sl.pos.x, sl.pos.y, sl.pos.z);
        scale((float) 0.5);
        shape(carShape);

        q = Quat.multiply(new Quat( new Vec(0, 1, 0), PApplet.atan2(-sl.vel.z, sl.vel.x)),
                new Quat( new Vec(0, 0, 1), PApplet.asin(sl.vel.y / sl.vel.mag())) );

        frame.setRotation(q);
        pushMatrix();
        frame.applyTransformation();
        popMatrix();
    }

    public void accelerate()
    {
        PVector t = new PVector();
        t.set(sl.acc);

        float acMag = map(pxn.getSlider("LT-RT").getValue(), (float) -1, (float) 1, (float) 1.2, (float) -1.2);

        //if(sl.acc.mag() == 0 && (-pxn.getSlider("LT-RT").getValue() <= -0.1 || -pxn.getSlider("LT-RT").getValue() >= 0.1))
        //if(sl.acc.mag() == 0)
        //{
            //sl.acc.set(sl.lastAcc);
        //    sl.acc.mult(acMag);
        //}

        /*if(sl.acc.mag() <= 0.1 && (-pxn.getSlider("LT-RT").getValue() > -0.1 && -pxn.getSlider("LT-RT").getValue() < 0.1))
        {
            sl.acc.mult(0);
        }*/

        if(-pxn.getSlider("LT-RT").getValue() >= 0.8)
        {
            sl.acc.add(t.normalize().mult((float) 0.2));
            //sl.acc.setMag(acMag + sl.acc.mag());
        }

        if(-pxn.getSlider("LT-RT").getValue() <= -0.8)
        {
            /*t.normalize();
            t.set(-t.x, -t.y, -t.z);
            sl.acc.add(t.mult((float) 0.2));*/
            sl.acc.sub(t.normalize().mult((float) 0.4));
            //sl.acc.setMag(sl.acc.mag() + acMag);
        }

        if(sl.acc.mag()!=0)
            sl.lastAcc.set(sl.acc);
        System.out.println(sl.acc.mag());

        //sl.acc.setMag(acMag + sl.acc.mag());
        //System.out.println(acMag);
    }

    void turn()
    {
        float dt = pxn.getSlider("LEFT_X").getValue(), ang;
        if(dt >= 0.5)
        {
            ang = -PApplet.PI * 5 / 180;
            pushMatrix();
            if(globAng<=3)
            {
            carShape.rotateY(ang);
            globAng+=1;
            angles.add(ang);
            }
            sl.acc.set((sl.acc.z*PApplet.cos(ang)) + (sl.acc.x*PApplet.sin(ang)), sl.acc.y,
                    (sl.acc.z*PApplet.cos(ang)) - (sl.acc.x*PApplet.sin(ang)));
            //carShape.rotateY(-ang);
            //globAngL-=1;
            popMatrix();
        }
        else if(dt <= -0.5)
        {
            ang = PApplet.PI * 5 / 180;
            pushMatrix();
            if(globAng<=3)
            {
            carShape.rotateY(ang);
            globAng+=1;
            angles.add(ang);
            }
            sl.acc.set((sl.acc.z*PApplet.cos(ang)) + (sl.acc.x*PApplet.sin(ang)), sl.acc.y,
                    (sl.acc.z*PApplet.cos(ang)) - (sl.acc.x*PApplet.sin(ang)));
            //carShape.rotateY(-ang);
            //globAngR-=1;
            popMatrix();
        }

        else
        {
            if(angles.size()!=0)
            {
                carShape.rotateY(- angles.remove(angles.size()-1));
                globAng-=1;
            }
        }


    }

    public void keyPressed()
    {
  /*      switch (key)
        {
            case 'w':
                //sl.acc.setMag(sl.acc.mag()*(float)7.3);
                break;

            default:
                break;
        }*/
    }

    public static void main(String[] args)
    {
        PApplet.main("interactivity_ws");
    }
}
