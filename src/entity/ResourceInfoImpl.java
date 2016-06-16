package entity;

import java.util.Date;

public class ResourceInfoImpl {

	private int AppLimit;
	private int CurrentAppCount;
	private String HDFSDirectory;
	private int HDFSDirectoryQuota;
	private int HDFSDirectoryRemaining;
	private String Queue;
	private Date CreateTime;
	private int CreateUserID;
	private char Expired;
	private int SubmitJobTimes;
	private Date LastSubmitTime;
	
	public ResourceInfoImpl(int appLimit, int currentAppCount,
			String hDFSDirectory, int hDFSDirectoryQuota,
			int hDFSDirectoryRemaining, String queue, Date createTime,
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

	public String getQueue() {
		return Queue;
	}

	public void setQueue(String queue) {
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

}
