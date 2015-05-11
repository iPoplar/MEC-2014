
/*******************************************************

* Function List:                程序中涉及的所有方法
*	setStatus		职务状态的设置
*	dealClose	        关闭整个窗口
*	checkListEmpty		判断职务列表是否为空
*	getDutiesInformation    通过职务编号查找职务所对应的名称及月薪
*       findStatus              查询数据库中的所有可用的职务，即状态为0的职务
*       updateDutiesInformation 修改列表中的信息，此时只能修改职务的状态 
*       addDutiesInformation    添加职务信息
*       initDutiesList          初始化职务列表
*       reinitFrame             再次初始化
*       dealAction              处理事件发生的动作
*       initFrame               初始化
*/

import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.net.aso.l;


public class DutiesManagement 
{
	private JFrame jfrmMain;                  /*定义主窗口*/   
	private Container container;              /*定义可以盛装主窗口中各个控件的容器*/
	
	private JLabel jlblTopic;                 /*标题*/
	
	private Choice choiceDutiesList;          /*职务列表的下拉列表框*/
	
	private DefaultListModel dlmDutiesList;   /*职务列表框中真正存储数据的地方，以后的增加、修改都对其进行*/
	private JList jlstDutiesList;             /*职务列表*/
	private JScrollPane jscpDutiesList;       /*职务列表框的滚动条*/
	
	private JLabel jlblAllDuties;             /*所有职务的个数*/
	
	private JLabel jlblDutiesInformation;     /*职务信息*/
	
	private JLabel jlblDutiesNumber;          /*职务编号*/
	private JLabel jlblDutiesNumberContent;  
	
	private JLabel jlblDutiesName;            /*职务名称*/
	private JTextField jtxtDutiesName;        /*职务名称所对应的文本框*/
	
	private JLabel jlblDutiesSalary;          /*职务月薪*/
	private JTextField jtxtDutiesSalary;      /*职务月薪所对应的文本框*/
	private JLabel jblDutiesSalaryUnit;
	
	private JLabel jlblDutiesStatus;
	
	private JRadioButton[] jrdbStatus;        /*职务状态*/
	private ButtonGroup bgStatus;
	
	private JButton jbtnAdd;                  /*增加按钮*/
	private JButton jbtnModify;               /*修改按钮*/
	private JButton jbtnExit;                 /*退出按钮*/
	
	private int lastAction;
	private String lastName;
	
	private static final int ADDATION = 1;
	private static final int MODIFY = 2;
	
	private static final String TOPIC = "职务管理";
	
	private static final int BROWS = 0;
	private static final int EDIT = 1;
	private boolean checkListValues = true;
	
	public DutiesManagement()
	{
		initFrame();   
		reinitFrame();
		dealAction();
	}
	
	/*******************************************************
	* Function Name: 	setStatus
	* Purpose: 			职务状态的设置
	* Params : 
	*	@int 	Status 	用户传递的状态
	*Return:            空
	*******************************************************/

	private void setStatus(int Status)
	{
		boolean status = Status == BROWS;
		
			jlstDutiesList.setEnabled(status);
			
			jlblDutiesNumberContent.setEnabled(!status);
			jtxtDutiesName.setEnabled(!status);
			jtxtDutiesSalary.setEnabled(!status);
			jrdbStatus[0].setEnabled(!status);
			jrdbStatus[1].setEnabled(!status);
	
	}
	
	/*******************************************************
	* Function Name: 	dealClose
	* Purpose: 			关闭整个窗口
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void dealClose()
	{
		 jfrmMain.dispose();
	}
	
	
	/*******************************************************
	* Function Name: 	checkListEmpty
	* Purpose: 			判断职务列表框内容是否为空
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void checkListEmpty()              
	{
		jbtnModify.setEnabled(dlmDutiesList.getSize() > 0);
	}	
	
	/*******************************************************
	* Function Name: 	getDutiesInformation
	* Purpose: 			通过职务编号查找职务所对应的名称及月薪
	* Params : 
	*	@String		S	职务编号
	* Return: 			空
	*******************************************************/

	private void getDutiesInformation(String S)

