public class TreeObject implements Comparable<TreeObject>
{
	Long data;
	private int frequency;
	
	public TreeObject(long key)
	{
		this.data = key;
		this.frequency = 1;
	}

	public TreeObject(long key, int frequency)
	{
		this.data = key;
		frequency = 1;
	}

	public void incrementFrequency()
	{
		frequency++;
	}
	public int getFrequency()
	{
		return frequency;
	}
	public Long getKey()
	{
		return data;
	}
	public boolean equals(TreeObject ob)
	{
		if(data.equals(ob.getKey()))
		{
			return true;
		}
		return false;
	}
	public String toString()
	{
		String output = "";
		for(int i = (output.length()*2 - 2); i > 0; i-=2) //length*2 because two bits per letter
		{
			long temp = ((long)data >> i) & 3; //extracts the last two bits
			if(temp == 0) //0 = 00 = A
			{
				output += "A";
			}
			else if(temp == 1) //1 = 01 = C
			{
				output += "C";
			}
			else if(temp == 2) //2 = 10 = G
			{
				output += "G";
			}
			else if(temp == 3) //3 = 11 = T
			{
				output += "T";
			}
		}
		output += ": " + frequency; 
		return output;
	}
	/**
	* To compare two objects. This will be the DNA substring long.
	* Refactor when necessary.
	*/
	@Override
	public int compareTo(TreeObject d)
	{
		if(data > d.getKey())
		{
			return 1;
		}
		if(data < d.getKey())
		{
			return -1;
		}
		else return 0;
	}
}