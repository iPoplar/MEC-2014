************************************************************************
 *Function List:
 *	AgencyInformation()					���췽��
 *	AddAgencyInformation()				�����ݿ��������
 *	CreateNo()							�Զ����ɴ�����
 *	dealAction()						�����¼�
 *	Id18()								У�����֤��18λ
 *	initAdd()							ˢ��ԺУ�����ᡢ�鳤���
 *	initAgency(String)					��ʼ�����������Ϣ
 *	initAgencyList(String String)		��ʼ�������б���Ϣ
 *	initInternalFrame()					��������
 *	initSector()						��ʼ��ѡ�������б�
 *	initSelected2()						��ʼ���ڡ���ְ��ȫ�����������б�
 *	SelectSex()							�ж���Ů�����ء�0����1��
 *	showMessage(JInternalFrame,String)	�����õ���ʾ����
 *	updateAgency()						�޸����ݿ�
 *	verifyId()							У�����֤��
 *	verifyName()						У������
 ****************************************************************************
 *All		������ʾ���д���
 *At		������ʾ��ְ����
 *Out		������ʾ��ְ����
 *BROWSE	������ʾ���״̬
 *EDIT		������ʾ�༭״̬
 *flagU		ȫ�ֱ���	����ԺУ�����б�����¼���ִ�����
 *flagD 	ȫ�ֱ���	����Ժϵ�����б�����¼���ִ�����
*/	