	{
        String SQLString = "SELECT jobName,jobSalary,jobStatus FROM  SYS_MEC_JOB WHERE jobNo = '" + S +"'";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			while(rs.next())
			{
				String Name = rs.getString("JOBNAME");
				String Salary = rs.getString("JOBSALARY");
				String Status = rs.getString("JOBSTATUS");

				jtxtDutiesName.setText(Name);
				jtxtDutiesSalary.setText(Salary);
	
				if(Status.equals("0"))
				{
					jrdbStatus[0].setSelected(true);
				}
				else
				{
					
					jrdbStatus[1].setSelected(true);
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		}
		catch (Exception e) 
		{
			System.out.println("关闭异常！");
		}
	}
	
	/*******************************************************
	* Function Name: 	findStatus
	* Purpose: 			查询数据库中的所有可用的职务，即状态为0的职务
	* Params:           无
	* Return: 		           空
	*******************************************************/

	private void findStatus()       
	{
		 String SQLInsertString="select * from SYS_MEC_JOB where jobStatus = '0' order by(jobNo) ";
		 dlmDutiesList.removeAllElements();
		 
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLInsertString); 
				
				checkListValues = false;//判断职务列表框中是否有数据项，如果没有, 查询数据库，将数据库中的值插入到职务列表框中。
				while(rs.next())
				{
					
					String No = rs.getString("JOBNO");
					String Name = rs.getString("JOBNAME");
			
					dlmDutiesList.addElement(No + " " + Name);
				
					if(dlmDutiesList.getSize() > 0)
				   {
						jlstDutiesList.setSelectedIndex(0);
						jrdbStatus[0].setSelected(true);
						setStatus(BROWS);
				   }
				   else
				   {
					   dlmDutiesList.removeAllElements();
					   jtxtDutiesName.setText("");
				   }
				}
						
				String  i = dlmDutiesList.getSize() + ""; 
				jlblAllDuties.setText("共" + i + "个职务");
				
				checkListValues = true;
				String selected = jlstDutiesList.getSelectedValue().toString();
				jlblDutiesNumberContent.setText(selected.substring(0,3));
			    String S = jlblDutiesNumberContent.getText();
			    
				getDutiesInformation(S);
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			try 
			{
				FStatement.disConnection();
			}
			catch (Exception e) 
			{
				System.out.println("关闭异常！");
			}
	}
	
