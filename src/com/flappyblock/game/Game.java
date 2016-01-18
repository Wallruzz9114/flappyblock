package com.flappyblock.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Game implements ActionListener, MouseListener {
	public static Game game;
	public final int FRAME_WIDTH = 650, FRAME_HEIGHT = 650;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> pipes;
	public Random randomObject;
	public int ticks, motion, score;
	public boolean gameStarted, gameOver = false;
	
	public Game() {
		JFrame mainFrame = new JFrame();
		Timer timer = new Timer(20, this);
		renderer = new Renderer();
		randomObject = new Random();
		
		mainFrame.add(renderer);
		mainFrame.setTitle("Flappy Block");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		mainFrame.addMouseListener(this);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		bird = new Rectangle((FRAME_WIDTH / 2) - 10, (FRAME_HEIGHT / 2) - 10, 20, 20);
		pipes = new ArrayList<Rectangle>();
		
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		
		timer.start();
	}
	
	public void addPipe(boolean oldPipe) {
		int spaceBetweenPipes = 300;
		int pipeWidth = 100;
		int pipeHeight = 50 + randomObject.nextInt(300);
		
		if (oldPipe) {
			pipes.add(new Rectangle(FRAME_WIDTH + pipeWidth + pipes.size() * 300, FRAME_HEIGHT - pipeHeight - 110, pipeWidth, pipeHeight));
			pipes.add(new Rectangle(FRAME_WIDTH + pipeWidth + (pipes.size() - 1) * 300, 0, pipeWidth, FRAME_HEIGHT - pipeHeight - spaceBetweenPipes));
		} else {
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600, FRAME_HEIGHT - pipeHeight - 110, pipeWidth, pipeHeight));
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, pipeWidth, FRAME_HEIGHT - pipeHeight - spaceBetweenPipes));
		}
	}
	
	public void paintPipe(Graphics g, Rectangle pipe) {
		g.setColor(Color.GRAY);
		g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
	}
	
	public void jump() {
		if (gameOver) {			
			bird = new Rectangle((FRAME_WIDTH / 2) - 10, (FRAME_HEIGHT / 2) - 10, 20, 20);
			pipes.clear();
			motion = 0;
			score = 0;
			
			addPipe(true);
			addPipe(true);
			addPipe(true);
			addPipe(true);
			addPipe(true);
			
			gameOver = false;
		}
		
		if (!gameStarted) {
			gameStarted = true;
		} else if (!gameOver) {
			if (motion > 0) {
				motion = 0;
			}
			motion -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		
		if (gameStarted) {
			for (int i = 0; i < pipes.size(); i++) {
				Rectangle selectedPipe = pipes.get(i);
				selectedPipe.x -= 10;
			}
			
			if (ticks % 2 == 0 && motion < 15) {
				motion += 2;
			}
			
			for (int i = 0; i < pipes.size(); i++) {
				Rectangle selectedPipe = pipes.get(i);
				
				if (selectedPipe.x + selectedPipe.width < 0) {
					pipes.remove(selectedPipe);
					
					if (selectedPipe.y == 0) {
						addPipe(false);
					}
				}
			}
			
			bird.y += motion;
			
			for (Rectangle pipe : pipes) {
				if (pipe.y == 0
						&& bird.x + (bird.width / 2) > pipe.x + (pipe.width / 2) - 10
						&& bird.x + (bird.width / 2) < pipe.x + (pipe.width / 2) + 10) {
					score = score + 1;
				}				
				
				if (pipe.intersects(bird)) {
					gameOver = true;
					bird.x = pipe.x - bird.width;
				}
			}
			
			if (bird.y > FRAME_HEIGHT - 110 || bird.y < 0) {
				gameOver = true;
			}
			
			if (gameOver) {
				bird.y = FRAME_HEIGHT - 110 - bird.height;
			}
		}
		
		renderer.repaint();
	}
	
	public void repaint(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		
		g.setColor(Color.ORANGE);
		g.fillRect(0, FRAME_HEIGHT - 110, FRAME_WIDTH, 110);
		
		g.setColor(Color.GREEN);
		g.fillRect(0, FRAME_HEIGHT - 110, FRAME_WIDTH, 20);
		
		g.setColor(Color.RED);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for (Rectangle pipe : pipes) {
			paintPipe(g, pipe);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Consolas", 1, 50));
		
		if (!gameStarted) {
			g.drawString("Click to start!", 120, (FRAME_HEIGHT / 2) - 50);
		}
		
		if (gameOver) {
			int newline = g.getFont().getSize() + 5 ;
	        String[] strings = {"Game Over! Score: " + String.valueOf(score), "Click to play again!"};
	        int y = (FRAME_HEIGHT / 2) - 50;
	        
	        for (int i = 0; i < strings.length; i++) {
	            g.drawString(strings[i], 60, y += newline );
	        }
		}
		
		if (!gameOver && gameStarted) {
			g.drawString(String.valueOf(score), (FRAME_WIDTH / 2) - 25, 160);
		}	
	}
	
	public static void main(String[] args) {
		game = new Game();
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		jump();
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	

	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
