package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domin.Gril;
import com.example.demo.respository.GrilRespository;

@Service
public class CrilService {

	@Autowired
	private GrilRespository grilRespository;
	
	@Transactional
	public void insertTwo(){
		Gril grilA = new Gril();
		grilA.setAge(66);
		grilRespository.save(grilA);
		
		int c = 5/0;
		Gril grilB = new Gril();
		grilB.setAge(20);
		grilRespository.save(grilB);
		
	}
	 
}
