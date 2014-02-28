package example.hibernate.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import example.hibernate.po.User;
import example.hibernate.utill.HibernateUtil;

public class HibernateTest {
	
	@Test
	//保存session
	public void saveTest(){
		Session session = HibernateUtil.getSession();
		//Transaction : hibernate 中控制事物
//		Transaction transaction = null;
		
		try {
//			transaction = session.getTransaction();
//			transaction.begin();
			session.beginTransaction();
			User u = new User();
			u.setUsername("sb");
			u.setReTime(new Date());
			u.setLastLog(new Date());
			session.save(u);
//			transaction.commit();
			session.getTransaction().commit();
		} catch (Exception e) {
//			transaction.rollback();
			session.getTransaction().rollback();
			HibernateUtil.closeSession(session);
		}
	}
	
	/**
	 * 查询 有get load 两种，load是延迟加载
	 * 对表数据没有操作的情况下无需开启事物
	 */
	@Test
	public void getTest(){
		Session session = HibernateUtil.getSession();
		try {
			User u = (User) session.get(User.class, 1);
			System.out.println(u);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@Test
	public void loadTest(){
		Session session = HibernateUtil.getSession();
		try {
			User u = (User)session.load(User.class, 1);//1是主键值
			System.out.println(u.getClass());
//			System.out.println(u);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@Test
	public void updateTest(){
		Session session = HibernateUtil.getSession();
		try {
			User u = (User) session.load(User.class, 1);
			u.setLastLog(new Date());
			session.beginTransaction();
			session.update(u);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@Test
	public void saveOrUpdateTest(){
		Session session = HibernateUtil.getSession();
		try {
			User u = new User();
			u.setUsername("canada");
			u.setReTime(new Date());
			u.setLastLog(new Date());
			session.beginTransaction();
			session.saveOrUpdate(u);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@Test 
	public void deleteTest(){
		Session session = HibernateUtil.getSession();
		try {
			User u = (User) session.load(User.class,2);
			session.beginTransaction();
			session.delete(u);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@Test
	public void cacheTest(){
		Session session = HibernateUtil.getSession();
		User u1 = new User();
		u1.setUsername("100");
		u1.setLastLog(new Date());
		u1.setReTime(new Date());
		
		session.beginTransaction();
		session.save(u1);
		u1.setUsername("900");
//		session.flush();
//		u1.setUsername("1000");
		session.getTransaction().commit();
		session.close();
	}
	
	
	
}
