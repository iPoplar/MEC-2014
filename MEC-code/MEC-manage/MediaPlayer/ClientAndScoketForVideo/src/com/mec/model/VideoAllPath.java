package com.mec.model;

import java.io.Serializable;

public class VideoAllPath implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1860479786686648866L;
	private String videoid;
	private String videopath;
	public String getVideoid() 
	{
		return videoid;
	}
	public void setVideoid(String videoid)
	{
		this.videoid = videoid;
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
