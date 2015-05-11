package com.mec.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mec.model.VideoAllInformation;

public class VideoInformation
{	
	private static Statement statement ;
	
	/*******************************************************
	* Function Name: 	connection
	* Purpose: 			数据库的连接	
	********************************************************/
	public static void connection() throws SQLException, ClassNotFoundException
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
			System.out.println("驱动不存在");
			throw e;
		} catch (SQLException e) 
		{
			System.out.println("jdbc:java不存在");
			throw e;
		}
	}
		
	/*******************************************************
	* Function Name: 	getAllVideoInformation
	* Purpose: 			从表sys_video_information中查出数据
	********************************************************/
		public static ArrayList<VideoAllInformation> getAllVideoInformation() throws SQLException
		{
			ResultSet rs;
			String Id;
			String Name;
			String Path;
			
			ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();		
			String SQLString = "SELECT * FROM sys_video_information";
			
			try
			{
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					Id = rs.getString("videoid");
					Name = rs.getString("videoname");
					Path = rs.getString("videopath");
					
					VideoAllInformation videoInfo = new VideoAllInformation();
					videoInfo.setVideoid(Id);
					videoInfo.setVideoname(Name);
					videoInfo.setVideopath(Path);
					reList.add(videoInfo);				
				} 
			}catch(SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
				
			return reList;			
		}
		
		public static ArrayList<VideoAllInformation> FindSameIdVideoInforation(String videoId)throws SQLException
		{
			ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();
			String SQLString = "SELECT VIDEOID FROM SYS_VIDEO_INFORMATION WHERE VIDEOID LIKE '" + videoId + "' || '_'";
			
			System.out.println(SQLString + "***"+ videoId.length());
			try
			{
				ResultSet rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					VideoAllInformation videoInfo = new VideoAllInformation();
					String id = rs.getString("VIDEOID");
					videoInfo.setVideoid(id);
					reList.add(videoInfo);
				}				
			}catch(SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
			
			return reList;
		}
		
		/*******************************************************
		* Function Name: 	FindIdVideoInformation
		* Purpose: 			根据视频名称获取编号	
		* 参数：				（String） vname 指定的视频名称
		********************************************************/
	public static ArrayList<VideoAllInformation> FindIdVideoInformation(String vname) throws SQLException
	{
		ResultSet rs;	
		String Id = null ;	
		ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();
				
		String SQLString =  "select VIDEOID from SYS_VIDEO_INFORMATION where VIDEONAME = '"+ vname +"'";		
		
		try
		{
			rs = statement.executeQuery(SQLString);			
			while(rs.next())
			{
				VideoAllInformation videoInfo = new VideoAllInformation();
				Id = rs.getString("videoid");
				videoInfo.setVideoid(Id);
				reList.add(videoInfo);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return reList;	
	}
	
	/*******************************************************
	* Function Name: 	FindNameVideoInformation
	* Purpose: 			根据编号获取视频名称	
	* 参数：				（String） videoid 指定的编号
	********************************************************/
	public static ArrayList<VideoAllInformation> FindNameVideoInformation(String videoid) throws SQLException
	{
		ResultSet rs;	
		String Name = null ;
		ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();
			
		String SQLString =  "select VIDEONAME from SYS_VIDEO_INFORMATION where VIDEOID = '"+ videoid +"'";
	
		try
		{
			rs = statement.executeQuery(SQLString);	
			while(rs.next())
			{
				VideoAllInformation videoInfo = new VideoAllInformation();
				Name = rs.getString("videoname");	
				videoInfo.setVideoname(Name);
				reList.add(videoInfo);		
			}			
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return reList;	
	}
	/*******************************************************
	* Function Name: 	FindPathVideoInformationAN
	* Purpose: 			根据视频名称获取视频路径	
	* 参数：				（String） vname 指定的视频名称
	********************************************************/
	public static ArrayList<VideoAllInformation> FindPathVideoInformationAN(String vname) throws SQLException
	{
		ResultSet rs;	
		String Path = null ;
		ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();
			
		String SQLString =  "select videoPath from SYS_VIDEO_INFORMATION where VIDEONAME = '"+ vname +"'";
		System.out.println(SQLString);
	
		try
		{
			rs = statement.executeQuery(SQLString);	
			while(rs.next())
			{
				VideoAllInformation videoInfo = new VideoAllInformation();
				Path = rs.getString("videoPath");
				videoInfo.setVideopath(Path);
				reList.add(videoInfo);		
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return reList;	
	}

	/*******************************************************
	* Function Name: 	FindPathVideoInformationAI
	* Purpose: 			根据视频编号获取视频路径	
	* 参数：				（String） vid 指定的编号
	********************************************************/
	public static ArrayList<VideoAllInformation> FindPathVideoInformationAI(String vid) throws SQLException
	{
		ResultSet rs;	
		String Path = null ;
		ArrayList<VideoAllInformation> reList = new ArrayList<VideoAllInformation>();
			
		String SQLString =  "select videoPath from SYS_VIDEO_INFORMATION where  VIDEOID = '"+ vid +"'";

		try
		{
			rs = statement.executeQuery(SQLString);	
			while(rs.next())
			{
				Path = rs.getString("videoPath");
				VideoAllInformation videoInfo = new VideoAllInformation();
				
				videoInfo.setVideopath(Path);
				reList.add(videoInfo);		
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
				
		return reList;			
	}
		
	/*******************************************************
	* Function Name: 	addData
	* Purpose: 			添加数据
	* 参数：				（String）No,Name,Path 要添加的编号、名称、路径
	********************************************************/
	public static int addData(String No,String Name, String Path) throws SQLException
	{
		
		String SQLString =  "insert into SYS_VIDEO_INFORMATION (videoid,videoname,videopath) values ('"+No+"','"+Name+"','"+Path+"')";
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
		} catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
	}

}
