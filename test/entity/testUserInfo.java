package entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import service.UserInfoDAO;
import service.impl.UserInfoDAOImpl;
import db.MD5creator;
import db.MyHibernateSessionFactory;

public class testUserInfo {
	
	@Test
	public void test1(){
		Configuration config = new Configuration().configure();
		//StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder()
		//	.applySettings(config.getProperties());
		//StandardServiceRegistry ssr = ssrb.build();
		//SessionFactory sessionFactory = config.buildSessionFactory(ssr);
		SchemaExport seExport = new SchemaExport(config);
		seExport.create(true,true);
		
	}
	@Test
	public void add(){
		Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
//		TaskInfo t1 = new TaskInfo("TASK01","running","/user","/res","/err",new Date());
//		TaskInfo t2 = new TaskInfo("TASK02","killed","/user","/res","/err",new Date());
//		TaskInfo t3 = new TaskInfo("TASK03","running","/user","/res","/err",new Date());
//		TaskInfo t4 = new TaskInfo("TASK04","running","/user","/res","/err",new Date());
//		TaskInfo t5 = new TaskInfo("TASK05","running","/user","/res","/err",new Date());
//		TaskInfo t6 = new TaskInfo("TASK06","running","/user","/res","/err",new Date());
//		TaskInfo t7 = new TaskInfo("TASK07","running","/user","/res","/err",new Date());
//		TaskInfo t8 = new TaskInfo("TASK08","running","/user","/res","/err",new Date());
//		TaskInfo t9 = new TaskInfo("TASK09","running","/user","/res","/err",new Date());
//		Set<TaskInfo> s1 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s2 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s3 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s4 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s5 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s6 = new HashSet<TaskInfo>();
//		Set<TaskInfo> s7 = new HashSet<TaskInfo>();
//		s1.add(t1);
//		s2.add(t2);
//		s2.add(t3);
//		s3.add(t4);
//		s3.add(t5);
//		s4.add(t6);
//		s5.add(t7);
//		s6.add(t8);
//		s7.add(t9);
		UserInfo u1 = new UserInfo(1,"la@gc.com","admin","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u2 = new UserInfo(2,"sasa@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u3 = new UserInfo(3,"ha@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u4 = new UserInfo(4,"qa@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u5 = new UserInfo(5,"wa@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u6 = new UserInfo(6,"ea@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		UserInfo u7 = new UserInfo(7,"ra@gc.com","user","运营部",MD5creator.MD5("123456"),new Date());
		
//		u1.setTaskInfos(s1);
//		u2.setTaskInfos(s2);
//		u3.setTaskInfos(s3);
//		u4.setTaskInfos(s4);
//		u5.setTaskInfos(s5);
//		u6.setTaskInfos(s6);
//		u7.setTaskInfos(s7);
		
		ResourceInfo r1 = new ResourceInfo(u1.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r2 = new ResourceInfo(u2.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r3 = new ResourceInfo(u3.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r4 = new ResourceInfo(u4.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r5 = new ResourceInfo(u5.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r6 = new ResourceInfo(u6.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		ResourceInfo r7 = new ResourceInfo(u7.getId(),10,0,"smoketest",1073741824,0,1,new Date(),1,'F',0,new Date());
		
		ResourceApplication ra1 = new ResourceApplication(u1.getId(),0,0,0,'F');
		ResourceApplication ra2 = new ResourceApplication(u2.getId(),0,0,0,'F');
		ResourceApplication ra3 = new ResourceApplication(u3.getId(),0,0,0,'F');
		ResourceApplication ra4 = new ResourceApplication(u4.getId(),0,0,0,'F');
		ResourceApplication ra5 = new ResourceApplication(u5.getId(),0,0,0,'F');
		ResourceApplication ra6 = new ResourceApplication(u6.getId(),0,0,0,'F');
		ResourceApplication ra7 = new ResourceApplication(u7.getId(),0,0,0,'F');
//		
//		session.save(t1);
//		session.save(t2);
//		session.save(t3);
//		session.save(t4);
//		session.save(t5);
//		session.save(t6);
//		session.save(t7);
//		session.save(t8);
//		session.save(t9);
	
		session.save(u1);
		session.save(u2);
		session.save(u3);
		session.save(u4);
		session.save(u5);
		session.save(u6);
		session.save(u7);
		
		session.save(r1);
		session.save(r2);
		session.save(r3);
		session.save(r4);
		session.save(r5);
		session.save(r6);
		session.save(r7);
		
		session.save(ra1);
		session.save(ra2);
		session.save(ra3);
		session.save(ra4);
		session.save(ra5);
		session.save(ra6);
		session.save(ra7);
		
		session.flush();
		tx.commit();
	}
	@Test
	public void del(){
		Configuration config = new Configuration().configure();
		ServiceRegistry serviceRegistry = 
				new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		//UserProfile userProfile = new UserProfile(1,"1","1");
		//UserInfo u1 = new UserInfo("CN022","sasa@gc.com","user","运营部",MD5creator.MD5("123456"),new Date(),userProfile);
		//session.delete(u1);
		tx.commit();
		sessionFactory.close();
	}
	@Test
	public void test2() throws Exception{
        String _url = "http://222.201.145.144:4567/app/start";
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "text/html");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("user", "la@gc.com");
        connection.setRequestProperty("app", "smoketest.jar");
        connection.setRequestProperty("queue", "vip1");
        connection.setRequestProperty("input", "/user/smoketest/input");
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            line = reader.readLine();
            System.out.println(line);
        }
	}
	
	@Test
	public void test3() throws IOException{
		Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		String hql = "update UserInfo set name = 'aa',department = 'aa',role='aa' where id = 1";
		Query query = session.createQuery(hql);
		query.executeUpdate();  
		tx.commit();
	}
}
