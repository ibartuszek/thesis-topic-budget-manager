package hu.elte.bm.authenticationservice.app;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest
@TestPropertySource({
    "classpath:common_constraints.properties",
    "classpath:messages.properties",
    "classpath:database-h2.properties" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractAuthenticationServiceApplicationTest extends AbstractTestNGSpringContextTests {

}
