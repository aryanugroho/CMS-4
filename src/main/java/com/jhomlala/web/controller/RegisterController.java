package com.jhomlala.web.controller;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;





import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhomlala.dao.PersonDao;
import com.jhomlala.mail.MailController;
import com.jhomlala.model.Person;
import com.jhomlala.model.PersonRole;
import com.jhomlala.services.MyUserDetailsService;
import com.jhomlala.services.PersonService;





import java.util.Date;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Controller

public class RegisterController {   

	@Autowired
	PersonService personService;

    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegistration(Map<String, Object> model) {
        Person userForm = new Person();    
        model.put("userForm", userForm);

        return "Registration";
    }
    


    @Transactional(readOnly=true) 
    @RequestMapping(value = "register" ,  method = RequestMethod.POST)
    public String processRegistration2(@ModelAttribute("userForm") Person person,
            Map<String, Object> model) 
    {
    	
    	List <String> errorList = personService.register(person);
    	if (errorList.size() == 0 )
    	{
    		model.put("success", "success");
    	}
    	else
    		model.put("error", errorList);

        return "Registration";
    }
    
    
    @Transactional(readOnly=true)
    @RequestMapping(value = "/register/confirm",params = {"id","generated"}, method = RequestMethod.GET)
    public String activateRegister(Map<String, Object> model,
    		@RequestParam(value = "id") int id, 
    		@RequestParam(value = "generated") BigInteger generated ) 
    {
    	if (personService.activate(id, generated))
    		return "RegistrationSuccess";
    	else
    		return "RegistrationFailed";


        
    }
    
    
  
    
}
