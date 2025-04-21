package movie.business.app.repository;

import movie.business.app.model.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    void addContract(Contract contract);
    Optional<Contract> findById(String id);
    List<Contract> findAll();
    void deleteById(String id);
}
