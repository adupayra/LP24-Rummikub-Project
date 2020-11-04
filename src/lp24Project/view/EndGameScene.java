package lp24Project.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lp24Project.CircleLinkedList;
import lp24Project.Node;
import lp24Project.model.PlayerClass;

//Class corresponding to the panel shown at the end of a round
public class EndGameScene extends JPanel{
	private JPanel northPanel = new JPanel();
	private JLabel victoryText = new JLabel();
	private BorderLayout bl = new BorderLayout();
	
	private JPanel centerPanel = new JPanel();
	private JLabel[] scoreDisplayers;
	private JButton restartButton = new JButton("Restart");
	private JButton quitButton = new JButton("Quit");
	
	private static final Color PANELCOLOR = new Color(139, 0, 139);
	private static final Color BUTTONSCOLOR = new Color(201, 142, 196);
	
	
	public EndGameScene(int nbPlayers)
	{
		setLayout(bl);
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(0, 1, 0, 50));
		centerPanel.add(restartButton);
		centerPanel.add(quitButton);
		northPanel.add(victoryText);
		centerPanel.setBackground(PANELCOLOR);
		northPanel.setBackground(PANELCOLOR);
		restartButton.setBackground(BUTTONSCOLOR);
		quitButton.setBackground(BUTTONSCOLOR);
		restartButton.setForeground(Color.WHITE);
		quitButton.setForeground(Color.WHITE);
		victoryText.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		victoryText.setForeground(Color.WHITE);
		
		//Instantiate the right amount of Labels for the players
		scoreDisplayers = new JLabel[nbPlayers];
		for(int i = 0; i < scoreDisplayers.length; i++)
		{
			scoreDisplayers[i] = new JLabel("", SwingConstants.CENTER);
			centerPanel.add(scoreDisplayers[i]);
		}
	}
	
	public JButton getRestartButton()
	{
		return restartButton;
	}
	
	public JButton getQuitButton()
	{
		return quitButton;
	}
	
	public void init(CircleLinkedList players, PlayerClass winner)
	{
		Node temp = players.getHead();
		//display the player's scores on each label
		for(JLabel label : scoreDisplayers)
		{
			String text = temp.getValue().getName() + " score is " + temp.getValue().getScore();
			label.setText(text);
			label.setFont(new Font("TimesRoman", Font.BOLD, 20));
			label.setForeground(Color.WHITE);
			temp = temp.getNext();
			centerPanel.add(label);
		}
		
		victoryText.setText(winner.getName() + " won the round !");
	}
}
