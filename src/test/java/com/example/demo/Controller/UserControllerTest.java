package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    //setup test data objects and initialize them and commit them to test DB repository object
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    User user4 = new User();
    User user5 = new User();

    @BeforeEach
    @Transactional
    @Rollback
    public void setUp() {
//        user1.setId(1L);user1.setEmail("ammar@email.com");user1.setPassword("112233");
//        user2.setId(2L);user2.setEmail("alex@email.com");user2.setPassword("445566");
//        user3.setId(3L);user3.setEmail("john@email.com");user3.setPassword("778899");
//        user4.setId(4L);user4.setEmail("Susan@email.com");user4.setPassword("010203");
//        user5.setId(5L);user5.setEmail("Ana@email.com");user5.setPassword("040506");

        user1.setEmail("ammar@email.com");user1.setPassword("112233");
        user2.setEmail("alex@email.com");user2.setPassword("445566");
        user3.setEmail("john@email.com");user3.setPassword("778899");
        user4.setEmail("Susan@email.com");user4.setPassword("010203");
        user5.setEmail("Ana@email.com");user5.setPassword("040506");

        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        repository.save(user4);
        repository.save(user5);
    }

   @Test
   @Transactional
   @Rollback
    public void testGettingUsersListFromDB() throws Exception{
       //[1]setup: for lists, you will need to create test data in DB
       //create user and save it to DB as test data in the beforeEach
       //Create the request object and the route
       MockHttpServletRequestBuilder request = get("/user");

       //execute
       //Option+Command+V to extract a variable for a Type
       ResultActions perform = this.mvc.perform(request);

       perform.andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id", equalTo(user1.getId().intValue())))
               .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
               .andExpect(jsonPath("$[0].password", is(user1.getPassword())))
               .andExpect(jsonPath("$[1].id", equalTo(user2.getId().intValue())))
               .andExpect(jsonPath("$[1].email", is(user2.getEmail())))
               .andExpect(jsonPath("$[1].password", is(user2.getPassword())))
               .andExpect(jsonPath("$[2].id", equalTo(user3.getId().intValue())))
               .andExpect(jsonPath("$[2].email", is(user3.getEmail())))
               .andExpect(jsonPath("$[2].password", is(user3.getPassword())));
   }

    @Test
    @Transactional
    @Rollback
    public void testGetUserById() throws Exception {
        //[2]setup: for getting a [User] by id or another variable, you will need to create test data in DB
        //the object returned [User] will be a single object, not a list
        //create user and save it to DB as test data in the beforeEach

        //Create the request object and the route
        MockHttpServletRequestBuilder request = get("/user/%d".formatted(user4.getId()));

        //execute the request by sending it to the mvc request. No need headers to send with a get request,
        // as contentType, accept, etc..
        ResultActions perform = this.mvc.perform(request);

        //assert
        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", equalTo(user3.getId().intValue())))
                .andExpect(jsonPath("$.id").exists())
                .andExpect((jsonPath("$.email", is(user4.getEmail()))))
                .andExpect((jsonPath("$.password", is(user4.getPassword()))));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUserInDB() throws Exception {
        //[3]setup: for creating/posting a [User], you will need to create test JSON data object and pass it as body
        // with the request. The object returned [User] will be a single object of the same posted User.
        var testJasonString = """
                {
                     "email": "ammar.masoud@example.com",
                      "password": "112233"
                }
                """;

        var testJasonString2 = """
                {
                     "email": "ammar.masoud@example.com",
                      "password": "112233"
                },
                     {
                     "email": "ana.turcios@example.com",
                      "password": "445566"
                }
                """;

        //Create the request object and the route
        MockHttpServletRequestBuilder request = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testJasonString)
                .accept(MediaType.APPLICATION_JSON);

        //execute the request by sending it to the mvc request. You will need headers values to send with a Post request,
        // as contentType, accept, etc.
        ResultActions perform = this.mvc.perform(request);

        //assert
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect((jsonPath("$.email", is("ammar.masoud@example.com"))));
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchUserEmailById() throws Exception {
        //[3]setup: for creating/posting a [User], you will need to create test JSON data object and pass it as body
        // with the request. The object returned [User] will be a single object of the same posted User.

        //setup the JSON to send with the request, either through an Object mapper or a json string.
        var jsonString = mapper.writeValueAsString(user1);

        //set the JSON to check the response against
        var jsonExpected = """
                {
                    "id":%d,
                    "email": "ammar@email.com"

                }
                """.formatted(user1.getId());
//        var jsonExpected = """
//                {
//                    "email": "ammar@email.com"
//                }
//                """;

        //setup the request object
        MockHttpServletRequestBuilder request = patch("/user/dto/%d".formatted(user1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString);

        //execute the request
        ResultActions perform = this.mvc.perform(request);

        //assert the request and response
        assertTrue(repository.findByEmail(user1.getEmail()).isPresent());
        assertEquals(repository.count(),5);

        perform.andExpect(status().isOk())
                .andExpect(content().json(jsonExpected))
                .andExpect((jsonPath("$.email", is(user1.getEmail()))))
                .andExpect((jsonPath("$.id").exists()));
    }
    @Test
    @Transactional
    @Rollback
    public void testPatchUserEmailAndPasswordById() throws Exception {
        //[3]setup: for creating/posting a [User], you will need to create test JSON data object and pass it as body
        // with the request. The object returned [User] will be a single object of the same posted User.

        //setup the JSON to send with the request, either through an Object mapper or a json string.
        var jsonString = mapper.writeValueAsString(user1);

        //set the JSON to check the response against
        var jsonExpected = """
                {
                    "id":%d,
                    "email": "ammar@email.com",
                    "password":"112233"
                
                }
                """.formatted(user1.getId());

        //setup the request object
        MockHttpServletRequestBuilder request = patch("/user/dto/%d".formatted(user1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString);

        //execute the request
        ResultActions perform = this.mvc.perform(request);

        //assert the request and response
        assertTrue(repository.findByEmail(user1.getEmail()).isPresent());
        assertEquals(repository.count(),5);

        perform.andExpect(status().isOk())
                .andExpect(content().json(jsonExpected))
                .andExpect((jsonPath("$.email", is(user1.getEmail()))))
                .andExpect((jsonPath("$.id").exists()))
                .andExpect((jsonPath("$.password", is(user1.getPassword()))));
    }


}
































