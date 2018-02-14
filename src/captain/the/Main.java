package captain.the;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.view.ScrollingBackgroundView;
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
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.LocalTimer;
import com.sun.webkit.event.WCMouseWheelEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

public class Main extends GameApplication {

    public static int mapsizeX = 1230;
    public static int mapsizeY = 487;
    Input input = new Input();

    /*Player Stuff*/
    public static Entity player;
    int playerLife = 50; //hvor meget liv spiller har
    boolean playerAlive = true;//er Spiller i live
    int playerMANA = 100;//Player mana mængde
    private Entity ability;
    int playerDMG = 10;
    int playerAARange = 100; // AA range, hvor langt player kan slå
    boolean isPlayerMoving = false; //Aktiveres når player skal gå et sted hen.
    boolean playerOnlyAttacking = false;//Aktiveres hvis spiller ikke skal rykke sig, men kun skyde.
    boolean cancelPlayerAA = false;//Gør at player kan AA forevigt ved 1 klik
    /*Abilities*/
    Entity mousePosEntity;
    Point2D pointedSpot2DForAbility =new Point2D(0,0) ;//Hvor man kaster abilitien hen
    Point2D pointedSpot2DForAA =new Point2D(0,0) ;//Hvor man kaster abilitien hen
    boolean isQAbilityAlive = false;
    Entity qAbilityEntity;
    int qAbilityDMG = 50;
    boolean isEAbilityAlive = false;
    Entity eAbilityEntity;



    Point2D clickedspot;//Hvor man har klikket på skærmen

    /*The Wall & UI*/
    public static Entity wallEntity;
    int wallHP=250;
    boolean isWallAlive;
    Entity UIEntity;

    /*Enemy*/
    int enemiesSlainCounter = 0;
    EnemyControl enemyControl = new EnemyControl();
    public static Entity enemy;
    public static int enemyLife = 100;//Package protected, da den skal bruges i andre klasser.
    int enemyDMG=10;
    private boolean enemyAlive = true;
    boolean enemyOnWall = false;

    /*Bullet/ Auto attack*/
    public Entity bullet;
    boolean isBulletAlive = false;


    private double movementspeed = 0.3;
    captain.the.Direction currentDirection = captain.the.Direction.NORTH;//Package protected, da den skal bruges i andre klasser.

    /*Tillader andre klasser at hente positionen på player*/
    public double getPlayerPosX() {
        return player.getX();
    }

    public double getPlayerPosY() {
        return player.getY();
    }

    public int getEnemyLife() {
        return enemyLife;
    }

    /*-Input fra bruger (control af figuren) -*/
    @Override
    protected void initInput() {
        Input input = getInput(); //laver et input objekt
        /*Manual Movement #1 til højre*/
        input.addAction(new UserAction("Move Right") {
        @Override
        protected void onAction() {
            player.translateX(movementspeed);//går 1 pixel til højre ->
            player.setRotation(90);
            currentDirection = Direction.EAST;
        }

    }, KeyCode.D);
    /*Manual Movement #2 til venstre*/
        input.addAction(new UserAction("Move Left") {
        @Override
        protected void onAction() {
            player.translateX(-movementspeed);//Går 1 pixel til venstre
            player.setRotation(270);
            currentDirection = Direction.WEST;
        }

    }, KeyCode.A);
    /*Manual Movement #3 op*/
        input.addAction(new UserAction("Move Up") {
        @Override
        protected void onAction() {
            player.translateY(-movementspeed);//Går 1 pixel op
            player.setRotation(0);
            currentDirection = Direction.NORTH;
        }
    }, KeyCode.W);
    /*Manual Movement #4 ned*/
        input.addAction(new UserAction("Move Down") {
        @Override
        protected void onAction() {
            player.translateY(movementspeed);//Går 1 pixel ned
            player.setRotation(180);
            currentDirection = Direction.SOUTH;
        }
    }, KeyCode.S);

        /*-Tillader at afspille metoder, der er skrevet længere nede. -*/
        /*Q - Ability*/
        input.addInputMapping(new InputMapping("QAbility", KeyCode.Q));
        /*Left Click - Movement og AA*/
        input.addInputMapping(new InputMapping("AutoAttack", MouseButton.PRIMARY));


    }


