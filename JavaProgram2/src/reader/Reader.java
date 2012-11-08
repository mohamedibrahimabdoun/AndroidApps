package reader;

import java.io.DataInputStream;

public class Reader {

	String getString()
	{
		String str=null;
		try
		{
			DataInputStream dis=new DataInputStream(System.in);
			System.out.println("Pls Enter Sting");
			str=dis.readLine();
		}
		catch(Exception e){}
		return str;
	}
	int getInt()
	{
		int num=0;
		try
		{
			DataInputStream dis=new DataInputStream(System.in);
			System.out.println(" Enter your Integer No.");
			num=Integer.parseInt(dis.readLine());
		}
		catch(Exception e){}
		return num;
	}
	float getFloat()
	{
		float num=0;
		try
		{
			DataInputStream dis=new DataInputStream(System.in);
			System.out.println("Pls Enter Float");
			num=Float.parseFloat(dis.readLine());
		}
		catch(Exception e){}
		return num;
	}
	public static void main(String a[])
	{
		Reader r=new Reader();
		//System.out.println("Your Sting is: "+r.getString());
		//System.out.println("Your Integer is: "+r.getInt());
		//System.out.println("Your Float is: "+r.getFloat());
		int x=r.getInt();
		int y=r.getInt();
		int sum=r.Sumation(x,y);
		System.out.println("Your Sumation is: "+sum);
	}
	int Sumation(int num1,int num2)
	{
		return num1+num2;
	}
	
	
}
