package entity;

import java.util.Date;

public class ResourceInfoImpl {

	private int AppLimit;
	private int CurrentAppCount;
	private String HDFSDirectory;
	private long HDFSDirectoryQuota;
	private long HDFSDirectoryRemaining;
	private String Queue;
	private Date CreateTime;
	private long CreateUserID;
	private char Expired;
	private int SubmitJobTimes;
	private Date LastSubmitTime;
	private String Capacity;
	private String MaxCapacity;
	private String UsedCapacity;
	private String resourceLimit;

	public ResourceInfoImpl(int appLimit, int currentAppCount,
			String hDFSDirectory, long hDFSDirectoryQuota,
			long hDFSDirectoryRemaining, String queue, Date createTime,
			long createUserID, char expired, int submitJobTimes,
			Date lastSubmitTime, String capacity, String maxCapacity,
			String usedCapacity, String resourceLimit) {
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
		Capacity = capacity;
		MaxCapacity = maxCapacity;
		UsedCapacity = usedCapacity;
		this.resourceLimit = resourceLimit;
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

	public long getHDFSDirectoryQuota() {
		return HDFSDirectoryQuota;
	}

	public void setHDFSDirectoryQuota(long hDFSDirectoryQuota) {
		HDFSDirectoryQuota = hDFSDirectoryQuota;
	}

	public long getHDFSDirectoryRemaining() {
		return HDFSDirectoryRemaining;
	}

	public void setHDFSDirectoryRemaining(long hDFSDirectoryRemaining) {
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

	public long getCreateUserID() {
		return CreateUserID;
	}

	public void setCreateUserID(long createUserID) {
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

	public String getCapacity() {
		return Capacity;
	}

	public void setCapacity(String capacity) {
		Capacity = capacity;
	}

	public String getResourceLimit() {
		return resourceLimit;
	}

	public void setResourceLimit(String resourceLimit) {
		this.resourceLimit = resourceLimit;
	}

	public String getMaxCapacity() {
		return MaxCapacity;
	}

	public void setMaxCapacity(String maxCapacity) {
		MaxCapacity = maxCapacity;
	}

	public String getUsedCapacity() {
		return UsedCapacity;
	}

	public void setUsedCapacity(String usedCapacity) {
		UsedCapacity = usedCapacity;
	}

}
