package service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.QueueInfo;
import service.QueueInfoDAO;

public class QueueInfoDAOImpl implements QueueInfoDAO{

	@Override
	public boolean addQueue(QueueInfo q) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQueueCapacity(String name,double capacity) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update QueueInfo set Capacity='"+capacity+"' where QueueName ='"+name+"'";
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

}
