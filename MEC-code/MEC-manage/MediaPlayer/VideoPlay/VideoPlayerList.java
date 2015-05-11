import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import ��Ƶ������.JMFSample;

public class VideoPlayerList implements Runnable
{
	
	private JFrame FrmMain;
	private Container con;
	
	private JLabel jlblTopic;
	
	/* �γ������б�*/
	private JLabel jlblcourse;
	private static JComboBox jcmbcourse;
	
	/* �γ��б�*/
	private static DefaultListModel dlmjlstcourse;
	private static JList jlstjlstcourse;
	private JScrollPane jscpjlstcourse;

	/*  */
	private static JLabel jlblVideoCount;
	
	/*  */
	private JLabel jlblSeek;
	private JTextField jtxtSeek;
	private static JComboBox jcmbSeek;
	private JButton jbtnSeek;
	
	/*  */
	private JRadioButton jrdbSelectPart;
	private JRadioButton jrbtSelectAll;
	private ButtonGroup bgjrbtSelectAll;

	/*  */
	private JLabel jlblName;
	private JTextField jtxtName;

	/*  */
	private JLabel jlblKey;
	private JTextField jtxtKey;

	/*  */
	private JLabel jlblTime;
	private JTextField jtxtTime;
	
	/*  */
	private JLabel jlbltip;
	private JLabel jlblIntroduce;
	
	/*  */
	private JButton jbtnPlayer;
	private JButton jbtnExit;
	
	/*  */
	private String selectData = null;
			
	/*  */
	private final static String[] jcmbSeekstring = {"�ؼ���","����","ʱ��"};
	
	/*  */
	private final static DefaultComboBoxModel model = new DefaultComboBoxModel();
	
	
	private static final JComboBox cbInput = new JComboBox(model) 
	{
	   /**
		* 
		*/
		private static final long serialVersionUID = 3893406720999915739L;

		public Dimension getPreferredSize() 
	 	{
			return new Dimension(super.getPreferredSize().width, 0);
	    }
	};
	
	private static final int KEY_WORD = 0; 
	private static final int WRITER = 1;
	private static final int TIME = 2;
	
	private static final String Year = "20";
	
	private static final String ZHENG_ZE = "[A-Za-z0-9\u4e00-\u9fa5]{0,20}";
	
	private static ArrayList<String> items = new ArrayList<String>();
	
	private listIcon icon;
	
	Thread t;
	
	private String StudentID;
	private String StudentLoginTime;
	
	private final String exeName = "XMP.exe";

	
	/**���췽��*/
	public VideoPlayerList(/*String loginId, String loginTime*/) 
	{
		/*StudentID = loginId;
		StudentLoginTime = loginTime;
		*/
		initFrame();
		reinitFrame();
		dealAction();
		
		t = new Thread(this);
		t.start();
	}

