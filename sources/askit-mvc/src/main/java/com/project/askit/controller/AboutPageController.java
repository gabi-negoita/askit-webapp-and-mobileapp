package com.project.askit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutPageController extends AbstractPageController {

    @Autowired
    public AboutPageController() {
        super();
    }

    @RequestMapping("/about")
    public Object showAboutPage() {
        return "about";
    }
}