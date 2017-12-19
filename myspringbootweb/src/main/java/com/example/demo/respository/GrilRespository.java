package com.example.demo.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domin.Gril;

public interface GrilRespository extends JpaRepository<Gril, Integer>{
	
	public List<Gril> findGrilByAge(Integer age);

}
