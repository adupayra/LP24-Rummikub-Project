package lp24Project.model;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Tile extends JButton {
	public static final Color TILECOLOR = new Color(31, 160, 85);
	public static final Color ONFOCUSEDCOLOR = new Color(124, 230, 147);
	private static final ImageIcon JOKERICON = new ImageIcon("resources/jokerIcon.png");
	private int number;
	private Color color;
	private boolean isJoker = false;
	
	public Tile(int number, Color color)
	{
		this.setBackground(TILECOLOR);
		this.number = number;
		this.color = color;
		this.setForeground(color);
		
	}
	
	public boolean isJoker()
	{
		return isJoker;
	}
	
	public void setJoker(boolean bool)
	{
		isJoker = bool;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int number)
	{
		if(isJoker)
			this.number = number;
	}
	
	public void setColor(Color color)
	{
		if(isJoker)
			this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	@Override
	public void setEnabled(boolean bool)
	{
		super.setEnabled(bool);
		
		//For the joker's icon to appear, it is necessary to set the button's icon every time we set the button enable
		if(bool)
		{
			if(isJoker)
			{
				setIcon(JOKERICON);
			}
			else
			{
				this.setText("" + number);
			}
		}
		else
		{
			if(isJoker())
			{
				setIcon(null);
			}
			this.setText("");
		}
			
	}
	
	@Override
	public String toString()
	{
		return "number is "+ number + " Color is + "+ color.toString();
	}
}
