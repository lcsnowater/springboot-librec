package com.snowater.example.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebParam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snowater.example.entity.User;

@RestController
public class HelloController {

	@Value(value="${snowater.name}")
	private String name;
	@RequestMapping
	public String hello(){
		return "hello,wold!";
	}
	@RequestMapping(value="get")
	public Map<String,Object> get(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", name);
		map.put("hello", "hello,"+name);
		return map;
	}
	@RequestMapping(value="find/{age}")
	public User find(@PathVariable int age){
		User u = new User();
		u.setAge(age);
		u.setName(name);
		u.setBrthday(new Date());
		return u;
	}
	
}
