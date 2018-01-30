package captain.the;

import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.OnUserAction;

public class HittingAnimation {

    @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_BEGIN)
    public void hitBegin() {
        Main.player.setViewFromTexture("pixil-layer-Background(1).png");
    }


    @OnUserAction(name = "Hit", type = ActionType.ON_ACTION_END)
    public void hitEnd() {
        Main.player.setViewFromTexture("pixil-layer-Background.png");
    }
}
