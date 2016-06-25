package action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import com.opensymphony.xwork2.ModelDriven;
import db.MD5creator;
import entity.ResourceInfo;
import entity.UserInfo;
import service.ResourceInfoDAO;
import service.UserInfoDAO;
import service.impl.ResourceInfoDAOImpl;
import service.impl.UserInfoDAOImpl;

public class UserInfoAction extends SuperAction  implements ModelDriven<UserInfo>{

	private static final long serialVersionUID = 1L;
	private UserInfo user = new UserInfo();
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	//遍历所有用户
	public String query() throws UnsupportedEncodingException{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		UserInfoDAO udao = new UserInfoDAOImpl();
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSum = udao.pageSum();
		List<UserInfo> list = udao.queryAllUserInfo(page);
		if(list!=null&&list.size()>0){
			String resString = pageSum + ";";
			for(int i=0;i<list.size();i++){
				resString += list.get(i).getId() + "," +list.get(i).getName() + "," + list.get(i).getRole() + "," +list.get(i).getDepartment() + "," +
						list.get(i).getCreateTime() + ";";
			}
			inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//修改用户
	public String update(){
		UserInfoDAO udao = new UserInfoDAOImpl();
		String Id = request.getParameter("id");
		UserInfo u = udao.queryUserInfoById(Id);
		if(u!=null){
			session.setAttribute("userInfo", u);
			return "UserInfo_update_success";
		}else
			return "UserInfo_update_failed";
	}
	
	//保存修改数据
	public String save() throws Exception{
		UserInfoDAO udao = new UserInfoDAOImpl();
		if(udao.updateUserInfo(user)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//删除用户
	public String delete() throws UnsupportedEncodingException{
		UserInfoDAO udao = new UserInfoDAOImpl();
		String Id = request.getParameter("id");
		if(udao.deleteUserInfo(Id))
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//新增用户
	public String add() throws UnsupportedEncodingException{
		UserInfoDAO udao = new UserInfoDAOImpl();
		String pass = user.getPassword();
		user.setPassword(MD5creator.MD5(pass));
		user.setCreateTime(new Date());
		if(udao.addUserInfo(user)){
			ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
			ResourceInfo resourceInfo = new ResourceInfo(10,0,user.getName(),1073741824,0,1,new Date(),1,'F',0,new Date(),user.getId());
			if(resourceInfoDAO.AddResource(resourceInfo)){
				inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
			}else {
				inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
		}
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}

	//查找用户
	public String find() throws UnsupportedEncodingException{
		UserInfoDAO udao = new UserInfoDAOImpl();
		String name = request.getParameter("name");
		UserInfo userInfo = udao.queryUserInfoByName(name);
		if(userInfo!=null){
			String resString = userInfo.getId() + "," +userInfo.getName() + "," + userInfo.getRole() + "," +userInfo.getDepartment() + "," +
					userInfo.getCreateTime();
			inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
		
	@Override
	public UserInfo getModel() {
		// TODO Auto-generated method stub
		return user;
	}
}
