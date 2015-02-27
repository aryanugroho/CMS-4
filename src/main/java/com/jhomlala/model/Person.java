package com.jhomlala.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Set;
@Entity
@Table( name = "person", catalog = "test" )

public class Person 
{
	private int id;
	private String login;
	private String password;
	private Date registerDate;
	private Date lastOnlineDate;
	private String email;
	private String visibleName;
	private List <PersonRole> roles;
	private boolean enabled;
	private String generated;
	private boolean avatarSet;
	private Timestamp lastPostTime;
	public Person()
	{
		
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, length = 45)
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "login", nullable = false, length = 60)
	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "password", nullable = false, length = 60)
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "registerDate", nullable = false)
	public Date getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "lastOnlineDate", nullable = false)
	public Date getLastOnlineDate() {
		return lastOnlineDate;
	}


	public void setLastOnlineDate(Date lastOnlineDate) {
		this.lastOnlineDate = lastOnlineDate;
	}

	@Column(name = "email", nullable = false, length = 60)
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "visibleName", nullable = false, length = 20)
	public String getVisibleName() {
		return visibleName;
	}


	public void setVisibleName(String visibleName) {
		this.visibleName = visibleName;
	}

	@Transient
	public List<PersonRole> getRoles() {
		return roles;
	}

	public void setRoles(List<PersonRole> roles) {
		this.roles = roles;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "generated", nullable = false, length = 60)
	public String getGenerated() {
		return generated;
	}

	public void setGenerated(String generated) {
		this.generated = generated;
	}
	
	@Column(name = "avatarSet", nullable = false)
	public boolean isAvatarSet() {
		return avatarSet;
	}

	public void setAvatarSet(boolean avatarSet) {
		this.avatarSet = avatarSet;
	}
	@Column(name = "lastPostTime", nullable = false)
	public Timestamp getLastPostTime() {
		return lastPostTime;
	}

	public void setLastPostTime(Timestamp lastPostTime) {
		this.lastPostTime = lastPostTime;
	}



	
	
}
