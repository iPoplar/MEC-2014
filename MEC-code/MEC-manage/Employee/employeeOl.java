
/*******************************************************

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class employeeOl 
{
	
		private JFrame jfrmMain;
		private Container con;
		
		private JLabel jlblTopic;/*标题*/
		
		private JLabel jlblEmployee;/*员工编号*/
		private JLabel jlblEmployeeNo;
		
		private JLabel jlblEmployeeName;/*员工姓名*/
		private JTextField jtxtEmployeeName;
		
		private JLabel jlblEmployeePassword;/*员工密码*/
		private JPasswordField jpwEmployeePassword;
		
		private JLabel jlblEmployeeStatus;/*状态标签*/
		private JRadioButton[] jrdbEmployeeStatus;/*设置单选按钮*/
		private ButtonGroup bgStatus;/*设置按钮群*/
		
		private JLabel jlblEmployeeId;/*身份证号码*/
		private JTextField jtxtEmployeeId;
		
		private JLabel jlblEmployeeTel;
		private JTextField jtxtEmployeeTel;/*联系方式*/
		
		private JLabel jlblEmployeeNationNo;/*民族*/
		private JComboBox jcmbEmployeeNationNo;/*制作下拉列表框*/
		
		private JLabel jlblEmployeeNativeNo;/*籍贯*/
		private JComboBox jcmbEmployeeNativeNo;
		
		private JLabel jlblEmployeeJobNo;	/*职务*/
		private JComboBox jcmbEmployeeJobNo;
		
		private JComboBox jcmbEmployeeStatus;
		private DefaultListModel dlmEmployeeList;/*员工选项列表框*/
		private JList jlstEmployeeList;
		private JScrollPane jscpEmployeeList;/*滚动条*/
		private JLabel jlblEmployeeAccount;/*人数统计*/
		
		private JButton jbtnExit;/*退出按钮*/
		private JButton jbtnAdd;/*添加按钮*/
		private JButton jbtnModify;/*修改按钮*/		
		
		private int i = 0;/*变量i，来完成列表的计数*/
		private int lastAction;/*用来记录最后一次操作*/		
		
		private static final int ADDATION = 1;/*添加状态常量*/
		private static final int MODIFY = 2;/*修改状态常量*/	
		
		private final int STATUS_BROWS = 0;		 /*状态——浏览*/
		private final int STATUS_EDIT = 1;		 /*状态——编辑*/

		public employeeOl ()
		{
			initFrame();
			reinitFrame();
			dealAction();	
		}
		
		/*******************************************************
		* Function Name: 	reinitFrame
		* Purpose: 			再次初始化员工信息
		
		*******************************************************/
		private void reinitFrame() 
		{
			employeeJCMBInforametionInit();//下拉状态列表初始化
			employeeListInit(0);//默认在职员工信息列表初始化
			employeeSomeInforametionInit();//民族、籍贯初始化
		}
		
		/*******************************************************
		* Function Name: 	setWorkStatus
		* Purpose: 			设置操作界面的状态
		* 传递的参数：（int）status；用来放置浏览和编辑两种状态
		********************************************************/
		private void setWorkStatus(int status)
		{
			boolean value = status == STATUS_BROWS;		// 设置value变量来保存需要更改的状态；如果需要为	浏览状态则value为TRUE，不可激活
														// 如果需要为编辑状态则value为	FALSE，可激活
			jlstEmployeeList.setEnabled(value);
			jtxtEmployeeName.setEditable(!value);
			jpwEmployeePassword.setEditable(!value);
			jtxtEmployeeId.setEnabled(!value);
			jcmbEmployeeNationNo.setEnabled(!value);
			jcmbEmployeeNativeNo.setEnabled(!value);
			jtxtEmployeeTel.setEnabled(!value);
			jcmbEmployeeJobNo.setEnabled(!value);
			jrdbEmployeeStatus[0].setEnabled(!value);
			jrdbEmployeeStatus[1].setEnabled(!value);	
			jrdbEmployeeStatus[2].setEnabled(!value);
		}
	
		/*******************************************************
		* Function Name: 	employeeJCMBInforametionInit
		* Purpose: 			下拉状态列表初始化
		
		*******************************************************/
		private void employeeJCMBInforametionInit()//下拉状态列表初始化
		{
			String SQLString = "SELECT EMPLOYEESTATUSNAME FROM sys_mec_status";
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString);
			
				String JcmStatus;
				
				while(rs.next())
				{
					System.out.println(rs.getString("EMPLOYEESTATUSNAME"));
					JcmStatus = rs.getString("EMPLOYEESTATUSNAME");
					jcmbEmployeeStatus.addItem(JcmStatus);	
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
				System.out.println(" 下拉状态列表初始化出错！");
			}
		}
			
		/*******************************************************
		* Function Name: 	employeeListInit
		* Purpose: 			员工信息列表初始化
		* 传递的参数：（int）Status ，用来初始化不同状态下的列表
		*******************************************************/
		private void employeeListInit(int Status)//员工信息列表初始化
		{
			String OkStatus = Status + ""; 
			try
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				String SQLString = "SELECT EMPLOYEENO, EMPLOYEENAME FROM believe WHERE EMPLOYEESTATUS='"+ OkStatus +"'";
				 FResultSet rs = FStatement.executeQueryCon(SQLString);
				System.out.println("员工初始化" + SQLString);
				dlmEmployeeList.removeAllElements();
				while(rs.next())
				{
					String No;
					String Name;
				
					No = rs.getString("EMPLOYEENO");
					Name = rs.getString("EMPLOYEENAME");
					System.out.println(No + " " + Name);
					dlmEmployeeList.addElement(No + " " + Name);
				}
				
				/*当前列表人数的统计*/
				int employeeCount = dlmEmployeeList.getSize();
				if(employeeCount > 0)
					jlstEmployeeList.setSelectedIndex(0);
				employeeInforametionInit(jlstEmployeeList.getSelectedValue().toString().substring(0, 0+8));
				jlblEmployeeAccount.setText("共" + employeeCount + "名学生");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try 
			{
				FStatement.disConnection();
			} catch (Exception e)
			{
				System.out.println("员工信息列表初始化错了");
				e.printStackTrace();
			}
		}
		
		/*******************************************************
		* Function Name: 	employeeInforametionInit
		* Purpose: 			初始化员工基本信息
		* 传递的参数： （String）link，用来选中的员工的信息
		*******************************************************/
		private void employeeInforametionInit(String link)//初始化员工基本信息（）
		{
			jlstEmployeeList.removeAll();
			String SQLString = "SELECT * FROM believe WHERE EMPLOYEENo ='"+link+"'";
			System.out.println(SQLString);
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				String No;				//员工编号、姓名、身份证号、联系方式、密码、
				String Name;			//民族、籍贯、职务的编号
				String Id;		
				String Tel;
				String Password;
				String ENation;
				String ENative;
				String EJob;
				String Status;
				
				while(rs.next())
				{
					No = rs.getString("EMPLOYEENO");
					jlblEmployeeNo.setText(No);
					
					Name = rs.getString("EMPLOYEENAME");
					jtxtEmployeeName.setText(Name);
					
					Id = rs.getString("EMPLOYEEID");
					jtxtEmployeeId.setText(Id);
					
					Tel = rs.getString("EMPLOYEETEL");
					jtxtEmployeeTel.setText(Tel);
					
					Password = rs.getString("EMPLOYEEPASSWORD");
					jpwEmployeePassword.setText(Password);
					
					Status = rs.getString("EMPLOYEESTATUS");
					jrdbEmployeeStatus[Integer.parseInt(Status)].setSelected(true);
			
					ENation = rs.getString("EMPLOYEENATIONNO");
					ENative = rs.getString("EMPLOYEENATIVENO");
					EJob = rs.getString("EMPLOYEEJOBNO");
					selectChange(ENation, ENative, EJob);//选中的籍贯、民族、职务添加
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
				System.out.println("初始化员工基本信息（）！");
			}
		}
		
		/*******************************************************
		* Function Name: 	employeeSomeInforametionInit
		* Purpose: 			民族、籍贯、职务初始化
		*******************************************************/
		private void employeeSomeInforametionInit()//民族、籍贯、职务初始化
		{
			String SQLString = "SELECT nation FROM sys_mec_nation";
			String SQLStringl = "SELECT native FROM sys_mec_native";
			String SQLString2 = "SELECT jobName FROM sys_mec_job ORDER BY JOBNO";	

			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				
				String Nation;
				String Native;
				String Job;
				
				while(rs.next())
				{
					Nation = rs.getString("NATION");
					jcmbEmployeeNationNo.addItem(Nation+"");
				}
				FResultSet rs1 = FStatement.executeQueryCon(SQLStringl); 
				
				while(rs1.next())
				{	
					Native = rs.getString("NATIVE");
					jcmbEmployeeNativeNo.addItem(Native+"");	
				}
				
				FResultSet rs2 = FStatement.executeQueryCon(SQLString2); 
				
				while(rs2.next())
				{
					Job = rs.getString("JOBNAME");
					jcmbEmployeeJobNo.addItem(Job+"");	
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
				MECTool.showMessage(jfrmMain,"民族、籍贯、职务出错！");
			}
		}
		
		/******************************************************
		* Function Name: 	selectChange
		* Purpose: 			添加选中的籍贯、民族、职务
		* 传递的参数：			（String）NationNo,NativeNo,JobNo；
		* 					用来找出选中的员工的籍贯、民族、职务编号
		*******************************************************/
		private void selectChange( String NationNo, String NativeNo, String JobNo)//选中的籍贯、民族、职务添加
		{
			String SQLString = "SELECT NATION FROM sys_mec_nation WHERE NATIONNO='"+ NationNo +"'";
			String SQLStringl = "SELECT NATIVE FROM sys_mec_native WHERE NATIVENO='"+ NativeNo +"' ";
			String SQLString2 = "SELECT JOBNAME FROM sys_mec_job WHERE JOBNO='"+ JobNo +"' ";	
	
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				
				String Nation;
				String Native;
				String Job;
				
				while(rs.next())
				{
					Nation = rs.getString("NATION");
					jcmbEmployeeNationNo.setSelectedItem(Nation);
				}
				FResultSet rs1 = FStatement.executeQueryCon(SQLStringl); 
				
				while(rs1.next())
				{
					Native = rs.getString("NATIVE");
					jcmbEmployeeNativeNo.setSelectedItem(Native);	
				}
				
				FResultSet rs2 = FStatement.executeQueryCon(SQLString2); 
				
				while(rs2.next())
				{
					Job = rs.getString("JOBNAME");
					jcmbEmployeeJobNo.setSelectedItem(Job);	
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
				System.out.println("选中的籍贯、民族、职务添加！");
			}		
		}
		
		/*******************************************************************
		* Function Name: 	CheckNative
		* Purpose: 			籍贯和身份证上的数据是否一致的验证
		* 参数：				String ID 在sys_mec_native表中用于查找选中的籍贯的籍贯编号
		* 返回值：				NativeName返回选中籍贯编号所对应的所在地名称
		********************************************************************/
		private String CheckNative(String ID)//籍贯和身份证上的数据是否一致的验证
		{
			String NativeName = null ;
			String SQLString = "SELECT NATIVE FROM sys_mec_native WHERE NATIVENO ='"+ ID +"' ";
			try {
					FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
					FResultSet rs = FStatement.executeQueryCon(SQLString); 
					
					while(rs.next())
					{
						NativeName = rs.getString("NATIVE");					
					}
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
			try {
				FStatement.disConnection();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
				return NativeName;
		}
		
		/*******************************************************************
		* Function Name: 	GetMaxCount
		* Purpose: 			制作员工编号， 统计表中当前天已添加的数据
		* 
		* 返回值：				K 记录当天添加的次数
		********************************************************************/
		private int GetMaxCount() 
		{
			int k = 0;
			String SQLString = "select employeeName ,employeeno from believe ";
			try {
					FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
					FResultSet rs = FStatement.executeQueryCon(SQLString);
					dlmEmployeeList.removeAllElements();
					
					String id;
					String sd= new SimpleDateFormat("yyMMdd").format(new java.util.Date());
					System.out.println(sd);
					while(rs.next())
					{											
						id = rs.getString("employeeno"); 		
						if(id.substring(0, 6) ==  sd )
						{
							k++;
						}
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
			return k ;
			
		}
		
		/*************************************************************
		* Function Name: 	setAddButtonStatus
		* Purpose: 			监控输入框，若为空，则"确定"按钮不可用
		**************************************************************/
		private void setAddButtonStatus()	
		{
			if(jtxtEmployeeName.getText().length() > 0)	// 当姓名输入框大于0时
				jbtnAdd.setEnabled(true);		// 添加/确定按钮为可见     / enabled为激活
			else								// 否则
				jbtnAdd.setEnabled(false);		// 添加/确定按钮为b不可见
		}
		
		/*************************************************************
		* Function Name: 	CheckIdentityCardNative
		* Purpose: 			身份证中籍贯的验证
		* 参数：				String id 选中的员工编号
		* 返回值：				boolean OK 验证结果
		**************************************************************/
		private boolean  CheckIdentityCardNative(String id)
		{
		     //籍贯的验证
			boolean OK  = true;
		    int j = 0;
		    String SQLString = "select * from SYS_MEC_NATIVE where nativeno = "+ id.substring(0, 2) +"";
		    try {
					FResultSet rs = FStatement.executeQueryCon(SQLString);
					while(rs.next())
					{
						j++;
					}
					if(j > 0)
						OK = true;
					else
						OK =  false;
						
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
			return OK;
		}
		
		/*************************************************************
		* Function Name: 	CheckIdentityCardYMD
		* Purpose: 			身份证中出生日期的验证
		* 参数：				String id 选中的员工编号
		* 返回值：				boolean OK 验证结果
		**************************************************************/
		private boolean  CheckIdentityCardYMD(String id)
		{
		
			//年月日的验证
			boolean OK  = true;
			Calendar c = Calendar.getInstance();

			c.set(Integer.parseInt(id.substring(6, 10)),Integer.parseInt(id.substring(10, 12)), Integer.parseInt(id.substring(12, 14)));
		        	
			if( c.get(Calendar.YEAR)==Integer.parseInt(id.substring(6, 10)) && c.get(Calendar.MONTH) == Integer.parseInt(id.substring(10, 12)) && c.get(Calendar.DATE) ==Integer.parseInt(id.substring(12, 14)))
				OK = true;
			else
				OK = false;
			return OK;
		}
		
		/*************************************************************
		* Function Name: 	CheckIdentityCardLast
		* Purpose: 			身份证中校验位的验证
		* 参数：				String id 选中的员工编号
		* 返回值：				boolean OK 验证结果
		**************************************************************/
		private boolean  CheckIdentityCardLast(String id)
		{
			//身份证校验位的验证
			boolean OK  = true;
			char pszSrc[]=id.toCharArray();
			int iS = 0;
			int iW[]={7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
			char[] szVerCode = new char[]{'1','0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
			int i;
			for(i=0;i<17;i++)
			{
				iS += (int)(pszSrc[i]-'0') * iW[i];
			}
			int   iY = iS%11;	
			
			if(id.substring(17, 18).equals(String.valueOf(szVerCode[iY])))
			   	OK = true;
			else
				OK = false;	
			return  OK;
				
		}
		
		/*******************************************************
		* Function Name: 	dealAction
		* Purpose: 			事件处理		
		*******************************************************/
		private void dealAction()
		{									/*退出的动作监听*/
			jbtnExit.addActionListener			/* 动作监听器设置退出时需执行的方法，即完成退出，和退出时的问候。*/
			(
				new ActionListener()			
				{
					public void actionPerformed(ActionEvent arg0)	 
					{
						MECTool.showMessage(jfrmMain, "再见");		// 在此调用MEC工具类中的showMessage静态方法来实现关闭前的提示语句。
						jfrmMain.dispose();		
					}				
				}
			);

			/*设置点击事件，当点击员工信息时，更新员工信息显示*/
			jlstEmployeeList.addListSelectionListener	
			(
				new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent arg0) 
					{
						if(jlstEmployeeList.getSelectedIndex()!= -1)
						{
							String selected = jlstEmployeeList.getSelectedValue().toString();
							jlblEmployeeNo.setText(selected.substring(0,0+8));
							String link = jlblEmployeeNo.getText();
							
							employeeInforametionInit(link);//调用员工信息列表初始化							
						}
					}
				}
				);
			
			/*对选中状态下拉列表的监听*/
			jcmbEmployeeStatus.addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0) 
						{
							int Status ;
							Status = jcmbEmployeeStatus.getSelectedIndex();
					
							//根据被选中的值，初始化Jlist
							dlmEmployeeList.removeAllElements();
							employeeListInit(Status);
						}
					}
			);
			
			/*  点击"修改"按钮：将"添加"改为"确定"；将"修改"改为"放弃"；*/
			jbtnModify.addActionListener
			(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						if(jbtnModify.getText().equals("修改"))	// 当jbtnAdd按钮为修改时执行
						{		
							jbtnAdd.setText("确定");				// "添加"改为"确定"
							jbtnModify.setText("放弃");			// "修改"改为"放弃"
							
							setWorkStatus(STATUS_EDIT);			// 改为编辑状态
							setAddButtonStatus();				// 设置按钮是否可见（在变换到编辑状态之前）
							jtxtEmployeeName.selectAll();		// 将文本框中的数据选中
							jtxtEmployeeName.requestFocus();	// 请求焦点到姓名输入文本框
							lastAction = MODIFY;				//记录最后一次操作是"修改"
							
							employeeListInit(0);
						}
						else									// 当jbtnAdd按钮为非修改时执行
						{
							employeeListInit(0);
							jbtnAdd.setText("添加");				// 确定改为添加
							jbtnModify.setText("修改");			// 放弃改为修改
							jbtnAdd.setEnabled(true);			// 将添加按钮激活，以免在此添加时，而又因文本框为空时，不能点击添加按钮，因为添加和确定是同一个按钮，在不同状态下的表现
							//employeeListInit(0);				//员工信息列表初始化
							jlstEmployeeList.removeAll();
							setWorkStatus(STATUS_BROWS);		// 改变状态为浏览
						}
					}
				}
			);
						
				/* 点击"添加"按钮：将添加改为"确定"；将"修改"改为"放弃"；*/
				jbtnAdd.addActionListener
				(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e) 
						{
							if(jbtnAdd.getText().equals("添加"))		// 当jbtnAdd按钮为"添加"时执行
							{
								jbtnAdd.setText("确定");				// "添加"改为"确定"
								jbtnModify.setText("放弃");			// "修改"改为"放弃"
								
								setWorkStatus(STATUS_EDIT);			// 改变状态为编辑
								setAddButtonStatus();				// 设置按钮是否可见（在变换到编辑状态之前）
								jtxtEmployeeName.selectAll();		// 将文本框中的数据选中
								jtxtEmployeeName.requestFocus();	// 请求焦点到姓名输入文本框
								jtxtEmployeeName.setText("");		//清空姓名文本框
								jtxtEmployeeTel.setText("");		//清空联系方式文本框
								jpwEmployeePassword.setText("");	//清空密码文本框
								jtxtEmployeeId.setText("");			//清空身份证号文本框		
								jlblEmployeeNo.setText("");
								
								lastAction = ADDATION;
							}
							else									// 当jbtnAdd按钮为"确定"时执行
							{
								jbtnAdd.setText("添加");				// "确定"改为"添加"
								jbtnModify.setText("修改");
								setWorkStatus(STATUS_BROWS);		// 改变状态为浏览
								
								if(lastAction == ADDATION)
								{	
									boolean m , n, p, q;
									
									//姓名的正则判断
									m = Pattern.matches("[a-zA-Z\u4e00-\u9fa5]{2,10}" ,jtxtEmployeeName.getText() );
									//密码的正则判断
									n = Pattern.matches("[a-zA-Z0-9]{6,12}" , String.valueOf(jpwEmployeePassword.getPassword()));
									//身份证位数的正则判断
									p =Pattern.matches( "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$" ,jtxtEmployeeId.getText());
									//手机号码的正则判断
									q = Pattern.matches("^1[3|5|8|][0-9]{9}$",jtxtEmployeeTel.getText());
								
									if(m && n && p && q )
									{
										String sdf = new SimpleDateFormat("yyMMdd").format(new java.util.Date());
									
										int  y  = GetMaxCount() + 101;		//获取员工编号
										String Id = y + "";
										String IDD = sdf + Id.toString().substring(1,1+2) ;
										System.out.println(IDD);
										
										String AddName = jtxtEmployeeName.getText();
										String AddId = jtxtEmployeeId.getText();
										String AddNation = jcmbEmployeeNationNo.getSelectedItem().toString();
										String AddNative = (jcmbEmployeeNativeNo.getSelectedItem().toString());
										String AddJob = (jcmbEmployeeJobNo.getSelectedItem().toString());
										int AddPassword = jlblEmployeePassword.getText().hashCode();
										String AddTel = jtxtEmployeeTel.getText();
											
										/*数据的添加*/
										if(CheckIdentityCardNative(jtxtEmployeeId.getText()) && CheckIdentityCardYMD(jtxtEmployeeId.getText()) && CheckIdentityCardLast(jtxtEmployeeId.getText())
												&& jcmbEmployeeNativeNo.getSelectedItem().equals(CheckNative(jtxtEmployeeId.getText().toString().substring(0, 2))))	
										{
											String AddNationNo;
											String AddNativeNo;
											String AddJobNo;
											String SQLString1 = "SELECT NATIONNO FROM SYS_MEC_NATION WHERE LIKE '"+AddNation+"'";
											String SQLString2 = "SELECT NATIVENO FROM SYS_MEC_NATIVE WHERE LIKE '"+AddNative+"'";
											String SQLString3 = "SELECT JOBNO FROM SYS_MEC_JOB WHERE JOBNAME ='"+AddJob+"'"	;
											
											try
											{	//由所选的内容获得名称所对应的编号（民族、籍贯、职务）
												FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
												
												FResultSet rs1 = FStatement.executeQueryCon(SQLString1);
												rs1.next();
												AddNationNo = rs1.getString(0);
												
												FResultSet rs2 = FStatement.executeQueryCon(SQLString2);
												rs2.next();
												AddNativeNo = rs2.getString(0);
												
												FResultSet rs3 = FStatement.executeQueryCon(SQLString3);
												rs3.next();
												AddJobNo = rs3.getString(0);
												
												String SQLString = "INSERT INTO BELIEVE VALUES ('"+IDD+"','"+AddName+"','"+AddId+"','"+AddNationNo+"','"+AddNativeNo+"','"+AddJobNo+"','"+AddPassword+",'"+AddTel+"','0')";
												FStatement.executeUpdateCon(SQLString);
												
											}
											catch (SQLException a) 
											{
												MECTool.showMessage(jfrmMain, a.getMessage() + "请呼唤数据库管理员前来处理该问题。" +
												"\n对此微易码科技深表歉意！");
												a.printStackTrace();
											}
											try 
											{
												FStatement.disConnection();
											} catch (Exception a)
											{
												MECTool.showMessage(jfrmMain, "数据库关闭异常");
												a.printStackTrace();
											}
										}
										else
										{
											MECTool.showMessage(jfrmMain, "身份证号输入有误，或者身份证与籍贯信息不符，请查证后重新输入");
										}
									}
									else
									{
										MECTool.showMessage(jfrmMain, "姓名或者密码或者身份证号位数或者电话号码输入不符合要求，请查证后重新输入");
									}
										employeeListInit(0);
								}
								
								else  //lastAction == MODIFY
								{
									//  完成修改操作；指数据入库！
										
									String AddNo = jlblEmployeeNo.getText();
									String AddName = jtxtEmployeeName.getText();
									String AddId = jtxtEmployeeId.getText();
									String AddNation = jcmbEmployeeNationNo.getSelectedItem().toString();
									String AddNative = (jcmbEmployeeNativeNo.getSelectedItem().toString());
									String AddJob = (jcmbEmployeeJobNo.getSelectedItem().toString());
									int AddPassword = jlblEmployeePassword.getText().hashCode();
									String AddTel = jtxtEmployeeTel.getText();
									
									if(CheckIdentityCardNative(jtxtEmployeeId.getText()) && CheckIdentityCardYMD(jtxtEmployeeId.getText()) && CheckIdentityCardLast(jtxtEmployeeId.getText())
										&& jcmbEmployeeNativeNo.getSelectedItem().equals(CheckNative(jtxtEmployeeId.getText().toString().substring(0, 2))))	
									{
										String AddNationNo;
										String AddNativeNo;
										String AddJobNo;
										String SQLString1 = "SELECT NATIONNO FROM SYS_MEC_NATION WHERE LIKE '"+AddNation+"'";
										String SQLString2 = "SELECT NATIVENO FROM SYS_MEC_NATIVE WHERE LIKE '"+AddNative+"'";
										String SQLString3 = "SELECT JOBNO FROM SYS_MEC_JOB WHERE JOBNAME ='"+AddJob+"'"	;
										
										try
										{	//由所选的内容获得名称所对应的编号（民族、籍贯、职务）
											FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
											
											FResultSet rs1 = FStatement.executeQueryCon(SQLString1);
											rs1.next();
											AddNationNo = rs1.getString(0);
											
											FResultSet rs2 = FStatement.executeQueryCon(SQLString2);
											rs2.next();
											AddNativeNo = rs2.getString(0);
											
											FResultSet rs3 = FStatement.executeQueryCon(SQLString3);
											rs3.next();
											AddJobNo = rs3.getString(0);
											
											String SQLString = "UPDETE BELIEVE SET employeeNo,employeeName,employeeId,employeeNationNo,employeeNativeNo,employeeJobNo,employeePassword,employeeTel,employeeStatus  VALUES ('"+AddNo+"','"+AddName+"','"+AddId+"','"+AddNationNo+"','"+AddNativeNo+"','"+AddJobNo+"','"+AddPassword+"','"+AddTel+"','0')";
											FStatement.executeUpdateCon(SQLString);
										}
										catch (SQLException a) 
										{
											MECTool.showMessage(jfrmMain, a.getMessage() + "请呼唤数据库管理员前来处理该问题。" +
											"\n对此微易码科技深表歉意！");
											a.printStackTrace();
										}
										try 
										{
											FStatement.disConnection();
										} catch (Exception a)
										{
											MECTool.showMessage(jfrmMain, "数据库关闭异常");
											a.printStackTrace();
										}
									}
									else
									{
										MECTool.showMessage(jfrmMain, "身份证号输入有误，或者身份证与籍贯信息不符，请查证后重新输入");
									}
								}
								
							}
						}
					}
				);
		}
			
		
		
		/*************************************************************
		* Function Name: 	initFrame
		* Purpose: 			界面的初始化
		**************************************************************/
		private void initFrame()
			{

				Dimension dim;// 尺寸用以获取默认屏幕大小
				
				Font normalFont, StatusFont;
				
				normalFont = new Font("微软雅黑",Font.PLAIN,16);
				StatusFont = new Font("微软雅黑",Font.PLAIN,13); 
				
				jfrmMain = new JFrame("员工信息管理");
				con = jfrmMain.getContentPane();
				con.setLayout(null);	//设置布局
				
				jfrmMain.setSize(7500/15,6405/15);
				dim = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕尺寸
				jfrmMain.setLocation((dim.width-jfrmMain.getWidth())/2,(dim.height-jfrmMain.getHeight())/2);// 设置框架位置
				jfrmMain.setVisible(true);
				jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置退出默认
				
				jlblTopic = new JLabel("员工信息管理");
				jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,24));//设置标题字体
				jlblTopic.setForeground(Color.BLUE);
				jlblTopic .setBounds(2782/15, 0, 2160/15, 465/15);
				con.add(jlblTopic);
				
				jlblEmployee = new JLabel("员工编号");
				jlblEmployee.setFont(normalFont);
				jlblEmployee.setBounds(3600/15, 765/15, 960/15, 240/15);
				con.add(jlblEmployee);
				
				jlblEmployeeNo = new JLabel("");
				jlblEmployeeNo.setFont(StatusFont);
				jlblEmployeeNo.setBounds(4680/15, 765/15, 960/15, 240/15);
				con.add(jlblEmployeeNo);
				
				jlblEmployeeName = new JLabel("姓      名");
				jlblEmployeeName.setFont(normalFont);
				jlblEmployeeName.setBounds(3600/15, 1200/15, 960/15, 240/15);
				con.add(jlblEmployeeName);

				jtxtEmployeeName = new JTextField();
				jtxtEmployeeName.setFont(StatusFont);
				jtxtEmployeeName.setBounds(4680/15, 1133/15, 2415/15, 375/15);
				con.add(jtxtEmployeeName);

				jlblEmployeePassword = new JLabel("密      码");
				jlblEmployeePassword.setFont(normalFont);
				jlblEmployeePassword.setBounds(3600/15, 1695/15, 960/15, 240/15);
				con.add(jlblEmployeePassword);

				jpwEmployeePassword = new JPasswordField();
				jpwEmployeePassword.setFont(StatusFont);
				jpwEmployeePassword.setBounds(4680/15, 1628/15, 2415/15, 375/15);
				con.add(jpwEmployeePassword);

				jlblEmployeeNationNo = new JLabel("民      族");
				jlblEmployeeNationNo.setFont(normalFont);
				jlblEmployeeNationNo.setBounds(3600/15, 2220/15, 960/15, 240/15);
				con.add(jlblEmployeeNationNo);

				jcmbEmployeeNationNo = new JComboBox();
				jcmbEmployeeNationNo.setFont(StatusFont);
				jcmbEmployeeNationNo.setBounds(4680/15, 2160/15, 2415/15, 365/15);
				con.add(jcmbEmployeeNationNo);


				jlblEmployeeNativeNo = new JLabel("籍      贯");
				jlblEmployeeNativeNo.setFont(normalFont);
				jlblEmployeeNativeNo.setBounds(3600/15, 2700/15, 960/15, 240/15);
				con.add(jlblEmployeeNativeNo);
				
				jcmbEmployeeNativeNo = new JComboBox();
				jcmbEmployeeNativeNo.setFont(StatusFont);
				jcmbEmployeeNativeNo.setBounds(4680/15, 2640/15, 2415/15, 365/15);
				con.add(jcmbEmployeeNativeNo);		
			
				jlblEmployeeId = new JLabel("身份证号");
				jlblEmployeeId.setFont(normalFont);
				jlblEmployeeId.setBounds(3600/15, 3185/15, 960/15, 240/15);
				con.add(jlblEmployeeId);

				jtxtEmployeeId = new JTextField();
				jtxtEmployeeId.setFont(StatusFont);
				jtxtEmployeeId.setBounds(4680/15, 3120/15, 2415/15, 375/15);
				con.add(jtxtEmployeeId);

				jlblEmployeeJobNo = new JLabel("职      务");
				jlblEmployeeJobNo.setFont(normalFont);
				jlblEmployeeJobNo.setBounds(3600/15, 3660/15, 960/15, 240/15);
				con.add(jlblEmployeeJobNo);
				
				jcmbEmployeeJobNo = new JComboBox();
				jcmbEmployeeJobNo.setFont(StatusFont);
				jcmbEmployeeJobNo.setBounds(4680/15, 3600/15, 2415/15, 365/15);
				con.add(jcmbEmployeeJobNo);	
				
				
				jlblEmployeeTel = new JLabel("联系方式");
				jlblEmployeeTel.setFont(normalFont);
				jlblEmployeeTel.setBounds(3600/15, 4147/15, 960/15, 240/15);
				con.add(jlblEmployeeTel);

				jtxtEmployeeTel = new JTextField();
				jtxtEmployeeTel.setFont(StatusFont);
				jtxtEmployeeTel.setBounds(4680/15, 4080/15, 2415/15, 375/15);
				con.add(jtxtEmployeeTel);		
						
				jlblEmployeeStatus = new JLabel("状      态");
				jlblEmployeeStatus.setFont(normalFont);
				jlblEmployeeStatus.setBounds(3600/15, 4687/15, 960/15, 240/15);
				con.add(jlblEmployeeStatus);
				
				bgStatus = new ButtonGroup();
				jrdbEmployeeStatus = new JRadioButton[3];
				
				jrdbEmployeeStatus[0] = new JRadioButton("在职");		
				jrdbEmployeeStatus[0].setFont(normalFont);
				jrdbEmployeeStatus[0].setBounds(5680/15-70, 3560/15+67,895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[0]);
				con.add(jrdbEmployeeStatus[0]);
				
				jrdbEmployeeStatus[1] = new JRadioButton("停职");
				jrdbEmployeeStatus[1].setFont(normalFont);
				jrdbEmployeeStatus[1].setBounds(5680/15-13, 3560/15+67, 895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[1]);
				con.add(jrdbEmployeeStatus[1]);
				
				jrdbEmployeeStatus[2] = new JRadioButton("离职");
				jrdbEmployeeStatus[2].setFont(normalFont);
				jrdbEmployeeStatus[2].setBounds(5680/15+45, 3560/15+67, 895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[2]);
				con.add(jrdbEmployeeStatus[2]);
				
				jbtnAdd = new JButton("添加");
				jbtnAdd.setFont(StatusFont);
				jbtnAdd.setBounds(3615/15, 5280/15, 975/15, 495/15);
				con.add(jbtnAdd);
				
				jbtnModify = new JButton("修改");
				jbtnModify.setFont(StatusFont);
				jbtnModify.setBounds(4560/15, 5280/15, 975/15, 495/15);
				con.add(jbtnModify);
				
				jbtnExit = new JButton("退出");
				jbtnExit.setFont(StatusFont);
				jbtnExit.setBounds(6105/15, 5280/15, 975/15, 495/15);
				con.add(jbtnExit);
				
				jcmbEmployeeStatus = new JComboBox();
				jcmbEmployeeStatus.setFont(normalFont);
				jcmbEmployeeStatus.setBounds(135/15, 840/15, 3135/15, 360/15);
				con.add(jcmbEmployeeStatus);
				
				dlmEmployeeList = new DefaultListModel();
				jlstEmployeeList = new JList(dlmEmployeeList);
				jscpEmployeeList = new JScrollPane(jlstEmployeeList);
				jlstEmployeeList.setFont(normalFont);
				jscpEmployeeList.setBounds(135/15, 240/15+62, 3135/15, 3660/15);
				con.add(jscpEmployeeList);
				
				jscpEmployeeList.setBorder(BorderFactory.createTitledBorder("员工列表"));//边框工厂
				
				jlblEmployeeAccount = new JLabel("共"+i+"名员工");
				jlblEmployeeAccount.setFont(normalFont);
				jlblEmployeeAccount.setBounds(135/15, 5160/15-20, 3135/15, 255/15);
				con.add(jlblEmployeeAccount);
				
				jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jfrmMain.setVisible(true);
					
			
		}
		public static void main(String[] args)
		{
			new employeeOl();
		}	
}