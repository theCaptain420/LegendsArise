package captain.the;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.CollisionResult;
import com.almasb.fxgl.physics.box2d.collision.Collision;
import com.almasb.fxgl.scene.GameScene;
import com.almasb.fxgl.settings.GameSettings;
import com.sun.javafx.geom.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.apache.http.entity.EntityTemplate;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Map;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class Main extends GameApplication {
            private int mapsizeX = 800;
            private int mapsizeY = 800;

            HittingAnimation hittingAnimation = new HittingAnimation();

            public static Entity player;
            private int playerLife = 50;
            private boolean playerAlive = true;
            private Entity ability;
            private int playerDMG = 10;

            public static Entity enemy;
            private int enemyLife = 10;
            private boolean enemyAlive = true;


            private double movementspeed = 0.3;
            private enum Direction {NORTH, SOUTH, WEST, EAST};

            private Direction currentDirection = Direction.NORTH;

            /*Tillader andre klasser at hente positionen på player*/
            public double getPlayerPosX(){
                return player.getX();
            }
            public double getPlayerPosY(){
                return player.getY();
            }


            /*Input fra bruger samt control af figuren */
            @Override
            protected void initInput() {
                Input input = getInput(); //laver et input objekt
                input.addAction(new UserAction("Play Sound") {
                @Override
                    protected void onActionBegin(){
                    //getAudioPlayer().playSound("pumpedUpKicks.mp3");
                }
                }, KeyCode.F);


                input.addAction(new UserAction("Move Right") {
                @Override
                    protected void onAction(){
                    player.translateX(movementspeed);//går 1 pixel til højre ->

                    getGameState().increment("pixelsMoved",+1);
                    player.setRotation(90);
                    currentDirection = Direction.EAST;
                }

                }, KeyCode.D);

                input.addAction(new UserAction("Move Left") {
                    @Override
                    protected void onAction(){
                        player.translateX(-movementspeed);//Går 1 pixel til venstre
                        getGameState().increment("pixelsMoved",+1);
                        player.setRotation(270);
                        currentDirection = Direction.WEST;
                    }

                }, KeyCode.A);

                input.addAction(new UserAction("Move Up") {
                    @Override
                    protected void onAction() {
                        player.translateY(-movementspeed);//Går 1 pixel op
                        getGameState().increment("pixelsMoved",+1);
                        player.setRotation(0);
                        currentDirection = Direction.NORTH;
                    }
                }, KeyCode.W);

                input.addAction(new UserAction("Move Down") {
                    @Override
                    protected void onAction() {
                        player.translateY(movementspeed);//Går 1 pixel ned
                        getGameState().increment("pixelsMoved",+1);
                        player.setRotation(180);
                        currentDirection = Direction.SOUTH;
                    }
                }, KeyCode.S);

                /*Når man slår */
                input.addInputMapping(new InputMapping("Hit",KeyCode.J));

            }




            @Override
            protected void initGame() {
                player = Entities.builder()
                        .at(300,300)//player start pos
                        .viewFromTexture("Turtle.png")//Sætter figuren til at være dette billede
                        .buildAndAttach(getGameWorld());
                player.setScaleX(1);//Scaleringen på X af figuren(player)
                player.setScaleY(1);//Scaleringen på Y af figuren(player)


                enemy = Entities.builder()
                        .at(500,500)
                        .viewFromTexture("Turtle.png")
                        .buildAndAttach(getGameWorld());


            }

            @Override
            protected void initPhysics(){

            }

            @Override
            protected void initUI(){
                Text textPixels = new Text();
                textPixels.setTranslateX(50);//UI på 50X
                textPixels.setTranslateY(50);//UI på 50Y

                getGameScene().addUINode(textPixels);//Burde skrive hvor meget man har rykket sig.

                textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString());//Printer pixelsMoved

            }

            @Override
            protected void initGameVars(Map<String, Object> vars) {
                vars.put("pixelsMoved", 0);
            }
            //@Override
            //protected void onUpdate(){}
            //@Override
            //protected void initAssets() throws Exception{}

            /*
            public void getDirection(){
                Entity ability = new Entity();
                ability.translateX(player.getX()+20);
                ability.translateY(player.getY()+20);
                ability.setProperty("alive",true);

                float directionX = (float) player.getX();
                float directionY = (float) player.getY();

                if(currentDirection == Direction.NORTH){
                    directionX += 20;
                }else if(currentDirection == Direction.SOUTH){
                    directionX -= 20;
                }else if(currentDirection == Direction.WEST){
                    directionY += 20;
                }else if(currentDirection == Direction.EAST){
                    directionY -= 20;
                }

            }*/

            /*Hitting animation!*/
            @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_BEGIN)
            public void hitBegin() {
                Main.player.setViewFromTexture("pixil-layer-Background(1).png");
                player.setScaleX(3);
                player.setScaleY(3);

                /*Spørg om enemy bliver ramt, hvis ja, mister han liv..*/
                if((player.getY() + 100) < enemy.getY() || enemy.getY() < player.getY()){
                    enemyLife -= playerDMG;
                    System.out.println("enemy hit. " + enemyLife);
                }

            }


            @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_END)
            public void hitEnd() {
                Main.player.setViewFromTexture("pixil-layer-Background.png");
                player.setScaleX(3);
                player.setScaleY(3);
                getAudioPlayer().playSound("aaeffect.mp3");
            }

            @Override
            protected void initSettings(GameSettings settings) {
                settings.setWidth(mapsizeX);
                settings.setHeight(mapsizeY);
                settings.setTitle("Legends Arise");
                //settings.setMenuEnabled(true); //Viser menuen.
                settings.setIntroEnabled(false); //Fjerner introen
                settings.setVersion("0.1");
            }

            public static void main(String args[]){
                System.out.println("hello world!");
                launch(args);
            }
}
