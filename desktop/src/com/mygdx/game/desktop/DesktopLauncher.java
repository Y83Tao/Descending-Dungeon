package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MainFrame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.resizable = false;
		config.width = 1280;
		config.height = 768;
		config.addIcon("Hud/icons/attackDamage.png", Files.FileType.Internal);
		config.addIcon("Hud/icons/enduranceIcon.png", Files.FileType.Internal);
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;





		new LwjglApplication(new MainFrame(), config);

	}
}
