import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

		JLabel introduction = new JLabel("<html> <p style='text-align: center;'>What would you like to do? Note that in order to palmod or romhack, you'll need a Combined ROM, which is the type FightCade, FBA, and FBA-rr use. A Split ROM is the one used by Final Burn Neo and FightCade 2.</p></html>");
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
	final String[] gamesSupported = {"JoJo's HFTF", "JoJo's Venture", "Third Strike", "Second Impact", "New Generation", "Red Earth"};

	final String[][] combinedSet = {
			{"10", "20", "30", "31", "40", "41", "50", "51"}, //JoJo's HFTF
			{"10", "20", "30", "31", "40", "41", "50"}, //JoJo's Venture
			{"10", "20", "30", "31", "40", "41", "50", "51", "60", "61"}, //Third Strike
			{"10", "20", "30", "31", "40", "41", "50", "51"}, //Second Impact
			{"10", "30", "31", "40", "41", "50"}, //New Generation
			{"10", "30", "31", "40", "41", "50"} //Red Earth
	};

	final String[][][] splitSet = {
			{{"jojoba-simm1.0", "jojoba-simm1.1", "jojoba-simm1.2", "jojoba-simm1.3"}, {"jojoba-simm2.0", "jojoba-simm2.1", "jojoba-simm2.2", "jojoba-simm2.3"}, {"jojoba-simm3.0", "jojoba-simm3.1", "jojoba-simm3.2", "jojoba-simm3.3"}, {"jojoba-simm3.4", "jojoba-simm3.5", "jojoba-simm3.6", "jojoba-simm3.7"}, {"jojoba-simm4.0", "jojoba-simm4.1", "jojoba-simm4.2", "jojoba-simm4.3"}, {"jojoba-simm4.4", "jojoba-simm4.5", "jojoba-simm4.6", "jojoba-simm4.7"}, {"jojoba-simm5.0", "jojoba-simm5.1", "jojoba-simm5.2", "jojoba-simm5.3"}, {"jojoba-simm5.4", "jojoba-simm5.5", "jojoba-simm5.6", "jojoba-simm5.7"}}, //JoJo's HFTF
			{{"jojo-simm-1.0", "jojo-simm-1.1", "jojo-simm-1.2", "jojo-simm-1.3"}, {"jojo-simm-2.0", "jojo-simm-2.1", "jojo-simm-2.2", "jojo-simm-2.3"}, {"jojo-simm-3.0", "jojo-simm-3.1", "jojo-simm-3.2", "jojo-simm-3.3"}, {"jojo-simm-3.4", "jojo-simm-3.5", "jojo-simm-3.6", "jojo-simm-3.7"}, {"jojo-simm-4.0", "jojo-simm-4.1", "jojo-simm-4.2", "jojo-simm-4.3"}, {"jojo-simm-4.4", "jojo-simm-4.5", "jojo-simm-4.6", "jojo-simm-4.7"}, {"jojo-simm-5.0", "jojo-simm-5.1"}}, //JoJo's Venture
			{{"sfiii3-simm1.0", "sfiii3-simm1.1", "sfiii3-simm1.2", "sfiii3-simm1.3"}, {"sfiii3-simm2.0", "sfiii3-simm2.1", "sfiii3-simm2.2", "sfiii3-simm2.3"}, {"sfiii3-simm3.0", "sfiii3-simm3.1", "sfiii3-simm3.2", "sfiii3-simm3.3"}, {"sfiii3-simm3.4", "sfiii3-simm3.5", "sfiii3-simm3.6", "sfiii3-simm3.7"}, {"sfiii3-simm4.0", "sfiii3-simm4.1", "sfiii3-simm4.2", "sfiii3-simm4.3"}, {"sfiii3-simm4.4", "sfiii3-simm4.5", "sfiii3-simm4.6", "sfiii3-simm4.7"}, {"sfiii3-simm5.0", "sfiii3-simm5.1", "sfiii3-simm5.2", "sfiii3-simm5.3"}, {"sfiii3-simm5.4", "sfiii3-simm5.5", "sfiii3-simm5.6", "sfiii3-simm5.7"}, {"sfiii3-simm6.0", "sfiii3-simm6.1", "sfiii3-simm6.2", "sfiii3-simm6.3"}, {"sfiii3-simm6.4", "sfiii3-simm6.5", "sfiii3-simm6.6", "sfiii3-simm6.7"}}, //Third Strike
			{{"sfiii2-simm1.0", "sfiii2-simm1.1", "sfiii2-simm1.2", "sfiii2-simm1.3"}, {"sfiii2-simm2.0", "sfiii2-simm2.1", "sfiii2-simm2.2", "sfiii2-simm2.3"}, {"sfiii2-simm3.0", "sfiii2-simm3.1", "sfiii2-simm3.2", "sfiii2-simm3.3"}, {"sfiii2-simm3.4", "sfiii2-simm3.5", "sfiii2-simm3.6", "sfiii2-simm3.7"}, {"sfiii2-simm4.0", "sfiii2-simm4.1", "sfiii2-simm4.2", "sfiii2-simm4.3"}, {"sfiii2-simm4.4", "sfiii2-simm4.5", "sfiii2-simm4.6", "sfiii2-simm4.7"}, {"sfiii2-simm5.0", "sfiii2-simm5.1", "sfiii2-simm5.2", "sfiii2-simm5.3"}, {"sfiii2-simm5.4", "sfiii2-simm5.5", "sfiii2-simm5.6", "sfiii2-simm5.7"}}, //Second Impact
			{{"sfiii-simm1.0", "sfiii-simm1.1", "sfiii-simm1.2", "sfiii-simm1.3"}, {"sfiii-simm3.0", "sfiii-simm3.1", "sfiii-simm3.2", "sfiii-simm3.3"}, {"sfiii-simm3.4", "sfiii-simm3.5", "sfiii-simm3.6", "sfiii-simm3.7"}, {"sfiii-simm4.0", "sfiii-simm4.1", "sfiii-simm4.2", "sfiii-simm4.3"}, {"sfiii-simm4.4", "sfiii-simm4.5", "sfiii-simm4.6", "sfiii-simm4.7"}, {"sfiii-simm5.0", "sfiii-simm5.1"}}, //New Generation
			{{"redearth-simm1.0", "redearth-simm1.1", "redearth-simm1.2", "redearth-simm1.3"}, {"redearth-simm3.0", "redearth-simm3.1", "redearth-simm3.2", "redearth-simm3.3"}, {"redearth-simm3.4", "redearth-simm3.5", "redearth-simm3.6", "redearth-simm3.7"}, {"redearth-simm4.0", "redearth-simm4.1", "redearth-simm4.2", "redearth-simm4.3"}, {"redearth-simm4.4", "redearth-simm4.5", "redearth-simm4.6", "redearth-simm4.7"}, {"redearth-simm5.0", "redearth-simm5.1"}} //Red Earth
	};

	String[] combined;
	String[][] split;

	static JFileChooser chooser = new JFileChooser();

	@SuppressWarnings("resource")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int gameSelected = JOptionPane.showOptionDialog(null, "Please choose the game you wish to convert from combined to split", "Select a Game", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gamesSupported, gamesSupported[0]);
		combined = combinedSet[gameSelected];
		split = splitSet[gameSelected];

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JOptionPane.showMessageDialog(null, "Please select the directory where the Combined ROM is located");

		int returnVal = chooser.showOpenDialog(new JFrame());

		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String combinedPath = chooser.getSelectedFile().toString();

			JOptionPane.showMessageDialog(null, "Please select the directory where you want to place the Split ROM. Make sure there are no simm files there currently");

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
						if((combined.length > 6 && a < 2) || (combined.length <= 6 && a < 1))
							fileDivide = splitDataFile(combinedPath + "/" + combined[a], split[a].length);

						else
							fileDivide = splitUserFile(combinedPath + "/" + combined[a], split[a].length);

						for(int b = 0; b < split[a].length; b++)
						{
							currentFile = new File(splitPath + "/" + split[a][b]);
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
	public byte[][] splitDataFile(String path, int splitAmt) throws IOException
	{	
		File file = new File(path);
		FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
		ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

		byte[][] fileDivide = new byte[splitAmt][(int) (file.length() / splitAmt)];
		byte[] wholeFile = new byte[(int) file.length()];

		wrBuf.get(wholeFile);

		for(int a = 0; a < wholeFile.length; a++)
			fileDivide[a % splitAmt][a / splitAmt] = wholeFile[a];

		rwChannel.close();
		return fileDivide;
	}

	@SuppressWarnings("resource")
	public byte[][] splitUserFile(String path, int splitAmt) throws IOException
	{	
		File file = new File(path);
		FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
		ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

		byte[][] fileDivide = new byte[splitAmt][(int) (file.length() / splitAmt)];
		byte[] wholeFile = new byte[(int) file.length()];

		wrBuf.get(wholeFile);

		if(splitAmt == 2)
		{
			for(int a = 0; a < wholeFile.length; a++)
				fileDivide[a % splitAmt][a / splitAmt] = wholeFile[a];
		}

		else
		{
			for(int a = 0; a < wholeFile.length / 2; a++)
				fileDivide[a % 2][a / 2] = wholeFile[a];

			for(int b = wholeFile.length / 2; b < wholeFile.length; b++)
				fileDivide[((b - wholeFile.length / 2) % 2) + 2][(b - wholeFile.length / 2) / 2] = wholeFile[b];			
		}

		rwChannel.close();
		return fileDivide;
	}
}

