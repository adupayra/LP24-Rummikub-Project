package lp24Project.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import lp24Project.CircleLinkedList;
import lp24Project.model.PlayerClass;

public class View extends JPanel {
	//Singleton of the view instance
	private static View instance = new View();
	
	//Layout of the UI
	private CardLayout cl = new CardLayout();
	
	//The different states of the game
	private GameScene gameScene;
	private MenuScene menuScene;
	private EndGameScene endGameScene;
	
	//The elements that will constitute the game scene
	private Board board;
	private GameSlot[] gameSlots = new GameSlot[28];
	private Slot[] slots;
	private FeaturePanel featurePanel;
	
	//Constructor that will only happen once thanks to the singleton pattern
	private View()
	{
		setLayout(cl);
		initView();	
	}
	
	//Lazy instantation of the view classes' singleton and getter of that singleton
	public static View getInstance()
	{
		return instance;
	}
	
	
	public GameScene getGameScene()
	{
		return gameScene;
	}
	
	public MenuScene getMenuScene()
	{
		return menuScene;
	}
	
	public EndGameScene getEndGameScene()
	{
		return endGameScene;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public GameSlot[] getGameSlots()
	{
		return gameSlots;
	}
	
	public Slot[] getSlots()
	{
		return slots;
	}
	
	public FeaturePanel getFeaturePanel()
	{
		return featurePanel;
	}
	
	
//////////////////////////////(RE) INITIALIZATION OF THE VIEW//////////////////////////////
	
	private void initView()
	{
		//We instantiate the element which will remain between the rounds of the game
		menuScene = new MenuScene();
		add(menuScene, "1");
		initWindow();
		initGameSlotsAndSlots();
		initFeaturePanel();
		initBoard();
		gameScene = new GameScene(board);
		add(gameScene, "2");
		loadMenuScene();
	}
	
	//instantiation/initialization of variables which couldn't be instantiated before the user chose the number of players 
	public void delayedInitView(CircleLinkedList players)
	{
		endGameScene = new EndGameScene(players.getSize());
		add(endGameScene, "3");
		gameScene.initRacks(players);
		loadGameScene();
	}
	
	
	private void initWindow()
	{
		JFrame wind = new JFrame();
		//wind.setSize(new Dimension(1200,600));
		wind.setExtendedState(JFrame.MAXIMIZED_BOTH);
		wind.setLocationRelativeTo(null);
		wind.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wind.setContentPane(this);
		wind.setVisible(true);
	}	
	
	private void initBoard()
	{
		board = new Board(gameSlots);
		for(int i = 0; i < gameSlots.length; i++)
			board.add(gameSlots[i]);
		board.add(featurePanel);
	}
	
	private void initFeaturePanel()
	{
		featurePanel = new FeaturePanel();
	}
	
	private void initGameSlotsAndSlots()
	{
		slots = new Slot[gameSlots.length * 5];
		for(int i = 0; i < gameSlots.length * 5; i+=5)
		{
			gameSlots[i/5] = new GameSlot();
			for(int j = 0; j <  5; j++)
			{
				slots[i+j] = new Slot(gameSlots[i/5]);
				gameSlots[i/5].add(slots[i+j]);
				gameSlots[i/5].setSlot(slots[i+j], j);
			}
		}
	}
	
	
	public void loadGameScene()
	{
		Music.INSTANCE.loopPlaying();
		cl.show(this, "2");
	}
	
	public void loadMenuScene()
	{
		cl.show(this, "1");
	}
	
	public void loadEndGameScene(CircleLinkedList players, PlayerClass winner)
	{
		Music.INSTANCE.setLoopMusic(false);
		cl.show(this, "3");
		endGameScene.init(players, winner);
	}
	
}
