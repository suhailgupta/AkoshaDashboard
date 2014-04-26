package akosha.demo.signin.service;

import javax.servlet.http.HttpServletRequest;

import akosha.demo.model.AppAuthentication;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public interface FacebookInf {
	
	
	public Facebook getInstance(Facebook facebook);
	
	//public String storeposts(String pagename,boolean next,Facebook facebook);
	
   //public String storebrand(String page_id,String page_name);
	
		
	public String getlatestandoldtime(Facebook facebook,String pagename) throws FacebookException;
	
	public String getoldtime(Facebook facebook,String pagename,String time) throws FacebookException;

	public ResponseList<Post> getfeeds(Facebook facebook, String pagename) throws FacebookException;
	
	public ResponseList<Post> getoldfeeds(Facebook facebook, String pagename,String time) throws FacebookException;

	public ResponseList<Post> getnewfeeds(Facebook facebook, String pagename,String time) throws FacebookException;
	
}
