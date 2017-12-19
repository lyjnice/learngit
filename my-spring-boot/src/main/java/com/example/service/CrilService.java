package com.example.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domin.Gril;
import com.example.respository.GrilRespository;

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
