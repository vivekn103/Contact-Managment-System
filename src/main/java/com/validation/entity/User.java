package com.validation.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

//import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;



@Entity
public class User {
	@Autowired
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
	private List<Contacts> contacts;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "User Name Cannot be null!!")
	@Size(min = 3, max = 15,message = "User Name Must Be Between 3 To 15 character!!")
	private String userName;
	
	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="INVALID EMAIL!!")
	private String email;
	
	@Pattern(regexp = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$", 
            message = "Password must meet complexity criteria")
	private String password;
	private String role;
	private boolean enable;
	private String imageUrl;
	
	@Column(length=100)
	private String about;

	private String hashedPassword;
	
	
	

	public String getHashedPassword() {
		return hashedPassword;
	}




	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}




	public List<Contacts> getContacts() {
		return contacts;
	}




	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getUserName() {
		return userName;
	}




	public void setUserName(String userName) {
		this.userName = userName;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getRole() {
		return role;
	}




	public void setRole(String role) {
		this.role = role;
	}




	public boolean isEnable() {
		return enable;
	}




	public void setEnable(boolean enable) {
		this.enable = enable;
	}




	public String getImageUrl() {
		return imageUrl;
	}




	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}




	public String getAbout() {
		return about;
	}




	public void setAbout(String about) {
		this.about = about;
	}




	public User() {
		super();
		// TODO Auto-generated constructor stub
	}




	@Override
	public String toString() {
		return "User [contacts=" + contacts + ", id=" + id + ", userName=" + userName + ", email=" + email
				+ ", password=" + password + ", role=" + role + ", enable=" + enable + ", imageUrl=" + imageUrl
				+ ", about=" + about + "]";
	}

	
	
	
	
	
}
