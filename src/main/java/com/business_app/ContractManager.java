package com.business_app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ContractManager {

    // Список контрактов
    private List<Contract> contracts;

    // Инициализация списка контрактов
    public ContractManager() {
        this.contracts = new ArrayList<>();
    }

    // Метод для добавления нового контракта
    public void addContract(Contract contract) {
        if (contract == null) {
            log.warn("Попытка добавить пустым контракт.");
            return;
        }
        contracts.add(contract);
        System.out.println("Контракт добавлен: " + contract.getId());
    }

    // Метод для удаления контракта по ID
    public void removeContract(String contractId) {
        if (contractId == null || contractId.trim().isEmpty()) {
            log.warn("Попытка удалить контракт с пустым ID.");
            return;
        }
        boolean remoed = contracts.removeIf(contract -> contract.getId().equals(contractId));
        if (remoed) {
            System.out.println("Контракт удалён: " + contractId);
        } else {
            System.out.println("Контракт с ID " + contractId + " не найден.");
            log.error("Контракт с ID {} не найден.", contractId);
        }
    }

    // Метод для поиска контракта по ID и его вывода в консоль
    public void findAndPrintContract(String contractId) {
        if (contractId == null || contractId.trim().isEmpty()) {
            log.warn("Попытка найти контракт с пустым ID.");
            return;
        }
        for (Contract contract : contracts) {
            if (contract.getId().equals(contractId)) {
                System.out.println("Контракт найден: " + contract);
                return;
            }
        }
        log.warn("Контракт с ID {} не найден.", contractId);
    }

    // Метод для вывода всех контрактов
    public void printAllContracts() {
        if (contracts.isEmpty()) {
            log.error("Список контрактов пуст.");
        } else {
            for (Contract contract : contracts) {
                System.out.println(contract);
            }
        }
    }
}
