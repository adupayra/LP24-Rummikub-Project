package lp24Project.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import lp24Project.model.Tile;

public class Slot extends JPanel{
	private CardLayout cl = new CardLayout();
	private JButton freeSlotButton = new JButton();
	
	//gameSlot the slot is contained in
	private GameSlot gameSlot;
	
	public static final Dimension BUTTONINSLOTDIMENSION = new Dimension(35,35);
	private static final Color ENABLEDSLOTCOLOR = new Color(237, 0, 0);
	private static final Color DISABLEDSLOTCOLOR = new Color(229, 167, 190);
	private static final Color WARNINGCOLOR = new Color(255, 73, 1);
	
	public Slot(GameSlot gameSlot)
	{
		this.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e)
			{
				//listener to update the slot (visually) whenever a button is added to it
				updateSlot();
			}
		});
		this.gameSlot = gameSlot;
		setLayout(cl);
		add(freeSlotButton);
		freeSlotButton.setPreferredSize(BUTTONINSLOTDIMENSION);
		freeSlotButton.setBackground(ENABLEDSLOTCOLOR);
	}
	
	public GameSlot getGameSlot()
	{
		return gameSlot;
	}
	
	//determines if the slot is free or not
	public boolean hasTile()
	{
		return this.getComponentCount()==2;
	}
	
	public void switchTiles(Tile tileToSwitchWith)
	{
		Slot tileToSwitchWithParent = (Slot)tileToSwitchWith.getParent();
		tileToSwitchWithParent.add(this.getComponent(0), 0);
		this.add(tileToSwitchWith, 0);
	}
	
	@Override
	public void setEnabled(boolean bool)
	{
		JButton button = (JButton)this.getComponent(0);
		button.setEnabled(bool);
		if(bool)
		{
			setNormalColor(button);
		}
		else
			button.setBackground(DISABLEDSLOTCOLOR);
	}
	
	//Set warning color the slot
	public void setWarningOnTile()
	{
		JButton button = (JButton)this.getComponent(0);
		Thread warningThread = new Thread(new Runnable() {
			@Override
			public void run() 
			{
				button.setBackground(WARNINGCOLOR);
				gameSlot.setWarningLabelVisible(true);
				try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gameSlot.setWarningLabelVisible(false);
				setNormalColor(button);
			}
			
		});
		warningThread.start();
	}
	
	private void setNormalColor(JButton button)
	{
		if(this.hasTile())
			button.setBackground(Tile.TILECOLOR);
		else
			button.setBackground(ENABLEDSLOTCOLOR);
	}
	
	public void updateSlot()
	{
		cl.first(this);
	}
	
}
