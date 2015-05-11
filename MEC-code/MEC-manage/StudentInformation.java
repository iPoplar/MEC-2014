
/*******************************************************

* Function List:
*		initFrame					�����ʼ��
*		reinitFrame					�ٴγ�ʼ�������н�����ٴγ�ʼ��
*		dealAction					�¼�����
*		stateAlter					״̬����
*		addIdNumberRepeateInpect	���ʱ������֤�����Ƿ��ظ�
*		modifyIdNumberRepeateInpect	�޸�ʱ������֤�����Ƿ��ظ�
*		idNumberCodIpect			���֤����ĺϷ����ж�
*		inspectError				��֤�����ж�
*		updataData					ѧԱ������Ϣ���޸�
*		addData						ѧԱ������Ϣ�����
*		dealAction					�����¼��ļ���
*		initMajor					��ʼ��ָ��Ժϵ�µ�רҵ
*		initDepartment				��ʼ��ָ��ѧУ�µ�Ժϵ������ö�רҵ�ĳ�ʼ��
*		initSchool					��ʼ��ѧУ�б�����ö�Ժϵ�ĳ�ʼ��
*		initStudentInfoSelectFrame	��ʼ�����ᡢ���塢�꼶��ѧУ��Ժϵ��רҵ�����б�
*		initstudentBaseInfoList		��ʼ��ѧԱ��Ϣ�б�
*		initStudnetInfo				��ʼ��ָ�����֤�����ѧԱ��Ϣ
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
	private JFrame jfrmMain;				/*��������*/
	private Container container;			/*��������*/
	
	private JLabel jlblTopic;				/*�����ǩ����*/
	
	private DefaultListModel dlmStuBaseInfo;/*Ĭ���б�ģ������*/
	private JList jlstStuBaseInfo;			/*�б������*/
	private JScrollPane jscpStuBaseInfo;	/*������������*/
	
	private JLabel jlblIdNumber;			/*������ǩ����*/
	private JTextField jtxtIdNumber;		/*�����ı�������*/
	
	private JLabel jlblName;				/*���֤�ű�ǩ����*/
	private JTextField jtxtName;			/*���֤���ı�������*/
	
	private JLabel jlblNative;				/*�����ǩ����*/
	private JComboBox jcmbNative;			/*���������б������*/
	
	private JLabel jlblNation;				/*�����ǩ����*/
	private JComboBox jcmbNation;			/*���������б������*/
	
	private JLabel jlblGrade;				/*�꼶��ǩ����*/
	private JComboBox jcmbGrade;			/*�꼶�����б������*/
	
	private JLabel jlblSchool;				/*ѧУ��ǩ����*/
	private JComboBox jcmbSchool;			/*ѧУ�����б������*/
	
	private JLabel jlblDepartment;			/*Ժϵ��ǩ����*/
	private JComboBox jcmbDepartment;		/*Ժϵ�����б������*/
	
	private JLabel jlblMajor;				/*רҵ��ǩ����*/
	private JComboBox jcmbMajor;			/*רҵ�����б������*/
	
	private JLabel jlblStuSum;				/*ѧԱ��Ϣ������ǩ*/
	
	private JLabel jlblPhone;				/*�ֻ������ǩ����*/
	private JTextField jtxtPhone;			/*�ֻ������ı�������*/
	
	private JButton jbtnAdd;				/*��Ӱ�ť����*/ 
	private JButton jbtnModify;				/*�޸İ�ť����*/
	private JButton jbtnExit;				/*�˳���ť����*/
	
	private static final boolean BROWSE = false;		/*���״̬*/
	private static final boolean EDIT = true;			/*�༭״̬*/

	private static boolean ADDATION = true;				/*��¼�ϴεİ�ť������͡���ӡ�*/
	private static boolean MODIFY = false;				/*��¼�ϴεİ�ť������͡��޸ġ�*/
	private static boolean operating = false;			/*�������ּ�¼�����ݡ��������͡�*/

	private String restoreIdName = null;				/*�������ָֻ�����ʱ���������֤����*/
	
	private boolean listInitEnd = false;				/*��������ѧԱ��Ϣ�б��ʼ�����*/
	private boolean jcmbSchoolInitEnd = false;			/*��������ѧУ�б��ʼ�����*/
	private boolean jcmbDepartmentInitEnd = false;		/*��������Ժϵ�б��ʼ�����*/

 	/*******************************************************
 	* Function Name: 	StudentInformation
 	* Purpose: 			���췽��
 	* Params : 			�޲�
 	* Return: 			�޷���ֵ   	
 	*******************************************************/
 	public StudentInformation()
	{
		initFrame();
		reinitFrame();
		dealAction();
	}
 	
 	/*******************************************************
 	* Function Name: 	stateAlter
 	* Purpose: 			���������༭֮���״̬��Ǩ
 	* Params : 
 	*	@boolean     	state	����״̬
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
 	* Purpose: 			�޸�ʱ������֤�����Ƿ��ظ�
 	* Params : 			�޲�
 	* Return: 			boolean	true���� 	false�ظ�  	
 	*******************************************************/
	private boolean modifyIdNumberRepeateInpect()
	{
		for(int i = 0; i < dlmStuBaseInfo.getSize(); i++)
			if(dlmStuBaseInfo.getElementAt(i).toString().substring(0, 0+18).equals(jtxtIdNumber.getText())
					&& !jlstStuBaseInfo.getSelectedValue().toString().substring(0, 18).equals(jtxtIdNumber.getText()))
			{
				JOptionPane.showMessageDialog(jfrmMain, "���֤�������ظ�������������", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
		
				return false;
			}

		return true; 
	}
	
 	/*******************************************************
 	* Function Name: 	addIdNumberRepeateInpect
 	* Purpose: 			���ʱ������֤�����Ƿ��ظ�
 	* Params : 			�޲�
 	* Return: 			boolean	true���� 	false�ظ�  	
 	*******************************************************/
	private boolean addIidNumberRepeateInpect()
	{
		for(int i = 0; i < dlmStuBaseInfo.getSize(); i++)
			if(dlmStuBaseInfo.getElementAt(i).toString().substring(0, 0+18).equals(jtxtIdNumber.getText()))
			{
				JOptionPane.showMessageDialog(jfrmMain, "���֤�������ظ�������������", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
		
				return false;
			}

		return true; 
	}
	
 	/*******************************************************
 	* Function Name: 	idNumberCodIpect
 	* Purpose: 			�жϸ����֤���Ƿ�Ϸ�
 	* Params : 
 	*	@String     	id	���֤��
 	* Return: 			boolean	����ֵ 	trueΪ��ȷ 	false	 ���Ϸ�
 	*******************************************************/
	private boolean idNumberCodIpect(String id)
	{
		FResultSet YesNo = null;
		int a[] = new int[18];
		int inspect[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		char inspectCode[] = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};
		int sum = 0;
		String SQLID2 = "SELECT NATIVE FROM SYS_MEC_NATIVE WHERE NATIVENO =  '"+id.substring(0, 2)+"'";
		
		// У��λ��֤
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
		
		// ������֤
		Calendar cld  = Calendar.getInstance(); 
		cld.set(Integer.parseInt(id.substring(6, 6+4)), Integer.parseInt(id.substring(10, 10+2)), Integer.parseInt(id.substring(12, 12+2)));
		if(cld.get(Calendar.YEAR) == Integer.parseInt(id.substring(6, 6+4)) 
				&&cld.get(Calendar.MONTH) == Integer.parseInt(id.substring(10, 10+2))
				&&cld.get(Calendar.DATE) == Integer.parseInt(id.substring(12, 12+2)))
			;
		else
			return false;
		
		// ������֤
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
 	* Purpose: 			���֤���룬�������ֻ��������֤
 	* Params : 			�޲�
 	* Return: 			boolean	����ֵ 	trueΪ��ȷ 	false	 ���Ϸ�
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
					JOptionPane.showMessageDialog(jfrmMain, "�ֻ���������Ƿ�������������", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
					jtxtPhone.requestFocus();
					jtxtPhone.selectAll();
					return false;
				}
			else
			{	
				JOptionPane.showMessageDialog(jfrmMain, "��������Ƿ�������������", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
				jtxtName.requestFocus();
				jtxtName.selectAll();
				return false;
			}
		else
		{	
			JOptionPane.showMessageDialog(jfrmMain, "���֤������Ƿ�������������", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
			jtxtIdNumber.requestFocus();
			jtxtIdNumber.selectAll();
			return false;
		}
	}

 	/*******************************************************
 	* Function Name: 	updataData
 	* Purpose: 			���ݵĸ��¼��޸Ĳ���
 	* Params : 			�޲�
 	* Return: 			boolean ����ֵ 	trueΪ�ɹ� 	false	 ʧ��
 	*******************************************************/
	private boolean updataData()
	{
		String lId = null;		// ���֤��
		String lNname = null;	// ����
		String lNative = null;	// ����
		String lNation = null;	// ����
		String lGrade = null;	// �꼶
		String lMajor = null;	// רҵ
		String lTel = null;		// �绰
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
 	* Purpose: 			���ݿ�Ĳ��뼴��Ӳ���
 	* Params : 			�޲�
 	* Return: 			boolean ����ֵ 	trueΪ�ɹ� 	false	 ʧ��
 	*******************************************************/
	private boolean addData()
	{
		String lId = null;		// ���֤��
		String lNname = null;	// ����
		String lNative = null;	// ����
		String lNation = null;	// ����
		String lGrade = null;	// �꼶
		String lMajor = null;	// רҵ
		String lTel = null;	// �绰
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
	* Purpose: 			���и�������¼�
	* Params : 			�޲�
	* Return: 			�޷���ֵ
	*******************************************************/
	private void dealAction()
	{
		
		/*���֤���������ļ����¼�*/
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
		
		/*���������ļ����¼�*/
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
		
		/*�ֻ����������ļ����¼�*/
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
		
		/*jbmtModify��ť�ļ����¼�*/
		jbtnModify.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if((jbtnModify.getText().equals("�޸�")))
					{
						stateAlter(EDIT);
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
						jtxtIdNumber.requestFocus();
						jtxtIdNumber.selectAll();
						operating = MODIFY;
						restoreIdName = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
					}
					else
					{
						initStudnetInfo(restoreIdName);	// ѧԱ��Ϣ�ָ�
						stateAlter(BROWSE);
						jbtnAdd.setText("���");
						jbtnModify.setText("�޸�");
					}
				}
			}
		);
		
		/*addModify��ť�ļ����¼�*/
		jbtnAdd.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if((jbtnAdd.getText()).equals("���"))
					{
						stateAlter(EDIT);
						jbtnAdd.setText("ȷ��");
						jbtnModify.setText("����");
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
									jbtnAdd.setText("���");
									jbtnModify.setText("�޸�");
								}
								else 
									JOptionPane.showMessageDialog(jfrmMain, "���ݿ��쳣��", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(operating == MODIFY && modifyIdNumberRepeateInpect())
								if(updataData())
								{
									initstudentBaseInfoList();
									jlstStuBaseInfo.setSelectedIndex(0);	
									stateAlter(BROWSE);
									jbtnAdd.setText("���");
									jbtnModify.setText("�޸�");
								}
								else
									JOptionPane.showMessageDialog(jfrmMain, "���ݿ��쳣��", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
						}
					}
				}
			}
		);
		
		/*���ڵĹرռ����¼�*/
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
					JOptionPane.showMessageDialog(jfrmMain, "�ټ�����ӭ�´ι���", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION);
				}
			}			
		);
		
		/*�رհ�ť�ļ����¼�*/
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(JOptionPane.showConfirmDialog(jfrmMain, "ȷ��Ҫ�˳���", "΢������ܰ��ʾ", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
						jfrmMain.dispose();
				}
			}
		);
		
		/*ѧԱ������Ϣ�б��ļ����¼�*/
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
	* Purpose: 			��ʼ��ָ��Ժϵ�µ�רҵ
	* Params : 			
	*	@String id		ָ��Ժϵ���
	* Return: 			�޷���ֵ
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
	* Purpose: 			��ʼ��ָ��ѧУ�µ�Ժϵ������ö�רҵ�ĳ�ʼ��
	* Params : 			
	*	@String id		ָ��ѧУ���
	* Return: 			�޷���ֵ
	*******************************************************/
	private void initDepartment(String id)
	{
		try 
		{
			// ��ʼ��Ժϵ
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
			// ��ʼ��רҵ
			
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
	* Purpose: 			��ʼ��ѧУ�б�����ö�Ժϵ�ĳ�ʼ��
	* Params : 			�޲�
	* Return: 			�޷���ֵ
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
	* Purpose: 			��ʼ�����ᡢ���塢�꼶��ѧУ��Ժϵ��רҵ�����б�
	* Params : 			�޲�
	* Return: 			�޷���ֵ
	*******************************************************/
	private void initStudentInfoSelectFrame()
	{
		try
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			
			/*��ʼ������*/
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
			
			/*��ʼ������*/
			String SQLnation = "SELECT NATIONNO,NATION FROM SYS_MEC_NATION ORDER BY NATIONNO";
			FResultSet frsNation = FStatement.executeQueryCon(SQLnation);
			jcmbNation.removeAllItems();
			while(frsNation.next())
			{
				String nationid = frsNation.getString("NATIONNO");
				String nationName = frsNation.getString("NATION");
				jcmbNation.addItem(nationid + " " + nationName);
			}
			
			/*��ʼ���꼶*/
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
			
			/*��ʼ��ѧУ*/
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
	* Purpose: 			��ʼ��ѧԱ��Ϣ�б�
	* Params : 			�޲�
	* Return: 			�޷���ֵ
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
			jlblStuSum.setText("��"+i+"ѧԱ��Ϣ");
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
	* Purpose: 			��ʼ��ָ�����֤�����ѧԱ��Ϣ
	* Params : 			
	* 	@String 	idNo ���֤����	
	* Return: 			�޷���ֵ
	*******************************************************/
	private void initStudnetInfo(String idNo) 
	{
		String SQLinfo = "SELECT LEARNERID,LEARNERNAME,LEARNERNATIVE,LEARNERNATION,LEARNERGRADE,LEARNERMAJOR,LEARNERTEL FROM SYS_MEC_LEARNER WHERE LEARNERID = '" + idNo + "' ORDER BY LEARNERID";
		String lId = null;		// ���֤��
		String lNname = null;	// ����
		String lNative = null;	// ����
		String lNation = null;	// ����
		String lGrade = null;	// �꼶
		String lMajor = null;	// רҵ
		String lTel = null;	// �绰
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

			int i = 0;	// ����Ӧ����Ķ�Ӧ���±�
			while(i < jcmbNative.getItemCount())
			{	
				if(jcmbNative.getItemAt(i).toString().substring(0, 2).equals(lNative))
					jcmbNative.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// ����Ӧ����Ķ�Ӧ���±�
			while(i < jcmbNation.getItemCount())
			{	
				if(jcmbNation.getItemAt(i).toString().substring(0, 2).equals(lNation))
					jcmbNation.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// ����Ӧ�꼶�Ķ�Ӧ���±�
			while(i < jcmbGrade.getItemCount())
			{	
				if(jcmbGrade.getItemAt(i).toString().substring(0, 2).equals(lGrade))
					jcmbGrade.setSelectedIndex(i);
				i++;
			}

			i = 0;		// ����ӦѧԺ�Ķ�Ӧ���±�
			while(i < jcmbSchool.getItemCount())
			{
				if(jcmbSchool.getItemAt(i).toString().substring(0, 2).equals(lMajor.substring(0, 2)))
					jcmbSchool.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// ����ӦԺϵ�Ķ�Ӧ���±�
			while(i < jcmbDepartment.getItemCount())
			{
				if(jcmbDepartment.getItemAt(i).toString().substring(0, 4).equals(lMajor.substring(0, 4)))
					jcmbDepartment.setSelectedIndex(i);
				i++;
			}
			
			i = 0;		// ����Ӧרҵ�Ķ�Ӧ���±�
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
	* Purpose: 			��ʼ��ѧԱ�ĸ�����Ϣ
	* Params : 			�޲�
	* Return: 			�޷���ֵ
	*******************************************************/
	private void reinitFrame() 
	{
		initstudentBaseInfoList();
		initStudentInfoSelectFrame();
		
		String idNo = jlstStuBaseInfo.getSelectedValue().toString().substring(0, 0+18);
		
		/*ѧУ�б��ļ����¼�*/
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

		/*Ժϵ�б��ļ����¼�*/
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
	* Purpose: 			��ʼ�����ڽ���
	* Params : 			�޲�
	* Return: 			�޷���ֵ
	*******************************************************/
	private void initFrame() 
	{
		Dimension dim;
		Font normalFont = new Font("����", Font.PLAIN, 16);
				
		jfrmMain = new JFrame("ѧԱ������Ϣ����");
		container = jfrmMain.getContentPane();
		container.setLayout(null);
		jfrmMain.setSize(7725/15, 7425/15);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width-jfrmMain.getWidth())/2, (dim.height-jfrmMain.getHeight())/2);
		
		jlblTopic = new JLabel("ѧԱ������Ϣ����");
		jlblTopic.setFont(new Font("΢���ź�", Font.PLAIN, 24));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(2392/15, 0, 2880/15, 465/15);
		container.add(jlblTopic);
		
		dlmStuBaseInfo = new DefaultListModel();
		jlstStuBaseInfo = new JList(dlmStuBaseInfo);
		jscpStuBaseInfo = new JScrollPane(jlstStuBaseInfo);
		jscpStuBaseInfo.setBorder(BorderFactory.createTitledBorder("ѧԱ������Ϣ"));
		jlstStuBaseInfo.setFont(normalFont);
		jscpStuBaseInfo.setBounds(135/15, 675/15, 3600/15, 4935/15);
		container.add(jscpStuBaseInfo);
		
		jlblIdNumber = new JLabel("���֤��");
		jlblIdNumber.setFont(normalFont);
		jlblIdNumber.setBounds(4080/15, 675/15, 960/15, 240/15);
		container.add(jlblIdNumber);
		jtxtIdNumber = new JTextField();
		jtxtIdNumber.setFont(normalFont);
		jtxtIdNumber.setBounds(5160/15, 675/15, 2290/15, 360/15);
		container.add(jtxtIdNumber);

		jlblName = new JLabel("�ա�����");
		jlblName.setFont(normalFont);
		jlblName.setBounds(4080/15, 1297/15, 960/15, 240/15);
		container.add(jlblName);
		jtxtName = new JTextField();
		jtxtName.setFont(normalFont);
		jtxtName.setBounds(5160/15, 1297/15, 2290/15, 360/15);
		container.add(jtxtName);
		
		jlblNative = new JLabel("��������");
		jlblNative.setFont(normalFont);
		jlblNative.setBounds(4080/15, 1919/15, 960/15, 240/15);
		container.add(jlblNative);
		jcmbNative = new JComboBox();
		jcmbNative.setFont(normalFont);
		jcmbNative.setBounds(5160/15, 1905/15, 2290/15, 360/15);
		container.add(jcmbNative);

		jlblNation = new JLabel("�񡡡���");
		jlblNation.setFont(normalFont);
		jlblNation.setBounds(4080/15, 2541/15, 960/15, 240/15);
		container.add(jlblNation);
		jcmbNation = new JComboBox();
		jcmbNation.setFont(normalFont);
		jcmbNation.setBounds(5160/15, 2520/15, 2290/15, 360/15);
		container.add(jcmbNation);

		jlblGrade = new JLabel("�ꡡ����");
		jlblGrade.setFont(normalFont);
		jlblGrade.setBounds(4080/15, 3163/15, 960/15, 240/15);
		container.add(jlblGrade);
		jcmbGrade = new JComboBox();
		jcmbGrade.setFont(normalFont);
		jcmbGrade.setBounds(5160/15, 3120/15, 2290/15, 360/15);
		container.add(jcmbGrade);

		jlblSchool = new JLabel("ѧ����У");
		jlblSchool.setFont(normalFont);
		jlblSchool.setBounds(4080/15, 3785/15, 960/15, 240/15);
		container.add(jlblSchool);
		jcmbSchool = new JComboBox();
		jcmbSchool.setFont(normalFont);
		jcmbSchool.setBounds(5160/15, 3750/15, 2290/15, 360/15);
		container.add(jcmbSchool);

		jlblDepartment = new JLabel("Ժ����ϵ");
		jlblDepartment.setFont(normalFont);
		jlblDepartment.setBounds(4080/15, 4407/15, 960/15, 240/15);
		container.add(jlblDepartment);
		jcmbDepartment = new JComboBox();
		jcmbDepartment.setFont(normalFont);
		jcmbDepartment.setBounds(5160/15, 4365/15, 2290/15, 360/15);
		container.add(jcmbDepartment);
		
		jlblMajor = new JLabel("ר����ҵ");
		jlblMajor.setFont(normalFont);
		jlblMajor.setBounds(4080/15, 5029/15, 960/15, 240/15);
		container.add(jlblMajor);
		jcmbMajor = new JComboBox();
		jcmbMajor.setFont(normalFont);
		jcmbMajor.setBounds(5160/15, 4980/15, 2290/15, 360/15);
		container.add(jcmbMajor);

		jlblStuSum = new JLabel("��*��ѧԱ��Ϣ");
		jlblStuSum.setFont(normalFont);
		jlblStuSum.setBounds(135/15, 5690/15, 3600/15, 255/15);
		container.add(jlblStuSum);
		
		jlblPhone = new JLabel("�ֻ�����");
		jlblPhone.setFont(normalFont);
		jlblPhone.setBounds(4080/15, 5655/15, 960/15, 240/15);
		container.add(jlblPhone);
		jtxtPhone = new JTextField();
		jtxtPhone.setFont(normalFont);
		jtxtPhone.setBounds(5160/15, 5565/15, 2290/15, 360/15);
		container.add(jtxtPhone);
		
		jbtnAdd = new JButton("���");
		jbtnAdd.setFont(normalFont);
		jbtnAdd.setBounds(240/15, 6240/15, 1215/15, 465/15);
		container.add(jbtnAdd);
		
		jbtnModify = new JButton("�޸�");
		jbtnModify.setFont(normalFont);
		jbtnModify.setBounds(1440/15, 6240/15, 1215/15, 465/15);
		container.add(jbtnModify);

		jbtnExit = new JButton("�˳�");
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