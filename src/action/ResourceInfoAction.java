package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import service.JobInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.impl.JobInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.TaskInfoDAOImpl;

import com.opensymphony.xwork2.ModelDriven;

import db.JSONCreator;
import entity.JobInfo;
import entity.ResourceInfo;
import entity.ResourceInfoImpl;
import entity.TaskInfo;
import entity.UserInfo;

public class ResourceInfoAction extends SuperAction implements ModelDriven<TaskInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskInfo taskInfo = new TaskInfo();
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
	    				resourceInfo.getLastSubmitTime();
	    		inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
	    	}else {
	    		inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			}
	    }
	    else
	    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	//查看任务ID
	public String check() throws UnsupportedEncodingException{
		String ID=taskInfo.getId();
		TaskInfoDAO tdao = new TaskInfoDAOImpl();
		if(tdao.checkTaskInfoById(ID)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else{
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		}
		return "Ajax_Success";
	}
	
	//修改任务
	public String update(){
		TaskInfoDAO tdao = new TaskInfoDAOImpl();
		String id = request.getParameter("id");
		TaskInfo t = tdao.queryTaskById(id);
		if(t!=null){
			session.setAttribute("taskInfo", t);
			return "TaskInfo_update_success";
		}else
			return "TaskInfo_update_failed";
	}
	
	//保存修改任务
	public String save() throws Exception{
		TaskInfoDAO tdao = new TaskInfoDAOImpl();
		TaskInfo t = (TaskInfo)(session.getAttribute("taskInfo"));
		if(tdao.updateTask(t,taskInfo)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
			session.setAttribute("taskInfo", taskInfo);
		}
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//删除任务
	public String delete() throws UnsupportedEncodingException{
		TaskInfoDAO tdao = new TaskInfoDAOImpl();
		String id = request.getParameter("id");
		if(tdao.delTask(id))
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//查看作业
	public String showJobs() throws UnsupportedEncodingException{
		JobInfoDAO jobInfoDAO = new JobInfoDAOImpl();
		String id = (String)session.getAttribute("loginUserId");
		List<JobInfo> list = null;
		list = jobInfoDAO.queryJobInfo(id);
		if(list!=null&&list.size()>0){
			inputStream=new ByteArrayInputStream(JSONCreator.JobJSON(list).getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	//开始任务
	public String start(String jarName) throws IOException {
        String _url = "http://222.201.145.144:4567/app/start";
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "text/html");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("user", (String)session.getAttribute("loginUserName"));
        connection.setRequestProperty("app", jarName);
        connection.setRequestProperty("queue", "default");
        connection.setRequestProperty("input", "/user/smoketest/input");
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            line = reader.readLine();
            int first = line.indexOf("JobID is:");
            line = line.substring(first+9);
            first = line.indexOf("<br>");
            line = line.substring(0,first);
            return line;
        }
        else{
        	return "0";
        }
    }
	
	@Override
	public TaskInfo getModel() {
		// TODO Auto-generated method stub
		return taskInfo;
	}
}
