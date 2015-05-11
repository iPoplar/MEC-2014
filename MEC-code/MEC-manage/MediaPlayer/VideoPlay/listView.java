import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class listView extends JLabel  implements ListCellRenderer
{		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8212339261079565965L;
	private static final Color HIGHLIGHT_COLOR = new Color(68,122,210);

	public listView()
	{
		super();
		setOpaque(true);
		setIconTextGap(5);		
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) 
	{
		// TODO Auto-generated method stub
		listIcon item = (listIcon)value;
		this.setIcon(item.getIcon());
		this.setText(item.getText());
		
		if(isSelected)
		{
			setBackground(HIGHLIGHT_COLOR);
			setForeground(Color.white);
		}
		else
		{
			setBackground(Color.white);
			setForeground(Color.black);
		}
		
		return this;
	}

}
