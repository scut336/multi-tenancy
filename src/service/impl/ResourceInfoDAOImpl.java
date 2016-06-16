package service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javassist.expr.NewArray;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.ResourceInfo;
import entity.ResourceInfoImpl;
import entity.UserInfo;
import service.ResourceInfoDAO;

public class ResourceInfoDAOImpl implements ResourceInfoDAO{

	@Override
	public boolean AddResource(ResourceInfo r,String id) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "insert into ResourceInfo(AppLimit,CurrentAppCount,HDFSDirectory,HDFSDirectoryQuota,HDFSDirectoryRemaining,Queue,CreateTime,CreateUserID,Expired,SubmitJobTimes,LastSubmitTime,userID)"+
			"values ('"+r.getAppLimit()+"','"+r.getCurrentAppCount()+"','"+r.getHDFSDirectory()+"','"+r.getHDFSDirectoryQuota()+"','"+r.getHDFSDirectoryRemaining()+"','"+r.getQueue()+"','"+sdf.format(new Date())+"','"+
					r.getCreateUserID()+"','"+r.getExpired()+"','"+r.getSubmitJobTimes()+"','"+sdf.format(new Date())+"','"+id+"')";
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
			hql = "select QueueName from QueueInfo where id='"+resourceInfo.getQueue()+"'";
			query = session.createQuery(hql);
			String queue = query.uniqueResult().toString();
			session.flush();
			tx.commit();
			
			ResourceInfoImpl resourceInfoImpl = new ResourceInfoImpl(resourceInfo.getAppLimit(), resourceInfo.getCurrentAppCount(), resourceInfo.getHDFSDirectory(), resourceInfo.getHDFSDirectoryQuota(), resourceInfo.getHDFSDirectoryRemaining(), queue, resourceInfo.getCreateTime(), resourceInfo.getCreateUserID(), resourceInfo.getExpired(), resourceInfo.getSubmitJobTimes(), resourceInfo.getLastSubmitTime());
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

}
