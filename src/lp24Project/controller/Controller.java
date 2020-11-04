package lp24Project.controller;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import lp24Project.model.Model;
import lp24Project.model.Tile;
import lp24Project.view.Board;
import lp24Project.view.BottomRack;
import lp24Project.view.EndGameScene;
import lp24Project.view.FeaturePanel;
import lp24Project.view.GameScene;
import lp24Project.view.GameSlot;
import lp24Project.view.MenuScene;
import lp24Project.view.Rack;
import lp24Project.view.Slot;
import lp24Project.view.View;

public class Controller {
	//Singleton of the class
	private static Controller instance;
	
	//All these variables could be accessed directly using functions, however we prefer using something named "caching" for better optimization
	private View viewInstance = View.getInstance();
	private Model modelInstance = Model.getInstance();

	private MenuScene menuScene;
	private GameScene gameScene;
	private EndGameScene endGameScene;
	
	private BottomRack currentPlayerRack;
	private Board boardPanel;
	private FeaturePanel featurePanel;
	
	//The list of action that occured during a player's turn (Command pattern)
	private List<ICommand> commandList = new ArrayList<ICommand>();
	
	//Operations that will only occur once
	private Controller()
	{
		//add listeners to all the 106 tiles
		for(Tile tile : modelInstance.getPoolOfTiles())
		{
			tile.addActionListener((action) -> onTileClicked(tile));
		}
		
		//init controller's variable and adding listeners thanks to the variables which can be instantiated without know amount of players
		initController();
	}
	
	//Lazy instantiation of the singleton of the Controller class (and getter of that singleton)
	public static Controller getInstance()
	{
		if(instance == null)
		{
			instance = new Controller();
		}
		return instance;
	}
	
/////////////////////////////////CONTROLLER (RE)INITIALIZATION///////////////////////////////////////////////
	
	public void initController()
	{
		cachFirstVariables();
		addFirstListeners();
	}
	
	private void cachFirstVariables()
	{
		menuScene = viewInstance.getMenuScene();
		boardPanel = viewInstance.getBoard();
		featurePanel = viewInstance.getFeaturePanel();
		gameScene = viewInstance.getGameScene();
	}
	
	private void addFirstListeners()
	{
		//MenuScene listeners
		menuScene.getSecondWindowButton().addActionListener((action) -> onPlayClicked());
		menuScene.getChoosePlayerButton().addActionListener((action) -> onChoosePlayersClicked());
		menuScene.getRulesButton().addActionListener((action) -> onRulesClicked());
		menuScene.getUselessButton().addActionListener((action) -> onUselessClicked());
		menuScene.getExitButton().addActionListener((action) -> onExitClicked());
		
		//FeaturePanel listeners
		featurePanel.getRemoveEmptySpacesButton().addActionListener((action) -> onRemoveButtonClicked());
		featurePanel.getSortButton().addActionListener((action)-> onSortClicked());
		featurePanel.getConfirmPointsButton().addActionListener((action) -> onConfirmClicked());
		featurePanel.getUndoButton().addActionListener((action) -> onUndoClicked());
		featurePanel.getUndoAll().addActionListener((action) -> onUndoAllClicked());
		featurePanel.getValidateButton().addActionListener((action) -> onValidateClicked());
		
		//Slots listeners
		Slot[] slots = viewInstance.getSlots();
		for(int i = 0; i < slots.length; i++)
		{
			JButton button = (JButton)slots[i].getComponent(0);
			button.addActionListener(action -> onFreeSlotClicked(button));
		}
	}
	
	private void delayedInitController()
	{
		caching();
		addLlisteners();
	}
	
	private void caching()
	{
		endGameScene = viewInstance.getEndGameScene();
		currentPlayerRack = gameScene.getBottomRack();
		
	}
	
	private void addLlisteners() {
		//add listener on the bottom panel (corresponding to the current player rack)
		currentPlayerRack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				onRackClicked();
			}
		});
		
		
		//end game Scene listeners
		endGameScene.getQuitButton().addActionListener((action) -> onExitClicked());
		endGameScene.getRestartButton().addActionListener((action) -> onRestartClicked());
	}
	
////////////////////////////////////////END OF (RE) INITIALIZATION///////////////////////////////////////////////
	
