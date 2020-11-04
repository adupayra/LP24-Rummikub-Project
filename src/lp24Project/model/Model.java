package lp24Project.model;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import lp24Project.CircleLinkedList;
import lp24Project.Node;

public class Model {
	private static Model instance = new Model();
	
	//Players
	private CircleLinkedList players;
	
	//pool of tiles and pouch
	private Tile[] poolOfTiles = new Tile[106];
	private Pouch pouch;
	
	//Tile on which the player set the focus (clicked on it)
	private Tile focusedTile;
	
	//Tiles played during a player's turn, used to determine if the current player can take back the tiles from the board to his rack
	private List<Tile> tilesPlayedDuringTurn = new ArrayList<Tile>();
	
	
	
////////////////////////////////////GETTERS/SETTERS/////////////////////////////////////
	
	//Used to get access to the model by other classes
	public static Model getInstance()
	{
		return instance;
	}
	
	public Tile[] getPoolOfTiles()
	{
		return poolOfTiles;
	}
	
	public Pouch getPouch()
	{
		return pouch;
	}
	
	public List<Tile> getTilesPlayedDuringTurn()
	{
		return tilesPlayedDuringTurn;
	}
	
	
	public CircleLinkedList getPlayers()
	{
		return players;
	}
	
	public PlayerClass getCurrentPlayer()
	{
		return players.getHead().getValue();
	}
	
	public Tile getFocusedTile()
	{
		return focusedTile;
	}
	
	public Tile getTileFromPouch()
	{
		Tile drawedTile = pouch.pop();
		players.getHead().getValue().addTile(drawedTile);
		return drawedTile;
	}
	
	public boolean isPouchDrawable()
	{
		return pouch.isDrawable();
	}
	
	public void setFocusedTile(Tile tileToFocus)
	{
		if(tileToFocus == null)
		{
			if(focusedTile != null)
				focusedTile.setBackground(Tile.TILECOLOR);
			focusedTile = null;
		}
		else
		{
			if(focusedTile != null)
				focusedTile.setBackground(Tile.TILECOLOR);
			focusedTile = tileToFocus;
			focusedTile.setBackground(Tile.ONFOCUSEDCOLOR);
		}
		
	}
	
	private void setTilesDisabled()
	{
		for(Tile tile : poolOfTiles)
		{
			tile.setEnabled(false);
		}
	}
	
	public void setPouchDrawable(boolean bool)
	{
		pouch.setDrawable(bool);
	}
	

////////////////////////////////////END OF GETTERS/SETTERS/////////////////////////
	
/////////////////////////(RE)INITIALIZATION OF THE MODEL AND THE DATA///////////////////////

//Constructor made private to have only one instance of the model (singleton pattern)
	private Model()
	{
		modelInit();
	}
	
	//Function which is only launched at the instantiation of the model, therefore, once
	private void modelInit()
	{
		initTiles();	
	}
	
	//Instantiation of every tiles of the game
	private void initTiles()
	{
		Color[] tileColors = { Color.blue, Color.black, Color.orange, Color.red };
		int j = 0;
		for(int deckOf52Tiles = 1; deckOf52Tiles <= 2; deckOf52Tiles++)
		{
			for(Color color : tileColors)
			{
				for(int i = 1; i < 14; i++)
				{
					Tile newTile = new Tile(i, color);
					newTile.setEnabled(false);
					poolOfTiles[i+j - 1] = newTile;
				}
				j+=13;
			}
			
		}
		//add jokers
		Tile joker1 = new Tile(0, Color.green);
		Tile joker2 = new Tile(0, Color.green);
		poolOfTiles[poolOfTiles.length - 2] = joker1;
		joker1.setJoker(true);
		joker1.setEnabled(false);
		poolOfTiles[poolOfTiles.length - 1] = joker2;
		joker2.setJoker(true);
		joker2.setEnabled(false);
	}
	

	//called by the controller when the player hits restart
	public void reInitModel()
	{
		tilesPlayedDuringTurn.removeAll(tilesPlayedDuringTurn);
		Node temp = players.getHead();
		do
		{
			temp.getValue().resetTiles();
			temp = temp.getNext();
		}while(temp != players.getHead());
		setTilesDisabled();
		initRound();
	}
	
	//Called by the Controller when the player hits Play
	public void instantiatePlayers(int nbPlayers)
	{
		players = new CircleLinkedList();
		for(int i = 0; i < nbPlayers; i++)
		{
			players.addNode(new PlayerClass());
		}
		
		initRound();
	}
	
	//Initialize the data we need for the game
	public void initRound()
	{
		shuffleTiles();
		initPlayersAndPouch();
	}

