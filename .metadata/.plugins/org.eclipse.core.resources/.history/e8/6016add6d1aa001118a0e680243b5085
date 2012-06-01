package com.manql.ManqlKeyboard;

public class pair implements Comparable<pair>{
	String s;
	double f;
	public pair()
	{
		s = "";
		f = -1;
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
	@Override
	public int compareTo(pair obj) {
		pair arg0 = (pair)obj;
		if(this.f>arg0.f){
			return -1;
		}
		else if(this.f<arg0.f){
			return 1;		
		}
		return 0;
	}
}