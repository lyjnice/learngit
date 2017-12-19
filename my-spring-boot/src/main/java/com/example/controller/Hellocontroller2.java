package com.example.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.properties.GrilProperties;

@RestController
@RequestMapping(value = "hello")
public class Hellocontroller2 {
	@Autowired
    private GrilProperties  grilProperties;
    
	@RequestMapping(value = "/hello2/{id}", method = RequestMethod.GET)
	public String say(@PathVariable("id") Integer myid) {
		return "id:"+myid;
	}
	//http://localhost:8081/hello/999/hello3
	@RequestMapping(value = "/{id}/hello3", method = RequestMethod.GET)
	public String say2(@PathVariable("id") Integer myid) {
		return "id:"+myid;
	}
	//http://localhost:8081/hello/hello4?id=444
	@RequestMapping(value = "/hello4", method = RequestMethod.GET)
	public String say3(@RequestParam("id") Integer myid) {
		return "id:"+myid;
	}
	//http://localhost:8081/hello/hello5?id=555
	//@RequestMapping(value = "/hello4", method = RequestMethod.GET)
	@GetMapping(value = "hello5")
	public String say4(@RequestParam("id") Integer myid) {
		return "id:"+myid;
	}
}
