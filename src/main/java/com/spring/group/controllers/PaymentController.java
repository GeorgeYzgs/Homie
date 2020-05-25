package com.spring.group.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.spring.group.models.rental.PaymentLog;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.services.PaypalService;
import com.spring.group.services.TokenService;
import com.spring.group.services.bases.PaymentLogServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

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
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MessageSource messageSource;

    @PostMapping("/pay")
    public String processPayment(@RequestParam("id") String id, @RequestParam("price") Double price,
                                 RedirectAttributes redirectAttributes, Locale userLocale) throws PayPalRESTException {
        Rental rental = rentalService.getRentalByID(Integer.parseInt(id));
        if (rentalService.hasPaidRent(rental)) {
            redirectAttributes.addFlashAttribute("messageDanger",
                    messageSource.getMessage("Rent.already.paid", null, userLocale));
            return "redirect:/my-profile/properties";
        }
        if (price != rental.getAgreedPrice()) {
            redirectAttributes.addFlashAttribute("messageDanger",
                    messageSource.getMessage("Rent.wrong.price", null, userLocale));
            return "redirect:/my-profile/properties";
        }
        Payment payment = paypalService.createPayment(price, id);
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return "redirect:" + link.getHref();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/pay/cancel")
    public String cancelPay(RedirectAttributes redirectAttributes, Locale userLocale) {
        redirectAttributes.addFlashAttribute("messageDanger", messageSource.getMessage("Payment.cancel", null, userLocale));
        return "redirect:/my-profile/properties";
    }

    //Cannot change the two param names, they are from the paypal json.
    @GetMapping("/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                             RedirectAttributes redirectAttributes, Locale userLocale) throws PayPalRESTException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            parsePaypalResponse(payment);
            redirectAttributes.addFlashAttribute("messageSuccess",
                    messageSource.getMessage("Payment.accepted", null, userLocale));
            return "redirect:/my-profile/properties";
        }
        redirectAttributes.addFlashAttribute("messageDanger",
                messageSource.getMessage("Payment.declined", null, userLocale));
        return "redirect:/my-profile/properties";
    }

    //TODO email owner and tenant?
    @PostMapping("/close-contract")
    public String closeContract(@RequestParam("id") Integer id, Authentication auth,
                                RedirectAttributes redirectAttributes, Locale userLocale) {
        Rental rental = rentalService.getRentalByID(id);
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        if (!rentalService.hasPaidRent(rental)) {
            redirectAttributes.addFlashAttribute("messageDanger",
                    messageSource.getMessage("Contract.close.fail", null, userLocale));
            return "redirect:/my-profile/properties";
        }
        if (rental.getTenant().getId() != loggedUser.getId()) {
            redirectAttributes.addFlashAttribute("messageDanger",
                    messageSource.getMessage("Contract.close.other", null, userLocale));
            return "redirect:/my-profile/properties";
        }
        rentalService.closeRental(rental);
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Contract.close.success", null, userLocale));
        return "redirect:/my-profile/properties";
    }

    private void parsePaypalResponse(Payment payment) {
        int rentalID = Integer.parseInt(payment.getTransactions().get(0).getDescription());
        Rental rental = rentalService.getRentalByID(rentalID);
        Double amount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
        PaymentLog paymentLog = new PaymentLog(amount, rental);
        tokenService.informPayment(rental.getTenant());
        paymentLogService.insertPaymentLog(paymentLog);
    }
}
