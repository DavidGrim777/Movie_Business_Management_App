package movie.business.app.repository;

import lombok.extern.slf4j.Slf4j;
import movie.business.app.enums.FinanceType;
import movie.business.app.model.FinanceRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class FinanceRepository {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Сохраняет список финансовых записей в CSV-файл.
    public void saveRecords(List<FinanceRecord> records, boolean testMode) {
        if (!testMode) {
            String fileNameCSV = "finance_records.csv";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameCSV))) {
                // Заголовок CSV (5 колонок)
                writer.write("ID, Тип, Сумма, Премьера, Описание, Дата");
                writer.newLine();
                // Для каждой записи пишем данные.
                for (FinanceRecord record : records) {
                    String line = String.join(", ",
                            record.getId(),
                            record.getType().name(),
                            String.format(Locale.US, "%.2f", record.getAmount()), // Формат суммы с точкой
                            record.getDescription(),
                            record.getDate().format(formatter));
                    writer.write(line);
                    writer.newLine();
                }
                log.info("Финансовые записи успешно сохранены в файл {}", fileNameCSV);
            } catch (IOException e) {
                log.error("Ошибка при сохранении финансовых записей в файл: {}", e.getMessage());
            }
        } else {
            String fileNameCSV = "test_finance_records.csv";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameCSV))) {
                // Заголовок CSV (5 колонок)
                writer.write("ID, Тип, Сумма, Премьера, Описание, Дата");
                writer.newLine();
                // Для каждой записи пишем данные.
                for (FinanceRecord record : records) {
                    String line = String.join(", ",
                            record.getId(),
                            record.getType().name(),
                            String.format(Locale.US, "%.2f", record.getAmount()), // Формат суммы с точкой
                            record.getDescription(),
                            record.getDate().format(formatter));
                    writer.write(line);
                    writer.newLine();
                }
                log.info("Финансовые записи успешно сохранены в тестовый файл {}", fileNameCSV);
            } catch (IOException e) {
                log.error("Ошибка при сохранении финансовых записей в тестовый файл: {}", e.getMessage());
            }
        }
    }

    // Загружает финансовые записи из CSV-файла. Строки, не соответствующие формату, пропускаются.
    public List<FinanceRecord> loadRecords(boolean testMode) {
        List<FinanceRecord> records = new ArrayList<>();

        if (!testMode) {
            String fileName = "finance_records.csv";
            Path filePath = Paths.get(fileName);
            if (!Files.exists(filePath)) {
                log.info("Файл {} не найден. Начинаем с пустого списка записей.", fileName);
                System.out.println("Файл не найден. Начинаем с пустого списка записей.");
                return records;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                reader.readLine(); // Пропускаем заголовок
                String line;
                while ((line = reader.readLine()) != null) {
                    // фильтр спасает от ошибок при чтении CSV-данных и элементов отчёта вместе
                    // (например, с System.out.println или writer.write(...) в generateFinanceReport)
                    if (line.trim().isEmpty() || line.startsWith("Премьера:") ||
                            line.startsWith("Поступление") || line.startsWith("Итог:") ||
                            line.startsWith("======================================")) {
                        continue;
                    }
                    String[] data = line.split(", ");
                    if (data.length < 5) { // Ожидаем 5 колонок: ID, Тип, Сумма, Описание, Дата
                        log.warn("Строка не соответствует формату и будет пропущена: {}", line);
                        continue;
                    }
                    // Для простоты здесь премьера просто читается как строка, далее можно расширять
                    // data[0] - название премьеры (не используется в FinanceRecord)
                    String id = data[0];
                    String typeString = data[1];// Тип (INCOME, EXPENSE и т.д.)
                    double amount;
                    try {
                        amount = Double.parseDouble(data[2]);
                    } catch (NumberFormatException e) {
                        log.warn("Невозможно преобразовать сумму '{}'. Строка будет пропущена.", data[2]);
                        continue;
                    }

                    String description = data[3]; // Описание транзакции
                    LocalDate date;
                    try {
                        date = LocalDate.parse(data[4], formatter);
                    } catch (DateTimeParseException e) {
                        log.warn("Неверный формат даты '{}'. Строка будет пропущена.", data[4]);
                        continue;
                    }
                    try {
                        FinanceType type = FinanceType.valueOf(typeString);
                        records.add(new FinanceRecord(id, type, amount, description, date));
                    } catch (IllegalArgumentException e) {
                        log.warn("Неизвестный тип записи '{}'. Запись будет пропущена.", typeString);
                    }
                }
                log.info("Финансовые записи успешно загружены. Количество записей: {}", records.size());
            } catch (IOException e) {
                log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
            }
            return records;

        } else {
            String fileName = "test_finance_records.csv";
            Path filePath = Paths.get(fileName);
            if (!Files.exists(filePath)) {
                log.info("Тестовы Файл {} не найден. Начинаем с пустого списка записей.", fileName);
                System.out.println("Файл не найден. Начинаем с пустого списка записей.");
                return records;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                reader.readLine(); // Пропускаем заголовок
                String line;
                while ((line = reader.readLine()) != null) {
                    // фильтр спасает от ошибок при чтении CSV-данных и элементов отчёта вместе
                    // (например, с System.out.println или writer.write(...) в generateFinanceReport)
                    if (line.trim().isEmpty() || line.startsWith("Премьера:") ||
                            line.startsWith("Поступление") || line.startsWith("Итог:") ||
                            line.startsWith("======================================")) {
                        continue;
                    }
                    String[] data = line.split(", ");
                    if (data.length < 5) { // Ожидаем 5 колонок: ID, Тип, Сумма, Описание, Дата
                        log.warn("Строка не соответствует формату в тесте и будет пропущена: {}", line);
                        continue;
                    }
                    // Для простоты здесь премьера просто читается как строка, далее можно расширять
                    // data[0] - название премьеры (не используется в FinanceRecord)
                    String id = data[0];
                    String typeString = data[1];// Тип (INCOME, EXPENSE и т.д.)
                    double amount;
                    try {
                        amount = Double.parseDouble(data[2]);
                    } catch (NumberFormatException e) {
                        log.warn("Невозможно преобразовать сумму в тесте'{}'. Строка будет пропущена.", data[2]);
                        continue;
                    }

                    String description = data[3]; // Описание транзакции
                    LocalDate date;
                    try {
                        date = LocalDate.parse(data[4], formatter);
                    } catch (DateTimeParseException e) {
                        log.warn("Неверный формат даты в тесте'{}'. Строка будет пропущена.", data[4]);
                        continue;
                    }
                    try {
                        FinanceType type = FinanceType.valueOf(typeString);
                        records.add(new FinanceRecord(id, type, amount, description, date));
                    } catch (IllegalArgumentException e) {
                        log.warn("Неизвестный тип записи в тесте'{}'. Запись будет пропущена.", typeString);
                    }
                }
                log.info("Финансовые записи успешно загружены в тест. Количество записей: {}", records.size());
            } catch (IOException e) {
                log.error("Ошибка чтения тестового файла {}: {}", fileName, e.getMessage());
            }
            return records;
        }
    }

    // Генерация PDF-отчёта с использованием Apache PDFBox.
    public void generatePDFReport(List<FinanceRecord> records) {
        String pdfFileName = "finance_report.pdf";
        try (org.apache.pdfbox.pdmodel.PDDocument document = new org.apache.pdfbox.pdmodel.PDDocument()) {
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
            document.addPage(page);

            java.io.File fontFile = new java.io.File("/System/Library/Fonts/Supplemental/Arial.ttf");
            org.apache.pdfbox.pdmodel.font.PDType0Font font = org.apache.pdfbox.pdmodel.font.PDType0Font.load(document, fontFile);

            org.apache.pdfbox.pdmodel.PDPageContentStream contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page);

            float yPosition = 750;
            float margin = 50;
            float rowHeight = 16;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float[] columnWidths = {70, 70, 70, 220, 100};
            String[] headers = {"ID", "Тип", "Сумма", "Описание", "Дата"};

            // Заголовок отчёта
            contentStream.setFont(font, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Детализированный финансовый отчёт");
            contentStream.endText();

            yPosition -= 25;

            // Рисуем заголовки таблицы
            float nextX = margin;
            contentStream.setFont(font, 11);
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(nextX + 2, yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
                nextX += columnWidths[i];
            }

            // Горизонтальная линия под заголовком
            contentStream.moveTo(margin, yPosition - 2);
            contentStream.lineTo(margin + tableWidth, yPosition - 2);
            contentStream.stroke();

            yPosition -= rowHeight;

            contentStream.setFont(font, 10);
            for (FinanceRecord record : records) {
                if (yPosition < 50) {
                    contentStream.close();
                    page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page);
                    yPosition = 750;
                }
                String[] data = {
                        record.getId(),
                        record.getType().toString(),
                        String.format("%.2f", record.getAmount()),
                        record.getDescription(),
                        record.getDate().toString()
                };
                nextX = margin;
                for (int i = 0; i < data.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX + 2, yPosition);
                    contentStream.showText(data[i].length() > 35 ? data[i].substring(0, 35) + "..." : data[i]);
                    contentStream.endText();
                    nextX += columnWidths[i];
                }

                // Горизонтальная линия
                contentStream.moveTo(margin, yPosition - 2);
                contentStream.lineTo(margin + tableWidth, yPosition - 2);
                contentStream.stroke();

                yPosition -= rowHeight;
            }

            yPosition -= 10;
            double totalIncome = records.stream().filter(r -> r.getType() == FinanceType.INCOME).mapToDouble(FinanceRecord::getAmount).sum();
            double totalExpense = records.stream().filter(r -> r.getType() == FinanceType.EXPENSE).mapToDouble(FinanceRecord::getAmount).sum();
            double overallTotal = totalIncome - totalExpense;

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(String.format("Общий доход: %.2f, Расходы: %.2f, Итог: %.2f", totalIncome, totalExpense, overallTotal));
            contentStream.endText();

            contentStream.close();
            document.save(pdfFileName);
            log.info("PDF-отчёт успешно сгенерирован: {}", pdfFileName);
            System.out.println("PDF-отчёт успешно сгенерирован: " + pdfFileName);

        } catch (IOException e) {
            log.error("Ошибка при генерации PDF-отчёта: {}", e.getMessage());
        }
    }
}