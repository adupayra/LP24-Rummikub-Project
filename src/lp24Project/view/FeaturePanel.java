package lp24Project.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

//Panel with the feature button (Sort rack, confirm points, validate...)
public class FeaturePanel extends JPanel {
	private JButton validate = new JButton("Validate");
	private JButton sort = new JButton("Sort rack");
	private JButton confirmPoints = new JButton("Confirm 30 points");
	private JButton removeEmptySpaces = new JButton("Remove empty spaces and sort slots");
	private JButton undo = new JButton("Undo");
	private JButton undoAll = new JButton("Undo All");
	private static final Color FEATUREBUTTONSCOLOR = new Color(129, 20, 83);
	
	public FeaturePanel()
	{
		setLayout(new GridLayout());
		setBackground(Color.cyan);
		add(sort);
		add(removeEmptySpaces);
		add(validate);
		add(confirmPoints);
		add(undo);
		add(undoAll);
		undoAll.setBackground(FEATUREBUTTONSCOLOR);
		undoAll.setForeground(Color.white);
		undo.setBackground(FEATUREBUTTONSCOLOR);
		undo.setForeground(Color.white);
		confirmPoints.setBackground(FEATUREBUTTONSCOLOR);
		confirmPoints.setForeground(Color.white);
		removeEmptySpaces.setBackground(FEATUREBUTTONSCOLOR);
		validate.setBackground(FEATUREBUTTONSCOLOR);
		sort.setBackground(FEATUREBUTTONSCOLOR);
		sort.setForeground(Color.white);
		removeEmptySpaces.setForeground(Color.white);
		validate.setForeground(Color.white);
	}
	
	public JButton getRemoveEmptySpacesButton()
	{
		return removeEmptySpaces;
	}
	
	public JButton getSortButton()
	{
		return sort;
	}
	
	public JButton getValidateButton()
	{
		return validate;
	}
	
	
	public JButton getConfirmPointsButton()
	{
		return confirmPoints;
	}
	
	public JButton getUndoButton()
	{
		return undo;
	}
	
	public JButton getUndoAll()
	{
		return undoAll;
	}
	
}
