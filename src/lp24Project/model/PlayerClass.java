package lp24Project.model;

public class PlayerClass {
	private TileList tiles;
	private boolean isOut = false; //This variable tells if the player has access to the other player's set or not
	private String name;
	private int score;
	
	public PlayerClass()
	{
		
	}
//////////////////////////////////GETTERS AND SETTERS////////////////////////////
	public boolean isOut()
	{
		return isOut;
	}
	
	public void setOut(boolean bool)
	{
		isOut = bool;
	}
	
	public int getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score += score;
	}
	
	public TileList getTiles()
	{
		return tiles;
	}
	
	public Tile getTileAtIndex(int index)
	{
		return tiles.get(index);
	}
	
	public String getName()
	{
		return name;
	}
	
	
	public boolean isTilePossessed(Tile tile)
	{
		for(Tile playerTile : tiles)
		{
			if(playerTile == tile)
			{
				return true;
			}
		}
		return false;
	}
	
//////////////////////////////////END OF GETTERS/SETTERS//////////////////////////
	
	public void initPlayer(TileList tiles, String name)
	{
		this.tiles = tiles;
		if(name != null)
			this.name = name;
		
	}
	
	public void removeTile(Tile tileToRemove)
	{
		tiles.remove(tileToRemove);
	}
	
	public void addTile(Tile tileToAdd)
	{
		tiles.add(tileToAdd);
		
	}
	
	public void sortTiles()
	{
		if(!tiles.isEmpty())
			tiles.sort();
	}
	
	public void resetTiles()
	{
		tiles.removeAll(tiles);
	}
	
	public int computeScore()
	{
		int sum = 0;
		for(Tile tile : tiles)
		{
			if(tile.isJoker())
			{ //add an extra 30 points if the tile is a joker
				sum+=30;
			}
			else
				sum += tile.getNumber();
		}
		return sum;
	}
	
	public void computeScoreAndAddIt()
	{
		score -= computeScore();
	}
	
}
