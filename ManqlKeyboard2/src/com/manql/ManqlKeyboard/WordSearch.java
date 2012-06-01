package com.manql.ManqlKeyboard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

import com.example.android.softkeyboard.R;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

public class WordSearch {
	private final int MAX_LEN = 30;
	private final int MAX_DB = 100;
	private final int MAX_QWERTY = 4;
	private final double QWERTY_DIST = 0.2;
	public Tree dictree;
	public Tree mytree;
	
	Context context = null;
	
	public void main(String argc[]){
		System.out.println("Read start");
		DBset();
		System.out.println("Read ok");
		init();
		state(0,0);
		state(0,0);
		state(0,0);
//		state(0,0);
		state(0,-1);
	}
	
	public WordSearch(Context context)
	{
		this.context = context;
		
		qwerty_co = new point[MAX_LEN];
		for(int i=0; i<MAX_LEN; i++)
		{
			qwerty_co[i] = new point();
		}
		
		DBset();
		init();
	}
	public void DBset()
	{
		int i=0;
		//FileInputStream fi = null;
		InputStream fi = null;
		try {
			fi = context.getResources().openRawResource(R.raw.frequency);
			//fi = new FileInputStream("frequency.txt");
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(fi);
		dictree = new Tree();
		mytree = new Tree();
		while(in.hasNext())
		{
			//Log.i("LOG", "count" + i);
			String n = in.next(); /// max length = 22
			if(n.length()>0)
				dictree.insert(n,freq_find(i));
			i++;
			
			if(i>20000)
				break;
		}
//		System.out.println(""+dictree.setfreq(dictree.root));
		
		
		//mytree.print(mytree.root);
		
	}
	private double freq_find(int i)
	{
		return 20 - Math.log((double)i+1.5);
	}
	public void go()
	{
		int i;
		for(i=0; i<pre_len; i++)
		{
			System.out.println("  "+pre[i].s+" "+pre[i].f + " " +dictree.root.getfreq(pre[i].s,0));
			
		}
		for(i=0; i<10 && i<fin.size(); i++)
		{
			System.out.println(fin.get(i).s + "  " + fin.get(i).f);
		}
		
	}
	public void init()
	{
		input_len=0;
		pre_len = 0;
		ban_len = 0;
		inild();
	}
	private point[] qwerty_co; 
	private point input;
	private int input_len;
	private String[] input_string;
	private pair[] now;
	private pair[][] pre_log;
	private int[] pre_log_len;
	private ArrayList<pair> fin;
	private pair[] pre;
	private int pre_len;
	private ArrayList<pair> temp;
	private int[] in_l;
	private pair[][] character;
	private void inild()
	{
		input = new point();
		/*
		qwerty_co = new point[MAX_LEN];
		for(int i=0; i<MAX_LEN; i++)
		{
			qwerty_co[i] = new point();
		}
		*/
		now = new pair[MAX_DB];
		pre = new pair[MAX_DB];
		pre_log = new pair[MAX_LEN][];
		pre_log_len = new int[MAX_LEN];
		ban_list = new String[MAX_DB];
		for(int i=0; i<MAX_LEN; i++)
			pre_log[i] = new pair[MAX_DB];
		fin = new ArrayList<pair>();
		temp = new ArrayList<pair>();
		
		in_l = new int[MAX_LEN];
		character = new pair[MAX_LEN][];

		input_len=0;
		pre_len = 1;
		pre[0] = new pair();
	}
	public String getfin(int i)
	{
		if(i<pre_len)
			return fin.get(i).s;
		return "";
	}
	public double getfinf(int i)
	{
		if(i<pre_len)
			return fin.get(i).f;
		return 0;
	}
	
	public void qwerty_set(point[] qt)
	{
		int i;
		for(i=0; i<26; i++)
		{
			qwerty_co[i] = new point();
			qwerty_co[i]= qt[i];
		}
	}
	public void qwerty_set(int[] x,int[] y)
	{
		int i;
		for(i=0; i<26; i++)
		{
			qwerty_co[i] = new point(x[i],y[i]);
		}
	}
	
	public ArrayList<pair> qt;
	private void getchar(point in)
	{
		int i, j;
		qt = new ArrayList<pair>();
		double sum=0;
		
		for(i=0; i<26; i++)
		{
			char dd = 'a';
			dd += i;
			String ts = Character.toString(dd);
			double tm = 1/in.dist(qwerty_co[i]);
			qt.add(new pair(ts,tm));
			sum+=tm;
		}
		Collections.sort(qt);
		sum = qt.get(0).f;
		character[input_len] = new pair[MAX_QWERTY];
		for(i=0;i<MAX_QWERTY; i++)
		{
			if(qt.get(i).f < sum*QWERTY_DIST)
				break;
			character[input_len][i] = new pair(qt.get(i).s,qt.get(i).f);
		}
		//out에 어떻게 잘 넣었다고 치자.zz
		in_l[input_len] = i;
		
		
		
	/*	for(int i=0; i<in_l[input_len]; i++)
		{
			character[input_len][i] = new pair("a",0.5);
		}
		
		if(input_len == 0)
		{
			character[input_len][0] = new pair("d",0.8);
			character[input_len][1] = new pair("t",0.3);
			character[input_len][2] = new pair("r",0.5);
			character[input_len][3] = new pair("f",0.2);
		}if(input_len == 1)
		{
			character[input_len][0] = new pair("d",0.5);
			character[input_len][1] = new pair("e",0.5);
			character[input_len][2] = new pair("h",0.5);
			character[input_len][3] = new pair("f",0.2);
		}if(input_len == 2)
		{
			character[input_len][0] = new pair("d",0.5);
			character[input_len][1] = new pair("e",0.6);
			character[input_len][2] = new pair("r",0.5);
			character[input_len][3] = new pair("f",0.2);
		}if(input_len == 3)
		{
			character[input_len][0] = new pair("d",0.5);
			character[input_len][1] = new pair("e",0.3);
			character[input_len][2] = new pair("r",0.8);
			character[input_len][3] = new pair("f",0.2);
		}
		*/
	}
	private void sunja()
	{
		int i,j,	temp_len=0 , now_len=0;

		pair tt;
		temp.clear();
		for(i=0 ; i<pre_len; i++)
		{
			for(j=0 ; j<in_l[input_len] ; j++)
			{
				tt = new pair();
			//	System.out.println("" + input_len+character[input_len][j].s+" "+character[input_len][j].f + " "+pre[i].f);
				tt.set(pre[i].s+ character[input_len][j].s,pre[i].f<0?character[input_len][j].f:pre[i].f * character[input_len][j].f );
				temp.add(tt);
			//	System.out.println(""+temp.get(temp_len).s + temp.get(0).s);
				temp_len++;
			}
		}
		Collections.sort(temp);

		pre_log_len[input_len] = pre_len;
		for(i=0; i<pre_len; i++)
			pre_log[input_len][i] = new pair(pre[i].s,pre[i].f);
		pre_len=temp_len;
		if(pre_len>MAX_DB)
			pre_len=MAX_DB;
		for(i=0 ; i<pre_len; i++)
		{
//			now[i]= new pair(temp.get(i).s,temp.get(i).f);
//			System.out.println("now is "+now[i].s + " "+now[i].f);
//			pre[i] = now[i];
			pre[i]= new pair(temp.get(i).s,temp.get(i).f);
		}
		//pre_len = now_len;
		

	}

	private void calfin()
	{
		int i, j;
		pair tt;
		fin.clear();
		for(i=0; i<pre_len; i++)
		{
			tt= new pair();
			tt.s = pre[i].s;
			tt.f = dictree.root.getfreq(pre[i].s,0);
			tt.f = tt.f * tt.f * pre[i].f;
	//		System.out.println(" tt "+tt.s+" "+now[i].f+" "+Node.getfreq(dictree.root,now[i].s) + Node.isword(dictree.root,now[i].s));
			fin.add(tt);
		}
		Collections.sort(fin);
		
	}
	public void state(int x1,int y1)
	{
		if(x1<0 || y1<0)
		{
			backsp();
			calfin();
			input_len++;
		}
		else
		{
			if(input_len<MAX_LEN)
			{
				input.set(x1,y1);
				getchar(input);
				sunja();
				calfin();
				input_len++;
			}
		}
	}

	String[] ban_list;
	int ban_len;
	int bantl;
	public void backsp(){
		int i,j;

		if(input_len==0)
		{
			input_len = -1;
			return;
		}
		else if(input_len == 1)
		{
			pre[0] = new pair();
			pre_len = 1;
			input_len = -1;
			return ;
		}
		ban_list[ban_len] = new String(pre[bantl].s);
		ban_len++;
		pre_len = pre_log_len[input_len-1];
		for(i=0 ; i<pre_len; i++)
		{
			pre[i] = pre_log[input_len-1][i];
		}
		input_len-=2;
	}
	//김일두 : 스트링 받아오는 퍼블릭 함수 생성 
	public String getString(int n)
	{
		if(fin.size()>n)
		{
			int i;
			while(n<MAX_DB-1)
			{
				for( i=0; i<ban_len; i++)
				{
					if(fin.get(n).s.compareTo(ban_list[i]) == 0)
						break;
				}
				if(i < ban_len)
				{
					n++;
					continue;
				}
				break;
			}
			bantl = n;
			return new String(fin.get(n).s);
		}
		else
		{
			bantl=0;
			return "";
		}
	}
}
