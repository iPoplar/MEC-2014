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
	
	public static String showInputDialog(JFrame jfrm)
	{
		return JOptionPane.showInputDialog(null,"���������룺 \n","");
	}
}