///////////////////////////////////////MENU AND END GAME SCENE LISTENERS AND RELATED FUNCTIONS/////////////////////////////////////
	
	private void onPlayClicked()
	{
		//The user chose the number of players, therefore every thing can now be initialized
		//initialization of the model
		modelInstance.instantiatePlayers(menuScene.getNumberOfPlayers());
		
		//initialization of the view when knowing the amount of players
		viewInstance.delayedInitView(modelInstance.getPlayers());
		
		//caches and add listeners of the variables which couldn't be instantiated before the user chose the number of players 
		delayedInitController();
	}
	
	private void onChoosePlayersClicked()
	{
		menuScene.setInternalFrameVisible(true);
	}
	
	private void onRulesClicked()
	{
		try 
		{
			URI uri= new URI("https://tesera.ru/images/items/784463/Rummikub_Rules.pdf");   
			java.awt.Desktop.getDesktop().browse(uri);
			 
		} catch (Exception e) {
			   
			e.printStackTrace();
		}
	}
	
	private void onUselessClicked()
	{
		MenuScene.clickCount++;
		if(MenuScene.clickCount == 20)
			menuScene.displayUselessLabel();
	}
	
	private void onExitClicked()
	{
		System.exit(0);
	}
	
	private void onRestartClicked()
	{
		
		modelInstance.reInitModel();
		gameScene.reinitGameScene(modelInstance.getPlayers());
		boardPanel.reinitGameSlots();
		viewInstance.loadGameScene();
	}
////////////////////////////////////END OF MENU AND END GAME SCENE LISTENERS//////////////////////////////
	
