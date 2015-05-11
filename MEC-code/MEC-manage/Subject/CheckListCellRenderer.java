import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class CheckListCellRenderer extends JCheckBox implements ListCellRenderer 
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6128649863025729508L;
	protected static Border m_noFocusBorder = new EmptyBorder(1, 1, 1, 1); 

	public CheckListCellRenderer() 
	{ 
		super(); 
		setOpaque(true); 
		setBorder(m_noFocusBorder); 
	} 

	public Component getListCellRendererComponent(JList list, Object value, 
			int index, boolean isSelected, boolean cellHasFocus) 
	{ 
		setText(value.toString()); 

		BaseData data = (BaseData) value; 
		setSelected(data.isSelected()); 
		setToolTipText(data.getHelpString());

		setBackground(isSelected ? list.getSelectionBackground() : data.getBackground());
		if(list.isEnabled())
			setForeground(isSelected ? list.getSelectionForeground() : data.getForeground());
		else
			setForeground(Color.LIGHT_GRAY); 
		
		setFont(list.getFont()); 
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : m_noFocusBorder);

		return this; 
	} 
} 
