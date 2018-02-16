package captain.the;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;

public class Main extends GameApplication {
    //Sætter banens Størrelse.
    public static int mapsizeX = 1230;
    public static int mapsizeY = 487;

    //Laver et input af typen input
    Input input = new Input();

    /*Player Stuff*/
    public static Entity player;
    int playerLife = 50; //hvor meget liv spiller har
    int playerMANA = 100;//Player mana mængde
    int playerDMG = 10; //Hvor meget player bullets/aa skader
    boolean isPlayerMoving = false; //Aktiveres når player skal gå et sted hen.
    boolean playerOnlyAttacking = false;//Aktiveres hvis spiller ikke skal rykke sig, men kun skyde.
    private double movementspeed = 0.3;//Hvor hurtigt spiller rykker sig

    /*Abilities*/
    Entity mousePosEntity;//Musens position på mappet
    Point2D pointedSpot2DForAbility = new Point2D(0, 0);//Hvor man kaster abilitien hen
    boolean isQAbilityAlive = false;//Om q er aktiv
    Entity qAbilityEntity;//Q's entity
    int qAbilityDMG = 50; //Hvor meget q skader
    Point2D clickedspot;//Hvor man har klikket på skærmen

    /*The Wall & UI*/
    public static Entity wallEntity; //Entity væg
    int wallHP = 250;//wall hp
    Entity UIEntity;//"Billedet" der viser hp og mana

    /*Enemy*/
    int enemiesSlainCounter = 1; //hvor mange man har dræbt
    EnemyControl enemyControl = new EnemyControl();//Henter et objekt af typen enemycontrol
    public static Entity enemy;
    public static int enemyLife = 100;//Package protected, da den skal bruges i andre klasser.
    int enemyDMG = 10;//Hvor meget enemy skader på player


    /*Bullet/ Auto attack*/
    public Entity bullet;//Bullet af typen entity
    Point2D pointedSpot2DForAA = new Point2D(0, 0);//Hvor man kaster bullet hen
    boolean isBulletAlive = false; //Så man kun kan skyde 1 skud af gangen

    /*Hvilken vej player er.*/
    captain.the.Direction currentDirection = captain.the.Direction.NORTH;//Package protected, da den skal bruges i andre klasser.

    /*Tillader andre klasser at hente positionen på player*/
    public double getPlayerPosX() { return player.getX(); }
    public double getPlayerPosY() { return player.getY(); }

