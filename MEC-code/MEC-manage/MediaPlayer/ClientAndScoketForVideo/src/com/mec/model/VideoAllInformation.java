package com.mec.model;

import java.io.Serializable;

public class VideoAllInformation implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1934778836829430542L;
	private String videoid;
	private String videoname;
	private String videopath;
	
	public String getVideoid() 
	{
		return videoid;
	}
	public void setVideoid(String videoid)
	{
		this.videoid = videoid;
	}
	public String getVideoname() 
	{
		return videoname;
	}
	public void setVideoname(String videoname) 
	{
		this.videoname = videoname;
	}
	public String getVideopath()
	{
		return videopath;
	}
	public void setVideopath(String videopath) 
	{
		this.videopath = videopath;
	}
	

}
