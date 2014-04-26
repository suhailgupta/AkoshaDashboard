package akosha.demo.model;

import java.util.Date;

public class Status_Post {

	private String pid;
	private String pmessage;
	private long post_date;
	private String person_name;
	
	
	public Status_Post(String pid, String pmessage, long post_date,
			String person_name) {
		//super();
		this.pid = pid;
		this.pmessage = pmessage;
		this.post_date = post_date;
		this.person_name = person_name;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public long getPost_date() {
		return post_date;
	}
	public void setPost_date(long post_date) {
		this.post_date = post_date;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPmessage() {
		return pmessage;
	}
	public void setPmessage(String pmessage) {
		this.pmessage = pmessage;
	}
	
}
