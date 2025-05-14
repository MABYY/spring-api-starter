package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckoutRequestDTO;
import com.codewithmosh.store.dtos.CheckoutResponseDTO;
import com.codewithmosh.store.dtos.ErrorDTO;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;

import com.stripe.net.Webhook;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@AllArgsConstructor
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;
    private final OrderRepository orderRepository;


    @PostMapping("/place")
    public CheckoutResponseDTO checkout( @Valid @RequestBody CheckoutRequestDTO request) {
        return checkoutService.checkout(request);
    };


    @PostMapping("/webhook")
    public ResponseEntity<Void> handleStripeWebhook(@RequestBody String payload,
                        @RequestHeader("Stripe-Signature") String signature)
    {

        try {

            var event =  Webhook.constructEvent(payload, signature, webhookSecretKey);
            var stripeObject =  event.getDataObjectDeserializer().getObject().orElse(null);
            var paymentintent = (PaymentIntent) stripeObject;

            switch (event.getType()) { // check stripe-java version is the latest in pom.xml file @ dev dashboard
               case "payment_intent.succeeded" ->{
                  if(paymentintent != null) {
                      var orderId = paymentintent.getMetadata().get("order_id");
                      var order = orderRepository.findById(Long.valueOf(orderId)).orElse(null);
                      if(order != null) {
                          order.setStatus(OrderStatus.PAID);
                          orderRepository.save(order);
                      }
                  }
               }

               case "payment_intent.payment_failed" ->{
                   if(paymentintent != null) {
                       var orderId = paymentintent.getMetadata().get("order_id");
                       var order = orderRepository.findById(Long.valueOf(orderId)).orElse(null);
                       if(order != null) {
                           order.setStatus(OrderStatus.FAILED);
                           orderRepository.save(order);
                       }
                   }
               }

               default ->{

                }

           }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

    };

@ExceptionHandler({PaymentException.class})
    public ResponseEntity<?> handlePaymentException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO("Error creating checkout session"));
    }
    @ExceptionHandler({CartNotFoundException.class, CartNotFoundException.class })
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }




}
