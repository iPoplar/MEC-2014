
/*******************************************************

* Function List:                �������漰�����з���
*	setStatus		ְ��״̬������
*	dealClose	        �ر���������
*	checkListEmpty		�ж�ְ���б��Ƿ�Ϊ��
*	getDutiesInformation    ͨ��ְ���Ų���ְ������Ӧ�����Ƽ���н
*       findStatus              ��ѯ���ݿ��е����п��õ�ְ�񣬼�״̬Ϊ0��ְ��
*       updateDutiesInformation �޸��б��е���Ϣ����ʱֻ���޸�ְ���״̬ 
*       addDutiesInformation    ���ְ����Ϣ
*       initDutiesList          ��ʼ��ְ���б�
*       reinitFrame             �ٴγ�ʼ��
*       dealAction              �����¼������Ķ���
*       initFrame               ��ʼ��
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
	private JFrame jfrmMain;                  /*����������*/   
	private Container container;              /*�������ʢװ�������и����ؼ�������*/
	
	private JLabel jlblTopic;                 /*����*/
	
	private Choice choiceDutiesList;          /*ְ���б�������б��*/
	
	private DefaultListModel dlmDutiesList;   /*ְ���б���������洢���ݵĵط����Ժ�����ӡ��޸Ķ��������*/
	private JList jlstDutiesList;             /*ְ���б�*/
	private JScrollPane jscpDutiesList;       /*ְ���б��Ĺ�����*/
	
	private JLabel jlblAllDuties;             /*����ְ��ĸ���*/
	
	private JLabel jlblDutiesInformation;     /*ְ����Ϣ*/
	
	private JLabel jlblDutiesNumber;          /*ְ����*/
	private JLabel jlblDutiesNumberContent;  
	
	private JLabel jlblDutiesName;            /*ְ������*/
	private JTextField jtxtDutiesName;        /*ְ����������Ӧ���ı���*/
	
	private JLabel jlblDutiesSalary;          /*ְ����н*/
	private JTextField jtxtDutiesSalary;      /*ְ����н����Ӧ���ı���*/
	private JLabel jblDutiesSalaryUnit;
	
	private JLabel jlblDutiesStatus;
	
	private JRadioButton[] jrdbStatus;        /*ְ��״̬*/
	private ButtonGroup bgStatus;
	
	private JButton jbtnAdd;                  /*���Ӱ�ť*/
	private JButton jbtnModify;               /*�޸İ�ť*/
	private JButton jbtnExit;                 /*�˳���ť*/
	
	private int lastAction;
	private String lastName;
	
	private static final int ADDATION = 1;
	private static final int MODIFY = 2;
	
	private static final String TOPIC = "ְ�����";
	
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
	* Purpose: 			ְ��״̬������
	* Params : 
	*	@int 	Status 	�û����ݵ�״̬
	*Return:            ��
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
	* Purpose: 			�ر���������
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void dealClose()
	{
		 jfrmMain.dispose();
	}
	
	
	/*******************************************************
	* Function Name: 	checkListEmpty
	* Purpose: 			�ж�ְ���б�������Ƿ�Ϊ��
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void checkListEmpty()              
	{
		jbtnModify.setEnabled(dlmDutiesList.getSize() > 0);
	}	
	
	/*******************************************************
	* Function Name: 	getDutiesInformation
	* Purpose: 			ͨ��ְ���Ų���ְ������Ӧ�����Ƽ���н
	* Params : 
	*	@String		S	ְ����
	* Return: 			��
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
			System.out.println("�ر��쳣��");
		}
	}
	
	/*******************************************************
	* Function Name: 	findStatus
	* Purpose: 			��ѯ���ݿ��е����п��õ�ְ�񣬼�״̬Ϊ0��ְ��
	* Params:           ��
	* Return: 		           ��
	*******************************************************/

	private void findStatus()       
	{
		 String SQLInsertString="select * from SYS_MEC_JOB where jobStatus = '0' order by(jobNo) ";
		 dlmDutiesList.removeAllElements();
		 
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLInsertString); 
				
				checkListValues = false;//�ж�ְ���б�����Ƿ�����������û��, ��ѯ���ݿ⣬�����ݿ��е�ֵ���뵽ְ���б���С�
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
				jlblAllDuties.setText("��" + i + "��ְ��");
				
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
				System.out.println("�ر��쳣��");
			}
	}
	
	/*******************************************************
	* Function Name: 	updateDutiesInformation
	* Purpose: 			�޸��б��е���Ϣ����ʱֻ���޸�ְ���״̬
	* Params :          ��
	* Return: 			��
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
			MECTool.showMessage(jfrmMain, e.getMessage() + "��������ݿ����Աǰ����������⡣" +
			"\n�Դ�΢����Ƽ����Ǹ�⣡");
			e.printStackTrace();
		}
		try 
		{
			FStatement.disConnection();
		} catch (Exception e)
		{
			MECTool.showMessage(jfrmMain, "���ݿ�ر��쳣");
			e.printStackTrace();
		}
	}
	
	/*******************************************************
	* Function Name: 	addDutiesInformation
	* Purpose: 			���ְ����Ϣ
	* Params :          ��
	* Return: 			��
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
					
					if(rs == true && strSalary.length()>=4 && strSalary.length()<=5)//��н����Ϊ4λ-5λ����֮��
					{
					String SQLInsertString="insert into SYS_MEC_JOB values('" + str +"', '"+strName+"', '"+strSalary+"','0')";
					
					FStatement.executeUpdateCon(SQLInsertString);
				
					}
					else
					{
						MECTool.showMessage(jfrmMain, "���������Ϣ��������������");
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
					if(rs ==true && strSalary.length()>=4 && strSalary.length()<=5)//��н����Ϊ4λ����-5λ����֮��
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
						  MECTool.showMessage(jfrmMain, "����ظ�");
					   }
					}
					else
					{
						MECTool.showMessage(jfrmMain, "���������Ϣ��������������");
					}
			   }
				  
				 FStatement.disConnection();
				}
				catch (Exception e) {
				MECTool.showMessage(jfrmMain, e.getMessage() + "��������ݿ����Աǰ����������⡣" +
				"\n�Դ�΢����Ƽ����Ǹ�⣡");
			}
	    }
	
	/*******************************************************
	* Function Name: 	reinitFrame
	* Purpose: 			�ٴγ�ʼ��
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void reinitFrame()
	{
		initDutiesList();
	}
	
	/*******************************************************
	* Function Name: 	initDutiesList
	* Purpose: 			��ʼ��ְ���б�
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void initDutiesList()    
	{
		 String SQLString = "SELECT jobNo,jobName,jobStatus FROM  SYS_MEC_JOB order by(jobNo)";
		 dlmDutiesList.removeAllElements();
		 
			try 
			{
				FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
				FResultSet rs = FStatement.executeQueryCon(SQLString); 
				
				checkListValues = false;//�ж�ְ���б�����Ƿ�����������û��, ��ѯ���ݿ⣬�����ݿ��е�ֵ���뵽ְ���б���С�
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
				jlblAllDuties.setText("��" + i + "��ְ��");
				
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
				System.out.println("�ر��쳣��");
			}
		}
	
	/*******************************************************
	* Function Name: 	dealAction
	* Purpose: 			�����¼������Ķ���
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void dealAction()
	{
		/*�����б�ļ����¼�*/
	   choiceDutiesList.addItemListener  
		(
				new ItemListener()
				{
					public void itemStateChanged(ItemEvent arg0)
					{
	
						if(choiceDutiesList.getSelectedItem().equals("����ְ���б�"))
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
	
	   /*ְ���б���б�ѡ��������ļ����¼�*/
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
		
		/*"�˳�"��ť�ļ����¼�*/
		jbtnExit.addActionListener    
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						MECTool.showMessage(jfrmMain, "��ȷ��Ҫ�˳���");
						dealClose();
					}
					
				}
	    );
		
		/*"�޸�"��ť�ļ����¼�*/
		jbtnModify.addActionListener 
		(
				
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						int i = 0;
						if (jbtnModify.getText().equals("�޸�"))
						{
							jbtnModify.setText("����");
							jbtnAdd.setText("ȷ��");
							
							i = choiceDutiesList.getSelectedIndex();
							jtxtDutiesName.setEditable(false);
							jtxtDutiesSalary.setEditable(false);
							
							setStatus(EDIT);
						}
						else
						{
							checkListEmpty();
							jbtnModify.setText("�޸�");
							jbtnAdd.setText("���");
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
      
		/*"���"��ť�ļ����¼�*/
		jbtnAdd.addActionListener    
		(
				new ActionListener()
				{
					
					public void actionPerformed(ActionEvent e) 
					{
						int i = 0;
						if (jbtnAdd.getText().equals("���"))
						{
							jbtnAdd.setText("ȷ��");
							jbtnModify.setText("����");
							setStatus(EDIT);
							
							i = choiceDutiesList.getSelectedIndex();
							
							jlblDutiesNumberContent.setText(null);//���ְ��������Ӧ������
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
							jbtnAdd.setText("���");
							jbtnModify.setText("�޸�");
							
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
	* Purpose: 			��ʼ��
	* Params :          ��
	* Return: 			��
	*******************************************************/

	private void initFrame()
	{
		Dimension dim;
		Font normalFont = new Font("����",Font.PLAIN,16);
		
		jfrmMain = new JFrame(TOPIC);
		container = jfrmMain.getContentPane();
		container.setLayout(null);
		jfrmMain.setSize(6750/15, 5700/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2,(dim.height - jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel(TOPIC);
		jlblTopic.setFont(new Font("΢���ź�",Font.PLAIN,24));
		jlblTopic.setForeground(Color.blue);
		jlblTopic.setBounds(2647/15,0,1440/15,465/15);
		container.add(jlblTopic);
		
		choiceDutiesList = new Choice();
		choiceDutiesList.setFont(normalFont);
		choiceDutiesList.setBounds(240/15,720/15,2895/15,360/15);
		choiceDutiesList.add("����ְ���б�");
		choiceDutiesList.add("����ְ���б�");
		container.add(choiceDutiesList);
		
		dlmDutiesList = new DefaultListModel();
		jlstDutiesList = new JList(dlmDutiesList);
		jscpDutiesList = new JScrollPane(jlstDutiesList);
		jlstDutiesList.setFont(normalFont);
		
		jscpDutiesList.setBorder(BorderFactory.createTitledBorder("ְ���б�"));
		jscpDutiesList.setBounds(240/15, 1080/15, 2895/15, 2895/15);
		container.add(jscpDutiesList);
		
		jlblAllDuties = new JLabel("��0��ְλ");
		jlblAllDuties.setFont(normalFont);
		jlblAllDuties.setBounds(240/15,4080/15,2760/15,240/15);
		container.add(jlblAllDuties);

		
		jlblDutiesInformation = new JLabel("ְ����Ϣ");
		jlblDutiesInformation.setFont(normalFont);
		jlblDutiesInformation.setBounds(4320/15,720/15,960/15,240/15);
		container.add(jlblDutiesInformation);
		
		jlblDutiesNumber = new JLabel("ְ����");
		jlblDutiesNumber.setFont(normalFont);
		jlblDutiesNumber.setBounds(3360/15,1200/15,960/15,240/15);
		container.add(jlblDutiesNumber);
		
		jlblDutiesNumberContent = new JLabel("");
		jlblDutiesNumberContent.setFont(normalFont);
		jlblDutiesNumberContent.setBounds(4440/15,1200/15,1935/15,255/15);
		container.add(jlblDutiesNumberContent);
		
		jlblDutiesName = new JLabel("ְ������");
		jlblDutiesName.setFont(normalFont);
		jlblDutiesName.setBounds(3360/15,1920/15,960/15,240/15);
		container.add(jlblDutiesName);
		
		jtxtDutiesName = new JTextField("");
		jtxtDutiesName.setFont(normalFont);
		jtxtDutiesName.setBounds(4440/15,1853/15,1935/15,375/15);
		container.add(jtxtDutiesName);
		
		jlblDutiesSalary = new JLabel("ְ����н");
		jlblDutiesSalary.setFont(normalFont);
		jlblDutiesSalary.setBounds(3360/15,2640/15,960/15,240/15);
		container.add(jlblDutiesSalary);
		
		jtxtDutiesSalary = new JTextField("");
		jtxtDutiesSalary.setFont(normalFont);
		jtxtDutiesSalary.setBounds(4440/15,2573/15,1695/15,375/15);
		container.add(jtxtDutiesSalary);
		
		jblDutiesSalaryUnit = new JLabel("Ԫ");
		jblDutiesSalaryUnit.setFont(normalFont);
		jblDutiesSalaryUnit.setBounds(6120/15,2640/15,240/15,240/15);
		container.add(jblDutiesSalaryUnit);
		
		
		jlblDutiesStatus = new JLabel("ְ��״̬");;
		jlblDutiesStatus.setFont(normalFont);
		jlblDutiesStatus.setBounds(3360/15,3360/15,960/15,240/15);
		container.add(jlblDutiesStatus);
		
		bgStatus = new ButtonGroup();
		jrdbStatus = new JRadioButton[2];
		
		jrdbStatus[0] = new JRadioButton("����");
		jrdbStatus[0].setFont(normalFont);
		jrdbStatus[0].setBounds(4440/15, 3293/15, 975/15-5, 375/15);
		bgStatus.add(jrdbStatus[0]);
		container.add(jrdbStatus[0]);
		
		jrdbStatus[1] = new JRadioButton("������");
		jrdbStatus[1].setFont(normalFont);
		jrdbStatus[1].setBounds(5280/15, 3293/15, 1095/15, 375/15);
		bgStatus.add(jrdbStatus[1]);
		container.add(jrdbStatus[1]);
		

		jbtnAdd = new JButton("���");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(240/15, 4560/15, 1215/15, 465/15);
		container.add(jbtnAdd);
		
		jbtnModify = new JButton("�޸�");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1440/15,4560/15, 1215/15, 465/15);
		container.add(jbtnModify);
		
		jbtnExit = new JButton("�˳�");
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
