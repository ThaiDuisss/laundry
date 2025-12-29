package com.laundry.mapper;

import com.laundry.dto.DTO.ProductInfo;
import com.laundry.dto.request.OrderRequest;
import com.laundry.entity.OrderItems;
import com.laundry.entity.Orders;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderItems toOrderItems(ProductInfo productInfo);
    Orders toOrders (OrderRequest request);
}
