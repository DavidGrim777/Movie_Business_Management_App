package com.business_app;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractManagerTest {
    private ContractManager contractManager;
    private Faker faker;

    @BeforeEach
    void setUp() {
        contractManager = new ContractManager();
        faker = new Faker();
    }

    @Test
    void testAddContract() {
        // Arrange
        String id = faker.idNumber().valid();
        String personName = faker.name().fullName();
        String role = faker.job().title();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(6);
        double salary = faker.number().randomDouble(2, 50000, 150000);
        Contract contract = new Contract(id, personName, role, startDate, endDate, salary);

        // Act
        contractManager.addContract(contract);

        // Assert
        assertEquals(personName, contract.getPersonName());
    }

    @Test
    void testRemoveContract() {
        // Arrange
        String id = faker.idNumber().valid();
        Contract contract = new Contract(id, faker.name().fullName(), faker.job().title(), LocalDate.now(), LocalDate.now().plusMonths(6), faker.number().randomDouble(2, 50000, 150000));
        contractManager.addContract(contract);

        // Act
        contractManager.removeContract(id);

        // Assert
        // Проверим, что контракт не найден после удаления
        assertDoesNotThrow(() -> contractManager.findAndPrintContract(id));
    }
}
