package movie.business.app.repository;
import movie.business.app.model.FinanceRecord;

import java.util.List;

public interface FinanceRepository {
    void saveRecords(List<FinanceRecord> records, boolean testMode);
    List<FinanceRecord> loadRecords(boolean testMode);
    void generatePDFReport(List<FinanceRecord> records);
}
