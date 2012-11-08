package javaclass.pkg;
import java.io.*;

public class Progams {

	/**
	 * @param args
	 */
	private int no;
	private String name;
	private int scor;
	static int a=100;
	String S_Name0;
	String S_Scor0;
	Progams (String n,int s)
	{
	no =a++;
	name=n;
	scor=s;
	}
	void setscor(int s)
	{
	scor=s;
	}
	int getscor()
	{
	return scor;
	}
	String getName()
	{
	 return name;
	}
	void write()
	{
	System.out.println(no+name+scor);
	}

}

   class Run
{
	public static void main (String[]args)// throws IOException
	{
	DataInputStream d= new DataInputStream(System.in);
	Progams st[]=new Progams[10];
	for(int i=0;i<10;i++)
	{

	System.out.println(" can U plz Enter ur Nmae & ur scor");
	
	try {
		String S_Scor;
		String S_Name;
		S_Scor = d.readLine();

		S_Name = d.readLine();
	
	int a =Integer.parseInt(S_Scor);
	st[i]=new Progams(S_Name,a);
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	for(int j=0;j<10;j++)
	{
	if(st[j].getscor()>50)
	System.out.println(st[j].getName());
	int l=0;
	int max=st[0].getscor();
	for(int i1=1; i1<10;i1++)
	 l=st[i1].getscor();
	//l++;
	
	System.out.println(l);
	}
	}
	}
	}
	
	}

	