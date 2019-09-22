package hu.elte.bm.authenticationservice.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestPropertySource({
    "classpath:messages.properties",
    "classpath:database-h2.properties" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public abstract class AbstractAuthenticationServiceApplicationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected MockMvc mvc;

}
