package lp24Project.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lp24Project.model.Tile;
import lp24Project.model.TileList;

public class Rack extends JPanel {
	//Tiles to attach to the Rack
	protected TileList buttonsToAttach;
	
	protected JLabel playerLabel = new JLabel();
	public static final Dimension BUTTONDIMENSIONS = new Dimension(50,50);
	
	public TileList getButtonsToAttach()
	{
		return buttonsToAttach;
	}
	
	
	public Rack(TileList buttonsToAttach, String playerName)
	{
		this.setBackground(GameScene.RACKCOLOR);
		add(playerLabel);
		setNewPlayer(buttonsToAttach, playerName);
	}
	
	public void setNewPlayer(TileList buttonsToAttach, String playerName)
	{
		//replace the previous tiles to attach by the new ones
		for(int i = 0; i < buttonsToAttach.size(); i++)
		{
			add(buttonsToAttach.get(i));
			buttonsToAttach.get(i).setVisible(true);
			buttonsToAttach.get(i).setBackground(Tile.TILECOLOR);
			buttonsToAttach.get(i).setPreferredSize(BUTTONDIMENSIONS);
		}
		this.buttonsToAttach = buttonsToAttach;
		playerLabel.setText(playerName);
		playerLabel.setForeground(Color.white);
	}
	
	public void refreshTileList()
	{
		//update the buttons to attach list after any change in the player's tiles
		Component[] tiles = this.getComponents();
		buttonsToAttach.removeAll(buttonsToAttach);
		for(int i = 1; i < tiles.length; i ++)
		{
			buttonsToAttach.add(i - 1, (Tile)tiles[i]);
		}
		updateTilesUI();
	}
	
	//used to avoid some graphic bugs
	protected void updateTilesUI()
	{
		setVisible(false);
		setVisible(true);
	}
	
	//Used to remove all the tiles attached to the rack at the end of a round
	public void reinit()
	{
		Component[] components = this.getComponents();
		for(int i = 1; i < components.length; i++)
			remove(components[i]);
	}


}
