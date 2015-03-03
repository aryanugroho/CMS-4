package com.jhomlala.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jhomlala.model.Post;

@Repository
public class PostDaoImpl implements PostDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insert(Post post) {

		Session session = sessionFactory.openSession();
		int id = 0;
		Transaction tx = null;
		try {

			tx = session.beginTransaction();
			session.save(post);
			tx.commit();
		} catch (Exception exc) {
			if (tx != null)
				tx.rollback();

		} finally {
			session.close();
		}

	}

	@Override
	public List<Post> getPosts(int count) {
		List<Post> postList = new ArrayList<Post>();

		Criteria cr = sessionFactory.getCurrentSession().createCriteria(
				Post.class);
		cr.addOrder(Order.desc("id"));
		cr.setMaxResults(count);
		postList = cr.list();
		return postList;
	}

	@Override
	public List<Post> getPostsForPersonWithId(int id, int count) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(
				Post.class);
		cr.add(Restrictions.eq("authorID", id));
		cr.setMaxResults(count);
		return cr.list();

	}

	@Override
	public Post getPostWithId(int id) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(
				Post.class);
		cr.add(Restrictions.eqOrIsNull("id", id));
		List<Post> returnList = cr.list();
		if (returnList.size() > 0)
			return returnList.get(0);
		else
			return null;
	}

}
