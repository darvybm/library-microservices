package pucmm.eict.library.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pucmm.eict.library.authservice.dto.MailRegistrationDTO;

@FeignClient(name = "NOTIFICATIONSERVICE")
public interface NotificationClient {

    @PostMapping("/mail/send-registration")
    ResponseEntity<?> sendMail(@RequestBody MailRegistrationDTO mailRegistrationDTO);

}
