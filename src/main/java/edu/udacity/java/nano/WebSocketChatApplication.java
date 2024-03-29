package edu.udacity.java.nano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class WebSocketChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketChatApplication.class, args);
    }

    /**
     * This is the method which returns the login page
     * @return Login page.
     */
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    /**
     * This is the method which returns the chat room page
     * @return Chatroom Page.
     */
    @GetMapping("/index")
    public ModelAndView index(String username, HttpServletRequest request) throws UnknownHostException {
        ModelAndView chatPage = new ModelAndView("chat");
        chatPage.addObject("username", username);
        return chatPage;
    }
}
