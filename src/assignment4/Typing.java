package assignment4;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Typing extends JPanel implements Runnable{
	
	// Atrribute
	private int width;
	private int height;
	private Color color;
	private GameStage gs;
	private boolean stop;
	
	// Component
	private JTextField field;
	
	// Picture and position
	private Image picture1;
	private Image picture2;
	private int picture1X;
	private int picture1Y;
	private int picture2X;
	private int picture2Y;
	
	// Data structure for picture
	private ArrayList<Word> knownWord;
	private ArrayList<Integer>	usedIndex1;
	private int knownWordIndex;
	private ArrayList<String>	unknownWordFile;
	private ArrayList<Integer>	usedIndex2;
	private int unknownWordFileIndex;
	private Random r;
	
	// Output data structure
	private String userInput1;
	private String userInput2;
	private ArrayList<Word> toOutput;
	
	// Setter Getter
	public void setWidth(int w){ width = w; }
	public void setHeight(int h){height = h; }
	public void setColor(Color c){color = c;}
	public void setGameStage(GameStage g){gs = g;}
	public void setStop(boolean b){stop = b;}
	
	public void setPicture1X(int x){picture1X = x;}
	public void setPicture1Y(int y){picture1Y = y;}
	public void setPicture2X(int x){picture2X = x;}
	public void setPicture2Y(int y){picture2Y = y;}
	
	public int getWidth(){	return width; }
	public int getHeight(){	return height;}
	public Color getColor(){return color;}
	public GameStage getGameStage(){return gs;}
	public boolean getStop(){return stop;}
	
	public int getPicture1X(){	return picture1X;}
	public int getPicture1Y(){	return picture1Y;}
	public int getPicture2X(){	return picture2X;}
	public int getPicture2Y(){	return picture2Y;}
	
	// Constructor
	Typing(int w,int h,Color c,GameStage g)
	{
		// Basic setup
		setWidth(w);
		setHeight(h);
		setColor(c);
		setGameStage(g);
		
		// Initialize component
		field = new JTextField(20);
		field.setBounds(0, 535, 300, 30);
		field.addActionListener(new ActionListener(){// When user enter "enter"
			public void actionPerformed(ActionEvent e) {
				// Get userInput by Scanner through field
				Scanner scanner = new Scanner(field.getText());
				// User must input
				if(scanner.hasNext())	userInput1 = scanner.next();
				if(scanner.hasNext())	userInput2 = scanner.next();
				
				// Reset TextField
				field.setText("");
				
				scanner.close();
				
				// Check whether UserInput match known word 
				if(knownWord.get(knownWordIndex).getWord().equals(userInput1))
				{
					if(userInput2 != null)// User must Input second word
						if(!userInput2.isEmpty())// And not empty
							correct();
				}
				else
					changeBackground(false);
			}
		});
		
		// Add Component
		this.add(field);
		
		// Deal with data for word
		r = new Random();
		knownWord = new ArrayList<Word>();
		usedIndex1 = new ArrayList<Integer>();
		unknownWordFile = new ArrayList<String>();
		usedIndex2 = new ArrayList<Integer>();
		toOutput =  new ArrayList<Word>();
		try {
			// Read the known word into ArrayList
			Scanner scanner = new Scanner(new File("known_words.txt"));
			while(scanner.hasNext())
			{
				Word tmp = new Word(scanner.next(),scanner.next());
				knownWord.add(tmp);
			}
			
			// Read the unknown word's file name into ArrayList
			scanner = new Scanner(new File("unknown_words.txt"));
			while(scanner.hasNext())
				unknownWordFile.add(scanner.next());
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		init();
	}
	
	// init the game
	public void init()
	{
		setStop(false);
		
		// Reset
		usedIndex1.clear();
		usedIndex2.clear();
		toOutput.clear();
		
		// Read first Image for picture1 and picture2
		changePicture1();
		changePicture2();
		
		repaint();	
	}
	
	// Change picture1 ,Reset position
	public void changePicture1()
	{
		// Find a non use picture
		while(usedIndex1.contains(knownWordIndex))
			knownWordIndex = r.nextInt(knownWord.size());
		
		// Read Image
		try {
			picture1 = ImageIO.read(new File("img/known/" + knownWord.get(knownWordIndex).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Set position
		setPicture1Y(0);
		setPicture1X(5);
	}
	// Change picture2, Reset position
	public void changePicture2()
	{
		// Find a non use picture
		while(usedIndex2.contains(unknownWordFileIndex))
			unknownWordFileIndex = r.nextInt(unknownWordFile.size());
		
		// Read Image
		try {
			picture2 = ImageIO.read(new File("img/unknown/" + unknownWordFile.get(unknownWordFileIndex)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set position
		setPicture2Y(0);
		setPicture2X(15 + picture1.getWidth(this));// Avoid two pictures cover each other
	}
	
	// Animation for backGround color
	public void changeBackground(boolean correct)
	{
		Color a = new Color(255, 36,0);
		Color b = new Color(142,252,0);
		Color origin = new Color(224,255,255);
		
		Thread changeColor = new Thread(new Runnable(){
			public void run() {
				int cnt = 0;
				while(cnt < 4)
				{
					if(cnt%2 == 0)
					{
						if(correct)	setColor(b);
						else	setColor(a);
					}
					else
						setColor(origin);
					cnt++;
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		changeColor.start();
	}
	
	// UserInput is right
	public void correct()
	{
		// Add to used
		usedIndex1.add(new Integer(knownWordIndex));
		usedIndex2.add(new Integer(unknownWordFileIndex));
		
		// Add to toOutput
		toOutput.add(knownWord.get(knownWordIndex));
		toOutput.add(new Word(unknownWordFile.get(unknownWordFileIndex) ,  userInput2));
		
		getGameStage().addScore();
		changePicture1();
		changePicture2();
		changeBackground(true);
		
	}
	
	// Drawing method
	public void paintComponent(Graphics g)
	{
		this.setBackground(getColor());
		g.drawImage(picture1, picture1X, picture1Y, this);
		g.drawImage(picture2, picture2X, picture2Y, this);
	}
	
	public void run() {
		while(!stop)
		{
			// Animation for word
			setPicture1Y(getPicture1Y()+1);
			setPicture2Y(getPicture2Y()+1);
			
			// Check Whether reach the bottom, if so , change picture and reset Y
			if(getPicture1Y() >= 500)
				changePicture1();
			if(getPicture2Y() >= 500)
				changePicture2();
							
			
			field.setBounds(0, 535, 300, 30);
			repaint();
			
			// Check Win or not
			if(gs.getStop() == true)
				setStop(true);
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// GameOver output the file
		try {
			PrintWriter w = new PrintWriter("output.txt");
			for(Word cur : toOutput)
				w.println(cur.getFile() + " " + cur.getWord());
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
