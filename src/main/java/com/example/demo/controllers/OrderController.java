package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{userName}")
	public ResponseEntity<UserOrder> submit(@PathVariable String userName) {

		User user = userRepository.findByUserName(userName);

		if(user == null) {
			log.error("[Fail] - [SUBMIT ORDER] - User name \"" + userName + "\" not found" );
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.info("[Success] - [SUBMIT ORDER] - User name \"" + userName + "\" has submit an order successfully");

		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{userName}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[Fail] - [ORDER HISTORY] - User name \"" + userName + "\" not found" );
			return ResponseEntity.notFound().build();
		}

		List<UserOrder> history = orderRepository.findByUser(user);

		log.info("[Success] - [ORDER HISTORY] - Getting order history of user name \"" + userName + "\" successfully");
		return ResponseEntity.ok(history);
	}
}
