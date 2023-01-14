package com.luismichu.chess;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.luismichu.chess.MyChess;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(true);
		config.setTitle("Chess");
		config.setWindowedMode(480, 480);
		Graphics.Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();

		int width = 800;
		int height = 800;

		Graphics.Monitor mon = monitors[1];
		Graphics.DisplayMode mode = Lwjgl3ApplicationConfiguration.getDisplayMode(mon);
		int posX = mon.virtualX + mode.width/2 - width/2;
		int posY = mon.virtualY + mode.height/2 - height/2;

		config.setWindowedMode(width, height);
		config.setWindowPosition(posX, posY - 200);

		new Lwjgl3Application(new MyChess(), config);
	}
}
