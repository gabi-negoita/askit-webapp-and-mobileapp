package com.project.askit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootPageController extends AbstractPageController {

    @RequestMapping("/")
    public Object rootRedirect() {
        return new RedirectView("/home", true);
    }
}
