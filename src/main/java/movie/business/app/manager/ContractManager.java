package movie.business.app.manager;

import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.ContractStatus;
import movie.business.app.model.Contract;
import movie.business.app.repository.ContractRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


@Slf4j
public class ContractManager {

    private final ContractRepository contractRepository;
    // Инициализация списка контрактов
    public ContractManager(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    // Метод для добавления нового контракта
    public void addContract(Contract contract) {
        if (contract == null) {
            log.warn("Попытка добавить пустым контракт.");
            return;
        }
        contractRepository.addContract(contract);
        System.out.println("Контракт добавлен: " + contract.getId());
        log.info("Контракт добавлен: {}", contract.getId());
    }

    // Метод для удаления контракта по ID
    public void removeContract(String contractId) {
        if (contractId == null || contractId.trim().isEmpty()) {
            log.warn("Попытка удалить контракт с пустым ID.");
            return;
        }
        Optional<Contract> found = contractRepository.findById(contractId);
        if (found.isPresent()) {
            contractRepository.deleteById(contractId);
            System.out.println("Контракт удалён: " + contractId);
            log.info("Контракт удалён: {}", contractId);
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
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        if (optionalContract.isPresent()) {
            Contract contract = optionalContract.get();
            System.out.println("Контракт найден: " + contract);
        } else {
        System.out.println("Контракт с ID " + contractId + " не найден.");
        log.warn("Контракт с ID {} не найден.", contractId);
    }
    }

    // Метод для вывода всех контрактов
    public void printAllContracts() {
        List<Contract> contracts = contractRepository.findAll();
        if (contracts.isEmpty()) {
            System.out.println("Список контрактов пуст.");
            log.error("Список контрактов пуст.");
        } else {
            for (Contract contract : contracts) {
                System.out.println(contract);
            }
        }
    }

    public void updateStatus(Contract contract, ContractStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Неверный статус. Обновление не выполнено.");
            log.warn("Неверный статус. Обновление не выполнено.");
            return;
        }

        if (contract.getStatus() == newStatus) {
            System.out.println("Контракт уже имеет статус: " + newStatus);
            log.info("Контракт уже имеет статус: {}", newStatus);
            return;
        }

        contract.setStatus(newStatus);
        System.out.println("Статус контракта обновлён на: " + newStatus);
        log.info("Статус контракта обновлён на: {}", newStatus);
    }

    // Метод для обновления статусов всех контрактов
    public void updateAllContractStatuses() {
        for (Contract contract : contractRepository.findAll()) {
            if (contract.getEndDate().isBefore(LocalDate.now())) {
                if (contract.getStatus() != ContractStatus.COMPLETED) {
                    contract.setStatus(ContractStatus.COMPLETED);
                    System.out.println("Статус контракта с ID " + contract.getId() + " обновлён на COMPLETED");
                    log.info("Статус контракта с ID {} обновлён на COMPLETED", contract.getId());
                }
            } else if (contract.getStatus() != ContractStatus.ACTIVE) {
                contract.setStatus(ContractStatus.ACTIVE);
                System.out.println("Статус контракта с ID " + contract.getId() + " обновлён на ACTIVE");
                log.info("Статус контракта с ID {} обновлён на ACTIVE", contract.getId());
            }
        }
    }

    public void createContract(Scanner scanner) {
        String contractId = UUID.randomUUID().toString().substring(0, 16);

        String personName;
        do {
            System.out.print("Введите имя: ");
            personName = scanner.nextLine().trim();
            if (personName.isEmpty()) {
                System.out.println("Ошибка: имя не может быть пустым.");
            }
        } while (personName.isEmpty());

        String role;
        do {
            System.out.print("Введите роль: ");
            role = scanner.nextLine().trim();
            if (role.isEmpty()) {
                System.out.println("Ошибка: роль не может быть пустой.");
            }
        } while (role.isEmpty());

        LocalDate startDate = null;
        while (startDate == null) {
            System.out.print("Введите дату начала (yyyy-MM-dd): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException exception) {
                System.out.println("Ошибка: неверный формат даты.");
            }
        }

        LocalDate endDate = null;
        while (endDate == null) {
            System.out.print("Введите дату окончания (yyyy-MM-dd): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine().trim());
                if (endDate.isBefore(startDate)) {
                    System.out.println("Ошибка: дата окончания раньше начала.");
                    endDate = null;
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Ошибка: неверный формат даты.");
            }
        }

        double salary = -1;
        while (salary < 0) {
            System.out.print("Введите гонорар: ");
            try {
                salary = scanner.nextDouble();
                if (salary < 0) {
                    System.out.println("Ошибка: гонорар не может быть отрицательным.");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Ошибка: введите числовое значение.");
                scanner.nextLine(); // очистка буфера
            }
            scanner.nextLine(); // после nextDouble
        }

        ContractStatus contractStatus = null;
        while (contractStatus == null) {
            System.out.println("Выберите статус контракта:");
            for (ContractStatus s : ContractStatus.values()) {
                System.out.println("- " + s.name());
            }
            System.out.print("Введите статус: ");
            String inputStatus = scanner.nextLine().trim().toUpperCase();
            try {
                contractStatus = ContractStatus.valueOf(inputStatus);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: неверный статус.");
            }
        }

        Contract contract = new Contract(contractId, personName, role, startDate, endDate, salary, contractStatus);
        addContract(contract);
        System.out.println("Контракт добавлен: " + personName + " (" + role + "), статус: " + contractStatus);
    }
}