	ResultSet True_False = null;
	
		
	/**��Ӽ���½״̬���̣߳���ֹͬʱ��¼*/
	public void run()
	{
		String checkloginSQL = "select STU_LOGIN_STATUS from SYS_STU_LOGIN_INFORMATION where STU_LOGIN_TIME = '"+ StudentLoginTime +"'";

		while(true)
		{
			try 
			{
				Thread.sleep(5000);
				True_False = MECDatabase.doSQL(checkloginSQL);
				//System.out.println("��ȡ�ĵ�¼ʱ�䣺"+ StudentLoginTime);
				//System.out.println(True_False.getRow()+"************");
				String SATUS_1 = " ";
				
				
				if(True_False.next())
				{
					//System.out.println("�����ˣ�");
					SATUS_1 = True_False.getString("STU_LOGIN_STATUS");
					//System.out.println(SATUS_1+"***");
				}
							
				
				if(SATUS_1.equals("0"))
				{
					//MECTool.showMessage(FrmMain, "΢������ܰ��ʾ");
					TimeDialog.showDialog(FrmMain, "΢������ܰ��ʾ", 6);
					Thread.sleep(5000);
					FrmMain.dispose();
					t.stop();
				}
				//else
					//System.out.println("��"+SATUS_1+"��"+"����������");
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean findProcessOfExe(String name)
	{
		boolean ok = false;
		boolean Break = false;
		try {
			Process proc = Runtime.getRuntime().exec("tasklist");
			 BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			  String info = reader.readLine();
			  while (info != null && !Break)
			  {
				  System.out.println(info);
				  ok = info.matches("^" + name +".*$");
				  if(ok)
					  Break = true;
			      info = reader.readLine();
			  }
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	private void deleteFile(String path)
	{
		File file = new File(path);
		File[] files;
		files = file.listFiles(new FileFilter()
		{
			public boolean accept(File pathname) 
			{
				return pathname.getName().matches("^.+\\.avi$");
			}
		});
		for(int i = 0; i < files.length; i++)
		{
			System.out.println(files[i]);
			files[i].delete();
		}
	}
	
	private String secert(File file) 
	{
	    String path = "e:\\" + file.getName().substring(0, file.getName().length() - 4);
		
	    int[] key;
	    String key1;
		int lengthofkey;
		InputStream in = null;
		OutputStream os = null;
		DataInputStream dis = null;
	    try 
	    {
	    	// һ�ζ�����ֽ�
	    	int byteread= 32768;
	    	int index = 0;
	    	int j = 0;
	    	int k = 1;
		    byte[] tempbytes = new byte[byteread];
		    in = new FileInputStream(file);
		    dis = new DataInputStream(in);
	        os = new FileOutputStream(path);
	        lengthofkey = dis.readUnsignedByte();
	        byte[] key3 = new byte[lengthofkey];
	        in.read(key3);
	        key1 = new String(key3);
			String[] prikey = key1.replaceAll(" +", " ").split(" ");
			if(prikey.length == 0)
			{
				key= new int[prikey.length];
				for(int i=0; i<prikey.length; i++)
				{
					key[i] = Integer.parseInt(prikey[i]);
					System.out.println(key[i]);
				}
			}
			else
				key = new int[] {110,112,113,119,120};
	        //�������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
	        while (in.read(tempbytes,0,byteread) != -1) 
	        {
	        	if(index >= key.length)
	        		index = 0;
	        	tempbytes[0] = (byte) (Integer.parseInt(tempbytes[0] + "") ^ key[index]);
	        	os.write(tempbytes);
	        	index++;
	        	j++;
	        }
	       // valueOfProgress = 100;
	        in.close();
	        os.close();
	        
	    }catch (Exception e1) 
	    {
	    	e1.printStackTrace();
	    } finally
	    {
	    	if (in != null) 
	    	{
	    		try 
	    		{
	    			in.close();
	            }
	    		catch (IOException e1) 
	    		{
	            }
	    	}
	    }
		    
		return path;
	}
	
	
	private String download(listIcon licon)
	{
		String path = "";
		boolean connection = true;
		while(connection)
		{
			int i = 0;
			System.out.println(i++);
			try 
			{								
				ClientTest ct = new ClientTest();
				path = ProgressDialog.showDialog(FrmMain, licon.getText());
				
				//path = ClientTest.sendMessage(licon.getText());
				//MECTool.showMessage(FrmMain, "���سɹ����ļ�������E:\\"+ licon.getText());
				connection = false;
				
			} catch (Exception e) 
			{
				System.out.println("����ʧ�ܣ�");
				
				/*try 
				{
					new ClientTest();
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return null;
				}*/
				//e.printStackTrace();
			} 
		}
		return path;
	}
	
	private void reinitFrame()
	{
		initinterface();
		initjcmbcourse();
	}
	
	
	/**����ѧ����¼״̬��1Ϊ���ߣ�0Ϊ����*/
	private void setstudentStatus(String StudentID, String StudentLoginTime)
	{
		String updateStudentStatus = "update SYS_STU_LOGIN_INFORMATION SET STU_LOGIN_STATUS = '0' WHERE STU_LOGIN_TIME = '"+ StudentLoginTime +"' AND STU_LOGIN_ID = '"+ StudentID +"'";
		
		int count;
		
		try 
		{
			count = MECDatabase.executeUpdateCon(updateStudentStatus);
			//System.out.println("�ɹ�����״̬" + count + "��");
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**ͨ��ʱ�������õ���Ƶ����*/
	private void getVideoNamebyTime(String getTime)
	{
		dlmjlstcourse.removeAllElements();
		
		String seekResultbyTimeSQL = "select VIDEONAME,VIDEOID FROM SYS_VIDEO_INFORMATION WHERE VIDEOID like (select SUBJECTID from SYS_SUBJECT_INFORMATION where SUBJECTNAME = '"+ jcmbcourse.getSelectedItem().toString() +"')||'"+ getTime.substring(2) +"'||'_'";
		
		String videoName;
		ResultSet TimeseekRs = null;
		
		jlstjlstcourse.setCellRenderer(new listView());
		
		try 
		{
			TimeseekRs = MECDatabase.doSQL(seekResultbyTimeSQL);
			
			while(TimeseekRs.next())
			{
				videoName = TimeseekRs.getString("VIDEONAME");
				//listIcon item = new listIcon("F:\\sp.jpg",videoName);
				listIcon item = new listIcon("/img/sp.jpg",videoName);
				dlmjlstcourse.addElement(item);
			}			
		} catch (Exception e) 
		{
			System.out.println("ͨ��������ʽ�������ʧ��");
			e.printStackTrace();
		}
		jlstjlstcourse.setSelectedIndex(0);
		jlblVideoCount.setText("������������"+dlmjlstcourse.size() +"����Ƶ");

	}
	
	
	/**ͨ���ؼ��ֵ��������õ���Ƶ���Ƹ�����Ƶ�б�*/
	private static void showVideoNameInList(String videoName)
	{
		dlmjlstcourse.removeAllElements();
		
		jlstjlstcourse.setCellRenderer(new listView());
		//listIcon item = new listIcon("F:\\sp.jpg",videoName);
		listIcon item = new listIcon("/img/sp.jpg",videoName);
		dlmjlstcourse.addElement(item);
		
		jlstjlstcourse.setSelectedIndex(0);
		jlblVideoCount.setText("������������"+dlmjlstcourse.size() +"����Ƶ");
	}
	
	
	/**ͨ������ؼ������������Ƶ*/	
	private void seekResultinitlist(String seekresult)
	{
		dlmjlstcourse.removeAllElements();

		String seekResultSQL = "select distinct VIDEONAME from SYS_VIDEO_KEYWORDS,SYS_VIDEO_INFORMATION WHERE (VIDEOKEYWORDS = '"+ seekresult +"' " +
								"AND SYS_VIDEO_KEYWORDS.VIDEOID = SYS_VIDEO_INFORMATION.VIDEOID AND substr(SYS_VIDEO_INFORMATION.VIDEOID,0,3) = " +
								"(select SUBJECTID from SYS_SUBJECT_INFORMATION where SUBJECTNAME = '"+jcmbcourse.getSelectedItem().toString() +"')) OR SYS_VIDEO_INFORMATION.VIDEONAME = '"+ seekresult +"'";
		String videoName;
		ResultSet seekResult = null;
		
		jlstjlstcourse.setCellRenderer(new listView());
		
		try
		{
			seekResult = MECDatabase.doSQL(seekResultSQL);
			
			while(seekResult.next())
			{
				videoName = seekResult.getString("VIDEONAME");
				//listIcon item = new listIcon("F:\\sp.jpg",videoName);
				listIcon item = new listIcon("/img/sp.jpg",videoName);

				dlmjlstcourse.addElement(item);
			}
			
		} catch (Exception e) 
		{
			System.out.println("���������ť�󣬲�ѯ����ʧ�ܣ�");
			e.printStackTrace();
		}		
		jlstjlstcourse.setSelectedIndex(0);
		jlblVideoCount.setText("������������"+dlmjlstcourse.size() +"����Ƶ");
		
	}
	
	
	/**��ʼ����Ƶ����*/
	private void initinterface()
	{
		jrbtSelectAll.setSelected(true);
		jtxtName.setEditable(false);
		jtxtKey.setEditable(false);
		jtxtTime.setEditable(false);
	}
	
	
	/**��ʼ����Ƶ���ơ���Ƶ�ؼ�������Ƶ�ϴ�����*/
	private void initdetail(String videoname)
	{
		jtxtName.setText(videoname);
		
		jtxtKey.setText("");
		
		ResultSet jtxttimers = null;
		ResultSet jtxtKeyword = null;
		
		String videoTime = "";
		String videoKeyword = "";
		
		String getTimeSQL = "select VIDEOID from SYS_VIDEO_INFORMATION " +
							"where VIDEONAME = '"+ videoname +"'";
		String getKeyword = "select * from SYS_VIDEO_KEYWORDS where VIDEOID =" +
							" (select VIDEOID from SYS_VIDEO_INFORMATION where VIDEONAME = '"+ videoname +"')";		
		
		try 
		{			
			jtxttimers = MECDatabase.doSQL(getTimeSQL);
			while(jtxttimers.next())
			{
				videoTime = jtxttimers.getString("VIDEOID");
				//System.out.println(Year + videoTime.substring(3, 3+2) + "-" +videoTime.substring(5, 5+2) + "-" + videoTime.substring(7, 7+2));
			}
			jtxtKeyword = MECDatabase.doSQL(getKeyword);
			while(jtxtKeyword.next())
			{
				videoKeyword = jtxtKeyword.getString("VIDEOKEYWORDS");
				//System.out.println(videoKeyword);
				jtxtKey.setText(jtxtKey.getText()+" "+videoKeyword);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		jtxtTime.setText(Year + videoTime.substring(3, 3+2) + "-" +videoTime.substring(5, 5+2) + "-" + videoTime.substring(7, 7+2));
		
	}
	
	
	/**��ʼ���γ������б�*/
	private void initjcmbcourse()
	{
		//String strSQL = "select SUBJECTNAME from SYS_SUBJECT_INFORMATION";
		ArrayList<String> Subsubjectid = new ArrayList<String>();
		String getSubsubjectId = "select SUBSUBJECTID from SYS_SUBJECT_RELATION where SUBJECTID = '002'";
		String SubsubjectId;
		String courseName;
		
		ResultSet jcmbrs = null;
		ResultSet jcmbsubNamers = null;
		try 
		{
			MECDatabase.connection("mecdb", "mec_prog_user", "654321");
			jcmbrs = MECDatabase.doSQL(getSubsubjectId);
			
			while(jcmbrs.next())
			{
				SubsubjectId = jcmbrs.getString("SUBSUBJECTID");
				Subsubjectid.add(SubsubjectId);
			}
			for(int i = 0; i < Subsubjectid.size();i++)
			{
				String getSubsubjectName = "select SUBJECTNAME FROM SYS_SUBJECT_INFORMATION WHERE SUBJECTID = '"+ Subsubjectid.get(i) +"'";
				jcmbsubNamers = MECDatabase.doSQL(getSubsubjectName);
				jcmbsubNamers.next();
				
				courseName = jcmbsubNamers.getString("SUBJECTNAME");
				jcmbcourse.addItem(courseName);
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("���ݿ������쳣��");
		}
		//System.out.println(jcmbcourse.getItemAt(0));
		initjlstcourse(jcmbcourse.getItemAt(0).toString());
		jlblVideoCount.setText(jcmbcourse.getItemAt(0).toString()+"����"+dlmjlstcourse.getSize()+"����Ƶ");
}
	
	
	/** ��ʼ���γ��б�*/
	private static void initjlstcourse(String selected)
	{
		dlmjlstcourse.removeAllElements();
		items.clear();
		
		jlstjlstcourse.setCellRenderer(new listView());
		
		ResultSet jlstrs = null;
		String videoName;
		
		String initlistSQL = "select VIDEONAME from SYS_SUBJECT_INFORMATION,SYS_VIDEO_INFORMATION where SUBJECTNAME = '"+selected+"' and substr(videoid,0,3) = " +
				"(select SUBJECTID from SYS_SUBJECT_INFORMATION where SUBJECTNAME = '" +selected +"') ";
		
		try 
		{
			jlstrs = MECDatabase.doSQL(initlistSQL);
			
			while(jlstrs.next())
			{
				videoName = jlstrs.getString("VIDEONAME");
				//System.out.println(videoName);
				//listIcon item = new listIcon("F:\\sp.jpg",videoName);
				listIcon item = new listIcon("/img/sp.jpg",videoName);
				dlmjlstcourse.addElement(item);
				items.add(videoName);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		jlstjlstcourse.setSelectedIndex(0);
		//System.out.println(dlmjlstcourse.getElementAt(0));
		//initdetail(dlmjlstcourse.getElementAt(0).toString());
	}
	
	
	/**�������¼�*/
	private void dealAction()
	{
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(!findProcessOfExe(exeName))
					{
						deleteFile("E:\\");
						Runtime.getRuntime().exit(0);
						FrmMain.dispose();
						try 
						{			
							setstudentStatus(StudentID,StudentLoginTime);
							MECDatabase.disconnection();
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
						t.stop();
						System.exit(0);
					}
					else
						MECTool.showMessage(FrmMain, "�����˳�Ѹ�׿���,�ٹر���Ƶ�����б���");
					
					//TODO
					
				}
			}
		);
		
		
		FrmMain.addWindowListener
		(
			new WindowListener()
			{
				public void windowActivated(WindowEvent arg0) {}
				public void windowClosed(WindowEvent arg0) {}				
				public void windowDeactivated(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
				public void windowOpened(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) 
				{
					if(!findProcessOfExe(exeName))
					{
						deleteFile("E:\\");
						Runtime.getRuntime().exit(0);
						FrmMain.dispose();
						try 
						{			
							setstudentStatus(StudentID,StudentLoginTime);
							MECDatabase.disconnection();
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
						t.stop();
						System.exit(0);
					}
					else
						MECTool.showMessage(FrmMain, "�����˳�Ѹ�׿���,�ٹر���Ƶ�����б���");
					
				
					
				}
			}
		);
		
		jbtnPlayer.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
			
					//System.out.println("����?��");
					String path = "";
					//jbtnPlayer.setText("��������");
					//jlblIntroduce.setText("����������...");
					//MECTool.showMessage(FrmMain, "׼����ʼ����");
					
					listIcon licon = (listIcon)dlmjlstcourse.getElementAt(jlstjlstcourse.getSelectedIndex());
					path = download(licon);
					if(path != null)
					{
					
						//jlblIntroduce.setText("");
						//jbtnPlayer.setText("����");
						//MECTool.showMessage(FrmMain, "���سɹ���");
						System.out.println("jfjdf" + path);
						File fl = new File(path);					
						String Path = secert(fl);		//����
								//��������e:\2014-04-12�ٽ���Դ.avi��
						try 
						{
							Runtime.getRuntime().exec("attrib +s +h "+Path);
							//Runtime.getRuntime().exec("attrib " + Path + " +H");
						} catch (IOException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	
						
						try 
						{
							Process pro = Runtime.getRuntime().exec("D:/Program Files/Thunder Network/Xmp/Program/XMP.exe " + Path);
							//pro.waitFor();
							//pro.destroy();
							
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
	
					}
					else
					{
						MECTool.showMessage(FrmMain, "�������ظ�����ͬһ��Ƶ������");
					}
				}
			}
		);
		
				
		jcmbSeek.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					jtxtSeek.setText("");
					if(jcmbSeek.getSelectedIndex() == TIME)
					{
						new MainFrame(FrmMain.getX(),FrmMain.getY());
						jtxtSeek.setEditable(false);
					}
					else
					{
						jtxtSeek.setEditable(true);
						jtxtSeek.grabFocus();
					}
				}
				
			}
		);
		
		jcmbcourse.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{

					//System.out.println(jcmbcourse.getSelectedItem().toString());
					initjlstcourse(jcmbcourse.getSelectedItem().toString());
					jlblVideoCount.setText(jcmbcourse.getSelectedItem().toString()+ "����" + dlmjlstcourse.getSize()+"����Ƶ");
				}
				
			}
		);
		
		jlstjlstcourse.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0) 
				{
					if(jlstjlstcourse.getSelectedIndex() != -1)
					{
						icon = (listIcon) jlstjlstcourse.getSelectedValue();
						
						//System.out.println(icon.getText()+"---");
						initdetail(icon.getText().toString());
					}
				}
				
			}
		);
		
		jbtnSeek.addActionListener
		(
			new	ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(!jtxtSeek.getText().isEmpty())
					{
						if(!jcmbSeek.getSelectedItem().equals("ʱ��"))
						{
							//jtxtSeek.getText();
							seekResultinitlist(jtxtSeek.getText().toString());
							//showVideoNameInList(jtxtSeek.getText().toString());
						}
						else
						{
							//System.out.println(jtxtSeek.getText().toString().replace("-","").substring(2));
							getVideoNamebyTime(jtxtSeek.getText().toString().replace("-",""));							
						}
					}
				}
				
			}
		);
		
		
						
	}

	private static void setAdjusting(JComboBox cbInput, boolean adjusting) 
    {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }
    
    private static boolean isAdjusting(JComboBox cbInput) 
    {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean)
        {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }
    
    
    /**��JText�������JComBobox*/
    public static void setupAutoComplete(final JTextField txtInput,  final ArrayList<String> items) 
	{
		    setAdjusting(cbInput, false);
		    for (String item : items) 
		    {
		        model.addElement(item);
		    }
		    cbInput.setSelectedItem(null);
		          
		    cbInput.addActionListener
		    (
		    		new ActionListener() 
		    		{
		    			public void actionPerformed(ActionEvent e) 
		    			{
		    				if (!isAdjusting(cbInput)) 
		    				{
		    					if (cbInput.getSelectedItem() != null) 
		    					{
		    						txtInput.setText(cbInput.getSelectedItem().toString());
		    					}
		    				}
		    			}
		    		}
		    );
		       
		        /** ���̼����¼�*/
		   txtInput.addKeyListener
		   (
				   new KeyAdapter() 
				   {
					   public void keyPressed(KeyEvent e) 
					   {
						   setAdjusting(cbInput, true);
						   if (e.getKeyCode() == KeyEvent.VK_SPACE) 
						   {
							   if (cbInput.isPopupVisible()) 
							   {
								   e.setKeyCode(KeyEvent.VK_ENTER);
							   }
						   }
						   if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) 
						   {
							   e.setSource(cbInput);
							   cbInput.dispatchEvent(e);
						   if (e.getKeyCode() == KeyEvent.VK_ENTER) 
						   {
							   if(!txtInput.getText().isEmpty() && cbInput.getItemCount() != 0)//
							   {
								   txtInput.setText(cbInput.getSelectedItem().toString());
								   cbInput.setPopupVisible(false);
								   showVideoNameInList(txtInput.getText().toString());
							   }
							   							   
						   }
						   }
						   if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
						   {
		                      cbInput.setPopupVisible(false);
						   }
						   setAdjusting(cbInput, false);
					   }
				   }
		   );
      
		          /**�ı������ݱ仯����*/
		   txtInput.getDocument().addDocumentListener
	       (
	    		  
	        	 new DocumentListener() 
	        	 {
	        		 public void insertUpdate(DocumentEvent e) 
       			     {
	        			 updateList();
       			     }
	  
	        		 public void removeUpdate(DocumentEvent e) 
        			 {
        				 updateList();
        			 }
  
        			 public void changedUpdate(DocumentEvent e) 
        			 {
        				 updateList();
        			 }
 
        			 private void updateList() 
        			 {
        				 setAdjusting(cbInput, true);
        				 model.removeAllElements();
        				 String input = txtInput.getText();
        				 if (!input.isEmpty()) 
        				 {
        					 //for (String item : items) 
        					for(int i = 0 ; i < items.size(); i++)	 
        					 {
        						if(!jcmbSeek.getSelectedItem().equals("ʱ��"))//��ʱ�� ���������ܳ��������б�
        						{
        						// if (item.toLowerCase().startsWith(input.toLowerCase())) 
        						 if(items.get(i).indexOf(input) != -1 && input.matches(ZHENG_ZE)) 
        						 {
        							 model.addElement(items.get(i));	                 
        						 }
        						}
        					 }
        				 }
        				 else
        				 {
        					initjlstcourse(jcmbcourse.getSelectedItem().toString());
        					jlblVideoCount.setText(jcmbcourse.getSelectedItem().toString()+ "����" + dlmjlstcourse.getSize()+"����Ƶ");
						 }
        				 cbInput.setPopupVisible(model.getSize() > 0);
	        			 setAdjusting(cbInput, false);
	        		 }
	        	}
	       );
	}
	
    
    /**��ʼ������*/
	private void initFrame()
	{
		Dimension dim;
		Font normalFont;
		Font resultFont;
      
		normalFont = new Font("����",Font.BOLD,12+3);
		resultFont = new Font("����",Font.PLAIN,12+3);

		FrmMain = new JFrame("΢������Ƶ");
		con = FrmMain.getContentPane();
		con.setLayout(null);

		/******************************************/
		/**			                 ��ӱ���                                   */
		/******************************************/
		JPanel imagePanel;
		//ImageIcon background = new ImageIcon("F:/361.jpg");// ����ͼƬ
		ImageIcon background = new ImageIcon(FrmMain.getClass().getResource("/img/361.jpg"));
		JLabel label = new JLabel(background);// �ѱ���ͼƬ��ʾ��һ����ǩ����
		label.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
		//**�����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��*//
		imagePanel = (JPanel) FrmMain.getContentPane();
		imagePanel.setOpaque(false);
		FrmMain.getLayeredPane().setLayout(null);
		// �ѱ���ͼƬ��ӵ��ֲ㴰�����ײ���Ϊ����
		FrmMain.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		/******************************************/
		
		String stricon = "/img/icon1.jpg";

		try
		{
			Image image = ImageIO.read(FrmMain.getClass().getResource(stricon));
			
			FrmMain.setIconImage(image);
		} catch (IOException e) 
		{
						e.printStackTrace();
		}

		FrmMain.setSize(467,415);
		FrmMain.setFont(new Font("����",Font.PLAIN,16));
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		FrmMain.setLocation((dim.width - FrmMain.getWidth())/2, (dim.height - FrmMain.getHeight())/2);
		//System.out.println(dim.width+ "***"+ dim.height);
		
		jlblTopic = new JLabel("΢������Ƶ�����б�");
		jlblTopic.setFont(new Font("����",Font.BOLD,24+6));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(85,0,288,33);
		con.add(jlblTopic);
		
		jlblcourse = new JLabel("�γ����:");
		jlblcourse.setFont(normalFont);
		jlblcourse.setForeground(Color.black);
		jlblcourse.setBounds(8,56,177,25);
		con.add(jlblcourse);
		
		jcmbcourse = new JComboBox();
		jcmbcourse.setFont(resultFont);
		jcmbcourse.setForeground(Color.black);
		jcmbcourse.setBounds(8,80,177,24);
		jcmbcourse.setBackground(Color.white);
		con.add(jcmbcourse);
		
		jcmbSeek = new JComboBox(jcmbSeekstring);
		jcmbSeek.setFont(resultFont);
		jcmbSeek.setForeground(Color.black);
		jcmbSeek.setBounds(200,56,73,24);
		jcmbSeek.setBackground(Color.white);
		con.add(jcmbSeek);
		
		dlmjlstcourse = new DefaultListModel();
		jlstjlstcourse = new JList(dlmjlstcourse);
		jlstjlstcourse.setFont(resultFont);
		jlstjlstcourse.setForeground(Color.black);
		jscpjlstcourse = new JScrollPane(jlstjlstcourse);
		jscpjlstcourse.setBounds(8,120,177,232);
		jscpjlstcourse.setBorder(BorderFactory.createTitledBorder("��Ƶ�б� "));
		con.add(jscpjlstcourse);
		
		jlblVideoCount = new JLabel();
		jlblVideoCount.setFont(normalFont);
		jlblVideoCount.setForeground(Color.black);
		jlblVideoCount.setBounds(8,352,177+70,17);
		con.add(jlblVideoCount);
		
		jtxtSeek = new JTextField();
		jtxtSeek.setFont(resultFont);
		jtxtSeek.setForeground(Color.black);
		jtxtSeek.setBounds(280,55,1800/15+2+27,26-1);
		
		jtxtSeek.setLayout(new BorderLayout());
		jtxtSeek.add(cbInput,BorderLayout.SOUTH);
		con.add(jtxtSeek);
		
		jbtnSeek = new JButton(new ImageIcon(FrmMain.getClass().getResource("/img/seek1.jpg")));
		jbtnSeek.setBounds(400+27,55,49-26,25-1);
		jbtnSeek.setBorderPainted(false);
		con.add(jbtnSeek);
		
		jlblSeek = new JLabel("��ѯ��ʽ");
		jlblSeek.setFont(normalFont);
		jlblSeek.setForeground(Color.black);
		jlblSeek.setBounds(200,104,64,16);
		con.add(jlblSeek);
		
		bgjrbtSelectAll = new ButtonGroup();
		
		jrbtSelectAll = new JRadioButton("�����ѯ");
		jrbtSelectAll.setFont(new Font("����",Font.PLAIN,14));
		jrbtSelectAll.setForeground(Color.black);
		jrbtSelectAll.setBounds(280,96,89-8,33-10);
		con.add(jrbtSelectAll);
		jrbtSelectAll.setToolTipText("��������Ƶ�в���");
		bgjrbtSelectAll.add(jrbtSelectAll);
		
		jrdbSelectPart = new JRadioButton("�����ѯ");
		jrdbSelectPart.setFont(new Font("����",Font.PLAIN,14));
		jrdbSelectPart.setForeground(Color.black);
		jrdbSelectPart.setBounds(368,96,89-8,33-10);
		jrdbSelectPart.setToolTipText("����Ƶ�б��н��в���");
		con.add(jrdbSelectPart);
		bgjrbtSelectAll.add(jrdbSelectPart);
		
		jlblName = new JLabel("��Ƶ����");
		jlblName.setFont(normalFont);
		jlblName.setForeground(Color.black);
		jlblName.setBounds(200,144,64,16);
		con.add(jlblName);
		
		jtxtName = new JTextField();
		jtxtName.setFont(resultFont);
		jtxtName.setForeground(Color.black);
		jtxtName.setBounds(280,139,169,26);
		jtxtName.setBackground(Color.white);
		con.add(jtxtName);
		
		jlblKey = new JLabel("��Ƶ�ؼ���");
		jlblKey.setFont(normalFont);
		jlblKey.setForeground(Color.black);
		jlblKey.setBounds(200,184,80,16);
		con.add(jlblKey);
		
		jtxtKey = new JTextField();
		jtxtKey.setFont(resultFont);
		jtxtKey.setBounds(280,179,169,26);
		jtxtKey.setForeground(Color.black);
		jtxtKey.setBackground(Color.white);
		con.add(jtxtKey);
			
		jlblTime = new JLabel("����ʱ��");
		jlblTime.setFont(normalFont);
		jlblTime.setBounds(200,224,64,16);
		jlblTime.setForeground(Color.black);
		con.add(jlblTime);
		
		jtxtTime = new JTextField();
		jtxtTime.setFont(resultFont);
		jtxtTime.setForeground(Color.black);
		jtxtTime.setBounds(280,219,169,26);
		jtxtTime.setBackground(Color.white);
		con.add(jtxtTime);
		
		jlbltip = new JLabel("΢������ܰ��ʾ��");
		jlbltip.setFont(normalFont);
		jlbltip.setForeground(Color.black);
		jlbltip.setBounds(200,264,128,16);
		con.add(jlbltip);
		
		jlblIntroduce = new JLabel("΢����2014������Ŀʵѵ�ɹ���");
		jlblIntroduce.setFont(resultFont);
		jlblIntroduce.setForeground(Color.black);
		jlblIntroduce.setBounds(3000/15,4200/15-20,3735/15,855/15);
		con.add(jlblIntroduce);
		
		jbtnPlayer = new JButton("����");
		jbtnPlayer.setFont(new Font("����",Font.PLAIN,12+4));
		jbtnPlayer.setForeground(Color.black);
		jbtnPlayer.setBounds(280,348,73,24);
		jbtnPlayer.setBackground(Color.white);
		jbtnPlayer.setBorder(BorderFactory.createLineBorder(Color.black,1));
		con.add(jbtnPlayer);
		
		jbtnExit = new JButton("�˳�");
		jbtnExit.setFont(new Font("����",Font.PLAIN,12+4));
		jbtnExit.setForeground(Color.black);
		jbtnExit.setBounds(376,348,73,24);
		jbtnExit.setBackground(Color.white);
		jbtnExit.setBorder(BorderFactory.createLineBorder(Color.black,1));
		con.add(jbtnExit);
		
		FrmMain.setResizable(false);
		FrmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		FrmMain.setVisible(true);
			
		setupAutoComplete(jtxtSeek, items);//�����ı������

	}
	
