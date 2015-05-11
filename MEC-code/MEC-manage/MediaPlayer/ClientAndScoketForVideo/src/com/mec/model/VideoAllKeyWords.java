package com.mec.model;

import java.io.Serializable;

public class VideoAllKeyWords implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2521055560027677511L;
	private String videoid;
	private String keywords;
	public String getVideoid() 
	{
		return videoid;
	}
	public void setVideoid(String videoid)
	{
		this.videoid = videoid;
	}
	public String getKeywords() 
	{
		return keywords;
	}
	public void setKeywords(String keywords) 
	{
		this.keywords = keywords;
	}
	

}
