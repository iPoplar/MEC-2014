import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FStatement 
{
	private static String sqlString  = "";
	//private static String[][] arrayString;
	private static Connection con = null;
	
	private static Statement statement;
	private static ResultSetMetaData rsdm;
	
	public static String OracleDriver = "oracle.jdbc.driver.OracleDriver";
	public static String SQLServerDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static String MySQLDriver = "com.mysql.jdbc.Driver";
	public static String SybaseDriver = "com.sybase.jdbc.SybDriver";
	public static String DB2Driver = "com.ibm.db2.jdbc.app.DB2Driver";
	public static String SqlDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	//public final String IfxDriver = "com.informix.jdbc.IfxDriver";
	
	private final static String Driver2Url[][] = {
		{"oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@", ":", ":"},
		{"com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://", ":", ";DatabaseName="},
		{"com.mysql.jdbc.Driver", "jdbc:mysql://", ":", "/"},
		{"com.sybase.jdbc.SybDriver", "jdbc:sybase:Tds:", ":", "/"},
		{"com.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2://", ":", "/"},
		{"com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://",":",";DatabaseName="}
	//	{"IfxDriver","jdbc:Informix-sqli://", ":", "/", ":I NFORMIXSER=myserver"}
	};
	
	private FStatement() throws SQLException
	{
	}
	
	public static void disConnection()throws Exception
	{
		try 
		{
			statement.close();
		} catch (SQLException e) 
		{
			throw new Exception("Class：FStatement,Method: disConnection(), 关闭异常！");
		}
	}
	
	private static String UrlDeal(String driverName, String hostName, String portName, String DataBaseName)
	{
		String mess = null;
		
		//System.out.println(driverName + " " + hostName+ " " + portName + " " + DataBaseName);
		for(int i = 0; i < Driver2Url.length; i++)
		{
			if(Driver2Url[i][0].equalsIgnoreCase(driverName))
			{
				mess = Driver2Url[i][1] + hostName + Driver2Url[i][2] + portName + Driver2Url[i][3] + DataBaseName;
			}
		}
		//JOptionPane.showMessageDialog(new JFrame("温馨提示"),mess);
		
		return mess;
	}
	
	public static int executeUpdateCon(String SQLString) throws SQLException
	{
		int num = 0;
		
		try 
		{
			num = statement.executeUpdate(SQLString);
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class: " +
					"Statement, Method:executeUpdateCon(),更新数据库异常！aaaaaaaaa"+SQLString );
		}
		return num;
	}
	
	public static FResultSet executeQueryCon(String SQLString) throws SQLException
	{
		String str  = "";
		FResultSet name = null;
		String[][] resultString;
		
		try 
		{
			ResultSet rs = statement.executeQuery(SQLString);
			rsdm = rs.getMetaData();
			int crow = rsdm.getColumnCount();
			
			for(int i = 1; i <= crow; i++)
			{
				str += new StringBuffer().append(rsdm.getColumnLabel(i)).append("\t");
			}
			str += new StringBuffer().append('\n');
			for(int i = 1; i <= crow; i++)
			{
				str += new StringBuffer().append(rsdm.getColumnTypeName(i)).append("\t");
			}
			str += new StringBuffer().append('\n');
			while(rs.next())
			{
				for(int i = 1; i <= crow; i++)
				{
					str += new StringBuffer().append(rs.getString(i)).append('\t');
				}
				str += new StringBuffer().append('\n');
			}
			//rs.close();
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class: Statement, " +
					"Method:executeQueryCon(),查询数据库异常！\n"+SQLString+"111" );
		}
		sqlString = str;
		
		resultString = splitString();
		name = new FResultSet(resultString);
		

	/*	for(int i = 0; i < arrayString.length; i++)
		{
			for(int j = 0; j < arrayString[0].length; j++)
			{
				System.out.println(arrayString[i][j]);
			}
			System.out.println("abc");
		}*/
		
		return name;
	}
	
	public static void connection(String DriverName, String hostName, String portName, String dataBaseName, String userName, String password) throws SQLException
	{
		try 
		{
			//System.out.println(DriverName);
			Class.forName(DriverName);
			String str = UrlDeal(DriverName, hostName, portName, dataBaseName);
			//System.out.println(str);
			con = DriverManager.getConnection(str, userName, password);
			statement = con.createStatement();
		} catch (ClassNotFoundException e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class: Statement, Method:connection(),连接数据库异常！" );
			throw new SQLException("Class:Statement, Method: connection(),用户名或登录密码异常！");
		}
	}
	
	private static boolean checkSQLString(String SQLString) throws SQLException
	{
		String[] str = null;
		//sqlString = SQLString;
		
		str = SQLString.split("\n | \t");
		
		return str.length <= 1;
	}
	
	public static String[][] splitString() throws SQLException
	{
		String[] recordString;
		String[][] resultString;
		//System.out.println(sqlString);
		
		recordString = sqlString.split("\n");
		resultString = new String[recordString.length][];
		
		for(int i = 0; i < recordString.length; i++)
		{
			resultString[i] = recordString[i].split("\t");
		}
		
		return resultString;
	}
	
	private static void dealConnection(String SQLString) throws SQLException
	{
		MecClient client;
		try 
		{
			client = new MecClient();
			client.sendMess(SQLString);
			
			while(client.stop == true)
				;
			sqlString  = client.str;
		} catch (Exception e) 
		{
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"Class: Statement, Method:dealConnection(),连接服务器异常！" );
		}
	}

	public static FResultSet executeQuery(String SQLString) throws SQLException
	{
		String[][] resultString;
		if(checkSQLString(SQLString))
		{
			dealConnection(SQLString);
		}
		else
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"SQL语句存在非法字符！" );
		resultString = splitString();
		FResultSet rs = new FResultSet(resultString);
		
		return rs;
	}
	
	/*public static ResultSet executeQuery() throws SQLException
	{
		splitString();
		ResultSet rs = new ResultSet(arrayString);
		
		return rs;
	}*/
	
	public static int executeUpdate(String SQLString) throws SQLException
	{//需要返回一个字符表示更新的记录条数
		if(checkSQLString(SQLString))//对于 CREATE TABLE 或 DROP TABLE 等不操作行的语句，executeUpdate 的返回值总为零。
		{
			dealConnection(SQLString);
		}
		else
			JOptionPane.showMessageDialog(new JFrame("温馨提示"),"SQL语句存在非法字符！" );
		
		return Integer.parseInt(sqlString);
	}
	
	
	public static void close() throws SQLException
	{
		//待处理
	}
	
	public static boolean isClose() throws SQLException
	{
		//待处理
		return false;
	}
	
	public static boolean execute(String SQLString) throws SQLException
	{
		return splitString().length > 1;//存在值则返回true
	}
}
