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
	
	/**获取系统时间*/
	private String getSystemTime()
	{
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format( now ); 
	}
	
	/**检测登陆账号的长度、学生的序号不超过500*/
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
		//TODO 查数据库科目编号是否存在
		
		
		return IdRight;
	}	

	
	/**再次初始化，连接数据库*/
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
		/**把登陆记录添加到数据库*/
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
	
	
	/**登陆状态检测*/
	private void isOnline(String loginId)
	{
		//System.out.println("进入了isoline");
		String SQLselect = "SELECT * FROM SYS_STU_LOGIN_INFORMATION WHERE STU_LOGIN_ID = '" + loginId + "' AND STU_LOGIN_STATUS = '1'";
		String loginTime = "";
		try 
		{
			FResultSet selectSet = FStatement.executeQueryCon(SQLselect);
			if(selectSet.next())
			{
				//System.out.println("哇靠");
				loginTime = selectSet.getString("STU_LOGIN_TIME");
				modifyOnlineStatus(loginTime);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		//System.out.println("退出了isoline");
	}
	
	/**修改登陆状态，把本学号登陆前的状态都改为“下线”*/
	private void modifyOnlineStatus(String loginTime)
	{
		//System.out.println("进入了modifyOnlineStatus");
		String SQLupdate = "UPDATE SYS_STU_LOGIN_INFORMATION SET STU_LOGIN_STATUS = '0' WHERE STU_LOGIN_TIME = '"+loginTime+"'";
		try 
		{
			FStatement.executeUpdateCon(SQLupdate);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		//System.out.println("退出了modifyOnlineStatus");
	}
	
	public void initFrame()
	{
		jfrmMain = new JFrame("微易码视频登录");
		
		jfrmMain.setSize(530,350);
		int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		jfrmMain.setLocation((x-jfrmMain.getWidth())/2, (y-jfrmMain.getHeight())/2);
		jfrmMain.setResizable(false);
		
//		把Logo放置在JFrame的北边。
		String Src = "/img/Login.png";
		Icon icon = new ImageIcon(getClass().getResource(Src));
		JLabel jlibPictrue = new JLabel(icon);
		jfrmMain.add(jlibPictrue, BorderLayout.NORTH);
		
//		登陆面板
		JPanel mainPanel = new JPanel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		mainPanel.setBorder(BorderFactory.createTitledBorder(border, "输入登录信息", TitledBorder.CENTER, TitledBorder.TOP));
		jfrmMain.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel jlibLogin = new JLabel("学号：");
		jlibLogin.setBounds(15, 40, 65, 40);
		jlibLogin.setFont(new Font("宋体", Font.BOLD, 20));
		mainPanel.add(jlibLogin);
		
		idTxt = new JTextField();
		idTxt.setBounds(85, 40, 400, 40);
		idTxt.setFont(new Font("宋体", Font.BOLD, 40));
		idTxt.requestFocusInWindow();
		mainPanel.add(idTxt);
		
//		按钮面板放置在JFrame的南边
		JPanel btnPanel = new JPanel();
		jfrmMain.add(btnPanel, BorderLayout.SOUTH);
		btnPanel.setLayout(new BorderLayout());
		btnPanel.setBorder(new EmptyBorder(2, 8, 4, 8));
		exitBtn = new JButton("退出");
		btnPanel.add(exitBtn, BorderLayout.EAST);
		submitBtn = new JButton("登录");
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
						JOptionPane.showMessageDialog(jfrmMain, "输入学号有误，请重新输入");
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
					// 退出本功能窗口
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
