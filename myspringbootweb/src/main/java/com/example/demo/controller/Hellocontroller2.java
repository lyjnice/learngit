package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.respository.GrilRespository;



@RestController
@RequestMapping(value = "hello")
public class Hellocontroller2 {
	
	@Autowired
	private GrilRespository grilRespository;
	
	@RequestMapping(value = "/hello2/{id}", method = RequestMethod.GET)
	public String say(@PathVariable("id") Integer myid) {
		return "id:"+myid;
	}
}
