package service;

import java.util.List;

import entity.ActionInfo;

public interface ActionInfoDAO {

	public boolean add(ActionInfo actionInfo);
	
	public List<ActionInfo> find(int page,int num);
	
	public int applyPageSum();
	
	public boolean del(String user,String time);
	
}
