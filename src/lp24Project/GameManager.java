package lp24Project;

import lp24Project.controller.Controller;
import lp24Project.model.Model;
import lp24Project.view.View;

//class used to start the game
public class GameManager {

	//Singleton of the gameManager
	public static final GameManager INSTANCE = new GameManager();
	
	private GameManager()
	{
		
	}
	
	public void start()
	{
		View.getInstance();
		Model.getInstance();
		Controller.getInstance();
	}
}
