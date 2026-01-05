//package com.laundry.controller;
//
//import com.laundry.dto.request.OrderRequest;
//import com.laundry.dto.response.ApiResponse;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.payos.PayOS;
//import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
//import vn.payos.model.webhooks.ConfirmWebhookResponse;
//
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/order")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
//public class OrderController {
//  PayOS payOS;
//  OrderService orderService;
//
//  @PostMapping(path = "/create")
//  public ApiResponse<CreatePaymentLinkResponse> createPaymentLink(
//          @RequestBody OrderRequest requestBody) {
//      return orderService.createPaymentLinkRequestBodyApiResponse(requestBody);
//  }
//
//  @PostMapping(path = "/confirm-webhook")
//  public ApiResponse<ConfirmWebhookResponse> confirmWebhook(
//          @RequestBody Map<String, String> requestBody) {
//    try {
//      ConfirmWebhookResponse result = payOS.webhooks().confirm(requestBody.get("webhookUrl"));
//      for (Map.Entry<String, String> entry : requestBody.entrySet()) {
//        System.out.println(entry.getKey() + ": " + entry.getValue());
//      }
//      return ApiResponse.<ConfirmWebhookResponse>builder()
//              .message("ok")
//              .data(result)
//              .build();
//    } catch (Exception e) {
//      e.printStackTrace();
//      return ApiResponse.<ConfirmWebhookResponse>builder()
//              .message(e.getMessage())
//              .build();
//    }
//  }
//
//}