package lp24Project.controller;

//Interface used for the undo operations the user can make. Every action the player will be able to undo will implement this interface.
//the whole process (the interface plus the actions) consists in the implementation of command pattern
public interface ICommand {

	public void execute();
	
	public void undo();
}
