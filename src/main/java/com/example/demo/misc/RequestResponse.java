package com.example.demo.misc;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RequestResponse {
    /* #2. Access querystring data as individual items
    Accessing Query Strings//Name the method parameter the same as the querystring parameter
    Patch /tasks?filter=completed&owner=Chloe HTTP/1.1
    Host: example.com
     */
    @GetMapping("/individual-example")
    public String getIndividualParams(@RequestParam String filter) {
        return String.format("Filter is : %s", filter);
    }

    /*
    Accessing non-valid Java variable names
    GET /people?sort-by=firstName&sort-dir=desc HTTP/1.1
    Host: example.com
    */
    @GetMapping("/people")
    public String getPeople(
            @RequestParam("sort-by") String sortBy,
            @RequestParam(value = "sort-dir") String sortDirection) {
        return String.format("sortBy is %s and sortDirection is %s", sortBy, sortDirection);
    }

    /*
    Customizing and Accessing non-required and default query params
    GET //tasks?type=bike&owner=Chloe HTTP/1.1
    Host: example.com
    */
    @GetMapping("/vehicle")
    public String myCoolMethod(@RequestParam(required = false) String type) {
        return type;
    }

    @GetMapping("/other")
    public String myCoolMethodWithDefaultValue(@RequestParam(value = "type", defaultValue = "car")
                                                       String type) {
        return type;
    }

    /* #3 - Access querystring data as a map
    To get a Map (such as a HashMap or MultiValueMap) of all the querystring parameters
       - Define a single parameter in your method
       - Annotate your parameter with @RequestParam
         NOTE: If you want to access multiple params with the same name,
        use @RequestParam MultiValueMap<String, String>.
        This may happen when you are receiving data from form data where there are multiple
        checkboxes with the same name.
    */

    @GetMapping("/map-example")
    public String getMapParams(@RequestParam Map<String, Object> querystring) {
        return querystring.toString();
    }

    /* #4 - Access querystring data from an object
    Spring can also map query parameters to classes for you. In order to do this you will need to:
        - Define a class that has getters/setters
        - Add a parameter to your method that matches the type you created above
        GET /tasks?sortBy=title&owner=Chloe HTTP/1.1
        Host: example.com
    */

    @GetMapping("/tasks")
    public String getTasks(TaskInfo taskInfo) {
        return String.format("sort-by is %s; owner is %s",
                taskInfo.getSortBy(), taskInfo.getOwner());
    }

    //Accessing a Cookie
    @GetMapping("/cookie")
    public String getCookie(@CookieValue(name = "foo") String cookie) {
        return cookie;
    }

    /* #5 - Access path variables data from a route, a map, or an object
    In order to access path variables, follow these steps:
        1- Define an endpoint (as above)
        2- Add placeholders to the route definitions such as /tasks/{taskId}/comments/{commentId}
        3- Add 1 parameter per placeholder to your method
        4- Annotate each parameter with @PathVariable
     */
    @GetMapping("/individual-example/{q}/{from}") // matches /individual-example/foo/bar
    public String getIndividualParams(@PathVariable String from, @PathVariable("q") String query) {
        return String.format("q:%s from:%s", query, from);
    }

    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public String getCommentsForTask(@PathVariable int taskId, @PathVariable int commentId) {
        return String.format("taskId is %d; commentId is %d", taskId, commentId);
    }
    //as a map
    @GetMapping("/test/tasks/{taskId}/comments/{commentId}")
    public String getCommentsAsAMap(@PathVariable Map pathVariables) {
        return pathVariables.toString(); // {taskId=46, commentId=35}
    }

    @GetMapping("/test/tasks/{taskId}/comments/{commentId}")
    public String getCommentsForTask(TaskInfo ids) {
        return String.format("taskId is %d; commentId is %s", ids.getTaskId(), ids.getCommentId());
    }

}
