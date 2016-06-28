package service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MD5creator;
import db.MyHibernateSessionFactory;
import entity.UserInfo;
import service.UserInfoDAO;

public class UserInfoDAOImpl implements UserInfoDAO{

	@Override
	public List<UserInfo> UsersLogin(UserInfo u) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select new UserInfo(id,role) from UserInfo where name = '"+u.getName()+"' and password = '"+MD5creator.MD5(u.getPassword())+"'";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			List<UserInfo> list = query.list();
			session.flush();
			tr.commit();
			System.out.println(list.size());
			if(list.size()>0){
				return list;
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
	public boolean checkUserInfoById(String Id) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select u.name from UserInfo u where u.id = '"+Id+"'";
			Query query = session.createQuery(hql);
			List list = query.list();
			tr.commit();
			if(list.size()>0){
				return false;
			}else{
				return true;
			}
		}catch(Exception e){
			tr.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tr!=null){
				tr=null;
			}
		}
	}
	
	@Override
	public List<UserInfo> queryAllUserInfo(int page) {
		Transaction tx = null;
		List<UserInfo> list = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new UserInfo(u.id,u.name,u.role,u.department,u.createTime) from UserInfo u";
			Query query = session.createQuery(hql);
			query.setFirstResult((page-1)*10); 
			query.setMaxResults(10); 
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

	@Override
	public UserInfo queryUserInfoById(String Id) {
		Transaction tx = null;
		UserInfo u = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select new UserInfo(u.id,u.name,u.role,u.department,u.createTime) from UserInfo u where u.id='"+Id+"'";
			Query query = session.createQuery(hql);
			u = (UserInfo)query.uniqueResult();
			tx.commit();
			return u;
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
	public boolean addUserInfo(UserInfo u) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(u);
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
	public boolean updateUserInfo(UserInfo u) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update UserInfo set name = '" + u.getName() + "',department = '" + u.getDepartment() + "',role='"+u.getRole()+"' where id = '" + u.getId() + "'";
			Query query = session.createQuery(hql);
			query.executeUpdate();  
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			int queue = 0;
			if(u.getRole().equals("user"))
				queue=1;
			else if(u.getRole().equals("vip1"))
				queue=2;
			else if(u.getRole().equals("vip2"))
				queue=3;
			hql = "update ResourceInfo set queue = '" + queue + "' where UserID = '" + u.getId() + "'";
			query = session.createQuery(hql);
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
	public boolean deleteUserInfo(String Id) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "delete from UserInfo u where u.id='"+Id+"'";
			Query query = session.createQuery(hql);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "delete from TaskInfo t where t.userInfo='"+Id+"'";
			query = session.createQuery(hql);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "delete from ResourceInfo r where r.userID='"+Id+"'";
			query = session.createQuery(hql);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "delete from JobInfo j where j.id='"+Id+"'";
			query = session.createQuery(hql);
			query.executeUpdate();
			session.flush();
			tx.commit();
			
			session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "delete from ResourceApplication r where r.UserID='"+Id+"'";
			query = session.createQuery(hql);
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

	@Override
	public int pageSum() {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(u.id) from UserInfo u";
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
	public UserInfo queryUserInfoByName(String name) {
		Transaction tx = null;
		UserInfo u = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "select new UserInfo(u.id,u.name,u.role,u.department,u.createTime) from UserInfo u where u.name='"+name+"'";
			Query query = session.createQuery(hql);
			u = (UserInfo)query.uniqueResult();
			tx.commit();
			return u;
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
	public boolean checkUserInfoByName(String name) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select u.name from UserInfo u where u.name = '"+name+"'";
			Query query = session.createQuery(hql);
			List list = query.list();
			tr.commit();
			if(list.size()>0){
				return false;
			}else{
				return true;
			}
		}catch(Exception e){
			tr.rollback();
			e.printStackTrace();
			return false;
		}finally{
			if(tr!=null){
				tr=null;
			}
		}
	}

	@Override
	public long userSum() {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(u.id) from UserInfo u";
			Query query = session.createQuery(hql);
			long count = (long)query.uniqueResult();
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

}
