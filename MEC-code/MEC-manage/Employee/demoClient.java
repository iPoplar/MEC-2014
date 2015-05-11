import java.sql.SQLException;

public class demoClient 
{
	public static void main(String[] args)
	{
		String SQLString = "SELECT * FROM believe";
		String SQLStringl = "SELECT EMPLOYEENO FROM believe";		
		try 
		{
			FStatement.connection(FStatement.OracleDriver, "192.168.1.5", "1521", "mecDb", "mec_prog_user", "654321");
			
			FResultSet rs = FStatement.executeQueryCon(SQLString); 
			
			
			while(rs.next())
			{
				System.out.println(rs.getString(1));
				//System.out.println("EMPLOYEENO");
			}
			FResultSet rsl = FStatement.executeQueryCon(SQLStringl); 
			
			while(rsl.next())
			{
				System.out.println(rsl.getString("EMPLOYEENO"));
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			FStatement.disConnection();
		} catch (Exception e) 
		{
			System.out.println("πÿ±’“Ï≥££°");
		}
	}
}
