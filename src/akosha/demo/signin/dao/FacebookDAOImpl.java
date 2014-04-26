package akosha.demo.signin.dao;


import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import akosha.demo.model.AppAuthentication;
import akosha.demo.model.Brand;
import akosha.demo.model.Status_Comments;
import akosha.demo.model.Status_Post;
import akosha.demo.utility.DBConnection;

public class FacebookDAOImpl implements FacebookDAO {

	@Override
	public AppAuthentication getappdetails() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=DBConnection.get();
		Statement stmt=null;
		AppAuthentication app=null;
		try{
			String sqlstring="select app_id,appsecret_id,app_permissions from app_authentication";

			app=new AppAuthentication();

			stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sqlstring);
			while(r.next())
			{
				String app_id=r.getString("app_id");
				String app_sec_id=r.getString("appsecret_id");
				String app_perms=r.getString("app_permissions");
				app.setApp_permissions(app_perms);
				app.setApp_secretid(app_sec_id);
				app.setAppid(app_id);
			}
		}finally{
			stmt.close();
			conn.close();
		}
		return app;
	}

	@Override
	public String storeposts(String pagename,Status_Post posts) throws SQLException {
		// TODO Auto-generated method stub
		String businesslogic="unsuccessful";
		String post_id=posts.getPid();
		String post_message=posts.getPmessage();
		long post_date=posts.getPost_date();

		Connection conn=DBConnection.get();
		String sqlstring="insert into status_post values(?,?,?,?)";

		PreparedStatement prestmt = conn.prepareStatement(sqlstring);

		prestmt.setString(1, pagename);
		prestmt.setString(2, post_id);


		prestmt.setString(3, post_message);
		prestmt.setLong(4, post_date);
		int rowsaffected=prestmt.executeUpdate();
		if(rowsaffected>0)
			businesslogic="posts_store_successful";
		else {

			throw new SQLException();
		}
		return businesslogic;

	}

	/*@Override
	public String storecomments(Status_Comments comment) {
		// TODO Auto-generated method stub

		String businesslogic="";
		String comment_id=comment.getCid();
		String comment_message=comment.getCmesage();
		String comment_person_name=comment.getFrom_person_name();
		String comment_person_id=comment.getFrom_person_id();
		Connection conn=DBConnection.get();
		String sqlstring="insert into comments values(?,?,?,?)";
		PreparedStatement prestmt;
		try {
			prestmt = conn.prepareStatement(sqlstring);

		prestmt.setString(1, comment_id);
		prestmt.setString(2, comment_message);
		prestmt.setString(3, comment_person_name);
		prestmt.setString(4, comment_person_id);
		int rowsaffected=prestmt.executeUpdate();
		if(rowsaffected>0)
			businesslogic="comments_store_successful";
		}catch (SQLException e) {
			// TODO: handle exception
			businesslogic="comments_store_unsuccessful";
		}
		return businesslogic;
	}

	@Override
	public String storebrand(Brand brand) {
		// TODO Auto-generated method stub
		String businesslogic="";
		Connection conn=DBConnection.get();
		String sqlstring="insert into brand values(?,?)";
		PreparedStatement prestmt;
		try {
			prestmt = conn.prepareStatement(sqlstring);

		prestmt.setString(1, brand.getB_id());
		prestmt.setString(2, brand.getB_name());
		int rowsaffected=prestmt.executeUpdate();
		if(rowsaffected>0)
			businesslogic="brand_store_successful";
		}catch (SQLException e) {
			// TODO: handle exception
			businesslogic="brand_store_unsuccessful";
		}
		return businesslogic;
	}*/



}
