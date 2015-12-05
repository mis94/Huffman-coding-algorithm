package M;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StandardHuffman {

	private JFrame frame;
	private File myFile ;
	private String charstream ;
	private JTextField textField;
	TreeMap<String, String> codeMap ;
	TreeMap<String, String> decodeMap ;
	private String decode1 = new String() ;
	private String decode2 = new String() ;
	byte []bytes ;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StandardHuffman window = new StandardHuffman();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StandardHuffman() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel chooseLabel = new JLabel("Choose file to compress");
		chooseLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		chooseLabel.setBounds(10, 11, 280, 41);
		frame.getContentPane().add(chooseLabel);
		
		textField = new JTextField();
		textField.setBounds(10, 63, 314, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse"); 
		btnBrowse.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				JFileChooser ChosenFile = new JFileChooser();
				ChosenFile.showOpenDialog(null);
				myFile = ChosenFile.getSelectedFile();
				if(myFile==null) // If he didn't choose a file
					return ;
				textField.setText(myFile.getAbsolutePath());
				
				String choice = JOptionPane.showInputDialog("1:Compress"+"     "+"2:Decompress") ;
				if(choice.contains("1"))
					try {
						readFile(true); // only reads a string to compress
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				else
					try {
						readFile(false) ; // read a table(map) and code to uncompress
					} catch (IOException e1) {
						e1.printStackTrace();
					} 
				
				if(choice.contains("1")==true) // check according to choice
					compress(); 
				else
					deCompress() ;
			}
		});
		btnBrowse.setBounds(334, 63, 89, 20);
		frame.getContentPane().add(btnBrowse);
	}
	
	public void readFile(boolean b) throws IOException
	{
		if(b==true)
		{
			try {
				Scanner input = new Scanner(myFile) ; 
				while(input.hasNext())
				{
					charstream = input.next() ; 
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else
		{
			try {
				File temp = new File("tableFile.txt") ; // map file
				Scanner input = new Scanner(temp) ;
				decode1 = input.nextLine() ; // read the map file
				InputStream in = new FileInputStream(myFile);
				byte[] buf = new byte[1024] ; // array of bytes the compressed bytes
				int len;
				while ((len = in.read(buf)) > 0) 
				{
					in.read(buf, 0, len) ;
				}
				int x = buf[0] ; // first the number of bytes to read and convert
				int sz = buf[1] ; // second the size of the string 
				for(int i=2;i<=x;i++) // the third element(index 2) is the first actual byte to convert
				{
					int y ;
					if(sz-7<0)
						y = sz ; // if we reached the last one and the remaining size less than 7
					else
						y = 7 ; // regular case is that each one is seven bits
					decode2 += convertToBinary(buf[i], y) ;
					sz -= 7 ;
				}
 			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void compress()
	{
		codeMap = new TreeMap<>() ;
		Vector< Pair<String, Integer> > vect = new Vector< Pair<String, Integer> >() ;
		// this vector saves the (letter, occurrence times), Pair is an implemented class
		for(char ch='a';ch<='z';ch++) // count each letter occured how many times
		{
			int x = 0 ;
			for(int i=0;i<charstream.length();i++)
			{
				if(charstream.charAt(i)==ch)
					x++ ;
			}
			String temp = new String() ;
			temp += ch ;
			if(x>0)
				vect.addElement(new Pair<String, Integer>(temp, x));
		}
		for(char ch='A';ch<='Z';ch++) // Do the same but for capital letters
		{
			int x = 0 ;
			for(int i=0;i<charstream.length();i++)
			{
				if(charstream.charAt(i)==ch)
					x++ ;
			}
			String temp = new String() ;
			temp += ch ;
			if(x>0)
				vect.addElement(new Pair<String, Integer>(temp, x));
		}
		Vector< Pair<Integer, Node> > tree = new Vector< Pair<Integer, Node> >() ;
		// save each pair of (number of occurrence of letters in string, node)
		for(int i=0;i<vect.size();i++)
		{
			Node node = new Node(vect.get(i).l, vect.get(i).r) ;
			// put first nodes who have no children
			tree.addElement(new Pair<Integer, Node>(node.getNum(), node));
		}
		Collections.sort(tree); // sort the vector
		while(tree.size()>1)
		{
			String temp = new String(tree.get(0).r.getStr()+tree.get(1).r.getStr()) ;//combination of two strings
			int x = tree.get(0).r.getNum()+tree.get(1).r.getNum() ;// sum of two integers
			// make a new node as combination of the least two nodes and make them as its children
			Node newNode = new Node(temp, x, tree.get(0).r, tree.get(1).r) ;
			tree.remove(0) ;
			tree.remove(0) ;
			// remove the first two nodes
			tree.addElement(new Pair<Integer, Node>(newNode.getNum(), newNode));//push the new node to vector
			Collections.sort(tree); // sort the vector again
		}
		// now we have only one node containing all letters and the total number of characters
		rec(tree.get(0).r, "") ; // go recurse on it
		// now code map contains each letter ----> its code(zeros and ones)
		StringBuilder compressed = new StringBuilder() ;
		for(int i=0;i<charstream.length();i++)
		{
			String temp = new String() ;
			temp += charstream.charAt(i) ;
			compressed.append(codeMap.get(temp)) ;
		}
		// compressed is all zeros and ones encoded code
		writeCompressed(compressed.toString(), codeMap) ; // write the code and map to file
	}
	
	public void rec(Node n, String code)
	{
		if(n.getStr().length()==1)
		{
			codeMap.put(n.getStr(), code) ;
			return ;
		}
		String codeLeft = code ;
		codeLeft += '0' ;
		rec(n.getLeft(), codeLeft) ;
		
		String codeRight = code ;
		codeRight += '1' ;
		rec(n.getRight(), codeRight) ;
		return ;
	}
	
	public void deCompress()
	{
		decodeMap = new TreeMap<>() ; 
		StringBuilder uncompressed = new StringBuilder() ;
		String code = new String() ;
		String value = new String() ;
		int x = 1 ;
		
		for(int i=0, j=0;j<decode1.length();j++)
		{
			if(decode1.charAt(j)=='$'&&x%2!=0) // means we read a code
			{
				code = decode1.substring(i, j) ;
				i = j+1 ;
				x++ ;
			}else if(decode1.charAt(j)=='$'&&x%2==0) // means we read a key
			{
				value = decode1.substring(i, j) ;
				i = j+1 ;
				x++ ;
				decodeMap.put(code, value) ;
			}
		}
		for(int i=0,j=1;j<decode2.length();j++) // decode2 contains the zeros and ones
		{
			String searcher = decode2.substring(i, j) ;
			if(decodeMap.containsKey(searcher)==true) // if this is found in map
			{
				uncompressed.append(decodeMap.get(searcher)) ; // append the key matched to the string
				i = j ;
			}
			if(j==decode2.length()-1) // if we reached the end
			{	
				j++ ;
				searcher = decode2.substring(i, j) ;
				if(decodeMap.containsKey(searcher)==true)
					uncompressed.append(decodeMap.get(searcher)) ; // append and break
				break ;
			}
		}
		writeDecompressed(uncompressed.toString()); // write it to the file
	}
	
	public void writeCompressed(String toWrite, TreeMap<String, String> cM)
	{
		try{
			java.io.DataOutputStream dos = new java.io.DataOutputStream(new FileOutputStream("compressed.txt"));
			Vector<Byte> vect = new Vector<Byte>();
			vect.addElement((byte)toWrite.length()); // the size of the string itself
			for(int i=0,j=0;i<toWrite.length();i++)
			{
				if(i%7==0&&i!=0)
				{
					vect.addElement(convertToByte(toWrite.substring(j,i))) ;
					j = i ;
				}
				if(i==toWrite.length()-1)
				{
					vect.addElement(convertToByte(toWrite.substring(j, i+1)));
					break ;
				}
			}
			
			for(int i=0;i<vect.size();i++)
			{
				if(i==0)
					dos.write((byte)vect.size()); // write the size of vector to know how many bytes we loop
				dos.writeByte(vect.get(i));		  // when we read and decompress
			}
				
			dos.close();
			
			Formatter fo = new Formatter("tableFile.txt") ; // write the map to its file
			Set setOfKeys = cM.keySet();
			Iterator iterator = setOfKeys.iterator();
			while(iterator.hasNext()) 
			{
				// write the map in the form: code$key$code$key$
				String key = (String) iterator.next();
				String value = (String)cM.get(key);
				fo.format("%s",value);
				fo.format("%s","$");
				fo.format("%s",key);
				fo.format("%s","$");
		 	}
			fo.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	public byte convertToByte(String str)
	{
		byte ret = 0 ;
		for(int i=str.length()-1,j=0;i>=0;i--,j++)
		{
			if(str.charAt(i)=='1')
				ret += Math.pow(2, j) ;
		}
		return ret ;
	}
	
	public String convertToBinary(byte b, int y)
	{
		String ret = new String() ;
		if(b<0) // sebak men dh
		{
			ret += '1' ;
			b *= -1 ;
		}
		while(b>0)
		{
			if(b%2==0)
				ret += '0' ;
			else
				ret += '1' ;
			b /= 2 ;
		} // u need to reverse
		String ret2 = new String() ;
		for(int i=ret.length()-1;i>=0;i--)
			ret2 += ret.charAt(i) ;
		String ret3 = new String() ;
		int remZeros = y-ret2.length() ; // number of zeros to be put in the beginning
		for(int i=0;i<remZeros;i++)
			ret3 += '0' ;
		ret3 += ret2 ;
		return ret3 ;
	}
	
	public void writeDecompressed(String toWrite)
	{
		try {
			Formatter fo = new Formatter("Uncompressed.txt") ;
			fo.format("%s", toWrite) ;
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
