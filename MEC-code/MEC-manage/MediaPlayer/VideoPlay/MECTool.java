import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MECTool 
{
	public static void showMessage(JFrame jfrm, String mess)
	{
		JOptionPane.showMessageDialog(jfrm, mess, "微易码温馨提示", JOptionPane.DEFAULT_OPTION);
	}
	
	public static int getUserChoice(JFrame jfrm, String mess, int type)
	{
		return JOptionPane.showConfirmDialog(jfrm, mess, "微易码温馨提示", type);
	}
	
	public static String showInputDialog(JFrame jfrm)
	{
		return JOptionPane.showInputDialog(null,"请输入密码： \n","");
	}
}