	/*********************************************�ڲ��ࡾ����ʽ������**********************************************/
	public class MainFrame extends JFrame 
	{
	    /**
	     * ����ʽ����
	     */
	    private static final long serialVersionUID = 1L;
	    
		Point mousepoint;
	    
	    JPanel panel = new JPanel(new BorderLayout());
	    JPanel panel1 = new JPanel();
	    JPanel panel2 = new JPanel(new GridLayout(7, 7));
	    JPanel panel3 = new JPanel();
	    JLabel[] label = new JLabel[49];
	    JLabel y_label = new JLabel("���");
	    JLabel m_label = new JLabel("�·�");
	    JComboBox com1 = new JComboBox();
	    JComboBox com2 = new JComboBox();
	    int re_year, re_month;
	    int x_size, y_size;
	    String year_num;
	    Calendar now = Calendar.getInstance(); // ʵ����Calendar
	    public MainFrame(int x,int y)
	    {
	        super("������");
	        setSize(247, 288);
	    	setLocation(x+202,y+112);    
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        panel1.add(y_label);
	        panel1.add(com1);
	        panel1.add(m_label);
	        panel1.add(com2);
	    
	        for (int i = 0; i < 49; i++) 
	        {
	            label[i] = new JLabel("", JLabel.CENTER);// ����ʾ���ַ�����Ϊ����
		        if(i > 6) 
		        {
		        	label[i].addMouseListener
			        (
			            new MouseListener()
			            	{
								public void mouseClicked(MouseEvent arg0) 
								{
									selectData = com1.getSelectedItem()+"-"+(Integer.valueOf(com2.getSelectedItem().toString())+100+"").substring(1)
									+"-"+(Integer.valueOf(arg0.getSource().toString().split("#")[1])+100+"").substring(1);									jtxtSeek.setText(selectData);
									dispose();
									
								}
								public void mouseEntered(MouseEvent arg0)
								{
									int n = Integer.valueOf(arg0.getSource().toString().split("#")[2]);
									label[n].setBorder(BorderFactory.createLineBorder (Color.red, 3));
								}
								public void mouseExited(MouseEvent arg0)
								{
									int n = Integer.valueOf(arg0.getSource().toString().split("#")[2]);
									label[n].setBorder(BorderFactory.createLineBorder (Color.gray  , 1));
								}
								public void mousePressed(MouseEvent arg0){}
								public void mouseReleased(MouseEvent arg0){}
			            	}
			       );
		        }
	            
	            panel2.add(label[i]);
	        }
	        //panel3.add(new Clock(this));
	        panel.add(panel1, BorderLayout.NORTH);
	        panel.add(panel2, BorderLayout.CENTER);
	        //panel.add(panel3, BorderLayout.SOUTH);
	        panel.setBackground(Color.white);
	        panel1.setBackground(Color.white);
	        panel2.setBackground(Color.white);
	        //panel3.setBackground(Color.white);
	        Init();
	        com1.addActionListener(new ClockAction());
	        com2.addActionListener(new ClockAction());
	        setUndecorated(true);
	        setContentPane(panel);
	        setVisible(true);
	        setResizable(false);

	        addMouseListener
	        (
	        	new MouseListener()
	        	{
					public void mouseClicked(MouseEvent arg0) {}
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) 
					{
						mousepoint = MouseInfo.getPointerInfo().getLocation();

						if(!(mousepoint.x > getX() && mousepoint.x < (getX()+247) && mousepoint.y > getY() && mousepoint.y < (getY()+288)))
							dispose();
							
					}
					public void mousePressed(MouseEvent arg0) {}
					public void mouseReleased(MouseEvent arg0) {}
	        		
	        	}
	        );
	    }

