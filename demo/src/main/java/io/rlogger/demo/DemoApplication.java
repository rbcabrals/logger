package io.rlogger.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import io.rlogger.demo.services.ExampleService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Component
	public static class Demo implements CommandLineRunner {

		@Autowired
		private ExampleService exampleService;

		@Override
		public void run(String... args) throws Exception {
			exampleService.getName(this.getClass());
		}
	}
}
