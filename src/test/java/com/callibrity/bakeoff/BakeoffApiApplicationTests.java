package com.callibrity.bakeoff;

import com.callibrity.bakeoff.domain.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BakeoffApiApplicationTests {

	@Autowired
	private ArtistRepository repository;

	@Test
	void contextLoads() {
		assertThat(repository).isNotNull();
	}

}
