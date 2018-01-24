package captain.the;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.shape.Rectangle;
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

public class Main extends GameApplication {

    private Entity player;


    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
        @Override
            protected void onAction(){
            player.translateX(1);//går 1 pixel til højre ->
        }

        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction(){
                player.translateX(-1);//Går 1 pixel til venstre
            }

        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player.translateY(-1);//Går 1 pixel op
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.translateY(1);//Går 1 pixel ned
            }
        }, KeyCode.S);
    }


    @Override
    protected void initGame() {
        player = Entities.builder()
                .at(300,300)//player pos
                .viewFromNode(new Rectangle(25,25, Color.BLUE))//størrelse of farve
                .buildAndAttach(getGameWorld());

    }

    @Override
    protected void initPhysics(){}

    @Override
    protected void initUI(){}

    //@Override
    //protected void onUpdate(){}
    //@Override
    //protected void initAssets() throws Exception{}

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(700);
        settings.setTitle("Legends Arise");
        //settings.setMenuEnabled(true); //Viser menuen.
        settings.setIntroEnabled(false); //Fjerner introen
    }

    public static void main(String args[]){
        System.out.println("hello world!");
        launch(args);
    }
}
