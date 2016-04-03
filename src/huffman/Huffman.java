package huffman;

import java.io.*;

/*
****************************************************************
*  Compilation:  javac Huffman.java
*  Execution:    java Huffman - < input.txt   (compress)
*  Execution:    java Huffman + < input.txt   (expand)
*  Dependencies: BinaryIn.java BinaryOut.java
*  Data files:   http://algs4.cs.princeton.edu/55compression/abra.txt
*                http://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
*
*  Compress or expand a binary input stream using the Huffman algorithm.
*
*  % java Huffman - < abra.txt | java BinaryDump 60
*  010100000100101000100010010000110100001101010100101010000100
*  000000000000000000000000000110001111100101101000111110010100
*  120 bits
*
*  % java Huffman - < abra.txt | java Huffman +
*  ABRACADABRA!
*
*************************************************************************/

public class Huffman {

   // alphabet size of extended ASCII
  // private static final int R = 256;//10;//81;//256; ERA 256 y lo he pasado a 9. 5 es para PR
   private  static int R = 256;//10;//81;//256; ERA 256 y lo he pasado a 9. 5 es para PR
   
   
   public Huffman(int number_of_symbols)
   {
	   R=number_of_symbols;
   }
   
   // Huffman trie node
   private static class Node implements Comparable<Node> {
       private final char ch;
       private final int freq;
       private final Node left, right;

       Node(char ch, int freq, Node left, Node right) {
           this.ch    = ch;
           this.freq  = freq;
           this.left  = left;
           this.right = right;
       }

       // is the node a leaf node?
       private boolean isLeaf() {
           assert (left == null && right == null) || (left != null && right != null);
           return (left == null && right == null);
       }

       // compare, based on frequency
       public int compareTo(Node that) {
           return this.freq - that.freq;
       }
   }


   // compress bytes from standard input and write to standard output
   
   public static int[] getTranslateCodes(int[] freq)
   {   
	   int[] newcodes=new int[R];
	   Node root = buildTrie(freq);

       // build code table
       String[] st = new String[R];
       buildCode(st, root, "");

       // print trie for decoder
       writeTrie(root);  
       int len_total=0;
       for (int i=0;i<R;i++)
       {
    	   //System.out.println("code:"+i+" ="+st[input[i]]);
    	 //  System.out.println(" "+input[i]);
    	   //System.out.println("code:"+(i+1)+" ="+st[i+1]);
    	   //System.out.println("code:"+(i));//+" ="+st[i]);
    	   System.out.println("code:"+(i)+" ="+st[i]);
    	   if (st[i]!=null) newcodes[i]=st[i].length();
    	   if (st[i]!=null)  len_total+=st[i].length()*freq[i];
       }
       System.out.println("len total:"+len_total+" bits");
       System.out.println("bpp:"+(float)len_total/(512f*512f));
       
       return newcodes;
   }
   public static int getLenTranslateCodes(int[] freq)
   {   
	   int[] newcodes=new int[R];
	   Node root = buildTrie(freq);

       // build code table
       String[] st = new String[R];
       buildCode(st, root, "");

       // print trie for decoder
       writeTrie(root);  
       int len_total=0;
       for (int i=0;i<R;i++)
       {
    	   //System.out.println("code:"+i+" ="+st[input[i]]);
    	 //  System.out.println(" "+input[i]);
    	   //System.out.println("code:"+(i+1)+" ="+st[i+1]);
    	   //System.out.println("code:"+(i));//+" ="+st[i]);
    	   //System.out.println("code:"+(i)+" ="+st[i]);
    	   if (st[i]!=null) {newcodes[i]=st[i].length(); 
    	   System.out.println(" code:"+i+" len:"+st[i].length());
    	   }
    	   
    	   if (st[i]!=null)  len_total+=st[i].length()*freq[i];
       }
     //  System.out.println("len total:"+len_total+" bits");
     //  System.out.println("bpp:"+(float)len_total/(512f*512f));
       
       return len_total;
   }
   
