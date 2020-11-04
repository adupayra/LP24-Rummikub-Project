package lp24Project.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//This class consists of a list of tiles which we can know if it is a set
public class TileList extends ArrayList<Tile> implements Comparator<Tile>{
	private boolean hasJoker = false;
	private boolean isRun = false;
	private boolean isGroup = false;
	private int score;
	
	public TileList()
	{
		super();
	}
	
	public TileList(TileList tiles)
	{
		super(tiles);
		hasJoker();
	}

	public boolean isRun()
	{
		return isRun;
	}
	
	public boolean isGroup()
	{
		return isGroup;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void sort()
	{
		//the sort won't be the same depending of the presence of a joker or not
		hasJoker();
		if(hasJoker == true)
		{
			sortWithJokers();
		}
		else
			super.sort(this);
	}
	
	private void hasJoker()
	{
		hasJoker = false;
		for(Tile tile : this)
		{
			if(tile.isJoker())
				hasJoker = true;
		}
	}
	
	//jokers are first being removed in order to sort the list without them, and then placed back at their original place
	private void sortWithJokers() {
		List<Integer> jokersIndexes = getJokersIndexes();
		boolean alreadySorted = false;
		//get the joker index and remove the joker from the list
		Tile temp = this.get(jokersIndexes.get(0));
		this.remove((int)jokersIndexes.get(0));
		if(jokersIndexes.size() == 2)
		{
			//if there's a second joker, do the same as for the first one
			Tile temp2 = this.get(jokersIndexes.get(1) - 1);
			this.remove((int)jokersIndexes.get(1) - 1);

			super.sort(this);
			alreadySorted = true;
			//add the two jokers at their original place
			this.add(jokersIndexes.get(1) - 1, temp2);
		}
		if(!alreadySorted)
			super.sort(this);
		//particular case when the joker is at the end of the list, because we can't explicitly place a new element at the end of a list using indexes
		if(this.size() != 0 && jokersIndexes.get(0) != this.size())
		{
			this.add(jokersIndexes.get(0), temp);
		}
		else
			this.add(temp);
		
	}
	
	private List<Integer> getJokersIndexes()
	{
		List<Integer> listOfJokers = new ArrayList<Integer>();
		for(int i = 0; i < this.size(); i ++)
		{
			if(this.get(i).isJoker())
				listOfJokers.add(i);
		}
		return listOfJokers;
	}
	
	//check if the list is a run or a group or both
	public boolean checkCombinations()
	{
		checkRun();
		checkGroup();
		return isRun == true || isGroup == true;
	}
	
	private void checkRun()
	{
		if(this.size() < 3)
		{
			isRun = false;
		}
		else
		{
			if(this.hasJoker)
			{
				giveFirstPosJokerValues();
			}
			runLoop();
		}
	}
	
	//handles the specific cases where the set starts with 2 jokers or with 1 joker
	private void giveFirstPosJokerValues()
	{
		
		if(get(0).isJoker() && get(1).isJoker())
		{
			this.get(0).setColor(get(2).getColor());
			this.get(0).setNumber(get(2).getNumber() - 2);
		}
		else if(get(0).isJoker())
		{
			this.get(0).setColor(get(1).getColor());
			this.get(0).setNumber(get(1).getNumber() - 1);
		}
	}

	private void runLoop() 
	{
		boolean check = true;
		Tile firstTile = this.get(0);
		if(firstTile.getNumber() <= 0)
			check = false; //case where the first tile is a joker and its value must be negative in order for the set to be valid
		int i = 0;
		while(i < this.size() - 1 && check == true)
		{
			if(get(i + 1).isJoker())
			{
				//I attributed actual tiles values to the jokers in order to compute the actual score of the set
				//It also is easier to treat the joker when they have the same attributes as the other tiles
				this.get(i + 1).setColor(firstTile.getColor());
				this.get(i + 1).setNumber(firstTile.getNumber() + i + 1);
				if(get(i + 1).getNumber() >= 14)
					check = false;
			}
			if(get(i + 1).getNumber() != firstTile.getNumber() + (i+1) || get(i+1).getColor() != firstTile.getColor()) //condition for the set to be a run
				check = false;
			i++;
		}
		isRun = check;
		if(isRun == true)
		{
			int runScore = computeScore();
			score = runScore > score ? runScore : score;
		}
	}
	
	private void checkGroup()
	{
		if(this.size() < 3 || this.size() > 4)
		{
			isGroup = false;
		}
		else
		{
			//The list of colors will be used to determine if a color has already been checked in the set
			List<Color> colors = new ArrayList<Color>();
			colors.add(Color.red);
			colors.add(Color.orange);
			colors.add(Color.black);
			colors.add(Color.blue);
			
			boolean check = true;
			//specific cases of 2 jokers at first and second places, and 1 joker at first place
			if(this.get(0).isJoker() && this.get(1).isJoker())
			{
				this.get(0).setNumber(this.get(2).getNumber());
			}
			else if(this.get(0).isJoker())
				this.get(0).setNumber(this.get(1).getNumber());
			else
				colors.remove(this.get(0).getColor());
			int groupNumber = this.get(0).getNumber();
			
			int i = 0;
			while(i < this.size() - 1 && check == true)
			{
				if(get(i+1).isJoker())
					this.get(i + 1).setNumber(groupNumber);
				else if(get(i+1).getNumber() != get(i).getNumber() || !colors.contains(get(i+1).getColor())) //condition for the set to be a group
					check = false;
				else
					colors.remove(get(i+1).getColor());
				i++;
			}
			isGroup =  check;
			if(isGroup == true)
			{
				int groupScore = computeScore();
				score = groupScore > score ? groupScore : score;
			}
		}
	}
	
	//Compute the amount of points of the set
	private int computeScore()
	{
		int sum = 0;
		for(Tile tile : this)
		{
			sum += tile.getNumber();
		}
		return sum;
	}
	
	//used to sort the list of tiles increasingly
	@Override
	public int compare(Tile tile1, Tile tile2) {
		return tile1.getNumber() < tile2.getNumber() ? -1 : tile1.getNumber() == tile2.getNumber() ? 0 : 1;
	}
}
