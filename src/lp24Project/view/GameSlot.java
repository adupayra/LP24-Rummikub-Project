package lp24Project.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lp24Project.model.Tile;
import lp24Project.model.TileList;

public class GameSlot extends JPanel {
	private JLabel warningLabel = new JLabel("WARNING");
	private Slot[] slots = new Slot[5];
	private TileList tilesList;
	
	
	public TileList getTilesList()
	{
		
		if(tilesList == null)
			check();
		return tilesList;
	}
	
	public Slot[] getAllButtons()
	{
		return slots;
	}
	
	public void setSlot(Slot slot, int index)
	{
		slots[index] = slot;
	}
	
	
	public GameSlot()
	{
		warningLabel.setForeground(Color.WHITE);
		add(warningLabel);
		warningLabel.setVisible(false);
		this.setBackground(GameScene.GAMECOLOR);
		this.setLayout(new FlowLayout(0,0,0));
	}
	
	public void reinitGameSlot()
	{
		for(Slot slot : slots)
		{
			slot.setEnabled(true);
			if(slot.getComponentCount() == 2)
			{
				slot.remove(0);
			}
		}
	}
	
	public List<JButton> getFreeSlot()
	{
		List<JButton> freeButtons = new ArrayList<JButton>();
		for(Slot slot : slots)
		{
			if(slot.getComponentCount() == 1)
			{
				freeButtons.add((JButton)slot.getComponent(0));
			}
		}
		return freeButtons;
	}
	
	
	public boolean isFree()
	{
		for(Slot slot : slots)
		{
			//if slot has more than 1 children, then it means it contains a tile, therefore the slot is not free (and neither is the game slot)
			if(slot.getComponentCount() != 1)
			{
				return false;
			}
		}
		return true;
	}
	
	//for these two functions, this might not be very smart because the first usage of set/isEnabled is totally wiped out, however I'm pretty sure that calling the super method at the beginning
	//wouldn't change anything, I just didn't know how the setEnabled function worked on panels and didn't want it to ruin what I intended to do
	
	@Override
	public void setEnabled(boolean bool)
	{
		for(Slot slot : slots)
		{
			slot.setEnabled(bool);
		}
	}
	
	@Override
	public boolean isEnabled()
	{
		JButton button = (JButton)slots[0].getComponent(0);
		return button.isEnabled();
	}
	
	//checks if the gameSlot contains a valid set
	public boolean check()
	{
		createTileListAndRemoveSpaces();
		if(tilesList.size() == 0) //empty gameSlot
			return false;
			
		updateSlots(); //sort the tiles in the slots, then place them back while removing the empty spaces between the tiles
		if(!tilesList.checkCombinations()) //checks if the contained set is valid or not
		{
			setWarning(); //display a warning message
			return false;
		}
		return true;
		
		
	}
	
	public void createTileListAndRemoveSpaces()
	{
		//create a list of tiles based on what contains the gameSlot
		tilesList = new TileList();
		for(Slot slot : slots)
		{
			if(slot.hasTile())
			{
				tilesList.add((Tile)slot.getComponent(0));
			}
		}
		updateSlots();
	}
	

	private void updateSlots() {
		tilesList.sort();
		for(int i = 0; i < tilesList.size(); i++)
		{
			slots[i].add(tilesList.get(i), 0);
			slots[i].updateSlot();
		}
	}
	
	private void setWarning()
	{
		for(Slot slot : slots)
		{
			slot.setWarningOnTile();
		}
	}
	
	
	public void setWarningLabelVisible(boolean bool)
	{
		warningLabel.setVisible(bool);
	}
}
