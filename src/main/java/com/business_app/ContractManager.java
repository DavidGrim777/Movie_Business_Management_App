package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class ContractManager {
    private List<Contract> contracts;

    public ContractManager() {
        this.contracts = new ArrayList<>();
    }
    public void addContract(Contract contract){
        if (contract == null){
            System.out.println("Попытка добавить null-контракт.");
            log.warn("Попытка добавить null-контракт.");
            return;
        }
        contracts.add(contract);
        System.out.println("Контракт добавлен: " + contract.getId());
        log.info("Контракт добавлен: {}", contract.getId());
    }
    public void removeContract(String contractId){
        if (contractId == null || contractId.trim().isEmpty()){
            System.out.println("Попытка удалить контракт с пустым ID.");
            log.warn("Попытка удалить контракт с пустым ID.");
            return;
        }
        Contract contractToRemove = null;
        for (Contract contract : contracts){
            if (contract.getId().equals(contractId)){
                contractToRemove = contract;
                break;
            }
        }
        if (contractToRemove != null){
            contracts.remove(contractToRemove);
            System.out.println("Контракт удалён: " + contractToRemove.getId());
            log.info("Контракт удалён: {}", contractToRemove.getId());
        }else {
            System.out.println("Контракт с ID " + contractId + " не найден.");
            log.info("Контракт с ID {} не найден.", contractId);
        }
    }
    public void findAndPrintContract(String contractId){
        if (contractId == null || contractId.trim().isEmpty()){
            System.out.println("Попытка найти контракт с пустым ID.");
            log.warn("Попытка найти контракт с пустым ID.");
            return;
        }
        for (Contract contract : contracts){
            if (contract.getId().equals(contractId)){
                System.out.println("Контракт найден: " + contract);
                log.info("Контракт найден: {}", contract);
                return;
            }
        }
        System.out.println("Контракт с ID " + contractId + "не найден.");
        log.warn("Контракт с ID {} не найден.", contractId);
    }
}
