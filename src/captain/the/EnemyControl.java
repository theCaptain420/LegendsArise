package captain.the;


import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;

import static captain.the.Main.enemy;
import static captain.the.Main.mapsizeX;
import static captain.the.Main.mapsizeY;

/*VIRKER IKKE*/
public class EnemyControl extends Control {
    private double point2dXRandomGen= (Math.random()*mapsizeX);
    private double point2dYRandomGen= (Math.random()*mapsizeY);

    Point2D point2DTilRandomSpot = new Point2D(point2dXRandomGen,point2dYRandomGen);
    @Override
    public void onUpdate(Entity entity, double tpf) {
        if(entity.getPosition()==point2DTilRandomSpot){
            point2dXRandomGen = (Math.random()*mapsizeX)+200;
            point2dYRandomGen = (Math.random()*mapsizeY)+200;
            entity.translateTowards(point2DTilRandomSpot, 1);

        }else {

            entity.translateTowards(point2DTilRandomSpot, 1);

        }
        }

    public void moveON(Entity entity){
        if(entity.getPosition()==point2DTilRandomSpot){
            point2dXRandomGen = (Math.random()*mapsizeX);
            point2dYRandomGen = (Math.random()*mapsizeY);

        }else {

            entity.translateTowards(point2DTilRandomSpot, 1);

        }
    }

}
