package entity;

public class TaskInfoImpl {

	private String id;
	private String TaskStatus;
	private String TaskLog;
	private String TaskResult;
	private String TaskError;
	private String lastStartTime;
	private String createDate;
	private String TaskID;
	private String TaskName;
	private String userName;
	
	public TaskInfoImpl(){}

	public TaskInfoImpl(String id, String taskStatus, String taskLog,
			String taskResult, String taskError, String lastStartTime,
			String createDate, String taskID, String userName) {
		this.id = id;
		TaskStatus = taskStatus;
		TaskLog = taskLog;
		TaskResult = taskResult;
		TaskError = taskError;
		this.lastStartTime = lastStartTime;
		this.createDate = createDate;
		TaskID = taskID;
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskStatus() {
		return TaskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		TaskStatus = taskStatus;
	}

	public String getTaskLog() {
		return TaskLog;
	}

	public void setTaskLog(String taskLog) {
		TaskLog = taskLog;
	}

	public String getTaskResult() {
		return TaskResult;
	}

	public void setTaskResult(String taskResult) {
		TaskResult = taskResult;
	}

	public String getTaskError() {
		return TaskError;
	}

	public void setTaskError(String taskError) {
		TaskError = taskError;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTaskID() {
		return TaskID;
	}

	public void setTaskID(String taskID) {
		TaskID = taskID;
	}

	public String getTaskName() {
		return TaskName;
	}

	public void setTaskName(String taskName) {
		TaskName = taskName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(String lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	
}
