package com.example.demo.controller;

import com.example.demo.dto.UserAuthenticationDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    //inject the User Repository using constructor injection
    private final UserRepository userTable;

    public UserController(UserRepository userTable) {
        this.userTable = userTable;
    }

    /*
    There are different ways to refer to the methods inside controllers. You'll hear them called:
     - "routes"
     - "endpoints"
     - "request mappings"
      - "route mappings"
    Those are all effectively synonymous.
     */

    //1- Get all (List): "/user".  JPA method: .findAll [returns a list of users from the DB]
    // takes no args in method
    @GetMapping("")
    public Iterable<User> findAllUsers() {
        return userTable.findAll();
    }

    /*2- Get a single user (Read) "/user/{id}".  JPA method: .findById [returns a single user]
       takes a @PathVariable param + Type and variable.
       ***one way with Optional<User>***
        @GetMapping("/user/{id}")
        public Optional<User> getUserById(@PathVariable Long id){
            return userTable.findById(id);
    }
        ***OR***
        By using the Entity type as a return type and amend the .get() method at the end
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        //Changing the JSON views within the methods. Return a MappingJacksonValue object
        User user = userTable.findById(id).get();
        MappingJacksonValue value = new MappingJacksonValue(user);

        if (userTable.existsById(id)) {
            return userTable.findById(id).get();
//      return userTable.findById(id).orElseThrow();
        } else {
            value.setSerializationView(Views.ListView.class);
            return new User();
        }
    }

    //3- Post/create a new user (Create) "/user".  JPA method: .save [creates a new user]
    // takes @RequestBody param and Entity object
    @PostMapping("")
    @JsonView(Views.SecretView.class) // to restrict what you send back with the JSON response for the password
    public User createNewUser(@RequestBody User user) {
        return userTable.save(user);
    }

    //4- Patch/update a user (Update) "/user/{id}".  JPA method: .save [updates attributes of user]
    // takes PathParam to look up Entity and @RequestBody to
    @PatchMapping("/{id}")
    @JsonView(Views.SecretView.class)
    public User updateUserInfo(@PathVariable Long id, @RequestBody Map<String, Object> updatedUser) {
        //first step in a patch, get the user record to update
        User currentUser = userTable.findById(id).get();

        //----------------------------------
//        updatedUser.forEach((key, value) -> {
//            switch (key){
//                case "email":currentUser.setEmail(value.toString()); break;
//                case "password": currentUser.setPassword((String) value);
//            }
//        });
        //----------------------------------

        //-----------OR---------------------
        if (updatedUser.containsKey("email")) {
            currentUser.setEmail(updatedUser.get("email").toString());
        }
        if (updatedUser.containsKey("password")) {
            currentUser.setPassword(updatedUser.get("password").toString());
        }
        //-----------OR---------------------

        //save the updated current user into the DB
        return userTable.save(currentUser);
    }

    //4- Patch/update a user (Update) "/user/{id}".  JPA method: .save [updates attributes of user]
    @PatchMapping("/dto/{id}")
//    @JsonView(Views.SecretView.class)
    public User updateUserInfoUsingDTO(@PathVariable Long id, @RequestBody UserDto updatedUser) {
        //first step in a patch, get the user record to update
        User currentUser = userTable.findById(id).get();

        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPassword(updatedUser.getPassword());

        //save the updated current user into the DB
        return userTable.save(currentUser);
    }

    //5- Delete a user (Delete) "/user/{id}".  JPA method: .deleteById [delete a user row]
    // takes a @Pathvariable id to get the user

    @DeleteMapping("/{id}")
    public Map<String, String> deleteUserById(@PathVariable Long id) {
        Map<String, String> deleteResult = new HashMap<>();

        try {
            userTable.deleteById(id);
            deleteResult.put("count", String.valueOf(userTable.count()));
        } catch (EmptyResultDataAccessException e) {
            deleteResult.put("error", "User id: " + id + " does not exist. It was either deleted or never created.");
        }
        return deleteResult;
    }

    @PostMapping("/authenticate")
    @JsonView(Views.SecretView.class)
    public Map<String, Object> authenticateUser(@RequestBody UserDto userReceived) {
        Map<String, Object> jsonResponse = new HashMap<>();
        UserAuthenticationDto authenticationDto = new UserAuthenticationDto(); //come back to refactor Egor orElseThrow
        Optional<User> user = userTable.findByEmail(userReceived.getEmail());
        jsonResponse.put("authenticated", authenticationDto.isAuthenticated());

//        user.ifPresent((e) -> { //the e is the user returned object
//        });

        if (user.isPresent()) {
            if (user.get().getPassword().equals(userReceived.getPassword())) {
                authenticationDto.setAuthenticated(true);
                jsonResponse.put("authenticated", authenticationDto.isAuthenticated());
                jsonResponse.put("user", user);

            } else {
                jsonResponse.put("authenticated", authenticationDto.isAuthenticated());
            }
        }
        return jsonResponse;
    }
}
