package com.laundry.service.impl;

import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.mapper.BaseMapper;
import com.laundry.repository.BaseRepository;
import com.laundry.service.BaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BaseServiceImp<T, ID, Req, Res,
        Map extends BaseMapper<T, Req, Res>,
        Repo extends BaseRepository<T, ID>>
        implements BaseService<T,ID, Req, Res> {

    Map mapper;
    Repo repository;

    protected abstract ErrorEnum getNotFoundError();

    @Override
    public List<Res> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    public Res findById(ID id) {
        T entity = repository.findById(id).orElseThrow(() -> new AppException(getNotFoundError()));
        return mapper.toResponse(entity);
    }

    @Override
    public Res save(Req entity) {
        T toEntity = mapper.toEntity(entity);
        T savedEntity = repository.save(toEntity);
        return mapper.toResponse(savedEntity);
    }

    @Override
    public Res update(ID id, Req reqest) {
        T entity = repository.findById(id).orElseThrow(() -> new AppException(getNotFoundError()));
        mapper.updateEntityFromRequest(reqest, entity);
        T updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(ID id) {
        T entity = repository.findById(id).orElseThrow(() -> new AppException(getNotFoundError()));
        repository.delete(entity);
    }
    @Override
    public void softDelete(ID id) {
        repository.findById(id).orElseThrow(() -> new AppException(getNotFoundError()));
        repository.softDelete(id);
    }
}