   public static String[] getTranslateCodesString(int[] freq) {   
	   int[] newcodes=new int[10];
	   Node root = buildTrie(freq);

       // build code table
       String[] st = new String[R];
       buildCode(st, root, "");

       // print trie for decoder
       writeTrie(root);  

       return st;
   }
   
   
   public static void compress() {
       // read the input
	   System.out.println(" entrada en compress()");
	   //File mifile=new File("./resultados/hops.txt");
	  // FileInputStream fi=new FileInputStream(mifile);
	   //DataInputStream di=new DataInputStream(fi);
       //String s = BinaryStdIn.readString();
	   try{
	   //BufferedReader br=new BufferedReader(new FileReader("./resultados/codes.txt"));
	   //BufferedWriter bw=new BufferedWriter(new FileWriter("./resultados/codes_compress.txt"));
	   
		   BufferedReader br=new BufferedReader(new FileReader("./results/codes.txt"));
		   BufferedWriter bw=new BufferedWriter(new FileWriter("./results/codes_compress.txt"));
		   
	   //String s = br.readString();
	   String s = br.readLine();
       char[] input = s.toCharArray();
       
       // tabulate frequency counts
       int[] freq = new int[R];
       for (int i = 0; i < input.length; i++)
           freq[input[i]]++;
       
       System.out.println(" leida entrada");
       // build Huffman trie
       Node root = buildTrie(freq);

       // build code table
       String[] st = new String[R];
       buildCode(st, root, "");

       // print trie for decoder
       writeTrie(root);

       // print number of bytes in original uncompressed message
       System.out.println("longitud inicial:"+input.length);
       System.out.println("tabla de codigos");
       for (int i=0;i<10;i++)
       {
    	   //System.out.println("code:"+i+" ="+st[input[i]]);
    	 //  System.out.println(" "+input[i]);
    	   System.out.println("code:"+(i+1)+" ="+st[i+'1']);
    	   
       }
       BinaryStdOut.write(input.length);

       // use Huffman code to encode input
       for (int i = 0; i < input.length; i++) {
           String code = st[input[i]];
           for (int j = 0; j < code.length(); j++) {
               if (code.charAt(j) == '0') {
                   //BinaryStdOut.write(false);
                   bw.write('0');
                  // System.out.println(" "+input[i]+" ="+code);
               }
               else if (code.charAt(j) == '1') {
                  // BinaryStdOut.write(true);
            	   bw.write('1');
               }
               else throw new IllegalStateException("Illegal state");
           }
       }

       // close output stream
       BinaryStdOut.close();
       br.close();
       bw.close();
	   }catch(Exception e){ System.out.println("horror");}
   }

   // build the Huffman trie given frequencies
   private static Node buildTrie(int[] freq) {

       // initialze priority queue with singleton trees
       MinPQ<Node> pq = new MinPQ<Node>();
       for (char i = 0; i < R; i++)
           if (freq[i] > 0)
               pq.insert(new Node(i, freq[i], null, null));

       // merge two smallest trees
       while (pq.size() > 1) {
           Node left  = pq.delMin();
           Node right = pq.delMin();
           Node parent = new Node('\0', left.freq + right.freq, left, right);
           pq.insert(parent);
       }
       return pq.delMin();
   }


   // write bitstring-encoded trie to standard output
   private static void writeTrie(Node x) {
       if (x.isLeaf()) {
           BinaryStdOut.write(true);
           BinaryStdOut.write(x.ch, 8);
           return;
       }
       BinaryStdOut.write(false);
       writeTrie(x.left);
       writeTrie(x.right);
   }

   // make a lookup table from symbols and their encodings
   private static void buildCode(String[] st, Node x, String s) {
       if (!x.isLeaf()) {
           buildCode(st, x.left,  s + '0');
           buildCode(st, x.right, s + '1');
       }
       else {
           st[x.ch] = s;
       }
   }


   // expand Huffman-encoded input from standard input and write to standard output
   public static void expand() {

       // read in Huffman trie from input stream
       Node root = readTrie(); 

       // number of bytes to write
       int length = BinaryStdIn.readInt();

       // decode using the Huffman trie
       for (int i = 0; i < length; i++) {
           Node x = root;
           while (!x.isLeaf()) {
               boolean bit = BinaryStdIn.readBoolean();
               if (bit) x = x.right;
               else     x = x.left;
           }
           BinaryStdOut.write(x.ch, 8);
       }
       BinaryStdOut.close();
   }


   private static Node readTrie() {
       boolean isLeaf = BinaryStdIn.readBoolean();
       if (isLeaf) {
           return new Node(BinaryStdIn.readChar(), -1, null, null);
       }
       else {
           return new Node('\0', -1, readTrie(), readTrie());
       }
   }


   public static void main(String[] args) {
       if      (args[0].equals("-")) compress();
       else if (args[0].equals("+")) expand();
       else throw new IllegalArgumentException("Illegal command line argument");
   }

}
