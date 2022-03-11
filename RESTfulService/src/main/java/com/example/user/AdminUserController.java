package com.example.user;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import ch.qos.logback.core.joran.util.beans.BeanUtil;


@RestController
@RequestMapping(value = "/admin")
public class AdminUserController {
private UserDaoService service;
//생성자를 통한 의존관계주입
public AdminUserController(UserDaoService service) {
	this.service=service;
}

@GetMapping("/users")
public MappingJacksonValue retrieveAllUsers(){
	List<User> users=service.findAll();
	SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
	FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo", filter);
	MappingJacksonValue mapping=new MappingJacksonValue(users);
	mapping.setFilters(filters);
	return mapping;
}

@GetMapping("/v1/users/{id}")
public  MappingJacksonValue  retrieveUserV1(@PathVariable int id){
	User user=service.findOne(id);
	if(user==null) {
		throw new UserNotFoundException(String.format("ID[%s] not found",id));
	}
	
	SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
	FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo", filter);
	MappingJacksonValue mapping=new MappingJacksonValue(user);
	mapping.setFilters(filters);
	return mapping;
}

@GetMapping("/v2/users/{id}")
public  MappingJacksonValue  retrieveUserV2(@PathVariable int id){
	User user=service.findOne(id);
	if(user==null) {
		throw new UserNotFoundException(String.format("ID[%s] not found",id));
	}
	UserV2 userV2=new UserV2();
	BeanUtils.copyProperties(user, userV2);
	userV2.setGrade("VIP");
	
	SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","grade");
	FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfoV2", filter);
	MappingJacksonValue mapping=new MappingJacksonValue(userV2);
	mapping.setFilters(filters);
	return mapping;
}

@PostMapping("/users")
public ResponseEntity createUser(@Valid@RequestBody User user) {
	User savedUser=service.save(user);
	URI location=ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
	
	return ResponseEntity.created(location).build();
	
}
@DeleteMapping("/users/{id}")
public void deleteUser(@PathVariable int id) {
	User user=service.deleteById(id);
	if(user==null) {
		throw new UserNotFoundException(String.format("Id[%s] not found",id));
	}
}
}
