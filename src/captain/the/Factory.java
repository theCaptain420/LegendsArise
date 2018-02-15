package captain.the;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SetEntityFactory
public class Factory implements EntityFactory {
    //Farven på "enemy"
    Color color = Color.RED;

    //En builder/skabelon til alle "enemy" entities der bliver spawnet.
    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {

        return Entities.builder()
                .type(Types.ENEMY) //type enemy
                .from(data)
                //.viewFromTextureWithBBox("8bitTriangle.png")//Hvis jeg gerne ville have den til at være et billedet i stedet for rectangle
                .viewFromNodeWithBBox(new Rectangle(25, 25, color))//Dens useende med hitbox
                .with(new CollidableComponent(true))//Om den kan kollidere med andre entities.
                .with(new EnemyControl())//Får alle enemies til at bruge EnemyControl klassen.
                .build();//Sætter den i verdenen

    }

    //Tillader andre klasser at bestemme farven på "enemy"
    public void setColor(Color color) {
        this.color = color;
    }
}
