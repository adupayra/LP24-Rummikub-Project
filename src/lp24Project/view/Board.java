package lp24Project.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

//This class corresponds to the center panel where the game and interactions will mostly occur
public class Board extends JPanel {
	private GameSlot[] gameSlots;
	private JLabel message = new JLabel();
	
	
	public Board(GameSlot[] gameSlots)
	{
		this.setBackground(GameScene.GAMECOLOR);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 50, 50);
		this.setLayout(flowLayout);
		this.gameSlots = gameSlots;
		add(message);
		message.setForeground(Color.white);
		message.setFont(new Font("TimesRoman", Font.BOLD, 25));
		message.setVisible(false);
	}
	
	public GameSlot[] getGameSlots()
	{
		return gameSlots;
	}
	
	public void reinitGameSlots()
	{
		for(GameSlot gameSlot : gameSlots)
		{
			gameSlot.reinitGameSlot();
		}
	}
	
	//returns all the free gameSlots on the board (gameSlots which doesn't contain any tile)
	public List<GameSlot> getFreeGameSlots()
	{
		List<GameSlot> freeGameSlots = new ArrayList<GameSlot>();
		for(GameSlot gameSlot : gameSlots)
		{
			if(gameSlot.isFree())
			{
				freeGameSlots.add(gameSlot);
			}
		}
		return freeGameSlots;
	}
	
	//returns every gameSlot containing at least one tile
	public List<GameSlot> getOccupiedGameSlots()
	{
		List<GameSlot> occupiedGameSlots = new ArrayList<GameSlot>();
		for(GameSlot gameSlot : gameSlots)
		{
			if(!gameSlot.isFree())
			{
				occupiedGameSlots.add(gameSlot);
			}
		}
		return occupiedGameSlots;
	}
	
	//returns every disabled GameSlots (whose slots are all disabled)
	public List<GameSlot> getDisabledGameSlots()
	{
		List<GameSlot> gameSlotList = new ArrayList<GameSlot>();
		for(GameSlot gameSlot : gameSlots)
		{
			if(!gameSlot.isEnabled())
			{
				gameSlotList.add(gameSlot);
			}
		}
		return gameSlotList;
	}
	
	//returns every gameSlots which is occupied yet enabled
	public List<GameSlot> getOccupiedAndEnabledGameSlots()
	{
		List<GameSlot> gameSlotList = new ArrayList<GameSlot>();
		for(GameSlot gameSlot : gameSlots)
		{
			if(gameSlot.isEnabled() && !gameSlot.isFree())
			{
				gameSlotList.add(gameSlot);
			}
		}
		return gameSlotList;
	}
	
	//Start the thread which will display a message for a fixed amount of time on the screen
	public void displayOutMessage(boolean bool)
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run()
			{
				displayMessage(bool);	
			}
		});
		t.start();
	}
	
	//display the message for 3 seconds
	private void displayMessage(boolean bool) {
		if(bool == true)
			message.setText("Congratulations ! you can now play with other player's tiles !");
		else
			message.setText("You don't have enough points, remove all your tiles or add some others");
		message.setVisible(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		message.setVisible(false);
	}

}
