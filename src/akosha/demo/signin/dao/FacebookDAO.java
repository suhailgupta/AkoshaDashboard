package akosha.demo.signin.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import akosha.demo.model.AppAuthentication;
import akosha.demo.model.Brand;
import akosha.demo.model.Status_Comments;
import akosha.demo.model.Status_Post;

public interface FacebookDAO {

	public AppAuthentication getappdetails() throws SQLException;
	
	public String storeposts(String pageid,Status_Post post) throws SQLException;
	
//	public String storecomments(Status_Comments comment);
//	public String storebrand(Brand brand);
	
	
	
}