	//Shuffles the pool of tiles before distributing them
	private void shuffleTiles()
	{
		 Random rand = new Random(); 
         
        for (int i = 0; i < poolOfTiles.length; i++) 
        { 
	        // Random for remaining positions. 
	        int r = i + rand.nextInt(106 - i); 
	        
	        //swapping the elements 
	        Tile temp = poolOfTiles[r]; 
	        poolOfTiles[r] = poolOfTiles[i]; 
	        poolOfTiles[i] = temp; 
               
        } 

	}
	
	//Distribution of tiles to the player and to the pouch
	private void initPlayersAndPouch()
	{
		int j = 0;
		int nbTiles = 13;
		Node temp = players.getHead();
		for(j = 0; j < nbTiles * players.getSize(); j+=nbTiles)
		{
			TileList playerRack = new TileList();
			for(int i = 0; i < nbTiles; i++)
			{
				//the condition is here so that the first player's tiles will be enabled
				if(j==0)
				{
					poolOfTiles[i].setEnabled(true);
				}
				playerRack.add(poolOfTiles[i+j]);
			}
			playerRack.sort();
			temp.getValue().initPlayer(playerRack,  "Player " + (j/nbTiles + 1));
			temp = temp.getNext();
		
		}
		
		//Instantiation of the pouch
		pouch = new Pouch();
		for(int p = j; p < poolOfTiles.length; p++)
		{
			pouch.push(poolOfTiles[p]);			
		}
	}
	
//////////////////////////////END OF INIT///////////////////////////
	
/////////////////////////////MANIPULATION OF DATA///////////////////
	
	//called when there's a change of player
	public void nextTurn()
	{
		players.moveToNext(tilesPlayedDuringTurn);
		setFocusedTile(null);
		tilesPlayedDuringTurn.removeAll(tilesPlayedDuringTurn);
	}
	
	
	
	//The next 5 functions are here to manage the tiles played by a player during his turn
	public void addTilePlayedDuringTurn(Tile tileToAdd)
	{
		tilesPlayedDuringTurn.add(tileToAdd);
	}
	
	public void removeTileFromPlayer(Tile tileToRemove)
	{
		players.getHead().getValue().removeTile(tileToRemove);
	}
	
	public void removeTilePlayedDuringTurn(Tile tileToRemove)
	{
		if(!tilesPlayedDuringTurn.isEmpty())
			tilesPlayedDuringTurn.remove(tileToRemove);
	}
	
	public void removeTilesPlayedDuringTurn()
	{
		for(Tile tile : tilesPlayedDuringTurn)
		{
			players.getHead().getValue().removeTile(tile);
		}
		tilesPlayedDuringTurn.removeAll(tilesPlayedDuringTurn);
	}
	
	public boolean playedTilesContainsTile(Tile tile)
	{
		if(!tilesPlayedDuringTurn.isEmpty())
		{
			return tilesPlayedDuringTurn.contains(tile);
		}
		return false;
	}
	
	
	
////////////////////////////VICTORY MANAGEMENT ON DATA/////////////////////
	public PlayerClass victory()
	{
		//set winner's score based on the loser's score
		players.getTail().getValue().setScore(findLoserScore());
		
		Node temp = players.getHead();
		//go through the player's list and compute their score until we get to the winner
		do
		{
			
			temp.getValue().computeScoreAndAddIt();
			temp = temp.getNext();
			
		}while(temp != players.getTail());
		
		//We know that the winner is the last player who played, hence the tail of our circleLinkedList
		return players.getTail().getValue();
	}
	
	//Find the score of the one with the less points in order to add it to the winner's score
	private int findLoserScore()
	{
		int score = Integer.MIN_VALUE;
		Node temp = players.getHead();
		//go through the player's list and find the biggest amount of points (which corresponds to the loser's points)
		do
		{
			int tempScore = temp.getValue().computeScore();
			score = score > tempScore ? score : tempScore;
			temp = temp.getNext();
		}while(temp != players.getTail());
		return score;
	}
	
	
	//Find the winner in case of pouch empty and set scores (winner gets no points and losers gets add their tiles as negative score)
	public PlayerClass lessTileVictory() 
	{
		int nbTiles = Integer.MAX_VALUE;
		PlayerClass winner= null;
		Node temp = players.getHead();
		//go through the player's list and find player with the least amount of tiles
		do
		{
			int tempTiles = temp.getValue().getTiles().size();
			if(tempTiles < nbTiles)
			{
				winner = temp.getValue();
				nbTiles = tempTiles;
			}
			temp =temp.getNext();
		}while(temp != players.getHead());
		
		Node temp2 = players.getHead();
		do
		{
			if(temp2.getValue() != winner)
				temp2.getValue().computeScoreAndAddIt();
			temp2 = temp2.getNext();
		}while(temp2 != players.getHead());
		return winner;
	}
}
