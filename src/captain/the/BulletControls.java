package captain.the;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;

import java.awt.*;


public class BulletControls extends Control {
    private double targetXLocation = 100;
    private double targetYLocation = 100;

    Entity bullet = new Entity();

    Point2D point2DTarget = new Point2D(targetYLocation,targetYLocation);

    public void setBulletControls(double targetXLocation,double targetYLocation ){
        this.targetXLocation = targetXLocation;
        this.targetYLocation = targetYLocation;
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
        bullet.translateTowards(point2DTarget,0.1);

    }

    Point p = MouseInfo.getPointerInfo().getLocation();


/*
    public Point2D getMousepos(){
        return Input.Scennput.getMousePositionUI();
    }*/
}
