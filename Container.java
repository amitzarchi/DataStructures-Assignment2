
//Don't change the class name
public class Container {
	private Point data;//Don't delete or change this field;
	public Container prev;
	public Container next;
	public Container twin;

	public Container(Point data)
	{
		this.data = data;
		this.prev = null;
		this.next = null;
		this.twin = null;

	}
	
	//Don't delete or change this function
	public Point getData()
	{
		return data;
	}

	public Container getPrev()
	{
		return prev;
	}
	public void setPrev(Container prev)
	{
		this.prev = prev;
	}

	public Container getNext()
	{
		return next;
	}
	public void setNext(Container next)
	{
		this.next = next;
	}
	public Container getTwin() 
	{
		return twin;
	}
	public void setTwin(Container twin)
	{
		this.twin = twin;
	}

}
