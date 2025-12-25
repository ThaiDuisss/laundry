package com.laundry.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    // Xóa mềm
    @Modifying
    @Query("update #{#entityName} e set e.isDeleted = true where e.id = :id")
    void softDelete(@Param("id") ID id);

    @Transactional
    @Modifying
    void delete(T entity);

    // Lấy trong khoảng thời gian
    @Query("select e from #{#entityName} e where e.creatDate between :start and :end")
    List<T> findAllByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
