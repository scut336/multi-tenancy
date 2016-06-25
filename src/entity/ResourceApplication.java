package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ResourceApplication {

	@Id
	@Column(length=32)
	private String UserID;
	private int AppLimit;
	private int HDFSDirectoryQuota;
	private long Queue;
	private char Enable;

	
	public ResourceApplication(){}

	public ResourceApplication(String userID, int appLimit, int hDFSDirectoryQuota,
			long queue, char enable) {
		AppLimit = appLimit;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
		Queue = queue;
		Enable = enable;
		UserID = userID;
	}

	public int getAppLimit() {
		return AppLimit;
	}

	public void setAppLimit(int appLimit) {
		AppLimit = appLimit;
	}

	public int getHDFSDirectoryQuota() {
		return HDFSDirectoryQuota;
	}

	public void setHDFSDirectoryQuota(int hDFSDirectoryQuota) {
		HDFSDirectoryQuota = hDFSDirectoryQuota;
	}

	public long getQueue() {
		return Queue;
	}

	public void setQueue(long queue) {
		Queue = queue;
	}

	public char getEnable() {
		return Enable;
	}

	public void setEnable(char enable) {
		Enable = enable;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}
	
}
