package M;

public class Node {
	Node right ; // right child
	Node left ; // left child
	String str ;
	int num ;
	
	Node(String str, int num)
	{
		this.str = str ;
		this.num = num ;
		right = null ;
		left = null ;
	}
	
	Node(String str, int num, Node right, Node left)
	{
		this.str = str ;
		this.num = num ;
		this.right = right ;
		this.left = left ;
	}
	
	void setStr(String s)
	{
		str = s ;
	}
	
	void setNum(int n)
	{
		num = n ;
	}
	
	String getStr()
	{
		return str ;
	}
	
	int getNum()
	{
		return num ;
	}
	
	void setRight(Node r)
	{
		right = r ;
	}
	
	void setLeft(Node l)
	{
		left = l ;
	}
	
	Node getRight()
	{
		return right ;
	}
	
	Node getLeft()
	{
		return left ;
	}
}
