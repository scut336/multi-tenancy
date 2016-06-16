package action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.struts2.ServletActionContext;

import entity.JobInfo;
import service.JobInfoDAO;
import service.ResourceInfoDAO;
import service.TaskInfoDAO;
import service.impl.JobInfoDAOImpl;
import service.impl.ResourceInfoDAOImpl;
import service.impl.TaskInfoDAOImpl;

public class JarFileUploadAction extends SuperAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File file;
	private String fileFileName;  
    private String fileContentType;
    private String message = "上传成功";
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String execute() throws Exception{
		ResourceInfoDAO resourceInfoDAO = new ResourceInfoDAOImpl();
		if(!resourceInfoDAO.AddResourceApp(session.getAttribute("loginUserId").toString())){
			this.setMessage("ResourceErr");
            return ERROR;
		}
		String _url = "http://222.201.145.144:4567/app/upload";
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        File f = this.getFile();
        FileInputStream fis = new FileInputStream(f);  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        byte[]b = new byte[1024];  
        int n;  
        while ((n = fis.read(b)) != -1)  
        {  
            bos.write(b, 0, n);  
        }  
        fis.close();  
        bos.close();  
        byte[] bytes = bos.toByteArray();  
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("user", (String)session.getAttribute("loginUserName"));
        connection.setRequestProperty("app", fileFileName);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            JobInfoDAO jobInfoDAO = new JobInfoDAOImpl();
            if(jobInfoDAO.addJob(new JobInfo(fileFileName, (String)session.getAttribute("loginUserId")))){
            	this.setMessage("ok");
                return SUCCESS;
            }else{
            	resourceInfoDAO.SubResourceApp(session.getAttribute("loginUserId").toString());
            	this.setMessage("insertError");
                return ERROR;
            }
            
        }
        this.setMessage("error");
        return ERROR;
	}
	/*
	public String execute() throws Exception {      
		String path2 = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path2+"/";
        String path = basePath+"/upload";  
        System.out.println(path);
        try {  
            File f = this.getFile();  
            if(this.getFileFileName().endsWith(".exe")){  
                message="文件错误";  
                return ERROR;  
            }  
            FileInputStream inputStream = new FileInputStream(f);  
            FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getFileFileName());  
            byte[] buf = new byte[1024];  
            int length = 0;  
            while ((length = inputStream.read(buf)) != -1) {  
                outputStream.write(buf, 0, length);  
            }  
            inputStream.close();  
            outputStream.flush();  
            this.setMessage("http://localhost:8080/ajaxfile/upload/"+this.getFileFileName());  
        } catch (Exception e) {  
            e.printStackTrace();  
            message = "上传异常!!!!";  
        }  
        return SUCCESS;  
    }*/
	
}
