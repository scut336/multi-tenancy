package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import service.QueueInfoDAO;
import service.impl.QueueInfoDAOImpl;

import com.opensymphony.xwork2.ModelDriven;

import entity.TaskInfo;

public class QueueInfoAction extends SuperAction implements ModelDriven<TaskInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskInfo taskInfo = new TaskInfo();
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	//查看队列信息
	public String get() throws Exception{
		String strURL = "";
		strURL = "http://222.201.145.144:8088/ws/v1/cluster/scheduler";  
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
	    double usedCapacity = 0;
	    double maxCapacity = 0;
	    String queueName = "";
	    int memoryUnit = 0;
	    int vcoreUnit = 0;
	    JSONObject json=new JSONObject(buf);
	    JSONArray jsonArray = json.getJSONObject("scheduler").getJSONObject("schedulerInfo").getJSONObject("queues").getJSONArray("queue");
        for(int i=0;i<jsonArray.length();i++){  
            JSONObject jsonObject=jsonArray.getJSONObject(i);  
            usedCapacity=jsonObject.getDouble("usedCapacity");
            maxCapacity = jsonObject.getDouble("maxCapacity");
            queueName = jsonObject.getString("queueName");
            memoryUnit = jsonObject.getJSONObject("AMResourceLimit").getInt("memory");
            vcoreUnit = jsonObject.getJSONObject("AMResourceLimit").getInt("vCores");
        }
	    QueueInfoDAO queue = new QueueInfoDAOImpl();
	    if(queue.updateQueueCapacity(queueName, maxCapacity-usedCapacity)){
	    	String result = (maxCapacity-usedCapacity)+"|"+usedCapacity + "|" + maxCapacity + "|" + queueName + "|" + memoryUnit+"|"+vcoreUnit;
    		inputStream=new ByteArrayInputStream(result.getBytes("UTF-8"));
	    }
	    else
	    	inputStream=new ByteArrayInputStream("0".getBytes("UTF-8"));
		return "Ajax_Success";	
	}
	
	@Override
	public TaskInfo getModel() {
		// TODO Auto-generated method stub
		return taskInfo;
	}
}
