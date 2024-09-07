package pucmm.eict.library.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pucmm.eict.library.notificationservice.dto.BookDTO;

@FeignClient(name = "CATALOGUESERVICE", path = "/catalogue")
public interface CatalogueClient {

    @GetMapping("/{id}")
    BookDTO getBook(@PathVariable String id);
}