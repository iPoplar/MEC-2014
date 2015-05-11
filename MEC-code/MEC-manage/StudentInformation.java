
/*******************************************************

* Function List:
*		initFrame					界面初始化
*		reinitFrame					再次初始化，进行界面的再次初始化
*		dealAction					事件处理
*		stateAlter					状态设置
*		addIdNumberRepeateInpect	添加时检测身份证号码是否重复
*		modifyIdNumberRepeateInpect	修改时检测身份证号码是否重复
*		idNumberCodIpect			身份证号码的合法性判断
*		inspectError				验证错误判断
*		updataData					学员基本信息的修改
*		addData						学员基本信息的添加
*		dealAction					各项事件的监听
*		initMajor					初始化指定院系下的专业
*		initDepartment				初始化指定学校下的院系，会调用对专业的初始化
*		initSchool					初始化学校列表，会调用对院系的初始化
*		initStudentInfoSelectFrame	初始化籍贯、民族、年级、学校、院系、专业下拉列表
*		initstudentBaseInfoList		初始化学员信息列表
*		initStudnetInfo				初始化指定身份证号码的学员信息
*******************************************************/

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StudentInformation 
{
	private JFrame jfrmMain;				/*窗口声明*/
	private Container container;			/*容器声明*/
	
	private JLabel jlblTopic;				/*标题标签声明*/
	
	private DefaultListModel dlmStuBaseInfo;/*默认列表模板声明*/
	private JList jlstStuBaseInfo;			/*列表框声明*/
	private JScrollPane jscpStuBaseInfo;	/*滚动窗口声明*/
	
	private JLabel jlblIdNumber;			/*姓名标签声明*/
	private JTextField jtxtIdNumber;		/*姓名文本框声明*/
	
	private JLabel jlblName;				/*身份证号标签声明*/
	private JTextField jtxtName;			/*身份证号文本框声明*/
	
	private JLabel jlblNative;				/*籍贯标签声明*/
	private JComboBox jcmbNative;			/*籍贯下拉列表框声明*/
	
	private JLabel jlblNation;				/*民族标签声明*/
	private JComboBox jcmbNation;			/*民族下拉列表框声明*/
	
	private JLabel jlblGrade;				/*年级标签声明*/
	private JComboBox jcmbGrade;			/*年级下拉列表框声明*/
	
	private JLabel jlblSchool;				/*学校标签声明*/
	private JComboBox jcmbSchool;			/*学校下拉列表框声明*/
	
	private JLabel jlblDepartment;			/*院系标签声明*/
	private JComboBox jcmbDepartment;		/*院系下拉列表框声明*/
	
	private JLabel jlblMajor;				/*专业标签声明*/
	private JComboBox jcmbMajor;			/*专业下拉列表框声明*/
	
	private JLabel jlblStuSum;				/*学员信息数量标签*/
	
	private JLabel jlblPhone;				/*手机号码标签声明*/
	private JTextField jtxtPhone;			/*手机号码文本框声明*/
	
	private JButton jbtnAdd;				/*添加按钮声明*/ 
	private JButton jbtnModify;				/*修改按钮声明*/
	private JButton jbtnExit;				/*退出按钮声明*/
	
	private static final boolean BROWSE = false;		/*浏览状态*/
	private static final boolean EDIT = true;			/*编辑状态*/

	private static boolean ADDATION = true;				/*记录上次的按钮点击类型“添加”*/
	private static boolean MODIFY = false;				/*记录上次的按钮点击类型“修改”*/
	private static boolean operating = false;			/*用来保持记录的数据“操作类型”*/

	private String restoreIdName = null;				/*用来保持恢复数据时，所需身份证号码*/
	
	private boolean listInitEnd = false;				/*用来控制学员信息列表初始化完毕*/
	private boolean jcmbSchoolInitEnd = false;			/*用来控制学校列表初始化完毕*/
	private boolean jcmbDepartmentInitEnd = false;		/*用来控制院系列表初始化完毕*/

 	/*******************************************************
 	* Function Name: 	StudentInformation
 	* Purpose: 			构造方法
 	* Params : 			无参
 	* Return: 			无返回值   	
 	*******************************************************/
 	public StudentInformation()
	{
		initFrame();
		reinitFrame();
		dealAction();
	}
 	
 	/*******************************************************
 	* Function Name: 	stateAlter
 	* Purpose: 			进行浏览与编辑之间的状态变迁
 	* Params : 
 	*	@boolean     	state	设置状态
 	* Return: 			void   	
 	*******************************************************/
	private void stateAlter(boolean state)
	{
		jtxtIdNumber.setEnabled(state);
		jtxtName.setEnabled(state);
		jcmbNative.setEnabled(state);
		jcmbNation.setEnabled(state);
		jcmbGrade.setEnabled(state);
		jcmbSchool.setEnabled(state);
		jcmbDepartment.setEnabled(state);
		jcmbMajor.setEnabled(state);
		jtxtPhone.setEnabled(state);

		jlstStuBaseInfo.setEnabled(!state);
	}

 	/*******************************************************
 	* Function Name: 	modifyIdNumberRepeateInpect
 	* Purpose: 			修改时检测身份证号码是否重复
 	* Params : 			无参
 	* Return: 			boolean	true正常 	false重复  	
 	*******************************************************/
	private boolean modifyIdNumberRepeateInpect()
	{
		for(int i = 0; i < dlmStuBaseInfo.getSize(); i++)
			if(dlmStuBaseInfo.getElementAt(i).toString().substring(0, 0+18).equals(jtxtIdNumber.getText())
					&& !jlstStuBaseInfo.getSelectedValue().toString().substring(0, 18).equals(jtxtIdNumber.getText()))
			{
				JOptionPane.showMessageDialog(jfrmMain, "身份证号输入重复，请重新输入", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
		
				return false;
			}

		return true; 
	}
	
 	/*******************************************************
 	* Function Name: 	addIdNumberRepeateInpect
 	* Purpose: 			添加时检测身份证号码是否重复
 	* Params : 			无参
 	* Return: 			boolean	true正常 	false重复  	
 	*******************************************************/
	private boolean addIidNumberRepeateInpect()
	{
		for(int i = 0; i < dlmStuBaseInfo.getSize(); i++)
			if(dlmStuBaseInfo.getElementAt(i).toString().substring(0, 0+18).equals(jtxtIdNumber.getText()))
			{
				JOptionPane.showMessageDialog(jfrmMain, "身份证号输入重复，请重新输入", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
		
				return false;
			}

		return true; 
	}
	
 	/*******************************************************
 	* Function Name: 	idNumberCodIpect
 	* Purpose: 			判断该身份证号是否合法
 	* Params : 
 	*	@String     	id	身份证号
 	* Return: 			boolean	返回值 	true为正确 	false	 不合法
 	*******************************************************/
	private boolean idNumberCodIpect(String id)
	{
		FResultSet YesNo = null;
		int a[] = new int[18];
		int inspect[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		char inspectCode[] = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};
		int sum = 0;
		String SQLID2 = "SELECT NATIVE FROM SYS_MEC_NATIVE WHERE NATIVENO =  '"+id.substring(0, 2)+"'";
		
		// 校验位验证
		for(int i = 0; i < 17; i++)
		{
			a[i] = Integer.parseInt(id.substring(i, i+1));
			sum += a[i]*inspect[i];
		}
		
		sum = sum%11;
		if(id.substring(17, 17+1).equals(String.valueOf(inspectCode[sum])))
			;
		else
			return false;
		
		// 日期验证
		Calendar cld  = Calendar.getInstance(); 
		cld.set(Integer.parseInt(id.substring(6, 6+4)), Integer.parseInt(id.substring(10, 10+2)), Integer.parseInt(id.substring(12, 12+2)));
		if(cld.get(Calendar.YEAR) == Integer.parseInt(id.substring(6, 6+4)) 
				&&cld.get(Calendar.MONTH) == Integer.parseInt(id.substring(10, 10+2))
				&&cld.get(Calendar.DATE) == Integer.parseInt(id.substring(12, 12+2)))
			;
		else
			return false;
		
		// 籍贯验证
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			YesNo = FStatement.executeQueryCon(SQLID2);
			FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		try 
		{
			if(YesNo.getRow() > 2)
				return true;
			else
				return false;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

 	/*******************************************************
 	* Function Name: 	inspectError
 	* Purpose: 			身份证号码，姓名，手机号码的验证
 	* Params : 			无参
 	* Return: 			boolean	返回值 	true为正确 	false	 不合法
 	*******************************************************/
	private boolean inspectError()
	{
		String id = jtxtIdNumber.getText();
		String name = jtxtName.getText();
		String phone = jtxtPhone.getText();
		
		String inspectName = "[\u4e00-\u9fa5]{2,10}";
		String inspectPhone = "[1-9][0-9]{10}";
		
		if(idNumberCodIpect(id))
			if(name.matches(inspectName))
				if(phone.matches(inspectPhone))
					return true;
				else
				{
					JOptionPane.showMessageDialog(jfrmMain, "手机号码输入非法，请重新输入", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
					jtxtPhone.requestFocus();
					jtxtPhone.selectAll();
					return false;
				}
			else
			{	
				JOptionPane.showMessageDialog(jfrmMain, "姓名输入非法，请重新输入", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
				jtxtName.requestFocus();
				jtxtName.selectAll();
				return false;
			}
		else
		{	
			JOptionPane.showMessageDialog(jfrmMain, "身份证号输入非法，请重新输入", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
			jtxtIdNumber.requestFocus();
			jtxtIdNumber.selectAll();
			return false;
		}
	}

 	/*******************************************************
 	* Function Name: 	updataData
 	* Purpose: 			数据的更新即修改操作
 	* Params : 			无参
 	* Return: 			boolean 返回值 	true为成功 	false	 失败
 	*******************************************************/
	private boolean updataData()
	{
		String lId = null;		// 身份证号
		String lNname = null;	// 姓名
		String lNative = null;	// 籍贯
		String lNation = null;	// 民族
		String lGrade = null;	// 年级
		String lMajor = null;	// 专业
		String lTel = null;		// 电话
		String lOldId = null;
		lId = jtxtIdNumber.getText();
		lNname = jtxtName.getText();
		lNative = jcmbNative.getSelectedItem().toString().substring(0, 2);
		lNation = jcmbNation.getSelectedItem().toString().substring(0, 2);
		lGrade = jcmbGrade.getSelectedItem().toString().substring(0, 2);
		lMajor = jcmbMajor.getSelectedItem().toString().substring(0, 6);
		lTel = jtxtPhone.getText();
		lOldId = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
		String SQLUpdate = "UPDATE SYS_MEC_LEARNER SET LEARNERID = '"+ lId +"', LEARNERNAME = '"+ lNname +"', LEARNERNATIVE = '"+ lNative +"', LEARNERNATION = '"+ lNation +"', LEARNERGRADE = '"+ lGrade +"', LEARNERMAJOR = '"+ lMajor +"', LEARNERTEL = '"+ lTel +"' WHERE LEARNERID = '"+ lOldId +"'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLUpdate);
			FStatement.disConnection();
			return true;
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}

 	/*******************************************************
 	* Function Name: 	addData
 	* Purpose: 			数据库的插入即添加操作
 	* Params : 			无参
 	* Return: 			boolean 返回值 	true为成功 	false	 失败
 	*******************************************************/
	private boolean addData()
	{
		String lId = null;		// 身份证号
		String lNname = null;	// 姓名
		String lNative = null;	// 籍贯
		String lNation = null;	// 民族
		String lGrade = null;	// 年级
		String lMajor = null;	// 专业
		String lTel = null;	// 电话
		lId = jtxtIdNumber.getText();
		lNname = jtxtName.getText();
		lNative = jcmbNative.getSelectedItem().toString().substring(0, 2);
		lNation = jcmbNation.getSelectedItem().toString().substring(0, 2);
		lGrade = jcmbGrade.getSelectedItem().toString().substring(0, 2);
		lMajor = jcmbMajor.getSelectedItem().toString().substring(0, 6);
		lTel = jtxtPhone.getText();
		String SQLAdd = "INSERT INTO SYS_MEC_LEARNER VALUES ('"+lId+"', '"+lNname+"', '"+lNative+"', '"+lNation+"', '"+lGrade+"', '"+lMajor+"', '"+lTel+"')";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLAdd);
			FStatement.disConnection();
			return true;
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	/*******************************************************
	* Function Name: 	dealAction
	* Purpose: 			进行各项监听事件
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void dealAction()
	{
		
		/*身份证号码输入框的监听事件*/
		jtxtIdNumber.addCaretListener
		(
			new CaretListener()
			{
				public void caretUpdate(CaretEvent arg0) 
				{
					if(jtxtIdNumber.getText().toString().length() == 0)
						jbtnAdd.setEnabled(false);

					if(jtxtName.getText().toString().length() != 0 &&
							jtxtIdNumber.getText().toString().length() != 0&&
							jtxtPhone.getText().toString().length() != 0)
						jbtnAdd.setEnabled(true);
				}
			}
		);
		
		/*姓名输入框的监听事件*/
		jtxtName.addCaretListener
		(
			new CaretListener()
			{
				public void caretUpdate(CaretEvent arg0) 
				{
					if(jtxtName.getText().toString().length() == 0)
						jbtnAdd.setEnabled(false);

					if(jtxtName.getText().toString().length() != 0 &&
							jtxtIdNumber.getText().toString().length() != 0&&
							jtxtPhone.getText().toString().length() != 0)
						jbtnAdd.setEnabled(true);
				}
			}
		);
		
		/*手机号码输入框的监听事件*/
		jtxtPhone.addCaretListener
		(
			new CaretListener()
			{
				public void caretUpdate(CaretEvent e) 
				{
					if(jtxtPhone.getText().toString().length() == 0)
						jbtnAdd.setEnabled(false);
					
					if(jtxtName.getText().toString().length() != 0 &&
							jtxtIdNumber.getText().toString().length() != 0&&
							jtxtPhone.getText().toString().length() != 0)
						jbtnAdd.setEnabled(true);
				}
			}
		);
		
		/*jbmtModify按钮的监听事件*/
		jbtnModify.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if((jbtnModify.getText().equals("修改")))
					{
						stateAlter(EDIT);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						jtxtIdNumber.requestFocus();
						jtxtIdNumber.selectAll();
						operating = MODIFY;
						restoreIdName = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
					}
					else
					{
						initStudnetInfo(restoreIdName);	// 学员信息恢复
						stateAlter(BROWSE);
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
					}
				}
			}
		);
		
		/*addModify按钮的监听事件*/
		jbtnAdd.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if((jbtnAdd.getText()).equals("添加"))
					{
						stateAlter(EDIT);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						jtxtIdNumber.requestFocus();
						jtxtIdNumber.selectAll();
						operating = ADDATION;
						restoreIdName = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
					}
					else
					{
						if(inspectError())
						{
							if(operating == ADDATION && addIidNumberRepeateInpect())
							{
								if(addData())
								{
									initstudentBaseInfoList();
									for(int i = 0; i < dlmStuBaseInfo.getSize(); i++)
									{
										if(jlstStuBaseInfo.indexToLocation(i).toString().substring(0, 18).equals(restoreIdName))
											jlstStuBaseInfo.setSelectedIndex(i);	
									}
									initStudnetInfo(restoreIdName);
									stateAlter(BROWSE);
									jbtnAdd.setText("添加");
									jbtnModify.setText("修改");
								}
								else 
									JOptionPane.showMessageDialog(jfrmMain, "数据库异常！", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(operating == MODIFY && modifyIdNumberRepeateInpect())
								if(updataData())
								{
									initstudentBaseInfoList();
									jlstStuBaseInfo.setSelectedIndex(0);	
									stateAlter(BROWSE);
									jbtnAdd.setText("添加");
									jbtnModify.setText("修改");
								}
								else
									JOptionPane.showMessageDialog(jfrmMain, "数据库异常！", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
						}
					}
				}
			}
		);
		
		/*窗口的关闭监听事件*/
		jfrmMain.addWindowListener
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
					JOptionPane.showMessageDialog(jfrmMain, "再见，欢迎下次光临", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION);
				}
			}			
		);
		
		/*关闭按钮的监听事件*/
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(JOptionPane.showConfirmDialog(jfrmMain, "确定要退出吗？", "微易码温馨提示", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
						jfrmMain.dispose();
				}
			}
		);
		
		/*学员基本信息列表框的监听事件*/
		jlstStuBaseInfo.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0) 
				{
					if(jlstStuBaseInfo.getSelectedIndex() != -1&&(listInitEnd == false))
					initStudnetInfo(jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18));
				}
			}
		);
	}
	
	/*******************************************************
	* Function Name: 	initMajor
	* Purpose: 			初始化指定院系下的专业
	* Params : 			
	*	@String id		指定院系编号
	* Return: 			无返回值
	*******************************************************/
	private void initMajor(String id)
	{
		try 
		{
			String SQLMajor = "SELECT UDMID, UDMNAME FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID, 1, 4) = '" + id + "' AND SUBSTR(UDMID, 5, 2) !='00' AND UDMSTATUS = '0' ORDER BY UDMID";
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet frsMajor = FStatement.executeQueryCon(SQLMajor);
			jcmbMajor.removeAllItems();
			while(frsMajor.next())
			{
				String majorid = frsMajor.getString("UDMID");
				String majorName = frsMajor.getString("UDMNAME");
				jcmbMajor.addItem(majorid + " " + majorName);
			}
			FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*******************************************************
	* Function Name: 	initDepartment
	* Purpose: 			初始化指定学校下的院系，会调用对专业的初始化
	* Params : 			
	*	@String id		指定学校编号
	* Return: 			无返回值
	*******************************************************/
	private void initDepartment(String id)
	{
		try 
		{
			// 初始化院系
			String SQLDepartment = "SELECT UDMID, UDMNAME FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID, 5) = '00' AND SUBSTR(UDMID,1,2) = '" + id + "' AND SUBSTR(UDMID, 3, 2) !='00' AND UDMSTATUS = '0' ORDER BY UDMID";
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet frsDepartment = FStatement.executeQueryCon(SQLDepartment);
			jcmbDepartment.removeAllItems();
			jcmbDepartmentInitEnd = true;
			while(frsDepartment.next())
			{
				String departmentid = frsDepartment.getString("UDMID");
				String departmentName = frsDepartment.getString("UDMNAME");
				jcmbDepartment.addItem(departmentid + " " + departmentName);
			}
			jcmbDepartmentInitEnd = false;
			// 初始化专业
			
			if(jcmbDepartment.getSelectedItem() != null)
			{
				id = jcmbDepartment.getSelectedItem().toString().substring(0, 4);
				initMajor(id);
			}
			else
				jcmbMajor.removeAllItems();
				
		FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*******************************************************
	* Function Name: 	initSchool
	* Purpose: 			初始化学校列表，会调用对院系的初始化
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void initSchool()
	{
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			String SQLschool = "SELECT UDMID,UDMNAME FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID, 3) = '0000' AND UDMSTATUS = '0' ORDER BY UDMID";
			FResultSet frsSchool = FStatement.executeQueryCon(SQLschool);
			jcmbSchoolInitEnd = true;
			while(frsSchool.next())
			{
				String schoolid = frsSchool.getString("UDMID");
				String schoolName = frsSchool.getString("UDMNAME");
				jcmbSchool.addItem(schoolid + " " + schoolName);
			}
			jcmbSchoolInitEnd = false;
			FStatement.disConnection();
			String id = jcmbSchool.getSelectedItem().toString().substring(0, 0+2);
			initDepartment(id);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*******************************************************
	* Function Name: 	initStudentInfoSelectFrame
	* Purpose: 			初始化籍贯、民族、年级、学校、院系、专业下拉列表
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void initStudentInfoSelectFrame()
	{
		try
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			
			/*初始化籍贯*/
			String SQLnative = "SELECT NATIVENO,NATIVE FROM SYS_MEC_NATIVE ORDER BY NATIVENO";
			FResultSet frsNative = FStatement.executeQueryCon(SQLnative);
			jcmbNative.removeAllItems();
			while(frsNative.next())
			{
				String nativeid = frsNative.getString("NATIVENO");
				String nativeName = frsNative.getString("NATIVE");
				jcmbNative.addItem(nativeid + " " + nativeName);
			}
			
			jcmbNative.setSelectedIndex(26); 
			
			/*初始化民族*/
			String SQLnation = "SELECT NATIONNO,NATION FROM SYS_MEC_NATION ORDER BY NATIONNO";
			FResultSet frsNation = FStatement.executeQueryCon(SQLnation);
			jcmbNation.removeAllItems();
			while(frsNation.next())
			{
				String nationid = frsNation.getString("NATIONNO");
				String nationName = frsNation.getString("NATION");
				jcmbNation.addItem(nationid + " " + nationName);
			}
			
			/*初始化年级*/
			String SQLgrade = "SELECT GRADENO,GRADENAME FROM SYS_MEC_GRADE ORDER BY GRADENO";
			FResultSet frsgrade = FStatement.executeQueryCon(SQLgrade);
			jcmbGrade.removeAllItems();
			while(frsgrade.next())
			{
				String gradeid = frsgrade.getString("GRADENO");
				String gradeName = frsgrade.getString("GRADENAME");
				jcmbGrade.addItem(gradeid + " " + gradeName);
			}
			FStatement.disConnection();
			
			/*初始化学校*/
			initSchool();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*******************************************************
	* Function Name: 	initstudentBaseInfoList
	* Purpose: 			初始化学员信息列表
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void initstudentBaseInfoList() 
	{
		int i = 0;
		try 
		{
			dlmStuBaseInfo.removeAllElements();
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			String SQLStuBaseInfo = "SELECT LEARNERID, LEARNERNAME FROM SYS_MEC_LEARNER ORDER BY LEARNERID";
			FResultSet frsStuBaseInfo = FStatement.executeQueryCon(SQLStuBaseInfo);
			listInitEnd = true;
			while(frsStuBaseInfo.next())
			{
				i++;
				String StuBaseInfoid = frsStuBaseInfo.getString("LEARNERID");
				String StuBaseInfoName = frsStuBaseInfo.getString("LEARNERNAME");
				dlmStuBaseInfo.addElement(StuBaseInfoid + " " + StuBaseInfoName);
				jlstStuBaseInfo.setSelectedIndex(0);
			}
			listInitEnd = false;
			FStatement.disConnection();
			jlblStuSum.setText("共"+i+"学员信息");
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*******************************************************
	* Function Name: 	initStudnetInfo
	* Purpose: 			初始化指定身份证号码的学员信息
	* Params : 			
	* 	@String 	idNo 身份证号码	
	* Return: 			无返回值
	*******************************************************/
	private void initStudnetInfo(String idNo) 
	{
		String SQLinfo = "SELECT LEARNERID,LEARNERNAME,LEARNERNATIVE,LEARNERNATION,LEARNERGRADE,LEARNERMAJOR,LEARNERTEL FROM SYS_MEC_LEARNER WHERE LEARNERID = '" + idNo + "' ORDER BY LEARNERID";
		String lId = null;		// 身份证号
		String lNname = null;	// 姓名
		String lNative = null;	// 籍贯
		String lNation = null;	// 民族
		String lGrade = null;	// 年级
		String lMajor = null;	// 专业
		String lTel = null;	// 电话
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet frsStuInfo = FStatement.executeQueryCon(SQLinfo);
			while(frsStuInfo.next())
			{
				lId = frsStuInfo.getString("learnerId");
				lNname = frsStuInfo.getString("learnerName");
				lNative = frsStuInfo.getString("learnerNative");
				lNation = frsStuInfo.getString("learnerNation");
				lGrade = frsStuInfo.getString("learnerGrade");
				lMajor = frsStuInfo.getString("learnerMajor");
				lTel = frsStuInfo.getString("learnerTel");
			}

			jtxtIdNumber.setText(lId);
			jtxtName.setText(lNname);

			int i = 0;	// 找相应籍贯的对应的下标
			while(i < jcmbNative.getItemCount())
			{	
				if(jcmbNative.getItemAt(i).toString().substring(0, 2).equals(lNative))
					jcmbNative.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// 找相应民族的对应的下标
			while(i < jcmbNation.getItemCount())
			{	
				if(jcmbNation.getItemAt(i).toString().substring(0, 2).equals(lNation))
					jcmbNation.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// 找相应年级的对应的下标
			while(i < jcmbGrade.getItemCount())
			{	
				if(jcmbGrade.getItemAt(i).toString().substring(0, 2).equals(lGrade))
					jcmbGrade.setSelectedIndex(i);
				i++;
			}

			i = 0;		// 找相应学院的对应的下标
			while(i < jcmbSchool.getItemCount())
			{
				if(jcmbSchool.getItemAt(i).toString().substring(0, 2).equals(lMajor.substring(0, 2)))
					jcmbSchool.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// 找相应院系的对应的下标
			while(i < jcmbDepartment.getItemCount())
			{
				if(jcmbDepartment.getItemAt(i).toString().substring(0, 4).equals(lMajor.substring(0, 4)))
					jcmbDepartment.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// 找相应专业的对应的下标
			while(i < jcmbDepartment.getItemCount())
			{
				if(jcmbDepartment.getItemAt(i).toString().substring(0, 6).equals(lMajor.substring(0, 6)))
					jcmbDepartment.setSelectedIndex(i);
				i++;
			}
			
			jtxtPhone.setText(lTel);
			FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*******************************************************
	* Function Name: 	reinitFrame
	* Purpose: 			初始化学员的各项信息
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void reinitFrame() 
	{
		initstudentBaseInfoList();
		initStudentInfoSelectFrame();
		
		String idNo = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
		
		/*学校列表框的监听事件*/
		jcmbSchool.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					initDepartment(jcmbSchool.getSelectedItem().toString().substring(0, 2));
				}
			}
		);

		/*院系列表框的监听事件*/
		jcmbDepartment.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					if(jcmbDepartment.getSelectedIndex() != -1 && jcmbSchoolInitEnd == false && jcmbDepartmentInitEnd == false)
						initMajor(jcmbDepartment.getSelectedItem().toString().substring(0, 4));
				}
			}
		);

		
		initStudnetInfo(idNo);
		
		stateAlter(BROWSE);
	}

	/*******************************************************
	* Function Name: 	initFrame
	* Purpose: 			初始化窗口界面
	* Params : 			无参
	* Return: 			无返回值
	*******************************************************/
	private void initFrame() 
	{
		Dimension dim;
		Font normalFont = new Font("宋体", Font.PLAIN, 16);
				
		jfrmMain = new JFrame("学员基本信息管理");
		container = jfrmMain.getContentPane();
		container.setLayout(null);
		jfrmMain.setSize(7725/15, 7425/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width-jfrmMain.getWidth())/2, (dim.height-jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel("学员基本信息管理");
		jlblTopic.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(2392/15, 0, 2880/15, 465/15);
		container.add(jlblTopic);
		
		dlmStuBaseInfo = new DefaultListModel();
		jlstStuBaseInfo = new JList(dlmStuBaseInfo);
		jscpStuBaseInfo = new JScrollPane(jlstStuBaseInfo);
		jscpStuBaseInfo.setBorder(BorderFactory.createTitledBorder("学员基本信息"));
		jlstStuBaseInfo.setFont(normalFont);
		jscpStuBaseInfo.setBounds(135/15, 675/15, 3600/15, 4935/15);
		container.add(jscpStuBaseInfo);
		
		jlblIdNumber = new JLabel("身份证号");
		jlblIdNumber.setFont(normalFont);
		jlblIdNumber.setBounds(4080/15, 675/15, 960/15, 240/15);
		container.add(jlblIdNumber);
		jtxtIdNumber = new JTextField();
		jtxtIdNumber.setFont(normalFont);
		jtxtIdNumber.setBounds(5160/15, 675/15, 2290/15, 360/15);
		container.add(jtxtIdNumber);

		jlblName = new JLabel("姓　　名");
		jlblName.setFont(normalFont);
		jlblName.setBounds(4080/15, 1297/15, 960/15, 240/15);
		container.add(jlblName);
		jtxtName = new JTextField();
		jtxtName.setFont(normalFont);
		jtxtName.setBounds(5160/15, 1297/15, 2290/15, 360/15);
		container.add(jtxtName);
		
		jlblNative = new JLabel("籍　　贯");
		jlblNative.setFont(normalFont);
		jlblNative.setBounds(4080/15, 1919/15, 960/15, 240/15);
		container.add(jlblNative);
		jcmbNative = new JComboBox();
		jcmbNative.setFont(normalFont);
		jcmbNative.setBounds(5160/15, 1905/15, 2290/15, 360/15);
		container.add(jcmbNative);

		jlblNation = new JLabel("民　　族");
		jlblNation.setFont(normalFont);
		jlblNation.setBounds(4080/15, 2541/15, 960/15, 240/15);
		container.add(jlblNation);
		jcmbNation = new JComboBox();
		jcmbNation.setFont(normalFont);
		jcmbNation.setBounds(5160/15, 2520/15, 2290/15, 360/15);
		container.add(jcmbNation);

		jlblGrade = new JLabel("年　　级");
		jlblGrade.setFont(normalFont);
		jlblGrade.setBounds(4080/15, 3163/15, 960/15, 240/15);
		container.add(jlblGrade);
		jcmbGrade = new JComboBox();
		jcmbGrade.setFont(normalFont);
		jcmbGrade.setBounds(5160/15, 3120/15, 2290/15, 360/15);
		container.add(jcmbGrade);

		jlblSchool = new JLabel("学　　校");
		jlblSchool.setFont(normalFont);
		jlblSchool.setBounds(4080/15, 3785/15, 960/15, 240/15);
		container.add(jlblSchool);
		jcmbSchool = new JComboBox();
		jcmbSchool.setFont(normalFont);
		jcmbSchool.setBounds(5160/15, 3750/15, 2290/15, 360/15);
		container.add(jcmbSchool);

		jlblDepartment = new JLabel("院　　系");
		jlblDepartment.setFont(normalFont);
		jlblDepartment.setBounds(4080/15, 4407/15, 960/15, 240/15);
		container.add(jlblDepartment);
		jcmbDepartment = new JComboBox();
		jcmbDepartment.setFont(normalFont);
		jcmbDepartment.setBounds(5160/15, 4365/15, 2290/15, 360/15);
		container.add(jcmbDepartment);
		
		jlblMajor = new JLabel("专　　业");
		jlblMajor.setFont(normalFont);
		jlblMajor.setBounds(4080/15, 5029/15, 960/15, 240/15);
		container.add(jlblMajor);
		jcmbMajor = new JComboBox();
		jcmbMajor.setFont(normalFont);
		jcmbMajor.setBounds(5160/15, 4980/15, 2290/15, 360/15);
		container.add(jcmbMajor);

		jlblStuSum = new JLabel("共*名学员信息");
		jlblStuSum.setFont(normalFont);
		jlblStuSum.setBounds(135/15, 5690/15, 3600/15, 255/15);
		container.add(jlblStuSum);
		
		jlblPhone = new JLabel("手机号码");
		jlblPhone.setFont(normalFont);
		jlblPhone.setBounds(4080/15, 5655/15, 960/15, 240/15);
		container.add(jlblPhone);
		jtxtPhone = new JTextField();
		jtxtPhone.setFont(normalFont);
		jtxtPhone.setBounds(5160/15, 5565/15, 2290/15, 360/15);
		container.add(jtxtPhone);
		
		jbtnAdd = new JButton("添加");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(240/15, 6240/15, 1215/15, 465/15);
		container.add(jbtnAdd);
		
		jbtnModify = new JButton("修改");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1440/15, 6240/15, 1215/15, 465/15);
		container.add(jbtnModify);

		jbtnExit = new JButton("退出");
		jbtnExit.setFont(normalFont);
		jbtnExit.setBounds(6240/15, 6240/15, 1215/15, 465/15);
		container.add(jbtnExit);

		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrmMain.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new StudentInformation();
	}
}