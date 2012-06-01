package com.logic;

import java.io.PrintStream;

public class Tree {
	public Node root;
	
	public Tree()
	{
		root = new Node();
	}
	public void insert(String nword,int freq)
	{
		if(Node.isword(root,nword))
		{
			return;
		}
		Node.insert(root,nword,freq);
	}

	public double getfreq(Node root,String s,int j)
	{
		Node node = root;
		char k = s.charAt(j);
			if(node.ischild[k-'a'])
			{
				
			}
		return -1.8;
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
}
