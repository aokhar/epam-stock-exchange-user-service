package com.epam.rd.stock.exchange.security.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(value = "com.epam.rd.stock.exchange")
@EnableTransactionManagement
@Transactional
@Rollback
@AutoConfigureMockMvc(addFilters = false)
public abstract class AbstractIntegrationTest {

}
