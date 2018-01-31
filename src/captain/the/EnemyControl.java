package captain.the;


import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;


public class EnemyControl extends Control {
    Main mainCaller = new Main();

    private PhysicsComponent physics;

    private LocalTimer jumpTimer;

    @Override
    public void onAdded(Entity entity) {
        jumpTimer = FXGL.newLocalTimer();
        jumpTimer.capture();
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
        if (jumpTimer.elapsed(Duration.seconds(0.01))) {
            jump();
            jumpTimer.capture();
        }
    }

    public void jump() {
        mainCaller.enemyposXTilSpawn += 10;
        mainCaller.enemyposYTilSpawn += 10;


    }
}
