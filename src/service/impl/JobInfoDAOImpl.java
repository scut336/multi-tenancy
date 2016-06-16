package service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.JobInfo;
import service.JobInfoDAO;

public class JobInfoDAOImpl implements JobInfoDAO{

	@Override
	public boolean addJob(JobInfo j) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "delete from JobInfo j where j.id='"+j.getId()+"' and j.name='"+j.getName()+"'";
			Query query = session.createQuery(hql);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(j);
			session.flush();
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
	public List<JobInfo> queryJobInfo(String id) {
		Transaction tx = null;
		List<JobInfo> list = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new JobInfo(j.name,j.id) from JobInfo j where j.id='"+id+"'";
			Query query = session.createQuery(hql);
			list = query.list();
			tx.commit();
			return list;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return list;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

}
