/*import java.awt.Dialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MECTool
{
	public static void showMessage(JFrame jframe, String mess)
	{
		JOptionPane.showMessageDialog(jframe, mess, "���Ǿ���", Dialog.ERROR);
		
	}
	
}*/

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MECTool 
{
	public static void showMessage(JFrame jfrm, String mess)
	{
		JOptionPane.showMessageDialog(jfrm, mess, "΢������ܰ��ʾ", JOptionPane.DEFAULT_OPTION);
	}
	
	public static int getUserChoice(JFrame jfrm, String mess, int type)
	{
		return JOptionPane.showConfirmDialog(jfrm, mess, "΢������ܰ��ʾ", type);
	}
}
