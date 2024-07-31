package com.oseanchen.demoapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoAppApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	private String NAME = "name";
	private String NAME_VALUE = "Sean";
	private String AGE = "age";
	private int AGE_VALUE = 31;

	@Test
	void set() {
		redisTemplate.opsForValue().set(NAME, NAME_VALUE, 5, TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(AGE, AGE_VALUE);
	}

	@Test
	void get() {
		Object age = redisTemplate.opsForValue().get(NAME);
		redisTemplate.delete(NAME);
		System.out.println(age);
	}

}
