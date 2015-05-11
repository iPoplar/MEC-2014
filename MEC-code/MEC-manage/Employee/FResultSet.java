import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FResultSet 
{
	private static String[][] arrayString;
	private static int place = 0;
	private static final int BEGAIN_PLACE = 1;
	
	private static final String[][]typeTable = {
		{"CHAR", "String"},
		{"VARCHAR2", "String"},
		{"LONG", "String"},
		{"NUMBER", "BigDecimal"},
		{"RAW", "byte[]"},
		{"LONGRAW", "byte[]"},
		{"DATE", "Date"},
		{"TIMESTAMP", "Timestamp"},
		{"INTEGER", "int"},
		{"FLOAT", "float"},
		{"REAL", "float"},
		{"BIGINT", "long"},
		{"BIT", "boolean"}
	};
	
	public FResultSet(String[][] temp) throws SQLException
	{
		arrayString = new String[temp.length][temp[1].length];
		
		for(int i = 0; i < temp.length; i++)
		{
			for(int j = 0; j < temp[1].length; j++)
			{
				arrayString[i][j] = temp[i][j];
			}
		}//复制数组
		
		dealType();//处理类型转换
		beforFirst();
	}
	
	public String getTypeName(String recordName)
	{
		String StringType = null;
		for(int i = 0; i < arrayString[0].length; i++)
		{
			if(arrayString[0][i].equalsIgnoreCase(recordName))
				StringType = arrayString[1][i];
		}
		
		return StringType;
	}
	
	private String changeType(String typeName)//改变数据类型名称
	{
		int i;
		String name = null;
		
		for(i = 0; i < typeTable.length && !typeTable[i][0].equalsIgnoreCase(typeName); i++)
		;
		if(i < typeTable.length)
		{
			name = typeTable[i][1];
		}
		
		return name;
	}
	
	private void dealType()//处理数据类型名
	{
		for(int i = 0; i < arrayString[0].length; i++)
		{
			arrayString[1][i] = changeType(arrayString[1][i]);
		}
	}
	
	public int getPlace()
	{
		return place;
	}
	
	public boolean next() throws SQLException
	{
		return (++place < arrayString.length);
	}
	
	private void setPlace(int newPlace) throws SQLException
	{
		place = newPlace;
	}
	
	public void beforFirst() throws SQLException
	{
		setPlace(BEGAIN_PLACE);
	}
	
	public void afterLast() throws SQLException
	{
		setPlace(arrayString.length);
	}
	
	public void first() throws SQLException
	{
		setPlace(BEGAIN_PLACE+1);
	}
	
	public void last() throws SQLException
	{
		setPlace(arrayString.length-1);
	}
	
	public int getRow() throws SQLException
	{
		return arrayString.length-2;
	}
	
	public void close() throws SQLException
	{
		//待处理
	}
	
	public boolean isClose() throws SQLException
	{
		return false;
	}
	
	private String dealMess(String nameString) throws SQLException
	{
		int crow = -1;
		String mess = null;
		
		for(int i = 0; i < arrayString[0].length; i++)
		{
			if(arrayString[0][i].equalsIgnoreCase(nameString))
			{
				crow = i;		//查找与用户提供的字段名相同的那一列
			}
		}
		
		if(crow != -1 && place != -1 && place != arrayString.length)
		{
			//处理语句
			mess = arrayString[place][crow];
		}
		else
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"未查询与【" + nameString +"】到相关信息");

		return mess;
	}
	
	public String getString(String nameString) throws SQLException
	{
		String mess = dealMess(nameString);
		
		return mess;
	}
	
	public String getString(int index) throws SQLException
	{
		//String mess = arrayString[place][index];
		String mess = getString(arrayString[0][index]);
		
		return mess;
	}

	public int getInt(String nameString) throws SQLException
	{
		int mess = Integer.parseInt(dealMess(nameString));
		
		return mess;
	}
	
	public int getInt(int index) throws SQLException
	{
		int mess = getInt(arrayString[0][index]);
		
		return mess;
	}
	
	public boolean getBoolean(String nameString) throws SQLException
	{
		String str = dealMess(nameString);
		
		return str.equals(true); 
	}
	
	public boolean getBoolean(int index) throws SQLException
	{
		return getBoolean(arrayString[0][index]);
	}
	
	public Date getDate(String nameString) throws SQLException
	{
		Date date = null;
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(dealMess(nameString));
		} catch (ParseException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class:ResultSet, Method:getDate(),获取日期信息异常！");
		}
		
		return date;
	}
	
	public Date getDate(int index) throws SQLException
	{
		return getDate(arrayString[0][index]);
	}
	
	public Time getTime(String nameString) throws SQLException
	{
		Time time = null;
		
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
			time = (Time) formatter.parse(nameString);
		} catch (ParseException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class:ResultSet, Method:getTime(),获取时间信息异常！");
		}
		
		return time;
	}
	
	public Time getTime(int index) throws SQLException
	{
		return getTime(arrayString[0][index]);
	}
	
	public Double getDouble(String nameString) throws SQLException
	{
		return Double.parseDouble(dealMess(nameString));
	}
	
	public Double getDouble(int index) throws SQLException
	{
		return getDouble(arrayString[0][index]);
	}
	
	public Float getFloat(String nameString) throws SQLException
	{
		return Float.parseFloat(dealMess(nameString));
	}
	
	public Float getFloat(int index) throws SQLException
	{
		return getFloat(arrayString[0][index]);
	}
	
	public Long getLong(String nameString) throws SQLException
	{
		return Long.parseLong(dealMess(nameString));
	}
	
	public Long getLong(int index) throws SQLException
	{
		return getLong(arrayString[0][index]);
	}
	
	public boolean isBeforeFirst() throws SQLException
	{
		return place == BEGAIN_PLACE;
	}
	
	public boolean isAfterLast() throws SQLException
	{
		return place == arrayString.length;
	}
	
	public boolean isFirst() throws SQLException
	{
		return place == BEGAIN_PLACE + 1;
	}
	
	public boolean isLast() throws SQLException
	{
		return place == arrayString.length - 1;
	}
}
