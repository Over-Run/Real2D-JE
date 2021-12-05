package org.overrun.real2d.client;

import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Main {
    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        config.width = 854;
        config.height = 480;
        config.title = "Real2D " + Real2D.VERSION;
        new GameApp(Real2D.instance = new Real2D(),
            config);
    }
}
