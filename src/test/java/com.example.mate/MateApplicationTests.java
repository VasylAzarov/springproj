package com.example.mate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.profiles.active=test"})
public class MateApplicationTests {

    @Test
    void contextLoads() {
    }
}