class SplitToCombined implements ActionListener
{
	final String[] gamesSupported = {"JoJo's HFTF", "JoJo's Venture", "Third Strike", "Second Impact", "New Generation", "Red Earth"};

	final String[][] combinedSet = {
			{"10", "20", "30", "31", "40", "41", "50", "51"}, //JoJo's HFTF
			{"10", "20", "30", "31", "40", "41", "50"}, //JoJo's Venture
			{"10", "20", "30", "31", "40", "41", "50", "51", "60", "61"}, //Third Strike
			{"10", "20", "30", "31", "40", "41", "50", "51"}, //Second Impact
			{"10", "30", "31", "40", "41", "50"}, //New Generation
			{"10", "30", "31", "40", "41", "50"} //Red Earth
	};

	final String[][][] splitSet = {
			{{"jojoba-simm1.0", "jojoba-simm1.1", "jojoba-simm1.2", "jojoba-simm1.3"}, {"jojoba-simm2.0", "jojoba-simm2.1", "jojoba-simm2.2", "jojoba-simm2.3"}, {"jojoba-simm3.0", "jojoba-simm3.1", "jojoba-simm3.2", "jojoba-simm3.3"}, {"jojoba-simm3.4", "jojoba-simm3.5", "jojoba-simm3.6", "jojoba-simm3.7"}, {"jojoba-simm4.0", "jojoba-simm4.1", "jojoba-simm4.2", "jojoba-simm4.3"}, {"jojoba-simm4.4", "jojoba-simm4.5", "jojoba-simm4.6", "jojoba-simm4.7"}, {"jojoba-simm5.0", "jojoba-simm5.1", "jojoba-simm5.2", "jojoba-simm5.3"}, {"jojoba-simm5.4", "jojoba-simm5.5", "jojoba-simm5.6", "jojoba-simm5.7"}}, //JoJo's HFTF
			{{"jojo-simm-1.0", "jojo-simm-1.1", "jojo-simm-1.2", "jojo-simm-1.3"}, {"jojo-simm-2.0", "jojo-simm-2.1", "jojo-simm-2.2", "jojo-simm-2.3"}, {"jojo-simm-3.0", "jojo-simm-3.1", "jojo-simm-3.2", "jojo-simm-3.3"}, {"jojo-simm-3.4", "jojo-simm-3.5", "jojo-simm-3.6", "jojo-simm-3.7"}, {"jojo-simm-4.0", "jojo-simm-4.1", "jojo-simm-4.2", "jojo-simm-4.3"}, {"jojo-simm-4.4", "jojo-simm-4.5", "jojo-simm-4.6", "jojo-simm-4.7"}, {"jojo-simm-5.0", "jojo-simm-5.1"}}, //JoJo's Venture
			{{"sfiii3-simm1.0", "sfiii3-simm1.1", "sfiii3-simm1.2", "sfiii3-simm1.3"}, {"sfiii3-simm2.0", "sfiii3-simm2.1", "sfiii3-simm2.2", "sfiii3-simm2.3"}, {"sfiii3-simm3.0", "sfiii3-simm3.1", "sfiii3-simm3.2", "sfiii3-simm3.3"}, {"sfiii3-simm3.4", "sfiii3-simm3.5", "sfiii3-simm3.6", "sfiii3-simm3.7"}, {"sfiii3-simm4.0", "sfiii3-simm4.1", "sfiii3-simm4.2", "sfiii3-simm4.3"}, {"sfiii3-simm4.4", "sfiii3-simm4.5", "sfiii3-simm4.6", "sfiii3-simm4.7"}, {"sfiii3-simm5.0", "sfiii3-simm5.1", "sfiii3-simm5.2", "sfiii3-simm5.3"}, {"sfiii3-simm5.4", "sfiii3-simm5.5", "sfiii3-simm5.6", "sfiii3-simm5.7"}, {"sfiii3-simm6.0", "sfiii3-simm6.1", "sfiii3-simm6.2", "sfiii3-simm6.3"}, {"sfiii3-simm6.4", "sfiii3-simm6.5", "sfiii3-simm6.6", "sfiii3-simm6.7"}}, //Third Strike
			{{"sfiii2-simm1.0", "sfiii2-simm1.1", "sfiii2-simm1.2", "sfiii2-simm1.3"}, {"sfiii2-simm2.0", "sfiii2-simm2.1", "sfiii2-simm2.2", "sfiii2-simm2.3"}, {"sfiii2-simm3.0", "sfiii2-simm3.1", "sfiii2-simm3.2", "sfiii2-simm3.3"}, {"sfiii2-simm3.4", "sfiii2-simm3.5", "sfiii2-simm3.6", "sfiii2-simm3.7"}, {"sfiii2-simm4.0", "sfiii2-simm4.1", "sfiii2-simm4.2", "sfiii2-simm4.3"}, {"sfiii2-simm4.4", "sfiii2-simm4.5", "sfiii2-simm4.6", "sfiii2-simm4.7"}, {"sfiii2-simm5.0", "sfiii2-simm5.1", "sfiii2-simm5.2", "sfiii2-simm5.3"}, {"sfiii2-simm5.4", "sfiii2-simm5.5", "sfiii2-simm5.6", "sfiii2-simm5.7"}}, //Second Impact
			{{"sfiii-simm1.0", "sfiii-simm1.1", "sfiii-simm1.2", "sfiii-simm1.3"}, {"sfiii-simm3.0", "sfiii-simm3.1", "sfiii-simm3.2", "sfiii-simm3.3"}, {"sfiii-simm3.4", "sfiii-simm3.5", "sfiii-simm3.6", "sfiii-simm3.7"}, {"sfiii-simm4.0", "sfiii-simm4.1", "sfiii-simm4.2", "sfiii-simm4.3"}, {"sfiii-simm4.4", "sfiii-simm4.5", "sfiii-simm4.6", "sfiii-simm4.7"}, {"sfiii-simm5.0", "sfiii-simm5.1"}}, //New Generation
			{{"redearth-simm1.0", "redearth-simm1.1", "redearth-simm1.2", "redearth-simm1.3"}, {"redearth-simm3.0", "redearth-simm3.1", "redearth-simm3.2", "redearth-simm3.3"}, {"redearth-simm3.4", "redearth-simm3.5", "redearth-simm3.6", "redearth-simm3.7"}, {"redearth-simm4.0", "redearth-simm4.1", "redearth-simm4.2", "redearth-simm4.3"}, {"redearth-simm4.4", "redearth-simm4.5", "redearth-simm4.6", "redearth-simm4.7"}, {"redearth-simm5.0", "redearth-simm5.1"}} //Red Earth
	};

