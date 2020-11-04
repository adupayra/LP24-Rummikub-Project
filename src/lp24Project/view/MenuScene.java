package lp24Project.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MenuScene extends JPanel {
	//Player selection Frame
	private JFrame secondWindow = new JFrame();
	private JButton secondWindowButton = new JButton("Play");
	private JRadioButton player2 = new JRadioButton("2 Players");
	private JRadioButton player3 = new JRadioButton("3 Players");
	private JRadioButton player4 = new JRadioButton("4 Players");
	
	//Menu UI
	private JLabel title = new JLabel("RUMMIKUB");
	private JPanel northPanel = new JPanel();
	private BorderLayout borderLayout = new BorderLayout();
	private JPanel centerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
	private JButton choosePlayerButton = new JButton("Choose the number of Players");
	private JButton rulesButton = new JButton("Rules");
	private JButton uselessButton = new JButton("Useless Button");
	private JButton exitButton = new JButton("RageQuit");
	private static final Color LABELCOLOR = Color.white;
	private static final Color PANELCOLOR = new Color(50, 190, 220);
	private static final Color BUTTONCOLOR = new Color(0, 143, 254);

	//Easter egg UI
	private JLabel uselessButtonLabel = new JLabel("I guess that this will be my easter egg");
	private Thread thread1;
	private Thread thread2;
	private boolean isDisplayed = true;
	public static int clickCount = 0;
	
////////////////////////////////////////MENU GETTERS///////////////////////////////////
	public JButton getChoosePlayerButton()
	{
		return choosePlayerButton;
	}
	
	public JButton getRulesButton()
	{
		return rulesButton;
	}
	
	public JButton getUselessButton()
	{
		return uselessButton;
	}
	
	public JButton getExitButton()
	{
		return exitButton;
	}
	
	public JButton getSecondWindowButton()
	{
		return secondWindowButton;
	}
	
	public void setInternalFrameVisible(boolean bool)
	{
		secondWindow.setVisible(bool);
	}
	
//////////////////////////////////END OF MENU GETTERS//////////////////////////////
	
	public MenuScene()
	{
		//Display UI
		this.setLayout(borderLayout);
		
		JPanel northPanel = new JPanel();
		this.add(northPanel, BorderLayout.NORTH);
		northPanel.add(title);
		title.setFont(new Font("TimesRoman", Font.BOLD, 50));
		this.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setBorder(BorderFactory.createTitledBorder("Menu Buttons"));
		centerPanel.add(choosePlayerButton);
		centerPanel.add(rulesButton);
		centerPanel.add(uselessButton);
		centerPanel.add(exitButton);
		centerPanel.add(uselessButtonLabel);
		uselessButtonLabel.setVisible(false);
		title.setForeground(LABELCOLOR);
		centerPanel.setBackground(PANELCOLOR);
		northPanel.setBackground(PANELCOLOR);
		choosePlayerButton.setBackground(BUTTONCOLOR);
		rulesButton.setBackground(BUTTONCOLOR);
		uselessButton.setBackground(BUTTONCOLOR);
		exitButton.setBackground(BUTTONCOLOR);
		choosePlayerButton.setForeground(LABELCOLOR);
		rulesButton.setForeground(LABELCOLOR);
		uselessButton.setForeground(LABELCOLOR);
		exitButton.setForeground(LABELCOLOR);
		uselessButtonLabel.setForeground(LABELCOLOR);
		initSecondWindow();
		
		//Easter egg management
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e)
			{
				isDisplayed = false;
			}
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				isDisplayed = true;
			}
		});
		
		thread1 = new Thread(new Runnable() {
			@Override
			public void run()
			{
				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(isDisplayed)
					partyHard();
			}
		});
		thread1.start();
	}
	
	
	private void initSecondWindow()
	{
		JPanel panel = new JPanel();
		secondWindow.setContentPane(panel);
		secondWindow.setSize(200,150);
		secondWindow.setLocationRelativeTo(null);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(player2);
		buttonGroup.add(player3);
		buttonGroup.add(player4);
		panel.add(player2);
		panel.add(player3);
		panel.add(player4);
		panel.add(secondWindowButton);
		secondWindow.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e)
			{
				player2.setSelected(true);
			}
		});
		secondWindow.setVisible(false);
		
		
		
	}
	
	//get the choice the player made with the radio buttons
	public int getNumberOfPlayers()
	{
		int numberOfPlayers = 0;
		String displayedMessage = "";
		if(player2.isSelected())
		{
			displayedMessage = "2 players are playing";
			numberOfPlayers = 2;
		}
		else if(player3.isSelected())
		{
			displayedMessage = "3 players are playing";
			numberOfPlayers = 3;
		}
		else if(player4.isSelected())
		{
			displayedMessage = "4 players are playing";
			numberOfPlayers = 4;
		}
		 JOptionPane.showMessageDialog(MenuScene.this, displayedMessage);
		 secondWindow.setVisible(false);
		 return numberOfPlayers;
	}
	
	
/////////////////////////////////////EASTER EGG FUNCTIONS//////////////////////////
	private void partyHard()
	{
		disableButtons();
		thread2 = new Thread(new Runnable(){
			@Override
			public void run()
			{
				titleThread();
			}});
		thread2.start();
		panelThread();
		
	}
	
	private void disableButtons()
	{
		choosePlayerButton.setEnabled(false);
		rulesButton.setEnabled(false);
		exitButton.setEnabled(false);
		uselessButton.setEnabled(false);
	}
	
	private void titleThread()
	{
		title.setText("PARTYHARD");
		while(true)
		{
			title.setForeground(generateRandomColor());
			title.setVisible(!title.isVisible());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void panelThread()
	{
		long time = System.currentTimeMillis();
		while(System.currentTimeMillis() < time + 1000)
		{
			
			setRandomColors();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	private void setRandomColors()
	{
		
		centerPanel.setBackground(generateRandomColor());
		northPanel.setBackground(generateRandomColor());
		choosePlayerButton.setBackground(generateRandomColor());
		rulesButton.setBackground(generateRandomColor());
		uselessButton.setBackground(generateRandomColor());
		exitButton.setBackground(generateRandomColor());
		choosePlayerButton.setForeground(generateRandomColor());
		rulesButton.setForeground(generateRandomColor());
		uselessButton.setForeground(generateRandomColor());
		exitButton.setForeground(generateRandomColor());
		uselessButtonLabel.setForeground(generateRandomColor());
	}
	private Color generateRandomColor()
	{
		Random rand = new Random();
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}
	
	public void displayUselessLabel()
	{
		uselessButtonLabel.setVisible(true);
		uselessButtonLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 5));
	}
}
