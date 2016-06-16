package action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import service.UserInfoDAO;
import service.impl.UserInfoDAOImpl;

import com.opensymphony.xwork2.ModelDriven;

import entity.UserInfo;

public class UserAction extends SuperAction implements ModelDriven<UserInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfo user = new UserInfo();
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	//用户登陆
	public String login(){
		UserInfoDAO udao = new UserInfoDAOImpl();
		List<UserInfo> list = udao.UsersLogin(user);
		if(list!=null&&list.size()>0){
			session.setAttribute("loginUserName", user.getName());
			session.setAttribute("loginUserRole", list.get(0).getRole());
			session.setAttribute("loginUserId", list.get(0).getId());
			request.setAttribute("login", "success");
			return "Login_Success";
		}else{
			request.setAttribute("login", "failed");
			return "Login_Failed";
		}
	}
	
	//用户退出
	public String logout(){
		session.invalidate();
		return "Logout_Success";
	}

	//检查用户ID是否重复
	public String check() throws UnsupportedEncodingException{
		String ID=user.getId();
		String name = user.getName();
		UserInfoDAO udao = new UserInfoDAOImpl();
		if(ID!=null){
			if(udao.checkUserInfoById(ID)){
				inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
			}else{
				inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
		}else if(name!=null){
			if(udao.checkUserInfoByName(name)){
				inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
			}else{
				inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
		}
		return "Ajax_Success";
	}
	
	@Override
	public UserInfo getModel() {
		// TODO Auto-generated method stub
		return user;
	}

}
