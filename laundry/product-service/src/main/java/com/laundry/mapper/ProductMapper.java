package com.laundry.mapper;

import com.laundry.dto.request.ProductRequest;
import com.laundry.dto.response.ProductResponse;
import com.laundry.entity.Items;
import com.laundry.entity.Prices;
import com.laundry.entity.ProductCategories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Items, ProductRequest, ProductResponse> {
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "category", source = "category")
    Items toItem(ProductRequest dto, ProductCategories category);

    @Mapping(target = "categoryId", source = "entity.category.id")
    @Mapping(target = "prices", expression = "java(toPriceMap(prices))")
    ProductResponse toResponse(Items entity, List<Prices> prices);

    default Map<Integer, BigDecimal> toPriceMap(List<Prices> prices) {
        return prices.stream().collect(Collectors.toMap(p -> p.getService().getId(), Prices::getPrice));
    }

}
