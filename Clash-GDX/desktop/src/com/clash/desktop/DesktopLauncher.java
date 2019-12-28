package com.clash.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.clash.Clash;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Clash.TITLE + " v" + Clash.VERSION;
		config.vSyncEnabled = true;
		config.width = 1280;
		config.height = 720;
		/*config.fullscreen = true;
		config.width = 1920;
		config.height = 1080;*/
		new LwjglApplication(new Clash(), config);
	}
}
