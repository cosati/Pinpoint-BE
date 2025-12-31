package com.cosati.photo_map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "folder.path=${java.io.tmpdir}/storage-test"
})
class PhotoMapApplicationTests {

	@Test
	void contextLoads() {
	}

}
