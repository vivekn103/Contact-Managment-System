package com.validation.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.validation.entity.Contacts;
import com.validation.entity.User;
import com.validation.message.Messages;
import com.validation.repository.ContactRepository;
import com.validation.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName=principal.getName();
//		System.out.println(userName);
		User user = userRepository.getUserByUserName(userName);
//		System.out.println(user);
		model.addAttribute("user", user);
	}
	
	
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{	
				return "normal/user_dashboard";
	}
	
	
	
	
	@RequestMapping("failure")
	public String processFailure()
	{
		return "home";
	}
	
	
	
	
	
	//open add form handler
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model)
	{	
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contacts());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contacts contact,
			@RequestParam("profileImage")MultipartFile file ,
			Principal principal,
			Model model,HttpSession session)
	{	
		try {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		//Processing and uploading file
		if(file.isEmpty())
		{
			//if the file is empty write the message
			//throw new Exception();
			contact.setImage("contact.png");
			
		}
		else
		{
			//else upload the file to folder and update the image name to Contact class
			contact.setImage(file.getOriginalFilename());
			//for uploading the image to folder we need to mention path
			File saveFile = new ClassPathResource("static/image").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			// now image is uploaded to folder
			System.out.println("Image has Been uploaded");
		}
		
		contact.setUser(user);
		 user.getContacts().add(contact);
		 
		 System.out.println(contact);
		 this.userRepository.save(user);
		 model.addAttribute("contact", contact);
		 System.out.println("User has Altered to : "+ user);
		 session.setAttribute("message", new Messages("Your Contact is Added!! Add More","success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.setAttribute("message", new Messages("Something went wrong try again adding the contact","danger"));
		}
		return "normal/add_contact_form";
	}
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page ,Model model,Principal principal)
	{	model.addAttribute("title", "Show User Contacts");
		String name = principal.getName();
		User userByUserName = this.userRepository.getUserByUserName(name);
		Pageable pagable = PageRequest.of(page, 5);
		Page<Contacts> listOfContacts = this.contactRepository.findContactsByUser(userByUserName.getId(),pagable);
		model.addAttribute("contacts", listOfContacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", listOfContacts.getTotalPages());
		return "normal/show-contacts";
	}
	
	//Showing Specific Contact Contact
	@RequestMapping("/{cId}/contact")
	public String showPerticularContact(@PathVariable("cId") Integer cId, Model model, Principal principal)
	{	
		System.out.println(cId);
		
		
		
		Optional<Contacts> optionalContact = this.contactRepository.findById(cId);
		Contacts contact = optionalContact.get();
		
		String name = principal.getName();
		User userByPrincipal = this.userRepository.getUserByUserName(name);
		
		
		if(userByPrincipal.getId()==contact.getUser().getId())
			model.addAttribute("title", contact.getFirstName());
			model.addAttribute("contact",contact);
		return "normal/perticular_Contact";
	}
	
	//Deleting Specefic Contact
	@RequestMapping("/delete/{cId}")
	@Transactional
	public String deleteContact(@PathVariable("cId") Integer cId,Model model,Principal principal,HttpSession session)
	{	
		Optional<Contacts> findById = this.contactRepository.findById(cId);
		Contacts contacts = findById.get();
		
		contacts.setUser(null);
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		System.out.println(user);
		
		user.getContacts().remove(contacts);
		this.userRepository.save(user);
			
			
			session.setAttribute("message", new Messages("Contact Deleted SuccessFully...","Success"));
			return "redirect:/user/show-contacts/0";
		
	}
	
	//open Update Form Handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId,Model model, Principal principal)
	{	Contacts contacts = this.contactRepository.findById(cId).get();
	
		model.addAttribute("title","Update Contact");
		model.addAttribute("contact", contacts);
		return "normal/updateForm";
	}
	
	// Updating Specefic Contact Handler
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateContact(@ModelAttribute Contacts contact,@RequestParam("profileImage") MultipartFile file, Model model,HttpSession session,Principal principal  )
	{	
		try {
			
			//System.out.println(contact.getFirstName());
			//fetch old Contact to delete Photo
			Contacts oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			oldContactDetail.setFirstName(contact.getFirstName());
			oldContactDetail.setSecondName(contact.getSecondName());
			oldContactDetail.setWork(contact.getWork());
			oldContactDetail.setEmail(contact.getEmail());
			oldContactDetail.setPhnum(contact.getPhnum());
			oldContactDetail.setDescription(contact.getDescription());
			User user = this.userRepository.getUserByUserName(principal.getName());
			oldContactDetail.setUser(user);
			
			
			System.out.println(oldContactDetail);
			//Contacts contacts = oldContactDetail.get();
			//System.out.println(oldContactDetail.getcId());
			
			if(!file.isEmpty())
			{
				//delete old photo
				 File deleteFile = new ClassPathResource("static/image").getFile(); 
				 File file1 = new File(deleteFile, oldContactDetail.getImage());
				 file1.delete();
				
				//Update new Photo
				
				  File saveFile = new ClassPathResource("static/image").getFile(); 
				  Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator +file.getOriginalFilename());
				  Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				  oldContactDetail.setImage(file.getOriginalFilename());
				
			}
			
			  else { oldContactDetail.setImage(oldContactDetail.getImage()); }
			 
			
			
			//User user = this.userRepository.getUserByUserName(principal.getName());
			//contact.setUser(user);
//			contact.setcId(contacts.getcId());
			
			contactRepository.save(oldContactDetail);
			//this.contactRepository.flush();
//			Contacts refreshedContact = this.contactRepository.findById(contact.getcId()).get();
//			System.out.println(refreshedContact.getcId());
//			this.contactRepository.save(refreshedContact);
			
			session.setAttribute("message", new Messages("Your Contact is Updated","success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "redirect:/user/"+contact.getcId()+ "/contact";
	}
	
	// Showing User Profile
	@RequestMapping("/userProfile")
	public String userProfile(Model model)
	{	
		model.addAttribute("title", "Profile Page");
		return "normal/userProfile";
	}
	
	//Editing the User Profile Form
	@RequestMapping(value="/editeUserProfile/{id}",method = RequestMethod.POST)
	public String userProfileForm(@PathVariable Integer id,Model model)
	{	User user = this.userRepository.getUserByUserId(id.intValue());
		model.addAttribute("this_user", user);
		return "normal/userProfileEdit";
	}
	
	@RequestMapping(value="/updateProfile",method=RequestMethod.POST)
	public String editUserProfile(@ModelAttribute("user") User user,@RequestParam("id")int Userid , Model model,Principal principal)
	{	//System.out.println("*************"+user.getId());
		System.out.println("*************"+Userid);
//		int a=Integer.parseInt(Userid);
		User oldUser=this.userRepository.getUserByUserId(Userid);
		oldUser.setId(Userid);
		oldUser.setUserName(user.getUserName());
		oldUser.setEmail(user.getEmail());
//		oldUser.setPassword(user.getPassword());
		oldUser.setRole(user.getRole());
		oldUser.setEnable(user.isEnable());
		oldUser.setImageUrl(user.getImageUrl());
		oldUser.setAbout(user.getAbout());
//		oldUser.setHashedPassword(passwordEncoder.encode(user.getPassword()));
		oldUser.setContacts(user.getContacts());
		if(user.getPassword().equals(oldUser.getPassword()))
		{
			oldUser.setPassword(oldUser.getPassword());
			oldUser.setHashedPassword(passwordEncoder.encode(oldUser.getPassword()));
		}
		else {
			oldUser.setPassword(user.getPassword());
			oldUser.setHashedPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		this.userRepository.save(oldUser);
		return "redirect:/user/userProfile";
	}
	
	
}
