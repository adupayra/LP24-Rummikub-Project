package lp24Project.controller;

import java.awt.Container;

import lp24Project.model.Model;
import lp24Project.model.Tile;
import lp24Project.view.BottomRack;
import lp24Project.view.Rack;
import lp24Project.view.Slot;

//One of the action the user will be able to cancel
public class PlaceTileOnBoard implements ICommand{
	//caching
	Model modelInstance = Model.getInstance();
	
	//variables we need in order to reverse the action
	Tile previousTile;
	Tile tileToPlace;
	Container previousContainer;
	Slot destinationSlot;
	
	BottomRack currentPlayerRack;
	
	public PlaceTileOnBoard(Tile tileToPlace, Slot destinationSlot, BottomRack currentPlayerRack)
	{
		this.tileToPlace = tileToPlace;
		this.previousTile = tileToPlace;
		previousContainer = tileToPlace.getParent();
		this.destinationSlot = destinationSlot;
		this.currentPlayerRack = currentPlayerRack;
		execute();
	}
	
	@Override
	public void execute() {
		
		destinationSlot.add(previousTile,0);
		if(previousContainer instanceof Rack)
		{
			modelInstance.addTilePlayedDuringTurn(previousTile);
			currentPlayerRack.refreshTileList();
		}
		modelInstance.setFocusedTile(null);
	}

	@Override
	public void undo() {
		if(previousContainer instanceof Rack)
		{
			//this action is actually the same as putting a tile back in the rack, so I decided to instantiate a new RackClicked action even though we won't use its undo method
			RackClicked rackClicked = new RackClicked(currentPlayerRack, tileToPlace);
		}
		else
		{
			if(previousContainer.getComponentCount() != 2) //Some feature buttons allow the tiles to move without adding an action to the actionLists. Therefore it can happen that, when cancelling
															//an action, the slot planned for the tile's reception is already filled by another tile. In that case nothing happens when cancelling.
			{
				Slot previousSlot = (Slot)previousContainer;
				previousSlot.add(previousTile, 0);
			}
			
		}
	}

	
}
