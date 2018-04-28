package io.rlogger.demo.services;

import org.springframework.stereotype.Service;

import io.rlogger.annotations.RLevel;

import io.rlogger.annotations.RLogger;

@RLogger(level = RLevel.DEBUG)
@Service
public class ExampleService {

	public String getName(final Class<?> clazz){
		return clazz.getName();
	}
}