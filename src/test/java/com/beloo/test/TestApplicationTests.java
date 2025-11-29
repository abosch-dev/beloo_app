package com.beloo.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        // 1. Forzar el dialecto de H2 para los tests
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        // 2. Forzar el uso de H2 en memoria
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class TestApplicationTests {

    @Test
    void contextLoads() {
    }

}
