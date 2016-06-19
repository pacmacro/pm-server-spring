package com.pm.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PmServerApplication.class)
@ComponentScan("com.pm.server")
@WebAppConfiguration
public class PmServerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
