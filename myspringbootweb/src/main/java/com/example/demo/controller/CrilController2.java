package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domin.Gril;
import com.example.demo.respository.GrilRespository;

@RestController
@RequestMapping(value = "gril")
public class CrilController2 {

	@Autowired
	private GrilRespository grilRespository;
    
	/**
	 * 新增女生
	 */
	@PostMapping(value = "/add")
	public Gril addGril(@Valid Gril gril,BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			System.out.println(bindingResult.getFieldError().getDefaultMessage());
			return null;
		}
		return  grilRespository.save(gril);
	}
	
	 /**
     * 查询女生list
     * @return
     */
	@GetMapping(value = "/getlist")
	public List<Gril> getGrilList() {
		return grilRespository.findAll();
	}
	 
}
