package service;

import java.util.List;

import entity.TaskInfo;
import entity.TaskInfoImpl;
import entity.UserInfo;

public interface TaskInfoDAO {

	public boolean addTask(TaskInfo t);
	
	public boolean delTask(String id);
	
	public boolean updateTaskHDFS(String id,String hdfs);
	
	public List<TaskInfoImpl> findTask(long id,int page,int num);
	
	public boolean checkTaskInfoById(String id);
	
	public TaskInfo queryTaskById(String id);
	
	public int pageSum();
	
	public long taskSum();
	
	public boolean insertJarName(String id,String name);
	
	public String findJarName(String id);
	
	public String findUserName(String id);
	
	public String findLastStartTime(String id);
	
	public boolean insertTaskID(String id,String taskID,String time);
	
	public boolean updateTaskStatus(String TaskID,String num);
	
	public TaskInfoImpl findTaskInfo(long id,String taskid);
	
	public List<TaskInfoImpl> findTaskInfoJar(long id,String taskname,int page,int num);
	
	public int pageJarSum(long id, String taskname);
	
}
