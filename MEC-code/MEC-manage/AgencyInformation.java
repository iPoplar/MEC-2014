************************************************************************
 *Function List:
 *	AgencyInformation()					构造方法
 *	AddAgencyInformation()				向数据库添加数据
 *	CreateNo()							自动生成代理编号
 *	dealAction()						监听事件
 *	Id18()								校验身份证第18位
 *	initAdd()							刷新院校、籍贯、组长编号
 *	initAgency(String)					初始化代理界面信息
 *	initAgencyList(String String)		初始化代理列表信息
 *	initInternalFrame()					创建界面
 *	initSector()						初始化选择下拉列表
 *	initSelected2()						初始化在、离职、全部代理下拉列表
 *	SelectSex()							判断男女，返回“0”或“1”
 *	showMessage(JInternalFrame,String)	调试用的显示方法
 *	updateAgency()						修改数据库
 *	verifyId()							校验身份证号
 *	verifyName()						校验姓名
 ****************************************************************************
 *All		常量表示所有代理
 *At		常量表示在职代理
 *Out		常量表示离职代理
 *BROWSE	常量表示浏览状态
 *EDIT		常量表示编辑状态
 *flagU		全局变量	控制院校下拉列表监听事件的执行与否
 *flagD 	全局变量	控制院系下拉列表监听事件的执行与否
*/	

