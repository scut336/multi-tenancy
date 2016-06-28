package service;

import java.util.List;

import entity.ResourceApplication;
import entity.ResourceApplicationImpl;
import entity.ResourceInfo;
import entity.ResourceInfoImpl;
import entity.UserInfo;

public interface ResourceInfoDAO {
	
	public boolean AddResource(ResourceInfo r);
	
	public boolean DelResource(UserInfo u);
	
	public boolean UpdateResource(String id,long left);
	
	public boolean AddResourceApp(String id);
	
	public boolean SubResourceApp(String id);
	
	public boolean AddResourceJobTime(String id);
	
	public ResourceInfoImpl getHDFSDirectoryQuota(String id);
	
	public boolean ApplyResource(String userid,String queue,long hdfs,int num);
	
	public Object[] HDFS(String userID);
	
	public List<ResourceApplicationImpl> ShowResourceApplication(int page);
	
	public int pageSum();
	
	public long applySum();
	
	public boolean manageApply(String name,char enable);
	
	public List<ResourceApplicationImpl> ShowResource(int page);
	
	public boolean UpdateResourceInfo(String name,String queue,String hdfs,String job);
}
