package com.laundry.service;


import java.util.List;

public interface BaseService<T, ID, Req, Res> {
    List<Res> findAll();
    Res findById(ID id);
    Res save(Req entity);
    Res update(ID id, Req Reqest);
    void softDelete(ID id);
    void delete(ID id);

}
