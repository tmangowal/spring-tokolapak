package com.cimb.tokolapak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	
	// User1 Password = 123321 -> abc123abc1212
	// User2 Password = 123321 -> cba1212ab123
	
	@Autowired
	private UserRepo userRepo;
	
	private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private EmailUtil emailUtil;
	
	@PostMapping
	public User registerUser(@RequestBody User user) {
//		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
//		
//		if (findUser.toString() != "Optional.empty") {
//			throw new RuntimeException("Username exists!");
//		}
		String encodedPassword = pwEncoder.encode(user.getPassword());
		String verifyToken = pwEncoder.encode(user.getUsername() + user.getEmail());
		
		user.setPassword(encodedPassword);
		user.setVerified(false);
		// Simpan verifyToken di database
		user.setVerifyToken(verifyToken);
		
		User savedUser = userRepo.save(user);
		savedUser.setPassword(null);
		
		// Kirim verifyToken si user ke emailnya user
		String linkToVerify = "http://localhost:8080/users/verify/" + user.getUsername() + "?token=" + verifyToken;
		
		String message = "<h1>Selamat! Registrasi Berhasil</h1>\n";
		message += "Akun dengan username " + user.getUsername() + " telah terdaftar!\n";
		message += "Klik <a href=\"" + linkToVerify + "\">link ini</a> untuk verifikasi email anda.";
		
		
		emailUtil.sendEmail(user.getEmail(), "Registrasi Akun", message);
		
		return savedUser;
	}
	
	// Cara 1 menggunakan POST method
	@PostMapping("/login")
	public User loginUser (@RequestBody User user) {
		User findUser = userRepo.findByUsername(user.getUsername()).get();
								// Password raw       password sudah encode
		if (pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		} 
		
		throw new RuntimeException("Wrong password!");
//		return null;
	}
	// localhost:8080/users/login?username=seto&password=password123
	@GetMapping("/login")
	public User getLoginUser(@RequestParam String username, @RequestParam String password) {
		User findUser = userRepo.findByUsername(username).get();

		if (pwEncoder.matches(password, findUser.getPassword())) {	
			findUser.setPassword(null);
			return findUser;
		} 

		throw new RuntimeException("Wrong password!");
	}
	
	@PostMapping("/sendEmail")
	public String sendEmailTesting() {
		this.emailUtil.sendEmail("thedevmango@gmail.com", "Testing Spring Mail", "<h1>Hey there</h1> \nApa Kabar?");
		return "Email Sent!";
	}
	
	@GetMapping("/verify/{username}")
	public String verifyUserEmail (@PathVariable String username, @RequestParam String token) {
		User findUser = userRepo.findByUsername(username).get();
		
		if (findUser.getVerifyToken().equals(token)) {
			findUser.setVerified(true);
		} else {
			throw new RuntimeException("Token is invalid");
		}
		
		userRepo.save(findUser);
		
		return "Sukses!";
	}
}
