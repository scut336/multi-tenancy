package service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.ResourceInfo;
import entity.UserInfo;
import entity.UserProfile;
import service.UserProfileDAO;

public class UserProfileDAOImpl implements UserProfileDAO{

	@Override
	public boolean addUserProfile(UserProfile up, UserInfo ui) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delUserProfile(UserProfile u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProfile(UserProfile oldU, UserProfile newU) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserProfile findUserProfile(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new UserProfile(u.HDFS,u.MapReduce) from UserProfile u where u.userID='"+id+"'";
			Query query = session.createQuery(hql);
			UserProfile userProfile = (UserProfile)query.uniqueResult();
			tx.commit();
			return userProfile;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return new UserProfile("null","null");
		}finally{
			if(tx!=null)
				tx = null;
		}
	}

}
