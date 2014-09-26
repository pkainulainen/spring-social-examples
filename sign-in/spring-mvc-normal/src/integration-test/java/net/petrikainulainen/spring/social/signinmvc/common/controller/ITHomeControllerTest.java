package net.petrikainulainen.spring.social.signinmvc.common.controller;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.petrikainulainen.spring.social.signinmvc.ColumnSensingFlatXMLDataSetLoader;
import net.petrikainulainen.spring.social.signinmvc.IntegrationTestConstants;
import net.petrikainulainen.spring.social.signinmvc.config.ExampleApplicationContext;
import net.petrikainulainen.spring.social.signinmvc.config.IntegrationTestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.server.samples.context.SecurityRequestPostProcessors.userDetailsService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class, IntegrationTestContext.class})
//@ContextConfiguration(locations = {"classpath:exampleApplicationContext.xml", "classpath:applicationContext-integrationTest.xml"})
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ColumnSensingFlatXMLDataSetLoader.class)
@DatabaseSetup("/net/petrikainulainen/spring/social/signinmvc/user/users.xml")
public class ITHomeControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void showHomePage_AsAnonymous_ShouldRenderLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void showHomePage_AsFacebookUser_ShouldRenderHomePage() throws Exception {
        mockMvc.perform(get("/")
                .with(userDetailsService(IntegrationTestConstants.User.FACEBOOK_USER.getUsername()))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }

    @Test
    public void showHomePage_AsRegisteredUser_ShouldRenderHomePage() throws Exception {
        mockMvc.perform(get("/")
                .with(userDetailsService(IntegrationTestConstants.User.REGISTERED_USER.getUsername()))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }

    @Test
    public void showHomePage_AsTwitterUser_ShouldRenderHomePage() throws Exception {
        mockMvc.perform(get("/")
                .with(userDetailsService(IntegrationTestConstants.User.TWITTER_USER.getUsername()))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }
}
