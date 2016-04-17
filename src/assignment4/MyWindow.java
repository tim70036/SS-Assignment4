package assignment4;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyWindow extends JFrame {
	// Attribute
	private int width;
	private int height;

	// Constructor
	MyWindow(int w,int h)
	{
		width = w;
		height = h;

		// Basic setup
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);

		// Add GameStage
		GameStage stage = new GameStage(700,600,new Color(135,206,250));
		this.add(stage);
		stage.setBounds(300, 0, 700, 600);

		// Add Typing
		Typing type = new Typing(300,600,new Color(224,255,255), stage);
		this.add(type);
		type.setBounds(0, 0, 300, 600);


		while(true)
		{
			// Init, clear the data played before
			stage.init();
			type.init();
			// Control of Game thread
			Thread typing = new Thread(type);
			Thread painting = new Thread(stage);
			typing.start();
			painting.start();

			// Wait game to stop
			try {
				typing.join();
				painting.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Replay?
			int replay = JOptionPane.showConfirmDialog(this,"Game Over ! Do you want to replay ? ","Game Over",
	                JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
			if(replay == 1)	break;
		}		
	}
}
