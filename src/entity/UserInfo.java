package entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(length=100)
	private String name;
	@Column(length=32)
	private String role;
	@Column(length=100)
	private String department;
	@Column(length=32)
	private String password;
	private Date createTime;
	
	public UserInfo(){}
	
	public UserInfo(long id, String role) {
		this.id = id;
		this.role = role;
	}

	public UserInfo(long id, String name, String role, String department,
			String password, Date createTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.department = department;
		this.password = password;
		this.createTime = createTime;
	}

	public UserInfo(String name, String role, String department,
			String password, Date createTime) {
		this.name = name;
		this.role = role;
		this.department = department;
		this.password = password;
		this.createTime = createTime;
	}

	public UserInfo(long id, String name, String role, String department,
			Date createTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.department = department;
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
