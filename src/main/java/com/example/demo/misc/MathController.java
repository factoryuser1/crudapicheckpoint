package com.example.demo.misc;

import com.example.demo.service.MathService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/math")
public class MathController {
    ///math/calculate?operation=add&x=4&y=6 should render the string 4 + 6 = 10
    @GetMapping("/calculate")
    public String getResult(@RequestParam(value = "operator", defaultValue = "add") String operator,
                            @RequestParam String x,
                            @RequestParam String y)
    {
        Integer a = Integer.valueOf(x);
        Integer b = Integer.valueOf(y);

        return switch (operator) {
            case "subtract" -> a + " - " + b + " = " + (a - b);
            case "multiply" -> a + " * " + b + " = " + (a * b);
            case "divide" -> a + " / " + b + " = " + (a / b);
            default -> a + " + " + b + " = " + (a + b);
        };
    }

    @PostMapping("/sum")
    public String getSum(@RequestParam("n") List<String> params) {
        String sumResult = MathService.getSumString(params);
        return sumResult;
    }
}
