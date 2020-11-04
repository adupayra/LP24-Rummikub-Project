package lp24Project.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import lp24Project.CircleLinkedList;
import lp24Project.Node;
import lp24Project.model.PlayerClass;

public class GameScene extends JPanel{
	//Side panels, corresponding to the player's racks
	private List<Rack> playerRacks;	
	private BottomRack bottomRack;
	
	public static final Color GAMECOLOR = new Color(133, 6, 6);
	public static final Color RACKCOLOR = new Color(9, 82, 40);
	
	public BottomRack getBottomRack()
	{
		return bottomRack;
	}
	
//////////////////////////////INITIALIZATION//////////////////////////////
	
//We need the amount of player in order to construct the list of Racks
	public GameScene(Board board)
	{
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);
		//initGameScene(players);
	}

	
	public void reinitGameScene(CircleLinkedList players)
	{
		for(Rack rack : playerRacks)
			rack.reinit();
		updatePlayers(players);
	}
	
	
	public void initRacks(CircleLinkedList players)
	{
		playerRacks = new ArrayList<Rack>();
		PlayerClass player1 = players.getHead().getValue();
		PlayerClass player2 = players.getHead().getNext().getValue();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0,0);
		GridLayout gridLayout = new GridLayout(20, 1);
		
		bottomRack = new BottomRack(player1.getTiles(), player1.getName());
		bottomRack.setLayout(flowLayout);
		this.add(bottomRack, BorderLayout.SOUTH);
		playerRacks.add(bottomRack);
		
		//Add an amount of Racks based on the number of players
		switch(players.getSize())
		{
		
		case 2 : Rack topRack = new Rack(player2.getTiles(), player2.getName());
				topRack.setLayout(flowLayout);
				this.add(topRack, BorderLayout.NORTH);
				playerRacks.add(topRack);
				
			break;
			
		default : Rack rightRack = new Rack(player2.getTiles(), player2.getName());
				rightRack.setLayout(gridLayout);
				this.add(rightRack, BorderLayout.EAST);
				playerRacks.add(rightRack);
				
				PlayerClass player3 = players.getHead().getNext().getNext().getValue();
				Rack topRackBis = new Rack(player3.getTiles(), player3.getName());
				topRackBis.setLayout(flowLayout);
				this.add(topRackBis, BorderLayout.NORTH);
				playerRacks.add(topRackBis);
				
		}
		
		if(players.getSize() == 4)
		{
			PlayerClass player4 = players.getTail().getValue();
			Rack leftRack = new Rack(player4.getTiles(), player4.getName());
			leftRack.setLayout(gridLayout);
			this.add(leftRack, BorderLayout.WEST);
			playerRacks.add(leftRack);
		}
		else if(players.getSize() == 3)
		{
			//add an empty panel for symmetry
			addNewFillPanel();
		}
		
	}
	
	private void addNewFillPanel()
	{
		JPanel fillPanel = new JPanel(new CardLayout());
		add(fillPanel, BorderLayout.WEST);
		JButton fillButton = new JButton();
		fillButton.setBackground(RACKCOLOR);
		fillButton.setPreferredSize(Rack.BUTTONDIMENSIONS);
		fillButton.setEnabled(false);
		fillPanel.add(fillButton);
	}
	
////////////////////////////////////END OF INITIALIZATION//////////////////////////
	
	//Update the rack list clockwise (current player goes from bottom rack to left rack/top rack depending on the number of players)
	public void updatePlayers(CircleLinkedList players)
	{
		Node temp = players.getHead();
		for(Rack rack : playerRacks)
		{
			rack.setNewPlayer(temp.getValue().getTiles(), temp.getValue().getName());
			temp = temp.getNext();
		}
		
	}
	
	//refresh UI manually in case of graphics problems
	public void repaintRacks()
	{
		for(Rack rack : playerRacks)
		{
			rack.repaint();
		}
	}
}
