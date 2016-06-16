package db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class MyHibernateSessionFactory {
	
	private static SessionFactory sessionFactory;
	private MyHibernateSessionFactory(){
		
	}
	
	public static SessionFactory getSessionFactory(){
		if(sessionFactory==null){
			Configuration config = new Configuration().configure();
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder()
				.applySettings(config.getProperties());
			//ServiceRegistry serviceRegistry = 
					//new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
			StandardServiceRegistry ssr = ssrb.build();
			sessionFactory = config.buildSessionFactory(ssr);
			return sessionFactory;
		}else{
			return sessionFactory;
		}
	}
}
