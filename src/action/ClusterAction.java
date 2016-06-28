package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.QueueInfo;
import service.QueueInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.UserInfoDAO;
import service.impl.QueueInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.TaskInfoDAOImpl;
import service.impl.UserInfoDAOImpl;

public class ClusterAction extends SuperAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	//查看队列信息
	public String getCluster() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String strURL = "";
		strURL = "http://222.201.145.144:8088/ws/v1/cluster/info";  
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
	    json = json.getJSONObject("clusterInfo");
	    long starton = json.getLong("startedOn");
	    String state = json.getString("state");
	    String rversion = json.getString("resourceManagerVersion");
	    String hversion = json.getString("hadoopVersion");
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Long time=new Long(starton);  
	    String d = format.format(time);
	    String result = d+","+state+","+rversion+","+hversion;
	    inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	public String getMetrics() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String strURL = "";
		strURL = "http://222.201.145.144:8088/ws/v1/cluster/metrics";  
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
	    json=json.getJSONObject("clusterMetrics");
	    int appsSubmitted = json.getInt("appsSubmitted");
	    int appsCompleted = json.getInt("appsCompleted");
	    int appsPending = json.getInt("appsPending");
	    int appsRunning = json.getInt("appsRunning");
	    int appsFailed = json.getInt("appsFailed");
	    int appsKilled = json.getInt("appsKilled");
	    int reservedMB = json.getInt("reservedMB");
	    int availableMB = json.getInt("availableMB");
	    int allocatedMB = json.getInt("allocatedMB");
	    int reservedVirtualCores = json.getInt("reservedVirtualCores");
	    int availableVirtualCores = json.getInt("availableVirtualCores");
	    int allocatedVirtualCores = json.getInt("allocatedVirtualCores");
	    int lostNodes = json.getInt("lostNodes");
	    int unhealthyNodes = json.getInt("unhealthyNodes");
	    int decommissionedNodes = json.getInt("decommissionedNodes");
	    int rebootedNodes = json.getInt("rebootedNodes");
	    int activeNodes = json.getInt("activeNodes");
	    String result = appsSubmitted+","+appsCompleted+","+appsPending+","+appsRunning+","+appsFailed+","+appsKilled+","+
	    		reservedMB+","+availableMB+","+allocatedMB+","+reservedVirtualCores+","+availableVirtualCores+","+allocatedVirtualCores+","+
	    		lostNodes+","+unhealthyNodes+","+decommissionedNodes+","+rebootedNodes+","+activeNodes;
	    inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	public String getNodes() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		String strURL = "";
		strURL = "http://222.201.145.144:8088/ws/v1/cluster/nodes";  
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
	    json = json.getJSONObject("nodes");
	    JSONArray jsonArray = json.getJSONArray("node");
	    String result = "";
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    for(int i=0;i<jsonArray.length();i++){
	    	JSONObject temp = jsonArray.getJSONObject(i);
	    	Long time=new Long(temp.getLong("lastHealthUpdate"));  
	    	result+=temp.getString("nodeHostName")+","+temp.getString("state")+","+temp.getString("version")+","+
	    			format.format(time)+","+temp.getInt("usedMemoryMB")+","+temp.getInt("availMemoryMB")+","+
	    			temp.getInt("usedVirtualCores")+","+temp.getInt("availableVirtualCores")+";";
	    }
	    inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	public String getSum() throws Exception{
		if(!session.getAttribute("loginUserRole").equals("admin")){
			inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
			return "Ajax_Success";
		}
		QueueInfoDAO queueInfoDAO = new QueueInfoDAOImpl();
		TaskInfoDAO taskInfoDAO = new TaskInfoDAOImpl();
		UserInfoDAO userInfoDAO = new UserInfoDAOImpl();
		ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
		long usersum = userInfoDAO.userSum();
		long tasksum = taskInfoDAO.taskSum();
		long queueSum = queueInfoDAO.querySum();
		long applySum = resourceInfoDAO.applySum();
		String resString = usersum+","+tasksum+","+queueSum+","+applySum;
		inputStream=new ByteArrayInputStream(resString.getBytes("UTF-8"));
		return "Ajax_Success";	
		
	}
}
