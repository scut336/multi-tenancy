package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import db.ProjectProperties;
import service.ActionInfoDAO;
import service.JobInfoDAO;
import service.QueueInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.UserInfoDAO;
import service.impl.ActionInfoDAOImpl;
import service.impl.JobInfoDAOImpl;
import service.impl.QueueInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.TaskInfoDAOImpl;
import service.impl.UserInfoDAOImpl;
import entity.ActionInfo;
import entity.JobInfo;
import entity.ResourceApplicationImpl;
import entity.ResourceInfoImpl;

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
		strURL = ProjectProperties.getValue("scheduler");  
	    URL url = new URL(strURL);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	    if (httpConn.getResponseCode() == 200){
	    	InputStreamReader input = new InputStreamReader(httpConn  
		            .getInputStream(), "utf-8");  
		    BufferedReader bufReader = new BufferedReader(input);  
		    String line = "";  
		    StringBuilder contentBuf = new StringBuilder();  
		    while ((line = bufReader.readLine()) != null) {  
		        contentBuf.append(line);  
		    }  
		    String buf = contentBuf.toString();
		    double capacity = 0;
		    double usedCapacity = 0;
		    double maxCapacity = 0;
		    String queueName = "";
		    int memoryUnit = 0;
		    int vcoreUnit = 0;
		    JSONObject json=new JSONObject(buf);
		    JSONArray jsonArray = json.getJSONObject("scheduler").getJSONObject("schedulerInfo").getJSONObject("queues").getJSONArray("queue");
	        for(int i=0;i<jsonArray.length();i++){  
	            JSONObject jsonObject=jsonArray.getJSONObject(i);
	            capacity = jsonObject.getDouble("capacity");
	            usedCapacity=jsonObject.getDouble("usedCapacity");
	            maxCapacity = jsonObject.getDouble("maxCapacity");
	            queueName = jsonObject.getString("queueName");
	            memoryUnit = jsonObject.getJSONObject("AMResourceLimit").getInt("memory");
	            vcoreUnit = jsonObject.getJSONObject("AMResourceLimit").getInt("vCores");
	            QueueInfoDAO queue = new QueueInfoDAOImpl();
	            queue.updateQueueCapacity(queueName, capacity,maxCapacity,usedCapacity,memoryUnit,vcoreUnit);
	        }
	    }
	    
        
		strURL = "";
		strURL = ProjectProperties.getValue("hdfs")+"user/"+session.getAttribute("loginUserName")+"?op=GETCONTENTSUMMARY";  
	    url = new URL(strURL);  
	    httpConn = (HttpURLConnection) url.openConnection();  
	    if (httpConn.getResponseCode() == 200){
	    	InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");  
		    BufferedReader bufReader = new BufferedReader(input);  
		    String line = "";  
		    StringBuilder contentBuf = new StringBuilder();  
		    while ((line = bufReader.readLine()) != null) {  
		        contentBuf.append(line);  
		    }  
		    String buf = contentBuf.toString(); 
		    JSONObject json=new JSONObject(buf);
		    JSONObject jsonObject = json.getJSONObject("ContentSummary");
		    long spaceConsumed = jsonObject.getLong("spaceConsumed");
		    ResourceInfoDAO res = new ResourceInfoDAOImpl();
		    if(res.UpdateResource(Long.parseLong(session.getAttribute("loginUserId").toString()), spaceConsumed)){
		    	ResourceInfoImpl resourceInfo = res.getHDFSDirectoryQuota(Long.parseLong(session.getAttribute("loginUserId").toString()));
		    	if(resourceInfo!=null){
		    		long left = resourceInfo.getHDFSDirectoryQuota() - spaceConsumed;
		    		String result = spaceConsumed + ","+left+","+resourceInfo.getHDFSDirectoryQuota()+","+resourceInfo.getAppLimit()+","+resourceInfo.getCurrentAppCount()+","+(resourceInfo.getAppLimit()-resourceInfo.getCurrentAppCount())+","+
		    				resourceInfo.getHDFSDirectory()+","+resourceInfo.getQueue()+","+resourceInfo.getCreateTime()+","+resourceInfo.getSubmitJobTimes()+","+
		    				resourceInfo.getLastSubmitTime()+","+resourceInfo.getCapacity()+","+resourceInfo.getMaxCapacity()+","+resourceInfo.getUsedCapacity()+","+resourceInfo.getResourceLimit();
		    		inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
		    	}else {
		    		inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
				}
		    }
		    else
		    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
	    }else{
	    	ResourceInfoDAO res = new ResourceInfoDAOImpl();
	    	ResourceInfoImpl resourceInfo = res.getHDFSDirectoryQuota(Long.parseLong(session.getAttribute("loginUserId").toString()));
	    	if(resourceInfo!=null){
	    		long left = resourceInfo.getHDFSDirectoryQuota() - resourceInfo.getHDFSDirectoryRemaining();
	    		String result = resourceInfo.getHDFSDirectoryRemaining() + ","+left+","+resourceInfo.getHDFSDirectoryQuota()+","+resourceInfo.getAppLimit()+","+resourceInfo.getCurrentAppCount()+","+(resourceInfo.getAppLimit()-resourceInfo.getCurrentAppCount())+","+
	    				resourceInfo.getHDFSDirectory()+","+resourceInfo.getQueue()+","+resourceInfo.getCreateTime()+","+resourceInfo.getSubmitJobTimes()+","+
	    				resourceInfo.getLastSubmitTime()+","+resourceInfo.getCapacity()+","+resourceInfo.getMaxCapacity()+","+resourceInfo.getUsedCapacity()+","+resourceInfo.getResourceLimit();
	    		inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
	    	}else {
	    		inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
	    }
	    
		return "Ajax_Success";	
	}
	
	//提交申请
	public String apply() throws Exception{
		long userID = Long.parseLong(session.getAttribute("loginUserId").toString());
		String queue = request.getParameter("queue");
		long hdfs = Long.parseLong(request.getParameter("hdfs"));
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
		long userID = Long.parseLong(session.getAttribute("loginUserId").toString());
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
		if(rdao.manageApply(session.getAttribute("loginUserName").toString(),name, 'T'))
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
		if(rdao.manageApply(session.getAttribute("loginUserName").toString(),name, 'F'))
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//查看申请记录
	public String applyHistory() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		int page = Integer.parseInt(request.getParameter("page"));
		ActionInfoDAO actionInfoDAO = new ActionInfoDAOImpl();
		int pageSum = actionInfoDAO.applyPageSum();
		List<ActionInfo> list = actionInfoDAO.find(page, 10);
		if(list!=null&&list.size()>0){
			String resString = pageSum + ";";
			for(int i=0;i<list.size();i++){
				resString += list.get(i).getUser() + "~" +list.get(i).getTime() + "~" + list.get(i).getContext() + ";";
			}
			inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//删除申请记录
	public String delHistory() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String user = request.getParameter("user");
		String time = request.getParameter("time");
		ActionInfoDAO actionInfoDAO = new ActionInfoDAOImpl();
		if(actionInfoDAO.del(user,time)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//HDFS路径
	public String HDFSRoute() throws Exception{
		String res = "";
		String addString = request.getParameter("add");
		String strURL = ProjectProperties.getValue("hdfs")+"user/"+session.getAttribute("loginUserName")+"/"+addString;  
		URL url = new URL(strURL+"?op=LISTSTATUS");  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    if (httpConn.getResponseCode() == 200){
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
			for(int i=0;i<jsonArray.length();i++){
				JSONObject temp = jsonArray.getJSONObject(i);
				if(temp.getString("type").equals("DIRECTORY"))
					res+="1,"+temp.getString("pathSuffix")+"|";
				else
					res+="0,"+temp.getString("pathSuffix")+","+
					ProjectProperties.getValue("download")+"user/"+session.getAttribute("loginUserName")+"/"+addString+"/"+temp.getString("pathSuffix")+"?op=OPEN&namenoderpcaddress=master:9000&amp;offset=0"+"|";
			}
			inputStream=new ByteArrayInputStream(res.getBytes("UTF-8"));
	    }else{
	    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
	    }
		return "Ajax_Success";
	}
	
	//HDFS路径
	public String HDFSComRoute() throws Exception{
		String res = "";
		String strURL = ProjectProperties.getValue("hdfs")+"data/share?op=LISTSTATUS";  
		URL url = new URL(strURL);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    if (httpConn.getResponseCode() == 200){
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
			for(int i=0;i<jsonArray.length();i++){
				JSONObject temp = jsonArray.getJSONObject(i);
				res+="0,"+temp.getString("pathSuffix")+","+
					ProjectProperties.getValue("download")+"data/share/"+temp.getString("pathSuffix")+"?op=OPEN&namenoderpcaddress=master:9000&amp;offset=0"+"|";
			}
			inputStream=new ByteArrayInputStream(res.getBytes("UTF-8"));
	    }else{
	    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
	    }
		return "Ajax_Success";
	}
	
	//查看作业情况
	public String Job() throws Exception{
		JobInfoDAO jobInfoDAO = new JobInfoDAOImpl();
	    List<JobInfo> res = jobInfoDAO.queryJobInfo(Long.parseLong(session.getAttribute("loginUserId").toString()));
		String reString = "";
	    for(int i=0;i<res.size();i++)
			reString += res.get(i).getName()+"|";
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
		UserInfoDAO userInfoDAO = new UserInfoDAOImpl();
		int pageSum = userInfoDAO.pageSum();
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
	
	//更新资源信息
	public String updateResource() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String name = request.getParameter("name");
		String queue = request.getParameter("queue");
		String hdfs = request.getParameter("hdfs");
		String job = request.getParameter("job");
		ResourceInfoDAO rdao = new ResourceInfoDAOImpl();
		if(rdao.UpdateResourceInfo(name, queue, hdfs, job)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//结果路径
	public String ResultRoute() throws Exception{
		String res = "";
		String taskid = request.getParameter("id");
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		String jarname = taskInfoDAO.findJarName(taskid);
		String time = taskInfoDAO.findLastStartTime(taskid);
		int indexF = jarname.indexOf(".");
		jarname = jarname.substring(0,indexF);
		String username = taskInfoDAO.findUserName(taskid);
		String strURL = ProjectProperties.getValue("hdfs")+"user/"+username+"/output/"+jarname+"/"+time;  
		URL url = new URL(strURL+"?op=LISTSTATUS");  
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
	    strURL = strURL.replace("50070", "50075");
		for(int i=0;i<jsonArray.length();i++){
			JSONObject temp = jsonArray.getJSONObject(i);
			res+=temp.getString("pathSuffix")+","+
						strURL+"/"+temp.getString("pathSuffix")+"?op=OPEN&namenoderpcaddress=master:9000&amp;offset=0"+"|";
		}
		inputStream=new ByteArrayInputStream(res.getBytes("UTF-8"));
		return "Ajax_Success";
	}
}
