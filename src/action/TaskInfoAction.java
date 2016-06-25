package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import service.JobInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.impl.JobInfoDAOImpl;
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
		    result = result.replace(".0", "");
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
	public String start() throws IOException {
		String id = request.getParameter("id");
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
            System.out.println(line);
            if(taskInfoDAO.insertTaskID(id, line)){
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
	
	@Override
	public TaskInfo getModel() {
		// TODO Auto-generated method stub
		return taskInfo;
	}
}
