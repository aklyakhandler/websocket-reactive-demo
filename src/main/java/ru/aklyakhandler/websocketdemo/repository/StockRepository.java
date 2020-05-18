package ru.aklyakhandler.websocketdemo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.aklyakhandler.websocketdemo.model.data.Stock;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock, Long> {
    @Query("SELECT * FROM stock WHERE id IN (:stockIds)")
    List<Stock> getStockByIdIn(@Param("stockIds") Iterable<Long> stockIds);
}
