package ru.aklyakhandler.websocketdemo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.aklyakhandler.websocketdemo.model.data.Contract;

import java.util.List;

public interface ContractRepository extends CrudRepository<Contract, Long> {

    @Query("SELECT stock_id FROM contract WHERE user_id = :userId")
    List<Long> getStockIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT * FROM contract WHERE user_id = :userId")
    List<Contract> getContractsByUserId(@Param("userId") Long userId);
}
