package com.manql.ManqlKeyboard;

public class point{
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
	public double dist(point n)
	{
		double tx = (x - n.x);
		double ty = (y - n.y);
		if(tx == ty && ty==0)
			return 0.99;
		return Math.sqrt(tx*tx+ty*ty);
	}
}
