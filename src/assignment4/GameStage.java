package assignment4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStage extends JPanel implements Runnable {
	
	// Attribute
	private int currentScore;
	private int winScore;
	private boolean stop;
	
	private int width;
	private int height;
	private Color color;
	
	private JLabel label;
	private Duck duck;
	private BG bg;
	private Ball ball;
	
	// Getter Setter
	public void setCurrentScore(int s){	currentScore = s; }
	public void setWinScore(int s){	winScore = s; }
	public void setWidth(int w){ width = w; }
	public void setHeight(int h){height = h; }
	public void setColor(Color c){color = c;}
	public void setStop(boolean b){stop = b;}
	
	public int	getCurrentScore(){	return currentScore;}
	public int 	getWinScore(){	return winScore;}
	public int getWidth(){	return width; }
	public int getHeight(){	return height;}
	public Color getColor(){return color;}
	public boolean getStop(){return stop;}
	
	// Constructor
	public GameStage(int w, int h, Color c)
	{
		// Basic setup
		setCurrentScore(0);
		setWinScore(1);
		setWidth(w);
		setHeight(h);
		setColor(c);
		this.setSize(getWidth(), getHeight());
		
		init();
	}
	
	// init the game
	public void init()
	{
		setCurrentScore(0);
		setWinScore(1);
		setStop(false);
		// Initialize component
		duck = new Duck(0,300,300);
		bg = new BG(0, 0);
		ball = new Ball(700, 300);
		label = new JLabel("Score : " + getCurrentScore());
		label.setFont(new Font(label.getName(), Font.PLAIN, 25));
		label.setBounds(310, 0, 200, 40);
		
		// Add component
		this.add(label);
		
		repaint();
	}
	
	// Replay
	public void replay()
	{
		
	}
	
	// Drawing method
	public void paintComponent(Graphics g)
	{
		this.setBackground(getColor());
		g.drawImage(bg.getImage(), bg.getBgX(), bg.getBgY(),this);
		g.drawImage(duck.getImage(), duck.getDuckCurrentX(), duck.getDuckCurrentY(), this);
		g.drawImage(ball.getImage(), ball.getBallCurrentX(), ball.getBallCurrentY(),this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop)
		{
			// Animation of Duck
			int duckY = duck.getDuckCurrentY();
			if(duck.getDuckDirection() == 1)// Up
			{
				if(duckY > duck.getDuckAnchorY() - 10)
					duck.setDuckCurrentY(duckY - 1);
				else
					duck.setDuckDirection(0);
			}
			else// Down
			{
				if(duckY < duck.getDuckAnchorY() + 10)
					duck.setDuckCurrentY(duckY + 1);
				else
					duck.setDuckDirection(1);
			}
			
			repaint();
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addScore()
	{
		// Add score
		setCurrentScore(getCurrentScore() + 1);
		// Update JLabel
		label.setText("Score : " + getCurrentScore());
	}
}
