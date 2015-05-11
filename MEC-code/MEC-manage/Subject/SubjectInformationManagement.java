import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class SubjectInformationManagement
{
	private JFrame jfrmMain;
	private Container con;
	
	private JLabel jlblTopic;
	
	private JLabel jlblSubject;
	private JScrollPane jscpSubject;
	private JList jlstSubject;
	private DefaultListModel dlmSubject;
	
	private JLabel jlblSubjectRelation;
	private JScrollPane jscpSubjectRelation;
	private JList jlstSubjectRelation;
	private DefaultListModel dlmSubjectRelation;
	
	private JLabel jlblSubjectIdValue;
	private JLabel jlblSubjectId;
	
	private JLabel jlblSubjectName;
	private JTextField jtxtSubjectName;
	
	private JRadioButton jrdbStatus[];
	private ButtonGroup bgStatus;
	
	private JButton jbtnExit;
	private JButton jbtnModify;
	private JButton jbtnAdd;
	
	private CheckListCellRenderer clcrSubjectRelationList;
	
	private static final boolean BROWSE = true;
	private static final boolean EDIT = false;
	
	private static final boolean STATUSADD  =true;
	private static final boolean STATUSMODIFY = false;
	
	private boolean identification = false;
	
	private String notes = null;
	
	private ArrayList<String> alOld = new ArrayList<String>();
	
	private boolean status;

	public SubjectInformationManagement()
	{
		initFrame();
		dealAction();
		reinitFrame();
	}
	
	private boolean ModifyData()
	{	
		// 科目修改
		String id = jlblSubjectIdValue.getText();
		String name = jtxtSubjectName.getText();
		String state = "0";
		if(jrdbStatus[0].isSelected() == true)
			state = "1";
		
		String SQLUpdate = "UPDATE SYS_SUBJECT_INFORMATION SET SUBJECTNAME = '"+ name +"', SUBJECTVIDEO = '"+ state +"' WHERE SUBJECTID = '"+ id +"'";
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLUpdate);
			FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}

		// 关系建立
		ArrayList<String> alNew = new ArrayList<String>();
		
		for(int i = 0; i < dlmSubjectRelation.getSize(); i++)
		{
			SubjectData data = (SubjectData)dlmSubjectRelation.getElementAt(i);
			if(data.isSelected() == true)
					alNew.add(dlmSubjectRelation.elementAt(i).toString().substring(0, 3));
		}
		
		try 
		{		
			String did = jlblSubjectIdValue.getText();
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			
			int i = 0, j = 0;
			while(i < alNew.size() && j < alOld.size())
			{
				if(Integer.parseInt(alNew.get(i)) > Integer.parseInt(alOld.get(j)))
				{
					String SQLAdd = "DELETE SYS_SUBJECT_RELATION WHERE SUBJECTID = '"+did+"'AND SUBSUBJECTID = '"+alOld.get(j)+"'";
					FStatement.executeUpdateCon(SQLAdd);
					j++;
				}
				else if(Integer.parseInt(alNew.get(i)) < Integer.parseInt(alOld.get(j)))
				{
					String SQLAdd = "INSERT INTO SYS_SUBJECT_RELATION VALUES ('"+did+"','"+alNew.get(i)+"')";
					FStatement.executeUpdateCon(SQLAdd);
					i++;
				}
				else 
				{
					i++;
					j++;
				}
			}
			while(i < alNew.size())
			{	
				String SQLAdd = "INSERT INTO SYS_SUBJECT_RELATION VALUES ('"+did+"','"+alNew.get(i)+"')";
				FStatement.executeUpdateCon(SQLAdd);
				i++;
			}
			while(j < alOld.size())
			{
				String SQLAdd = "DELETE SYS_SUBJECT_RELATION WHERE SUBJECTID = '"+did+"'AND SUBSUBJECTID = '"+alOld.get(j)+"'";
				FStatement.executeUpdateCon(SQLAdd);
				j++;
			}
			
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
		
		// 笨方法
		/*		
 		* try 
		{		
			String did = jlblSubjectIdValue.getText();
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			
			for(int i=0; i < alOld.size(); i++)
			{
				String SQLAdd = "DELETE SYS_SUBJECT_RELATION WHERE SUBJECTID = '"+did+"'AND SUBSUBJECTID = '"+alOld.get(i)+"'";
				FStatement.executeUpdateCon(SQLAdd);
			}
			for(int i=0; i < alNew.size(); i++)
			{
				String SQLAdd = "INSERT INTO SYS_SUBJECT_RELATION VALUES ('"+did+"','"+alNew.get(i)+"')";
				FStatement.executeUpdateCon(SQLAdd);
			}
			
			FStatement.disConnection();
			initSubjectList();
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
		*/
	}
	

	private boolean AddData()
	{
		String id = (dlmSubject.getSize()+1001+"").substring(1, 1+3);
		String name = jtxtSubjectName.getText();
		String state = "0";
		if(jrdbStatus[0].isSelected() == true)
			state = "1";

		String SQLAdd = "INSERT INTO SYS_SUBJECT_INFORMATION VALUES('"+id+"', '"+name+"', '"+state+"')";
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FStatement.executeUpdateCon(SQLAdd);
			FStatement.disConnection();
			initSubjectList();
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
	

	private void status(boolean status)
	{
		jlstSubject.setEnabled(status);

		jlstSubjectRelation.setEnabled(!status);
		jrdbStatus[0].setEnabled(!status);
		jrdbStatus[1].setEnabled(!status);
		jtxtSubjectName.setEditable(!status);
	}


	private void initSubjectInformation(String id)
	{
		String SQLID1 = "SELECT SUBJECTID, SUBJECTNAME, SUBJECTVIDEO FROM SYS_SUBJECT_INFORMATION WHERE SUBJECTID = '"+ id +"'";
		String SQLID2 = "SELECT SUBSUBJECTID FROM SYS_SUBJECT_RELATION WHERE SUBJECTID ='"+id+"' ORDER BY SUBSUBJECTID";
		
		for(int i = 0; i < dlmSubjectRelation.getSize(); i++)
		{	
				SubjectData data = (SubjectData)dlmSubjectRelation.getElementAt(i);
				data.setSelected(false);
		}
		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet fs1 = FStatement.executeQueryCon(SQLID1);
			if(fs1.next())
			{
				jlblSubjectIdValue.setText(fs1.getString("SUBJECTID"));
				jtxtSubjectName.setText(fs1.getString("SUBJECTNAME"));
				if(fs1.getString("SUBJECTVIDEO").equals("1"))
					jrdbStatus[0].setSelected(true);
				else
					jrdbStatus[1].setSelected(true);
					
			}

			FResultSet fs2 = FStatement.executeQueryCon(SQLID2);
			String idNo = null;
			alOld.clear();
			int j = 0;;
			while(fs2.next())
			{
				idNo = fs2.getString("SUBSUBJECTID");
				alOld.add(idNo);	// 保存数据库原有数据
				for(int i = 0; i < dlmSubjectRelation.getSize(); i++)
				{	
					if(dlmSubjectRelation.getElementAt(i).toString().substring(0, 3).equals(idNo))
					{
						SubjectData data = (SubjectData)dlmSubjectRelation.getElementAt(i);
						data.setSelected(true);
					}
				}
				j++;
			}
			jlstSubjectRelation.repaint();
			FStatement.disConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	

	private void reinitFrame()
	{
		jlstSubjectRelation.setSelectionBackground(Color.WHITE);
		clcrSubjectRelationList = new CheckListCellRenderer();
		jlstSubjectRelation.setCellRenderer(clcrSubjectRelationList);
		
		initSubjectList();
		initSubjectInformation(jlstSubject.getSelectedValue().toString().substring(0, 3));
	}


	private void initSubjectList() 
	{
		String SQLID2 = " SELECT SUBJECTID, SUBJECTNAME FROM SYS_SUBJECT_INFORMATION ORDER BY SUBJECTID";
		try 
		{
			identification = true;
			dlmSubject.removeAllElements();
			dlmSubjectRelation.removeAllElements();
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecdb", "mec_prog_user", "654321");
			FResultSet fs = FStatement.executeQueryCon(SQLID2);
			while(fs.next())
			{
				dlmSubject.addElement(fs.getString("SUBJECTID")+" "+fs.getString("SUBJECTNAME"));
				dlmSubjectRelation.addElement(new SubjectData(fs.getString("SUBJECTID"), fs.getString("SUBJECTNAME")));
			}
			FStatement.disConnection();
			jlstSubjectRelation.repaint();
			jlstSubject.setSelectedIndex(0);
			identification = false;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void dealAction()
	{
		// jtxtSubjectName 监听事件
		jtxtSubjectName.addKeyListener
		(
			new KeyListener()
			{

				@Override
				public void keyPressed(KeyEvent arg0) 
				{
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER 
							&& jtxtSubjectName.getText().length()>2 
							&& jtxtSubjectName.getText().length()<20)
					{
						if((jbtnAdd.getText().equals("添加")))
						{
							status(EDIT);
							jbtnAdd.setText("确定");
							jbtnModify.setText("放弃");
							jlblSubjectIdValue.setText("");
							jtxtSubjectName.setText("");
							jrdbStatus[0].setSelected(true);
							jlstSubjectRelation.setEnabled(false);
							status = STATUSADD;
							notes = jlstSubject.getSelectedValue().toString().substring(0, 3);
						}
						else
							if(status == STATUSADD)		// 添加的确定
								if(AddData())	// 添加确定成功
								{
									initSubjectInformation(notes);	
									status(BROWSE);
									jbtnAdd.setText("添加");
									jbtnModify.setText("修改");
									initSubjectList();
									jlstSubject.setSelectedIndex(Integer.parseInt(notes));
								}
								else		// 确定失败
									JOptionPane.showMessageDialog(jfrmMain, "添加异常");	
							else						// 修改的确定
								if(ModifyData())	// 添加确定成功
								{
									initSubjectInformation(notes);	
									status(BROWSE);
									jbtnAdd.setText("添加");
									jbtnModify.setText("修改");
								}
								else		// 确定失败
									JOptionPane.showMessageDialog(jfrmMain, "修改异常");	
						jbtnAdd.requestFocus();
					}
				}
				public void keyReleased(KeyEvent arg0) {}
				public void keyTyped(KeyEvent arg0) {}			
			}
		);
		
		jtxtSubjectName.addCaretListener
		(
			new CaretListener()
			{
				public void caretUpdate(CaretEvent arg0) 
				{
					if(jtxtSubjectName.getText().length()>2 && jtxtSubjectName.getText().length()<20)
						jbtnAdd.setEnabled(true);
					else
						jbtnAdd.setEnabled(false);
				}
			}
		);
		
		// modify按钮的监听
		jbtnModify.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					if((jbtnModify.getText().equals("修改")))
					{
						status(EDIT);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						jrdbStatus[0].setSelected(true);
						
						status = STATUSMODIFY;
						notes = jlstSubject.getSelectedValue().toString().substring(0, 3);
					}
					else
					{
						initSubjectInformation(notes);	
						status(BROWSE);
						jbtnAdd.setText("添加");
						jbtnModify.setText("修改");
					}
				}
			}
		);
		
		// add按钮的监听
		jbtnAdd.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if((jbtnAdd.getText().equals("添加")))
					{
						status(EDIT);
						jbtnAdd.setText("确定");
						jbtnModify.setText("放弃");
						jlblSubjectIdValue.setText("");
						jtxtSubjectName.setText("");
						jtxtSubjectName.requestFocus();
						jrdbStatus[0].setSelected(true);
						jlstSubjectRelation.setEnabled(false);
						status = STATUSADD;
						notes = jlstSubject.getSelectedValue().toString().substring(0, 3);
					}
					else
						if(status == STATUSADD)		// 添加的确定
							if(AddData())	// 添加确定成功
							{
								initSubjectInformation(notes);	
								status(BROWSE);
								jbtnAdd.setText("添加");
								jbtnModify.setText("修改");
								initSubjectList();
								jlstSubject.setSelectedIndex(Integer.parseInt(notes));
							}
							else		// 确定失败
								JOptionPane.showMessageDialog(jfrmMain, "添加异常");	
						else						// 修改的确定
							if(ModifyData())	// 添加确定成功
							{
								initSubjectInformation(notes);	
								status(BROWSE);
								jbtnAdd.setText("添加");
								jbtnModify.setText("修改");
							}
							else		// 确定失败
								JOptionPane.showMessageDialog(jfrmMain, "修改异常");	
					
							
				}
			}
		);
		
		// 科目列表监听
		jlstSubject.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0) 
				{
					if(identification == false)
					initSubjectInformation(jlstSubject.getSelectedValue().toString().substring(0, 3));
				}
			}
		);
		
		// 关系列表监听
		jlstSubjectRelation.addMouseListener
		(
			new MouseListener()
			{
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
				
				public void mouseClicked(MouseEvent arg0) 
				{
					if(jlstSubjectRelation.isEnabled())
					{
						SubjectData data = (SubjectData)jlstSubjectRelation.getSelectedValue();
						data.invertSelected();
						jlstSubjectRelation.repaint();
					}
				}
			}
		);

		// 退出监听
		jbtnExit.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(JOptionPane.showConfirmDialog(jfrmMain, "确认退出？", "微易码温馨提示", 0) == JOptionPane.OK_OPTION)
						jfrmMain.dispose();
				}
			}
		);
	}
	

	private void initFrame()
	{
		Dimension dim;
		
		jfrmMain = new JFrame("科目信息管理");
		jfrmMain.setResizable(false);
		con = jfrmMain.getContentPane();
		con.setLayout(null);
		
		jrdbStatus = new JRadioButton[2];

		jrdbStatus[0] = new JRadioButton("有视频");
		jrdbStatus[1] = new JRadioButton("无视频");
		jrdbStatus[0].setFont(new Font("微软雅黑",Font.PLAIN,20));
		jrdbStatus[1].setFont(new Font("微软雅黑",Font.PLAIN,20));
		jrdbStatus[0].setBounds(6120/15, 7320/15, 1300/15, 375/15);
		jrdbStatus[1].setBounds(7440/15, 7320/15, 1300/15, 375/15);
		con.add(jrdbStatus[0]);
		con.add(jrdbStatus[1]);
		
		bgStatus = new ButtonGroup();
		bgStatus.add(jrdbStatus[0]);
		bgStatus.add(jrdbStatus[1]);
		
		jfrmMain.setSize(715,621);
		jfrmMain.setFont(new Font("宋体",Font.PLAIN,16));
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation((dim.width - jfrmMain.getWidth())/2, (dim.height - jfrmMain.getHeight())/2);
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		dlmSubjectRelation = new DefaultListModel();
		jlstSubjectRelation = new JList(dlmSubjectRelation);
		jlstSubjectRelation.setFont(new Font("宋体",Font.PLAIN,14));
		jscpSubjectRelation = new JScrollPane(jlstSubjectRelation);
		jscpSubjectRelation.setBounds(408,96,257,340);
		con.add(jscpSubjectRelation);


		jtxtSubjectName = new JTextField();
		jtxtSubjectName.setFont(new Font("宋体",Font.PLAIN,14));
		jtxtSubjectName.setBounds(136,488,193,25);
		con.add(jtxtSubjectName);


		jbtnExit = new JButton("退出");
		jbtnExit.setFont(new Font("微软雅黑",Font.PLAIN,15));
		jbtnExit.setBounds(600,544,65,33);
		con.add(jbtnExit);


		jbtnModify = new JButton("修改");
		jbtnModify.setFont(new Font("微软雅黑",Font.PLAIN,15));
		jbtnModify.setBounds(520,544,65,33);
		con.add(jbtnModify);


		jbtnAdd = new JButton("添加");
		jbtnAdd.setFont(new Font("微软雅黑",Font.PLAIN,15));
		jbtnAdd.setBounds(440,544,65,33);
		con.add(jbtnAdd);


		dlmSubject = new DefaultListModel();
		jlstSubject = new JList(dlmSubject);
		jlstSubject.setFont(new Font("宋体",Font.PLAIN,14));
		jscpSubject = new JScrollPane(jlstSubject);
		jscpSubject.setBounds(40,96,257,340);
		con.add(jscpSubject);


		jlblSubjectIdValue = new JLabel();
		jlblSubjectIdValue.setFont(new Font("宋体",Font.PLAIN,14));
		jlblSubjectIdValue.setBounds(136,448,193,25);
		con.add(jlblSubjectIdValue);


		jlblSubjectId = new JLabel("科目编号");
		jlblSubjectId.setFont(new Font("微软雅黑",Font.PLAIN,20));
		jlblSubjectId.setBounds(40,448,81,25);
		con.add(jlblSubjectId);


		jlblSubjectName = new JLabel("科目名称");
		jlblSubjectName.setFont(new Font("微软雅黑",Font.PLAIN,20));
		jlblSubjectName.setBounds(40,488,81,25);
		con.add(jlblSubjectName);


		jlblSubjectRelation = new JLabel("科目关系");
		jlblSubjectRelation.setFont(new Font("微软雅黑",Font.PLAIN,20));
		jlblSubjectRelation.setBounds(496,56,81,33);
		con.add(jlblSubjectRelation);


		jlblSubject = new JLabel("科目列表");
		jlblSubject.setFont(new Font("微软雅黑",Font.PLAIN,20));
		jlblSubject.setBounds(128,56,81,33);
		con.add(jlblSubject);


		jlblTopic = new JLabel("科目信息管理");
		jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,32));
		jlblTopic.setBounds(257,0,193,49);
		con.add(jlblTopic);

		status(BROWSE);
		
		jfrmMain.setVisible(true);
	}


	public static void main(String[] args)
	{
		new SubjectInformationManagement();
	}
}