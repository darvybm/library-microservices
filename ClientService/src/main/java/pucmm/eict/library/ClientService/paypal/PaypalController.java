package pucmm.eict.library.ClientService.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pucmm.eict.library.ClientService.dto.Order;
import pucmm.eict.library.ClientService.service.OrderService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;
    private final OrderService orderService;


    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("amount") String amount,
            @RequestParam("orderId") String orderId,
            HttpServletRequest request
    ){
        try {
            String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();

            String cancel = baseUrl + "/payment/cancel";
            String successUrl = baseUrl + "/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    "USD",
                    "paypal",
                    "sale",
                    "Pago de Orden de Compra en BookHouse",
                    cancel,
                    successUrl
            );
            for (Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e){
            log.error("Error occurred::  " ,e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                return "cart/confirmation";
            }
        } catch (PayPalRESTException e){
            log.error("Error occurred::  " ,e);
        }
        return "cart/confirmation";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel(){
        return "cart/cancel";
    }

    @GetMapping("/payment/error")
    public String paymentError(){
        return "cart/error";
    }
}
