package pucmm.eict.library.notificationservice.controller;

import com.sendgrid.helpers.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pucmm.eict.library.notificationservice.dto.OrderDTO;
import pucmm.eict.library.notificationservice.service.JReportService;
import pucmm.eict.library.notificationservice.service.MailService;

@Controller
@RequestMapping("/reports")
public class InvoiceController {

    private final JReportService jReportService;
    private final MailService mailService;

    @Autowired
    public InvoiceController(JReportService jReportService, MailService mailService) {
        this.jReportService = jReportService;
        this.mailService = mailService;
    }

    @PostMapping("/invoice")
    public ResponseEntity<byte[]> generateInvoice(@RequestBody OrderDTO orderDTO) {
        try {
            byte[] pdfBytes = jReportService.exportReport(orderDTO);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "invoice.pdf");
            mailService.sendEmailWithPdf(orderDTO, pdfBytes, "compra-"+orderDTO.getId()+".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
