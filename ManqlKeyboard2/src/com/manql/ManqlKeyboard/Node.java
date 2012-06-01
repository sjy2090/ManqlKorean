package com.manql.ManqlKeyboard;

public class Node {
	public double myrank;
	public static double FLOAT_ZERO = 0.000000001;
	public boolean[] ischild;
	public Node[] child;
	
	public Node()
	{
		myrank = -1;
		ischild = new boolean[26];
		child = new Node[26];
		for(int i=0; i<26; i++)
		{
			ischild[i] = false;
		}
	}
	public boolean isword(String nword,int l)
	{
		int  k;
		char j;
		int len = nword.length();
		if(len <=l+1)
			return (myrank >= -FLOAT_ZERO);
		j = nword.charAt(l);
		if(j>='A' && j<='Z')
			k = j -'A';
		else
			k = j-'a';
		if(k<0 || k>25)
			return true;
		else if(ischild[k])
		{
			return child[k].isword(nword,l+1);
		}		
		return false;
	}
	public void insert(String nword, double freq,int l)
	{
		int k;
		char j;
		int len = nword.length();
		if(len <= l+1)
		{
			myrank = freq;
			return;
		}
		j = nword.charAt(l);
		if(j>='A' && j<='Z')
			k = j -'A';
		else
			k = j - 'a';
		if(k<0 || k>25)
		{
			return;
		}
		else if(ischild[k])
		{
			child[k].insert(nword,freq,l+1);
		}
		else
		{
			child[k] = new Node();
			ischild[k] = true;
			child[k].insert(nword,freq,l+1);
		}
		return;
	}
	public double getfreq(String nword,int l)
	{
		int k;
		char j;
		int len = nword.length();
		if(len <= l+1)
		{
			if(myrank<FLOAT_ZERO)
				return 0;
			return myrank;			
		}
		j = nword.charAt(l);
		if(j>='A' && j<='Z')
			k = j -'A';
		else
			k = j-'a';
		if(k<0 || k>25)
		{
			return child[k].getfreq(nword, l+1);
		}
		else if(ischild[k])
		{
			return child[k].getfreq(nword, l+1);
		}
		else
		{
			return 0;
			/*
			child[k] = new Node();
			ischild[k] = true;
			return child[k].getfreq(nword,l+1);*/
		}
		
	}

}
