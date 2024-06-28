package com.validation.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;



@Entity
public class Contacts {
	@Autowired
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cId;
	private String firstName;
	private String secondName;
	private String work;
	private String email;
	private String phnum;
	private String image;
	@Column(length=100)
	private String description;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhnum() {
		return phnum;
	}
	public void setPhnum(String phnum) {
		this.phnum = phnum;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public Contacts(User user, int cId, String name, String secondName, String work, String email, String phnum,
//			String image, String description) {
//		super();
//		this.user = user;
//		this.cId = cId;
//		this.name = name;
//		this.secondName = secondName;
//		this.work = work;
//		this.email = email;
//		this.phnum = phnum;
//		this.image = image;
//		this.description = description;
//	}
	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * @Override public String toString() { return "Contacts [user=" + user +
	 * ", cId=" + cId + ", firstName=" + firstName + ", secondName=" + secondName +
	 * ", work=" + work + ", email=" + email + ", phnum=" + phnum + ", image=" +
	 * image + ", description=" + description + "]"; }
	 */
	@Override
	public boolean equals(Object obj)
	{
		return this.cId==((Contacts)obj).getcId();
	}
	
	
}