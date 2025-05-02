package com.example.postservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    @RequestMapping(value = "/{value:^(?!api|static).*}")
    public String forward(@PathVariable String value) {
        return "forward:index.html";
    }
}
