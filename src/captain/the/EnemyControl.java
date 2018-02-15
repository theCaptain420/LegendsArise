package captain.the;


import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static captain.the.Main.*;

/*Enemy Control*/
public class EnemyControl extends Control {
    private double point2dXRandomGen= (Math.random()*mapsizeX);
    private double point2dYRandomGen= (Math.random()*mapsizeY);
    private double enemyMovementSpeed = 0.35;
    public int enemySpawnOnY = (int) (Math.random()*mapsizeY);

    private int localEnemylifeBaseHP = 50;
    private int LocalEnemylife = localEnemylifeBaseHP;
    public void setLocalEnemylife(int DMG){ this.LocalEnemylife -=DMG;
        System.out.println("enemy life to :"+this.LocalEnemylife);}//Ændrer enemy liv.
    public int getLocalEnemylife(){return LocalEnemylife;}
    public void resetLocalEnemyLife(){this.LocalEnemylife=localEnemylifeBaseHP; }

    public void setEnemyMovementSpeed(double movementSpeed){
        this.enemyMovementSpeed = movementSpeed;
    }

    Point2D point2dWallerino = new Point2D(0,enemySpawnOnY);

    Point2D point2DTilRandomSpot = new Point2D(point2dXRandomGen,point2dYRandomGen);
    @Override
    public void onUpdate(Entity entity, double tpf) {
        if(entity.distance(Main.player)<=100){
            entity.translateTowards(Main.player.getPosition(), enemyMovementSpeed);

        }else {

            entity.translateTowards(point2dWallerino, enemyMovementSpeed);

        }
        if(LocalEnemylife<=0){
            entity.removeFromWorld();
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

    public void setLocalEnemylifeBaseHP(int i){ this.localEnemylifeBaseHP=i;    }

    public int getEnemySpawnOnY(){ return enemySpawnOnY;}

}
