package com.project.TeamGenerator.controllers;

import com.project.TeamGenerator.models.dto.UserDTO;
import com.project.TeamGenerator.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team-generator")
@AllArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserDTO userDTO, Model model) {
        try {
            log.info(userService.registerUser(userDTO));
        }catch (NullPointerException e){
            model.addAttribute("error", "Empty username or password");
            return "register";
        }catch (Exception e) {
            model.addAttribute("error", "Username is already in use");
            return "register";
        }

        return "redirect:/team-generator/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
