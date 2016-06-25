package entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserInfo {

	@Id
	@GeneratedValue(generator="id")
	@GenericGenerator(name="id",strategy="assigned")
	@Column(length=32)
	private String id;
	private String name;
	private String role;
	private String department;
	@Column(length=32)
	private String password;
	private Date createTime;
	
	public UserInfo(){}
	
	public UserInfo(String id, String role) {
		this.id = id;
		this.role = role;
	}

	public UserInfo(String id, String name, String role, String department,
			String password, Date createTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.department = department;
		this.password = password;
		this.createTime = createTime;
	}

	public UserInfo(String id, String name, String role, String department,
			Date createTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.department = department;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
