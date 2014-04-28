package akosha.demo.signin;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import akosha.demo.model.AppAuthentication;
import akosha.demo.signin.service.FacebookInf;
import akosha.demo.signin.service.FacebookInfImpl;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Page;
import facebook4j.Paging;
import facebook4j.Permission;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthSupport;
import facebook4j.internal.http.HttpResponse;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

/*
 *Single Controller which has handlers for different type of request.
 *
 *
 *
 **/

@Controller
public class MainController {


	/*This is the handler for SignIn to facebook 
	 * */
	@RequestMapping(value="/signin",method=RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException{

		Facebook facebook = new FacebookFactory().getInstance();

		request.getSession().setAttribute("facebook", facebook);
		FacebookInf facebookinf=new FacebookInfImpl();
		facebook=facebookinf.getInstance(facebook);
		StringBuffer callbackURL = request.getRequestURL();
		int index = callbackURL.lastIndexOf("/");
		callbackURL.replace(index, callbackURL.length(), "").append("/callback");
		response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackURL.toString()));

	}

	// this handler is used for redirecting to our App after Oauth from facebook
	@RequestMapping(value="/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
		// getting oauthcode.
		String  oauthCode = request.getParameter("code");
		request.getSession().setAttribute("oauthcode", oauthCode);
		try {
			System.out.println("accesstoken");
			AccessToken access_token=facebook.getOAuthAccessToken(oauthCode);
			String access_string=access_token.getToken();
			request.getSession().setAttribute("access_token", access_string);
			System.out.println(access_string);
		} catch (FacebookException e) {
			throw new ServletException(e);
		}
		response.sendRedirect(request.getContextPath() + "/postlogin");

	}
	// this handler is returning view after user logs in successfully

	@RequestMapping(value="/postlogin")
	public String postlogin(HttpServletRequest request, HttpServletResponse response){

		if(request.getSession().getAttribute("facebook") !=null)
			return "postlogin";
		else
			return "welcome";
	}

	// to check whether session exists or not .This handler requires for browser back button.
	// if response sent as notloggedin, then user should be redirected to login page.
	@RequestMapping(value="/sessioncheck")
	public @ResponseBody String postloginSessionChecker(HttpServletRequest request, HttpServletResponse response){

		System.out.println(request.getSession().getAttribute("facebook")==null);
		if(request.getSession().getAttribute("facebook") !=null)
			return "loggedin";
		else
			return "notloggedin";
	}


	// this handler takes care of returning latest posts from facebook.This handler is running every 2 minutes to check
	//whether there are latest posts are not .If not, it will return empty list and if there are any new feeds published,
	//then it will return new published feeds.
	@RequestMapping(value="/latestposts")
	public @ResponseBody ResponseList<Post> getlatestposts(@RequestParam boolean next,@RequestParam String name,HttpServletRequest request, HttpServletResponse response) throws FacebookException {
		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
		//getting facebook page id
		
		FacebookInf finf=new FacebookInfImpl();
		//calling service layer method ot get new feeds for the same page.
		//this method will run every 2 minutes.		
		ResponseList<Post> feeds=finf.getnewfeeds(facebook, name, request.getSession().getAttribute("latestepoch").toString().substring(0,10));

		if(feeds !=null && feeds.size()>0)
		{ 
			//getting time for first latest feed so that next time ,it will fetch all latest posts after this time.

			long statustime=feeds.get(0).getCreatedTime().getTime();
			//now we are overiding first latest feed time in session 
			request.getSession().setAttribute("latestepoch", statustime);
		}
		return feeds;
	}

	//this handler takes care of getting posts from facebook for particular page.
	//This handler is having 1 boolean parameter also as a request to check whether user is requesting old feeds 
	//for same page or new feeds for new page.
	@RequestMapping(value="/getposts")
	public @ResponseBody ResponseList<Post> getPageposts(@RequestParam boolean next,@RequestParam String name,HttpServletRequest request, HttpServletResponse response) throws FacebookException, NonextPagefoundException {
		ResponseList<Post> feeds=null;
		try{	
			Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");

			FacebookInf facebookimpl=new FacebookInfImpl();

			//next=false means the request is for new page 
			if(next==false)
			{
				// calling service layer method to get all feeds 
				feeds=facebookimpl.getfeeds(facebook, name);
				// now we are fetching first latest feed time and last feed time so that old feeds we can take based
				//on last feed time and new feeds we can take from first latest feed time.
				//service layer is returning e.g. <firstfeedtime,lastfeedtime>
				String oldandlatestime=facebookimpl.getlatestandoldtime(facebook, name);
				//now we are splitting to get first feed time  and last feed time.
				String[] times=oldandlatestime.split(",");
				// now we are storing first feed time and last feed time in session. 
				request.getSession().setAttribute("latestepoch", times[0]);
				request.getSession().setAttribute("oldepoch", times[1]);

			}else
			{
				// it will enter in this when request is for same page but older posts.
				// now we are getting next oldtime upto which we have to get feeds. 
				String nextoldnexttime=facebookimpl.getoldtime(facebook, name, (String)request.getSession().getAttribute("oldepoch"));
				if(nextoldnexttime !=null){
					feeds=facebookimpl.getoldfeeds(facebook, name, nextoldnexttime);
					//if there are any next old posts , then we have to over-ride old feed time of same page.
					request.getSession().setAttribute("oldepoch", nextoldnexttime);
				}else 
					throw new NonextPagefoundException();

			}

		}catch (NonextPagefoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			request.setAttribute("nonextpage", "No more posts to show");
			System.out.println(request.getAttribute("nonextpage"));
			throw e;
		}catch (FacebookException e) {
			// TODO: handle exception
			request.setAttribute("nopagefound", "No more posts to show");
			System.out.println(request.getAttribute("nonextpage"));
			throw e;
		}

		return feeds;

	}

	// this handler takes care of logout from our App.
	@RequestMapping(value="/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response){
		String accessToken = "";
		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");


		accessToken = facebook.getOAuthAccessToken().getToken();
		//invalidating session.
		request.getSession().invalidate();

		// Log Out of the Facebook
		StringBuffer next = request.getRequestURL();
		int index = next.lastIndexOf("/");
		next.replace(index+1, next.length(), "");
		try {
			response.sendRedirect("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			request.setAttribute("logoutresponse", "Problem in logging out");
			e.printStackTrace();
		}
	}


}
