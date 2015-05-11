
/*******************************************************
* Source code in : 
Դ�����·��

* Function List:                ���������漰���ķ���
*       setStatus		��ʦ״̬������
*       dealClose	        �ر���������
*	isUniqueId	     	�б����֤��Ψһ��
*	InsertData		��ӽ�ʦ��Ϣ
*	getNumber               �õ���ʦ���
*       dealAction              ��������¼�
*       getStatus               �õ�ָ����ʦ�Ľ�ʦ״̬��Ϣ
*       updateData              �޸Ľ�ʦ״̬��Ϣ
*       CombineSelect           �õ���ʦ�ڿα���Ϣ
*       initTeacherList         ��ʼ�����ν�ʦ�б�
*       secondFind              �õ����֤������ϵ��ʽ��Ϣ
*       initAll                 ��ʼ������״̬��ʦ�б�
*       showMessage             ��ʾ����ʽ�Ի�����Ϣ
*       initFrameMain           ��ʼ������
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
	private JFrame jfrmMain;                       /*���洰��*/    
	private Container con;			       /*�洢����ؼ�*/
	 
	private JLabel jlblTopic;                      /*��������*/
	private JLabel jlblTeacherInfo;                /*��ʦ��Ϣ��ǩ*/
	
	private JLabel jlblTeacherNumber;	       /*��ʦ��ű�ǩ*/
	private JLabel  jlblTeacherNum;		       /*��ʦ����ı���*/
	
	private JLabel jlblTeacherName;		       /*��ʦ������ǩ*/
	private JTextField jtxtTeacherName;	       /*��ʦ�����ı���*/
	
	private JLabel jlblTeacherId;		       /*���֤�ű�ǩ*/
	private JTextField  jtxtTeacherId;             /*���֤���ı���*/
	
	private JLabel jlblTeacherTel;                 /*��ϵ��ʽ��ǩ*/
	private JTextField jtxtTeacherTel;             /*��ϵ��ʽ�ı���*/
	             
	private JLabel jlblCourse;		       /*��ʦ�ڿα����*/	
	private DefaultListModel  dlmCourse;	       /*��ʦ�ڿα��д洢����*/ 
	private JList  jlstCourse;		       /*��ʦ�ڿα�*/
	private JScrollPane  jscpCourse;               /*�ӹ������Ľ�ʦ�ڿα�*/
	
	private JLabel jlblStatus;                     /*״̬��ǩ*/
	private JRadioButton[] jrdbStatus;             /*��ѡ��ť����*/
	private ButtonGroup   bgStatus;		       /*����ѡ��ť*/
	
	private JLabel jlblTeacherList;                /*��ʦ�б����*/
	private DefaultListModel  dlmTeacherList;      /*��ʦ�б��д洢����*/ 
	private JList  jlstTeacherList;		       /*��ʦ�б�*/
	private JScrollPane  jscpTeacherList;          /*�ӹ������Ľ�ʦ�б�*/
	
        private JLabel  jlblTeacherCount;              /*ͳ�ƽ�ʦ�б��еļ�¼��*/
	
	private JButton jbtnAdd;		       /*��Ӱ�ť*/
	private JButton jbtnModify;		       /*�޸İ�ť*/
	private JButton jbtnExit;                      /*�˳���ť*/
	
	private JComboBox  jcmbChioceList;             /*�����б��*/
	
	private int lastAction;
	
	private static final int ADDTION = 1;
	private static final int MODIFY = 2;

	private static final int  BROW = 0;
	private static final int  EDIT =1;
	
	private static final String Topic = "��ʦ������Ϣ����";
	private final static String[] jcmbstring= {"����ʾ���ν�ʦ����Ϣ","��ʾȫ����ʦ����Ϣ"};
	
	/*******************************************************
	* Function Name: 	        TeacherManagement
	* Params :                      ��
	* Purpose: 		        ��ʦ��Ϣ�����췽��
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
	* Purpose: 			����״̬��Ϣ
	* Params : 
	* @int    status                �����༭״̬
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
	* Params :                      ��
	* Purpose: 			�����˳�����
	*******************************************************/

       public void dealClose()
       {
    	  showMessage(jfrmMain,"��ȷ��Ҫ�˳���");
    	  jfrmMain.dispose();
       }

      /*******************************************************
      * Function Name: 	                isUniqueId
      * Purpose: 			�б����֤��Ψһ��
      * Params : 
      * @String sid                     �û���������֤��
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
 		System.out.println("�ر��쳣��");
 	 }
 	 return bool;
      }
 
        /*******************************************************
	* Function Name: 	        InsertData 
	* Params :                      ��
	* Purpose: 			�����ݿ������Ϣ
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
        
        /*�ж������Ƿ�Ϸ�*/
        Pattern pat = Pattern.compile(regEx[0]);   
		Matcher mat = pat.matcher(jtxtTeacherName.getText()); 
        boolean res = mat.find();   
		if(res==false)
		{
			 showMessage(jfrmMain,"���벻�Ϸ�������������2-10������");
			 jtxtTeacherName.setText("");
			 jtxtTeacherName.getText();
			 ok = false;
		}
		else
		         strName = jtxtTeacherName.getText();
	
		/*�ж����֤���Ƿ�Ϸ�*/
		pat = Pattern.compile(regEx[1]);   	
		mat = pat.matcher(jtxtTeacherId.getText());
	        res = mat.find(); 
	    if(ok==true)
	    {  
	    	if(res == false)
		{
			showMessage(jfrmMain,"���벻�Ϸ�������������18λ���֤��");
			jtxtTeacherId.setText("");
			jtxtTeacherId.getText();
		        ok = false;	 
		}
		else
		{
			String sid= jtxtTeacherId.getText();
			/*�ж����֤���Ƿ��ظ�*/
			if(isUniqueId(sid) == false)
			{
				showMessage(jfrmMain,"���֤�����벻���ظ������������룡");
				jtxtTeacherId.setText("");
				jtxtTeacherId.getText();
				ok = false;
			}		    
			else
			        strId = sid;
		}
	    }
	    	
		
            /*�ж���ϵ��ʽ�Ƿ�Ϸ�*/
	    pat = Pattern.compile(regEx[2]);   	
	    mat = pat.matcher(jtxtTeacherTel.getText()); 
	    res = mat.find();   
	    if(ok == true)/*���������֤�ž��Ϸ�*/
	    {
	    	if(res==false )
		{
			 showMessage(jfrmMain,"���벻�Ϸ�������������11λ�ֻ���");
			 jtxtTeacherTel.setText("");
			 jtxtTeacherTel.getText();
			 ok = false;	 
		}
	    	else
		         strTel = jtxtTeacherTel.getText();
	    }
	    
          /*ȫ���Ϸ������*/
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
			System.out.println("�����쳣�� ");
	     }	
             try      
	     {
		 	FStatement.disConnection();
	     } catch (Exception e) 
	     {
			System.out.println("�ر��쳣��");
	     }
        }    
    }
      
        /*******************************************************
	* Function Name: 	        getNumber 
	* Params :                      ��
	* Purpose: 			�õ���ʦ���
	* Return: 			String  	
	*******************************************************/

       public String getNumber()
       {
    	       String time = null;
    	       int    i ;
    	       String Ostr = null;      //�����ݿ�ȡ�����һ����¼�Ľ�ʦ���
    	       String str1 = null;      //����λ���
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
			System.out.println("�ر��쳣��");
		}
		
       	       /*�õ�ϵͳʱ��*/
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
			System.out.println("�ر��쳣��");
		}
	    return time;
       }
        
        /*******************************************************
	* Function Name: 	        dealAction 
	* Params :                      ��
	* Purpose: 			�¼�����
	* Return: 			void  	
	*******************************************************/

	private void dealAction()
	{
		
                /*"�˳�"��ť�ļ����¼�*/
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
		
                /*����ļ����¼�*/
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
 
		/*"���"��ť�ļ����¼�*/
		jbtnAdd.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					if(jbtnAdd.getText().equals("���"))
					{
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
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
					else//�����ȷ���� ������� �����ݿ⼰��ʦ�б�
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
						jbtnAdd.setText("���");
						jbtnModify.setText("�޸�");
						jtxtTeacherName.removeAll();
						setStatus(BROW);	
					}
				}				
			}
		);
		
                /*"�޸�"��ť�ļ����¼�*/
		jbtnModify.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					if(jbtnModify.getText().equals("�޸�"))
					{
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
						setStatus(EDIT);
						
						jtxtTeacherName.setEditable(false);
						jtxtTeacherId.setEditable(false);
						jtxtTeacherTel.setEditable(false);
						
						jlstCourse.setEnabled(false);//������޸ġ������ڿ�Ŀ�б�ӦΪ���ɸ���״̬
					
						lastAction = MODIFY;
					        jlblTeacherNum.getText();
						jtxtTeacherName.getText();
				                jtxtTeacherId.getText();
					        jtxtTeacherTel.getText();
						
					}
					else//��� ��������
					{
						jbtnAdd.setText("���");
						jbtnModify.setText("�޸�");
					        jrdbStatus[0].setSelected(true);
						initTeacherList(); 
							
						setStatus(BROW);
					}
				}			
			}	
		);

		/*��ʦ�б���Ƿ�ѡ�еļ����¼�*/
		jlstTeacherList.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0)
				{
					if(jlstTeacherList.getSelectedIndex()!=-1)
					{
						String selected = jlstTeacherList.getSelectedValue().toString();
						jlblTeacherNum.setText(selected.substring(0,0+8));//selected.substring(0,0+8)ѡ�еı��
						jtxtTeacherName.setText(selected.substring(9));
					        secondFind(); 
					
					        getStatus();
					}
				}		
			}
		);

		/*��ʦ�б�������������¼�*/
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

		/*�����б�������������¼�*/
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
	* Params :                      ��
	* Purpose: 			�����û�ѡ�еı�Ų�ѯ���ݿ�õ���ʦ״̬��Ϣ
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
			System.out.println("�ر��쳣��");
		}
	}

	/*******************************************************
	* Function Name: 	        updateData
	* Params :                      ��
	* Purpose: 			�����û�ѡ�е���Ϣ��ɸü�¼�����ݿ��е�״̬���޸�
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
			 System.out.println("�޸��쳣��");
		}		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			System.out.println("�ر��쳣��");
		}
	}

        /*******************************************************
	* Function Name: 	        initAll
	* Params :                      ��
	* Purpose: 			��ʼ������״̬�Ľ�ʦ��Ϣ
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
				
				jlblTeacherCount.setText("��"+dlmTeacherList.getSize()+"����ʦ");
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
			System.out.println("�ر��쳣��");
		}
       }
	
       /*******************************************************
	* Function Name: 	        CombineSelect
	* Params :                      ��
	* Purpose: 			�õ���ʦ�ڿα���Ϣ 
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
			System.out.println("�ر��쳣��");
		}
        }
	 
        /*******************************************************
	* Function Name: 	        initTeacherList
	* Params :                      ��
	* Purpose: 			��ʼ�����ν�ʦ��Ϣ
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
				
				jlblTeacherCount.setText("��"+dlmTeacherList.getSize()+"����ʦ");
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
			System.out.println("�ر��쳣��");
		}
        }

	/*******************************************************
	* Function Name: 	        secondFind
	* Params :                      ��
	* Purpose: 			����ѡ�еĽ�ʦ��Ų�ѯ�����֤�ź���ϵ��ʽ
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
			System.out.println("�ر��쳣��");
		}
		
	}
	
	/*******************************************************
	* Function Name: 	        showMessage
	* Purpose: 			��ʾ�����Ի�����Ϣ
	* Params : 
	* @JFrame jfrm	                ���洰��ʵ��
	* @String mess		        ����ΪString��ʵ��
	* Return: 			void   	
	*******************************************************/

	private void showMessage(JFrame jfrm,String mess)
	{
		JOptionPane.showMessageDialog(jfrm, mess, "΢������ܰ��ʾ", JOptionPane.DEFAULT_OPTION);
		
	}

	/*******************************************************
	* Function Name: 	        initFrameMain
	* Params :                      ��
	* Purpose: 			��ʼ����ʦ��Ϣ����
	* Return: 			void   	
	*******************************************************/

	public void initFrameMain()
	{
		Dimension  dim;
		Font NormalFont = new Font("����",Font.PLAIN,16); 
		
		jfrmMain = new JFrame(Topic);
		con = jfrmMain.getContentPane();
		con.setLayout(null);
		jfrmMain.setSize(7605/15, 7470/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2, (dim.height - jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel (Topic);
		jlblTopic.setFont(new Font("΢���ź�",Font.PLAIN,24));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(2197/15, 0, 3090/15, 495/15);
		con.add(jlblTopic);
		
		jlblTeacherInfo = new JLabel("��ʦ��Ϣ");
		jlblTeacherInfo.setFont(NormalFont);
		jlblTeacherInfo.setBounds(4920/15, 840/15, 975/15, 255/15);
		con.add(jlblTeacherInfo);
		
		
		jlblTeacherNumber = new JLabel("��ʦ���");
		jlblTeacherNumber.setFont(NormalFont);
		jlblTeacherNumber.setBounds(3600/15, 1500/15, 975/15, 255/15);
		con.add(jlblTeacherNumber);
		
		jlblTeacherNum = new JLabel();
		jlblTeacherNum.setFont(NormalFont);
		jlblTeacherNum.setBounds(4680/15, 1440/15, 2655/15, 375/15);
		con.add(jlblTeacherNum);
		
		jlblTeacherName = new JLabel("��ʦ����");
		jlblTeacherName.setFont(NormalFont);
		jlblTeacherName.setBounds(3600/15, 2120/15, 975/15, 255/15);
		con.add(jlblTeacherName);
		
		jtxtTeacherName = new JTextField();
		jtxtTeacherName.setFont(NormalFont);
		jtxtTeacherName.setBounds(4680/15, 2060/15, 2655/15, 375/15);
		con.add(jtxtTeacherName);
		
		jlblTeacherId = new JLabel("���֤��");
		jlblTeacherId.setFont(NormalFont);
		jlblTeacherId.setBounds(3600/15, 2740/15, 975/15, 255/15);
		con.add(jlblTeacherId);
		
		jtxtTeacherId= new JTextField();
		jtxtTeacherId.setFont(NormalFont);
		jtxtTeacherId.setBounds(4680/15, 2680/15, 2655/15, 375/15);
		con.add(jtxtTeacherId);
		
		jlblTeacherTel = new JLabel("��ϵ��ʽ");
		jlblTeacherTel.setFont(NormalFont);
		jlblTeacherTel.setBounds(3600/15, 3360/15, 975/15, 255/15);
		con.add(jlblTeacherTel);
		
		jtxtTeacherTel= new JTextField();
		jtxtTeacherTel.setFont(NormalFont);
		jtxtTeacherTel.setBounds(4680/15, 3300/15, 2655/15, 375/15);
		con.add(jtxtTeacherTel);
		
		jlblCourse = new JLabel("���ڿ�Ŀ");
		jlblCourse.setFont(NormalFont);
		jlblCourse.setBounds(4980/15, 3840/15, 975/15, 255/15);
		con.add(jlblCourse);
		
		dlmCourse = new DefaultListModel();
		jlstCourse = new JList(dlmCourse);
		jscpCourse = new JScrollPane(jlstCourse);
		jlstCourse.setFont(NormalFont);
		jscpCourse.setBounds(3600/15, 4320/15, 3735/15, 1260/15);
		con.add(jscpCourse);
		
		jlblStatus = new JLabel("״    ̬");
		jlblStatus.setFont(NormalFont);
		jlblStatus.setBounds(3600/15, 5760/15, 975/15, 255/15);
		con.add(jlblStatus);
		
		bgStatus = new ButtonGroup();
		jrdbStatus = new JRadioButton[2];
		
		jrdbStatus[0] = new JRadioButton("����");
		jrdbStatus[0].setFont(NormalFont);
		jrdbStatus[0].setBounds(4800/15, 5640/15, 976/15, 495/15);
		bgStatus.add(jrdbStatus[0]);
		con.add(jrdbStatus[0]);
		
		jrdbStatus[1] = new JRadioButton("������");
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
		jscpTeacherList.setBorder(BorderFactory.createTitledBorder("��ʦ�б�"));
		con.add(jscpTeacherList);
		
		jlblTeacherCount = new JLabel("��0����ʦ");
		jlblTeacherCount.setFont(NormalFont);
		jlblTeacherCount.setBounds(120/15, 5880/15, 3135/15, 375/15);
		con.add(jlblTeacherCount);
		
		jbtnAdd = new JButton("���");
		jbtnAdd.setFont(NormalFont);
		jbtnAdd.setBounds(120/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnAdd);
		
		jbtnModify = new JButton("�޸�");
		jbtnModify.setFont(NormalFont);
		jbtnModify.setBounds(1320/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnModify);
		
		jbtnExit = new JButton("�˳�");
		jbtnExit.setFont(NormalFont);
		jbtnExit.setBounds(6000/15, 6360/15, 1215/15, 495/15);
		con.add(jbtnExit);
		
		
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrmMain.setVisible(true);
	}

	/*******************************************************
	* Function Name: 	        main 
	* Purpose: 			������
	* Return: 			void   	
	*******************************************************/

	public static void main(String[] args) 
	{
		new TeacherManagement();	
	}
}
