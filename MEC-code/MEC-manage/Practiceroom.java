
/*******************************************************

* Function List:
*		MachineRome3()						构造方法
*		initFrame();						创建界面		
*		dealAction();						事件监听
*		setstatus(BROWS);					初始化界面
*		initSubjectList();					初始化机房列表信息
*		showSubject(jlblNum.getText())		初始化机房界面信息	
*   	initComboBox();						初始化机房状态下拉列表
*       initcombox();						初始化分点下拉列表
*		math()								计算分点下的机房个数
*		zhengze()							判断机房名称输入长度，返回true或false


*******************************************************/
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
public class Practiceroom
{
	//子窗体
	private JInternalFrame jfrmMain;
	private JFrame jfrm;
	private JDesktopPane jdtp;
	private Container con;
	//标题
	private JLabel jlblTopic;
	
	//机房列表
	private DefaultListModel dlmPractice;
	private JList jlstPractice;
	private JScrollPane jscPractice;
	
	//分点
	private JLabel jlblPoint;
	private JComboBox jtxtPoint;
	
	//机房选择
	private JComboBox jcoPracticeroom;
	
	//机房数量
	private JLabel jlblNumber;
	
	//机房编号
	private JLabel jlblId;
	private JLabel jlblNum;
	
	//机房名称
	private JLabel jlblName;
	private JTextField jtxtName;
	
	//机子数目
	private JLabel jlblCount;
	private JTextField jtxtCount;
	private JLabel jlblTai;
	
	//机房状态
	private JLabel jlblStatus;
	private JRadioButton[] jraStatus;
	private ButtonGroup bgStatus;
				
	//添加
	private JButton jbtnAdd;
	
	//修改
	private JButton jbtnModify;
	
	//退出
	private JButton jbtnExit;
	
	/*表示分点下的机房个数*/
	private String Idd;
	
	/*最后一次操作 区别是添加的确定还是修改的确定*/
	private int lastAction;
	private static final int MODIFY=1;
	private static final int addition=0;
	
	/*全局变量 浏览和编辑状态*/
	private static final int BROWS = 0;
	private static final int EDIT = 1;
	
	
	public Practiceroom(DesktopPane jdtp, JFrame jfrm)  //构造方法
	{
		this.jfrm = jfrm;
		this.jdtp = jdtp; 
		
		initFrame();	
		reinitJframe();
		dealAction();
		setstatus(BROWS);
	}	
	private void reinitJframe() 
	{
        initSubjectList();
    	showSubject(jlblNum.getText());
    	initComboBox();
        initcombox();
       
	}
	
	private void showSubject(String Ud)  //初始化界面信息
	{
	
		Ud = jlstPractice.getSelectedValue().toString().substring(0,4);
		String SQLString =" SELECT practiceroomId, practiceroomName,practiceroomCount,practiceroomStatus FROM SYS_MEC_PRACTICEROOM WHERE practiceroomId='"+Ud+"'";
		try 
		{     FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			  FResultSet rs = FStatement.executeQueryCon(SQLString);
			  
			    String Id;
				String Name;
				String Count;
				String Status;
				String point;
			  while(rs.next())
			  {
				    Id = rs.getString("practiceroomId");
					Name = rs.getString("practiceroomName");
					Count=rs.getString("practiceroomCount");
					Status=rs.getString("practiceroomStatus");
					
					
					point=Id.substring(0, 2);
					//jtxtPoint.setText(point);
					jtxtCount.setText(Count);
					jtxtName.setText(Name);
					jlblNum.setText(Id);
				    jraStatus[Integer.parseInt(Status)].setSelected(true);
				  
			  }
		} catch (SQLException e) 
		 {
		 	
			e.printStackTrace();
		}
	}

	
	private void initComboBox()
    {
   
		jcoPracticeroom.removeAllItems();
		jcoPracticeroom.addItem("所有机房");
		jcoPracticeroom.addItem("可用机房");
		//jcoPracticeroom.setSelectedIndex(0);
		jcoPracticeroom.setEditable(false);
    }
	
