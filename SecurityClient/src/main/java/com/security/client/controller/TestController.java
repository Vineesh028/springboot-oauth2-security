package com.security.client.controller;


import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {


	@GetMapping("/{name}")
	@ResponseStatus(OK)
	public ResponseEntity<String> createCity(@PathVariable String name) {

		String greet= "Hello "+name+" !";

		return ResponseEntity.status(OK).body(greet);
	}
}
