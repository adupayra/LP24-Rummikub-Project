package lp24Project;

import lp24Project.model.PlayerClass;

//Element of the circle linked list (I could've make it generic, but in that case it was kind of specific to the player class so I preferred not to do it generic)
public class Node {

	private Node next;
	private Node previous;
	private PlayerClass value;
	
	public Node(PlayerClass value)
	{
		this.value = value;
	}
	public Node getNext()
	{
		return next;
	}
	
	public void setNext(Node next)
	{
		this.next = next;
	}
	
	public Node getPrevious()
	{
		return previous;
	}
	
	public void setPrevious(Node previous)
	{
		this.previous = previous;
	}
	
	public PlayerClass getValue()
	{
		return value;
	}
	
	public void setValue(PlayerClass value)
	{
		this.value = value;
	}
	
	
}
