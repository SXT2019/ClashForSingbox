package com.cat.clashforsingbox;

import com.cat.clashforsingbox.utils.SingboxForClash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClashForSingboxApplicationTests {

	@Test
	void contextLoads() {

        SingboxForClash singboxForClash = new SingboxForClash();
        singboxForClash.outFile();

    }

}
