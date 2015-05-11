
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
public class StudentLogin
{
	private JFrame jfrmStudentLogin;
	private Container con;
	private JButton jbtnExit;
	private JButton jbtnConfirm;
	private JTextField jtxtStudentId;
	private JLabel jlblStudentId;
	private JLabel jlblStudentLogin;
	public StudentLogin()
	{
		initFrame();
		dealAction();
		reinitFrame();
	}
	private void initFrame()
	{
		Dimension dim;

		jfrmStudentLogin = new JFrame("jfrmStudentLogin");
		con = jfrmStudentLogin.getContentPane();
		con.setLayout(null);


		jfrmStudentLogin.setSize(668,478);
		jfrmStudentLogin.setFont(new Font("宋体",Font.PLAIN,16));
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmStudentLogin.setLocation((dim.width - jfrmStudentLogin.getWidth())/2, (dim.height - jfrmStudentLogin.getHeight())/2);
		jfrmStudentLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		jbtnExit = new JButton("退出");
		jbtnExit.setFont(new Font("楷体",Font.PLAIN,20));
		jbtnExit.setBounds(512,152,88,40);
		con.add(jbtnExit);


		jbtnConfirm = new JButton("确定");
		jbtnConfirm.setFont(new Font("楷体",Font.PLAIN,20));
		jbtnConfirm.setBounds(416,152,88,40);
		con.add(jbtnConfirm);


		jtxtStudentId = new JTextField();
		jtxtStudentId.setFont(new Font("宋体",Font.PLAIN,16));
		jtxtStudentId.setBounds(120,80,448,32);
		con.add(jtxtStudentId);


		jlblStudentId = new JLabel("学员证号");
		jlblStudentId.setFont(new Font("楷体",Font.PLAIN,20));
		jlblStudentId.setBounds(32,80,80,24);
		con.add(jlblStudentId);


		jlblStudentLogin = new JLabel("微易码学员登录");
		jlblStudentLogin.setFont(new Font("楷体",Font.PLAIN,35));
		jlblStudentLogin.setBounds(202,0,248,40);
		con.add(jlblStudentLogin);
		jfrmStudentLogin.setVisible(true);
		
	}
	private void dealAction()
	{
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// 退出本功能窗口
					jfrmStudentLogin.dispose();
				}
			}
		);
		
		jbtnConfirm.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					String loginTime = getSystemTime();
					String loginId = jtxtStudentId.getText();
					String loginStatus = "1";
					
					if(judgeId(loginId))
					{
						System.out.println("密码正确进入");
						isOnline(loginId);
						insertLoginInforMation(loginTime, loginId,loginStatus);
						System.out.println("完成功能");
					}
					else
					{
						JOptionPane.showMessageDialog(jfrmStudentLogin, "输入学号有误，请重新输入");
					}
				}
			}
		);
	}
	private void reinitFrame()
	{
		
	}
	private String getSystemTime()
	{
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format( now ); 
	}
	private boolean judgeId(String id)
	{
		Pattern p = Pattern.compile("\\d{12}");
		Matcher m = p.matcher(id);
		return m.find();
	}
	private void insertLoginInforMation(String loginTime, String loginId,String loginStatus)
	{
		String SQLinsert = "INSERT INTO SYS_STU_LOGIN_INFORMATION VALUES ("+loginTime+","+loginId+","+loginStatus+")";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLinsert);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private void isOnline(String loginId)
	{
		System.out.println("进入了isoline");
		String SQLselect = "SELECT * FROM SYS_STU_LOGIN_INFORMATION WHERE STU_LOGIN_ID = " + loginId + "AND STU_LOGIN_STATUS = '1'";
		String loginTime = "";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet selectSet = FStatement.executeQueryCon(SQLselect);
			if(selectSet.next())
			{
				System.out.println("哇靠");
				loginTime = selectSet.getString("STU_LOGIN_TIME");
				modifyOnlineStatus(loginTime);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("退出了isoline");
	}
	
	private void modifyOnlineStatus(String loginTime)
	{
		System.out.println("进入了modifyOnlineStatus");
		String SQLupdate = "UPDATE SYS_STU_LOGIN_INFORMATION SET STU_LOGIN_STATUS = '0' WHERE STU_LOGIN_TIME = " + loginTime;
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLupdate);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("退出了modifyOnlineStatus");
	}
	public static void main(String[] args)
	{
		new StudentLogin();
	}

}