package net.petrikainulainen.spring.social.signinmvc.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Petri Kainulainen
 */
@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String redirectRequestToRegisterPage() {
        LOGGER.debug("Redirecting request to register page.");

        return "redirect:/user/register";
    }

}
