package com.laundry.service.imp;

import com.laundry.dto.DTO.PageDto;
import com.laundry.dto.request.ProductRequest;
import com.laundry.dto.response.PageResponse;
import com.laundry.dto.response.ProductResponse;
import com.laundry.entity.Items;
import com.laundry.entity.Prices;
import com.laundry.entity.ProductCategories;
import com.laundry.entity.Services;
import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.mapper.ProductMapper;
import com.laundry.repository.CategoryRepository;
import com.laundry.repository.ItemRepository;
import com.laundry.repository.PriceRepository;
import com.laundry.repository.ServiceRepository;
import com.laundry.service.ProductService;
import com.laundry.service.impl.BaseServiceImp;
import com.laundry.service.impl.RedisService;
import jakarta.transaction.Transactional;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
@Slf4j(topic = "ProductService")
public class ProductServiceImp extends BaseServiceImp<Items, Integer, ProductRequest, ProductResponse, ProductMapper,ItemRepository> implements ProductService {
    PriceRepository priceRepository;
    CategoryRepository categoryRepository;
    ServiceRepository serviceRepo;
    RedisService redisService;
    

    public ProductServiceImp(ProductMapper mapper, ItemRepository repository, ItemRepository itemRepository, PriceRepository priceRepository, CategoryRepository categoryRepository, ServiceRepository serviceRepo, RedisService redisService) {
        super(mapper, repository);
        this.priceRepository = priceRepository;
        this.categoryRepository = categoryRepository;
        this.serviceRepo = serviceRepo;
        this.redisService = redisService;
    }

    @Override
    protected ErrorEnum getNotFoundError() {
        return null;
    }

    @Transactional
    @Override
    public ProductResponse save(ProductRequest request) {
        ProductCategories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Services> services = serviceRepo.findAll();

        for (Services s : services) {
            if (!request.getPrice().containsKey(s.getId())) {
                throw new AppException(ErrorEnum.SERVICE_NOT_FOUND);
            }
        }

        Items item = mapper.toItem(request, category);
        final Items savedItem  = repository.save(item);

        List<Prices> prices = new ArrayList<>();
        for (Services s : services) {
            Prices price = Prices.builder()
                    .item(savedItem)
                    .service(s)
                    .price(request.getPrice().get(s.getId()))
                    .build();
            prices.add(priceRepository.save(price));
        }

        // Xóa cache search
        String searchCachePattern = "product:search:*";
        // Note: Cần implement method deleteByPattern trong BaseRedisService
        
        return mapper.toResponse(item, prices);
    }

    @Override
    public List<ProductResponse> findByCategory(Integer categoryId) {
        //Lấy danh sách sản phẩm theo categoryId
        //Map sang ProductResponse và trả về
        return repository.findByCategoryId(categoryId).stream()
                .map(item ->
                    mapper.toResponse(item, priceRepository.findByItemId(item.getId()))
                ).toList();
    }

    @Override
    public PageResponse<ProductResponse> search(PageDto pageDto, Pageable pageable) {
        String cacheKey = redisService.buildKey("product:search", Map.of(
                "category", pageDto.getCategory() != null ? pageDto.getCategory() : "",
                "keyword", pageDto.getKeyword() != null ? pageDto.getKeyword() : "",
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "sort", pageable.getSort().toString()
        ));

        // Lấy cache
        Object cached = redisService.get(cacheKey);
        if (redisService.exists(cacheKey)) {
            return (PageResponse<ProductResponse>) cached;
        }

        log.info("Cache miss, query DB...");

        // Query DB
        Page<ProductResponse> pageResult = repository.search(pageDto.getCategory(), pageDto.getKeyword(), pageable)
                .map(item -> {
                    List<Prices> prices = priceRepository.findByItemId(item.getId());
                    return mapper.toResponse(item, prices);
                });

        // Convert sang PageResponse
        PageResponse<ProductResponse> response = PageResponse.from(pageResult);

        // Cache lại
        redisService.set(cacheKey, response);
        redisService.expireKey(cacheKey, 5, TimeUnit.DAYS);

        return response;
    }

}
