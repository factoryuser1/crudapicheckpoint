package com.example.demo.misc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestResponseTest {

    @Autowired
    private MockMvc mvc;

    //QueryString
    @Test
    public void testIndexEndpointQS() throws Exception {
        this.mvc.perform(get("/vehicles?year=1987&doors=2"));
//                .andExpect(status().isOk());
    }

    /*Path variables
     */

    @Test
    public void testIndexEndpointPV() throws Exception {
        int driverId = 4; // in real life you might pull this from a database...

        this.mvc.perform(get(String.format("/drivers/%d/trips", driverId)));
//                .andExpect(status().isOk());
    }

    /*    url-encoded form data
        The goal of your test is to create the following HTTP request:
        POST /comments HTTP/1.1
        Host: example.com
        Content-Type: application/x-www-form-urlencoded
        content=Firsties!&author=Dwayne
     */
    @Test
    public void testCreateComment() throws Exception {
        String content = String.valueOf(new Random().nextInt());

        MockHttpServletRequestBuilder request1 = post("/comments")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", content)
                .param("author", "Dwayne");

        this.mvc.perform(request1);
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.format("Dwayne said %s!", content)));
    }

    @Test
    public void testCookies() throws Exception {
        this.mvc.perform(get("/cookie").cookie(new Cookie("foo", "bar")))
                .andExpect(status().isOk())
                .andExpect(content().string("bar"));
    }

    @Test
    public void testHeaders() throws Exception {
        this.mvc.perform(get("/header").header("Host", "example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("example.com"));
    }
}
