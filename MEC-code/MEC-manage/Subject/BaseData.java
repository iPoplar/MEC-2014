import java.awt.Color;

import javax.swing.JList;
import javax.swing.ListModel;

public class BaseData
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -556982494635738262L;
	protected String helpString;
	protected Color background;
	protected Color foreground;
	protected boolean selected; 

	public BaseData(String helpString)
	{
		this.helpString = helpString;
		selected = false;
	}
	
	public BaseData() 
	{ 
		this(null);
	} 
	
	public Color getBackground()
	{
		return background;
	}
	
	public Color getForeground()
	{
		return foreground;
	}
	
	public void setBackground(Color background)
	{
		this.background = background;
	}
	
	public void setForeground(Color foreground)
	{
		this.foreground = foreground;
	}
	
	public String getHelpString()
	{
		return helpString;
	}
	
	public void setHelpString(String helpString)
	{
		if(helpString != null)
			this.helpString = helpString;
		else
			this.helpString = "";
	}

	public void setSelected(boolean selected) 
	{ 
		this.selected = selected; 
	} 

	public void invertSelected() 
	{ 
		selected = !selected; 
	} 

	public boolean isSelected() 
	{ 
		return selected; 
	} 
	
	public int getSelectedCount(JList list)
	{
		int cnt = 0;
		int i;
		ListModel model = list.getModel();
		
		for(i = 0; i < model.getSize(); i++)
		{
			BaseData baseData = (BaseData)model.getElementAt(i);
			if(baseData.isSelected())
				cnt++;
		}
		
		return cnt;
	}
}