///////////////////////////////////GAME SCENE LISTENERS AND RELATED FUNCTIONS//////////////
	private void onTileClicked(Tile tileSource)
	{
		//caching of focusedTile
		Tile focusedTile = modelInstance.getFocusedTile();
		
		//if there is a focusedTile, then it means that the user wants to switch the places of the focused tile and the tile he just clicked on
		if(focusedTile != null)
		{
			Container tileSourceParent = tileSource.getParent();
			Container focusedTileParent = focusedTile.getParent();
			
			//different possible actions depending on where the tiles are
			if(tileSourceParent instanceof Rack && modelInstance.playedTilesContainsTile(focusedTile))
			{
				SwitchTilesBoardToRack newAction = new SwitchTilesBoardToRack(focusedTile, tileSource, (Slot)focusedTileParent, currentPlayerRack);
				commandList.add(newAction);
			}
			else if(focusedTileParent instanceof Rack && modelInstance.playedTilesContainsTile(tileSource))
			{
				SwitchTilesBoardToRack newAction = new SwitchTilesBoardToRack(tileSource, focusedTile, (Slot)tileSourceParent, currentPlayerRack);
				commandList.add(newAction);
			}
			else if(tileSourceParent instanceof Slot && focusedTileParent instanceof Slot)
			{
				SwitchTilesBoardToBoard newAction = new SwitchTilesBoardToBoard(focusedTile, tileSource, (Slot)focusedTileParent);
				commandList.add(newAction);
			}
			else if (focusedTileParent instanceof Rack && tileSourceParent instanceof Rack)
				switchTilesRackToRack(focusedTile, tileSource);
			modelInstance.setFocusedTile(null);
		}
		else
		{
			//if there is no focused tile, then the tile the user clicked on is now focused
			modelInstance.setFocusedTile(tileSource);
		}
	}
	
	private void switchTilesRackToRack(Tile focusedTile, Tile tileSource)
	{
		currentPlayerRack.switchButtons(focusedTile, tileSource);
	}
	
	
	private void onFreeSlotClicked(JButton sourceButton)
	{
		//if the current player clicked on a focused tile without clicking on a tile before hand, then no action should occur
		if(modelInstance.getFocusedTile() != null)
		{
			Tile focusedTile = modelInstance.getFocusedTile();
			Slot buttonParent = (Slot)sourceButton.getParent();
			if(!buttonParent.hasTile())
			{
				PlaceTileOnBoard newAction = new PlaceTileOnBoard(focusedTile, buttonParent, currentPlayerRack);
				commandList.add(newAction);
			}
			//Since the player actually played, the pouch becomes un accessible
			modelInstance.setPouchDrawable(false);
			
			viewInstance.repaint();
		}
		
	}
	
	//Occurs when the user clicks on the low panel containing his tiles(next to his tiles)
	private void onRackClicked()
	{
		Tile focusedTile = modelInstance.getFocusedTile();
		if(focusedTile != null)
		{
			//checks if the player has already played the focused tile during his turn. If so, then he can take it back.
			if(modelInstance.playedTilesContainsTile(focusedTile))
			{
				RackClicked newAction = new RackClicked(currentPlayerRack, focusedTile);
				commandList.add(newAction);
			}
		}
		
	}
	
	//This buttons sort all the game slots and remove every empty spaces between the tiles
	private void onRemoveButtonClicked()
	{
		for(GameSlot gameSlot : boardPanel.getGameSlots())
		{
			gameSlot.createTileListAndRemoveSpaces();
		}
	}
	
	private void onSortClicked()
	{
		currentPlayerRack.updateAfterSort();
	}
	
	
	private void onConfirmClicked()
	{
		//checks if the player already has full access to the board (to other player's sets)
		if(!modelInstance.getCurrentPlayer().isOut())
		{
			//checks if the board contains set errors
			boolean checkBoard = checkBoard();
			
			//if there are no errors and if the player has enough point, then he gets to have a full access to the board
			if(checkBoard && pointsReached())
			{
				UnlockFullAccess newAction = new UnlockFullAccess(boardPanel, featurePanel);
				commandList.add(newAction);
			}
			//if there is no mistake in the player's set but he doesn't have the required amount of points, then a specific message appears
			else if(checkBoard == true)
				boardPanel.displayOutMessage(false);
			
			featurePanel.getValidateButton().setEnabled(true);
		}
	}
	
	private boolean pointsReached()
	{
		int sum = 0;
		//get every slot where the current player place his tiles
		for(GameSlot gameSlot : boardPanel.getOccupiedAndEnabledGameSlots())
		{
			//sum the points of these slots
			int gameSlotPoints = gameSlot.getTilesList().getScore();
			sum += gameSlotPoints;
		}
		if(sum >= 30)
			return true;
		return false;
	}
	
	private void onUndoClicked()
	{	
		if(!commandList.isEmpty())
		{
			commandList.get(commandList.size() - 1).undo();
			commandList.remove(commandList.size()-1);	
		}
	}
	
	private void onUndoAllClicked()
	{
		while(!commandList.isEmpty())
			onUndoClicked();
	}

	
	private void onValidateClicked()
	{
		if(checkBoard() && checkForPlayersPoints())//checks if all the sets on the board have no mistakes, and then if the player is out or if he has no sets on the board
		{
			
			if(modelInstance.isPouchDrawable() && modelInstance.getPouch().size() > 0) //Make the player draw a card at the end of his turn if he didn't do anything
				currentPlayerRack.addNewTile(modelInstance.getTileFromPouch(), currentPlayerRack.getComponentCount());
			
			//empty the command list so that the next player can't undo the action of the last player
			emptyCommandList();
			
			
			if(modelInstance.getPouch().size() == 0)
			{
				//no more tiles in the pouch, victory of one of the player
				lessTileVictory();
				
				return;
			}
			
			
			if(modelInstance.getPlayers().getTail().getValue().getTiles().size() == 0)
			{
				//the player who just played has no tiles left, he won the round
				victory();
				return;
			}
			
			modelInstance.setPouchDrawable(true);
			nextTurn();
			
			
			if(!modelInstance.getCurrentPlayer().isOut())
			{
				//if the new player doesn't have full access to the board yet, then the sets of the other players are locked
				featurePanel.getConfirmPointsButton().setEnabled(true);
				for(GameSlot gameSlot : boardPanel.getOccupiedGameSlots())
				{
					gameSlot.setEnabled(false);
				}
			}
			else
			{
				featurePanel.getConfirmPointsButton().setEnabled(false);
				for(GameSlot gameSlot : boardPanel.getGameSlots())
				{
					gameSlot.setEnabled(true);
				}
			}
		}
	}

	private void nextTurn() {
		//Update the list of tiles that each player owns
		modelInstance.nextTurn();
		//Update the tiles contained by the racks
		gameScene.updatePlayers(modelInstance.getPlayers());
		//Update the racks UI
		gameScene.repaintRacks();
	}
	
	private boolean checkBoard()
	{
		//checks if there are some mistakes in the sets on the board
		boolean isChecked = true;
		for(GameSlot gameSlot : boardPanel.getOccupiedGameSlots())
		{
			if(!gameSlot.check())
			{
				isChecked = false;
			}
		}
		return isChecked;
	}
	
	
	//this function only occurs when the board doesn't contain any mistake
	//This function exists in case where the player didn't hit the confirm button while not having full access to the board, but has some tiles left on the board
	private boolean checkForPlayersPoints()
	{
		if(!modelInstance.getCurrentPlayer().isOut() && boardPanel.getOccupiedAndEnabledGameSlots().size() != 0)
		{
			if(pointsReached())
			{
				//if the player hit the confirm button and had enough points with his points on the board to have the full access, then he unlock that full access to the board
				//(we consider that he forgot to hit the confirmation button)
				UnlockFullAccess newAction = new UnlockFullAccess(boardPanel, featurePanel);
				if(boardPanel.getDisabledGameSlots().size() != 0)
				{
					commandList.add(newAction);
					return false; //Simulate a click on confirm 30 points
				}
				return true; //The player has the 30 points when clicking validate, and there is no other players set on the board. This return exists in order to prevent the player from clicking to times
						//on the validate button
			}
			//else, he simply forgot to take his tiles back from the board, the game tell him to put on more points, or to remove his tiles
			boardPanel.displayOutMessage(false);
			return false;
				
		}
		return true; //The player is out or the board doesn't contain any set formed by the player
	}
	
	private void victory()
	{
		viewInstance.loadEndGameScene(modelInstance.getPlayers(), modelInstance.victory());
	}
	
	private void lessTileVictory()
	{
		viewInstance.loadEndGameScene(modelInstance.getPlayers(), modelInstance.lessTileVictory());
	}
	
	private void emptyCommandList()
	{
		while(!commandList.isEmpty())
		{
			commandList.remove(0);
		}
	}	
}
