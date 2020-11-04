package lp24Project.model;

import java.util.Stack;

//Corresponds to the pouch, it simply is a stack with a boolean in order to manage its accessibility by the player
public class Pouch extends Stack<Tile>{
	private boolean isDrawable = true;
	
	public Pouch()
	{
		super();
	}
	
	public boolean isDrawable()
	{
		return isDrawable;
	}
	
	public void setDrawable(boolean bool)
	{
		isDrawable = bool;
	}
	public Stack<Tile> getPouch()
	{
		return this;
	}
	
	public Tile push(Tile tile)
	{	
		return super.push(tile);
	}
	
	@Override
	public Tile pop()
	{
		if(this.size() != 0)
			return super.pop();
		return null;
	}
}
