package com.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.util.Scanner;

class point{
	double x;
	double y;
	public point()
	{
		x=y=0;
	}
	public point(int nx, int ny)
	{
		x = nx;
		y = ny;
	}
	public point(double nx, double ny)
	{
		x = nx;
		y = ny;
	}
	public void set(int nx, int ny)
	{
		x = nx;
		y = ny;
	}
	public void set(double nx, double ny)
	{
		x = nx;
		y = ny;
	}
}
class pair{
	String s;
	double f;
	public pair()
	{
		s = null;
		f = 1;
	}
	public pair(String nc, double nf)
	{
		s = nc;
		f = nf;
	}
	public void set(String nc, double nf)
	{
		s = nc;
		f = nf;
	}
}
public class Fail_Main {
	private static final int MAX_LEN = 30;
	public static Tree dictree;
	public static Tree mytree;
	public static void main(String argc[]){

		int i=0;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream("frequency.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(fi);
		dictree = new Tree();
		mytree = new Tree();
		while(in.hasNext())
		{
			String n = in.next(); /// max length = 22
			if(n.length()>0)
				dictree.insert(n,freq_find(i));
			i++;
		}
		
		System.out.println("Read ok");
		
		
		//mytree.print(mytree.root);
		
	}
	private static int freq_find(int i)
	{
		return 100000 - i;
	}
	
	
	
	public static void go()
	{
		
	}
	public static void inil()
	{
		input_len=0;
		pre_len = 0;
	}
	public static point[] qwerty_co; 
	public static point input;
	public static int input_len;
	public static String[] input_string;
	public static pair[] now;
	public static pair[] fin;
	public static pair[] pre;
	public static int pre_len;
	public static pair[] temp;
	public static int[] in_l;
	public static pair[][] character;
	public static void inild()
	{
		input = new point();
		qwerty_co = new point[MAX_LEN];
		for(int i=0; i<MAX_LEN; i++)
		{
			qwerty_co[i] = new point();
		}
		now = new pair[50];
		pre = new pair[50];
		fin = new pair[50];
		temp = new pair[200];
		
		in_l = new int[MAX_LEN];
		character = new pair[MAX_LEN][];

		input_len=0;
		pre_len = 1;
	}
	public static void getchar(point in)
	{
		//out에 어떻게 잘 넣었다고 치자.zz
		in_l[input_len] = 3;
		character[input_len] = new pair[in_l[input_len]];
		
		
		
	/*	for(int i=0; i<in_l[input_len]; i++)
		{
			character[input_len][i] = new pair("a",0.5);
		}*/
		
		character[input_len][0] = new pair("a",0.5);
		character[input_len][1] = new pair("s",0.3);
		character[input_len][2] = new pair("z",0.2);
		
	}
	public static void sunja()
	{
		
	}
	
	public static void calfin()
	{
		
	}
	public static void state(int x1,int y1,int len)
	{
		if(len<-1)
		{
			go();
			inil();
		}
		else
		{
			if(len<MAX_LEN)
			{
				
				input.set(x1,y1);
				getchar(input);
				sunja();
				calfin();
				input_len ++;
			}
		}
	}
}
