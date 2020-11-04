package lp24Project.controller;

import lp24Project.model.Model;
import lp24Project.model.Tile;
import lp24Project.view.BottomRack;
import lp24Project.view.Slot;

//Action the user will be able to cancel
public class RackClicked implements ICommand {
	Model modelInstance = Model.getInstance();
	
	Tile previouslyFocusedTile;
	Slot previousSlot;
	
	BottomRack currentPlayerRack;
	
	public RackClicked(BottomRack currentPlayerRack, Tile focusedTile)
	{
		this.previouslyFocusedTile = focusedTile;
		this.currentPlayerRack = currentPlayerRack;
		previousSlot = (Slot)previouslyFocusedTile.getParent();
		execute();
	}
	
	@Override
	public void execute() {
		
		modelInstance.removeTilePlayedDuringTurn(previouslyFocusedTile);
		currentPlayerRack.addNewTile(previouslyFocusedTile, currentPlayerRack.getComponentCount());
		currentPlayerRack.refreshTileList();
		modelInstance.setFocusedTile(null);
		if(modelInstance.getTilesPlayedDuringTurn().size() == 0) //if, when returning a tile back to his rack, the player has not played any tiles on the board, then it means that the pouch
																//is enabled again
			modelInstance.setPouchDrawable(true);
		
	}

	@Override
	public void undo() {
		modelInstance.addTilePlayedDuringTurn(previouslyFocusedTile);
		previousSlot.add(previouslyFocusedTile, 0);
		modelInstance.setPouchDrawable(false);
		currentPlayerRack.refreshTileList();
		
	}
}
