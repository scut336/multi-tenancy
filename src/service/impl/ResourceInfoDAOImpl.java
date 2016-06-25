package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MD5creator;
import db.MyHibernateSessionFactory;
import entity.ResourceApplication;
import entity.ResourceApplicationImpl;
import entity.ResourceInfo;
import entity.ResourceInfoImpl;
import entity.TaskInfoImpl;
import entity.UserInfo;
import service.ResourceInfoDAO;

public class ResourceInfoDAOImpl implements ResourceInfoDAO{

	@Override
	public boolean AddResource(ResourceInfo r) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "insert into ResourceInfo(AppLimit,CurrentAppCount,HDFSDirectory,HDFSDirectoryQuota,HDFSDirectoryRemaining,Queue,CreateTime,CreateUserID,Expired,SubmitJobTimes,LastSubmitTime,userID)"+
			"values ('"+r.getAppLimit()+"','"+r.getCurrentAppCount()+"','"+r.getHDFSDirectory()+"','"+r.getHDFSDirectoryQuota()+"','"+r.getHDFSDirectoryRemaining()+"','"+r.getQueue()+"','"+sdf.format(new Date())+"','"+
					r.getCreateUserID()+"','"+r.getExpired()+"','"+r.getSubmitJobTimes()+"','"+sdf.format(new Date())+"','"+r.getUserID()+"')";
			Query query = session.createSQLQuery(sql);
			query.executeUpdate();
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx=null;
		}
	}

	@Override
	public boolean DelResource(UserInfo u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UpdateResource(String id,int left) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceInfo set HDFSDirectoryRemaining='"+left+"' where userID ='"+id+"'";
			Query query = session.createQuery(hql);
			query.executeUpdate();  
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public ResourceInfoImpl getHDFSDirectoryQuota(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new ResourceInfo(AppLimit,CurrentAppCount,HDFSDirectory,HDFSDirectoryQuota,HDFSDirectoryRemaining,Queue,CreateTime,CreateUserID,Expired,SubmitJobTimes,LastSubmitTime) from ResourceInfo where userID='"+id+"'";
			Query query = session.createQuery(hql);
			ResourceInfo resourceInfo = (ResourceInfo)query.uniqueResult();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select QueueName,Capacity,ResourceLimit from QueueInfo where id='"+resourceInfo.getQueue()+"'";
			query = session.createQuery(hql);
			Object[] queue = (Object[])query.uniqueResult();
			session.flush();
			tx.commit();
			
			ResourceInfoImpl resourceInfoImpl = new ResourceInfoImpl(resourceInfo.getAppLimit(), resourceInfo.getCurrentAppCount(), resourceInfo.getHDFSDirectory(), resourceInfo.getHDFSDirectoryQuota(), resourceInfo.getHDFSDirectoryRemaining(), queue[0].toString(), resourceInfo.getCreateTime(), resourceInfo.getCreateUserID(), resourceInfo.getExpired(), resourceInfo.getSubmitJobTimes(), resourceInfo.getLastSubmitTime(),queue[1].toString(),queue[2].toString());
			return resourceInfoImpl;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public boolean AddResourceApp(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new ResourceInfo(AppLimit,CurrentAppCount,HDFSDirectoryQuota,HDFSDirectoryRemaining) from ResourceInfo where userID='"+id+"'";
			Query query = session.createQuery(hql);
			ResourceInfo resourceInfo = (ResourceInfo)query.uniqueResult();
			session.flush();
			tx.commit();
			if(resourceInfo.getAppLimit()<=resourceInfo.getCurrentAppCount()||resourceInfo.getHDFSDirectoryQuota()<=resourceInfo.getHDFSDirectoryRemaining())
				return false;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceInfo set CurrentAppCount ='"+(resourceInfo.getCurrentAppCount()+1)+"',LastSubmitTime = '"+sdf.format(new Date())+"' where userID='"+id+"'";
			Query query2 = session.createQuery(hql);
			query2.executeUpdate();  
			session.flush();
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public boolean SubResourceApp(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select CurrentAppCount from ResourceInfo where userID='"+id+"'";
			Query query = session.createQuery(hql);
			int count = (int)query.uniqueResult();
			session.flush();
			tx.commit();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceInfo set CurrentAppCount ='"+(count-1)+"',LastSubmitTime = '"+sdf.format(new Date())+"' where userID='"+id+"'";
			Query query2 = session.createQuery(hql);
			query2.executeUpdate(); 
			session.flush();
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public boolean AddResourceJobTime(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select SubmitJobTimes from ResourceInfo where userID='"+id+"'";
			Query query = session.createQuery(hql);
			int times = (int)query.uniqueResult();
			session.flush();
			tx.commit();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceInfo set SubmitJobTimes ='"+(times+1)+"',LastSubmitTime = '"+sdf.format(new Date())+"' where userID='"+id+"'";
			Query query2 = session.createQuery(hql);
			query2.executeUpdate(); 
			session.flush();
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public boolean ApplyResource(String userid, String queue, int hdfs, int num) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select id from QueueInfo where QueueName='"+queue+"'";
			Query query = session.createQuery(hql);
			long id = (long)query.uniqueResult();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceApplication set Applimit ='"+num+"',Enable = 'W',HDFSDirectoryQuota ='"+hdfs+"',Queue='"+id+"' where UserID='"+userid+"'";
			Query query2 = session.createQuery(hql);
			query2.executeUpdate(); 
			session.flush();
			tx.commit();
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public Object[] HDFS(String userID) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select HDFSDirectoryQuota,HDFSDirectoryRemaining from ResourceInfo where userID='"+userID+"'";
			Query query = session.createQuery(hql);
			Object[] res = (Object[])query.uniqueResult();
			session.flush();
			tx.commit();
			return res;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public List<ResourceApplicationImpl> ShowResourceApplication(int page) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select u.name,q.QueueName,r.appLimit,r.hDFSDirectoryQuota from ResourceApplication r join UserInfo u on r.UserID=u.id join QueueInfo q on r.Queue=q.id where r.enable = 'W'";
			Query query = session.createSQLQuery(hql);
			query.setFirstResult((page-1)*10); 
			query.setMaxResults(10); 
			List<Object[]> list = query.list();
			List<ResourceApplicationImpl> res = new ArrayList<ResourceApplicationImpl>();
			for(int i=0;i<list.size();i++)
				res.add(new ResourceApplicationImpl(list.get(i)[0].toString(),list.get(i)[1].toString(),Integer.parseInt(list.get(i)[2].toString()),Integer.parseInt(list.get(i)[3].toString())));
			session.flush();
			tr.commit();
			if(res.size()>0){
				return res;
			}else{
				return null;
			}
		}catch(Exception e){
			tr.rollback();
			e.printStackTrace();
			return null;
		}finally{
			if(tr!=null){
				tr=null;
			}
		}
	}

	@Override
	public int pageSum() {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(userID) from ResourceApplication where enable = 'W'";
			Query query = session.createQuery(hql);
			double c = (double)((long)query.uniqueResult());
			int count = (int) Math.ceil(c/10);
			tx.commit();
			return count;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return 0;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public boolean manageApply(String name,char enable) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select id from UserInfo where name='"+name+"'";
			Query query = session.createQuery(hql);
			String id = (String)query.uniqueResult();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update ResourceApplication set Enable = '"+enable+"' where UserID='"+id+"'";
			Query query2 = session.createQuery(hql);
			query2.executeUpdate(); 
			session.flush();
			tx.commit();
			
			if(enable=='T'){
				session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
				hql = "select r.AppLimit,r.HDFSDirectoryQuota,r.Queue from ResourceApplication r where UserID='"+id+"'";
				Query query3 = session.createQuery(hql);
				Object[] val = (Object[])query3.uniqueResult();
				session.flush();
				tx.commit();
				
				session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
				hql = "update ResourceInfo set AppLimit = '"+val[0].toString()+"',HDFSDirectoryQuota='"+val[1].toString()+"',Queue='"+val[2].toString()+"' where UserID='"+id+"'";
				Query query4 = session.createQuery(hql);
				query4.executeUpdate(); 
				session.flush();
				tx.commit();
			}
			return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

	@Override
	public List<ResourceApplicationImpl> ShowResource(int page) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select u.name,q.QueueName,r.appLimit,r.hDFSDirectoryQuota from ResourceInfo r join UserInfo u on r.UserID=u.id join QueueInfo q on r.Queue=q.id";
			Query query = session.createSQLQuery(hql);
			query.setFirstResult((page-1)*10); 
			query.setMaxResults(10); 
			List<Object[]> list = query.list();
			List<ResourceApplicationImpl> res = new ArrayList<ResourceApplicationImpl>();
			for(int i=0;i<list.size();i++)
				res.add(new ResourceApplicationImpl(list.get(i)[0].toString(),list.get(i)[1].toString(),Integer.parseInt(list.get(i)[2].toString()),Integer.parseInt(list.get(i)[3].toString())));
			session.flush();
			tr.commit();
			if(res.size()>0){
				return res;
			}else{
				return null;
			}
		}catch(Exception e){
			tr.rollback();
			e.printStackTrace();
			return null;
		}finally{
			if(tr!=null){
				tr=null;
			}
		}
	}
}
