package service;

import java.util.List;

import entity.TaskInfo;
import entity.TaskInfoImpl;
import entity.UserInfo;

public interface TaskInfoDAO {

	public boolean addTask(TaskInfo t);
	
	public boolean delTask(String id);
	
	public boolean updateTaskHDFS(String id,String hdfs);
	
	public List<TaskInfoImpl> findTask(String id,int page,int num);
	
	public boolean checkTaskInfoById(String id);
	
	public TaskInfo queryTaskById(String id);
	
	public int pageSum();
	
	public boolean insertJarName(String id,String name);
	
	public String findJarName(String id);
	
	public boolean insertTaskID(String id,String taskID);
	
	public boolean updateTaskStatus(String TaskID,String num);
	
	public TaskInfoImpl findTaskInfo(String id,String taskid);
	
	public List<TaskInfoImpl> findTaskInfoJar(String id,String taskname,int page,int num);
	
	public int pageJarSum(String id, String taskname);
}
