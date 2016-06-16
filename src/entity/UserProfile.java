package entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Entity
public class UserProfile {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(length=2048)
	private String HDFS;
	@Column(length=2048)
	private String MapReduce;
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="userID",unique=true)
	private UserInfo userID;
	
	public UserProfile(){}

	public UserProfile(String hDFS, String mapReduce, UserInfo userID) {
		HDFS = hDFS;
		MapReduce = mapReduce;
		this.userID = userID;
	}

	public UserProfile(String hDFS, String mapReduce) {
		HDFS = hDFS;
		MapReduce = mapReduce;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHDFS() {
		return HDFS;
	}

	public void setHDFS(String hDFS) {
		HDFS = hDFS;
	}

	public String getMapReduce() {
		return MapReduce;
	}

	public void setMapReduce(String mapReduce) {
		MapReduce = mapReduce;
	}

	public UserInfo getUserID() {
		return userID;
	}

	public void setUserID(UserInfo userID) {
		this.userID = userID;
	}

}
