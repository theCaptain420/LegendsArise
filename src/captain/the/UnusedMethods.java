package captain.the;

import javafx.scene.input.KeyCode;

public class UnusedMethods {

    /*AT GÅ FREM OG TILBAGE*/
    /*-----------------------------------------*/
    /*
    * input.addAction(new UserAction("Play Sound") {
            @Override
            protected void onActionBegin() {
                //getAudioPlayer().playSound(".mp3");
            }
        }, KeyCode.F);

        /*Manual Movement #1*/
    /*
        input.addAction(new UserAction("Move Right") {
        @Override
        protected void onAction() {
            player.translateX(movementspeed);//går 1 pixel til højre ->

            player.setRotation(90);
            currentDirection = Direction.EAST;
        }

    }, KeyCode.RIGHT);
    /*Manual Movement #2*//*
        input.addAction(new UserAction("Move Left") {
        @Override
        protected void onAction() {
            player.translateX(-movementspeed);//Går 1 pixel til venstre
            player.setRotation(270);
            currentDirection = Direction.WEST;
        }

    }, KeyCode.LEFT);
    /*Manual Movement #3*//*
        input.addAction(new UserAction("Move Up") {
        @Override
        protected void onAction() {
            player.translateY(-movementspeed);//Går 1 pixel op
            player.setRotation(0);
            currentDirection = Direction.NORTH;
        }
    }, KeyCode.UP);
    /*Manual Movement #4*//*
        input.addAction(new UserAction("Move Down") {
        @Override
        protected void onAction() {
            player.translateY(movementspeed);//Går 1 pixel ned
            player.setRotation(180);
            currentDirection = Direction.SOUTH;
        }
    }, KeyCode.DOWN);
    *
    * */
    /*----------------------------*/

    /*Sætter baggrund*/
        /*
        Texture background = getAssetLoader().loadTexture("background.jpg");
        ScrollingBackgroundView baggrund = new ScrollingBackgroundView(background, Orientation.HORIZONTAL);
        getGameScene().addGameView(baggrund);
*/
        /*--------------------------*/

    /*Hitting "animation" samt skade til enemy. *//*
    //Skulle bruges til spil lign legend of zelda.
    @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_BEGIN)
    public void hitBegin() {
        Main.player.setViewFromTexture("pixil-layer-Background-Attacking.png");
        player.setScaleX(0.75);
        player.setScaleY(0.75);
        /*Når man står oppe over enemy og kigger ned *//*
        if (currentDirection.equals(Direction.SOUTH)) {
            if ((player.getY()) < enemy.getY()
                    && enemy.getY() < ((player.getY()) + 32)
                    && ((player.getX()) - 10) < enemy.getX()
                    && enemy.getX() < ((player.getX()) + 10)) {
                System.out.println("enemy hit. From North." + enemyLife);
                enemyLife -= playerDMG;


            }
        }
        /*Når man står neden under enemy og kigger op*//*
        if (currentDirection.equals(Direction.NORTH)) {
            if ((player.getY()) > enemy.getY()
                    && enemy.getY() < ((player.getY()) + 32)
                    && ((player.getX()) - 10) < enemy.getX()
                    && enemy.getX() < ((player.getX()) + 10)) {
                System.out.println("enemy hit. From South." + enemyLife);
                enemyLife -= playerDMG;

            }
        }
        /*Når man står på højre side og kigger mod venstre slår ham. *//*
        if (currentDirection.equals(Direction.WEST)) {
            if ((player.getX()) > enemy.getX()
                    && enemy.getX() < ((player.getX()) + 32)
                    && ((player.getY()) - 10) < enemy.getY()
                    && enemy.getY() < ((player.getY()) + 10)) {
                System.out.println("enemy hit. From east." + enemyLife);
                enemyLife -= playerDMG;

            }
        }
        /*Når man står på venstre side og slår mod højre*//*
        if (currentDirection.equals(Direction.EAST)) {
            if ((player.getX()) < enemy.getX()
                    && enemy.getX() < ((player.getX()) + 32)
                    && ((player.getY()) - 10) < enemy.getY()
                    && enemy.getY() < ((player.getY()) + 10)) {
                System.out.println("enemy hit. From west?." + enemyLife);
                enemyLife -= playerDMG;

            }
        }



    }*/

/*
    @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_END)
    public void hitEnd() {
        Main.player.setViewFromTexture("pixil-layer-Background-Standart.png");
        player.setScaleX(0.75);
        player.setScaleY(0.75);
        getAudioPlayer().playSound("aaeffect.mp3");
    }*/
}
