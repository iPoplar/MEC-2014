import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;


public class FileSecert 
{
	private JFrame JF;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem chooseFile,  choosefolder;
	
	private int valueOfProgress = 0;
	private JProgressBar progress;
	private JLabel background = new JLabel(new ImageIcon(getClass().getResource("background.jpg")));
	private JLabel producer = new JLabel();
	private Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
	private Insets scrInsets=Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()); 
	private void setMenuBar()
	{ 
		JF = new JFrame("视频加密");
		menuBar = new JMenuBar(); 
		chooseFile = new JMenuItem("单独处理文件");
		choosefolder = new JMenuItem("批量处理文件");
		
		file = new JMenu("文件");
		
		progress = new JProgressBar(1, 100); // 实例化进度条
        progress.setStringPainted(true);      // 描绘文字
        JF.add(progress,BorderLayout.SOUTH);
        
		JF.setJMenuBar(menuBar);  
		file.add(chooseFile);
		file.add(choosefolder);
		menuBar.add(file); 
		
		JF.setBounds(scrInsets.left,scrInsets.top,scrSize.width-scrInsets.left-scrInsets.right,scrSize.height-scrInsets.top-scrInsets.bottom);
		producer.setText("微易码科技暑期实训");
		background.setBounds(0, 0, JF.getWidth(), JF.getHeight());
		background.setVisible(true);
		JF.add(background, BorderLayout.CENTER);
		JF.add(producer, BorderLayout.NORTH);
		
		chooseOnefile();
		choosefolder();
		
		JF.setResizable(true);
		JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JF.setVisible(true);  
	}
	
	public void chooseOnefile()
	{
		chooseFile.addActionListener
		(
			new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					 FileDialog fd=new FileDialog(JF,"请选择加密文件",FileDialog.LOAD);
					 fd.setVisible(true);
					 if(fd.getFile() != null)
						 secert(new File(fd.getDirectory()+fd.getFile()));
				}
			}
		);
	}
	
	private void choosefolder()
	{
		choosefolder.addActionListener
		(
				new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						FolderSelectDialog d = new FolderSelectDialog(JF, "FOLDER");
						d.show();	
						if (d.isOKClick()) 
    			 	{
							File fileTemporary = new File(d.getSelectPath());
							try 
							{
								indexDocs(fileTemporary);
							}catch (Exception e1) 
							{
								e1.printStackTrace();
							}
    			 	}
					}
				}
		);
	}
		
	private void secert(File file) 
	{
		
		int[] key = {542,19879,35465,78951,45678,354654,7956543,154987,6543218,546,127};
		InputStream in = null;
        OutputStream out = null;
        DataInputStream dis = null;
        int k = 1;
        try 
        {
        	in = new FileInputStream(file);
            dis = new DataInputStream(in);
            out = new FileOutputStream(file.getPath() + ".mec");
            int tempbyte;
            tempbyte = dis.readUnsignedByte();
            int j = 0;
            for (int i = 0; i< file.length() - 1; i++,j++) 
            {
            	
            	if(i == (file.length() / 100 * k) - 1)
            	{
            		valueOfProgress++;
            		new thread1().start();
            		k++;
            	}
            	if(j == key.length)
                	j = 0;
            	tempbyte = tempbyte ^ key[j];
            	out.write(tempbyte);
                tempbyte = dis.readUnsignedByte();
            }
            in.close();
            out.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        valueOfProgress = 0;
        if(JOptionPane.showConfirmDialog(JF, "加密已完成，是否退出？") == 0)
			System.exit(0);
	}
	
	private void indexDocs(File file) throws Exception 
	{
		if (file.isDirectory())
		{
			String[] files = file.list();
			Arrays.sort(files);
			for (int i = 0; i < files.length; i++) 
			{
				indexDocs(new File(file, files[i])); 
			}
		}
		else if (file.getPath().endsWith(".mpg")) 
		{
			File fileTemporary = new File(file.getParent() + "\\" + file.getName());
			secert(fileTemporary); 
		}
	}
	
	class thread1 extends Thread
	{
		public void run() 
		{
			Dimension d = progress.getSize();
	       	Rectangle rect = new Rectangle(0,0, d.width, d.height);
	       	progress.setValue(valueOfProgress+1); // 随着线程进行，增加进度条值
	       	progress.setString(valueOfProgress + "%");
	       	progress.paintImmediately(rect);
	       	if(valueOfProgress == 100)
	       	{
	       		progress.setValue(100);
	       		progress.setString(100 + "%");
	       	}
	    }
		
	}
	
	public static void main(String[] args)
	{  
		 FileSecert FS = new FileSecert();
		 FS.setMenuBar();
	}
}

