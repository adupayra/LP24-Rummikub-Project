package lp24Project;

import java.util.List;

import lp24Project.model.PlayerClass;
import lp24Project.model.Tile;

//Circular linked list used for the players turn (in order to move to the next turn we just have to get to the next node of the linked list)
public class CircleLinkedList{

	//One reference to the head and one to the tail because I often needed both
	private Node head;
	private Node tail;
	private int size;
	
	public int getSize()
	{
		return size;
	}
	
	public Node getHead()
	{
		return head;
	}
	
	public Node getTail()
	{
		return tail;
	}
	
	public boolean isEmpty()
	{
		return head == null;
	}
	
	public void moveToNext(List<Tile> tilesToUnpossess)
	{
		tail = head;
		updatePossessedTiles(tilesToUnpossess);
		head = head.getNext();
		setCurrentPlayer();
	}
	
	//when changing turn, we know that the current player is the head, and that the last player to play was the tail of the list
	private void setCurrentPlayer()
	{
		for(Tile tile : tail.getValue().getTiles())
		{
			tile.setEnabled(false);
		}
		for(Tile tile : head.getValue().getTiles())
		{
			tile.setEnabled(true);
		}
	}
	
	public void addNode(PlayerClass value)
	{
		Node newNode = new Node(value);
		if(isEmpty())
		{
			head = newNode;
			tail = newNode;
		}
		else
		{
			newNode.setPrevious(tail);
			newNode.setNext(head);
			tail.setNext(newNode);
			head.setPrevious(newNode);
			tail=newNode;
		}
		size++;
	}
	
	
	private void updatePossessedTiles(List<Tile> tilesToUnPossess)
	{
		for(Tile tile : tilesToUnPossess)
		{
			head.getValue().removeTile(tile);
		}
	}
}
