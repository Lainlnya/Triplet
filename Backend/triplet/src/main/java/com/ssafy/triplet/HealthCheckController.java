package com.ssafy.triplet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}
