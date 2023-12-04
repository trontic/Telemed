package com.todowebapp.todowebapp;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class WebCalculatorController {

    @GetMapping("/calculate")
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

    @GetMapping("/calculate2")
    public String calculate(@RequestParam("a") int a, @RequestParam("b") int b,
                          Model model) throws IOException {
        int result = a+b;
        model.addAttribute("result",result);

        return "calculator";
    }

}