//java���õ��İ�
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
	//�Ӵ���
	private JInternalFrame jitfAgency;
	private Container con;
	private Container conAgency;
	private Font normalFont;
	
	//����
	private JLabel jlblTopic;
	
	//�ֵ�
	private JLabel jlblSector;
	private JComboBox jcmbSector;
	
	//ѡ���ڡ���ְ���������б�
	private JComboBox jcmbStatus;
	
	//�����б�
	private DefaultListModel dlmAgency;
	private JList jlstAgency;
	private JScrollPane jscpAgency;
	
	//��������
	private JLabel jlblAgencyNumber;
	
	//������
	private JLabel jlblAgencyNo;
	private JLabel jlblNo;
	
	//����ԺУ��ϵ��רҵ
	private JLabel jlblUniversity;
	private JLabel jlblDepartment;
	private JLabel jlblMajor;
	private JComboBox[] jcmbUDM = new JComboBox[3];
	
	//����
	private JLabel jlblName;
	private JTextField jtxtName;
	
	//�Ա�
	private JLabel jlblSex;
	private JRadioButton[] jrdbSex = new JRadioButton[2];
	private ButtonGroup btgpSex;
	
	//���֤��
	private JLabel jlblId;
	private JTextField jtxtId;
	
	//����
	private JLabel jlblNative;
	private JComboBox jcmbNative;
	
	//״̬
	private JLabel jlblStatus;
	private JRadioButton[] jrdbStatus = new JRadioButton[2];
	private ButtonGroup btgpStatus;
	
	//�鳤���
	private JLabel jlblManager;
	private JComboBox jcmbManager;
	
	//��Ӱ�ť
	private JButton jbtnAdd;
	
	//�޸İ�ť
	private JButton jbtnModify;
	
	//�˳���ť
	private JButton jbtnExit;
	
	private int bool;
	
	//ѡ���ڡ���ְ������
	private static final String At = "0";
	private static final String All = "2";
	private static final String Out = "1";
	
	//״̬����
	private static final int BROWSE = 1;
	private static final int EDIT = 0;
	
	//�����¼���־
	private boolean flagU = false;
	private boolean flagD;
	
	//���췽��
	public AgencyInformation()	
	{
		initInternalFrame();
		initSelected2();
		initSector();
		dealAction();				
	}
	
	/**************************************************
	 * Function Name: dealAction
	 * Purpose: ���е��¼�����
	 * Params: �޲�
	 * Return: �޷���ֵ
	 **************************************************/
	public void dealAction()	
	{
		jbtnAdd.addActionListener	//��Ӱ�ť����¼�
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					//showMessage(jitfAgency,jbtnAdd.getText());
					if(jbtnAdd.getText().equals("���"))
					{
						jcmbStatus.setSelectedIndex(0);
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
						SetStatus(EDIT);
						jlblNo.setVisible(false);
						jlblAgencyNo.setVisible(false);
						initAdd();
					}
					else	//ȷ��
					{
						jbtnAdd.setText("���");
						jbtnModify.setText("�޸�");
						if(jcmbUDM[0].isEnabled())	//��ӵ�ȷ��
						{
							if(verifyId() && verifyName())
							{
								AddAgencyInformation();
							}
							else
							{
								showMessage(jitfAgency,"������ݲ���ȷ��");
							}
					
							SetStatus(BROWSE);
							jlblAgencyNo.setVisible(true);
							jlblNo.setVisible(true);
						}
						else	//�޸ĵ�ȷ��
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
		
		jbtnModify.addActionListener	//�޸İ�ť����¼�
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jbtnModify.getText().equals("�޸�"))
					{
						if(jrdbStatus[0].isSelected())
						{
							bool = 0;
						}
						else
							bool = 1;
						
						jrdbStatus[0].setEnabled(true);
						jrdbStatus[1].setEnabled(true);
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
						jlstAgency.setEnabled(false);
					}
					else	//����
					{
						jbtnAdd.setText("���");
						jbtnModify.setText("�޸�");
						
						if(jcmbUDM[0].isEnabled())	//��ӵķ���
						{
							initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
							SetStatus(BROWSE);
							jlblAgencyNo.setVisible(true);
							jlblNo.setVisible(true);
						}
						else	//�޸ĵķ���
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
	
		jlstAgency.addMouseListener		//������Ϣ�б����ѡ������¼�
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
		
		jcmbUDM[1].addActionListener	//Ժϵ�����б�����¼�
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
							System.out.println("�ر��쳣��");
						}

					}
				}
			}
		);
		
		jcmbUDM[0].addItemListener	//ԺУ�����б�����¼�
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
						System.out.println("�ر��쳣��");
						}
					
						flagU = false;
						flagD = true;
					}
				}
			}
		);
		
		jcmbStatus.addItemListener	//ѡ��ȫ�����ڡ���ְ���������б�����¼�
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent e) 
				{
					initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
				}
			}
		);
		
		jcmbSector.addItemListener	//ѡ�������б�ѡ��ı����
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent arg0) 
				{
					initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
				}
			}
		);
		
		jbtnExit.addActionListener	//�˳���ť
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					dealClose();
				}
			}
		);
		
		this.addWindowListener		//�������Ͻǹر�
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
	 * Purpose: ��SQL����޸����ݿ����Ϣ
	 * Params: �޲�
	 * Return: �޷���ֵ
	 *****************************************************************/
	public void updateAgency()	//�޸����ݿ�
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
			System.out.println("�ر��쳣��");
		}
	}
	
	/**********************************************
	 * Function Name: initAdd
	 * Purpose: �������Ӱ�ťʱ��ʼ��ԺУ��ϵרҵ�����б�
	 * Params: �޲�
	 * Return: �޷���ֵ
	 **********************************************/
	public void initAdd()
	{
		String SQLStringU = "SELECT * FROM SYS_UDM_INFORMATION WHERE SUBSTR(UDMID,1,2) != '00' AND SUBSTR(UDMID,3) = '0000' AND UDMSTATUS = '0'";
		String SQLStringN = "SELECT * FROM SYS_MEC_NATIVE";
		String SQLStringMA = "SELECT * FROM SYS_MEC_AGENCY WHERE AGENCYMANAGERNO = '00000000000'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			//ˢ��ԺУ
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
			
			//ˢ�¼���
			FResultSet rsN = FStatement.executeQueryCon(SQLStringN);
			
			String strNative;
			String strNNo;
			while(rsN.next())
			{
				strNative = rsN.getString("NATIVE");//*******************************
				strNNo = rsN.getString("NATIVENO");
				jcmbNative.addItem(strNNo + strNative);
			}
			
			//ˢ���鳤
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
			System.out.println("�ر��쳣��");
		}
	}
	
	/*******************************************************
	 * Function Name: AddAgencyInformation
	 * Purpose: �����ݿ��������
	 * Params: �޲�
	 * Return: �޷���ֵ
	 *******************************************************/
	public void AddAgencyInformation()		
	{
		String strNo;		//������
		String strName;		//��������
		String strId;		//�������֤��
		String strUDM;		//����ԺУ���
		String strNative;	//��������
		String strManager;	//�����鳤���
		String strStatus;	//����״̬
		
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
		
		//д�����ݿ�

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
			System.out.println("�ر��쳣��");
		}
	}
	
	/*********************************************************
	 * Function Name: verifyName
	 * Purpose: �ж������Ƿ����Ҫ��
	 * Params: �޲�
	 * Return: ����Boolean���ͣ�������ȷʱ����true�����򷵻�false
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
	 * Purpose: ���ݴ����б�������Զ����ɴ�����
	 * Params: �޲�
	 * Return: ����һ��String���͵Ĵ�����
	 ***********************************************************/
	public String CreateNo()	//�Զ����ɴ�����
	{
		String strNo = null;
		String strSector; //�ֵ���
		String strUniversity;//ѧУ���
		String strDateYear;//��ǰ��
		Calendar Date = Calendar.getInstance();
		String strDateMonth;//��ǰ��
		Calendar Month = Calendar.getInstance();
		String strcon;//���
		
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
	 * Purpose: ���ý���ؼ��Ŀ�����
	 * Params: ���ͳ���������BROWSE��EDIT
	 * Return: �޷���ֵ
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
	 * Purpose: ���ݴ����б��ѡ�����ʼ��������Ϣ
	 * Params: String���͵Ĳ����������б��ѡ�����ǰ11���ַ�
	 * Return: �޷���ֵ
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
			System.out.println("�ر��쳣��");
		}
	}
	
	/*****************************************************
	 * Function Name:initAgencyList
	 * Purpose: ���ݷֵ���Ƿ���ְ��ʼ�������б���Ϣ
	 * Params: ����String���͵Ĳ������ֱ��Ƿֵ��ѡ������Ƿ���ְ��ѡ����
	 * Return: �޷���ֵ
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
				System.out.println("�ر��쳣��");
			}
		
			jlstAgency.setSelectedIndex(0);
		
			System.out.println(jlstAgency.isSelectionEmpty());
			if(jlstAgency.isSelectionEmpty())
			{
				showMessage(jitfAgency,"û�д���");
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
				System.out.println("�ر��쳣��");
			}
		
			jlstAgency.setSelectedIndex(0);
		
			String strNo;

			initAgency(((String) jlstAgency.getSelectedValue()).substring(0,11));
		}
		
		jlblAgencyNumber.setText("��" + dlmAgency.getSize() + "������");
		
	}
	
	/***********************************************************
	 * Function Name:initSector
	 * Purpose: ��ʼ���ֵ������б�
	 * Params: �޲�
	 * Return: �޷���ֵ
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
			System.out.println("�ر��쳣��");
		}
		
		initAgencyList((String)jcmbSector.getSelectedItem(),(String)jcmbStatus.getSelectedItem());
		
		SetStatus(BROWSE);
	}
	
	/**********************************************************
	 * Function Name:initSelected2
	 * Purpose: ��ʼ���Ƿ���ְ�����б�
	 * Params: �޲�
	 * Return: �޷���ֵ
	 **********************************************************/
	public void initSelected2()		
	{
		jcmbStatus.addItem(All + "ȫ��");
		jcmbStatus.addItem(At + "��ְ");
		jcmbStatus.addItem(Out + "��ְ");
	}
	
	/***********************************************************
	 * Function Name:verifyId
	 * Purpose: ��֤���֤�Ƿ���ȷ
	 * Params: �޲�
	 * Return: boolean���͵ķ���ֵ�����֤��ȷ����true���򷵻�false
	 ***********************************************************/
	public boolean verifyId()  
	{
		boolean result = false;
		String strId;
		String str0_2;		//ʡ
		String str6_10;		//������
		String str10_12;	//������
		String str12_14;	//������
		String str16;		//��orŮ
		String strNative;
		int Year;
		int Month;
		int Day;
		int Sex;
		strNative = ((String) jcmbNative.getSelectedItem()).substring(0,2);
		strId = jtxtId.getText();
		
		str0_2 = strId.substring(0,0+2);	//ʡ
		str6_10 = strId.substring(6,6+4);	//������
		str10_12 = strId.substring(10,12);	//������
		str12_14 = strId.substring(12,14);	//������
		str16 = strId.substring(16,17);		//��orŮ
		
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
	 * Purpose: ��֤���֤�ŵ�ʮ��λ�Ƿ���ȷ
	 * Params: �޲�
	 * Return: ����boolean���ͣ����֤�ŵ�ʮ��λ��ȷ����true���򷵻�false
	 ***********************************************************/
	public boolean Id18()	
	{
		boolean b = false;
		String strId = jtxtId.getText();	//���֤�ַ���
		String str17;						//���֤��ʮ��λ
		int remainder;						//����
		int[] intId = new int[17];
		int[] ratio = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7,9, 10, 5, 8, 4, 2 };	//ϵ��
		String[] v = { "1", "0", "x", "9", "8", "7", "6", "5", "4","3", "2"};	//У��λ����
		
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
	 * Purpose: ������Ů��ѡ���������0��1
	 * Params: �޲�
	 * Return: ���ͷ���ֵ���з���1��Ů����0 
	 ***********************************************************/
	public int SelectSex()	//����Ϊ�У�ż��ΪŮ
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
	 * Purpose: ��������������ʱ����ʾ��Ϣ��ʾ����
	 * Params: �����������ֱ�Ϊ���ڵĶ����Ҫ��ʾ���ַ���
	 * Return: �޷���ֵ
	 **********************************************************/
	public void showMessage(JInternalFrame jitf, String mess)	
	{
		JOptionPane.showMessageDialog(jitfAgency,mess,"������",JOptionPane.DEFAULT_OPTION);
	}
	
	/**********************************************************
	 * Function Name: dealClose
	 * Purpose: �رմ���
	 * Params: �޲�
	 * Return: �޷���ֵ
	 * 
	 **********************************************************/
	public void dealClose()	
	{
		this.dispose();
	}
	
	/**********************************************************
	 * Function Name: initInternalFrame
	 * Purpose: ���ɴ��������Ϣ����
	 * Params: �޲�
	 * Return: �޷���ֵ
	 **********************************************************/
	public void initInternalFrame()
	{
		this.setSize(1360,750);
		this.setLayout(null);
		con = this.getContentPane();
		normalFont = new Font("����",Font.PLAIN,16);
		
		jitfAgency = new JInternalFrame("������Ϣ",false,true,false,true);
		jitfAgency.setSize(8085/15,7230/15);
		jitfAgency.setLayout(null);
		conAgency = jitfAgency.getContentPane();
		con.add(jitfAgency);
		
		//����
		jlblTopic = new JLabel("������Ϣ����");
		jlblTopic.setFont(new Font("΢���ź�",Font.PLAIN,24));
		jlblTopic.setBounds(2902/15, 0, 2160/15, 465/15);
		conAgency.add(jlblTopic);
		
		//�ֵ�
		jlblSector = new JLabel("�ֵ���");
		jlblSector.setFont(normalFont);
		jlblSector.setBounds(120/15,840/15,960/15,240/15);
		conAgency.add(jlblSector);
		
		jcmbSector = new JComboBox();
		jcmbSector.setFont(normalFont);
		jcmbSector.setBounds(1200/15,780/15,855/15,360/15);
		conAgency.add(jcmbSector);
		
		//ѡ���ڡ���ְ���������б�
		jcmbStatus = new JComboBox();
		jcmbStatus.setFont(normalFont);
		jcmbStatus.setBounds(2160/15,780/15,975/15,360/15);
		conAgency.add(jcmbStatus);
		
		//�����б�
		dlmAgency = new DefaultListModel();
		jlstAgency = new JList(dlmAgency);
		jscpAgency = new JScrollPane(jlstAgency);
		jlstAgency.setFont(normalFont);
		jscpAgency.setBounds(120/15,1200/15,3015/15,4455/15);
		jscpAgency.setBorder(BorderFactory.createTitledBorder("�����б�"));
		conAgency.add(jscpAgency);
		
		//��������
		jlblAgencyNumber = new JLabel("��0������");
		jlblAgencyNumber.setFont(normalFont);
		jlblAgencyNumber.setBounds(120/15,5640/15,3015/15,240/15);
		conAgency.add(jlblAgencyNumber);//
		
		//������
		jlblAgencyNo = new JLabel("������");
		jlblAgencyNo.setFont(normalFont);
		jlblAgencyNo.setBounds(3360/15,840/15,960/15,240/15);
		conAgency.add(jlblAgencyNo);
		
		jlblNo = new JLabel("");
		jlblNo.setFont(normalFont);
		jlblNo.setBounds(4440/15,840/15,3360/15,240/15);
		conAgency.add(jlblNo);
		
		//����ԺУ
		jlblUniversity = new JLabel("����ԺУ");
		jlblUniversity.setFont(normalFont);
		jlblUniversity.setBounds(3360/15,1373/15,960/15,240/15);
		conAgency.add(jlblUniversity);
		
		jcmbUDM[0] = new JComboBox();
		jcmbUDM[0].setFont(normalFont);
		jcmbUDM[0].setBounds(4440/15,1313/15,3375/15,360/15);
		conAgency.add(jcmbUDM[0]);
		
		//����Ժϵ
		jlblDepartment = new JLabel("����Ժϵ");
		jlblDepartment.setFont(normalFont);
		jlblDepartment.setBounds(3360/15,1906/15,960/15,240/15);
		conAgency.add(jlblDepartment);
		
		jcmbUDM[1] = new JComboBox();
		jcmbUDM[1].setFont(normalFont);
		jcmbUDM[1].setBounds(4440/15,1846/15,3375/15,360/15);
		conAgency.add(jcmbUDM[1]);
		
		//����רҵ
		jlblMajor = new JLabel("����רҵ");
		jlblMajor.setFont(normalFont);
		jlblMajor.setBounds(3360/15,2439/15,960/15,240/15);
		conAgency.add(jlblMajor);
		
		jcmbUDM[2] = new JComboBox();
		jcmbUDM[2].setFont(normalFont);
		jcmbUDM[2].setBounds(4440/15,2379/15,3375/15,360/15);
		conAgency.add(jcmbUDM[2]);
		
		//����
		jlblName = new JLabel("��    ��");
		jlblName.setFont(normalFont);
		jlblName.setBounds(3360/15,2970/15,960/15,240/15);
		conAgency.add(jlblName);
		
		jtxtName = new JTextField();
		jtxtName.setFont(normalFont);
		jtxtName.setBounds(4440/15,2912/15,3375/15,360/15);
		conAgency.add(jtxtName);
		
		//�Ա�
		jlblSex = new JLabel("��    ��");
		jlblSex.setFont(normalFont);
		jlblSex.setBounds(3360/15,3505/15,960/15,240/15);
		conAgency.add(jlblSex);
		
		btgpSex = new ButtonGroup();
		jrdbSex[0] = new JRadioButton("��");
		jrdbSex[0].setFont(normalFont);
		jrdbSex[0].setBounds(4680/15,3438/15,975/15,375/15);
		conAgency.add(jrdbSex[0]);
		btgpSex.add(jrdbSex[0]);
		
		jrdbSex[1] = new JRadioButton("Ů");
		jrdbSex[1].setFont(normalFont);
		jrdbSex[1].setBounds(6360/15,3438/15,975/15,375/15);
		conAgency.add(jrdbSex[1]);
		btgpSex.add(jrdbSex[1]);
		
		//���֤��
		jlblId = new JLabel("���֤��");
		jlblId.setFont(normalFont);
		jlblId.setBounds(3360/15,4038/15,960/15,240/15);
		conAgency.add(jlblId);
		
		jtxtId = new JTextField();
		jtxtId.setFont(normalFont);
		jtxtId.setBounds(4440/15,3978/15,3375/15,360/15);
		conAgency.add(jtxtId);
		
		//����
		//TODO�����ж������
		jlblNative = new JLabel("��    ��");
		jlblNative.setFont(normalFont);
		jlblNative.setBounds(3360/15,4571/15,960/15,240/15);
		conAgency.add(jlblNative);
		
		jcmbNative = new JComboBox();
		jcmbNative.setFont(normalFont);
		jcmbNative.setBounds(4440/15,4511/15,3375/15,360/15);
		conAgency.add(jcmbNative);
		
		//״̬
		jlblStatus = new JLabel("״    ̬");
		jlblStatus.setFont(normalFont);
		jlblStatus.setBounds(3360/15,5104/15,960/15,240/15);
		conAgency.add(jlblStatus);
		
		btgpStatus = new ButtonGroup();
		jrdbStatus[0] = new JRadioButton("��ְ");
		jrdbStatus[0].setFont(normalFont);
		jrdbStatus[0].setBounds(4680/15,5037/15,975/15,375/15);
		conAgency.add(jrdbStatus[0]);
		btgpStatus.add(jrdbStatus[0]);

		jrdbStatus[1] = new JRadioButton("��ְ");
		jrdbStatus[1].setFont(normalFont);
		jrdbStatus[1].setBounds(6360/15,5037/15,975/15,375/15);
		conAgency.add(jrdbStatus[1]);
		btgpStatus.add(jrdbStatus[1]);
		
		//�鳤���
		jlblManager = new JLabel("�鳤���");
		jlblManager.setFont(normalFont);
		jlblManager.setBounds(3360/15,5640/15,960/15,240/15);
		conAgency.add(jlblManager);
		
		jcmbManager = new JComboBox();
		jcmbManager.setFont(normalFont);
		jcmbManager.setBounds(4440/15,5580/15,3375/15,360/15);
		conAgency.add(jcmbManager);
		
		//��Ӱ�ť
		jbtnAdd = new JButton("���");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(120/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnAdd);
		
		//�޸İ�ť
		jbtnModify = new JButton("�޸�");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1320/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnModify);
		
		//�˳���ť
		jbtnExit = new JButton("�˳�");
		jbtnExit.setFont(normalFont);
		jbtnExit.setBounds(6600/15,6120/15,1215/15,465/15);
		conAgency.add(jbtnExit);
		
		jitfAgency.setVisible(true);
		this.setVisible(true);
	}
}
