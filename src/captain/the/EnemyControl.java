package captain.the;


import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

import static captain.the.Main.mapsizeX;
import static captain.the.Main.mapsizeY;

/*Enemy Control*/
public class EnemyControl extends Control {
    //Hvor hurtigt enemy skal rykke sig.
    private double enemyMovementSpeed = 0.35;

    //Hvor enemy skal spawne på y aksen.
    public int enemySpawnOnY = (int) (Math.random() * mapsizeY);

    //Deklarerer og initialiserer enemy's liv
    private int localEnemylifeBaseHP = 50;
    private int LocalEnemylife = localEnemylifeBaseHP;

    //Hvor enemy skal gå hen(på væggen).
    Point2D point2dWallerino = new Point2D(0, enemySpawnOnY);

    //Laver et random spot på mappet
    private double point2dXRandomGen = (Math.random() * mapsizeX);
    private double point2dYRandomGen = (Math.random() * mapsizeY);
    Point2D point2DTilRandomSpot = new Point2D(point2dXRandomGen, point2dYRandomGen);

    //Ændre enemyliv, altså hvor meget han mister
    public void setLocalEnemylife(int DMG) {
        this.LocalEnemylife -= DMG;
        System.out.println("enemy life to :" + this.LocalEnemylife);
    }

    //Tillader andre klasser at hente enemy liv
    public int getLocalEnemylife() {
        return LocalEnemylife;
    }

    //Reseter enemy liv til start værdien
    public void resetLocalEnemyLife() {
        this.LocalEnemylife = localEnemylifeBaseHP;
    }

    //tillader andre klasser at ændre movementspeed på enemy
    public void setEnemyMovementSpeed(double movementSpeed) {
        this.enemyMovementSpeed = movementSpeed;
    }

    //Sætter basis livet på enemy.
    public void setLocalEnemylifeBaseHP(int i) {
        this.localEnemylifeBaseHP = i;
    }

    //Giver andre klasser mulighed for at hente hvor enemy spawner på Y.
    public int getEnemySpawnOnY() {
        return enemySpawnOnY;
    }

    //Opupdate: Hvad spillet skal gøre pr. window unload
    @Override
    public void onUpdate(Entity entity, double tpf) {
        //Hvis enemy er tæt på player, går den mod player. Ellers går den mod wall.
        if (entity.distance(Main.player) <= 100) {
            entity.translateTowards(Main.player.getPosition(), enemyMovementSpeed);
        } else {
            entity.translateTowards(point2dWallerino, enemyMovementSpeed);
        }

        //Hvis enemy liv <= 0, dør den/ bliver slettet fra spillet.
        if (LocalEnemylife <= 0) {
            entity.removeFromWorld();
        }
    }



}
