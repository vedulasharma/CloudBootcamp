package com.dbs.demo.pcf.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
	
	@Autowired
	CreditCardRepository creditCardRepository;
	
	@RequestMapping(path = "/creditcard/{number}", method=RequestMethod.GET)
	@ResponseBody
	public String getCreditCardInfo(@PathVariable Integer number) {
		CreditCard creditCard = creditCardRepository.findOne(number);
		return "Hello " + creditCard.getCardholderFirstName() + " " + creditCard.getCardholderLastName() + "!";
	}
}
