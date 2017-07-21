package com.dbs.demo.pcf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
	
	@RequestMapping(path = "/hello/{name}", method=RequestMethod.GET)
	@ResponseBody
	public String doHello(@PathVariable String name) {
		return "Hello " + name + "!";
	}
}
