package com.flappyblock.game;

import javax.swing.JPanel;
import com.flappyblock.game.Game;
import java.awt.Graphics;

public class Renderer extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Game.game.repaint(g);
	}
}
