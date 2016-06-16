package service;

import entity.UserInfo;
import entity.UserProfile;

public interface UserProfileDAO {

	public boolean addUserProfile(UserProfile up,UserInfo ui);
	
	public boolean delUserProfile(UserProfile u);
	
	public boolean updateProfile(UserProfile oldU,UserProfile newU);
	
	public UserProfile findUserProfile(String id);
}
