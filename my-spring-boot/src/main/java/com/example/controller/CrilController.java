package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domin.Gril;
import com.example.respository.GrilRespository;
import com.example.service.CrilService;

@RestController
public class CrilController {

	@Autowired
	private GrilRespository grilRespository;
	
	@Autowired
	private CrilService grilService;
	
	@Autowired
	private Gril gril;
    /**
     * 查询女生list
     * @return
     */
	@GetMapping(value = "/getlist")
	public List<Gril> getGrilList() {
		return grilRespository.findAll();
	}
	
	/**
	 * 新增女生
	 */
	@PostMapping(value = "/add")
	public Gril addGril(@RequestParam("age") Integer age) {
		gril.setAge(age);
		return  grilRespository.save(gril);
	}
	
	/**
	 * 查询一个女生
	 */
	@GetMapping(value = "/getGril")
	public Gril getGril(@RequestParam("id") Integer grilid){
		return grilRespository.findOne(grilid);
	}
	
	/**
	 * 更新
	 */
	@PutMapping(value= "/update")
	public Gril updateGril(@RequestParam("id") Integer id,@RequestParam("age") Integer age){
		gril.setId(id);
		gril.setAge(age);
		return grilRespository.save(gril);
	}
	/**
	 * 删除
	 */
	@DeleteMapping(value ="/del")
	public void  del(@RequestParam("id") Integer id){
		  grilRespository.delete(id);
	}
	
	/**
	 * 根据年龄查找女生  http://localhost:8081/getGrilByAge/11
	 */
	@GetMapping(value = "getGrilByAge/{age}")
	public List<Gril> getGrilByAge(@PathVariable("age") Integer age){
		return grilRespository.findGrilByAge(age);
	}
	
	/**
	 * 事务
	 */
	
	@PostMapping(value = "addTwo")
	public void addTwo(){
		grilService.insertTwo();
	}
}
