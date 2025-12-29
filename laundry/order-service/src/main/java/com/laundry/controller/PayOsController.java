package com.laundry.controller;

import com.laundry.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;
import vn.payos.core.FileDownloadResponse;
import vn.payos.exception.APIException;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.invoices.InvoicesInfo;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@RestController("/payos/orders")
public class PayOsController {
    PayOS payOS;
    @GetMapping(path = "/{orderId}")
    public ApiResponse<PaymentLink> getOrderById(@PathVariable("orderId") long orderId) {
        try {
            PaymentLink order = payOS.paymentRequests().get(orderId);
            return ApiResponse.<PaymentLink>builder()
                    .message("ok")
                    .data(order)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<PaymentLink>builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @PutMapping(path = "/{orderId}")
    public ApiResponse<PaymentLink> cancelOrder(@PathVariable("orderId") long orderId) {
        try {
            PaymentLink order = payOS.paymentRequests().cancel(orderId, "change my mind");
            return ApiResponse.<PaymentLink>builder()
                    .message("ok")
                    .data(order)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.<PaymentLink>builder()
                    .message(e.getMessage())
                    .build();
        }
    }


    @GetMapping(path = "/{orderId}/invoices")
    public ApiResponse<InvoicesInfo> retrieveInvoices(@PathVariable("orderId") long orderId) {
        try {
            InvoicesInfo invoicesInfo = payOS.paymentRequests().invoices().get(orderId);
            return ApiResponse.<InvoicesInfo>builder()
                    .message("ok")
                    .data(invoicesInfo)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.<InvoicesInfo>builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping(path = "/{orderId}/invoices/{invoiceId}/download")
    public ResponseEntity<?> downloadInvoice(
            @PathVariable("orderId") long orderId, @PathVariable("invoiceId") String invoiceId) {
        try {
            FileDownloadResponse invoiceFile =
                    payOS.paymentRequests().invoices().download(invoiceId, orderId);

            if (invoiceFile == null || invoiceFile.getData() == null) {
                return ResponseEntity.status(500).body( ApiResponse.builder()
                        .message("Fail")
                        .build());
            }

            ByteArrayResource resource = new ByteArrayResource(invoiceFile.getData());

            HttpHeaders headers = new HttpHeaders();
            String contentType =
                    invoiceFile.getContentType() == null
                            ? MediaType.APPLICATION_PDF_VALUE
                            : invoiceFile.getContentType();
            headers.set(HttpHeaders.CONTENT_TYPE, contentType);
            headers.set(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + invoiceFile.getFilename() + "\"");
            if (invoiceFile.getSize() != null) {
                headers.setContentLength(invoiceFile.getSize());
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (APIException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body( ApiResponse.<InvoicesInfo>builder()
                    .message(e.getMessage())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body( ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
