package com.telemed;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class TelemedController {

    @GetMapping("/telemedcalculate")
    public void calculate(@RequestParam("a") int a, @RequestParam("b") int b, @RequestParam("op") String op,
                          HttpServletResponse response) throws IOException {
        int result = 0;
        switch (op) {
            case "+" -> result = a + b;
            case "-" -> result = a - b;
            case "*" -> result = a * b;
            case "/" -> result = a / b;
        }
        response.getWriter().println("Result is: " + result);
    }

}