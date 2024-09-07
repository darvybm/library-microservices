package pucmm.eict.library.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailRegistrationDTO {
    @NotBlank(message = "Debes colocar un Recipiente")
    @Email(message = "Formato de Email Incorrecto")
    private String recipient;
    private String username;
    private String msgBody;
    private String subject;
}