	/*******************************************************
	* Function Name: 	updateDutiesInformation
	* Purpose: 			修改列表中的信息，此时只能修改职务的状态
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void updateDutiesInformation()   
	{
		
		String selected = jlstDutiesList.getSelectedValue().toString();
		jlblDutiesNumberContent.setText(selected.substring(0,3));
	    String S = jlblDutiesNumberContent.getText();
	    
		String SQLInsertString="update SYS_MEC_JOB set jobStatus = '1' where jobNo = '" + S +"'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLInsertString);
		}
		
		catch (SQLException e) 
		{
			MECTool.showMessage(jfrmMain, e.getMessage() + "请呼唤数据库管理员前来处理该问题。" +
			"\n对此微易码科技深表歉意！");
			e.printStackTrace();
		}
		try 
		{
			FStatement.disConnection();
		} catch (Exception e)
		{
			MECTool.showMessage(jfrmMain, "数据库关闭异常");
			e.printStackTrace();
		}
	}
	
	/*******************************************************
	* Function Name: 	addDutiesInformation
	* Purpose: 			添加职务信息
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void addDutiesInformation()         
	    
	{
		
	    	int i; 
			int j;
			String str;
			String strName;
			String strSalary = null ;
			String SQLString="select MAX(jobNo) from SYS_MEC_JOB";
			String Rstr = null;
			int length;
			boolean ok = true;
			boolean rs ;
		
			try 
			{
				
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				
				FResultSet rs1 = FStatement.executeQueryCon(SQLString); 
				while(rs1.next())
				{
					Rstr=rs1.getString("MAX(jobNo)");
				}
				if(Rstr==null)
				{
					str="001";
					strName=jtxtDutiesName.getText();
					strSalary = jtxtDutiesSalary.getText();
					String regEx = "[\u4e00-\u9fa5]{2,10}+$";
					Pattern pat = Pattern.compile(regEx);   
					Matcher mat = pat.matcher(strName); 
					rs =  mat.find() ;  
					
					if(rs == true && strSalary.length()>=4 && strSalary.length()<=5)//月薪必须为4位-5位数字之间
					{
					String SQLInsertString="insert into SYS_MEC_JOB values('" + str +"', '"+strName+"', '"+strSalary+"','0')";
					
					FStatement.executeUpdateCon(SQLInsertString);
				
					}
					else
					{
						MECTool.showMessage(jfrmMain, "您输入的信息有误，请重新输入");
					}
					FStatement.disConnection();
				}
				else
				{
					i = Integer.parseInt(Rstr);
					str=i+1+"";
					if(str.length()==1)
					{
						str="00"+str;
					}
					else
					{
						str = "0" + str;
					}
				    length=dlmDutiesList.getSize();
					
					strName=jtxtDutiesName.getText();
					strSalary = jtxtDutiesSalary.getText();
					
					String regEx ="[\u4e00-\u9fa5]{2,10}+$";
					Pattern pat = Pattern.compile(regEx);   
					Matcher mat = pat.matcher(strName); 
					rs =  mat.find() ;  
					if(rs ==true && strSalary.length()>=4 && strSalary.length()<=5)//月薪必须为4位数字-5位数字之间
					{
					    jtxtDutiesName.setText(strName);
					    jtxtDutiesSalary.setText(strSalary);
					
					    for(j=0;j<length;j++)
					   {
						   if(dlmDutiesList.getElementAt(j).toString().substring(4).equals(strName))
						      ok=false;
					   }
				    	if(ok)
					   {
					       String SQLInsertString="insert into SYS_MEC_JOB values('" + str +"', '"+ strName+"', '"+strSalary+"','0')";
					       FStatement.executeUpdateCon(SQLInsertString);
					   }
					   else
					   {
						  MECTool.showMessage(jfrmMain, "添加重复");
					   }
					}
					else
					{
						MECTool.showMessage(jfrmMain, "您输入的信息有误，请重新输入");
					}
			   }
				  
				 FStatement.disConnection();
				}
				catch (Exception e) {
				MECTool.showMessage(jfrmMain, e.getMessage() + "请呼唤数据库管理员前来处理该问题。" +
				"\n对此微易码科技深表歉意！");
			}
	    }
	
	/*******************************************************
	* Function Name: 	reinitFrame
	* Purpose: 			再次初始化
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void reinitFrame()
	{
		initDutiesList();
	}
	
	/*******************************************************
	* Function Name: 	initDutiesList
	* Purpose: 			初始化职务列表
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void initDutiesList()    
	{
		 String SQLString = "SELECT jobNo,jobName,jobStatus FROM  SYS_MEC_JOB order by(jobNo)";
		 dlmDutiesList.removeAllElements();
		 
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				
				checkListValues = false;//判断职务列表框中是否有数据项，如果没有, 查询数据库，将数据库中的值插入到职务列表框中。
				while(rs.next())
				{
					
					String No = rs.getString("JOBNO");
					String Name = rs.getString("JOBNAME");
					String Status = rs.getString("JOBSTATUS");
				
					
					dlmDutiesList.addElement(No + " " + Name);
				
					if(dlmDutiesList.getSize() > 0)
				   {
						jlstDutiesList.setSelectedIndex(0);
						if(Status.equals("0"))
						    jrdbStatus[0].setSelected(true);
						else
							jrdbStatus[1].setSelected(true);
						setStatus(BROWS);
				   }
				   else
				   {
					   dlmDutiesList.removeAllElements();
					   jtxtDutiesName.setText("");
				   }
				}
						
				String  i = dlmDutiesList.getSize() + ""; 
				jlblAllDuties.setText("共" + i + "个职务");
				
				checkListValues = true;
				String selected = jlstDutiesList.getSelectedValue().toString();
				jlblDutiesNumberContent.setText(selected.substring(0,3));
			    String S = jlblDutiesNumberContent.getText();
			    
				getDutiesInformation(S);
			}
			
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			try 
			{
				FStatement.disConnection();
			}
			catch (Exception e) 
			{
				System.out.println("关闭异常！");
			}
		}
	
	/*******************************************************
	* Function Name: 	dealAction
	* Purpose: 			处理事件发生的动作
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void dealAction()
	{
		/*下拉列表的监听事件*/
	   choiceDutiesList.addItemListener  
		(
				new ItemListener()
				{
					public void itemStateChanged(ItemEvent arg0)
					{
	
						if(choiceDutiesList.getSelectedItem().equals("所有职务列表"))
						{
							initDutiesList();  
						}
						else
						{
							findStatus();
						}
					}
				}
		);
	
