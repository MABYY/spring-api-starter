package com.codewithmosh.store.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name="Admin")
public class AdminController {

    @GetMapping("/home")
    public String admin() {
        return "Welcome to admin portal";
    }
}
