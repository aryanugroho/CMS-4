package com.jhomlala.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.jhomlala.model.PostVote;

public class PostVoteDaoImpl implements PostVoteDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insert(PostVote postVote) {
		Session session = sessionFactory.openSession();
		int id = 0;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(postVote);
			tx.commit();
		} catch (Exception exc) {
			if (tx != null)
				tx.rollback();

		} finally {
			session.close();
		}

	}

	@Override
	public void update(PostVote postVote) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(postVote);
			tx.commit();
		} catch (Exception exc) {
			if (tx != null)
				tx.rollback();

		} finally {
			session.close();
		}

	}

	@Override
	public List<PostVote> get(int postid) {
		
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(PostVote.class);
		cr.add(Restrictions.eqOrIsNull("postId", postid));
		
		return cr.list();
	}

}
