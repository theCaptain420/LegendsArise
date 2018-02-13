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


}