    /*Tillader andre klasser at hente enemylife*/
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
                player.setRotation(90); //Får player til at "kigge" den rigtige vej
                currentDirection = Direction.EAST; //Ændrer hans direction
            }

        }, KeyCode.D);//På klik D

        /*Manual Movement #2 til venstre*/
        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.translateX(-movementspeed);//Går 1 pixel til venstre
                player.setRotation(270);//Får player til at "kigge" den rigtige vej
                currentDirection = Direction.WEST;//Ændrer hans direction
            }

        }, KeyCode.A);


        /*Manual Movement #3 op*/
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player.translateY(-movementspeed);//Går 1 pixel op
                player.setRotation(0);//Får player til at "kigge" den rigtige vej
                currentDirection = Direction.NORTH;//Ændrer hans direction
            }
        }, KeyCode.W);


        /*Manual Movement #4 ned*/
        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.translateY(movementspeed);//Går 1 pixel ned
                player.setRotation(180);//Får player til at "kigge" den rigtige vej
                currentDirection = Direction.SOUTH;//Ændrer hans direction
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
        /*Sætter UI billedet i world(så det kan ses)*/
        UIEntity = Entities.builder()
                .at(((mapsizeX / 2) - 100), 370)
                .viewFromTexture("HPMANAHP.png")
                .buildAndAttach(getGameWorld());
        //Sætter skaleringen på det.
        UIEntity.setScaleX(0.50);
        UIEntity.setScaleY(0.50);


        /*Sætter Væg i world*/
        wallEntity = Entities.builder()
                .at(0, 0)//pos
                .type(Types.WALL)//type
                .viewFromNodeWithBBox(new Rectangle(80, 487, Color.BROWN))//Dens form samt en hitbox om den
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

    /*Tider der bliver timet ved hjælp af onUpdate*/
    //Gør at en enemy spawner med det samme
    double spawnEnemyTimerino = 1100;
    //Hvor tit en enemy skal spawne
    double enemySpawnerinoTimer = 800;
    //tæller Q tid
    double timerinoQ = 0;
    //tæller bullet tid
    double bulletTimerino = 0;

    @Override
    protected void onUpdate(double tpf) {
        /*Hvad AA skalgøre*/
        //Hvis den er i live skal den gå mod det sted hvor musen er.
        //Og hvis tiden(bulletTimerino) er gået, bliver den slettet fra world.
        if (isBulletAlive == true) {
            bullet.translateTowards(pointedSpot2DForAA, 1);
            if (bulletTimerino < 300) {
                bulletTimerino++;
            } else {
                bullet.removeFromWorld();
                bulletTimerino = 0;
                isBulletAlive = false;
            }
        }

        /*Hvor tit enemyspawner*/
        //Spawner enten en enemy eller tæller videre
        if (spawnEnemyTimerino > enemySpawnerinoTimer) {
            getGameWorld().spawn("enemy", mapsizeX, (mapsizeY / 2));
            spawnEnemyTimerino = 0;
        } else {
            spawnEnemyTimerino += 1;
        }




        /*Ability- Q*/
        //Hvis abilitien er i live, skal den enten rykke hen mod et musen,
        //ellers bliver den slettet
        if (isQAbilityAlive == true) {
            timerinoQ += 0.5;
            if (timerinoQ < 300) {
                qAbilityEntity.translateTowards(pointedSpot2DForAbility, 1);
            } else {
                timerinoQ = 0;//resetter timer til næste q.
                isQAbilityAlive = false;
                qAbilityEntity.removeFromWorld();//fjerner abilitien fra mappet.
            }
        }

    }

    /*Klasse når der sker collisioner mellem 2 entities*/
    @Override
    protected void initPhysics() {
        /*-Metode der checker om der er collision mellem en enemy entity og bullet entity, samt hvad den skal gøre-*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.BULLET) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity coin) {
                //Fjerne bullet fra world
                bullet.removeFromWorld();

                //Ændrer enemies liv
                enemyControl.setLocalEnemylife(playerDMG);

                //Checker om enemies liv er under/lig 0
                //Hvis den er, fjerner den enemy og giver point, samt mana.
                if (enemyControl.getLocalEnemylife() <= 0) {
                    //enemy
                    enemy.removeFromWorld();
                    enemyControl.resetLocalEnemyLife();//Gendanner enemy liv

                    //Tilføjer point og mana
                    getGameState().increment("enemiesslain", +1);
                    getGameState().increment("playerLifeIntMan", +1);//får mana for hver enemyslain
                    enemiesSlainCounter++;
                    playerMANA += 1;

                    //Afspiller lyd, når den dør.
                    getAudioPlayer().globalSoundVolumeProperty().setValue(0.2); //sætter volume af sounds
                    getAudioPlayer().playSound("Beep2.mp3");//afspiller sound


                }
                //fortæller programmet at bullet ikke længere er i live.
                isBulletAlive = false;
            }
        });

        /*-Metode der checker om der er collision mellem en enemy entity og qAbility entity, samt hvad den skal gøre-*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.QAbility) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity QAbilty) {
                //enemy
                enemy.removeFromWorld();

                //Tilføjer point og mana
                getGameState().increment("enemiesslain", +1);
                getGameState().increment("playerLifeIntMan", +2);//får mana for hver enemyslain
                playerMANA += 2;
                enemiesSlainCounter++;

                //Afspiller lyd
                getAudioPlayer().globalSoundVolumeProperty().setValue(0.2); //sætter volume af sounds
                getAudioPlayer().playSound("Beep2.mp3");//afspiller sound

                /*Det er er ikke nødvendigt, da jeg gerne vil have at qability OneShotter enemies*/
                /*enemyControl.setLocalEnemylife(qAbilityDMG);
                if(enemyControl.getLocalEnemylife()<=0){
                    enemy.removeFromWorld();
                    enemyControl.resetLocalEnemyLife();
                }*/


            }
        });

        /*-Metode der checker om der er collision mellem en wall entity og enemy entity, samt hvad den skal gøre-*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.WALL, Types.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity wallEntity, Entity enemy) {
                //enemy
                enemy.setViewFromTexture("pixelExplosion.gif");//viser explosion gif når wall bliver ramt

                //Afspiller en lyd når den dør
                getAudioPlayer().globalSoundVolumeProperty().setValue(0.2); //sætter volume af sounds
                getAudioPlayer().playSound("Explosion6.mp3");//afspiller sound

                //Ændre væggens liv
                wallHP -= 50;//får wall til at miste liv
                getGameState().increment("LifeWall", -50); //får wall til at miste liv

                //Hvis wall hp er under 0, laver den gameover
                if (wallHP <= 0) {
                    getDisplay().showConfirmationBox("You Died! \n" + "Wall HP reached 0. \n" + "Enemies slain : " + getGameState().getProperties().put("enemiesslain", "yes"), yes -> {
                        if (yes) {
                            exit();
                        }

                    });
                }
            }
        });

        /*-Metode der checker om der er collision mellem en enemy entity og player entity, samt hvad den skal gøre-*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.ENEMY, Types.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity entity) {
                playerLife -= enemyDMG; //Ændre player liv i koden.

                getGameState().increment("playerLifeInt", -enemyDMG);//(Til UI), ændre værdien af player liv
                enemy.setX(entity.getX() + 100); //Skubber den 100 pixels væk
                //Hvis player hp er under 0, laver den gameover
                if (playerLife <= 0) {
                    getDisplay().showConfirmationBox("You Died! \n" + "Player HP reached 0. \n" + "Enemies slain : " + getGameState().getProperties().put("enemiesslain", "yes"), yes -> {
                        if (yes) {
                            exit();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void initUI() {
        /*
        * Alt herunder har noget at gøre med opsætningen af UI.
        * Det er ikke andet end position, og at gøre den aktiv.(Så den kan ændres og ses.)
        *
        * */

        //PlayerHP
        Text textPixelsNumbers = new Text();
        textPixelsNumbers.setTranslateX((mapsizeX / 2) + 10);//UI på X
        textPixelsNumbers.setTranslateY(mapsizeY - 67);//UI på Y
        getGameScene().addUINode(textPixelsNumbers);
        textPixelsNumbers.textProperty().bind(getGameState().intProperty("playerLifeInt").asString());//Printer pixelsMoved

        //PlayerMana
        Text textPixelsNumbers2 = new Text();
        textPixelsNumbers2.setTranslateX((mapsizeX / 2) + 9);//UI på X
        textPixelsNumbers2.setTranslateY(mapsizeY - 50);//UI på Y
        getGameScene().addUINode(textPixelsNumbers2);
        textPixelsNumbers2.textProperty().bind(getGameState().intProperty("playerLifeIntMan").asString());//Printer pixelsMoved

        //WallHP
        Text textPixelsNumbers3 = new Text();
        textPixelsNumbers3.setTranslateX((mapsizeX / 2) + 9);//UI på X
        textPixelsNumbers3.setTranslateY(mapsizeY - 33);//UI på Y
        getGameScene().addUINode(textPixelsNumbers3);
        textPixelsNumbers3.textProperty().bind(getGameState().intProperty("LifeWall").asString());//Printer pixelsMoved

        //EnemiesSlain

        Text textPixelsNumbers4 = new Text();
        textPixelsNumbers4.textProperty().bind(getGameState().intProperty("enemiesslain").asString());//Printer pixelsMoved

        //En metode der bliver brugt til at ændre værdierne på UI.
        //Bliver brugt rundt omkring i spillet.
        //getGameState().increment("playerLifeIntMan", +1);


    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //Putter UI ind i spillet, så det kan ses og ændres.
        vars.put("playerLifeIntMan", playerMANA);
        vars.put("LifeWall", wallHP);
        vars.put("playerLifeInt", playerLife);
        vars.put("enemiesslain", enemiesSlainCounter);
    }


    /*Når man venstre kikker kommer der 1 skud ud, dette er metoden.*/
    @OnUserAction(name = "AutoAttack", type = ActionType.ON_ACTION_BEGIN)
    public void hitBeginAA() {

        //Musens position / hvor skudet skal flyve hen.
        double pointedSpot2D2x = (getInput().getMouseXUI());
        double pointedSpot2D2y = (getInput().getMouseYUI());

        //Hvis der ikke er et aktivt skud på banen, skal den køre dette.
        //Basically bare et skud der flyver hen mod musens position.
        if (isBulletAlive == false) {
            //Lyd
            getAudioPlayer().globalSoundVolumeProperty().setValue(0.2); //sætter volume af sounds
            getAudioPlayer().playSound("Beep4.mp3");//afspiller sound

            //Musens position
            pointedSpot2DForAA = new Point2D(pointedSpot2D2x, pointedSpot2D2y);

            //Ændre skud til at være aktiv, så man kun at have et skud af gangen.
            isBulletAlive = true;

            //laver en bullet.
            bullet = Entities.builder()
                    .type(Types.BULLET)
                    .at((player.getX() + 32), (player.getY()) + 32)
                    .viewFromNodeWithBBox(new Rectangle(10, 10, Color.BLUE))
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());
        }

    }



    /*Q ability*/
    //Hvor meget abilitien koster at bruge
    int qAbilityCost = 20;

    /*Når man klikker q, kommer den en ability ud, dette er metoden*/
    @OnUserAction(name = "QAbility", type = ActionType.ON_ACTION_BEGIN)
    public void hitBeginQ() {
        if (isQAbilityAlive == false && (playerMANA >= qAbilityCost)) {
            //lyd
            getAudioPlayer().globalSoundVolumeProperty().setValue(0.2); //sætter volume af sounds
            getAudioPlayer().playSound("Laser_Shoot1.mp3");//afspiller sound

            //Da vi gerne vil have q til at spawne inde i player(og billedet er stort med en lille spinner i midten)
            //bliver jeg nødt til at justere dens spawn, samt hvor den skal hen.
            double pointedSpot2D2x = (getInput().getMouseXUI() - 25);
            double pointedSpot2D2y = (getInput().getMouseYUI() - 25);
            pointedSpot2DForAbility = new Point2D(pointedSpot2D2x, pointedSpot2D2y);

            //pointedSpot2D = getInput().getMousePositionUI();
            mousePosEntity = Entities.builder().type(Types.MOUSEPOS).at(pointedSpot2DForAbility).with(new CollidableComponent()).buildAndAttach(getGameWorld());

            //Sætter til at være aktiv, bliver brugt så kun 1 q kan være aktiv af gangen.
            isQAbilityAlive = true;

            //Q builder(bygger q)
            qAbilityEntity = Entities.builder()
                    .type(Types.QAbility)
                    .at((player.getX()), (player.getY()))
                    .viewFromTextureWithBBox("qAbility.gif")
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());

            //Ændrer mana
            getGameState().increment("playerLifeIntMan", -qAbilityCost);
            playerMANA -= qAbilityCost;
        }
    }


    @Override
    protected void initSettings(GameSettings settings) {
        //Bestemmer størrelse af window
        settings.setWidth(mapsizeX);
        settings.setHeight(mapsizeY);

        //titlen
        settings.setTitle("Legends Arise Alpha");
        settings.setVersion("0.5.4");

        //Om man må gøre det full screen
        settings.setFullScreenAllowed(false);

        //andet
        //settings.setMenuEnabled(true); //Viser menuen.
        settings.setIntroEnabled(false); //Fjerner introen
    }

    @Override
    protected void preInit() {
        //Baggrund musik
        getAudioPlayer().playMusic("fireAuraBackgroundMusic.mp3");

    }

    //Det der kører hele skidtet
    public static void main(String args[]) {
        System.out.println("hello world!");
        launch(args);
    }
}
