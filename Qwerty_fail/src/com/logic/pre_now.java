/*
package com.logic;

public class pre_now {
	public static point[] qwerty_co = Fail_Main.qwerty_co; 
	public static point input=Fail_Main.input;
	public static int input_len=Fail_Main.input_len;
	public static String[] input_string=Fail_Main.input_string;
	public static pair[] now=Fail_Main.now;
	public static pair[] fin=Fail_Main.fin;
	public static pair[] pre=Fail_Main.pre;
	public static int pre_len=Fail_Main.pre_len;
	public static pair[] temp=Fail_Main.temp;
	public static int[] in_l=Fail_Main.in_l;
	public static pair[][] character=Fail_Main.character;
	public static void main(String[] arvc)
	{
		int i,j,k,temp_len=0 , now_len=0;
		
		for(i=0 ; i<pre_len; i++)
		{
			for(j=0 ; j<in_l[input_len] ; j++)
			{
				temp[temp_len++].f = pre[i].f * character[input_len][j].f;
				temp[temp_len].s=pre[i].s;
				temp[temp_len].s+= character[input_len][j].s.charAt(0);
			}
		}
		
		now_len=temp_len;
		if(now_len>50)
			now_len=50;
		for(i=0 ; i<now_len; i++)
		{
			now[i]=temp[i];
		}
	}
}

 class backsp {

	public static point[] qwerty_co = Fail_Main.qwerty_co; 
	public static point input=Fail_Main.input;
	public static int input_len=Fail_Main.input_len;
	public static String[] input_string=Fail_Main.input_string;
	public static pair[] pre=Fail_Main.pre;
	public static int pre_len=Fail_Main.pre_len;
	public static int[] in_l=Fail_Main.in_l;
	public static pair[][] character=Fail_Main.character;
	public static void main(String[] arvc)
	{
		int i,j,k,temp_len=0 , now_len=0;
		
		for(i=0 ; i<pre_len; i++)
		{
			for(j=0;j<in_l[input_len-1];j++)
			{
				if(pre[i].s.substring(input_len-1).compareTo(character[input_len-1][j].s) == 0)
				{
					pre[i].f /= character[input_len-1][j].f;
					pre[i].s = pre[i].s.substring(0,input_len-1);
				}
			}
		}
		input_len-=2;
	}
}
 */