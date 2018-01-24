import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.settings.GameSettings;

public class Main extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Legends Arise");
    }

    public static void main(String args[]){
        System.out.println("hello world!");
        launch(args);
    }
}
