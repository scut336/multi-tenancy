package service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.ActionInfo;
import entity.TaskInfoImpl;
import service.ActionInfoDAO;

public class ActionInfoDAOImpl implements ActionInfoDAO{

	@Override
	public boolean add(ActionInfo actionInfo) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(actionInfo);
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
	public List<ActionInfo> find(int page, int num) {
		Transaction tx = null;
		String hql = "";
		Query query;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new ActionInfo(user,time,context,type) from ActionInfo where type='0' order by time desc";
			query = session.createQuery(hql);
			query.setFirstResult((page-1)*num); 
			query.setMaxResults(num); 
			List<ActionInfo>actionInfos = query.list();
 			tx.commit();
			return actionInfos;
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
	public int applyPageSum() {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(a.id) from ActionInfo a where a.type='0'";
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
	public boolean del(String user, String time) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "delete from ActionInfo a where a.user='"+user+"' and a.time='"+time+"'";
			Query query = session.createQuery(hql);
			query.executeUpdate();
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
