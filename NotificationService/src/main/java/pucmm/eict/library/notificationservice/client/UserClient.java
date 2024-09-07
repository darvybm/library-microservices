package pucmm.eict.library.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pucmm.eict.library.notificationservice.dto.UserDTO;

@FeignClient(name = "AUTHSERVICE", path = "/users")
public interface UserClient {

    @GetMapping("/id/{id}")
    UserDTO getUserById(@PathVariable String id);
}