    @Override
    protected void initGame() {
        /*Sætter UI billede i world*/
        UIEntity = Entities.builder()
                .at(((mapsizeX/2)-100),370)
                .viewFromTexture("HPMANAHP.png")
                .buildAndAttach(getGameWorld());
        UIEntity.setScaleX(0.50);
        UIEntity.setScaleY(0.50);


        /*Sætter Væg i world*/
        wallEntity = Entities.builder()
                .at(0,0)//pos
                .type(Types.WALL)//type
                .viewFromNodeWithBBox(new Rectangle(80,487,Color.BROWN))//Dens form samt en hitbox om den
                .with(new CollidableComponent(true))//den can collide med andre entities.
                .buildAndAttach(getGameWorld());//bliver placeret i world

        /*Sætter Player i world.*/
        player = Entities.builder()
                .at(300, 300)//player start pos
                .type(Types.PLAYER)//Af hvilken type player er.
                .viewFromTextureWithBBox("8bitcircle.png")//Sætter figuren til at være dette billede(med hitbox af dette billede)
                .with(new CollidableComponent(true))//den can collide med andre entities.
                .buildAndAttach(getGameWorld());//Sætter den i verdenen
        player.setScaleX(0.75);//Scaleringen på X af figuren(player)
        player.setScaleY(0.75);//Scaleringen på Y af figuren(player)



    }

