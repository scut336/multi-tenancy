package service;

import java.util.List;

import entity.TaskInfo;
import entity.UserInfo;

public interface TaskInfoDAO {

	public boolean addTask(TaskInfo t);
	
	public boolean delTask(String id);
	
	public boolean updateTask(TaskInfo oldT,TaskInfo newT);
	
	public List<TaskInfo> findTask(String id,int page,int num);
	
	public boolean checkTaskInfoById(String id);
	
	public TaskInfo queryTaskById(String id);
	
	public int pageSum();
	
	public boolean insertJarName(String id,String name);
	
	public String findJarName(String id);
	
	public boolean insertTaskID(String id,String taskID);
	
	public boolean updateTaskStatus(String TaskID,String num);
	
	public TaskInfo findTaskInfo(String id,String taskid);
}
