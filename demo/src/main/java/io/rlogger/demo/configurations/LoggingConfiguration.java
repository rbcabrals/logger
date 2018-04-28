package io.rlogger.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.rlogger.handlers.LoggingHandler;

@Configuration
public class LoggingConfiguration {

	@Bean
	public LoggingHandler loggindHandler() {
		return new LoggingHandler();
	}
}