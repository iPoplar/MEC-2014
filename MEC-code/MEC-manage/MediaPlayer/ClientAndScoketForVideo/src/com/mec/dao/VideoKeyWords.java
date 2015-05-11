package com.mec.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mec.model.VideoAllInformation;
import com.mec.model.VideoAllKeyWords;

public class VideoKeyWords 
{
	private static Statement statement ;
	
	/*******************************************************
	* Function Name: 	connection
	* Purpose: 			���ݿ������	
	********************************************************/
	public static void connection()throws ClassNotFoundException, SQLException
	{
		Connection conn;		
		final String ojdbcDRIVER = "oracle.jdbc.driver.OracleDriver";
		final String ojdbcURL =  "jdbc:oracle:thin:@192.168.1.5:1521:mecdb";
		final String user = "mec_prog_user";
		final String pass = "654321";
		try 
		{
			Class.forName(ojdbcDRIVER);
			conn = DriverManager.getConnection(ojdbcURL, user, pass);
			statement = conn.createStatement();			
		} catch (ClassNotFoundException e) 
		{
			System.out.println("����������");
		} catch (SQLException e) 
		{
			System.out.println("jdbc:java������");
		}
	}
	
	/*******************************************************
	* Function Name: 	FindAllVideoKeyWords
	* Purpose: 			�ӱ�SYS_VIDEO_KEYWORDS�в�����йؼ���
	********************************************************/
	public static ArrayList<VideoAllKeyWords> FindAllVideoKeyWords() throws SQLException
	{
		ResultSet rs;
		String Id;
		String keyword;
		
		ArrayList<VideoAllKeyWords> reList = new ArrayList<VideoAllKeyWords>();
		String SQLString = "select * from SYS_VIDEO_KEYWORDS";
		
		try
		{
			rs = statement.executeQuery(SQLString);
			while(rs.next())
			{
				Id = rs.getString("videoid");
				keyword = rs.getString("videokeywords");			
				VideoAllKeyWords videoword = new VideoAllKeyWords();
				videoword.setVideoid(Id);
				videoword.setKeywords(keyword);			
				reList.add(videoword);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
			
		return reList;
		
	}
	
	/*******************************************************
	* Function Name: 	FindIdVideoKeyWords
	* Purpose: 			������Ƶ���ƻ�ȡ�ؼ���	
	* ������				��String�� videoname ָ������Ƶ����
	********************************************************/
		public static ArrayList<VideoAllKeyWords> FindIdVideoKeyWords(String videoname) throws SQLException
		{
			ResultSet rs;
			String Keyword;
			
			ArrayList<VideoAllKeyWords> reList = new ArrayList<VideoAllKeyWords>();		
			String SQLString = "select videokeywords from SYS_VIDEO_KEYWORDS where VIDEOID =" +
					" (select VIDEOID from SYS_VIDEO_INFORMATION where VIDEONAME = '"+ videoname +"')";		
			
			try
			{
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					Keyword = rs.getString("videokeywords");
					VideoAllKeyWords videoword = new VideoAllKeyWords();
					videoword.setKeywords(Keyword);
					reList.add(videoword);
				
				}				
			}catch(SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
				
			return reList;			
		}
		
		/*******************************************************
		* Function Name: 	addKeyword
		* Purpose: 			�������
		* ������				��String��No,keyword Ҫ��ӵı�š��ؼ���
		********************************************************/	
		public static int addKeyword(String No,String keyword) throws SQLException
		{
			
			String SQLString =  "insert into SYS_VIDEO_KEYWORDS (videoid,videokeywords) values ('"+No+"','"+keyword+"')";
			int count =  0;
			
			try
			{
				count = statement.executeUpdate(SQLString);				
			} catch (SQLException e) 
			{
				e.printStackTrace();
				throw e;
			}	
			
			return count;
		}
		
		/*******************************************************
		* Function Name: 	deleteKeyword
		* Purpose: 			����ָ���ؼ���ɾ������
		* ������				��String��keyword ָ���ؼ���
		********************************************************/
		public static int deleteKeyword(String keyword)throws SQLException
		{
			String SQLString = "delete  from SYS_VIDEO_KEYWORDS where videokeywords='"+keyword+"'";
			int count = 0;
			
			try 
			{
				count = statement.executeUpdate(SQLString);
			} catch (SQLException e) 
			{
				e.printStackTrace();
				throw e;
			}
			
			return count;
		}
		
		public static void breakConnection() throws SQLException
		{
			try 
			{
				statement.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
				throw e;
			}
		}

}
