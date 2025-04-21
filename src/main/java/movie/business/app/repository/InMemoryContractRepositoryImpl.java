package movie.business.app.repository;

import movie.business.app.model.Contract;

import java.util.*;

public class InMemoryContractRepositoryImpl implements ContractRepository {

    private final Map<String, Contract> contractMap = new HashMap<>();

    @Override
    public void addContract(Contract contract) {
        contractMap.put(contract.getId(), contract);
    }

    @Override
    public Optional<Contract> findById(String id) {
        return Optional.ofNullable(contractMap.get(id));
    }

    @Override
    public List<Contract> findAll() {
        return new ArrayList<>(contractMap.values());
    }

    @Override
    public void deleteById(String id) {
        contractMap.remove(id);
    }
}
