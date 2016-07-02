package service;

import java.util.List;

import entity.JobInfo;

public interface JobInfoDAO {

	public boolean addJob(JobInfo j);
	
	public List<JobInfo> queryJobInfo(long id);
}
