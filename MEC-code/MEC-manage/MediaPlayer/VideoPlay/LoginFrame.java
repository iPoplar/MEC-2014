import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class LoginFrame
{
	JFrame jfrmMain;
	private JTextField idTxt;
//	private JTextField jtxtLogin;
	private JButton submitBtn;
	private JButton exitBtn;
	
	public LoginFrame()
	{
		initFrame();
		reinitFrame();	
		dealAction();
	}
	
	/**��ȡϵͳʱ��*/
	private String getSystemTime()
	{
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format( now ); 
	}
	
	/**����½�˺ŵĳ��ȡ�ѧ������Ų�����500*/
	private boolean judgeId(String id)
	{
		boolean IdRight = true;
		
		Pattern p = Pattern.compile("\\d{12}");
		Matcher m = p.matcher(id);
		
		int StudentCount = Integer.parseInt(id.substring(9, 9 + 3));
		String SubjectCount = id.substring(6, 6 + 3);
		if(!m.find())
			IdRight = false;
		if(StudentCount >= 500)
			IdRight = false;
		//TODO �����ݿ��Ŀ����Ƿ����
		
		
		return IdRight;
	}	

	
	/**�ٴγ�ʼ�����������ݿ�*/
	private void reinitFrame()
	{
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}

	}
		/**�ѵ�½��¼��ӵ����ݿ�*/
	private void insertLoginInforMation(String loginTime, String loginId,String loginStatus)
	{
		String SQLinsert = "INSERT INTO SYS_STU_LOGIN_INFORMATION VALUES ('"+loginTime+"','"+loginId+"','"+loginStatus+"')";
		try 
		{
			FStatement.executeUpdateCon(SQLinsert);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
			
	}
	
	
	/**��½״̬���*/
	private void isOnline(String loginId)
	{
		//System.out.println("������isoline");
		String SQLselect = "SELECT * FROM SYS_STU_LOGIN_INFORMATION WHERE STU_LOGIN_ID = '" + loginId + "' AND STU_LOGIN_STATUS = '1'";
		String loginTime = "";
		try 
		{
			FResultSet selectSet = FStatement.executeQueryCon(SQLselect);
			if(selectSet.next())
			{
				//System.out.println("�ۿ�");
				loginTime = selectSet.getString("STU_LOGIN_TIME");
				modifyOnlineStatus(loginTime);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		//System.out.println("�˳���isoline");
	}
	
	/**�޸ĵ�½״̬���ѱ�ѧ�ŵ�½ǰ��״̬����Ϊ�����ߡ�*/
	private void modifyOnlineStatus(String loginTime)
	{
		//System.out.println("������modifyOnlineStatus");
		String SQLupdate = "UPDATE SYS_STU_LOGIN_INFORMATION SET STU_LOGIN_STATUS = '0' WHERE STU_LOGIN_TIME = '"+loginTime+"'";
		try 
		{
			FStatement.executeUpdateCon(SQLupdate);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		//System.out.println("�˳���modifyOnlineStatus");
	}
	
	public void initFrame()
	{
		jfrmMain = new JFrame("΢������Ƶ��¼");
		
		jfrmMain.setSize(530,350);
		int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		jfrmMain.setLocation((x-jfrmMain.getWidth())/2, (y-jfrmMain.getHeight())/2);
		jfrmMain.setResizable(false);
		
//		��Logo������JFrame�ı��ߡ�
		String Src = "/img/Login.png";
		Icon icon = new ImageIcon(getClass().getResource(Src));
		JLabel jlibPictrue = new JLabel(icon);
		jfrmMain.add(jlibPictrue, BorderLayout.NORTH);
		
//		��½���
		JPanel mainPanel = new JPanel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		mainPanel.setBorder(BorderFactory.createTitledBorder(border, "�����¼��Ϣ", TitledBorder.CENTER, TitledBorder.TOP));
		jfrmMain.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel jlibLogin = new JLabel("ѧ�ţ�");
		jlibLogin.setBounds(15, 40, 65, 40);
		jlibLogin.setFont(new Font("����", Font.BOLD, 20));
		mainPanel.add(jlibLogin);
		
		idTxt = new JTextField();
		idTxt.setBounds(85, 40, 400, 40);
		idTxt.setFont(new Font("����", Font.BOLD, 40));
		idTxt.requestFocusInWindow();
		mainPanel.add(idTxt);
		
//		��ť��������JFrame���ϱ�
		JPanel btnPanel = new JPanel();
		jfrmMain.add(btnPanel, BorderLayout.SOUTH);
		btnPanel.setLayout(new BorderLayout());
		btnPanel.setBorder(new EmptyBorder(2, 8, 4, 8));
		exitBtn = new JButton("�˳�");
		btnPanel.add(exitBtn, BorderLayout.EAST);
		submitBtn = new JButton("��¼");
		btnPanel.add(submitBtn, BorderLayout.WEST);
	
		jfrmMain.setVisible(true);
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void dealAction()
	{
		
		submitBtn.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					String loginTime = getSystemTime();
					String loginId = idTxt.getText();
					String loginStatus = "1";
					
					if(judgeId(loginId))
					{
						isOnline(loginId);
						insertLoginInforMation(loginTime, loginId,loginStatus);
									
						new VideoPlayerList(loginId,loginTime);
						jfrmMain.dispose();

						try 
						{
							FStatement.disConnection();
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(jfrmMain, "����ѧ����������������");
					}
				}
			}
		);
		
		exitBtn.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// �˳������ܴ���
					jfrmMain.dispose();
					try 
					{
						FStatement.disConnection();
					} catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		);
		
		jfrmMain.addWindowListener
		(
			new WindowListener()
			{
				public void windowActivated(WindowEvent e) {}
				public void windowClosed(WindowEvent e) {}
				public void windowDeactivated(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
				public void windowOpened(WindowEvent e) {}
				public void windowClosing(WindowEvent e) 
				{
					jfrmMain.dispose();
					try 
					{
						FStatement.disConnection();
					} catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				
				}
				
			}
		);
	}
	
	/*public static void main(String[] args)
	{
		new LoginFrame();
	}*/
}
