package com.springProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication
public class RedditCloneV2Application {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneV2Application.class, args);
	}

}
