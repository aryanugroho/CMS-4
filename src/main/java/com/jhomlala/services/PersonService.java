package com.jhomlala.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhomlala.dao.PersonDao;
import com.jhomlala.mail.MailController;
import com.jhomlala.model.Person;
import com.jhomlala.model.PersonRole;

@Service
public class PersonService {
	
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    PersonDao personDao;
    

    
    
    public boolean activate(int id,BigInteger generated)
    {
    	return personDao.activate(id, generated);
    }
    
    
    public List<String> register(Person person)
    {
    	List <String> errorList =  isPersonInsertable(person);
    	if (errorList.size()>0) return errorList;
    	insertPerson(person);
    	
    	return errorList;
    }
    
    
    private void insertPerson(Person person)
    {
    	person.setPassword(passwordEncoder.encode(person.getPassword()));
    	person.setLastOnlineDate(new Date());
        person.setRegisterDate(new Date());
        person.setId(0);
        person.setEnabled(false);
        person.setGenerated(generateString());
        List <PersonRole> roles = new ArrayList<PersonRole>();
        PersonRole userRole = new PersonRole();
        userRole.setRole("ROLE_USER");
        userRole.setPersonID(0);
        userRole.setPersonRoleID(0);
        roles.add(userRole);
        person.setRoles(roles);        
        person.setAvatarSet(false); 
        person.setLastPostTime(new Timestamp(System.currentTimeMillis()-1800000));
		personDao.insert(person);
		sendActivationMail(person);
	}



	private void sendActivationMail(Person person) 
	{
		MailController mailController = new MailController();
		String message = "You sucessfully registred. To activate your account please click in this link "+
		"http://localhost:8080/SpringSecurity/register/confirm?id="+person.getId()+"&generated="+person.getGenerated()+
		"<br> Please dont reply.";
		mailController.sendMail(person.getEmail(),"Activation",message);
	}



	private boolean checkEmail(String email)
    {
    	String emailPattern = 
    			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
    			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    	Pattern pattern = Pattern.compile(emailPattern);
    	Matcher matcher = pattern.matcher(email);
    	
    	return matcher.matches();
 
    }
    private boolean checkStrings(String receivedString)
    {
    	 String stringPattern = "^[A-Za-z0-9_-]{3,60}$"; // a - z from 3 to 60 strings
    	 Pattern pattern = Pattern.compile(stringPattern);
    	 Matcher matcher = pattern.matcher(receivedString);
    	 
    	 return matcher.matches();
    	
    }
    private boolean checkPassword(String receivedPassword)
    {
    	String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{3,60})"; // one digit, one lowercase, one upper
    	Pattern pattern = Pattern.compile(passwordPattern);
    	Matcher matcher = pattern.matcher(receivedPassword);
    	
    	return matcher.matches();
    }
    private boolean checkDoesExistsInDatabaseWithLogin(String login)
    {
    	Person personCheck = personDao.findByLogin(login);
    	if (personCheck !=null)
    		return false;
    	else
    		return true;
    }
    private boolean checkDoesExistsInDatabaseWithEmail(String email)
    {
       	Person personCheck = personDao.findByEmail(email);
    	if (personCheck !=null)
    		return false;
    	else
    		return true;
    }
    
    
    
    private List<String> isPersonInsertable(Person person)
    {
    	List <String> errorMessages = new ArrayList<String>();
    	if (!checkStrings(person.getLogin()))
    		errorMessages.add("Login can only contain alphanumeric characters(3-60)");
    	if (!checkPassword(person.getPassword()))
    		errorMessages.add("Password must contain: 1 digit and 1 uppercase character");
    	if (!checkEmail(person.getEmail()))
    		errorMessages.add("Wrong email format");
    	if (!checkStrings(person.getVisibleName()))
    		errorMessages.add("Visible name can only contain alphanumeric characters(3-60)");
    	if (!checkDoesExistsInDatabaseWithLogin(person.getLogin()))
    		errorMessages.add("There is account with this login!");
    	if (!checkDoesExistsInDatabaseWithEmail(person.getEmail()))
    		errorMessages.add("There is account with this email.");
    	
    	return errorMessages;
    	
    }
    
    
    private String generateString()
    {
    	SecureRandom random = new SecureRandom();
    	return new BigInteger(130,random).toString(60);
    }
    
    public void setLastPostTime(Person person)
    {
    	person.setLastPostTime(new Timestamp(System.currentTimeMillis()));
    	update(person);
    	
    }
    
    public Person findById(int id)
    {
    	return personDao.findById(id);
    }
    
    public Person findByLogin(String name)
    {
    	return personDao.findByLogin(name);
    }
    
    public Person findByEmail(String email)
    {
    	return personDao.findByEmail(email);
    }
    
    public void update(Person person)
    {
    	personDao.update(person);
    }


    

}
