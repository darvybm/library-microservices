package pucmm.eict.library.notificationservice.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pucmm.eict.library.notificationservice.client.UserClient;
import pucmm.eict.library.notificationservice.dto.MailRegistrationDTO;
import pucmm.eict.library.notificationservice.dto.OrderDTO;
import pucmm.eict.library.notificationservice.dto.UserDTO;
import pucmm.eict.library.notificationservice.util.DynamicTemplatePersonalization;

import java.util.Base64;

@Service
public class MailService {

    @Value("${sendgrid.api.key}")
    private String API_KEY;

    @Value("${sendgrid.api.sender-mail}")
    private String SENDER_MAIL;

    private final UserClient userClient;

    @Autowired
    public MailService(UserClient userClient) {
        this.userClient = userClient;
    }

    public Response sendEmailRegistration(MailRegistrationDTO mailRegistrationDTO) {

        final String TEMPLATE_ID = "d-ffc8f556ac294c4985a772dd8c9ee054";

        Email from = new Email(SENDER_MAIL);
        Email to = new Email(mailRegistrationDTO.getRecipient());
        Mail mail = new Mail();

        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        personalization.addTo(to);
        mail.setFrom(from);
        mail.setSubject(mailRegistrationDTO.getSubject());

        personalization.addDynamicTemplateData("username", mailRegistrationDTO.getUsername());
        mail.addPersonalization(personalization);
        mail.setTemplateId(TEMPLATE_ID);

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        Response response = null;

        try {
            System.out.println("MAIL");
            System.out.println(mail.build());
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sg.api(request);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }

    public Response sendEmailWithPdf(OrderDTO orderDTO, byte[] pdfBytes, String pdfFilename) {

        final String TEMPLATE_ID = "d-376e9cb9901b4ff4abd33ff1277433b8";

        UserDTO userDTO = userClient.getUserById(orderDTO.getUserId());
        Email from = new Email(SENDER_MAIL);
        Email to = new Email(userDTO.getEmail());
        Mail mail = new Mail();

        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        personalization.addTo(to);
        mail.setFrom(from);
        mail.setSubject("Orden de Compra Confirmada");

        personalization.addDynamicTemplateData("username", userDTO.getUsername());
        personalization.addDynamicTemplateData("orderId", orderDTO.getId());
        personalization.addDynamicTemplateData("amount", orderDTO.getTotalPrice());
        mail.addPersonalization(personalization);
        mail.setTemplateId(TEMPLATE_ID);

        // Attach PDF
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
        Attachments attachments = new Attachments();
        attachments.setContent(base64Pdf);
        attachments.setType("application/pdf");
        attachments.setFilename(pdfFilename);
        attachments.setDisposition("attachment");
        mail.addAttachments(attachments);

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        Response response = null;

        try {
            System.out.println("MAIL WITH PDF");
            System.out.println(mail.build());
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sg.api(request);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }
}