	String[] combined;
	String[][] split;

	static JFileChooser chooser = new JFileChooser();

	@SuppressWarnings("resource")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int gameSelected = JOptionPane.showOptionDialog(null, "Please choose the game you wish to convert from split to combined", "Select a Game", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gamesSupported, gamesSupported[0]);

		combined = combinedSet[gameSelected];
		split = splitSet[gameSelected];
		
		for(String combinedFile : combined)
			System.out.println(combinedFile);

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
						if((combined.length > 6 && a < 2) || (combined.length <= 6 && a < 1))
							wholeFile = combineDataFile(splitPath, a, split[a].length);

						else
							wholeFile = combineUserFile(splitPath, a, split[a].length);

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
	public byte[] combineDataFile(String path, int start, int splitAmt) throws IOException
	{	
		File file = new File(path + "/" + split[start][0]);
		byte[] wholeFile = new byte[(int) file.length() * splitAmt];

		for(int a = 0; a < splitAmt; a++)
		{
			file = new File(path + "/" + split[start][a]);
			byte[] splitFile = new byte[(int) file.length()];

			FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

			wrBuf.get(splitFile);

			for(int b = 0; b < splitFile.length; b++)
				wholeFile[a + (b * splitAmt)] = splitFile[b];

			rwChannel.close();
		}

		return wholeFile;
	}

	@SuppressWarnings("resource")
	public byte[] combineUserFile(String path, int start, int splitAmt) throws IOException
	{	
		File file = new File(path + "/" + split[start][0]);
		byte[] wholeFile = new byte[(int) file.length() * splitAmt];

		for(int a = 0; a < 2; a++)
		{
			file = new File(path + "/" + split[start][a]);
			byte[] splitFile = new byte[(int) file.length()];

			FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

			wrBuf.get(splitFile);

			for(int b = 0; b < splitFile.length; b++)
				wholeFile[a + (b * 2)] = splitFile[b];

			rwChannel.close();
		}

		if(splitAmt == 4)
		{
			for(int a = 2; a < 4; a++)
			{
				file = new File(path + "/" + split[start][a]);
				byte[] splitFile = new byte[(int) file.length()];

				FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
				ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

				wrBuf.get(splitFile);

				for(int b = 0; b < splitFile.length; b++)
					wholeFile[(wholeFile.length / 2) + (a - 2) + (b * 2)] = splitFile[b];

				rwChannel.close();
			}
		}

		return wholeFile;
	}
}
