package com.example.demo.misc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class RequestResponseTest {

    @Autowired
    private MockMvc mvc;

    //QueryString
    @Test
    public void testIndexEndpointQS() throws Exception {
        this.mvc.perform(get("/vehicles?year=1987&doors=2"))
                .andExpect(status().isOk());
    }

    //Path variables
    @Test
    public void testIndexEndpointPV() throws Exception {
        int driverId = 4; // in real life you might pull this from a database...

        this.mvc.perform(get(String.format("/drivers/%d/trips", driverId)))
                .andExpect(status().isOk());
    }

    //url-encoded form data

}
