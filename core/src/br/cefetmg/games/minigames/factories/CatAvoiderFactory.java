package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CatAvoider;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

public class CatAvoiderFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        return new CatAvoider(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("avoider/backgroundTexture.png", Texture.class);
                put("avoider/grey.png", Texture.class);
                put("avoider/catNinja.png", Texture.class);
                put("avoider/wool.png", Texture.class);
                put("avoider/cat-moving-upL.png", Texture.class);
                put("avoider/cat-moving-upR.png", Texture.class);
                put("avoider/cat-moving-downL.png", Texture.class);
                put("avoider/cat-moving-downR.png", Texture.class);
                put("avoider/cat-moving-left.png", Texture.class);
                put("avoider/cat-moving-right.png", Texture.class);
            }
        };
    }
}
