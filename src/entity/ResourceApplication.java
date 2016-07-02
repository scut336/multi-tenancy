package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ResourceApplication {

	@Id
	@GeneratedValue(generator="id")
	@GenericGenerator(name="id",strategy="assigned")
	private long UserID;
	private int AppLimit;
	private long HDFSDirectoryQuota;
	private long Queue;
	private char Enable;

	
	public ResourceApplication(){}

	public ResourceApplication(long userID, int appLimit, int hDFSDirectoryQuota,
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

	public long getHDFSDirectoryQuota() {
		return HDFSDirectoryQuota;
	}

	public void setHDFSDirectoryQuota(long hDFSDirectoryQuota) {
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

	public long getUserID() {
		return UserID;
	}

	public void setUserID(long userID) {
		UserID = userID;
	}
	
}
