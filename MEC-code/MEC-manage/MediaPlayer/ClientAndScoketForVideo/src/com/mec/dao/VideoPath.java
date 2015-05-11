package com.mec.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.mec.model.VideoAllPath;

public class VideoPath 
{
	private static Statement statement ;
	
	/*******************************************************
	* Function Name: 	connection
	* Purpose: 			数据库的连接	
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
			System.out.println("驱动不存在");
			throw e;
		} catch (SQLException e) 
		{
			System.out.println("jdbc:java不存在");
			throw e;
		}
	}
	
	/*******************************************************
	* Function Name: 	getAllVideoPath()
	* Purpose: 			查找sys_subject_path所有信息
	********************************************************/
	public static ArrayList<VideoAllPath> getAllVideoPath() throws SQLException
	{
		ResultSet rs;
		String Id;
		String Path;
		
		ArrayList<VideoAllPath> reList = new ArrayList<VideoAllPath>();		
		String SQLString = "SELECT * FROM sys_subject_path";
		
		try
		{
			rs = statement.executeQuery(SQLString);
			while(rs.next())
			{
				Id = rs.getString("videoid");
				Path = rs.getString("videopath");
			
				VideoAllPath videoInfo = new VideoAllPath();
				videoInfo.setVideoid(Id);
				videoInfo.setVideopath(Path);
				reList.add(videoInfo);
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
			
		return reList;			
	}
	
	/*******************************************************
	* Function Name: 	FindPath
	* Purpose: 			根据视频编号查找所属科目路径
	********************************************************/
	public static ArrayList<VideoAllPath> FindPath(String No) throws SQLException
	{
		ResultSet rs;	
		String Path;
		ArrayList<VideoAllPath> reList = new ArrayList<VideoAllPath>();
				
		String SQLString =  "select subjectpath from sys_subject_path where subjectid ='"+ No +"'";	
		System.out.println(SQLString);
		
		try
		{
			rs = statement.executeQuery(SQLString);			
			while(rs.next())
			{
				Path = rs.getString("subjectpath");	
				VideoAllPath videoPath = new VideoAllPath();
				videoPath.setVideopath(Path);
				reList.add(videoPath);
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
	* 参数：				（String）No,Path 要添加的编号、科目路径
	********************************************************/
	public static void addData(String No, String Path)throws SQLException
	{
		
		String SQLString =  "insert into sys_subject_path (subjectId,subjectPath) values ('"+No+"','"+Path+"')";		
		try
		{
			statement.executeUpdate(SQLString);
		} catch (SQLException e) 
		{
			e.printStackTrace();
			throw e;
		}	
	}
	
	/*******************************************************
	* Function Name: 	modifyData
	* Purpose: 			修改数据
	* 参数：				（String）No,Path 要添加的编号、科目路径
	********************************************************/
	public static void modifyData(String No, String Path)throws SQLException
	{
		
		String SQLString =  "update sys_subject_path set subjectpath='"+Path+"'where  subjectid='"+No+"'";	
		try
		{
			statement.executeUpdate(SQLString);
		} catch (SQLException e) 
		{
			e.printStackTrace();
			throw e;
		}	
	}
	
	public static void breakConnection() throws SQLException
	{
		try
		{
			statement.close();
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
}
