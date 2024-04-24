package api;

import com.hackerrank.test.utility.Order;
import com.hackerrank.test.utility.OrderedTestRunner;
import com.hackerrank.test.utility.TestWatchman;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(OrderedTestRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GreetingsControllerTest {

    /**
     * Integer used to specify the last executed test.
     */
    private static final int FINALTEST = 3;

    /**
     * Classrule used for testing purposes.
     */
    @ClassRule
    public static final SpringClassRule SPRINGCLASSRULE = new SpringClassRule();

    /**
     * Rule definition used for testing purposes.
     */
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    /**
     * Rule definition used for testing purposes.
     */
    @Rule
    public TestWatcher watchman = TestWatchman.watchman;

    /**
     * Mock object used for testing purposes.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Setup of the Springboot server.
     */
    @BeforeClass
    public static void setUpClass() {
        TestWatchman.watchman.registerClass(GreetingsControllerTest.class);
    }

    /**
     * Shutdown of the Springboot server.
     */
    @AfterClass
    public static void tearDownClass() {
        TestWatchman.watchman.createReport(GreetingsControllerTest.class);
    }

    /**
     *
     * @throws Exception
     *
     *                   It tests response to be "Hello Java!"
     */
    @Test
    @Order(1)
    public void greetJava() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/Java"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(response, "Hello Java!");
    }

    /**
     *
     * @throws Exception
     *
     *                   It tests response to be "Hello Spring!"
     */
    @Test
    @Order(2)
    public void greetSpring() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/Spring"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(response, "Hello Spring!");
    }

    /**
     *
     * @throws Exception
     *
     *                   It tests response to be "Hello RodJohnson!"
     */
    @Test
    @Order(FINALTEST)
    public void greetRodJohnson() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/RodJohnson"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(response, "Hello RodJohnson!");
    }
}
