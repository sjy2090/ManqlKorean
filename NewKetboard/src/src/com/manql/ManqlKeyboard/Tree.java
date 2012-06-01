package com.manql.ManqlKeyboard;

import java.io.PrintStream;

public class Tree {
	public Node root;
	public static double  K = 0.3;
	
	public Tree()
	{
		root = new Node();
	}
	public void insert(String nword,double freq)
	{
		if(Node.isword(root,nword))
		{
			return;
		}
		Node.insert(root,nword,freq);
	}

	public void print(Node root)
	{
		Node node = root;
//		System.out.print(node.word + " - ");
		if(node.myrank>0)
			System.out.println(node.word + " "+node.myrank);
//		System.out.println();
		for(int i=0;i<26; i++)
		{
			if(node.ischild[i])
			{
				print(node.child[i]);
			}
		}
	}
	public double setfreq(Node root)
	{
		Node node = root;
		double dd=0;
		int cnt=0;
		for(int i=0;i<26; i++)
		{
			if(node.ischild[i])
			{
				cnt++;
				dd +=setfreq(node.child[i]);
			}
		}
		if(cnt == 0)
			return node.myrank;
		node.myrank =  K*(node.myrank) + (1-K)*(dd/cnt);
		return node.myrank;
	}
}
