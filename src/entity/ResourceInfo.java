package entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ResourceInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private int AppLimit;
	private int CurrentAppCount;
	private String HDFSDirectory;
	private int HDFSDirectoryQuota;
	private int HDFSDirectoryRemaining;
	private int Queue;
	private Date CreateTime;
	private int CreateUserID;
	private char Expired;
	private int SubmitJobTimes;
	private Date LastSubmitTime;

	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="userID",unique=true)
	private UserInfo userID;
	
	public ResourceInfo(){}

	public ResourceInfo(long id, int appLimit, int currentAppCount,
			String hDFSDirectory, int hDFSDirectoryQuota,
			int hDFSDirectoryRemaining, int queue, Date createTime,
			int createUserID, char expired, int submitJobTimes,
			Date lastSubmitTime, UserInfo userID) {
		this.id = id;
		AppLimit = appLimit;
		CurrentAppCount = currentAppCount;
		HDFSDirectory = hDFSDirectory;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
		HDFSDirectoryRemaining = hDFSDirectoryRemaining;
		Queue = queue;
		CreateTime = createTime;
		CreateUserID = createUserID;
		Expired = expired;
		SubmitJobTimes = submitJobTimes;
		LastSubmitTime = lastSubmitTime;
		this.userID = userID;
	}

	public ResourceInfo(int appLimit, int currentAppCount,
			String hDFSDirectory, int hDFSDirectoryQuota,
			int hDFSDirectoryRemaining, int queue, Date createTime,
			int createUserID, char expired, int submitJobTimes,
			Date lastSubmitTime, UserInfo userID) {
		AppLimit = appLimit;
		CurrentAppCount = currentAppCount;
		HDFSDirectory = hDFSDirectory;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
		HDFSDirectoryRemaining = hDFSDirectoryRemaining;
		Queue = queue;
		CreateTime = createTime;
		CreateUserID = createUserID;
		Expired = expired;
		SubmitJobTimes = submitJobTimes;
		LastSubmitTime = lastSubmitTime;
		this.userID = userID;
	}

	public ResourceInfo(int appLimit, int currentAppCount,
			String hDFSDirectory, int hDFSDirectoryQuota,
			int hDFSDirectoryRemaining, int queue, Date createTime,
			int createUserID, char expired, int submitJobTimes,
			Date lastSubmitTime) {
		AppLimit = appLimit;
		CurrentAppCount = currentAppCount;
		HDFSDirectory = hDFSDirectory;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
		HDFSDirectoryRemaining = hDFSDirectoryRemaining;
		Queue = queue;
		CreateTime = createTime;
		CreateUserID = createUserID;
		Expired = expired;
		SubmitJobTimes = submitJobTimes;
		LastSubmitTime = lastSubmitTime;
	}

	
	public ResourceInfo(int appLimit, int currentAppCount,
			int hDFSDirectoryQuota, int hDFSDirectoryRemaining) {
		AppLimit = appLimit;
		CurrentAppCount = currentAppCount;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
		HDFSDirectoryRemaining = hDFSDirectoryRemaining;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAppLimit() {
		return AppLimit;
	}

	public void setAppLimit(int appLimit) {
		AppLimit = appLimit;
	}

	public int getCurrentAppCount() {
		return CurrentAppCount;
	}

	public void setCurrentAppCount(int currentAppCount) {
		CurrentAppCount = currentAppCount;
	}

	public String getHDFSDirectory() {
		return HDFSDirectory;
	}

	public void setHDFSDirectory(String hDFSDirectory) {
		HDFSDirectory = hDFSDirectory;
	}

	public int getHDFSDirectoryQuota() {
		return HDFSDirectoryQuota;
	}

	public void setHDFSDirectoryQuota(int hDFSDirectoryQuota) {
		HDFSDirectoryQuota = hDFSDirectoryQuota;
	}

	public int getHDFSDirectoryRemaining() {
		return HDFSDirectoryRemaining;
	}

	public void setHDFSDirectoryRemaining(int hDFSDirectoryRemaining) {
		HDFSDirectoryRemaining = hDFSDirectoryRemaining;
	}

	public int getQueue() {
		return Queue;
	}

	public void setQueue(int queue) {
		Queue = queue;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public int getCreateUserID() {
		return CreateUserID;
	}

	public void setCreateUserID(int createUserID) {
		CreateUserID = createUserID;
	}

	public char getExpired() {
		return Expired;
	}

	public void setExpired(char expired) {
		Expired = expired;
	}

	public int getSubmitJobTimes() {
		return SubmitJobTimes;
	}

	public void setSubmitJobTimes(int submitJobTimes) {
		SubmitJobTimes = submitJobTimes;
	}

	public Date getLastSubmitTime() {
		return LastSubmitTime;
	}

	public void setLastSubmitTime(Date lastSubmitTime) {
		LastSubmitTime = lastSubmitTime;
	}

	public UserInfo getUserID() {
		return userID;
	}

	public void setUserID(UserInfo userID) {
		this.userID = userID;
	}

	
}