    double spawnEnemyTimerino=1100;
    double timerinoQ=0;
    double bulletTimerino = 0;
    @Override
    protected void onUpdate(double tpf) {
        /*Hvad AA skalgøre*/
        if (isBulletAlive == true) {
            bullet.translateTowards(pointedSpot2DForAA, 1);
            if (bulletTimerino<300){
                bulletTimerino++;
            }else {
                bullet.removeFromWorld();
                bulletTimerino =0;
                isBulletAlive = false;
            }
        }

        if(spawnEnemyTimerino>1000){
            getGameWorld().spawn("enemy",mapsizeX,(mapsizeY/2));

            spawnEnemyTimerino=0;
        }else{
            spawnEnemyTimerino+=1;
        }


        /*Hvad player skal gøre når han skal rykke sig.*///bliver ikke brugt
        if (isPlayerMoving == true && playerOnlyAttacking == false) {
            //(playerOnlyAttacking==false)Gør at player ikke rykker sig, hvis han angriber.
            //(isPlayerMoving==true)"spørg" om den skal køre denne funktion.
            player.translateTowards(clickedspot, movementspeed);

        }

        /*Ability- Q*/
        if(isQAbilityAlive==true){
            timerinoQ+=0.5;
            if(timerinoQ<300) {
                qAbilityEntity.translateTowards(pointedSpot2DForAbility, 1);
            }else{
                timerinoQ=0;//resetter timer til næste q.
                isQAbilityAlive=false;
                qAbilityEntity.removeFromWorld();//fjerner abilitien fra mappet.
            }
        }

        /*Enemy på wall(får enemy til at skade wall)*/
        /*if(enemyOnWall==true){
            if(timer.elapsed(Duration.seconds(0.5))){
                System.out.println("wall be takin dmg");
            }
        }*/


        /*Får bot/enemy til at rykke sig mod spiller*/

        double point2dXTowards = (player.getX());
        double point2dYTowards = (player.getY());

        Point2D point2DTilSpiller = new Point2D(point2dXTowards, point2dYTowards);
        //enemy.translateTowards(point2DTilSpiller,0.1);//Får enemy til at gå imod spiller.

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.BULLET) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity coin) {
                bullet.removeFromWorld();
                enemyControl.setLocalEnemylife(playerDMG);
                if(enemyControl.getLocalEnemylife()<=0){
                    enemy.removeFromWorld();
                    enemyControl.resetLocalEnemyLife();
                    getGameState().increment("enemiesslain", +1);

                }

                isBulletAlive = false;
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.QAbility) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity QAbilty) {
                enemy.removeFromWorld();
                getGameState().increment("enemiesslain", +1);

                /*Det er er ikke nødvendigt, da jeg gerne vil have at qability OneShotter enemies*/
                /*enemyControl.setLocalEnemylife(qAbilityDMG);
                if(enemyControl.getLocalEnemylife()<=0){
                    enemy.removeFromWorld();
                    enemyControl.resetLocalEnemyLife();
                }*/

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.WALL, Types.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity wallEntity, Entity enemy) {
                enemy.setViewFromTexture("pixelExplosion.gif");//viser explosion gif når wall bliver ramt
                enemyOnWall=true;
                wallHP -= enemyControl.getLocalEnemylife();//får wall til at miste liv
                getAudioPlayer().playSound("Explosion6.mp3");//afspiller eksplosion når wall bliver ramt
                getGameState().increment("playerLifeWall", -enemyControl.getLocalEnemylife()); //får wall til at miste liv
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity entity) {
                playerLife-=enemyDMG;
                getGameState().increment("playerLifeInt", -enemyDMG);
                enemy.setX(entity.getX()+100); //Skubber den 100 pixels væk
                if(playerLife<=0){
                    getDisplay().showConfirmationBox("u ded... \n" + "Enemies slain : " + getGameState().getProperties().put("enemiesslain","yes"), yes -> {
                        if (yes){exit();}
                    });
                }
            }
        });

    }

    @Override
    protected void initUI() {
        //PlayerHP
        Text textPixelsNumbers = new Text();
        textPixelsNumbers.setTranslateX((mapsizeX/2)+10);//UI på X
        textPixelsNumbers.setTranslateY(mapsizeY-67);//UI på Y
        getGameScene().addUINode(textPixelsNumbers);
        textPixelsNumbers.textProperty().bind(getGameState().intProperty("playerLifeInt").asString());//Printer pixelsMoved

        //PlayerMana
        Text textPixelsNumbers2 = new Text();
        textPixelsNumbers2.setTranslateX((mapsizeX/2)+9);//UI på X
        textPixelsNumbers2.setTranslateY(mapsizeY-50);//UI på Y
        getGameScene().addUINode(textPixelsNumbers2);
        textPixelsNumbers2.textProperty().bind(getGameState().intProperty("playerLifeIntMan").asString());//Printer pixelsMoved

        //WallHP
        Text textPixelsNumbers3 = new Text();
        textPixelsNumbers3.setTranslateX((mapsizeX/2)+9);//UI på X
        textPixelsNumbers3.setTranslateY(mapsizeY-33);//UI på Y
        getGameScene().addUINode(textPixelsNumbers3);
        textPixelsNumbers3.textProperty().bind(getGameState().intProperty("playerLifeWall").asString());//Printer pixelsMoved

        //EnemiesSlain

        Text textPixelsNumbers4 = new Text();
        textPixelsNumbers3.textProperty().bind(getGameState().intProperty("enemiesslain").asString());//Printer pixelsMoved
        //getGameState().increment("enemiesslain", +1);


    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("playerLifeIntMan", playerMANA);
        vars.put("playerLifeWall", wallHP);
        vars.put("playerLifeInt", playerLife);
        vars.put("enemiesslain",enemiesSlainCounter);
    }






    /**/
    @OnUserAction(name = "AutoAttack", type = ActionType.ON_ACTION_BEGIN)
    public void hitBeginAA() {


        /*Hvis man klikker på enemy-"Autoattack"*//*
        enemy.getView().setOnMouseClicked(event -> {
            if (isBulletAlive == false) {
                isBulletAlive = true;
                playerOnlyAttacking = true;

                bullet = Entities.builder()
                        .type(Types.BULLET)
                        .at((player.getX() +32), (player.getY())+32)
                        .viewFromNodeWithBBox(new Rectangle(10, 10, Color.BLUE))
                        .with(new CollidableComponent(true))
                        .buildAndAttach(getGameWorld());

            }
        });*/
        double pointedSpot2D2x = (getInput().getMouseXUI());
        double pointedSpot2D2y = (getInput().getMouseYUI());
        //pointedSpot2D = new Point2D(pointedSpot2D2x,pointedSpot2D2y);
        if (isBulletAlive == false) {
            pointedSpot2DForAA = new Point2D(pointedSpot2D2x,pointedSpot2D2y);
            isBulletAlive = true;
            playerOnlyAttacking = true;

            bullet = Entities.builder()
                    .type(Types.BULLET)
                    .at((player.getX() +32), (player.getY())+32)
                    .viewFromNodeWithBBox(new Rectangle(10, 10, Color.BLUE))
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());
        }
