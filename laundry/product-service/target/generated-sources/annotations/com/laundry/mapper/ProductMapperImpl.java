package com.laundry.mapper;

import com.laundry.dto.request.ProductRequest;
import com.laundry.dto.response.ProductResponse;
import com.laundry.entity.Items;
import com.laundry.entity.Prices;
import com.laundry.entity.ProductCategories;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T16:13:06+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Items toEntity(ProductRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Items.ItemsBuilder items = Items.builder();

        items.name( arg0.getName() );

        return items.build();
    }

    @Override
    public ProductResponse toResponse(Items arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( arg0.getId() );
        productResponse.name( arg0.getName() );
        productResponse.prices( toPriceMap( arg0.getPrices() ) );
        productResponse.avatar( arg0.getAvatar() );

        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toResponseList(List<Items> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( arg0.size() );
        for ( Items items : arg0 ) {
            list.add( toResponse( items ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ProductRequest arg0, Items arg1) {
        if ( arg0 == null ) {
            return;
        }

        arg1.setName( arg0.getName() );
    }

    @Override
    public Items toItem(ProductRequest dto, ProductCategories category) {
        if ( dto == null && category == null ) {
            return null;
        }

        Items.ItemsBuilder items = Items.builder();

        if ( dto != null ) {
            items.name( dto.getName() );
        }
        if ( category != null ) {
            items.category( category );
            items.description( category.getDescription() );
        }

        return items.build();
    }

    @Override
    public ProductResponse toResponse(Items entity, List<Prices> prices) {
        if ( entity == null && prices == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        if ( entity != null ) {
            productResponse.categoryId( entityCategoryId( entity ) );
            productResponse.id( entity.getId() );
            productResponse.name( entity.getName() );
            productResponse.avatar( entity.getAvatar() );
        }
        productResponse.prices( toPriceMap(prices) );

        return productResponse.build();
    }

    private Integer entityCategoryId(Items items) {
        ProductCategories category = items.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
