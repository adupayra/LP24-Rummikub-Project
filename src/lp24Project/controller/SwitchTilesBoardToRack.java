package lp24Project.controller;

import lp24Project.model.Model;
import lp24Project.model.Tile;
import lp24Project.view.BottomRack;
import lp24Project.view.Slot;

//Action that the user will be able to cancel
public class SwitchTilesBoardToRack implements ICommand {
	//caching of the modelinstance
	Model modelInstance = Model.getInstance();
	
	//variables we need for the undo method
	Tile focusedTile;
	Tile tileSource;
	Slot focusedTileParent;
	
	BottomRack currentPlayerRack;
	
	public SwitchTilesBoardToRack(Tile focusedTile, Tile tileSource, Slot focusedTileParent, BottomRack currentPlayerRack)
	{
		this.focusedTile = focusedTile;
		this.tileSource = tileSource;
		this.focusedTileParent = focusedTileParent;
		this.currentPlayerRack = currentPlayerRack;
		execute();
	}
	
	@Override
	public void execute() {
		modelInstance.removeTilePlayedDuringTurn(focusedTile);
		modelInstance.addTilePlayedDuringTurn(tileSource);
		
		currentPlayerRack.switchButtonsWithBoard(focusedTile, tileSource, focusedTileParent);
	}

	@Override
	public void undo() {
		modelInstance.addTilePlayedDuringTurn(focusedTile);
		modelInstance.removeTilePlayedDuringTurn(tileSource);
		
		currentPlayerRack.switchButtonsWithBoard(tileSource,  focusedTile, focusedTileParent);
	}

}
