import javax.swing.Icon;
import javax.swing.ImageIcon;

public class listIcon 
{
	Icon icon;
	String text;
	
	public listIcon(String icon, String text)
	{
		//this.icon = new ImageIcon(icon);								//����·��	
		this.icon = new ImageIcon(this.getClass().getResource(icon));	//���·��
		this.text = text;
	}
	
	public Icon getIcon()
	{
		System.out.println(icon);
		return icon;
	}
	
	public String getText()
	{
		System.out.println(text);
		return text;
	}
}