	private void initcombox()  //初始化分点下拉列表
	{
		String StringSQL = " select SECTORID ,SECTORNAME from SYS_MEC_SECTOR where STATUS='0' order by SECTORID";
		try
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(StringSQL); 
		
			String Id;	
			String Name;
			
	    	
			while(rs.next())
			{
				
				Id = rs.getString("SECTORNAME");
				Name=rs.getString("SECTORID");
				
	
				jtxtPoint.addItem(Name+" "+Id);
				 
				
			}
			
			
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	
		try 
		{
			FStatement.disConnection();
		} catch (Exception e1) 
		{
			System.out.println("关闭异常！");
		}	
	}
 
	/*******************************************************
	* Function Name: 	math()
	* Purpose: 			取得以分点下拉列表选择的项开头的机房个数
	* Params : 
	*	@point			string		取分点下拉列表前两位 即分点号
	* Return: 			string 	返回以该分点开头的机房个数
	*******************************************************/

	private  void math()
	{
		String StringSQL;
		String point;
		point=((String) jtxtPoint.getSelectedItem()).substring(0, 2);//取分点下拉列表前两位即分点编号
		
		
		StringSQL="select count(*) from SYS_MEC_PRACTICEROOM where SUBSTR(practiceroomId,1,2)='"+point+"'";//计算以该分点开头的机房的个数
		
	    
		
		try
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(StringSQL); 
		
						
			
			
			while(rs.next())
			{
		
			Idd= rs.getString("COUNT(*)");
			
				
				 
				
			}
			
			
		} catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	
		try 
		{
			FStatement.disConnection();
		} catch (Exception e1) 
		{
			System.out.println("关闭异常！");
		}	
	}
			
	private boolean zhengze()  //判断输入内容是否合理
	{
		
		int  m;
		m =jtxtName.getText().length();
		if(2<m&&m<15)
		{
			return  true;
		}
		else
		{
			return false;
		}
	}
		
	
	
	private void initSubjectList()  //初始化列表信息
    {      
		   
	       String StringSQL = " SELECT practiceroomId, practiceroomName FROM SYS_MEC_PRACTICEROOM,SYS_MEC_SECTOR WHERE SUBSTR(practiceroomId,1,2)=sectorId AND status='0'order by practiceroomId ";
	       
		try {
			
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(StringSQL);
				dlmPractice.removeAllElements();
				
				String Id;
				String Name;
				int i = 0;
				while(rs.next())
				{
					i++;
					Id = rs.getString("practiceroomId");
			    	Name = rs.getString("practiceroomName");
			    	
			    	dlmPractice.addElement(Id + " " + Name);
			    	jlstPractice.setSelectedIndex(0);
				}

				jlblNumber.setText("共"+i+"机房");
		        jlstPractice.setSelectedIndex(0);
		    } catch (SQLException e) 
		      {
		    		
		    		e.printStackTrace();
		      }
		    try {
				    FStatement.disConnection();
			    } catch (Exception e) 
	              {  
			    	System.out.println("关闭异常！");
			      }
		if(dlmPractice.getSize() > 0)
		{
			jlstPractice.setSelectedIndex(0);
			String id = jlstPractice.getSelectedValue().toString().substring(0,4);
			jlblNum.setText(id);
		}
		else
		{
			jbtnModify.setEnabled(false);
		}

    	
    }
	
	
	private void setstatus(int Status)  //设置状态
	{
		boolean status = Status == BROWS;
		jcoPracticeroom.setEnabled(status);
		jscPractice.setEnabled(!status);
		
		jtxtPoint.setEnabled(!status);//检查
		jtxtName.setEnabled(!status);
		jtxtCount.setEnabled(!status);
		jraStatus[0].setEnabled(!status);
		jraStatus[1].setEnabled(!status);
			
	}
	
	
	private void dealAction() 
	{
		//退出监听
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					demoMenu1.removeClassNameFromArrayList(this.getClass());
					dealClose();	
				}
				
			}
		);
		
		jfrmMain.addInternalFrameListener						//监听JInternalFrame的退出事件；
		(
				new InternalFrameListener()
				{
					public void internalFrameClosing(InternalFrameEvent arg0) 
					{
						demoMenu1.removeClassNameFromArrayList(this.getClass());//需要调用demoMenu里 的方法；
					}
					public void internalFrameActivated(InternalFrameEvent arg0) {}
					public void internalFrameClosed(InternalFrameEvent arg0){}
					public void internalFrameDeactivated(InternalFrameEvent arg0) {}
					public void internalFrameDeiconified(InternalFrameEvent arg0) {}
					public void internalFrameIconified(InternalFrameEvent arg0) {}
					public void internalFrameOpened(InternalFrameEvent arg0) {}	
				}
		);
		
		//修改监听
		jbtnModify.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{						
						if(jbtnModify.getText().equals("修改"))
						{
							jbtnAdd.setText("确定");
							jbtnModify.setText("取消");
							setstatus(EDIT);
							
							jtxtName.setEditable(false);
							jtxtCount.setEditable(false);
							lastAction = MODIFY;				
						}
						else
						{
							jbtnAdd.setText("添加");
							jbtnModify.setText("修改");
							
							setstatus(BROWS);
							
							
						}
						
					}										
				}
						
		);
		
		//添加监听
		jbtnAdd.addActionListener
		(
				
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						
						if(jbtnAdd.getText().equals("添加"))
						{
							
							jbtnAdd.setText("确定");
							jbtnModify.setText("取消");
							setstatus(EDIT);
							jraStatus[0].setEnabled(false);
							jraStatus[1].setEnabled(false);
							
							lastAction = addition;				
						}
						else
						{
							if(lastAction == addition) //最后一次操作为添加的确定
							{
								// TODO 完成添加操作；指数据入库！
								
								if(zhengze())
								{
									String str;
									String strName;
							
									String SQLString ;
								
										String id;
								
							
								
										strName = jtxtName.getText();
										str=jtxtCount.getText();
							
										String s = ((Integer.parseInt(Idd) + 101) + "").substring(1,3);//将Idd转换为数字，与101相加，转换为字符串，取后两位，完成了自动编码的后两位的实现。
								
								
									
								
										//id=(String) jtxtPoint.getSelectedItem()+ s + "";
										id=((String) jtxtPoint.getSelectedItem()).substring(0,2)+ s + "";
								
								
										SQLString= "insert into SYS_MEC_PRACTICEROOM(practiceroomId, practiceroomName,practiceroomCount,practiceroomStatus) values ('"+id+"','" + strName + "','" + str + "','0')";
						
										jlblNum.setText(id);
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
								//dlmPractice.addElement(strName)
										
								}	
								else
								{
						
									JOptionPane.showMessageDialog(jfrmMain, "添加内容不合法", "微易码温馨提示", JOptionPane.DEFAULT_OPTION);
								}
								 
							}
							else
							{
								// TODO 完成修改操作；指更改数据库内容！
								 
								
								 String strStatus;
								 String SQLString;
								
								 String point=jlstPractice.getSelectedValue().toString().substring(0,4);
									if(jraStatus[0].isSelected())
										strStatus = "0";
									else
										strStatus = "1";
								
								SQLString="update SYS_MEC_PRACTICEROOM set practiceroomStatus='" + strStatus + "' where practiceroomId='"+point+"'";
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
							
							jbtnAdd.setText("添加");
							jbtnModify.setText("修改");
						
							setstatus(BROWS);
							
						}
						
					}
					
				}
				
		);
		
		//可用机房、所有机房下拉列表监听
		jcoPracticeroom.addItemListener
		(
				new ItemListener()
				{					
					public void itemStateChanged(ItemEvent arg0) 
					{
						if(jcoPracticeroom.isEnabled())
							
						{
							if(jcoPracticeroom.getSelectedItem().equals("所有机房"))
						
							{
								initSubjectList();
							}
								
						
						
						else 
						{
							
							String SQLString = "SELECT practiceroomId, practiceroomName FROM SYS_MEC_PRACTICEROOM WHERE PRACTICEROOMSTATUS=0";
							
							try
							{
								FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
								
								FResultSet rs = FStatement.executeQueryCon(SQLString); 
							
								String Id;
								String Name;
								int i=0;
								dlmPractice.removeAllElements();
								while(rs.next())
								{
									i++;
									Id = rs.getString("practiceroomId");
									Name = rs.getString("practiceroomName");
						
								
									dlmPractice.addElement(Id + " " +Name);
									
								}
								jlblNumber.setText("共"+i+"机房");
								// jlstPractice.setSelectedIndex(0);
								
							} catch (SQLException e1) 
							{
								e1.printStackTrace();
							}
						
							try 
							{
								FStatement.disConnection();
							} catch (Exception e1) 
							{
								System.out.println("关闭异常！");
							}	
						}
					 }
					
				}
				}
		);
		
		//分点下拉列表监听
		jtxtPoint.addItemListener
		(
				new ItemListener()
				{			
					public void itemStateChanged(ItemEvent e)
					{
						math();
					}
				}
						
		);
		
		//列表监听 调用函数showSubject
		jlstPractice.addMouseListener
		(
				new MouseListener()
				{
					public void mouseEntered(MouseEvent arg0){}
					public void mouseExited(MouseEvent arg0){}
					public void mousePressed(MouseEvent arg0){}
					public void mouseReleased(MouseEvent arg0){}
					
					public void mouseClicked(MouseEvent arg0) 
					{

					  if(jlstPractice.isEnabled())
					  {
						 if(jlstPractice.getSelectedIndex() >= 0)
						  {
							    String selected = jlstPractice.getSelectedValue().toString();
							    jlblNum.setText(selected.substring(0,4));
							    showSubject(jlblNum.getText());
							
						  }
						 else
						 {
							 jbtnModify.setEnabled(false);
						 }
						  
					  }
			 			
					}

					
				}
		);
		
		
	}
		private void dealClose()
		{
			jfrmMain.dispose();
		}
		
		
		//创建界面
		private void initFrame()
		{
			Dimension dim;
			Font normalFont = new Font("宋体", Font.PLAIN, 16);
			
			jfrmMain=new JInternalFrame("机房管理",false, true, false, true);
			con=jfrmMain.getContentPane();
			con.setLayout(null);
			jfrmMain.setSize(6975/15, 5205/15);
			
			dim=Toolkit.getDefaultToolkit().getScreenSize();
			jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2, (dim.height - jfrmMain.getHeight())/2);
			
			//标题
			jlblTopic = new JLabel("机房管理");
			jlblTopic.setFont(new Font("微软雅黑", Font.PLAIN, 24));
			jlblTopic.setForeground(Color.blue);
			jlblTopic.setBounds(2520/15, 0, 1815/15, 615/15);
			con.add(jlblTopic);
			
			//选择可用、全部机房下拉列表
			jcoPracticeroom=new JComboBox();
			jcoPracticeroom.setFont(new Font("宋体", Font.PLAIN, 12));
			jcoPracticeroom.setBounds(120/15, 600/15, 2895/15, 360/15);	
			con.add(jcoPracticeroom);
			
			//机房列表
			dlmPractice = new DefaultListModel();
			jlstPractice = new JList(dlmPractice);
			jscPractice = new JScrollPane(jlstPractice);
			jlstPractice.setFont(normalFont);
			jscPractice.setBounds(120/15, 960/15, 2895/15, 2775/15);
			jscPractice.setBorder(BorderFactory.createTitledBorder("机房信息"));
			con.add(jscPractice);
			
			//分点
			jlblPoint=new JLabel("分点");
			jlblPoint.setFont(normalFont);
			jlblPoint.setBounds(3240/15, 960/15, 960/15, 240/15);
			con.add(jlblPoint);
			
			//分点下拉列表
			jtxtPoint=new JComboBox();
			jtxtPoint.setFont(normalFont);
			jtxtPoint.setBounds(4200/15, 900/15, 2295/15, 375/15);
			con.add(jtxtPoint);
			
			//机房编号
			jlblId=new JLabel("机房编号");
			jlblId.setFont(normalFont);
			jlblId.setBounds(3240/15, 1590/15, 960/15, 240/15);
			con.add(jlblId);
			
			jlblNum=new JLabel("1234");
			jlblNum.setFont(normalFont);
			jlblNum.setBounds(4200/15, 1530/15, 2295/15,375/15);
			con.add(jlblNum);
			
			//机房名称
			jlblName=new JLabel("机房名称");
			jlblName.setFont(normalFont);
			jlblName.setBounds(3240/15, 2220/15, 960/15, 240/15);
			con.add(jlblName);
			
			jtxtName=new JTextField();
			jtxtName.setFont(normalFont);
			jtxtName.setBounds(4200/15, 2160/15, 2295/15,375/15);
			con.add(jtxtName);
			
			//机器数量
			jlblCount=new JLabel("机器数量");
			jlblCount.setFont(normalFont);
			jlblCount.setBounds(3240/15, 2850/15, 960/15, 240/15);
			con.add(jlblCount);
			
			jlblTai=new JLabel("台");
			jlblTai.setFont(normalFont);
			jlblTai.setBounds(6240/15, 2880/15, 240/15, 240/15);
			con.add(jlblTai);
			
			jtxtCount=new JTextField();
			jtxtCount.setFont(normalFont);
			jtxtCount.setBounds(4200/15, 2795/15, 1935/15,375/15);
			con.add(jtxtCount);
			
			//状态
			jlblStatus=new JLabel("状态");
			jlblStatus.setFont(normalFont);
			jlblStatus.setBounds(3240/15, 3480/15, 960/15, 240/15);
			con.add(jlblStatus);
			
			bgStatus = new ButtonGroup();
			jraStatus = new JRadioButton[2];
			
		    jraStatus[0] = new JRadioButton("可用");
			jraStatus[0].setFont(normalFont);
			jraStatus[0].setBounds(4200/15, 3480/15, 975/15, 255/15);
			bgStatus.add(jraStatus[0]);
			con.add(jraStatus[0]);
			
			jraStatus[1] = new JRadioButton("停用");
			jraStatus[1].setFont(normalFont);
			jraStatus[1].setBounds(5520/15, 3480/15, 975/15, 255/15);
			bgStatus.add(jraStatus[1]);
			con.add(jraStatus[1]);
			
			//机房数目
			jlblNumber=new JLabel("机房数目");
			jlblNumber.setFont(normalFont);
			jlblNumber.setBounds(120/15, 3840/15, 2895/15, 255/15);
			con.add(jlblNumber);

			//添加
			jbtnAdd = new JButton("添加");
			jbtnAdd.setFont(normalFont);
			jbtnAdd.setBounds(120/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnAdd);
			
			//修改
			jbtnModify = new JButton("修改");
			jbtnModify.setFont(normalFont);
			jbtnModify.setBounds(1320/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnModify);
			
			//退出
			jbtnExit = new JButton("退出");
			jbtnExit.setFont(normalFont);
			jbtnExit.setBounds(5400/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnExit);
		
			//jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jfrmMain.setVisible(true);
			
			jdtp.add(jfrmMain);
			
		}
		
		

}