	   /*职务列表框中被选中数据项的监听事件*/
		jlstDutiesList.addListSelectionListener   
		(
				new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent arg0)
					{
						String S;
						if((jlstDutiesList.getSelectedIndex() != -1)&& checkListValues ==  true)
						{
							String selected = jlstDutiesList.getSelectedValue().toString();
							jlblDutiesNumberContent.setText(selected.substring(0,3));
						    S = jlblDutiesNumberContent.getText();
						    getDutiesInformation(S);
						}
						
					}
					
				}
		);
		
		/*"退出"按钮的监听事件*/
		jbtnExit.addActionListener    
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						MECTool.showMessage(jfrmMain, "您确定要退出吗？");
						dealClose();
					}
					
				}
	    );
		
		/*"修改"按钮的监听事件*/
		jbtnModify.addActionListener 
		(
				
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						int i = 0;
						if (jbtnModify.getText().equals("修改"))
						{
							jbtnModify.setText("放弃");
							jbtnAdd.setText("确定");
							
							i = choiceDutiesList.getSelectedIndex();
							jtxtDutiesName.setEditable(false);
							jtxtDutiesSalary.setEditable(false);
							
							setStatus(EDIT);
						}
						else
						{
							checkListEmpty();
							jbtnModify.setText("修改");
							jbtnAdd.setText("添加");
							jtxtDutiesName.setEditable(true);
							jtxtDutiesSalary.setEditable(true);
							choiceDutiesList.setEnabled(true);
							
							initDutiesList();
							choiceDutiesList.select(i);
							jbtnModify.setEnabled(true);
					
							setStatus(BROWS);
							
						}
					}
				}
	    );
      
		/*"添加"按钮的监听事件*/
		jbtnAdd.addActionListener    
		(
				new ActionListener()
				{
					
					public void actionPerformed(ActionEvent e) 
					{
						int i = 0;
						if (jbtnAdd.getText().equals("添加"))
						{
							jbtnAdd.setText("确定");
							jbtnModify.setText("放弃");
							setStatus(EDIT);
							
							i = choiceDutiesList.getSelectedIndex();
							
							jlblDutiesNumberContent.setText(null);//清空职务编号所对应的内容
							jtxtDutiesSalary.setText(null);
							
							jlblDutiesStatus.setEnabled(true);
							choiceDutiesList.setEnabled(false);
							
							jrdbStatus[0].setEnabled(false);
							jrdbStatus[1].setEnabled(false);
							
							jtxtDutiesName.requestFocus();
							jtxtDutiesName.selectAll();
							
						
						    lastAction = ADDATION;
						    lastName = jtxtDutiesName.getText();
					   }
						else
						{
							if(lastAction == ADDATION)
							{
								addDutiesInformation();
								initDutiesList();
								
							}
							else
							{
							    updateDutiesInformation();
							    initDutiesList();
							}
							jbtnAdd.setText("添加");
							jbtnModify.setText("修改");
							
							choiceDutiesList.select(i);
	
							jrdbStatus[0].setEnabled(true);
							jrdbStatus[1].setEnabled(true);
							choiceDutiesList.setEnabled(true);
							
							setStatus(BROWS);
							
						}
					}
					
				}
		);
	}

	/*******************************************************
	* Function Name: 	initFrame
	* Purpose: 			初始化
	* Params :          无
	* Return: 			空
	*******************************************************/

	private void initFrame()
	{
		Dimension dim;
		Font normalFont = new Font("宋体",Font.PLAIN,16);
		
		jfrmMain = new JFrame(TOPIC);
		container = jfrmMain.getContentPane();
		container.setLayout(null);
		jfrmMain.setSize(6750/15, 5700/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2,(dim.height - jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel(TOPIC);
		jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,24));
		jlblTopic.setForeground(Color.blue);
		jlblTopic.setBounds(2647/15,0,1440/15,465/15);
		container.add(jlblTopic);
		
		choiceDutiesList = new Choice();
		choiceDutiesList.setFont(normalFont);
		choiceDutiesList.setBounds(240/15,720/15,2895/15,360/15);
		choiceDutiesList.add("所有职务列表");
		choiceDutiesList.add("可用职务列表");
		container.add(choiceDutiesList);
		
		dlmDutiesList = new DefaultListModel();
		jlstDutiesList = new JList(dlmDutiesList);
		jscpDutiesList = new JScrollPane(jlstDutiesList);
		jlstDutiesList.setFont(normalFont);
		
		jscpDutiesList.setBorder(BorderFactory.createTitledBorder("职务列表"));
		jscpDutiesList.setBounds(240/15, 1080/15, 2895/15, 2895/15);
		container.add(jscpDutiesList);
		
		jlblAllDuties = new JLabel("共0个职位");
		jlblAllDuties.setFont(normalFont);
		jlblAllDuties.setBounds(240/15,4080/15,2760/15,240/15);
		container.add(jlblAllDuties);

		
		jlblDutiesInformation = new JLabel("职务信息");
		jlblDutiesInformation.setFont(normalFont);
		jlblDutiesInformation.setBounds(4320/15,720/15,960/15,240/15);
		container.add(jlblDutiesInformation);
		
		jlblDutiesNumber = new JLabel("职务编号");
		jlblDutiesNumber.setFont(normalFont);
		jlblDutiesNumber.setBounds(3360/15,1200/15,960/15,240/15);
		container.add(jlblDutiesNumber);
		
		jlblDutiesNumberContent = new JLabel("");
		jlblDutiesNumberContent.setFont(normalFont);
		jlblDutiesNumberContent.setBounds(4440/15,1200/15,1935/15,255/15);
		container.add(jlblDutiesNumberContent);
		
		jlblDutiesName = new JLabel("职务名称");
		jlblDutiesName.setFont(normalFont);
		jlblDutiesName.setBounds(3360/15,1920/15,960/15,240/15);
		container.add(jlblDutiesName);
		
		jtxtDutiesName = new JTextField("");
		jtxtDutiesName.setFont(normalFont);
		jtxtDutiesName.setBounds(4440/15,1853/15,1935/15,375/15);
		container.add(jtxtDutiesName);
		
		jlblDutiesSalary = new JLabel("职务月薪");
		jlblDutiesSalary.setFont(normalFont);
		jlblDutiesSalary.setBounds(3360/15,2640/15,960/15,240/15);
		container.add(jlblDutiesSalary);
		
		jtxtDutiesSalary = new JTextField("");
		jtxtDutiesSalary.setFont(normalFont);
		jtxtDutiesSalary.setBounds(4440/15,2573/15,1695/15,375/15);
		container.add(jtxtDutiesSalary);
		
		jblDutiesSalaryUnit = new JLabel("元");
		jblDutiesSalaryUnit.setFont(normalFont);
		jblDutiesSalaryUnit.setBounds(6120/15,2640/15,240/15,240/15);
		container.add(jblDutiesSalaryUnit);
		
		
		jlblDutiesStatus = new JLabel("职务状态");;
		jlblDutiesStatus.setFont(normalFont);
		jlblDutiesStatus.setBounds(3360/15,3360/15,960/15,240/15);
		container.add(jlblDutiesStatus);
		
		bgStatus = new ButtonGroup();
		jrdbStatus = new JRadioButton[2];
		
		jrdbStatus[0] = new JRadioButton("可用");
		jrdbStatus[0].setFont(normalFont);
		jrdbStatus[0].setBounds(4440/15, 3293/15, 975/15-5, 375/15);
		bgStatus.add(jrdbStatus[0]);
		container.add(jrdbStatus[0]);
		
		jrdbStatus[1] = new JRadioButton("不可用");
		jrdbStatus[1].setFont(normalFont);
		jrdbStatus[1].setBounds(5280/15, 3293/15, 1095/15, 375/15);
		bgStatus.add(jrdbStatus[1]);
		container.add(jrdbStatus[1]);
		

		jbtnAdd = new JButton("添加");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(240/15, 4560/15, 1215/15, 465/15);
		container.add(jbtnAdd);
		
		jbtnModify = new JButton("修改");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1440/15,4560/15, 1215/15, 465/15);
		container.add(jbtnModify);
		
		jbtnExit = new JButton("退出");
		jbtnExit.setFont(normalFont);
		jbtnExit.setBounds(5160/15,4560/15, 1215/15, 465/15);
		container.add(jbtnExit);
		
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrmMain.setVisible(true);
		
	}
	
	public static void main(String[] agrs)
	{
		new DutiesManagement();
	}
}
