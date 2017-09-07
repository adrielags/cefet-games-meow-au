package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class CucumberMadness extends MiniGame {

    private Texture catTexture;
    private Cat cat;
    private Texture veggieTexture;
    private Array<Veggie> veggies;
    private Array<Texture> veggieTextures;
    private Sound backgroundMusic;
    private float spawnInterval = 0.2f;

    public CucumberMadness(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        catTexture = assets.get("cucumber-madness/cat-sprite.png", Texture.class);
        veggieTextures = new Array<Texture>();
        veggies = new Array<Veggie>();
        veggieTextures.addAll(
            assets.get("cucumber-madness/carrot.png", Texture.class),
            assets.get("cucumber-madness/onion.png", Texture.class),
            assets.get("cucumber-madness/tomato.png", Texture.class),
            assets.get("cucumber-madness/potato.png", Texture.class));
        backgroundMusic = assets.get("cucumber-madness/bensound-jazzcomedy.mp3", Sound.class);

//        TextureRegion[][] frames = TextureRegion.split(catTexture,
//                Cat.FRAME_WIDTH, Cat.FRAME_HEIGHT);
        
//        cat = new Cat(frames[0][0], 0 + 200);
        cat = new Cat(catTexture, 0 + 200);
        cat.setCenter(
            viewport.getWorldWidth() / 2f,
            cat.height);
        cat.setScale(5);

        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnVeggies();
            }

        }, 0, this.spawnInterval);
        
//        backgroundMusic.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    }

    private void spawnVeggies() {
        int index = MathUtils.random(0, veggieTextures.size - 1);

        Vector2 direction;
        Vector2 position = new Vector2();
        do {
           direction = new Vector2(MathUtils.random(-1, 1) * 500, MathUtils.random(-1, 1) * 500);
        } while (direction.x == 0f && direction.y == 0f);

        Veggie veggie = new Veggie(veggieTextures.get(index), direction);

        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            position.x = MathUtils.randomBoolean()
                    ? -veggie.FRAME_WIDTH
                    : viewport.getWorldWidth();
            position.y = MathUtils.random(
                    -veggie.FRAME_HEIGHT,
                    viewport.getWorldHeight());
        } else {
            position.y = MathUtils.randomBoolean()
                    ? -veggie.FRAME_HEIGHT
                    : viewport.getWorldHeight();
            position.x = MathUtils.random(
                    -veggie.FRAME_WIDTH,
                    viewport.getWorldWidth());
        }
        
        veggie.setPosition(position.x, position.y);
        veggies.add(veggie);

    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        cat.setCenter(click.x, click.y);
    }

    @Override
    public void onUpdate(float dt) {
        for (Veggie veggie : veggies) {
            veggie.update(dt);
        }

        for (Veggie veggie : veggies) {
//            Colisão veggie x cat
            if (veggie.getBoundingRectangle()
                    .overlaps(cat.getBoundingRectangle())) {
                super.challengeFailed();
            }
        }
    }

    @Override
    public void onDrawGame() {
        this.cat.draw(batch);

        for (Veggie veggie : veggies) {
            veggie.draw(batch);
        }
  
    }

    @Override
    public String getInstructions() {
        return "run.";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends Sprite {
        private final int lives = 9;
        private final float height;

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 50;

        public Cat(Texture texture, float height) {
            super(texture);
            this.height = height;
        }
    }
    
    class Veggie extends Sprite {
        private int FRAME_WIDTH;
        private int FRAME_HEIGHT;
        private Vector2 speed;

        public Veggie(Texture texture, Vector2 speed) {
            super(texture);
            this.speed = speed;
            
//            carrot
            if (texture == veggieTextures.get(0)) {
                FRAME_WIDTH = 34;
                FRAME_HEIGHT = 55;
//                onion
            } else if (texture == veggieTextures.get(1)) {
                FRAME_WIDTH = 38;
                FRAME_HEIGHT = 54;
//                tomato
            } else if (texture == veggieTextures.get(2)) {
                FRAME_WIDTH = 40;
                FRAME_HEIGHT = 49;
//                potato
            } else if (texture == veggieTextures.get(3)) {
                FRAME_WIDTH = 28;
                FRAME_HEIGHT = 60;
            }
        }

        public void update(float dt) {
            this.setPosition(this.getX() - this.speed.x * dt,
                    this.getY() - this.speed.y * dt);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
    }
}
