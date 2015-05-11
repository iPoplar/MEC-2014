
/*******************************************************

* Function List:
*		MachineRome3()						���췽��
*		initFrame();						��������		
*		dealAction();						�¼�����
*		setstatus(BROWS);					��ʼ������
*		initSubjectList();					��ʼ�������б���Ϣ
*		showSubject(jlblNum.getText())		��ʼ������������Ϣ	
*   	initComboBox();						��ʼ������״̬�����б�
*       initcombox();						��ʼ���ֵ������б�
*		math()								����ֵ��µĻ�������
*		zhengze()							�жϻ����������볤�ȣ�����true��false


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
	//�Ӵ���
	private JInternalFrame jfrmMain;
	private JFrame jfrm;
	private JDesktopPane jdtp;
	private Container con;
	//����
	private JLabel jlblTopic;
	
	//�����б�
	private DefaultListModel dlmPractice;
	private JList jlstPractice;
	private JScrollPane jscPractice;
	
	//�ֵ�
	private JLabel jlblPoint;
	private JComboBox jtxtPoint;
	
	//����ѡ��
	private JComboBox jcoPracticeroom;
	
	//��������
	private JLabel jlblNumber;
	
	//�������
	private JLabel jlblId;
	private JLabel jlblNum;
	
	//��������
	private JLabel jlblName;
	private JTextField jtxtName;
	
	//������Ŀ
	private JLabel jlblCount;
	private JTextField jtxtCount;
	private JLabel jlblTai;
	
	//����״̬
	private JLabel jlblStatus;
	private JRadioButton[] jraStatus;
	private ButtonGroup bgStatus;
				
	//���
	private JButton jbtnAdd;
	
	//�޸�
	private JButton jbtnModify;
	
	//�˳�
	private JButton jbtnExit;
	
	/*��ʾ�ֵ��µĻ�������*/
	private String Idd;
	
	/*���һ�β��� ��������ӵ�ȷ�������޸ĵ�ȷ��*/
	private int lastAction;
	private static final int MODIFY=1;
	private static final int addition=0;
	
	/*ȫ�ֱ��� ����ͱ༭״̬*/
	private static final int BROWS = 0;
	private static final int EDIT = 1;
	
	
	public Practiceroom(DesktopPane jdtp, JFrame jfrm)  //���췽��
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
	
	private void showSubject(String Ud)  //��ʼ��������Ϣ
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
		jcoPracticeroom.addItem("���л���");
		jcoPracticeroom.addItem("���û���");
		//jcoPracticeroom.setSelectedIndex(0);
		jcoPracticeroom.setEditable(false);
    }
	
	private void initcombox()  //��ʼ���ֵ������б�
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
			System.out.println("�ر��쳣��");
		}	
	}
 
	/*******************************************************
	* Function Name: 	math()
	* Purpose: 			ȡ���Էֵ������б�ѡ����ͷ�Ļ�������
	* Params : 
	*	@point			string		ȡ�ֵ������б�ǰ��λ ���ֵ��
	* Return: 			string 	�����Ը÷ֵ㿪ͷ�Ļ�������
	*******************************************************/

	private  void math()
	{
		String StringSQL;
		String point;
		point=((String) jtxtPoint.getSelectedItem()).substring(0, 2);//ȡ�ֵ������б�ǰ��λ���ֵ���
		
		
		StringSQL="select count(*) from SYS_MEC_PRACTICEROOM where SUBSTR(practiceroomId,1,2)='"+point+"'";//�����Ը÷ֵ㿪ͷ�Ļ����ĸ���
		
	    
		
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
			System.out.println("�ر��쳣��");
		}	
	}
			
	private boolean zhengze()  //�ж����������Ƿ����
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
		
	
	
	private void initSubjectList()  //��ʼ���б���Ϣ
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

				jlblNumber.setText("��"+i+"����");
		        jlstPractice.setSelectedIndex(0);
		    } catch (SQLException e) 
		      {
		    		
		    		e.printStackTrace();
		      }
		    try {
				    FStatement.disConnection();
			    } catch (Exception e) 
	              {  
			    	System.out.println("�ر��쳣��");
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
	
	
	private void setstatus(int Status)  //����״̬
	{
		boolean status = Status == BROWS;
		jcoPracticeroom.setEnabled(status);
		jscPractice.setEnabled(!status);
		
		jtxtPoint.setEnabled(!status);//���
		jtxtName.setEnabled(!status);
		jtxtCount.setEnabled(!status);
		jraStatus[0].setEnabled(!status);
		jraStatus[1].setEnabled(!status);
			
	}
	
	
	private void dealAction() 
	{
		//�˳�����
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
		
		jfrmMain.addInternalFrameListener						//����JInternalFrame���˳��¼���
		(
				new InternalFrameListener()
				{
					public void internalFrameClosing(InternalFrameEvent arg0) 
					{
						demoMenu1.removeClassNameFromArrayList(this.getClass());//��Ҫ����demoMenu�� �ķ�����
					}
					public void internalFrameActivated(InternalFrameEvent arg0) {}
					public void internalFrameClosed(InternalFrameEvent arg0){}
					public void internalFrameDeactivated(InternalFrameEvent arg0) {}
					public void internalFrameDeiconified(InternalFrameEvent arg0) {}
					public void internalFrameIconified(InternalFrameEvent arg0) {}
					public void internalFrameOpened(InternalFrameEvent arg0) {}	
				}
		);
		
		//�޸ļ���
		jbtnModify.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{						
						if(jbtnModify.getText().equals("�޸�"))
						{
							jbtnAdd.setText("ȷ��");
							jbtnModify.setText("ȡ��");
							setstatus(EDIT);
							
							jtxtName.setEditable(false);
							jtxtCount.setEditable(false);
							lastAction = MODIFY;				
						}
						else
						{
							jbtnAdd.setText("���");
							jbtnModify.setText("�޸�");
							
							setstatus(BROWS);
							
							
						}
						
					}										
				}
						
		);
		
		//��Ӽ���
		jbtnAdd.addActionListener
		(
				
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						
						if(jbtnAdd.getText().equals("���"))
						{
							
							jbtnAdd.setText("ȷ��");
							jbtnModify.setText("ȡ��");
							setstatus(EDIT);
							jraStatus[0].setEnabled(false);
							jraStatus[1].setEnabled(false);
							
							lastAction = addition;				
						}
						else
						{
							if(lastAction == addition) //���һ�β���Ϊ��ӵ�ȷ��
							{
								// TODO �����Ӳ�����ָ������⣡
								
								if(zhengze())
								{
									String str;
									String strName;
							
									String SQLString ;
								
										String id;
								
							
								
										strName = jtxtName.getText();
										str=jtxtCount.getText();
							
										String s = ((Integer.parseInt(Idd) + 101) + "").substring(1,3);//��Iddת��Ϊ���֣���101��ӣ�ת��Ϊ�ַ�����ȡ����λ��������Զ�����ĺ���λ��ʵ�֡�
								
								
									
								
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
											System.out.println("�ر��쳣��");
										}	
								//dlmPractice.addElement(strName)
										
								}	
								else
								{
						
									JOptionPane.showMessageDialog(jfrmMain, "������ݲ��Ϸ�", "΢������ܰ��ʾ", JOptionPane.DEFAULT_OPTION);
								}
								 
							}
							else
							{
								// TODO ����޸Ĳ�����ָ�������ݿ����ݣ�
								 
								
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
									System.out.println("�ر��쳣��");
								}	
								
							}
							
							jbtnAdd.setText("���");
							jbtnModify.setText("�޸�");
						
							setstatus(BROWS);
							
						}
						
					}
					
				}
				
		);
		
		//���û��������л��������б����
		jcoPracticeroom.addItemListener
		(
				new ItemListener()
				{					
					public void itemStateChanged(ItemEvent arg0) 
					{
						if(jcoPracticeroom.isEnabled())
							
						{
							if(jcoPracticeroom.getSelectedItem().equals("���л���"))
						
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
								jlblNumber.setText("��"+i+"����");
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
								System.out.println("�ر��쳣��");
							}	
						}
					 }
					
				}
				}
		);
		
		//�ֵ������б����
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
		
		//�б���� ���ú���showSubject
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
		
		
		//��������
		private void initFrame()
		{
			Dimension dim;
			Font normalFont = new Font("����", Font.PLAIN, 16);
			
			jfrmMain=new JInternalFrame("��������",false, true, false, true);
			con=jfrmMain.getContentPane();
			con.setLayout(null);
			jfrmMain.setSize(6975/15, 5205/15);
			
			dim=Toolkit.getDefaultToolkit().getScreenSize();
			jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2, (dim.height - jfrmMain.getHeight())/2);
			
			//����
			jlblTopic = new JLabel("��������");
			jlblTopic.setFont(new Font("΢���ź�", Font.PLAIN, 24));
			jlblTopic.setForeground(Color.blue);
			jlblTopic.setBounds(2520/15, 0, 1815/15, 615/15);
			con.add(jlblTopic);
			
			//ѡ����á�ȫ�����������б�
			jcoPracticeroom=new JComboBox();
			jcoPracticeroom.setFont(new Font("����", Font.PLAIN, 12));
			jcoPracticeroom.setBounds(120/15, 600/15, 2895/15, 360/15);	
			con.add(jcoPracticeroom);
			
			//�����б�
			dlmPractice = new DefaultListModel();
			jlstPractice = new JList(dlmPractice);
			jscPractice = new JScrollPane(jlstPractice);
			jlstPractice.setFont(normalFont);
			jscPractice.setBounds(120/15, 960/15, 2895/15, 2775/15);
			jscPractice.setBorder(BorderFactory.createTitledBorder("������Ϣ"));
			con.add(jscPractice);
			
			//�ֵ�
			jlblPoint=new JLabel("�ֵ�");
			jlblPoint.setFont(normalFont);
			jlblPoint.setBounds(3240/15, 960/15, 960/15, 240/15);
			con.add(jlblPoint);
			
			//�ֵ������б�
			jtxtPoint=new JComboBox();
			jtxtPoint.setFont(normalFont);
			jtxtPoint.setBounds(4200/15, 900/15, 2295/15, 375/15);
			con.add(jtxtPoint);
			
			//�������
			jlblId=new JLabel("�������");
			jlblId.setFont(normalFont);
			jlblId.setBounds(3240/15, 1590/15, 960/15, 240/15);
			con.add(jlblId);
			
			jlblNum=new JLabel("1234");
			jlblNum.setFont(normalFont);
			jlblNum.setBounds(4200/15, 1530/15, 2295/15,375/15);
			con.add(jlblNum);
			
			//��������
			jlblName=new JLabel("��������");
			jlblName.setFont(normalFont);
			jlblName.setBounds(3240/15, 2220/15, 960/15, 240/15);
			con.add(jlblName);
			
			jtxtName=new JTextField();
			jtxtName.setFont(normalFont);
			jtxtName.setBounds(4200/15, 2160/15, 2295/15,375/15);
			con.add(jtxtName);
			
			//��������
			jlblCount=new JLabel("��������");
			jlblCount.setFont(normalFont);
			jlblCount.setBounds(3240/15, 2850/15, 960/15, 240/15);
			con.add(jlblCount);
			
			jlblTai=new JLabel("̨");
			jlblTai.setFont(normalFont);
			jlblTai.setBounds(6240/15, 2880/15, 240/15, 240/15);
			con.add(jlblTai);
			
			jtxtCount=new JTextField();
			jtxtCount.setFont(normalFont);
			jtxtCount.setBounds(4200/15, 2795/15, 1935/15,375/15);
			con.add(jtxtCount);
			
			//״̬
			jlblStatus=new JLabel("״̬");
			jlblStatus.setFont(normalFont);
			jlblStatus.setBounds(3240/15, 3480/15, 960/15, 240/15);
			con.add(jlblStatus);
			
			bgStatus = new ButtonGroup();
			jraStatus = new JRadioButton[2];
			
		    jraStatus[0] = new JRadioButton("����");
			jraStatus[0].setFont(normalFont);
			jraStatus[0].setBounds(4200/15, 3480/15, 975/15, 255/15);
			bgStatus.add(jraStatus[0]);
			con.add(jraStatus[0]);
			
			jraStatus[1] = new JRadioButton("ͣ��");
			jraStatus[1].setFont(normalFont);
			jraStatus[1].setBounds(5520/15, 3480/15, 975/15, 255/15);
			bgStatus.add(jraStatus[1]);
			con.add(jraStatus[1]);
			
			//������Ŀ
			jlblNumber=new JLabel("������Ŀ");
			jlblNumber.setFont(normalFont);
			jlblNumber.setBounds(120/15, 3840/15, 2895/15, 255/15);
			con.add(jlblNumber);

			//���
			jbtnAdd = new JButton("���");
			jbtnAdd.setFont(normalFont);
			jbtnAdd.setBounds(120/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnAdd);
			
			//�޸�
			jbtnModify = new JButton("�޸�");
			jbtnModify.setFont(normalFont);
			jbtnModify.setBounds(1320/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnModify);
			
			//�˳�
			jbtnExit = new JButton("�˳�");
			jbtnExit.setFont(normalFont);
			jbtnExit.setBounds(5400/15, 4080/15, 1215/15, 465/15);
			con.add(jbtnExit);
		
			//jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jfrmMain.setVisible(true);
			
			jdtp.add(jfrmMain);
			
		}
		
		

}
