package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domin.Gril;
import com.example.respository.GrilRespository;

@RestController
@RequestMapping(value = "gril")
public class CrilController2 {

	@Autowired
	private GrilRespository grilRespository;
    
	/**
	 * 新增女生
	 */
	@PostMapping(value = "/add")
	public Gril addGril(Gril gril) {
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