	    class ClockAction implements ActionListener 
	    {
	        public void actionPerformed(ActionEvent arg0)
	        {
	            int c_year, c_month, c_week;
	            c_year = Integer.parseInt(com1.getSelectedItem().toString()); // �õ���ǰ��ѡ���
	            c_month = Integer.parseInt(com2.getSelectedItem().toString()) - 1; // �õ���ǰ�·ݣ�����1,������е���Ϊ0��11
	            c_week = use(c_year, c_month); // ���ú���use���õ����ڼ�
	            Resetday(c_week, c_year, c_month); // ���ú���Resetday
	        }
	    }
	    
	    public void Init()
	    {
	        int year, month_num, first_day_num;
	        String log[] = { "��", "һ", "��", "��", "��", "��", "��" };
	        for (int i = 0; i < 7; i++) 
	        {
	            label[i].setText(log[i]);
	        }
	        for (int i = 0; i < 49; i = i + 7)
	        {
	            label[i].setForeground(Color.red); // �������յ���������Ϊ��ɫ
	        }
	        for (int i = 6; i < 49; i = i + 7) 
	        {
	            label[i].setForeground(Color.green);// ������������������Ϊ��ɫ
	        }
	        for (int i = 1; i < 10000; i++) 
	        {
	            com1.addItem("" + i);
	        }
	        for (int i = 1; i < 13; i++) 
	        {
	            com2.addItem("" + i);
	        }
	        month_num = (int) (now.get(Calendar.MONTH)); // �õ���ǰʱ����·�
	        year = (int) (now.get(Calendar.YEAR)); // �õ���ǰʱ������
	        com1.setSelectedIndex(year - 1); // ���������б���ʾΪ��ǰ��
	        com2.setSelectedIndex(month_num); // ���������б���ʾΪ��ǰ��
	        first_day_num = use(year, month_num);
	        Resetday(first_day_num, year, month_num);
	    }

