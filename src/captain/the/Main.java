package captain.the;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

public class Main extends GameApplication {
            public static int mapsizeX = 800;
            public static int mapsizeY = 800;


            public static Entity player;
            int playerLife = 50; //Package protected, da den skal bruges i andre klasser.
            boolean playerAlive = true;//Package protected, da den skal bruges i andre klasser.
            private Entity ability;
            int playerDMG = 10;//Package protected, da den skal bruges i andre klasser.

            public static Entity enemy;
            int enemyLife = 10;//Package protected, da den skal bruges i andre klasser.

            public Entity bullet;
            boolean isBulletAlive = false;

            private boolean enemyAlive = true;


            private double movementspeed = 0.3;
            captain.the.Direction currentDirection = captain.the.Direction.NORTH;//Package protected, da den skal bruges i andre klasser.

            /*Tillader andre klasser at hente positionen på player*/
            public double getPlayerPosX(){
                return player.getX();
            }
            public double getPlayerPosY(){
                return player.getY();
            }

            public int getEnemyLife() {
                return enemyLife;
            }

            public void setEnemyLife(int enemyLife) {
                this.enemyLife = enemyLife;
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

                /**/
                input.addInputMapping(new InputMapping("AutoAttack",KeyCode.K));


            }


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


            @Override
            protected void onUpdate(double tpf) {
                if(isBulletAlive==true) {
                    bullet.translateTowards(enemy.getCenter(), 1);
                }

                /*Får bot/enemy til at rykke sig mod spiller*/

                double point2dXTowards= (player.getX());
                double point2dYTowards= (player.getY());

                Point2D point2DTilSpiller = new Point2D(point2dXTowards,point2dYTowards);
                //enemy.translateTowards(point2DTilSpiller,0.1);//Får enemy til at gå imod spiller.

            }

            @Override
            protected void initPhysics(){
                getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.BULLET) {
                    @Override
                    protected void onCollisionBegin(Entity enemy, Entity coin) {
                        bullet.removeFromWorld();
                        isBulletAlive = false;
                    }
                });
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

            /*Hitting "animation" samt skade til enemy. */
            @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_BEGIN)
            public void hitBegin() {
                Main.player.setViewFromTexture("pixil-layer-Background-Attacking.png");
                player.setScaleX(0.75);
                player.setScaleY(0.75);

                /*Når man står oppe over enemy og kigger ned */
                if(currentDirection.equals(Direction.SOUTH)) {
                    if ((player.getY()) < enemy.getY()
                            && enemy.getY() < ((player.getY()) + 32)
                            && ((player.getX()) - 10) < enemy.getX()
                            && enemy.getX() < ((player.getX()) + 10)) {
                        System.out.println("enemy hit. From North." +enemyLife);
                        enemyLife-=playerDMG;


                    }
                }
                /*Når man står neden under enemy og kigger op*/
                if(currentDirection.equals(Direction.NORTH)) {
                    if ((player.getY()) > enemy.getY()
                            && enemy.getY() < ((player.getY()) + 32)
                            && ((player.getX()) - 10) < enemy.getX()
                            && enemy.getX() < ((player.getX()) + 10)) {
                        System.out.println("enemy hit. From South." + enemyLife);
                        enemyLife -= playerDMG;

                    }
                }
                /*Når man står på højre side og kigger mod venstre slår ham. */
                if(currentDirection.equals(Direction.WEST)) {
                    if ((player.getX()) > enemy.getX()
                            && enemy.getX() < ((player.getX()) + 32)
                            && ((player.getY()) - 10) < enemy.getY()
                            && enemy.getY() < ((player.getY()) + 10)) {
                        System.out.println("enemy hit. From east." + enemyLife);
                        enemyLife -= playerDMG;

                    }
                }
                /*Når man står på venstre side og slår mod højre*/
                if(currentDirection.equals(Direction.EAST)) {
                    if ((player.getX()) < enemy.getX()
                            && enemy.getX() < ((player.getX()) + 32)
                            && ((player.getY()) - 10) < enemy.getY()
                            && enemy.getY() < ((player.getY()) + 10)) {
                        System.out.println("enemy hit. From west?." + enemyLife);
                        enemyLife -= playerDMG;

                    }
                }

                /*Spørg om enemy bliver ramt, hvis ja, mister han liv..*/


            }


            @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_END)
            public void hitEnd() {
                Main.player.setViewFromTexture("pixil-layer-Background-Standart.png");
                player.setScaleX(0.75);
                player.setScaleY(0.75);
                getAudioPlayer().playSound("aaeffect.mp3");
            }

            /**/
            @OnUserAction(name = "AutoAttack", type = ActionType.ON_ACTION_BEGIN)
            public void hitBeginAA(){

                if (isBulletAlive == false) {
                    isBulletAlive = true;

                    bullet = Entities.builder()
                            .type(Types.BULLET)

                            .at((player.getX() + 64), (player.getY() + 64))
                            .viewFromTextureWithBBox("Turtle.png")
                            //.viewFromNodeWithBBox(new Rectangle(10,10,Color.BLUE))
                            .with(new CollidableComponent(true))
                            .buildAndAttach(getGameWorld());
                }
                }
            @OnUserAction(name = "AutoAttack", type = ActionType.ON_ACTION_END)
            public void hitEndAA() {
                //bullet.removeFromWorld();

                }


            @Override
            protected void initSettings(GameSettings settings) {
                settings.setWidth(mapsizeX);
                settings.setHeight(mapsizeY);
                settings.setTitle("Legends Arise");
                //settings.setMenuEnabled(true); //Viser menuen.
                settings.setIntroEnabled(false); //Fjerner introen
                settings.setVersion("0.2");
            }

            public static void main(String args[]){
                System.out.println("hello world!");
                launch(args);
            }
}
