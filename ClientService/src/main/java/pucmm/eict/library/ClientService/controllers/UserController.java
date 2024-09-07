package pucmm.eict.library.ClientService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/users/{id}/purchases")
    public String getPurchasesByUser(@PathVariable("id") long id, Model model) {
        return "purchases/list";
    }

}
