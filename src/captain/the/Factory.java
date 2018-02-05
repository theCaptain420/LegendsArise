package captain.the;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

@SetEntityFactory
public class Factory implements EntityFactory {
    private LocalTimer jumpTimer;

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(Types.ENEMY)
                .from(data)
                .viewFromNodeWithBBox(new Rectangle(30, 30, Color.RED))
                .with(new EnemyControl())
                .build();


        }

    @Spawns("bullet")
    public  Entity newBullet(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(Types.BULLET)
                .from(data)
                .with(new BulletControls())
                .build();

        }

}
