package lp24Project.controller;

import lp24Project.model.Tile;
import lp24Project.view.Slot;

//Action the user will be able to cancel
public class SwitchTilesBoardToBoard implements ICommand{

	Tile focusedTile;
	Tile sourceTile;
	Slot focusedTileParent;
	
	public SwitchTilesBoardToBoard(Tile focusedTile, Tile sourceTile, Slot focusedTileParent)
	{
		this.focusedTile = focusedTile;
		this.sourceTile = sourceTile;
		this.focusedTileParent = focusedTileParent;
		execute();
		
	}
	@Override
	public void execute() {
		focusedTileParent.switchTiles(sourceTile);
	}

	@Override
	public void undo() {
		Slot tileSourceParent = (Slot)sourceTile.getParent();
		tileSourceParent.switchTiles(focusedTile);
		tileSourceParent.updateSlot();
		
	}

}
