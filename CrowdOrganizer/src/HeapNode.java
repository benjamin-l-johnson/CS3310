
public class HeapNode {
	private String name;
	private int value;
	
	
	public HeapNode(String name, int value)
	{
		this.name = name;
		this.value = value;
		
	}


	public int getPriorityValue() {
		return value;
	}


	public void setPriorityValue(int value) {
		this.value = value;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
}
