package action;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import db.ProjectProperties;

public class InputFileUploadAction extends SuperAction{

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
		String input = request.getParameter("input");
		input = input.equals("")?"/":input;
		String _url = ProjectProperties.getValue("dataUpload");
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
        connection.setRequestProperty("input", input);
        connection.setRequestProperty("file", fileFileName);
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
        	this.setMessage("ok");
            return SUCCESS;
        }
        this.setMessage("error");
        return ERROR;
	}
}
