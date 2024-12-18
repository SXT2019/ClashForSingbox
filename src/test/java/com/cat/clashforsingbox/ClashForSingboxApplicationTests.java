package com.cat.clashforsingbox;

import com.cat.clashforsingbox.utils.ClashForSingbox;
import com.cat.clashforsingbox.utils.SingboxForClash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClashForSingboxApplicationTests {

	@Test
	void contextLoads() {
		ClashForSingbox c = new ClashForSingbox();
		c.outFile();
    }

}
