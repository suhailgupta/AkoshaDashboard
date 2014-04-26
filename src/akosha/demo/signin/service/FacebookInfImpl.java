package akosha.demo.signin.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import akosha.demo.model.AppAuthentication;
import akosha.demo.model.Status_Post;
import akosha.demo.signin.dao.FacebookDAO;
import akosha.demo.signin.dao.FacebookDAOImpl;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Page;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class FacebookInfImpl implements FacebookInf{

	// getting appid ,appsecret_id and permissions in facebook instance from DB.
	@Override
	public Facebook getInstance(Facebook facebook) {
		// TODO Auto-generated method stub

		FacebookDAO fdinf=new FacebookDAOImpl();
		try{
			//Call to DAO layer to fetch app_id, appsecret_id and permissions for facebook instance 
			AppAuthentication app=fdinf.getappdetails();

			facebook.setOAuthAppId(app.getAppid(),app.getApp_secretid());
			facebook.setOAuthPermissions(app.getApp_permissions());

		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return facebook;
	}

	/*@Override
	public String storebrand(String page_id, String page_name) {
		// TODO Auto-generated method stub
		Brand brand=new Brand(page_id,page_name);
		FacebookDAO fdinf=new FacebookDAOImpl();
		String statusrecieved=fdinf.storebrand(brand);
	     if(statusrecieved.equals("brand_store_successful"))
		return "brand_successful";
	     else
	    	 return "brand not added";
	}*/
	/*
	public static void storecomments(PagableList<facebook4j.Comment> comments) {
		// TODO Auto-generated method stub
		FacebookDAO fdao=new FacebookDAOImpl();
		String statusrecieved="";
		for(int i=0;i<comments.size();i++){
     	   String comment_id=comments.get(i).getId();
     	   String cmessage=comments.get(i).getMessage();
     	   String from_person=comments.get(i).getFrom().getName();
     	   String comment_person_id=comments.get(i).getFrom().getId();

     	   Status_Comments sc=new Status_Comments(comment_id,from_person,cmessage,comment_person_id);
     	statusrecieved=  fdao.storecomments(sc);
     	if(statusrecieved.equals("comments_store_successful"))
     		System.out.println("comments are stored with posts");
	       //    list_comments.add(sc);
        }
	}*/

	// this method is getting called whenever request is coming for new page.
	//we are getting first feed time and last feed time from this method.
	@Override
	public String getlatestandoldtime(Facebook facebook,String pagename) throws FacebookException {
		// TODO Auto-generated method stub
		ResponseList<Post> feeds=null;
		Page page=facebook.getPage(pagename);
		if(page!=null){

			String feed_id=facebook.getPage(pagename).getId();
			feeds  = facebook.getFeed(pagename,
					new Reading().limit(25));

			if(feeds.size()>0){

				//time for first feed , so that we can check all latest feeds depend on this time.  
				long time2=feeds.get(0).getCreatedTime().getTime();
				//		  request.getSession().setAttribute("latestposts", time2);
				// need to check posts
				//facebook server is storing time in unix epoch format.
				// conversion of time to unix epoch format.
				String firstlatestepoch =((Long)time2).toString().substring(0,10);

				// getting Paging object so that we can check whether there are next old feeds are there are or not.
				Paging<Post> paging1 = feeds.getPaging();
				//   System.out.println(paging1);
				//   System.out.println(paging1.getNext().getQuery());
				// we are fetching quering parameters from paging object next parameter.
				String[] queryparams=paging1.getNext().getQuery().split("&");
				// we are taking length-1 because time is at position in query parameters.
				//it will be like for e.g until="<time in epoch format>"
				int length=queryparams.length-1;
				String[] time=queryparams[length].split("=");
				//taking time from which we need to check next old feeds.
				String oldepoch=time[1];
				//   String[] queryparams=paging1.getNext().getQuery()
				// returning <first feed time,last feed time>
				return firstlatestepoch+","+oldepoch;

			}

		}
		return null;
	}

	// this method gets called when request is coming for same page but old feeds.
	@Override
	public String getoldtime(Facebook facebook,String pagename,String time) throws FacebookException {
		// TODO Auto-generated method stub
		// now we are getting feeds based on last old time.
		ResponseList<Post> feeds  = facebook.getFeed(pagename,
				new Reading().until(time));
		// again we need to check paging and store next old last feed time.
		Paging<Post> paging1 = feeds.getPaging();
		if(paging1 !=null){
			//  System.out.println(paging1);
			//   System.out.println(paging1.getNext().getQuery());
			String[] queryparams=paging1.getNext().getQuery().split("&");
			int length=queryparams.length-1;
			String[] nextoldtime=queryparams[length].split("=");
			String oldepoch=nextoldtime[1];
			return oldepoch;
		}
		return null;
	}

	//this method is called first time to get feeds for new page.
	@Override
	public ResponseList<Post> getfeeds(Facebook facebook,String pagename) throws FacebookException {
		// TODO Auto-generated method stub
		String feed_id=facebook.getPage(pagename).getId();

		ResponseList<Post> feeds  = facebook.getFeed(pagename,
				new Reading().limit(25));
		//storing feeds in DB.
		String businessString=storefeedsindb(feed_id, feeds);
		if(businessString.equals("posts_store_successful"))
		{
			System.out.println("posts store successfully");
		}
		return feeds;

	}

	//this method gets called when request if for getting next old feeds.
	@Override
	public ResponseList<Post> getoldfeeds(Facebook facebook, String pagename,
			String time) throws FacebookException {
		// TODO Auto-generated method stub

		String feed_id=facebook.getPage(pagename).getId();
		ResponseList<Post> feeds   = facebook.getFeed(pagename,
				new Reading().until(time));
		String businessString=storefeedsindb(feed_id, feeds);
		if(businessString.equals("posts_store_successful"))
		{
			System.out.println("posts store successfully");
		}

		return feeds;
	}

	// this method get called every 2 minutes for new feeds.
	@Override
	public ResponseList<Post> getnewfeeds(Facebook facebook, String pagename,
			String time) throws FacebookException {
		// TODO Auto-generated method stub
		String feed_id=facebook.getPage(pagename).getId();
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
		Date date = new Date();
		System.out.println(df.format(date));
		String epoch =((Long)date.getTime()).toString();
		System.out.println("get new feeds");
		System.out.println(epoch);
		ResponseList<Post> feeds   = facebook.getFeed(pagename,
				new Reading().since(time).until(epoch));
		String businessString=storefeedsindb(feed_id, feeds);
		if(businessString.equals("posts_store_successful"))
		{
			System.out.println("posts store successfully");
		}


		return feeds;
	}

	//this method is storing feeds in DB.
	public static String  storefeedsindb(String pagename,ResponseList<Post> feeds){
		FacebookDAO fdao=new FacebookDAOImpl();

		String status_db_response="";
		if(feeds !=null)
		{
			for(int i=0;i<feeds.size();i++)
			{
				Post post = feeds.get(i);
				String post_id=post.getId();
				// Get (string) message.
				String message = post.getMessage();
				long date1=(Long)post.getCreatedTime().getTime();
				String fromname = post.getFrom().getName();
				Status_Post sp=new Status_Post(post_id,message,date1,fromname); 
				try{ 
					status_db_response=fdao.storeposts(pagename, sp);         
				}catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
					return (i-1)+" posts store successfully";
				}
			}

		}
		return status_db_response;
	}

}