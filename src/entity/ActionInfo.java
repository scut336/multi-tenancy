package entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ActionInfo{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(length=100)
	private String user;
	private Date time;
	@Column(length=500)
	private String context;
	private char type;
	
	public ActionInfo(){}

	public ActionInfo(String user, Date time, String context, char type) {
		this.user = user;
		this.time = time;
		this.context = context;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
}
