package captain.the;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;

public class Legend extends GameApplication {

/*-----------------------------------------*/
/*-DET HER ER EN TEST KLASSE, IGNORE DEN  -*/
/*-----------------------------------------*/
//Her tester jeg evt. metoder.

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {

    }
    Entity player, enemy;
    boolean isBulletAlive = false;
    @Override
    protected void initGame() {
        player = Entities.builder()
                .at(300,300)//player start pos
                .viewFromTexture("pixil-layer-Background-Standart.png")//Sætter figuren til at være dette billede
                .buildAndAttach(getGameWorld());
        player.setScaleX(0.75);//Scaleringen på X af figuren(player)
        player.setScaleY(0.75);//Scaleringen på Y af figuren(player)


        enemy = Entities.builder()
                .type(Types.ENEMY)
                .at(500,500)
                .viewFromTexture("enemystill.png")
                .bbox(new HitBox(BoundingShape.box(1,1)))
                .with(new EnemyControl())
                .with(new CollidableComponent(true))
                .buildAndAttach(getGameWorld());





    }

    @OnUserAction(name = "LeftClickerino", type = ActionType.ON_ACTION_BEGIN)
    public void hitBeginAA(){}

        //System.out.println(getInput().getMousePositionUI()+ " - " + enemy.getPosition() );
        /*Hvis man klikker på enemy*/
        /*AKA. Auto attack. */
/*
        enemy.getView().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isBulletAlive == false) {
                    isBulletAlive = true;
                    Entity bullet = Entities.builder()
                            .type(Types.BULLET)

                            .at((player.getX() + 64), (player.getY() + 64))
                            .viewFromNodeWithBBox(new Rectangle(10,10,Color.BLUE))
                            .with(new CollidableComponent(true))
                            .buildAndAttach(getGameWorld());
                }
            }} );}
            */

    public void iwoqeu(){
        System.out.println("reee");
    }
}
