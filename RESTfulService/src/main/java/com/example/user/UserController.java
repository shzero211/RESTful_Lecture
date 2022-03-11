package com.example.user;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class UserController {
private UserDaoService service;
//생성자를 통한 의존관계주입
public UserController(UserDaoService service) {
	this.service=service;
}

@GetMapping("/users")
public List<User> retrieveAllUsers(){
	return service.findAll();
}

@GetMapping("/users/{id}")
public User retrieveUser(@PathVariable int id){
	User user=service.findOne(id);
	if(user==null) {
		throw new UserNotFoundException(String.format("ID[%s] not found",id));
	}
	return user;
}
@PostMapping("/users")
public ResponseEntity createUser(@RequestBody User user) {
	User savedUser=service.save(user);
	URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
	
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