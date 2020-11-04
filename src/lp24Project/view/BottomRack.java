package lp24Project.view;

import lp24Project.model.Tile;
import lp24Project.model.TileList;

//this class isn't really necesssary, all these methods could be in Rack, however it makes more sense to distinguish the bottomRack from the other racks as a "special type of rack"
public class BottomRack extends Rack {
	
	public BottomRack(TileList buttonsToAttach, String playerName) {
		super(buttonsToAttach, playerName);
	}

	//used to add an extra tile to the rack within the turn of the current player
	public void addNewTile(Tile tileToAdd, int index)
	{
		add(tileToAdd, index);
		refreshTileList();
		tileToAdd.setEnabled(true);
		tileToAdd.setVisible(true);
		tileToAdd.setPreferredSize(BUTTONDIMENSIONS);
		updateTilesUI();
	}

	public void switchButtonsWithBoard(Tile focusedTile, Tile tileSource, Slot focusedTileParent)
	{
		//we get the index in order to place the tile from the board at the right place
		int indexOfRackTile = buttonsToAttach.indexOf(tileSource) + 1;
		addNewTile(focusedTile, indexOfRackTile);
		focusedTileParent.add(tileSource, 0);
	}
	
	public void switchButtons(Tile focusedTile, Tile tileSource) {
		int focusedIndex = buttonsToAttach.indexOf(focusedTile);
		int sourceIndex = buttonsToAttach.indexOf(tileSource);
		remove(focusedTile);
		addNewTile(focusedTile, sourceIndex + 1);
		remove(tileSource);
		addNewTile(tileSource, focusedIndex + 1);
	}
	
	
	
	//update the UI after the user clicks on the sort Rack button
	public void updateAfterSort()
	{
		buttonsToAttach.sort();
		this.removeAll();
		add(playerLabel);
		for(int i = 0; i < buttonsToAttach.size(); i++)
		{
			add(buttonsToAttach.get(i));
		}
		refreshTileList();
		updateTilesUI();
		
	}
	
	

}
