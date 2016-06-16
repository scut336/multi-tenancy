package service;

import entity.ResourceInfo;
import entity.ResourceInfoImpl;
import entity.UserInfo;

public interface ResourceInfoDAO {
	
	public boolean AddResource(ResourceInfo r,String id);
	
	public boolean DelResource(UserInfo u);
	
	public boolean UpdateResource(String id,int left);
	
	public boolean AddResourceApp(String id);
	
	public boolean SubResourceApp(String id);
	
	public boolean AddResourceJobTime(String id);
	
	public ResourceInfoImpl getHDFSDirectoryQuota(String id);
}
