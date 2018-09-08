package b;

public class MyExample
{
	private boolean firstBool;

	private boolean secondBool = true;

	private String singleString;

	public boolean FirstBool()
	{
		return firstBool;
	}

	public void setFirstBool(boolean firstBool)
	{
		this.firstBool = firstBool;
	}

	public boolean secondBool()
	{
		return secondBool;
	}

	public void setSecondBool(boolean secondBool)
	{
		this.secondBool = secondBool;
	}

	public String getSingleString()
	{
		return singleString;
	}

	public void setSingleString(String singleString)
	{
		this.singleString = singleString;
	}
}
