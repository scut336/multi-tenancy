package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import service.JobInfoDAO;
import service.QueueInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.impl.JobInfoDAOImpl;
import service.impl.QueueInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.TaskInfoDAOImpl;

import com.opensymphony.xwork2.ModelDriven;

import db.JSONCreator;
import entity.JobInfo;
import entity.TaskInfo;
import entity.TaskInfoImpl;

public class TaskInfoAction extends SuperAction implements ModelDriven<TaskInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskInfo taskInfo = new TaskInfo();
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	//添加任务
	public String add() throws Exception{
		ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		taskInfo.setCreateDate(new Date());
		taskInfo.setUserInfo(session.getAttribute("loginUserId").toString());
		//String taskid = start(taskInfo.getTaskName());
		//if(!taskid.equals("0")){
		resourceInfoDAO.AddResourceJobTime(session.getAttribute("loginUserId").toString());
		taskInfo.setTaskID("0");
		if(taskInfoDAO.addTask(taskInfo)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else{
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		}
		//}
		//else{
		//	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		//}
		return "Ajax_Success";
	}

	//查看所有任务
	public String query() throws UnsupportedEncodingException{
		int page = Integer.parseInt(request.getParameter("page"));
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		if(!session.getAttribute("loginUserRole").equals("admin")){
			String id = (String)session.getAttribute("loginUserId");
			List<TaskInfoImpl> list=taskInfoDAO.findTask(id,page,10);
			if(list!=null&&list.size()>0){
				String reString = taskInfoDAO.pageSum() + ";0;";
				for(int i=0;i<list.size();i++){
					reString += list.get(i).getId() + "," + list.get(i).getTaskStatus() + "," + list.get(i).getTaskLog() + "," + list.get(i).getTaskResult() + "," +
							list.get(i).getTaskError() + "," + list.get(i).getCreateDate() + "," + list.get(i).getTaskID() + ";";
				}
				inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
			}else
				inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		}else{
			List<TaskInfoImpl> list = taskInfoDAO.findTask("",page,10);
			if(list!=null&&list.size()>0){
				String reString = taskInfoDAO.pageSum() + ";1;";
				for(int i=0;i<list.size();i++){
					reString += list.get(i).getId() + "," + list.get(i).getUserName() + "," + list.get(i).getTaskStatus() + "," + list.get(i).getTaskLog() + "," + list.get(i).getTaskResult() + "," +
							list.get(i).getTaskError() + "," + list.get(i).getCreateDate() + "," + list.get(i).getTaskID() + ";";
				}
				inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
			}else
				inputStream=new ByteArrayInputStream("00".getBytes("UTF-8"));
		}
		return "Ajax_Success";	
	}
	
	//查看任务进度
	public String queryProcess() throws Exception{
		String []index = request.getParameter("index").split(",");
		System.out.println(index.length);
		String strURL = "";
		String reString = "";
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		for(int i=0;i<index.length;i++){
			strURL = "http://222.201.145.144:8088/ws/v1/cluster/apps/" + index[i];  
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
		    System.out.println(buf);
		    int beginIx = buf.indexOf("\"progress\":");  
		    int endIx = buf.indexOf(",\"trackingUI\"");
		    String result = buf.substring(beginIx, endIx); 
		    result = result.replace("\"progress\":", "");
		    int indexF = result.indexOf(".");
		    result = result.substring(0, indexF);
		    if(Integer.parseInt(result)<=100)
		    	taskInfoDAO.updateTaskStatus(index[i], result);
		    reString += index[i] + ":" + result + ",";
		}
		System.out.println(reString);
		inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
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
	
	//修改任务HDFS路径
	public String UpdateHDFS() throws UnsupportedEncodingException{
		TaskInfoDAO tdao = new TaskInfoDAOImpl();
		String id = request.getParameter("id");
		String hdfs = request.getParameter("hdfs");
		if(tdao.updateTaskHDFS(id,hdfs)){
			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
		}else
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
	public String start() throws IOException {
		String name = request.getParameter("name");
		if(!session.getAttribute("loginUserRole").equals("admin")){
			name = session.getAttribute("loginUserName").toString();
		}
		String id = request.getParameter("id");
		String input = request.getParameter("input");
		
		String strURL = "http://222.201.145.144:50070/webhdfs/v1/user/"+name+"?op=GETCONTENTSUMMARY";  
	    try{
	    	URL url = new URL(strURL);  
		    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
		    InputStreamReader input2 = new InputStreamReader(httpConn.getInputStream(), "utf-8");  
		    BufferedReader bufReader = new BufferedReader(input2);  
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
		    res.UpdateResource(session.getAttribute("loginUserId").toString(), spaceConsumed);
	    }catch(Exception e){
	    	return starting(name, id,input);
	    }
		return starting(name, id,input);
    }
	
	//继续执行start
	public String starting(String name,String id,String input) throws IOException{
	    Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		String time=format.format(date);

		QueueInfoDAO queueInfoDAO = new QueueInfoDAOImpl();
		String queue = queueInfoDAO.queryQueueByUser(name);
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		String jarName = taskInfoDAO.findJarName(id);
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
        connection.setRequestProperty("queue", queue);
        connection.setRequestProperty("input", input);
        connection.setRequestProperty("startTime", time);
        System.out.println(jarName+","+queue+","+input+","+time);
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            String line="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            line = reader.readLine();
            if(line.indexOf("Input path does not exist")>0){
            	inputStream=new ByteArrayInputStream("-1".getBytes("UTF-8"));
            	return "Ajax_Success";
            }
            int first = line.indexOf("JobID is:");
            line = line.substring(first+9);
            first = line.indexOf("<br>");
            line = line.substring(0,first);
            System.out.println(line);
            if(taskInfoDAO.insertTaskID(id, line,time)){
    			inputStream=new ByteArrayInputStream("1".getBytes("UTF-8"));
    		}else{
    			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
    		}
        }
        else{
        	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
        }
        return "Ajax_Success";
	}
	
	//查询任务
	public String find() throws UnsupportedEncodingException{
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		String taskid = request.getParameter("id");
		if(!session.getAttribute("loginUserRole").equals("admin")){
			String id = (String)session.getAttribute("loginUserId");
			TaskInfoImpl taskInfo =taskInfoDAO.findTaskInfo(id,taskid);
			if(taskInfo!=null){
				String reString = "0;";
				reString += taskInfo.getId() + "," + taskInfo.getTaskStatus() + "," + taskInfo.getTaskLog() + "," + taskInfo.getTaskResult() + "," +
						taskInfo.getTaskError() + "," + taskInfo.getCreateDate() + "," + taskInfo.getTaskID() + ";";
				inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
			}else
				inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		}else{
			TaskInfoImpl taskInfo =taskInfoDAO.findTaskInfo("",taskid);
			if(taskInfo!=null){
				String reString = "1;";
				reString += taskInfo.getId() + "," + taskInfo.getUserName() + "," + taskInfo.getTaskStatus() + "," + taskInfo.getTaskLog() + "," + taskInfo.getTaskResult() + "," +
						taskInfo.getTaskError() + "," + taskInfo.getCreateDate() + "," + taskInfo.getTaskID() + ";";
				inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
			}else
				inputStream=new ByteArrayInputStream("00".getBytes("UTF-8"));
		}
		return "Ajax_Success";
	}
	
	//查询Jar
	public String findJar() throws UnsupportedEncodingException{
		int page = Integer.parseInt(request.getParameter("page"));
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		String taskname = request.getParameter("jar");
		String id = (String)session.getAttribute("loginUserId");
		List<TaskInfoImpl> taskInfo =taskInfoDAO.findTaskInfoJar(id,taskname,page,10);
		if(taskInfo!=null){
			String reString = taskInfoDAO.pageJarSum(id, taskname)+";";
			for(int i=0;i<taskInfo.size();i++)
				reString += taskInfo.get(i).getId() + "," + taskInfo.get(i).getTaskStatus() + "," + taskInfo.get(i).getTaskLog() + "," + taskInfo.get(i).getTaskResult() + "," +
					taskInfo.get(i).getTaskError() + "," + taskInfo.get(i).getCreateDate() + "," + taskInfo.get(i).getTaskID() + ";";
			inputStream=new ByteArrayInputStream(reString.getBytes("UTF-8"));
		}else
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";
	}
	
	@Override
	public TaskInfo getModel() {
		// TODO Auto-generated method stub
		return taskInfo;
	}
}
