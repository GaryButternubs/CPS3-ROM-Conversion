package combinedAndSplitROM;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CombinedAndSplitSwap 
{
	public static void main(String[] args) throws Exception 
	{	
		final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height; //height of monitor
		final int frameWH = (int) (screenHeight / 1.5); //size of JFrame
		
		Font arialTitle = new Font("Arial", Font.BOLD, frameWH / 24);
		Font arial = new Font("Arial", Font.PLAIN, frameWH / 32);
		
		JFrame frame = new JFrame();
		frame.setSize(frameWH, frameWH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		JLabel introduction = new JLabel("<html> <p style='text-align: center;'>What would you like to do? Note that in order to palmod or romhack, you'll need a Combined ROM, which is the type FightCade, FBA, and FBA-rr use. A Split ROM is the one used by FBN and RedGGPO.</p></html>");
		introduction.setFont(arialTitle);
		introduction.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton combinedToSplit = new JButton("<html> <p style='text-align: center;'>Convert a Combined ROM into a Split ROM</p> </html>");
		combinedToSplit.setFont(arial);
		combinedToSplit.addActionListener(new CombinedToSplit());
		
		JButton splitToCombined = new JButton("<html> <p style='text-align: center;'>Convert a Split ROM into a Combined ROM</p> </html>");
		splitToCombined.setFont(arial);
		splitToCombined.addActionListener(new SplitToCombined());
		
		JPanel mainPanel = new JPanel(new GridLayout(2, 1, 0, frameWH / 6));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, frameWH / 12, 0));
		
		buttonPanel.add(combinedToSplit);
		buttonPanel.add(splitToCombined);
		
		mainPanel.add(introduction);
		mainPanel.add(buttonPanel);
		frame.add(mainPanel);
		frame.setVisible(true);
	}
}

class CombinedToSplit implements ActionListener
{
	final String[] combined = {"10", "20", "30", "31", "40", "41", "50", "51"};
	final String[] split = {"jojoba-simm1.0", "jojoba-simm1.1", "jojoba-simm1.2", "jojoba-simm1.3", "jojoba-simm2.0", "jojoba-simm2.1", "jojoba-simm2.2", "jojoba-simm2.3", "jojoba-simm3.0", "jojoba-simm3.1", "jojoba-simm3.2", "jojoba-simm3.3", "jojoba-simm3.4", "jojoba-simm3.5", "jojoba-simm3.6", "jojoba-simm3.7", "jojoba-simm4.0", "jojoba-simm4.1", "jojoba-simm4.2", "jojoba-simm4.3", "jojoba-simm4.4", "jojoba-simm4.5", "jojoba-simm4.6", "jojoba-simm4.7", "jojoba-simm5.0", "jojoba-simm5.1", "jojoba-simm5.2", "jojoba-simm5.3", "jojoba-simm5.4", "jojoba-simm5.5", "jojoba-simm5.6", "jojoba-simm5.7"};
	
	static JFileChooser chooser = new JFileChooser();
	
	@SuppressWarnings("resource")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JOptionPane.showMessageDialog(null, "Please select the directory where the Combined ROM is located");
		
		int returnVal = chooser.showOpenDialog(new JFrame());
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String combinedPath = chooser.getSelectedFile().toString();
			
			JOptionPane.showMessageDialog(null, "Please select the directory where you want to place the Split ROM. Make sure there are no jojoba-simm files there currently");
			
			returnVal = chooser.showOpenDialog(new JFrame());
			
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				String splitPath = chooser.getSelectedFile().toString();
				byte[][] fileDivide = null;
				File currentFile = null;
				
				for(int a = 0; a < combined.length; a++)
				{
					try 
					{
						if(a < 2)
							fileDivide = splitDataFile(combinedPath + "/" + combined[a]);
						
						else
							fileDivide = splitUserFile(combinedPath + "/" + combined[a]);
						
						for(int b = 0; b < 4; b++)
						{
							currentFile = new File(splitPath + "/" + split[b + (4 * a)]);
							currentFile.createNewFile();
							
							FileChannel rwChannel = new RandomAccessFile(currentFile, "rw").getChannel();
							ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileDivide[b].length);
							
							for(int c = 0; c < fileDivide[b].length; c++)
								wrBuf.put(fileDivide[b][c]);
							
							rwChannel.close();
						}
					} 
					
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Process failed");
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	public byte[][] splitDataFile(String path) throws IOException
	{	
		File file = new File(path);
		FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
		ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
		
		byte[][] fileDivide = new byte[4][(int) (file.length() / 4)];
		byte[] wholeFile = new byte[(int) file.length()];
		
		wrBuf.get(wholeFile);
		
		for(int a = 0; a < wholeFile.length; a++)
			fileDivide[a % 4][a / 4] = wholeFile[a];
		
		rwChannel.close();
		return fileDivide;
	}
	
