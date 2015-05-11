
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MECDatabase 
{
	private static Connection conn = null;
	private static Statement statement = null;
	
	public MECDatabase()
	{}
	
	public static void ModifydoSQL(String SQLString)
	{
		try 
		{
			 statement.execute(SQLString);

		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("");
		}
	}
	
	public static ResultSet doSQL(String SQLString) throws Exception
	{
		ResultSet rs = null;
		
		try 
		{
			rs = statement.executeQuery(SQLString);
		} catch (SQLException e) 
		{
			e.printStackTrace();
			throw new Exception("Class:MECDatabase\nMethod:public static ResultSet doSQL() throws Exception\nReason:SQL执行异常\nSQL:" +
				SQLString + "\n");
		}
		
		return rs;
	}
	
	public static void disconnection() throws Exception
	{
		try 
		{
			statement.close();
		} catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new Exception("Class:MECDatabase\nMethod:public static void disconnection() throws Exception\nReason:数据库关闭异常\n");
		}
	}
	
	public static void connection(String databaseName, String userName, String password) throws Exception
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe)
		{
			System.out.println(cnfe.getMessage());
			throw new Exception("Class:MECDatabase\nMethod:public static void connection() throws Exception\nReason:数据库驱动异常\n");
		}
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:" + databaseName,
				userName, password);
		statement = conn.createStatement();
	}
}
