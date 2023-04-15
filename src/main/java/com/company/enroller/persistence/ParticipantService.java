package com.company.enroller.persistence;

import com.company.enroller.App;
import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		return connector.getSession().createCriteria(Participant.class).list();
	}
	public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
		String hql = "FROM Participant p ";
		if(!key.isBlank()) {
			hql += "WHERE p.login LIKE '%"+key+"%' ";
		}
		if(sortBy.equals("login")) {
			if (sortOrder.equals("DESC")) {
				hql += "ORDER BY p.login DESC ";
			}
			else {
				hql += "ORDER BY p.login ASC ";
			}
		}
		return connector.getSession().createQuery(hql).list();
	}

	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public Participant add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		String hashedPassword = App.passwordEncoder().encode(participant.getPassword());
		participant.setPassword(hashedPassword);
		connector.getSession().save(participant);
		transaction.commit();
		return participant;
	}

	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

}
