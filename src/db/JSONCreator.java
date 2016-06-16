package db;

import java.util.List;

import entity.JobInfo;
import entity.ResourceInfo;
import entity.TaskInfo;
import entity.UserInfo;

public class JSONCreator {
	
	public static String JobJSON(List<JobInfo> list){
		String jsonStr = null;  
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("{");
        for(int i=0;i<list.size();i++){
        	strBuffer.append("\""+i+"\":\"");
        	strBuffer.append(list.get(i).getName());
        	strBuffer.append("\",");
        }
        strBuffer.deleteCharAt(strBuffer.length()-1);
        strBuffer.append("}");
        jsonStr = strBuffer.toString();  
        return jsonStr;  
	}
	
	public static String HDFSJSON(double leave,String url,String queue,int max){
		String jsonStr = null;  
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("{\"leave\":\"");
        strBuffer.append(leave);
        strBuffer.append("\",");
        strBuffer.append("\"url\":\"");
        strBuffer.append(url);
        strBuffer.append("\",");
        strBuffer.append("\"queue\":\"");
        strBuffer.append(queue);
        strBuffer.append("\",");
        strBuffer.append("\"max\":\"");
        strBuffer.append(max);
        strBuffer.append("\"}");
        jsonStr = strBuffer.toString();  
        return jsonStr;  
	}
	
	public static String UserInfoJSON(UserInfo u){
		String jsonStr = null;  
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("\"userInfo\":{");
        strBuffer.append("\"id\":\"");
        strBuffer.append(u.getId());
        strBuffer.append("\",");
        strBuffer.append("\"name\":\"");
        strBuffer.append(u.getName());
        strBuffer.append("\",");
        strBuffer.append("\"role\":\"");
        strBuffer.append(u.getRole());
        strBuffer.append("\",");
        strBuffer.append("\"department\":\"");
        strBuffer.append(u.getDepartment());
        strBuffer.append("\",");
        strBuffer.append("\"time\":\"");
        strBuffer.append(u.getCreateTime());
        strBuffer.append("\"}");
        jsonStr = strBuffer.toString();  
        return jsonStr;  
	}
	/*
	public static String ResourceJSON(ResourceInfo r){
		String jsonStr = null;  
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("\"resource\":{");
        strBuffer.append("\"TaskQueue\":\"");
        strBuffer.append(r.getTaskQueue());
        strBuffer.append("\",");
        strBuffer.append("\"TaskConfig\":\"");
        strBuffer.append(r.getTaskConfig());
        strBuffer.append("\"}");
        jsonStr = strBuffer.toString();  
        return jsonStr;  
	}*/
	
	public static String TaskJSON(List<TaskInfo> taskInfos){
		String jsonStr = null;  
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("\"task\":[");
        for(TaskInfo t:taskInfos){
        	strBuffer.append("{\"id\":\"");
            strBuffer.append(t.getId());
            strBuffer.append("\",");
            strBuffer.append("\"TaskStatus\":\"");
            strBuffer.append(t.getTaskStatus());
            strBuffer.append("\",");
            strBuffer.append("\"TaskLog\":\"");
            strBuffer.append(t.getTaskLog());
            strBuffer.append("\",");
            strBuffer.append("\"TaskResult\":\"");
            strBuffer.append(t.getTaskResult());
            strBuffer.append("\",");
            strBuffer.append("\"TaskError\":\"");
            strBuffer.append(t.getTaskError());
            strBuffer.append("\",");
            strBuffer.append("\"createDate\":\"");
            strBuffer.append(t.getCreateDate());
            strBuffer.append("\"}");
            strBuffer.append(",");
        }
        strBuffer.deleteCharAt(strBuffer.length()-1);
        strBuffer.append("]");
        jsonStr = strBuffer.toString();  
        return jsonStr;  
	}
}
