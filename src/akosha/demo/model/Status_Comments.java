package akosha.demo.model;

public class Status_Comments {

	private String cid;
	private String from_person_name;
	private String cmesage;
	private String from_person_id;
	
	
	public Status_Comments(String cid, String from_person_name, String cmesage,
			String from_person_id) {
		super();
		this.cid = cid;
		this.from_person_name = from_person_name;
		this.cmesage = cmesage;
		this.from_person_id = from_person_id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getFrom_person_name() {
		return from_person_name;
	}
	public void setFrom_person_name(String from_person_name) {
		this.from_person_name = from_person_name;
	}
	public String getCmesage() {
		return cmesage;
	}
	public void setCmesage(String cmesage) {
		this.cmesage = cmesage;
	}
	public String getFrom_person_id() {
		return from_person_id;
	}
	public void setFrom_person_id(String from_person_id) {
		this.from_person_id = from_person_id;
	}
	
}
