package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.OrderRequest;
import com.example.demo.domain.entity.OrderItems;
import com.example.demo.domain.entity.Orders;
import com.laundry.dto.DTO.ProductInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderItems toOrderItems(ProductInfo productInfo);
    Orders toOrders (OrderRequest request);
}
