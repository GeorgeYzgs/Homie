package com.spring.group.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.spring.group.models.rental.PaymentLog;
import com.spring.group.models.rental.Rental;
import com.spring.group.services.PaypalService;
import com.spring.group.services.bases.PaymentLogServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author George.Giazitzis
 */
@Controller
public class PaymentController {

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private PaymentLogServiceInterface paymentLogService;
    @Autowired
    private RentalServiceInterface rentalService;

    @PostMapping("/pay")
    public String processPayment(@RequestParam("id") String id,
                                 @RequestParam("price") Double price) throws PayPalRESTException {
        Payment payment = paypalService.createPayment(price, id);
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return "redirect:" + link.getHref();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/pay/cancel")
    public String cancelPay(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageDanger", "Your payment has been cancelled");
        return "redirect:/";
    }

    //Cannot change the two param names, they are from the paypal json.
    @GetMapping("/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                             RedirectAttributes redirectAttributes) throws PayPalRESTException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            parsePaypalResponse(payment);
            redirectAttributes.addFlashAttribute("messageSuccess", "Your payment has been accepted!");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("messageDanger", "There was an issue with your payment!");
        return "redirect:/";
    }


    private void parsePaypalResponse(Payment payment) {
        int rentalID = Integer.parseInt(payment.getTransactions().get(0).getDescription());
        Rental rental = rentalService.getRentalByID(rentalID);
        Double amount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
        PaymentLog paymentLog = new PaymentLog(amount, rental);
        paymentLogService.insertPaymentLog(paymentLog);
    }
}
