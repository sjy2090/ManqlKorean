package com.logic;

public class Node {
	public int myrank;
	public String word;
	public boolean[] ischild;
	public Node[] child;
	
	public Node()
	{
		myrank = 0;
		ischild = new boolean[26];
		child = new Node[26];
		for(int i=0; i<26; i++)
		{
			ischild[i] = false;
		}
		word = "";
	}
	public static boolean isword(Node root, String nword)
	{
		int i=0, k;
		char j;
		Node node = root;
		int len = nword.length();
		while(i<len){
			j = nword.charAt(i);
			k = j-'a';
			if(k<0 || k>25)
			{
				i++;
				continue;
			}
			if(node.ischild[k])
			{
				node = node.child[k];
				i++;
			}
			else
				break;
		}
		return (i==len);
	}
	public static void insert(Node root,String nword, int freq)
	{
		int i=0, k;
		char j;
		Node node = root;
		int len = nword.length();
		while(i<len){
			j = nword.charAt(i);
			k = j-'a';
			if(k<0 || k>25)
			{
				i++;
				continue;
			}
			if(node.ischild[k])
			{
				node = node.child[k];
			}
			else
			{
				node.child[k] = new Node();
				node.ischild[k] = true;
				node.child[k].word = node.word + j;
				node = node.child[k];
			}
			i++;
		}
		node.myrank = freq;
	}

}
