package lp24Project.controller;
import java.util.ArrayList;
import java.util.List;

import lp24Project.model.Model;
import lp24Project.model.Tile;
import lp24Project.view.Board;
import lp24Project.view.FeaturePanel;
import lp24Project.view.GameSlot;
import lp24Project.view.Slot;

//This class is the action of unlocking the board to the current player when he hits the confirm 30 points button
public class UnlockFullAccess implements ICommand{
	//caching
	Model modelInstance = Model.getInstance();
	Controller controllerInstancer = Controller.getInstance();
	
	//references to other objects that we need
	Board boardPanel;
	FeaturePanel featurePanel;
	List<Tile> tilesPlayedBeforeConfirmed = new ArrayList<Tile>();
	
	public UnlockFullAccess(Board boardPanel, FeaturePanel featurePanel)
	{
		this.boardPanel = boardPanel;
		this.featurePanel = featurePanel;
		this.tilesPlayedBeforeConfirmed.addAll(modelInstance.getTilesPlayedDuringTurn());
		execute();
	}
	
	@Override
	public void execute() {
		
		modelInstance.getCurrentPlayer().setOut(true); //set the player in the "all access" state
		boardPanel.displayOutMessage(true);
		modelInstance.removeTilesPlayedDuringTurn();
		featurePanel.getConfirmPointsButton().setEnabled(false);
		
		//make every gameSlot which were disabled enabled so that the player can now modify and play with them
		for(GameSlot gameSlot : boardPanel.getDisabledGameSlots())
		{
			gameSlot.setEnabled(true);
		}
	}

	@Override
	public void undo() {
		modelInstance.getCurrentPlayer().setOut(false); //retraining back the access of the player
		featurePanel.getConfirmPointsButton().setEnabled(true);
		
		for(Tile tile : tilesPlayedBeforeConfirmed)
		{
			modelInstance.addTilePlayedDuringTurn(tile); //searching for the tiles he played before confirming so that he can play with them again and take them back to his rack for instance
		}
		
		for(GameSlot gameSlot : boardPanel.getOccupiedGameSlots())
		{
			Slot firstSlot = (Slot)gameSlot.getComponent(1);
			if(!tilesPlayedBeforeConfirmed.contains(firstSlot.getComponent(0)))
			{
				gameSlot.setEnabled(false); //setting back the occupied game slot to disabled
			}
		}
	}

}
