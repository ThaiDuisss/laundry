package com.laundry.service;

import com.laundry.dto.DTO.PageDto;
import com.laundry.dto.request.ProductRequest;
import com.laundry.dto.response.PageResponse;
import com.laundry.dto.response.ProductResponse;
import com.laundry.entity.Items;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService extends BaseService<Items, Integer, ProductRequest, ProductResponse>{
    List<ProductResponse> findByCategory (Integer categoryId);
    PageResponse<ProductResponse> search(PageDto pageDto, Pageable pageable);
}