//java所用到的包
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AgencyInformation extends JFrame 
{
	//子窗体
	private JInternalFrame jitfAgency;
	private Container con;
	private Container conAgency;
	private Font normalFont;
	
	//标题
	private JLabel jlblTopic;
	
	//分点
	private JLabel jlblSector;
	private JComboBox jcmbSector;
	
	//选择在、离职代理下拉列表
	private JComboBox jcmbStatus;
	
	//代理列表
	private DefaultListModel dlmAgency;
	private JList jlstAgency;
	private JScrollPane jscpAgency;
	
	//代理数量
	private JLabel jlblAgencyNumber;
	
	//代理编号
	private JLabel jlblAgencyNo;
	private JLabel jlblNo;
	
	//所属院校、系、专业
	private JLabel jlblUniversity;
	private JLabel jlblDepartment;
	private JLabel jlblMajor;
	private JComboBox[] jcmbUDM = new JComboBox[3];
	
	//姓名
	private JLabel jlblName;
	private JTextField jtxtName;
	
	//性别
	private JLabel jlblSex;
	private JRadioButton[] jrdbSex = new JRadioButton[2];
	private ButtonGroup btgpSex;
	
	//身份证号
	private JLabel jlblId;
	private JTextField jtxtId;
	
	//籍贯
	private JLabel jlblNative;
	private JComboBox jcmbNative;
	
	//状态
	private JLabel jlblStatus;
	private JRadioButton[] jrdbStatus = new JRadioButton[2];
	private ButtonGroup btgpStatus;
	
	//组长编号
	private JLabel jlblManager;
	private JComboBox jcmbManager;
	
	//添加按钮
	private JButton jbtnAdd;
	
	//修改按钮
	private JButton jbtnModify;
	
	//退出按钮
	private JButton jbtnExit;
	
	private int bool;
	
	//选择在、离职代理常量
	private static final String At = "0";
	private static final String All = "2";
	private static final String Out = "1";
	
	//状态常量
	private static final int BROWSE = 1;
	private static final int EDIT = 0;
	
	//监听事件标志
	private boolean flagU = false;
	private boolean flagD;
	
	//构造方法
	public AgencyInformation()	
	{
		initInternalFrame();
		initSelected2();
		initSector();
		dealAction();				
	}
	
	/**************************************************
	 * Function Name: dealAction
	 * Purpose: 所有的事件监听
	 * Params: 无参
	 * Return: 无返回值
	 **************************************************/
	public void dealAction()	
	{
		jbtnAdd.addActionListener	//添加按钮点击事件
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					//showMessage(jitfAgency,jbtnAdd.getText());
					if(jbtnAdd.getText().equals("添加"))
					{
						jcmbStatus.setSelectedIndex(0);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						SetStatus(EDIT);
						jlblNo.setVisible(false);
						jlblAgencyNo.setVisible(false);
						initAdd();
					}
					else	//确定
					{
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
						if(jcmbUDM[0].isEnabled())	//添加的确定
						{
							if(verifyId() && verifyName())
							{
								AddAgencyInformation();
							}
							else
							{
								showMessage(jitfAgency,"添加内容不正确！");
							}
					
							SetStatus(BROWSE);
							jlblAgencyNo.setVisible(true);
							jlblNo.setVisible(true);
						}
						else	//修改的确定
						{
							updateAgency();
							jrdbStatus[0].setEnabled(false);
							jrdbStatus[1].setEnabled(false);
							SetStatus(BROWSE);
						}
					}
				}
				
			}
		);
		
		jbtnModify.addActionListener	//修改按钮点击事件
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jbtnModify.getText().equals("修改"))
					{
						if(jrdbStatus[0].isSelected())
						{
							bool = 0;
						}
						else
							bool = 1;
						
						jrdbStatus[0].setEnabled(true);
						jrdbStatus[1].setEnabled(true);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						jlstAgency.setEnabled(false);
					}
					else	//放弃
					{
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
						
						if(jcmbUDM[0].isEnabled())	//添加的放弃
						{
							initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
							SetStatus(BROWSE);
							jlblAgencyNo.setVisible(true);
							jlblNo.setVisible(true);
						}
						else	//修改的放弃
						{
							if(bool == 0)
								jrdbStatus[0].setSelected(true);
							else
							{
								jrdbStatus[1].setSelected(true);
							}
							jrdbStatus[0].setEnabled(false);
							jrdbStatus[1].setEnabled(false);
							SetStatus(BROWSE);
						}
					}
				}
			}
		);
	
		jlstAgency.addMouseListener		//代理信息列表鼠标选择监听事件
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}

				public void mouseReleased(MouseEvent arg0) 
				{
					initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
				}
			}
		);
		
		jcmbUDM[1].addActionListener	//院系下拉列表监听事件
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(flagD)
					{
						jcmbUDM[2].removeAllItems();
						String strDM;
						strDM = ((String) jcmbUDM[1].getSelectedItem()).substring(0,4);
						String SQLStringM = "SELECT * FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID,1,4) = '" + strDM + "' AND SUBSTR(UDMID,5) != '00' AND UDMSTATUS = '0'";
						
						try 
						{
							String strMajor;
							String strMNo;
							FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
							
							FResultSet rsM = FStatement.executeQueryCon(SQLStringM); 
							
							while(rsM.next())
							{
								strMajor = rsM.getString("UDMNAME");
								strMNo = rsM.getString("UDMID");
								jcmbUDM[2].addItem(strMNo + strMajor);
							}
						} catch (SQLException se) 
						{
							se.printStackTrace();
						}
						
						try 
						{
							FStatement.disConnection();
						} catch (Exception fe) 
						{
							System.out.println("关闭异常！");
						}

					}
				}
			}
		);
		
		jcmbUDM[0].addItemListener	//院校下拉列表监听事件
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent e) 
				{
					if(jcmbUDM[0].getSelectedIndex() != -1 && flagU)
					{
						flagD = false;
						jcmbUDM[1].removeAllItems();
						String strU;
						strU = ((String) jcmbUDM[0].getSelectedItem()).substring(0,2);
						String SQLStringD = "SELECT * FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID,1,2) = '" + strU + "' AND SUBSTR(UDMID,3,2) != '00' AND SUBSTR(UDMID,5) = '00' AND UDMSTATUS = '0'";
					
						try 
						{
							FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
						
							String strDepartment;
							String strDNo;
							FResultSet rsD = FStatement.executeQueryCon(SQLStringD); 
						
							while(rsD.next())
							{
								strDepartment = rsD.getString("UDMNAME");
								strDNo = rsD.getString("UDMID");
								jcmbUDM[1].addItem(strDNo + strDepartment);
							}
						} catch (SQLException se) 
						{
							se.printStackTrace();
						}
					
						try 
						{
							FStatement.disConnection();
						} catch (Exception fe) 
						{
						System.out.println("关闭异常！");
						}
					
						flagU = false;
						flagD = true;
					}
				}
			}
		);
		
		jcmbStatus.addItemListener	//选择全部、在、离职代理下拉列表监听事件
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent e) 
				{
					initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
				}
			}
		);
		
		jcmbSector.addItemListener	//选择下拉列表选项改变监听
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent arg0) 
				{
					initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
				}
			}
		);
		
		jbtnExit.addActionListener	//退出按钮
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					dealClose();
				}
			}
		);
		
		this.addWindowListener		//窗口右上角关闭
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
					dealClose();
				}
			}
		); 
	}
	
	/*****************************************************************
	 * Function Name: updateAgency
	 * Purpose: 用SQL语句修改数据库的信息
	 * Params: 无参
	 * Return: 无返回值
	 *****************************************************************/
	public void updateAgency()	//修改数据库
	{
		String strNo;
		String strStatus;
		
		strNo = jlblNo.getText();
		if(jrdbStatus[0].isSelected())
			strStatus = "0";
		else
			strStatus = "1";
		
		String SQLString = "UPDATE SYS_MEC_AGENCY SET AGENCYSTATUS = '" + strStatus + "' WHERE AGENCYNO = '" + strNo + "'";
		
		try
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLString); 
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			System.out.println("关闭异常！");
		}
	}
	
	/**********************************************
	 * Function Name: initAdd
	 * Purpose: 当点击添加按钮时初始化院校，系专业下拉列表
	 * Params: 无参
	 * Return: 无返回值
	 **********************************************/
	public void initAdd()
	{
		String SQLStringU = "SELECT * FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID,1,2) != '00' AND SUBSTR(UDMID,3) = '0000' AND UDMSTATUS = '0'";
		String SQLStringN = "SELECT * FROM SYS_MEC_NATIVE";
		String SQLStringMA = "SELECT * FROM SYS_MEC_AGENCY WHERE AGENCYMANAGERNO = '00000000000'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			//刷新院校
			FResultSet rsU = FStatement.executeQueryCon(SQLStringU); 
			
			String strUniversity;
			String strUNo;
			while(rsU.next())
			{
				strUniversity = rsU.getString("UDMNAME");
				strUNo = rsU.getString("UDMID");
				jcmbUDM[0].addItem(strUNo + " " + strUniversity);
			}
			
			flagU = true;
			
			//刷新籍贯
			FResultSet rsN = FStatement.executeQueryCon(SQLStringN);
			
			String strNative;
			String strNNo;
			while(rsN.next())
			{
				strNative = rsN.getString("NATIVE");//*******************************
				strNNo = rsN.getString("NATIVENO");
				jcmbNative.addItem(strNNo + strNative);
			}
			
			//刷新组长
			FResultSet rsMA = FStatement.executeQueryCon(SQLStringMA);
			
			String strManager;
			String strMANo;
			while(rsMA.next())
			{
				strManager = rsMA.getString("AGENCYNO");//*******************************
				jcmbManager.addItem(strManager);
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
			System.out.println("关闭异常！");
		}
	}
	
	/*******************************************************
	 * Function Name: AddAgencyInformation
	 * Purpose: 向数据库添加数据
	 * Params: 无参
	 * Return: 无返回值
	 *******************************************************/
	public void AddAgencyInformation()		
	{
		String strNo;		//代理编号
		String strName;		//代理姓名
		String strId;		//代理身份证号
		String strUDM;		//代理院校编号
		String strNative;	//代理籍贯编号
		String strManager;	//代理组长编号
		String strStatus;	//代理状态
		
		strNo = CreateNo();
		strName = jtxtName.getText();
		strId = jtxtId.getText();
		strUDM = ((String) jcmbUDM[2].getSelectedItem());
		strUDM = strUDM.substring(0,6);
		strNative = (String) jcmbNative.getSelectedItem();
		strNative = strNative.substring(0,2);
		strManager = (String) jcmbManager.getSelectedItem();
		
		if(jrdbStatus[0].isSelected())
			strStatus = "0";
		else 
			strStatus = "1";
		
		//写入数据库

		String SQLString = "INSERT INTO SYS_MEC_AGENCY (AGENCYNO,AGENCYNAME,AGENCYID,UDMID,AGENCYNATIVENO,AGENCYMANAGERNO,AGENCYSTATUS) VALUES ('" + strNo +"','" + strName + "','" + strId + "','" + strUDM + "','" + strNative + "','" + strManager + "','" + strStatus + "')";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FStatement.executeUpdateCon(SQLString); 
			dlmAgency.addElement(strNo + strName);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			System.out.println("关闭异常！");
		}
	}
	
	/*********************************************************
	 * Function Name: verifyName
	 * Purpose: 判断姓名是否符合要求
	 * Params: 无参
	 * Return: 返回Boolean类型，姓名真确时返回true，否则返回false
	 *********************************************************/
	public boolean verifyName()
	{
		boolean b = false;
		
		String strName;
		strName = jtxtName.getText();
		int i = strName.length();
		
		if(i >= 2 && i <= 10)
			b = true;

		return b;
	}
	
	/***********************************************************
	 * Function Name:CreateNo
	 * Purpose: 根据代理列表的数量自动生成代理编号
	 * Params: 无参
	 * Return: 返回一个String类型的代理编号
	 ***********************************************************/
	public String CreateNo()	//自动生成代理编号
	{
		String strNo = null;
		String strSector; //分点编号
		String strUniversity;//学校编号
		String strDateYear;//当前年
		Calendar Date = Calendar.getInstance();
		String strDateMonth;//当前月
		Calendar Month = Calendar.getInstance();
		String strcon;//序号
		
		int i = Month.get(Calendar.MONTH);
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
		strDateMonth = sdf.format(d);
		strDateYear = (Date.get(Calendar.YEAR) + "").substring(2,4);
		strSector = (String) jcmbSector.getSelectedItem();
		strUniversity = ((String) jcmbUDM[0].getSelectedItem()).substring(0,2);
		strcon = ((dlmAgency.getSize() + 1001)  + "").substring(1);
		strNo = strSector + strUniversity + strDateYear + strDateMonth + strcon; 
		
		return strNo;
	}
	
	/***************************************************************
	 * Function Name:SetStatus
	 * Purpose: 设置界面控件的可用性
	 * Params: 整型常量参数，BROWSE和EDIT
	 * Return: 无返回值
	 ***************************************************************/
	public void SetStatus(int status)
	{
		boolean BE = status == BROWSE;
		jcmbUDM[0].setEnabled(!BE);
		jcmbUDM[1].setEnabled(!BE);
		jcmbUDM[2].setEnabled(!BE);
		jtxtName.setEnabled(!BE);
		jrdbSex[0].setEnabled(!BE);
		jrdbSex[1].setEnabled(!BE);
		jtxtId.setEnabled(!BE);
		jcmbNative.setEnabled(!BE);
		jrdbStatus[0].setEnabled(!BE);
		jrdbStatus[1].setEnabled(!BE);
		jcmbManager.setEnabled(!BE);
		
		jcmbSector.setEnabled(BE);
		jcmbStatus.setEnabled(BE);
		jlstAgency.setEnabled(BE);
	}
	
	/**************************************************************
	 * Function Name: initAgency
	 * Purpose: 根据代理列表的选中项初始化界面信息
	 * Params: String类型的参数，代理列表的选中项的前11个字符
	 * Return: 无返回值
	 **************************************************************/
	public void initAgency(String strNo)	
	{
		String udmId = null;
		try 
		{
			String SQLString1 = "SELECT * FROM SYS_MEC_AGENCY,SYS_MEC_NATIVE WHERE AGENCYNO = '" + strNo + "' AND AGENCYNATIVENO = NATIVENO";
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString1); 
			
			String Name;
			String Id;
			String NativeName;
			String NativeId;
			String Status;
			String ManagerNo;
			
			while(rs.next())
			{
				Name = rs.getString("AGENCYNAME");
				Id = rs.getString("AGENCYID");
				NativeName = rs.getString("NATIVE");
				NativeId = rs.getString("NATIVENO");
				Status = rs.getString("AGENCYSTATUS");
				ManagerNo = rs.getString("AGENCYMANAGERNO");
				udmId = rs.getString("UDMID");

				jlblNo.setText(strNo);
				jtxtName.setText(Name);
				jtxtId.setText(Id);
				
				int IdSex;
				IdSex = Integer.parseInt(Id.substring(16,17));
				if((IdSex % 2) == 1)
					jrdbSex[0].setSelected(true);
				else 
					jrdbSex[1].setSelected(true);
				
				if(Status.equals("0"))
					jrdbStatus[0].setSelected(true);
				else
					jrdbStatus[1].setSelected(true);
				jcmbManager.removeAllItems();
				jcmbManager.addItem(ManagerNo);
				jcmbManager.setSelectedIndex(0);
				jcmbNative.addItem(NativeId + " " + NativeName);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}

		String UId;
		String DId;
		String MId;
		
		UId = udmId.substring(0,2) + "0000";
		DId = udmId.substring(0,2) + udmId.substring(2,4) + "00";
		MId = udmId;
		
		String SQLString2 = "SELECT * FROM SYS_UDM_INFORMATION WHERE UDMID = '" + UId + "' OR UDMID = '" + DId + "' OR UDMID = '" + MId + "'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString2); 
			
			String UDMName;
			String UDMId;
			for(int i = 0 ; rs.next(); i++)
			{	
				jcmbUDM[i].removeAllItems();
				UDMName = rs.getString("UDMNAME");
				UDMId = rs.getString("UDMID");

				jcmbUDM[i].addItem(UDMId + " " + UDMName);
				jcmbUDM[i].setSelectedIndex(0);
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
			System.out.println("关闭异常！");
		}
	}
	
	/*****************************************************
	 * Function Name:initAgencyList
	 * Purpose: 根据分点和是否在职初始化代理列表信息
	 * Params: 两个String类型的参数，分别是分点的选中项和是否在职的选中项
	 * Return: 无返回值
	 *****************************************************/
	public void initAgencyList(String no,String str)	
	{
		dlmAgency.removeAllElements();
		
		no = no.substring(0,2);
		str = str.substring(0,1);
		
		if(str.equals("2"))
		{
			String SQLString = "SELECT * FROM SYS_MEC_AGENCY WHERE SUBSTR(agencyNo,1,2) = '" + no + "'";
			
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
				String strNo;
				String strName;

				while(rs.next())
				{
					strNo = rs.getString("AGENCYNO");
					strName = rs.getString("AGENCYNAME");
	
					dlmAgency.addElement(strNo + " " + strName);
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
				System.out.println("关闭异常！");
			}
		
			jlstAgency.setSelectedIndex(0);
		
			System.out.println(jlstAgency.isSelectionEmpty());
			if(jlstAgency.isSelectionEmpty())
			{
				showMessage(jitfAgency,"没有代理");
			}
			else
			{
				initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
			}
		}
		else
		{
			String SQLString = "SELECT * FROM SYS_MEC_AGENCY WHERE SUBSTR(agencyNo,1,2) = '" + no + "' AND agencyStatus = '" + str + "'";
		
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
				String strNo;
				String strName;
				while(rs.next())
				{
					strNo = rs.getString("AGENCYNO");
					strName = rs.getString("AGENCYNAME");
					dlmAgency.addElement(strNo + " " + strName);
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
				System.out.println("关闭异常！");
			}
		
			jlstAgency.setSelectedIndex(0);
		
			String strNo;

			initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
		}
		
		jlblAgencyNumber.setText("共" + dlmAgency.getSize() + "名代理");
		
	}
	
	/***********************************************************
	 * Function Name:initSector
	 * Purpose: 初始化分点下拉列表
	 * Params: 无参
	 * Return: 无返回值
	 ***********************************************************/
	public void initSector()	
	{
		String SQLString = "SELECT * FROM SYS_MEC_SECTOR ORDER BY SECTORID";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			String SectorId;
			
			while(rs.next())
			{
				SectorId = rs.getString("SECTORID");
				System.out.println(rs.getString("SECTORID"));
				jcmbSector.addItem(SectorId);
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
			System.out.println("关闭异常！");
		}
		
		initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
		
		SetStatus(BROWSE);
	}
	
	/**********************************************************
	 * Function Name:initSelected2
	 * Purpose: 初始化是否在职下拉列表
	 * Params: 无参
	 * Return: 无返回值
	 **********************************************************/
	public void initSelected2()		
	{
		jcmbStatus.addItem(All + "全部");
		jcmbStatus.addItem(At + "在职");
		jcmbStatus.addItem(Out + "离职");
	}
	
	/***********************************************************
	 * Function Name:verifyId
	 * Purpose: 验证身份证是否正确
	 * Params: 无参
	 * Return: boolean类型的返回值，身份证正确返回true否则返回false
	 ***********************************************************/
	public boolean verifyId()  
	{
		boolean result = false;
		String strId;
		String str0_2;		//省
		String str6_10;		//出生年
		String str10_12;	//出生月
		String str12_14;	//出生日
		String str16;		//男or女
		String strNative;
		int Year;
		int Month;
		int Day;
		int Sex;
		strNative = ((String) jcmbNative.getSelectedItem()).substring(0,2);
		strId = jtxtId.getText();
		
		str0_2 = strId.substring(0,0+2);	//省
		str6_10 = strId.substring(6,6+4);	//出生年
		str10_12 = strId.substring(10,12);	//出生月
		str12_14 = strId.substring(12,14);	//出生日
		str16 = strId.substring(16,17);		//男or女
		
		Year = Integer.parseInt(str6_10);
		Month = Integer.parseInt(str10_12);
		Day = Integer.parseInt(str12_14); 
		Sex = Integer.parseInt(str16);
		
		if(str0_2.equals(strNative) && (Year >= 1984) && (Year <= 2002) && 
				(Month >= 1) && (Month <= 12) && (Day >= 1) && (Day <= 31) && 
				((Sex % 2) == SelectSex()) && Id18() )
					result = true;
		
		return result;
	}
	
	/***********************************************************
	 * Function Name:Id18
	 * Purpose: 验证身份证号第十八位是否正确
	 * Params: 无参
	 * Return: 返回boolean类型，身份证号第十八位正确返回true否则返回false
	 ***********************************************************/
	public boolean Id18()	
	{
		boolean b = false;
		String strId = jtxtId.getText();	//身份证字符串
		String str17;						//身份证第十八位
		int remainder;						//余数
		int[] intId = new int[17];
		int[] ratio = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7,9, 10, 5, 8, 4, 2 };	//系数
		String[] v = { "1", "0", "x", "9", "8", "7", "6", "5", "4","3", "2"};	//校验位对照
		
		for(int i = 0; i < 17; i++)
		{
			intId[i] = Integer.parseInt(String.valueOf(strId.charAt(i)));
		}
		
		int sum = 0;
		for(int i = 0; i < 17; i++)
		{
			sum += intId[i] * ratio[i];
		}
		str17 = strId.substring(17, 18);
		
		remainder = sum % 11;
		
		if(v[remainder].equals(str17))
			b = true;
		
		return b;
	}
	
	/***********************************************************
	 * Function Name: SelectSex
	 * Purpose: 根据男女的选择项返回整型0或1
	 * Params: 无参
	 * Return: 整型返回值，男返回1，女返回0 
	 ***********************************************************/
	public int SelectSex()	//奇数为男，偶数为女
	{
		int value;
		if(jrdbSex[0].isSelected())
			value = 1;
		else
			value = 0;
		
		return value;
	}
	
	/**********************************************************
	 * Function Name: showMessage
	 * Purpose: 当操作发生错误时的提示信息显示方法
	 * Params: 两个参数，分别为窗口的对象和要显示的字符串
	 * Return: 无返回值
	 **********************************************************/
	public void showMessage(JInternalFrame jitf, String mess)	
	{
		JOptionPane.showMessageDialog(jitfAgency,mess,"纠错辅助",JOptionPane.DEFAULT_OPTION);
	}
	
	/**********************************************************
	 * Function Name: dealClose
	 * Purpose: 关闭窗口
	 * Params: 无参
	 * Return: 无返回值
	 * 
	 **********************************************************/
	public void dealClose()	
	{
		this.dispose();
	}
	
	/**********************************************************
	 * Function Name: initInternalFrame
	 * Purpose: 生成代理管理信息界面
	 * Params: 无参
	 * Return: 无返回值
	 **********************************************************/
	public void initInternalFrame()
	{
		this.setSize(1360,750);
		this.setLayout(null);
		con = this.getContentPane();
		normalFont = new Font("宋体",Font.PLAIN,16);
		
		jitfAgency = new JInternalFrame("代理信息",false,true,false,true);
		jitfAgency.setSize(8085/15,7230/15);
		jitfAgency.setLayout(null);
		conAgency = jitfAgency.getContentPane();
		con.add(jitfAgency);
		
		//标题
		jlblTopic = new JLabel("代理信息管理");
		jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,24));
		jlblTopic.setBounds(2902/15, 0, 2160/15, 465/15);
		conAgency.add(jlblTopic);
		
		//分点
		jlblSector = new JLabel("分点编号");
		jlblSector.setFont(normalFont);
		jlblSector.setBounds(120/15,840/15,960/15,240/15);
		conAgency.add(jlblSector);
		
		jcmbSector = new JComboBox();
		jcmbSector.setFont(normalFont);
		jcmbSector.setBounds(1200/15,780/15,855/15,360/15);
		conAgency.add(jcmbSector);
		
		//选择在、离职代理下拉列表
		jcmbStatus = new JComboBox();
		jcmbStatus.setFont(normalFont);
		jcmbStatus.setBounds(2160/15,780/15,975/15,360/15);
		conAgency.add(jcmbStatus);
		
		//代理列表
		dlmAgency = new DefaultListModel();
		jlstAgency = new JList(dlmAgency);
		jscpAgency = new JScrollPane(jlstAgency);
		jlstAgency.setFont(normalFont);
		jscpAgency.setBounds(120/15,1200/15,3015/15,4455/15);
		jscpAgency.setBorder(BorderFactory.createTitledBorder("代理列表"));
		conAgency.add(jscpAgency);
		
		//代理数量
		jlblAgencyNumber = new JLabel("共0名代理");
		jlblAgencyNumber.setFont(normalFont);
		jlblAgencyNumber.setBounds(120/15,5640/15,3015/15,240/15);
		conAgency.add(jlblAgencyNumber);//
		
		//代理编号
		jlblAgencyNo = new JLabel("代理编号");
		jlblAgencyNo.setFont(normalFont);
		jlblAgencyNo.setBounds(3360/15,840/15,960/15,240/15);
		conAgency.add(jlblAgencyNo);
		
		jlblNo = new JLabel("");
		jlblNo.setFont(normalFont);
		jlblNo.setBounds(4440/15,840/15,3360/15,240/15);
		conAgency.add(jlblNo);
		
		//所属院校
		jlblUniversity = new JLabel("所属院校");
		jlblUniversity.setFont(normalFont);
		jlblUniversity.setBounds(3360/15,1373/15,960/15,240/15);
		conAgency.add(jlblUniversity);
		
		jcmbUDM[0] = new JComboBox();
		jcmbUDM[0].setFont(normalFont);
		jcmbUDM[0].setBounds(4440/15,1313/15,3375/15,360/15);
		conAgency.add(jcmbUDM[0]);
		
		//所属院系
		jlblDepartment = new JLabel("所属院系");
		jlblDepartment.setFont(normalFont);
		jlblDepartment.setBounds(3360/15,1906/15,960/15,240/15);
		conAgency.add(jlblDepartment);
		
		jcmbUDM[1] = new JComboBox();
		jcmbUDM[1].setFont(normalFont);
		jcmbUDM[1].setBounds(4440/15,1846/15,3375/15,360/15);
		conAgency.add(jcmbUDM[1]);
		
		//所属专业
		jlblMajor = new JLabel("所属专业");
		jlblMajor.setFont(normalFont);
		jlblMajor.setBounds(3360/15,2439/15,960/15,240/15);
		conAgency.add(jlblMajor);
		
		jcmbUDM[2] = new JComboBox();
		jcmbUDM[2].setFont(normalFont);
		jcmbUDM[2].setBounds(4440/15,2379/15,3375/15,360/15);
		conAgency.add(jcmbUDM[2]);
		
		//姓名
		jlblName = new JLabel("姓    名");
		jlblName.setFont(normalFont);
		jlblName.setBounds(3360/15,2970/15,960/15,240/15);
		conAgency.add(jlblName);
		
		jtxtName = new JTextField();
		jtxtName.setFont(normalFont);
		jtxtName.setBounds(4440/15,2912/15,3375/15,360/15);
		conAgency.add(jtxtName);
		
		//性别
		jlblSex = new JLabel("性    别");
		jlblSex.setFont(normalFont);
		jlblSex.setBounds(3360/15,3505/15,960/15,240/15);
		conAgency.add(jlblSex);
		
		btgpSex = new ButtonGroup();
		jrdbSex[0] = new JRadioButton("男");
		jrdbSex[0].setFont(normalFont);
		jrdbSex[0].setBounds(4680/15,3438/15,975/15,375/15);
		conAgency.add(jrdbSex[0]);
		btgpSex.add(jrdbSex[0]);
		
		jrdbSex[1] = new JRadioButton("女");
		jrdbSex[1].setFont(normalFont);
		jrdbSex[1].setBounds(6360/15,3438/15,975/15,375/15);
		conAgency.add(jrdbSex[1]);
		btgpSex.add(jrdbSex[1]);
		
		//身份证号
		jlblId = new JLabel("身份证号");
		jlblId.setFont(normalFont);
		jlblId.setBounds(3360/15,4038/15,960/15,240/15);
		conAgency.add(jlblId);
		
		jtxtId = new JTextField();
		jtxtId.setFont(normalFont);
		jtxtId.setBounds(4440/15,3978/15,3375/15,360/15);
		conAgency.add(jtxtId);
		
		//籍贯
		//TODO这里有多余代码
		jlblNative = new JLabel("籍    贯");
		jlblNative.setFont(normalFont);
		jlblNative.setBounds(3360/15,4571/15,960/15,240/15);
		conAgency.add(jlblNative);
		
		jcmbNative = new JComboBox();
		jcmbNative.setFont(normalFont);
		jcmbNative.setBounds(4440/15,4511/15,3375/15,360/15);
		conAgency.add(jcmbNative);
		
		//状态
		jlblStatus = new JLabel("状    态");
		jlblStatus.setFont(normalFont);
		jlblStatus.setBounds(3360/15,5104/15,960/15,240/15);
		conAgency.add(jlblStatus);
		
		btgpStatus = new ButtonGroup();
		jrdbStatus[0] = new JRadioButton("在职");
		jrdbStatus[0].setFont(normalFont);
		jrdbStatus[0].setBounds(4680/15,5037/15,975/15,375/15);
		conAgency.add(jrdbStatus[0]);
		btgpStatus.add(jrdbStatus[0]);

		jrdbStatus[1] = new JRadioButton("离职");
		jrdbStatus[1].setFont(normalFont);
		jrdbStatus[1].setBounds(6360/15,5037/15,975/15,375/15);
		conAgency.add(jrdbStatus[1]);
		btgpStatus.add(jrdbStatus[1]);
		
		//组长编号
		jlblManager = new JLabel("组长编号");
		jlblManager.setFont(normalFont);
		jlblManager.setBounds(3360/15,5640/15,960/15,240/15);
		conAgency.add(jlblManager);
		
		jcmbManager = new JComboBox();
		jcmbManager.setFont(normalFont);
		jcmbManager.setBounds(4440/15,5580/15,3375/15,360/15);
		conAgency.add(jcmbManager);
		
		//添加按钮
		jbtnAdd = new JButton("添加");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(120/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnAdd);
		
		//修改按钮
		jbtnModify = new JButton("修改");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1320/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnModify);
		
		//退出按钮
		jbtnExit = new JButton("退出");
		jbtnExit.setFont(normalFont);
		jbtnExit.setBounds(6600/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnExit);
		
		jitfAgency.setVisible(true);
		this.setVisible(true);
	}
}
