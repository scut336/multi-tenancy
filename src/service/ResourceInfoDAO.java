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
	
	public boolean UpdateResource(long id,long left);
	
	public boolean AddResourceApp(long id);
	
	public boolean SubResourceApp(long id);
	
	public boolean AddResourceJobTime(long id);
	
	public ResourceInfoImpl getHDFSDirectoryQuota(long id);
	
	public boolean ApplyResource(long userid,String queue,long hdfs,int num);
	
	public Object[] HDFS(long userID);
	
	public List<ResourceApplicationImpl> ShowResourceApplication(int page);
	
	public int pageSum();
	
	public long applySum();
	
	public boolean manageApply(String admin,String name,char enable);
	
	public List<ResourceApplicationImpl> ShowResource(int page);
	
	public boolean UpdateResourceInfo(String name,String queue,String hdfs,String job);
}
