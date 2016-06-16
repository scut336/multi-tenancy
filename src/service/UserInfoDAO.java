package service;
import java.util.List;
import entity.UserInfo;;

public interface UserInfoDAO {

	public List<UserInfo> UsersLogin(UserInfo u);
	
	public boolean checkUserInfoById(String Id);
	
	public boolean checkUserInfoByName(String name);
	
	public List<UserInfo> queryAllUserInfo(int page);
	
	public UserInfo queryUserInfoById(String Id);
	
	public UserInfo queryUserInfoByName(String name);
	
	public boolean addUserInfo(UserInfo u);
	
	public boolean updateUserInfo(UserInfo u);
	
	public boolean deleteUserInfo(String id);
	
	public int pageSum();
	
}