/*
        if (isBulletAlive == true) { //gør at man ikke kan rykke sig mens man skyder.
            playerOnlyAttacking = true;//Køre en funktion oppe i onUpdate.
        }

        /*Hvis man bare klikker på mappet/hvis man vil rykke sig*//*
        clickedspot = getInput().getMousePositionUI();
        isPlayerMoving = true;
        if (isPlayerMoving == true) {
            cancelPlayerAA = true;
        }*/
    }

    @OnUserAction(name = "AutoAttack", type = ActionType.ON_ACTION_END)
    public void hitEndAA() {
        //bullet.removeFromWorld();
        //playerOnlyAttacking = false;//gør den tilbage til false, så han kan rykke sig igen bagefter.

    }
    /*Q ability*/
    int qAbilityCost = 20;//hvor meget abilitien koster at bruge
    @OnUserAction(name = "QAbility", type = ActionType.ON_ACTION_BEGIN)
    public void hitBeginQ() {
        if(isQAbilityAlive==false&&(playerMANA>=qAbilityCost)){
            //Da vi gerne vil have q til at spawne inde i player(og billedet er stort med en lille spinner i midten)
            //bliver jeg nødt til at justere dens spawn, samt hvor den skal hen.
            double pointedSpot2D2x = (getInput().getMouseXUI()-25);
            double pointedSpot2D2y = (getInput().getMouseYUI()-25);
            pointedSpot2DForAbility = new Point2D(pointedSpot2D2x,pointedSpot2D2y);
            //pointedSpot2D = getInput().getMousePositionUI();
            mousePosEntity = Entities.builder().type(Types.MOUSEPOS).at(pointedSpot2DForAbility).with(new CollidableComponent()).buildAndAttach(getGameWorld());


            isQAbilityAlive=true;
            qAbilityEntity = Entities.builder()

                    .type(Types.QAbility)
                    .at((player.getX()),(player.getY()))
                    .viewFromTextureWithBBox("qAbility.gif")
                    //.bbox(new HitBox(BoundingShape.circle(32)))
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());

            getGameState().increment("playerLifeIntMan", -qAbilityCost);
            playerMANA-=qAbilityCost;
        }
    }



    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(mapsizeX);
        settings.setHeight(mapsizeY);
        settings.setTitle("Legends Arise Alpha");
        settings.setFullScreenAllowed(true);
        //settings.setMenuEnabled(true); //Viser menuen.
        settings.setIntroEnabled(false); //Fjerner introen
        settings.setVersion("0.5.3");
    }
    @Override
    protected void preInit(){
        getAudioPlayer().playMusic("fireAuraBackgroundMusic.mp3");

    }

    public static void main(String args[]) {
        System.out.println("hello world!");
        launch(args);
    }
}
//                getGameState().increment("pixelsMoved", +1);