//package com.laundry.controller;
//
//import com.laundry.dto.DTO.PageDto;
//import com.laundry.dto.request.PaymentRequest;
//import com.laundry.dto.response.ApiResponse;
//import com.laundry.dto.response.PageResponse;
//import com.laundry.dto.response.PaymentResponse;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping
//public class PaymentController extends BaseController<Items, Integer, PaymentRequest, PaymentResponse, ProductService> {
//
//    public PaymentController(ProductService service) {
//        super(service);
//    }
//    @GetMapping("/category/{categoryId}")
//    public List<PaymentResponse> getItemsByCategory(@PathVariable("categoryId") Integer categoryId) {
//        return service.findByCategory(categoryId);
//    }
//    protected Pageable buildPageable(int page, int size) {
//        if (page < 0) page = 0;
//        if (size <= 0) size = 10;
//        return PageRequest.of(page, size);
//    }
//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse<?>> getAllOrSearch(PageDto page, Pageable pageable) {
//        PageResponse<PaymentResponse> result = service.search(page, pageable);
//        return ResponseEntity.ok().body(ApiResponse.builder()
//                .data(result)
//                .code(200)
//                .message("Successfully")
//                .build());
//    }
//}
