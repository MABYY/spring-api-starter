package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.responses.Message;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Bean: Java creates and manages a new instance when the app start
@Tag(name="Home")
public class HomeController {

    @GetMapping("/home")
    public Message getHome () {
        return new  Message("Wonderful");
    };
}
