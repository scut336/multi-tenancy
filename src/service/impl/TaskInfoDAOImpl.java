package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.MyHibernateSessionFactory;
import entity.TaskInfo;
import entity.TaskInfoImpl;
import service.TaskInfoDAO;

public class TaskInfoDAOImpl implements TaskInfoDAO{

	@Override
	public boolean addTask(TaskInfo t) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String string = sdf.format(t.getCreateDate());
			String sql = "insert into TaskInfo(id,TaskName,TaskID,TaskError,TaskLog,TaskResult,createDate,userInfo,TaskStatus) "+
			"values ('"+t.getId()+"','"+t.getTaskName()+"','"+t.getTaskID()+"','"+t.getTaskError()+"','"+t.getTaskLog()+"','"+t.getTaskResult()+"','"+string+"','"+
					t.getUserInfo()+"','0')";
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
	public boolean delTask(String id) {
		Transaction tx = null;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			String hql = "delete from TaskInfo t where t.id='"+id+"'";
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
	public boolean updateTask(TaskInfo oldT, TaskInfo newT) {
		Transaction tx = null;
		String hql = "";
		if(oldT.getTaskResult().equals(newT.getTaskResult())&&oldT.getTaskLog().equals(newT.getTaskLog())
				&&oldT.getTaskError().equals(newT.getTaskError())&&oldT.getTaskStatus().equals(newT.getTaskStatus()))
			return true;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update TaskInfo set ";
			if(!oldT.getTaskResult().equals(newT.getTaskResult()))
				hql += "TaskResult = '" + newT.getTaskResult() + "',";
			if(!oldT.getTaskLog().equals(newT.getTaskLog()))
				hql += "TaskLog = '" + newT.getTaskLog() + "',";
			if(!oldT.getTaskError().equals(newT.getTaskError()))
				hql += "TaskError = '" + newT.getTaskError() + "',";
			if(!oldT.getTaskStatus().equals(newT.getTaskStatus()))
				hql += "TaskStatus = '" + newT.getTaskStatus() + "',";
			hql = hql.substring(0, hql.length()-1);
			hql += " where id = '" + oldT.getId() + "'";
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
	public List<TaskInfoImpl> findTask(String id,int page,int num) {
		Transaction tx = null;
		String hql = "";
		Query query;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			if(id.equals("")){
				hql = "select t.id,t.TaskStatus,t.TaskLog,t.TaskResult,t.TaskError,t.createDate,t.TaskID,u.name from TaskInfo t join UserInfo u on t.userInfo=u.id order by t.userInfo asc";
				query = session.createSQLQuery(hql);
			}
			else{
				hql = "select t.id,t.TaskStatus,t.TaskLog,t.TaskResult,t.TaskError,t.createDate,t.TaskID,u.name from TaskInfo t join UserInfo u on t.userInfo=u.id where t.userInfo='"+id+"'";
				query = session.createSQLQuery(hql);
			}
			query.setFirstResult((page-1)*num); 
			query.setMaxResults(num); 
			List<Object[]> taskInfos = query.list();
 			tx.commit();
 			if(taskInfos.size()==0)
 				return null;
 			List<TaskInfoImpl> taskInfoImpls = new ArrayList<TaskInfoImpl>();
 			for(int i=0;i<taskInfos.size();i++){
 				taskInfoImpls.add(new TaskInfoImpl(taskInfos.get(i)[0].toString(), taskInfos.get(i)[1].toString(), taskInfos.get(i)[2].toString(), taskInfos.get(i)[3].toString(), taskInfos.get(i)[4].toString(), taskInfos.get(i)[5].toString(), taskInfos.get(i)[6].toString(), taskInfos.get(i)[7].toString()));
 			}
			return taskInfoImpls;
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
	public boolean checkTaskInfoById(String id) {
		Transaction tr =null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tr = session.beginTransaction();
			hql = "select t.TaskLog from TaskInfo t where t.id = '"+id+"'";
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
	public TaskInfo queryTaskById(String id) {
		Transaction tx = null;
		String hql = "";
		Query query;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select new TaskInfo(t.id,t.TaskStatus,t.TaskLog,t.TaskResult,t.TaskError,t.createDate) from TaskInfo t where t.id='"+id+"'";
			query = session.createQuery(hql);
			TaskInfo taskInfo = (TaskInfo)query.uniqueResult();
 			tx.commit();
			return taskInfo;
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
	public int pageSum() {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select count(t.id) from TaskInfo t";
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
	public boolean insertJarName(String id, String name) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update TaskInfo set TaskName='"+name+"' where id='"+id+"'";
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
	public String findJarName(String id) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "select t.TaskName from TaskInfo t where t.id='"+id+"'";
			Query query = session.createQuery(hql);
			String taskname = (String)query.uniqueResult();
			tx.commit();
			return taskname;
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
	public boolean insertTaskID(String id, String taskID) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update TaskInfo set TaskID='"+taskID+"',TaskStatus = '0' where id='"+id+"'";
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
	public boolean updateTaskStatus(String TaskID, String num) {
		Transaction tx = null;
		String hql = "";
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			hql = "update TaskInfo set TaskStatus='"+num+"' where TaskID ='"+TaskID+"'";
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
	public TaskInfoImpl findTaskInfo(String id, String taskid) {
		Transaction tx = null;
		String hql = "";
		Query query;
		try{
			Session session = MyHibernateSessionFactory.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			if(id.equals("")){
				hql = "select t.id,t.TaskStatus,t.TaskLog,t.TaskResult,t.TaskError,t.createDate,t.TaskID,u.name from TaskInfo t join UserInfo u on t.userInfo=u.id where t.id='"+taskid+"'";
				query = session.createSQLQuery(hql);
			}
			else{
				hql = "select t.id,t.TaskStatus,t.TaskLog,t.TaskResult,t.TaskError,t.createDate,t.TaskID,u.name from TaskInfo t join UserInfo u on t.userInfo=u.id where t.userInfo='"+id+"' and t.id='"+taskid+"'";
				query = session.createSQLQuery(hql);
			}
			Object[] taskInfos = (Object[])query.uniqueResult();
 			tx.commit();
 			if(taskInfos.length==0)
 				return null;
 			TaskInfoImpl taskInfoImpls = new TaskInfoImpl(taskInfos[0].toString(), taskInfos[1].toString(), taskInfos[2].toString(), taskInfos[3].toString(), taskInfos[4].toString(), taskInfos[5].toString(), taskInfos[6].toString(), taskInfos[7].toString());
 			return taskInfoImpls;
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
