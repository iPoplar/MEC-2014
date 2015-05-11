
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
		
		private JLabel jlblTopic;/*����*/
		
		private JLabel jlblEmployee;/*Ա�����*/
		private JLabel jlblEmployeeNo;
		
		private JLabel jlblEmployeeName;/*Ա������*/
		private JTextField jtxtEmployeeName;
		
		private JLabel jlblEmployeePassword;/*Ա������*/
		private JPasswordField jpwEmployeePassword;
		
		private JLabel jlblEmployeeStatus;/*״̬��ǩ*/
		private JRadioButton[] jrdbEmployeeStatus;/*���õ�ѡ��ť*/
		private ButtonGroup bgStatus;/*���ð�ťȺ*/
		
		private JLabel jlblEmployeeId;/*���֤����*/
		private JTextField jtxtEmployeeId;
		
		private JLabel jlblEmployeeTel;
		private JTextField jtxtEmployeeTel;/*��ϵ��ʽ*/
		
		private JLabel jlblEmployeeNationNo;/*����*/
		private JComboBox jcmbEmployeeNationNo;/*���������б��*/
		
		private JLabel jlblEmployeeNativeNo;/*����*/
		private JComboBox jcmbEmployeeNativeNo;
		
		private JLabel jlblEmployeeJobNo;	/*ְ��*/
		private JComboBox jcmbEmployeeJobNo;
		
		private JComboBox jcmbEmployeeStatus;
		private DefaultListModel dlmEmployeeList;/*Ա��ѡ���б��*/
		private JList jlstEmployeeList;
		private JScrollPane jscpEmployeeList;/*������*/
		private JLabel jlblEmployeeAccount;/*����ͳ��*/
		
		private JButton jbtnExit;/*�˳���ť*/
		private JButton jbtnAdd;/*��Ӱ�ť*/
		private JButton jbtnModify;/*�޸İ�ť*/		
		
		private int i = 0;/*����i��������б�ļ���*/
		private int lastAction;/*������¼���һ�β���*/		
		
		private static final int ADDATION = 1;/*���״̬����*/
		private static final int MODIFY = 2;/*�޸�״̬����*/	
		
		private final int STATUS_BROWS = 0;		 /*״̬�������*/
		private final int STATUS_EDIT = 1;		 /*״̬�����༭*/

		public employeeOl ()
		{
			initFrame();
			reinitFrame();
			dealAction();	
		}
		
		/*******************************************************
		* Function Name: 	reinitFrame
		* Purpose: 			�ٴγ�ʼ��Ա����Ϣ
		
		*******************************************************/
		private void reinitFrame() 
		{
			employeeJCMBInforametionInit();//����״̬�б��ʼ��
			employeeListInit(0);//Ĭ����ְԱ����Ϣ�б��ʼ��
			employeeSomeInforametionInit();//���塢�����ʼ��
		}
		
		/*******************************************************
		* Function Name: 	setWorkStatus
		* Purpose: 			���ò��������״̬
		* ���ݵĲ�������int��status��������������ͱ༭����״̬
		********************************************************/
		private void setWorkStatus(int status)
		{
			boolean value = status == STATUS_BROWS;		// ����value������������Ҫ���ĵ�״̬�������ҪΪ	���״̬��valueΪTRUE�����ɼ���
														// �����ҪΪ�༭״̬��valueΪ	FALSE���ɼ���
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
		* Purpose: 			����״̬�б��ʼ��
		
		*******************************************************/
		private void employeeJCMBInforametionInit()//����״̬�б��ʼ��
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
				System.out.println(" ����״̬�б��ʼ������");
			}
		}
			
		/*******************************************************
		* Function Name: 	employeeListInit
		* Purpose: 			Ա����Ϣ�б��ʼ��
		* ���ݵĲ�������int��Status ��������ʼ����ͬ״̬�µ��б�
		*******************************************************/
		private void employeeListInit(int Status)//Ա����Ϣ�б��ʼ��
		{
			String OkStatus = Status + ""; 
			try
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				String SQLString = "SELECT EMPLOYEENO, EMPLOYEENAME FROM believe WHERE EMPLOYEESTATUS='"+ OkStatus +"'";
				 FResultSet rs = FStatement.executeQueryCon(SQLString);
				System.out.println("Ա����ʼ��" + SQLString);
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
				
				/*��ǰ�б�������ͳ��*/
				int employeeCount = dlmEmployeeList.getSize();
				if(employeeCount > 0)
					jlstEmployeeList.setSelectedIndex(0);
				employeeInforametionInit(jlstEmployeeList.getSelectedValue().toString().substring(0, 0+8));
				jlblEmployeeAccount.setText("��" + employeeCount + "��ѧ��");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try 
			{
				FStatement.disConnection();
			} catch (Exception e)
			{
				System.out.println("Ա����Ϣ�б��ʼ������");
				e.printStackTrace();
			}
		}
		
		/*******************************************************
		* Function Name: 	employeeInforametionInit
		* Purpose: 			��ʼ��Ա��������Ϣ
		* ���ݵĲ����� ��String��link������ѡ�е�Ա������Ϣ
		*******************************************************/
		private void employeeInforametionInit(String link)//��ʼ��Ա��������Ϣ����
		{
			jlstEmployeeList.removeAll();
			String SQLString = "SELECT * FROM believe WHERE EMPLOYEENo ='"+link+"'";
			System.out.println(SQLString);
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				String No;				//Ա����š����������֤�š���ϵ��ʽ�����롢
				String Name;			//���塢���ᡢְ��ı��
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
					selectChange(ENation, ENative, EJob);//ѡ�еļ��ᡢ���塢ְ�����
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
				System.out.println("��ʼ��Ա��������Ϣ������");
			}
		}
		
		/*******************************************************
		* Function Name: 	employeeSomeInforametionInit
		* Purpose: 			���塢���ᡢְ���ʼ��
		*******************************************************/
		private void employeeSomeInforametionInit()//���塢���ᡢְ���ʼ��
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
				MECTool.showMessage(jfrmMain,"���塢���ᡢְ�����");
			}
		}
		
		/******************************************************
		* Function Name: 	selectChange
		* Purpose: 			���ѡ�еļ��ᡢ���塢ְ��
		* ���ݵĲ�����			��String��NationNo,NativeNo,JobNo��
		* 					�����ҳ�ѡ�е�Ա���ļ��ᡢ���塢ְ����
		*******************************************************/
		private void selectChange( String NationNo, String NativeNo, String JobNo)//ѡ�еļ��ᡢ���塢ְ�����
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
				System.out.println("ѡ�еļ��ᡢ���塢ְ����ӣ�");
			}		
		}
		
		/*******************************************************************
		* Function Name: 	CheckNative
		* Purpose: 			��������֤�ϵ������Ƿ�һ�µ���֤
		* ������				String ID ��sys_mec_native�������ڲ���ѡ�еļ���ļ�����
		* ����ֵ��				NativeName����ѡ�м���������Ӧ�����ڵ�����
		********************************************************************/
		private String CheckNative(String ID)//��������֤�ϵ������Ƿ�һ�µ���֤
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
		* Purpose: 			����Ա����ţ� ͳ�Ʊ��е�ǰ������ӵ�����
		* 
		* ����ֵ��				K ��¼������ӵĴ���
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
		* Purpose: 			����������Ϊ�գ���"ȷ��"��ť������
		**************************************************************/
		private void setAddButtonStatus()	
		{
			if(jtxtEmployeeName.getText().length() > 0)	// ��������������0ʱ
				jbtnAdd.setEnabled(true);		// ���/ȷ����ťΪ�ɼ�     / enabledΪ����
			else								// ����
				jbtnAdd.setEnabled(false);		// ���/ȷ����ťΪb���ɼ�
		}
		
		/*************************************************************
		* Function Name: 	CheckIdentityCardNative
		* Purpose: 			���֤�м������֤
		* ������				String id ѡ�е�Ա�����
		* ����ֵ��				boolean OK ��֤���
		**************************************************************/
		private boolean  CheckIdentityCardNative(String id)
		{
		     //�������֤
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
		* Purpose: 			���֤�г������ڵ���֤
		* ������				String id ѡ�е�Ա�����
		* ����ֵ��				boolean OK ��֤���
		**************************************************************/
		private boolean  CheckIdentityCardYMD(String id)
		{
		
			//�����յ���֤
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
		* Purpose: 			���֤��У��λ����֤
		* ������				String id ѡ�е�Ա�����
		* ����ֵ��				boolean OK ��֤���
		**************************************************************/
		private boolean  CheckIdentityCardLast(String id)
		{
			//���֤У��λ����֤
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
		* Purpose: 			�¼�����		
		*******************************************************/
		private void dealAction()
		{									/*�˳��Ķ�������*/
			jbtnExit.addActionListener			/* ���������������˳�ʱ��ִ�еķ�����������˳������˳�ʱ���ʺ�*/
			(
				new ActionListener()			
				{
					public void actionPerformed(ActionEvent arg0)	 
					{
						MECTool.showMessage(jfrmMain, "�ټ�");		// �ڴ˵���MEC�������е�showMessage��̬������ʵ�ֹر�ǰ����ʾ��䡣
						jfrmMain.dispose();		
					}				
				}
			);

			/*���õ���¼��������Ա����Ϣʱ������Ա����Ϣ��ʾ*/
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
							
							employeeInforametionInit(link);//����Ա����Ϣ�б��ʼ��							
						}
					}
				}
				);
			
			/*��ѡ��״̬�����б�ļ���*/
			jcmbEmployeeStatus.addActionListener
			(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0) 
						{
							int Status ;
							Status = jcmbEmployeeStatus.getSelectedIndex();
					
							//���ݱ�ѡ�е�ֵ����ʼ��Jlist
							dlmEmployeeList.removeAllElements();
							employeeListInit(Status);
						}
					}
			);
			
			/*  ���"�޸�"��ť����"���"��Ϊ"ȷ��"����"�޸�"��Ϊ"����"��*/
			jbtnModify.addActionListener
			(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						if(jbtnModify.getText().equals("�޸�"))	// ��jbtnAdd��ťΪ�޸�ʱִ��
						{		
							jbtnAdd.setText("ȷ��");				// "���"��Ϊ"ȷ��"
							jbtnModify.setText("����");			// "�޸�"��Ϊ"����"
							
							setWorkStatus(STATUS_EDIT);			// ��Ϊ�༭״̬
							setAddButtonStatus();				// ���ð�ť�Ƿ�ɼ����ڱ任���༭״̬֮ǰ��
							jtxtEmployeeName.selectAll();		// ���ı����е�����ѡ��
							jtxtEmployeeName.requestFocus();	// ���󽹵㵽���������ı���
							lastAction = MODIFY;				//��¼���һ�β�����"�޸�"
							
							employeeListInit(0);
						}
						else									// ��jbtnAdd��ťΪ���޸�ʱִ��
						{
							employeeListInit(0);
							jbtnAdd.setText("���");				// ȷ����Ϊ���
							jbtnModify.setText("�޸�");			// ������Ϊ�޸�
							jbtnAdd.setEnabled(true);			// ����Ӱ�ť��������ڴ����ʱ���������ı���Ϊ��ʱ�����ܵ����Ӱ�ť����Ϊ��Ӻ�ȷ����ͬһ����ť���ڲ�ͬ״̬�µı���
							//employeeListInit(0);				//Ա����Ϣ�б��ʼ��
							jlstEmployeeList.removeAll();
							setWorkStatus(STATUS_BROWS);		// �ı�״̬Ϊ���
						}
					}
				}
			);
						
				/* ���"���"��ť������Ӹ�Ϊ"ȷ��"����"�޸�"��Ϊ"����"��*/
				jbtnAdd.addActionListener
				(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e) 
						{
							if(jbtnAdd.getText().equals("���"))		// ��jbtnAdd��ťΪ"���"ʱִ��
							{
								jbtnAdd.setText("ȷ��");				// "���"��Ϊ"ȷ��"
								jbtnModify.setText("����");			// "�޸�"��Ϊ"����"
								
								setWorkStatus(STATUS_EDIT);			// �ı�״̬Ϊ�༭
								setAddButtonStatus();				// ���ð�ť�Ƿ�ɼ����ڱ任���༭״̬֮ǰ��
								jtxtEmployeeName.selectAll();		// ���ı����е�����ѡ��
								jtxtEmployeeName.requestFocus();	// ���󽹵㵽���������ı���
								jtxtEmployeeName.setText("");		//��������ı���
								jtxtEmployeeTel.setText("");		//�����ϵ��ʽ�ı���
								jpwEmployeePassword.setText("");	//��������ı���
								jtxtEmployeeId.setText("");			//������֤���ı���		
								jlblEmployeeNo.setText("");
								
								lastAction = ADDATION;
							}
							else									// ��jbtnAdd��ťΪ"ȷ��"ʱִ��
							{
								jbtnAdd.setText("���");				// "ȷ��"��Ϊ"���"
								jbtnModify.setText("�޸�");
								setWorkStatus(STATUS_BROWS);		// �ı�״̬Ϊ���
								
								if(lastAction == ADDATION)
								{	
									boolean m , n, p, q;
									
									//�����������ж�
									m = Pattern.matches("[a-zA-Z\u4e00-\u9fa5]{2,10}" ,jtxtEmployeeName.getText() );
									//����������ж�
									n = Pattern.matches("[a-zA-Z0-9]{6,12}" , String.valueOf(jpwEmployeePassword.getPassword()));
									//���֤λ���������ж�
									p =Pattern.matches( "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$" ,jtxtEmployeeId.getText());
									//�ֻ�����������ж�
									q = Pattern.matches("^1[3|5|8|][0-9]{9}$",jtxtEmployeeTel.getText());
								
									if(m && n && p && q )
									{
										String sdf = new SimpleDateFormat("yyMMdd").format(new java.util.Date());
									
										int  y  = GetMaxCount() + 101;		//��ȡԱ�����
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
											
										/*���ݵ����*/
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
											{	//����ѡ�����ݻ����������Ӧ�ı�ţ����塢���ᡢְ��
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
												MECTool.showMessage(jfrmMain, a.getMessage() + "��������ݿ����Աǰ����������⡣" +
												"\n�Դ�΢����Ƽ����Ǹ�⣡");
												a.printStackTrace();
											}
											try 
											{
												FStatement.disConnection();
											} catch (Exception a)
											{
												MECTool.showMessage(jfrmMain, "���ݿ�ر��쳣");
												a.printStackTrace();
											}
										}
										else
										{
											MECTool.showMessage(jfrmMain, "���֤���������󣬻������֤�뼮����Ϣ���������֤����������");
										}
									}
									else
									{
										MECTool.showMessage(jfrmMain, "������������������֤��λ�����ߵ绰�������벻����Ҫ�����֤����������");
									}
										employeeListInit(0);
								}
								
								else  //lastAction == MODIFY
								{
									//  ����޸Ĳ�����ָ������⣡
										
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
										{	//����ѡ�����ݻ����������Ӧ�ı�ţ����塢���ᡢְ��
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
											MECTool.showMessage(jfrmMain, a.getMessage() + "��������ݿ����Աǰ����������⡣" +
											"\n�Դ�΢����Ƽ����Ǹ�⣡");
											a.printStackTrace();
										}
										try 
										{
											FStatement.disConnection();
										} catch (Exception a)
										{
											MECTool.showMessage(jfrmMain, "���ݿ�ر��쳣");
											a.printStackTrace();
										}
									}
									else
									{
										MECTool.showMessage(jfrmMain, "���֤���������󣬻������֤�뼮����Ϣ���������֤����������");
									}
								}
								
							}
						}
					}
				);
		}
			
		
		
		/*************************************************************
		* Function Name: 	initFrame
		* Purpose: 			����ĳ�ʼ��
		**************************************************************/
		private void initFrame()
			{

				Dimension dim;// �ߴ����Ի�ȡĬ����Ļ��С
				
				Font normalFont, StatusFont;
				
				normalFont = new Font("΢���ź�",Font.PLAIN,16);
				StatusFont = new Font("΢���ź�",Font.PLAIN,13); 
				
				jfrmMain = new JFrame("Ա����Ϣ����");
				con = jfrmMain.getContentPane();
				con.setLayout(null);	//���ò���
				
				jfrmMain.setSize(7500/15,6405/15);
				dim = Toolkit.getDefaultToolkit().getScreenSize();// ��ȡ��Ļ�ߴ�
				jfrmMain.setLocation((dim.width-jfrmMain.getWidth())/2,(dim.height-jfrmMain.getHeight())/2);// ���ÿ��λ��
				jfrmMain.setVisible(true);
				jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// �����˳�Ĭ��
				
				jlblTopic = new JLabel("Ա����Ϣ����");
				jlblTopic.setFont(new Font("΢���ź�",Font.PLAIN,24));//���ñ�������
				jlblTopic.setForeground(Color.BLUE);
				jlblTopic .setBounds(2782/15, 0, 2160/15, 465/15);
				con.add(jlblTopic);
				
				jlblEmployee = new JLabel("Ա�����");
				jlblEmployee.setFont(normalFont);
				jlblEmployee.setBounds(3600/15, 765/15, 960/15, 240/15);
				con.add(jlblEmployee);
				
				jlblEmployeeNo = new JLabel("");
				jlblEmployeeNo.setFont(StatusFont);
				jlblEmployeeNo.setBounds(4680/15, 765/15, 960/15, 240/15);
				con.add(jlblEmployeeNo);
				
				jlblEmployeeName = new JLabel("��      ��");
				jlblEmployeeName.setFont(normalFont);
				jlblEmployeeName.setBounds(3600/15, 1200/15, 960/15, 240/15);
				con.add(jlblEmployeeName);

				jtxtEmployeeName = new JTextField();
				jtxtEmployeeName.setFont(StatusFont);
				jtxtEmployeeName.setBounds(4680/15, 1133/15, 2415/15, 375/15);
				con.add(jtxtEmployeeName);

				jlblEmployeePassword = new JLabel("��      ��");
				jlblEmployeePassword.setFont(normalFont);
				jlblEmployeePassword.setBounds(3600/15, 1695/15, 960/15, 240/15);
				con.add(jlblEmployeePassword);

				jpwEmployeePassword = new JPasswordField();
				jpwEmployeePassword.setFont(StatusFont);
				jpwEmployeePassword.setBounds(4680/15, 1628/15, 2415/15, 375/15);
				con.add(jpwEmployeePassword);

				jlblEmployeeNationNo = new JLabel("��      ��");
				jlblEmployeeNationNo.setFont(normalFont);
				jlblEmployeeNationNo.setBounds(3600/15, 2220/15, 960/15, 240/15);
				con.add(jlblEmployeeNationNo);

				jcmbEmployeeNationNo = new JComboBox();
				jcmbEmployeeNationNo.setFont(StatusFont);
				jcmbEmployeeNationNo.setBounds(4680/15, 2160/15, 2415/15, 365/15);
				con.add(jcmbEmployeeNationNo);


				jlblEmployeeNativeNo = new JLabel("��      ��");
				jlblEmployeeNativeNo.setFont(normalFont);
				jlblEmployeeNativeNo.setBounds(3600/15, 2700/15, 960/15, 240/15);
				con.add(jlblEmployeeNativeNo);
				
				jcmbEmployeeNativeNo = new JComboBox();
				jcmbEmployeeNativeNo.setFont(StatusFont);
				jcmbEmployeeNativeNo.setBounds(4680/15, 2640/15, 2415/15, 365/15);
				con.add(jcmbEmployeeNativeNo);		
			
				jlblEmployeeId = new JLabel("���֤��");
				jlblEmployeeId.setFont(normalFont);
				jlblEmployeeId.setBounds(3600/15, 3185/15, 960/15, 240/15);
				con.add(jlblEmployeeId);

				jtxtEmployeeId = new JTextField();
				jtxtEmployeeId.setFont(StatusFont);
				jtxtEmployeeId.setBounds(4680/15, 3120/15, 2415/15, 375/15);
				con.add(jtxtEmployeeId);

				jlblEmployeeJobNo = new JLabel("ְ      ��");
				jlblEmployeeJobNo.setFont(normalFont);
				jlblEmployeeJobNo.setBounds(3600/15, 3660/15, 960/15, 240/15);
				con.add(jlblEmployeeJobNo);
				
				jcmbEmployeeJobNo = new JComboBox();
				jcmbEmployeeJobNo.setFont(StatusFont);
				jcmbEmployeeJobNo.setBounds(4680/15, 3600/15, 2415/15, 365/15);
				con.add(jcmbEmployeeJobNo);	
				
				
				jlblEmployeeTel = new JLabel("��ϵ��ʽ");
				jlblEmployeeTel.setFont(normalFont);
				jlblEmployeeTel.setBounds(3600/15, 4147/15, 960/15, 240/15);
				con.add(jlblEmployeeTel);

				jtxtEmployeeTel = new JTextField();
				jtxtEmployeeTel.setFont(StatusFont);
				jtxtEmployeeTel.setBounds(4680/15, 4080/15, 2415/15, 375/15);
				con.add(jtxtEmployeeTel);		
						
				jlblEmployeeStatus = new JLabel("״      ̬");
				jlblEmployeeStatus.setFont(normalFont);
				jlblEmployeeStatus.setBounds(3600/15, 4687/15, 960/15, 240/15);
				con.add(jlblEmployeeStatus);
				
				bgStatus = new ButtonGroup();
				jrdbEmployeeStatus = new JRadioButton[3];
				
				jrdbEmployeeStatus[0] = new JRadioButton("��ְ");		
				jrdbEmployeeStatus[0].setFont(normalFont);
				jrdbEmployeeStatus[0].setBounds(5680/15-70, 3560/15+67,895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[0]);
				con.add(jrdbEmployeeStatus[0]);
				
				jrdbEmployeeStatus[1] = new JRadioButton("ְͣ");
				jrdbEmployeeStatus[1].setFont(normalFont);
				jrdbEmployeeStatus[1].setBounds(5680/15-13, 3560/15+67, 895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[1]);
				con.add(jrdbEmployeeStatus[1]);
				
				jrdbEmployeeStatus[2] = new JRadioButton("��ְ");
				jrdbEmployeeStatus[2].setFont(normalFont);
				jrdbEmployeeStatus[2].setBounds(5680/15+45, 3560/15+67, 895/15, 495/15);
				bgStatus.add(jrdbEmployeeStatus[2]);
				con.add(jrdbEmployeeStatus[2]);
				
				jbtnAdd = new JButton("���");
				jbtnAdd.setFont(StatusFont);
				jbtnAdd.setBounds(3615/15, 5280/15, 975/15, 495/15);
				con.add(jbtnAdd);
				
				jbtnModify = new JButton("�޸�");
				jbtnModify.setFont(StatusFont);
				jbtnModify.setBounds(4560/15, 5280/15, 975/15, 495/15);
				con.add(jbtnModify);
				
				jbtnExit = new JButton("�˳�");
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
				
				jscpEmployeeList.setBorder(BorderFactory.createTitledBorder("Ա���б�"));//�߿򹤳�
				
				jlblEmployeeAccount = new JLabel("��"+i+"��Ա��");
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