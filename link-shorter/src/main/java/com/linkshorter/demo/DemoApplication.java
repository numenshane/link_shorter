package com.linkshorter.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	private static String[] arguments;
	public static void main(String[] args) {
		arguments = args;
		SpringApplication.run(DemoApplication.class, args);
	}

	public static String getArgsByName(String name) {
		for (String item: arguments) {
			if (item.contains(name)) {
				return item.split("=")[1];
			}
		}
		return "";
	}
}
