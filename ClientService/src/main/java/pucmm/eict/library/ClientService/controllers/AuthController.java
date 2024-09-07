package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pucmm.eict.library.ClientService.config.CryptoConfig;
import pucmm.eict.library.ClientService.dto.*;
import org.springframework.ui.Model;
import pucmm.eict.library.ClientService.service.AuthService;


@Controller
public class AuthController {

    private final AuthService authService;

    private CryptoConfig cryptoConfig;

    public AuthController(AuthService authService, CryptoConfig cryptoConfig) {
        this.authService = authService;
        this.cryptoConfig = cryptoConfig;
    }

    @GetMapping("/login")
    public String login(Model model, Boolean expired) {
        model.addAttribute("loginDTO", new LoginDTO());
        if (expired != null && expired) {
            model.addAttribute("message", "Tu sesión ha expirado. Por favor inicia sesión.");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());

        return "auth/register";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {
        try {
            LoginDTO loginDTO = new LoginDTO(username, password);
            TokenDTO tokenDTO = authService.loginUser(loginDTO);

            String encryptedToken = cryptoConfig.encrypt(tokenDTO.getToken());
            Cookie cookie = new Cookie("jwt-token", encryptedToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("Inicio de sesión exitoso");
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el inicio de sesión: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del sistema: " + e.getMessage());
        }
    }



    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("jwt-token", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Devuelve los errores de validación
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        registerDTO.setRole("USER");
        try {
            UserResponse userResponse = authService.registerUser(registerDTO);
            return ResponseEntity.ok("Registro exitoso");
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el registro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del sistema: " + e.getMessage());
        }
    }

}
