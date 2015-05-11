import javax.swing.JList;
import javax.swing.ListModel;

public class SubjectData extends BaseData
{
	protected String id;
	protected String name;
	
	public SubjectData(String id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}
	
	public String getId()
	{
		return id;
	}

	public String toString() 
	{ 
		return id + " " + name;
	} 
	
	private int getSelectedCountExceptFirst(ListModel lm, SubjectData data, int subjectCount)
	{
		int selectedCount = 0;
		
		for(int i = 1; i < subjectCount; i++)
		{
			data = (SubjectData)lm.getElementAt(i);
			if(data.isSelected())
				selectedCount++;
		}
		
		return selectedCount;
	}
	
	private void unselectedOtherExceptFirst(ListModel lm, SubjectData data, int subjectCount)
	{
		for(int i = 1; i < subjectCount; i++)
		{
			data = (SubjectData)lm.getElementAt(i);
			data.setSelected(false);
		}
	}
	
	public void checkSelected(JList list)
	{
		ListModel lm = list.getModel();
		SubjectData data;
		int subjectCount = lm.getSize();
		
		if(subjectCount > 0)
		{
			data = (SubjectData)lm.getElementAt(0);
			int selectedIndex = list.getSelectedIndex();
			if(selectedIndex != -1)
			{
				if(selectedIndex == 0 && data.isSelected())
					unselectedOtherExceptFirst(lm, data, subjectCount);
				else if(getSelectedCountExceptFirst(lm, data, subjectCount) == 0)
					data.setSelected(true);
				else
					data.setSelected(false);
			}
		}
	}
}
