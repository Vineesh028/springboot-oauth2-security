package com.security.client.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.client.entity.User;
import com.security.client.model.PasswordModel;
import com.security.client.model.UserModel;
import com.security.client.service.UserService;

@RestController
public class RegistrationController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody UserModel userModel) {
		User user = userService.registerUser(userModel);

		return ResponseEntity.status(OK).body(user);
	}

	@GetMapping("/verifyRegistration")
	public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		if (result.equalsIgnoreCase("valid")) {

			return ResponseEntity.status(OK).body("User Verified Successfully");

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad User");
	}

	@PostMapping("/savePassword")
	public ResponseEntity<String> savePassword(@RequestParam("token") String token,
			@RequestBody PasswordModel passwordModel) {
		String result = userService.validatePasswordResetToken(token);
		if (!result.equalsIgnoreCase("valid")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");

		}
		Optional<User> user = userService.getUserByPasswordResetToken(token);
		if (user.isPresent()) {
			userService.changePassword(user.get(), passwordModel.getNewPassword());
			return ResponseEntity.status(OK).body("Password Reset Successfully");
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody PasswordModel passwordModel) {
		User user = userService.findUserByEmail(passwordModel.getEmail());
		if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Old Password");
		}
		userService.changePassword(user, passwordModel.getNewPassword());
		return ResponseEntity.status(OK).body("Password Changed Successfully");
	}

}
