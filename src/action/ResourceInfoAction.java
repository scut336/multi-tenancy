package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import service.JobInfoDAO;
import service.ResourceInfoDAO;
import service.UserInfoDAO;
import service.impl.JobInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.UserInfoDAOImpl;
import entity.JobInfo;
import entity.ResourceApplication;
import entity.ResourceApplicationImpl;
import entity.ResourceInfoImpl;
import entity.UserInfo;

public class ResourceInfoAction extends SuperAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	//查看HDFS资源
	public String get() throws Exception{
		String strURL = "";
		strURL = "http://222.201.145.144:50070/webhdfs/v1/user/"+session.getAttribute("loginUserName")+"?op=GETCONTENTSUMMARY";  
	    URL url = new URL(strURL);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    InputStreamReader input = new InputStreamReader(httpConn  
	            .getInputStream(), "utf-8");  
	    BufferedReader bufReader = new BufferedReader(input);  
	    String line = "";  
	    StringBuilder contentBuf = new StringBuilder();  
	    while ((line = bufReader.readLine()) != null) {  
	        contentBuf.append(line);  
	    }  
	    String buf = contentBuf.toString(); 
	    JSONObject json=new JSONObject(buf);
	    JSONObject jsonObject = json.getJSONObject("ContentSummary");
	    int spaceConsumed = jsonObject.getInt("spaceConsumed");
	    ResourceInfoDAO res = new ResourceInfoDAOImpl();
	    if(res.UpdateResource(session.getAttribute("loginUserId").toString(), spaceConsumed)){
	    	ResourceInfoImpl resourceInfo = res.getHDFSDirectoryQuota(session.getAttribute("loginUserId").toString());
	    	if(resourceInfo!=null){
	    		int left = resourceInfo.getHDFSDirectoryQuota() - spaceConsumed;
	    		String result = spaceConsumed + ","+left+","+resourceInfo.getHDFSDirectoryQuota()+","+resourceInfo.getAppLimit()+","+resourceInfo.getCurrentAppCount()+","+(resourceInfo.getAppLimit()-resourceInfo.getCurrentAppCount())+","+
	    				resourceInfo.getHDFSDirectory()+","+resourceInfo.getQueue()+","+resourceInfo.getCreateTime()+","+resourceInfo.getSubmitJobTimes()+","+
	    				resourceInfo.getLastSubmitTime()+","+resourceInfo.getCapacity()+","+resourceInfo.getResourceLimit();
	    		inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
	    	}else {
	    		inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
	    }
	    else
	    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	//提交申请
	public String apply() throws Exception{
		String userID = session.getAttribute("loginUserId").toString();
		String queue = request.getParameter("queue");
		int hdfs = Integer.parseInt(request.getParameter("hdfs"));
		int num = Integer.parseInt(request.getParameter("num"));
		ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
		if(resourceInfoDAO.ApplyResource(userID, queue, hdfs, num)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else{
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		}
		return "Ajax_Success";	
	}
	
	//查看HDFS
	public String HDFS() throws Exception{
		String userID = session.getAttribute("loginUserId").toString();
		ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
		Object[] res = resourceInfoDAO.HDFS(userID);
		String reString = res[0].toString()+"|"+res[1].toString();
		inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	//资源申请管理
	public String confirm() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		ResourceInfoDAO rdao = new ResourceInfoDAOImpl();
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSum = rdao.pageSum();
		List<ResourceApplicationImpl> list = rdao.ShowResourceApplication(page);
		if(list!=null&&list.size()>0){
			String resString = pageSum + ";";
			for(int i=0;i<list.size();i++){
				resString += list.get(i).getUserName() + "," +list.get(i).getQueue() + "," + list.get(i).getHDFSDirectoryQuota() + "," +list.get(i).getAppLimit() + ";";
			}
			inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//同意申请
	public String accept() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String name = request.getParameter("name");
		ResourceInfoDAO rdao = new ResourceInfoDAOImpl();
		if(rdao.manageApply(name, 'T'))
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//拒绝申请
	public String refuse() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String name = request.getParameter("name");
		ResourceInfoDAO rdao = new ResourceInfoDAOImpl();
		if(rdao.manageApply(name, 'F'))
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//广搜HDFS路径
	public String HDFSRoute() throws Exception{
		String res = "";
		Queue<String> sk = new LinkedList<String>();
		String strURL = "";
		strURL = "http://222.201.145.144:50070/webhdfs/v1/user/"+session.getAttribute("loginUserName");  
	    sk.add(strURL+"?op=LISTSTATUS");
	    int size = sk.size();
	    while(!sk.isEmpty()){
	    	if(size==0){
	    		res+="|";
	    		size = sk.size();
	    	}
	    	String temp = sk.poll();
	    	res += temp+",";
	    	URL url = new URL(temp);  
		    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
		    InputStreamReader input = new InputStreamReader(httpConn  
		            .getInputStream(), "utf-8");  
		    BufferedReader bufReader = new BufferedReader(input);  
		    String line = "";  
		    StringBuilder contentBuf = new StringBuilder();  
		    while ((line = bufReader.readLine()) != null) {  
		        contentBuf.append(line);  
		    }  
		    String buf = contentBuf.toString();
		    JSONObject json=new JSONObject(buf);
		    JSONObject jsonObject = json.getJSONObject("FileStatuses");
		    JSONArray jsonArray = jsonObject.getJSONArray("FileStatus");
		    for(int ii=0;ii<jsonArray.length();ii++){
		    	JSONObject tempObject = jsonArray.getJSONObject(ii);
		    	res 
		    }
	    }
		
	    
	    return null;
	}
	
	//查看作业情况
	public String Job() throws Exception{
		String strURL = "";
		strURL = "http://222.201.145.144:50070/webhdfs/v1/user/"+session.getAttribute("loginUserName")+"?op=LISTSTATUS";  
	    URL url = new URL(strURL);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    InputStreamReader input = new InputStreamReader(httpConn  
	            .getInputStream(), "utf-8");  
	    BufferedReader bufReader = new BufferedReader(input);  
	    String line = "";  
	    StringBuilder contentBuf = new StringBuilder();  
	    while ((line = bufReader.readLine()) != null) {  
	        contentBuf.append(line);  
	    }  
	    String buf = contentBuf.toString(); 
	    JSONObject json=new JSONObject(buf);
	    JSONObject jsonObject = json.getJSONObject("ContentSummary");
	    int spaceConsumed = jsonObject.getInt("spaceConsumed");
	    
		inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	//查看资源使用情况
	public String listResource() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		ResourceInfoDAO rdao = new ResourceInfoDAOImpl();
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSum = rdao.pageSum();
		List<ResourceApplicationImpl> list = rdao.ShowResource(page);
		if(list!=null&&list.size()>0){
			String resString = pageSum + ";";
			for(int i=0;i<list.size();i++){
				resString += list.get(i).getUserName() + "," +list.get(i).getQueue() + "," + list.get(i).getHDFSDirectoryQuota() + "," +list.get(i).getAppLimit() + ";";
			}
			inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
}
