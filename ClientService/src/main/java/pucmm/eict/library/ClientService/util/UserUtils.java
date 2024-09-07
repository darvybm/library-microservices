package pucmm.eict.library.ClientService.util;

import jakarta.servlet.http.HttpServletRequest;
import pucmm.eict.library.ClientService.dto.UserDTO;
import pucmm.eict.library.ClientService.dto.UserResponse;

public class UserUtils {

    public static UserResponse getCurrentUser(HttpServletRequest request) {
        return (UserResponse) request.getAttribute("user");
    }
}