	@SuppressWarnings("resource")
	public byte[][] splitUserFile(String path) throws IOException
	{	
		File file = new File(path);
		FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
		ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
		
		byte[][] fileDivide = new byte[4][(int) (file.length() / 4)];
		byte[] wholeFile = new byte[(int) file.length()];
		
		wrBuf.get(wholeFile);
		
		for(int a = 0; a < wholeFile.length / 2; a++)
			fileDivide[a % 2][a / 2] = wholeFile[a];
		
		for(int b = wholeFile.length / 2; b < wholeFile.length; b++)
			fileDivide[((b - wholeFile.length / 2) % 2) + 2][(b - wholeFile.length / 2) / 2] = wholeFile[b];
		
		rwChannel.close();
		return fileDivide;
	}
}

class SplitToCombined implements ActionListener
{
	final String[] combined = {"10", "20", "30", "31", "40", "41", "50", "51"};
	final String[] split = {"jojoba-simm1.0", "jojoba-simm1.1", "jojoba-simm1.2", "jojoba-simm1.3", "jojoba-simm2.0", "jojoba-simm2.1", "jojoba-simm2.2", "jojoba-simm2.3", "jojoba-simm3.0", "jojoba-simm3.1", "jojoba-simm3.2", "jojoba-simm3.3", "jojoba-simm3.4", "jojoba-simm3.5", "jojoba-simm3.6", "jojoba-simm3.7", "jojoba-simm4.0", "jojoba-simm4.1", "jojoba-simm4.2", "jojoba-simm4.3", "jojoba-simm4.4", "jojoba-simm4.5", "jojoba-simm4.6", "jojoba-simm4.7", "jojoba-simm5.0", "jojoba-simm5.1", "jojoba-simm5.2", "jojoba-simm5.3", "jojoba-simm5.4", "jojoba-simm5.5", "jojoba-simm5.6", "jojoba-simm5.7"};
	
	static JFileChooser chooser = new JFileChooser();
	
	@SuppressWarnings("resource")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JOptionPane.showMessageDialog(null, "Please select the directory where the Split ROM is located");
		
		int returnVal = chooser.showOpenDialog(new JFrame());
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String splitPath = chooser.getSelectedFile().toString();
			
			JOptionPane.showMessageDialog(null, "Please select the directory where you want to place the Combied ROM. Make sure there are no Combined ROM files (ie. 10, 20, 30, etc.) there currently");
			
			returnVal = chooser.showOpenDialog(new JFrame());
			
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				String combinedPath = chooser.getSelectedFile().toString();
				byte[] wholeFile = null;
				File currentFile = null;
				
				for(int a = 0; a < combined.length; a++)
				{
					try 
					{
						if(a < 2)
							wholeFile = combineDataFile(splitPath, a * 4);
						
						else
							wholeFile = combineUserFile(splitPath, a * 4);
						
						currentFile = new File(combinedPath + "/" + combined[a]);
						currentFile.createNewFile();
						
						FileChannel rwChannel = new RandomAccessFile(currentFile, "rw").getChannel();
						ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, wholeFile.length);
						
						wrBuf.put(wholeFile);
						rwChannel.close();
					} 
					
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Process failed");
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	public byte[] combineDataFile(String path, int start) throws IOException
	{	
		File file = new File(path + "/" + split[start]);
		byte[] wholeFile = new byte[(int) file.length() * 4];
		
		for(int a = 0; a < 4; a++)
		{
			file = new File(path + "/" + split[start + a]);
			byte[] splitFile = new byte[(int) file.length()];
			
			FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
			
			wrBuf.get(splitFile);
			
			for(int b = 0; b < splitFile.length; b++)
				wholeFile[a + (b * 4)] = splitFile[b];
			
			rwChannel.close();
		}
		
		return wholeFile;
	}
	
	@SuppressWarnings("resource")
	public byte[] combineUserFile(String path, int start) throws IOException
	{	
		File file = new File(path + "/" + split[start]);
		byte[] wholeFile = new byte[(int) file.length() * 4];
		
		for(int a = 0; a < 2; a++)
		{
			file = new File(path + "/" + split[start + a]);
			byte[] splitFile = new byte[(int) file.length()];
			
			FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
			
			wrBuf.get(splitFile);
			
			for(int b = 0; b < splitFile.length; b++)
				wholeFile[a + (b * 2)] = splitFile[b];
			
			rwChannel.close();
		}
		
		for(int a = 2; a < 4; a++)
		{
			file = new File(path + "/" + split[start + a]);
			byte[] splitFile = new byte[(int) file.length()];
			
			FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
			
			wrBuf.get(splitFile);
			
			for(int b = 0; b < splitFile.length; b++)
				wholeFile[(wholeFile.length / 2) + (a - 2) + (b * 2)] = splitFile[b];
			
			rwChannel.close();
		}
		
		return wholeFile;
	}
}