	    public int use(int reyear, int remonth)
	    {
	        int week_num;
	        now.set(reyear, remonth, 1); // ����ʱ��Ϊ��Ҫ��ѯ�����µĵ�һ��
	        week_num = (int) (now.get(Calendar.DAY_OF_WEEK));// �õ���һ�������
	        return week_num;
	    }

	    @SuppressWarnings("deprecation")
	    public void Resetday(int week_log, int year_log, int month_log) 
	    {
	        int month_day_score; // �洢�·ݵ�����
	        int count;
	        month_day_score = 0;
	        count = 1;

	        Date date = new Date(year_log, month_log + 1, 1); // now
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.MONTH, -1); // ǰ����
	        month_day_score = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// ���һ��

	        for (int i = 7; i < 49; i++)
	        { // ��ʼ����ǩ
	            label[i].setText("");
	            label[i].setVisible(false); //���ñ�ǩ���ɼ�
	        }
	        
	        week_log = week_log + 6; // ����������6��ʹ��ʾ��ȷ
	        month_day_score = month_day_score + week_log;
	        
	        for (int i = week_log; i < month_day_score; i++, count++)
	        {
	            label[i].setText(count + "");
	            label[i].setVisible(true); //���ñ�ǩ�ɼ�
	            label[i].setBorder (BorderFactory.createLineBorder (Color.gray , 1));
	            label[i].setForeground(Color.BLUE);
	            label[i].setFont(new Font("����",Font.BOLD + Font.ITALIC,16));
	            label[i].setName("#"+count+"#"+i+"#");
	        }
	    }
	}
	
	
	public static void main(String[] args)
	{
		new VideoPlayerList();
	}
}
