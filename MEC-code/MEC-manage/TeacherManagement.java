
/*******************************************************
* Source code in : 
源代码的路径

* Function List:                程序中所涉及到的方法
*       setStatus		教师状态的设置
*       dealClose	        关闭整个窗口
*	isUniqueId	     	判别身份证号唯一性
*	InsertData		添加教师信息
*	getNumber               得到教师编号
*       dealAction              处理监听事件
*       getStatus               得到指定教师的教师状态信息
*       updateData              修改教师状态信息
*       CombineSelect           得到教师授课表信息
*       initTeacherList         初始化带课教师列表
*       secondFind              得到身份证号与联系方式信息
*       initAll                 初始化所有状态教师列表
*       showMessage             显示弹出式对话框信息
*       initFrameMain           初始化界面
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class TeacherManagement 
{
	private JFrame jfrmMain;                       /*界面窗体*/    
	private Container con;			       /*存储界面控件*/
	 
	private JLabel jlblTopic;                      /*界面大标题*/
	private JLabel jlblTeacherInfo;                /*教师信息标签*/
	
	private JLabel jlblTeacherNumber;	       /*教师编号标签*/
	private JLabel  jlblTeacherNum;		       /*教师编号文本框*/
	
	private JLabel jlblTeacherName;		       /*教师姓名标签*/
	private JTextField jtxtTeacherName;	       /*教师姓名文本框*/
	
	private JLabel jlblTeacherId;		       /*身份证号标签*/
	private JTextField  jtxtTeacherId;             /*身份证号文本框*/
	
	private JLabel jlblTeacherTel;                 /*联系方式标签*/
	private JTextField jtxtTeacherTel;             /*联系方式文本框*/
	             
	private JLabel jlblCourse;		       /*教师授课表标题*/	
	private DefaultListModel  dlmCourse;	       /*教师授课表中存储内容*/ 
	private JList  jlstCourse;		       /*教师授课表*/
	private JScrollPane  jscpCourse;               /*加滚动条的教师授课表*/
	
	private JLabel jlblStatus;                     /*状态标签*/
	private JRadioButton[] jrdbStatus;             /*单选按钮数组*/
	private ButtonGroup   bgStatus;		       /*管理单选按钮*/
	
	private JLabel jlblTeacherList;                /*教师列表标题*/
	private DefaultListModel  dlmTeacherList;      /*教师列表中存储内容*/ 
	private JList  jlstTeacherList;		       /*教师列表*/
	private JScrollPane  jscpTeacherList;          /*加滚动条的教师列表*/
	
        private JLabel  jlblTeacherCount;              /*统计教师列表中的记录数*/
	
	private JButton jbtnAdd;		       /*添加按钮*/
	private JButton jbtnModify;		       /*修改按钮*/
	private JButton jbtnExit;                      /*退出按钮*/
	
	private JComboBox  jcmbChioceList;             /*下拉列表框*/
	
	private int lastAction;
	
	private static final int ADDTION = 1;
	private static final int MODIFY = 2;

	private static final int  BROW = 0;
	private static final int  EDIT =1;
	
	private static final String Topic = "教师基本信息管理";
	private final static String[] jcmbstring= {"仅显示带课教师的信息","显示全部教师的信息"};
	
	/*******************************************************
	* Function Name: 	        TeacherManagement
	* Params :                      无
	* Purpose: 		        教师信息管理构造方法
	*******************************************************/

	public TeacherManagement()
	{
		initFrameMain();
		initTeacherList();
		dealAction();
	    
		setStatus(BROW);
	}

	/*******************************************************
	* Function Name: 	        setStatus
	* Purpose: 			设置状态信息
	* Params : 
	* @int    status                浏览或编辑状态
	* Return: 			void   	
	*******************************************************/

	private void setStatus(int status) 
	{
		boolean  Status = status==BROW;	
		
		jcmbChioceList.setEnabled(Status);
		jlstTeacherList.setEnabled(Status);
		
		jlblTeacherNum.setEnabled(!Status);
		jtxtTeacherName.setEnabled(!Status);
		jtxtTeacherId.setEnabled(!Status);	
		jtxtTeacherTel.setEnabled(!Status);
		
		jscpCourse.setEnabled(!Status);
		jlstCourse.setEnabled(!Status);
		
		jrdbStatus[0].setEnabled(!Status);
		jrdbStatus[1].setEnabled(!Status);
	}

	/*******************************************************
	* Function Name: 	        dealClose
	* Params :                      无
	* Purpose: 			处理退出界面
	*******************************************************/

       public void dealClose()
       {
    	  showMessage(jfrmMain,"你确定要退出吗");
    	  jfrmMain.dispose();
       }

      /*******************************************************
      * Function Name: 	                isUniqueId
      * Purpose: 			判别身份证号唯一性
      * Params : 
      * @String sid                     用户输入的身份证号
      * Return: 			boolean 	
      *******************************************************/

      private boolean isUniqueId(String sid)
      {
 	  boolean bool = false ;
    		
          String SQLString = "SELECT count(*) from SYS_MEC_TEACHER WHERE teacherId = '"+sid+"'";
 	  try 
 	  {
 		FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
 		
 		FResultSet rs = FStatement.executeQueryCon(SQLString); 
 		
 		while(rs.next())
 		{
 			if(rs.getString("COUNT(*)").equals("0"))
 				bool= true;				
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
 	 return bool;
      }
 
        /*******************************************************
	* Function Name: 	        InsertData 
	* Params :                      无
	* Purpose: 			向数据库插入信息
	* Return: 			void   	
	*******************************************************/ 

       public void InsertData()
      {
    	String  strno =getNumber();
	
    	String  strName = null;
    	String  strId = null;
        String  strTel = null;
	boolean ok = true; 
        final   String[] regEx = {"^[\u4e00-\u9fa5]{2,10}$", "^[1-9]\\d{16}[X|\\d]$", "^[1][34568][0-9]{9}$"};
        
        /*判断姓名是否合法*/
        Pattern pat = Pattern.compile(regEx[0]);   
		Matcher mat = pat.matcher(jtxtTeacherName.getText()); 
        boolean res = mat.find();   
		if(res==false)
		{
			 showMessage(jfrmMain,"输入不合法，请重新输入2-10个汉字");
			 jtxtTeacherName.setText("");
			 jtxtTeacherName.getText();
			 ok = false;
		}
		else
		         strName = jtxtTeacherName.getText();
	
		/*判断身份证号是否合法*/
		pat = Pattern.compile(regEx[1]);   	
		mat = pat.matcher(jtxtTeacherId.getText());
	        res = mat.find(); 
	    if(ok==true)
	    {  
	    	if(res == false)
		{
			showMessage(jfrmMain,"输入不合法，请重新输入18位身份证号");
			jtxtTeacherId.setText("");
			jtxtTeacherId.getText();
		        ok = false;	 
		}
		else
		{
			String sid= jtxtTeacherId.getText();
			/*判断身份证号是否重复*/
			if(isUniqueId(sid) == false)
			{
				showMessage(jfrmMain,"身份证号输入不能重复，请重新输入！");
				jtxtTeacherId.setText("");
				jtxtTeacherId.getText();
				ok = false;
			}		    
			else
			        strId = sid;
		}
	    }
	    	
		
            /*判断联系方式是否合法*/
	    pat = Pattern.compile(regEx[2]);   	
	    mat = pat.matcher(jtxtTeacherTel.getText()); 
	    res = mat.find();   
	    if(ok == true)/*姓名、身份证号均合法*/
	    {
	    	if(res==false )
		{
			 showMessage(jfrmMain,"输入不合法，请重新输入11位手机号");
			 jtxtTeacherTel.setText("");
			 jtxtTeacherTel.getText();
			 ok = false;	 
		}
	    	else
		         strTel = jtxtTeacherTel.getText();
	    }
	    
          /*全部合法后插入*/
         if(ok == true)
         {
    	    String SQLInsertString = "insert into SYS_MEC_TEACHER (teacherNo,teacherName,teacherId,teacherTel,teacherStatus) values ( '"+strno +"', '"+strName+"','"+strId+"','"+strTel+"','0')";
    	    System.out.println(SQLInsertString); 
    	    try 
	     {
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FStatement.executeUpdateCon(SQLInsertString);
 
	     } catch (SQLException e) 
	     {
			System.out.println("插入异常！ ");
	     }	
             try      
	     {
		 	FStatement.disConnection();
	     } catch (Exception e) 
	     {
			System.out.println("关闭异常！");
	     }
        }    
    }
      
        /*******************************************************
	* Function Name: 	        getNumber 
	* Params :                      无
	* Purpose: 			得到教师编号
	* Return: 			String  	
	*******************************************************/

       public String getNumber()
       {
    	       String time = null;
    	       int    i ;
    	       String Ostr = null;      //从数据库取得最后一条记录的教师编号
    	       String str1 = null;      //后两位序号
               String SQLString1 = "SELECT MAX(teacherNo) FROM SYS_MEC_TEACHER";
		
	       try 
	       {
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs1 = FStatement.executeQueryCon(SQLString1); 
			
			while(rs1.next())
			{
				Ostr= rs1.getString("MAX(teacherNo)");
			}
			if(Ostr.isEmpty())
			{
				str1 = "01";
			}
			else
			{
				i = Integer.parseInt(Ostr.substring(6, 8));
				i = i+1;
				str1 = i+"";
				if(str1.length()==1)
			           str1="0"+str1;
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
		
       	       /*得到系统时间*/
    	       String  SQLString = "select to_char(sysdate,'yymmdd') from dual";
    	       try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			while(rs.next())
			{	
				time = rs.getString(0)+str1.substring(0,0+2);
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
	    return time;
       }
        
        /*******************************************************
	* Function Name: 	        dealAction 
	* Params :                      无
	* Purpose: 			事件处理
	* Return: 			void  	
	*******************************************************/

	private void dealAction()
	{
		
                /*"退出"按钮的监听事件*/
		jbtnExit.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						dealClose();
					}
				}
		);
		
                /*窗体的监听事件*/
		jfrmMain.addWindowListener
		(
			new WindowListener()
			{
				public void windowActivated(WindowEvent arg0) {}
				public void windowClosed(WindowEvent arg0) {}
				public void windowDeactivated(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
				public void windowOpened(WindowEvent arg0){}
				
				public void windowClosing(WindowEvent arg0) 
				{
					dealClose();
				}
			}
		);
 
		/*"添加"按钮的监听事件*/
		jbtnAdd.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					if(jbtnAdd.getText().equals("添加"))
					{
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						setStatus(EDIT);
						
						jrdbStatus[0].setEnabled(false);
						jrdbStatus[1].setEnabled(false);
						
						jlstCourse.setEnabled(false);
						
						jtxtTeacherName.setEditable(true);
						jtxtTeacherId.setEditable(true);
						jtxtTeacherTel.setEditable(true);
						
						jlblTeacherNum.setText("");
						jtxtTeacherName.setText("");
						jtxtTeacherId.setText("");
						jtxtTeacherTel.setText("");
						
						jtxtTeacherName.requestFocus();
						jtxtTeacherName.selectAll();
						
						lastAction = ADDTION;
					    
					}
					else//点击“确定” 添加数据 到数据库及教师列表
					{
						if(lastAction == ADDTION)
						{			
							jtxtTeacherName.requestFocus();
							jtxtTeacherName.selectAll();
							
							InsertData();
							initTeacherList();	
						}
						else
						{
							updateData();
							initTeacherList();
						}
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
						jtxtTeacherName.removeAll();
						setStatus(BROW);	
					}
				}				
			}
		);
		
                /*"修改"按钮的监听事件*/
		jbtnModify.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					if(jbtnModify.getText().equals("修改"))
					{
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						setStatus(EDIT);
						
						jtxtTeacherName.setEditable(false);
						jtxtTeacherId.setEditable(false);
						jtxtTeacherTel.setEditable(false);
						
						jlstCourse.setEnabled(false);//点击“修改”，所授科目列表应为不可更改状态
					
						lastAction = MODIFY;
					        jlblTeacherNum.getText();
						jtxtTeacherName.getText();
				                jtxtTeacherId.getText();
					        jtxtTeacherTel.getText();
						
					}
					else//点击 “放弃”
					{
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
					        jrdbStatus[0].setSelected(true);
						initTeacherList(); 
							
						setStatus(BROW);
					}
				}			
			}	
		);

		/*教师列表框是否选中的监听事件*/
		jlstTeacherList.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0)
				{
					if(jlstTeacherList.getSelectedIndex()!=-1)
					{
						String selected = jlstTeacherList.getSelectedValue().toString();
						jlblTeacherNum.setText(selected.substring(0,0+8));//selected.substring(0,0+8)选中的编号
						jtxtTeacherName.setText(selected.substring(9));
					        secondFind(); 
					
					        getStatus();
					}
				}		
			}
		);

		/*教师列表框的鼠标点击监听事件*/
		jlstTeacherList.addMouseListener
		(
			new  MouseListener()
			{
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
				
				public void mouseClicked(MouseEvent arg0) 
				{
					if(jlstTeacherList.getSelectedIndex()!=-1)
					{
						String selected = jlstTeacherList.getSelectedValue().toString();
						jlblTeacherNum.setText(selected.substring(0,0+8));
						jtxtTeacherName.setText(selected.substring(9));
						secondFind();
						 
						getStatus();
					}
				}				
			}
		);

		/*下拉列表框的鼠标点击监听事件*/
		jcmbChioceList.addItemListener
		(
				new ItemListener()
				{
					public void itemStateChanged(ItemEvent arg0) 
					{
						if(jcmbChioceList.getSelectedIndex()==0)
						{
							dlmCourse.removeAllElements();
							initTeacherList();				   
						}
						else
						{  
							dlmCourse.removeAllElements();
							initAll();	
						}
					}
				}
	     );	
	}	

	/*******************************************************
	* Function Name: 	        getStatus
	* Params :                      无
	* Purpose: 			根据用户选中的编号查询数据库得到教师状态信息
	* Return: 			void   	
	*******************************************************/

	private void getStatus()  
	{
		String sec = jlstTeacherList.getSelectedValue().toString().substring(0,0+8);
		String SQLString = "select teacherStatus from SYS_MEC_TEACHER where teacherNo = '" + sec + "'";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString);
		        String status = null;
			while(rs.next())
			{
				status = rs.getString("TEACHERSTATUS");
				if(status.equals("0"))
				{
					jrdbStatus[0].setSelected(true);
				}
				else
					jrdbStatus[1].setSelected(true);
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
	* Function Name: 	        updateData
	* Params :                      无
	* Purpose: 			根据用户选中的信息完成该记录在数据库中的状态的修改
	* Return: 			void   	
	*******************************************************/

        private void updateData()
       {
    	        String sec = jlstTeacherList.getSelectedValue().toString().substring(0,0+8);
                String SQLString = "UPDATE  SYS_MEC_TEACHER SET teacherStatus = '1' WHERE teacherNo = '" + sec +"'";
		
		try 
		{
			 FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
		         FStatement.executeUpdateCon(SQLString); 
		} catch (SQLException e) 
		{
			 System.out.println("修改异常！");
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
	* Function Name: 	        initAll
	* Params :                      无
	* Purpose: 			初始化所有状态的教师信息
	* Return: 			void   	
	*******************************************************/

	private void initAll()
	{
		String SQLString = "select teacherNo,teacherName  from SYS_MEC_TEACHER   order by teacherNo asc";
		dlmTeacherList.removeAllElements();
		
		try 
		{
                        FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			String No;
			String Name;
			while(rs.next())
			{
			   No = rs.getString("TEACHERNO");
			   Name = rs.getString("TEACHERNAME");
			   dlmTeacherList.addElement(No + " " + Name);
			}
			String no;
			String name;
			
			if(dlmTeacherList.getSize()>0)
			{
				jlstTeacherList.setEnabled(true);
				jlstTeacherList.setSelectedIndex(0);
			
				no = dlmTeacherList.getElementAt(0).toString().substring(0, 0+8);
				name = dlmTeacherList.getElementAt(0).toString().substring(9);
				jlblTeacherNum.setText(no);
				jtxtTeacherName.setText(name);
			    
				secondFind();
				CombineSelect();
				
				jlblTeacherCount.setText("共"+dlmTeacherList.getSize()+"名教师");
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
	* Function Name: 	        CombineSelect
	* Params :                      无
	* Purpose: 			得到教师授课表信息 
	* Return: 			void   	
        *******************************************************/

	private void CombineSelect() 
       {
    	        String SQLString = "select teacherNo,subjectName from SYS_MEC_TEACHER_SUBJECT,SYS_MEC_SUBJECT where SYS_MEC_TEACHER_SUBJECT.subjectId=SYS_MEC_SUBJECT.subjectId order by teacherNo asc";
		dlmCourse.removeAllElements();
		
		try 
		{
                        FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			String comTno;
			String comSna;
			
			while(rs.next())
			{
			  comTno = rs.getString("TEACHERNO");
			  comSna = rs.getString("SUBJECTNAME");
			  dlmCourse.addElement(comTno + " " + comSna);
			  
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
	* Function Name: 	        initTeacherList
	* Params :                      无
	* Purpose: 			初始化带课教师信息
	* Return: 			void   	
	*******************************************************/

	private void initTeacherList()
	{
		String SQLString =  "select teacherNo,teacherName from SYS_MEC_TEACHER WHERE teacherStatus = '0'  order by teacherNo asc";
		dlmTeacherList.removeAllElements();
		
		try 
		{
                        FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			String No;
			String Name;
			
			while(rs.next())
			{
			   No = rs.getString("TEACHERNO");
			   Name = rs.getString("TEACHERNAME");
			   dlmTeacherList.addElement(No + " " + Name);
			  
			}

			String no;
			String name;
			
			if(dlmTeacherList.getSize()>0)
			{
				jlstTeacherList.setEnabled(true);
				no = dlmTeacherList.getElementAt(0).toString().substring(0, 0+8);
				name = dlmTeacherList.getElementAt(0).toString().substring(9);
				jlblTeacherNum.setText(no);
				jtxtTeacherName.setText(name);
		        
				jlstTeacherList.setSelectedIndex(0);
				
				secondFind();
				CombineSelect();
				jrdbStatus[0].setSelected(true);
				
				jlblTeacherCount.setText("共"+dlmTeacherList.getSize()+"名教师");
			}
			else
			{
				 jlblTeacherNum.setText("");
				 jtxtTeacherName.setText("");
				 jtxtTeacherTel.setText("");
				 jtxtTeacherId.setText("");
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
	* Function Name: 	        secondFind
	* Params :                      无
	* Purpose: 			根据选中的教师编号查询其身份证号和联系方式
	* Return: 			void   	
	*******************************************************/

	private void secondFind() 
	{
		String id;
		String tel = null;
		String strNo = jlstTeacherList.getSelectedValue().toString().substring(0,0+8);
		String SQLString = "select  teacherTel,teacherId from SYS_MEC_TEACHER WHERE teacherNo = '" + strNo + "'";
		try 
		{
                        FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			while(rs.next())
			{
			  id = rs.getString("TEACHERID");
			  tel = rs.getString("TEACHERTEL");
			  
			  jtxtTeacherId.setText(id);
			  jtxtTeacherTel.setText(tel);
			  
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
	* Function Name: 	        showMessage
	* Purpose: 			显示弹出对话框信息
	* Params : 
	* @JFrame jfrm	                界面窗体实例
	* @String mess		        类型为String的实例
	* Return: 			void   	
	*******************************************************/

	private void showMessage(JFrame jfrm,String mess)
	{
		JOptionPane.showMessageDialog(jfrm, mess, "微易码温馨提示", JOptionPane.DEFAULT_OPTION);
		
	}

	/*******************************************************
	* Function Name: 	        initFrameMain
	* Params :                      无
	* Purpose: 			初始化教师信息界面
	* Return: 			void   	
	*******************************************************/

	public void initFrameMain()
	{
		Dimension  dim;
		Font NormalFont = new Font("宋体",Font.PLAIN,16); 
		
		jfrmMain = new JFrame(Topic);
		con = jfrmMain.getContentPane();
		con.setLayout(null);
		jfrmMain.setSize(7605/15, 7470/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2, (dim.height - jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel (Topic);
		jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,24));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(2197/15, 0, 3090/15, 495/15);
		con.add(jlblTopic);
		
		jlblTeacherInfo = new JLabel("教师信息");
		jlblTeacherInfo.setFont(NormalFont);
		jlblTeacherInfo.setBounds(4920/15, 840/15, 975/15, 255/15);
		con.add(jlblTeacherInfo);
		
		
		jlblTeacherNumber = new JLabel("教师编号");
		jlblTeacherNumber.setFont(NormalFont);
		jlblTeacherNumber.setBounds(3600/15, 1500/15, 975/15, 255/15);
		con.add(jlblTeacherNumber);
		
		jlblTeacherNum = new JLabel();
		jlblTeacherNum.setFont(NormalFont);
		jlblTeacherNum.setBounds(4680/15, 1440/15, 2655/15, 375/15);
		con.add(jlblTeacherNum);
		
		jlblTeacherName = new JLabel("教师姓名");
		jlblTeacherName.setFont(NormalFont);
		jlblTeacherName.setBounds(3600/15, 2120/15, 975/15, 255/15);
		con.add(jlblTeacherName);
		
		jtxtTeacherName = new JTextField();
		jtxtTeacherName.setFont(NormalFont);
		jtxtTeacherName.setBounds(4680/15, 2060/15, 2655/15, 375/15);
		con.add(jtxtTeacherName);
		
		jlblTeacherId = new JLabel("身份证号");
		jlblTeacherId.setFont(NormalFont);
		jlblTeacherId.setBounds(3600/15, 2740/15, 975/15, 255/15);
		con.add(jlblTeacherId);
		
		jtxtTeacherId= new JTextField();
		jtxtTeacherId.setFont(NormalFont);
		jtxtTeacherId.setBounds(4680/15, 2680/15, 2655/15, 375/15);
		con.add(jtxtTeacherId);
		
		jlblTeacherTel = new JLabel("联系方式");
		jlblTeacherTel.setFont(NormalFont);
		jlblTeacherTel.setBounds(3600/15, 3360/15, 975/15, 255/15);
		con.add(jlblTeacherTel);
		
		jtxtTeacherTel= new JTextField();
		jtxtTeacherTel.setFont(NormalFont);
		jtxtTeacherTel.setBounds(4680/15, 3300/15, 2655/15, 375/15);
		con.add(jtxtTeacherTel);
		
		jlblCourse = new JLabel("所授科目");
		jlblCourse.setFont(NormalFont);
		jlblCourse.setBounds(4980/15, 3840/15, 975/15, 255/15);
		con.add(jlblCourse);
		
		dlmCourse = new DefaultListModel();
		jlstCourse = new JList(dlmCourse);
		jscpCourse = new JScrollPane(jlstCourse);
		jlstCourse.setFont(NormalFont);
		jscpCourse.setBounds(3600/15, 4320/15, 3735/15, 1260/15);
		con.add(jscpCourse);
		
		jlblStatus = new JLabel("状    态");
		jlblStatus.setFont(NormalFont);
		jlblStatus.setBounds(3600/15, 5760/15, 975/15, 255/15);
		con.add(jlblStatus);
		
		bgStatus = new ButtonGroup();
		jrdbStatus = new JRadioButton[2];
		
		jrdbStatus[0] = new JRadioButton("带课");
		jrdbStatus[0].setFont(NormalFont);
		jrdbStatus[0].setBounds(4800/15, 5640/15, 976/15, 495/15);
		bgStatus.add(jrdbStatus[0]);
		con.add(jrdbStatus[0]);
		
		jrdbStatus[1] = new JRadioButton("不带课");
		jrdbStatus[1].setFont(NormalFont);
		jrdbStatus[1].setBounds(6120/15-20, 5640/15, 976/15+8, 495/15);
		bgStatus.add(jrdbStatus[1]);
		con.add(jrdbStatus[1]);
		
		jcmbChioceList = new JComboBox(jcmbstring);
		jcmbChioceList.setBounds(120/15, 720/15, 3255/15, 360/15);
		jcmbChioceList.setFont(NormalFont);
		con.add(jcmbChioceList);
		
		
		dlmTeacherList = new DefaultListModel();
		jlstTeacherList = new JList(dlmTeacherList);
		jscpTeacherList = new JScrollPane(jlstTeacherList);
		jlstTeacherList.setFont(NormalFont);
		jscpTeacherList.setBounds(120/15, 1080/15, 3255/15, 4695/15);
		jscpTeacherList.setBorder(BorderFactory.createTitledBorder("教师列表"));
		con.add(jscpTeacherList);
		
		jlblTeacherCount = new JLabel("共0名教师");
		jlblTeacherCount.setFont(NormalFont);
		jlblTeacherCount.setBounds(120/15, 5880/15, 3135/15, 375/15);
		con.add(jlblTeacherCount);
		
		jbtnAdd = new JButton("添加");
		jbtnAdd.setFont(NormalFont);
		jbtnAdd.setBounds(120/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnAdd);
		
		jbtnModify = new JButton("修改");
		jbtnModify.setFont(NormalFont);
		jbtnModify.setBounds(1320/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnModify);
		
		jbtnExit = new JButton("退出");
		jbtnExit.setFont(NormalFont);
		jbtnExit.setBounds(6000/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnExit);
		
		
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrmMain.setVisible(true);
	}

	/*******************************************************
	* Function Name: 	        main 
	* Purpose: 			主方法
	* Return: 			void   	
	*******************************************************/

	public static void main(String[] args) 
	{
		new TeacherManagement();	
	}
}
