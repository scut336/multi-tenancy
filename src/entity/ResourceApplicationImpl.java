package entity;

public class ResourceApplicationImpl {

	private String UserName;
	private String Queue;
	private int AppLimit;
	private long HDFSDirectoryQuota;
	
	public ResourceApplicationImpl(){}

	public ResourceApplicationImpl(String userName, String queue, int appLimit,
			long hDFSDirectoryQuota) {
		UserName = userName;
		Queue = queue;
		AppLimit = appLimit;
		HDFSDirectoryQuota = hDFSDirectoryQuota;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getQueue() {
		return Queue;
	}

	public void setQueue(String queue) {
		Queue = queue;
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

}
