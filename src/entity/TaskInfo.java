package entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class TaskInfo {

	@Id
	@GeneratedValue(generator="id")
	@GenericGenerator(name="id",strategy="assigned")
	@Column(length=32)
	private String id;
	private String TaskStatus;
	private String TaskLog;
	private String TaskResult;
	private String TaskError;
	@Column(length=14)
	private String lastStartTime;
	private Date createDate;
	@Column(length=50)
	private String TaskID;
	@Column(length=50)
	private String TaskName;
	@Column(length=32)
	private String userInfo;
	
	public TaskInfo(){}
	
	public TaskInfo(String id, String taskStatus, String taskLog,
			String taskResult, String taskError, Date createDate,
			String taskID, String taskName, String userInfo) {
		this.id = id;
		TaskStatus = taskStatus;
		TaskLog = taskLog;
		TaskResult = taskResult;
		TaskError = taskError;
		this.createDate = createDate;
		TaskID = taskID;
		TaskName = taskName;
		this.userInfo = userInfo;
	}

	public TaskInfo(String id, String taskStatus, String taskLog,
			String taskResult, String taskError, Date createDate,
			String taskID, String userInfo) {
		this.id = id;
		TaskStatus = taskStatus;
		TaskLog = taskLog;
		TaskResult = taskResult;
		TaskError = taskError;
		this.createDate = createDate;
		TaskID = taskID;
		this.userInfo = userInfo;
	}

	public TaskInfo(String id, String taskStatus, String taskLog,
			String taskResult, String taskError, Date createDate) {
		this.id = id;
		TaskStatus = taskStatus;
		TaskLog = taskLog;
		TaskResult = taskResult;
		TaskError = taskError;
		this.createDate = createDate;
	}

	public TaskInfo(String id, String taskStatus, String taskLog,
			String taskResult, String taskError, Date createDate,
			String userInfo) {
		this.id = id;
		TaskStatus = taskStatus;
		TaskLog = taskLog;
		TaskResult = taskResult;
		TaskError = taskError;
		this.createDate = createDate;
		this.userInfo = userInfo;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getTaskName() {
		return TaskName;
	}

	public void setTaskName(String taskName) {
		TaskName = taskName;
	}

	public String getTaskID() {
		return TaskID;
	}

	public void setTaskID(String taskID) {
		TaskID = taskID;
	}

	public String getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(String lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

}
