package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.QueueInfo;
import entity.TaskInfoImpl;
import entity.UserInfo;
import service.QueueInfoDAO;

public class QueueInfoDAOImpl implements QueueInfoDAO{

	@Override
	public boolean addQueue(QueueInfo q) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQueueCapacity(String name,double capacity,double maxcapacity,double usedcapacity,int memoryUnit,int vcoreUnit) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update QueueInfo set Capacity='"+capacity+"',MaxCapacity='"+maxcapacity+"',UsedCapacity='"+usedcapacity+"',ResourceLimit='"+memoryUnit+","+vcoreUnit+"' where QueueName ='"+name+"'";
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
	public QueueInfo findQueue(String name) {
		Transaction tx = null;
		QueueInfo q = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select new QueueInfo(q.id, q.QueueName, q.Capacity, q.MaxCapacity, q.UsedCapacity,q.ParentQueue,q.IsLeafQueue,q.ResourceLimit,q.Enable,q.MaxWaitingTime) from QueueInfo q where q.QueueName='"+name+"'";
			Query query = session.createQuery(hql);
			q = (QueueInfo)query.uniqueResult();
			tx.commit();
			return q;
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
	public List<String> showQueue() {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select QueueName from QueueInfo";
			Query query = session.createQuery(hql);
			List<String> queue = query.list();
 			tx.commit();
			return queue;
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
	public List<QueueInfo> showQueues() {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select new QueueInfo(QueueName,Capacity,MaxCapacity,UsedCapacity,ResourceLimit,Enable,MaxWaitingTime) from QueueInfo";
			Query query = session.createQuery(hql);
			List<QueueInfo> queue = query.list();
 			tx.commit();
			return queue;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

}
