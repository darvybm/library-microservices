package pucmm.eict.library.notificationservice.controller;

import com.sendgrid.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.notificationservice.dto.MailRegistrationDTO;
import pucmm.eict.library.notificationservice.service.MailService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/mail")
@RestController
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send-registration")
    public ResponseEntity<?> sendMail(@Valid @RequestBody MailRegistrationDTO mailRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        Response response = mailService.sendEmailRegistration(mailRegistrationDTO);

        if (response == null) {
            return ResponseEntity.badRequest().body("Error sending email");
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
