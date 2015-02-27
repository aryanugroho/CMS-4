package com.jhomlala.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jhomlala.model.Comment;

@Repository
public class CommentDaoImpl implements CommentDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void insert(Comment comment)
	{
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
				tx = session.beginTransaction();
				session.save(comment);
				tx.commit();
			}
		catch (Exception exc) 
			{
				if (tx!=null) tx.rollback();
			}
		finally 
			{
	     		session.close();
			}

}

	@Override
	public List<Comment> getCommentsForPost(int postID) 
	{
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Comment.class);
		cr.add(Restrictions.eq("postID", postID));
		return cr.list();
		
	}

}
