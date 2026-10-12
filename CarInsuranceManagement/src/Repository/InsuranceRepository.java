package Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Insurance;
import Utils.FileManager;

public class InsuranceRepository {
    private final Map<String, Insurance> insurances = new HashMap<String, Insurance>();
    private final String filePath = "insurances.dat";
    private final FileManager fileManager;
    
    public InsuranceRepository(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public boolean addInsurance(Insurance insurance) {
        if (insurance == null || insurance.getInsuranceId() == null) {
            throw new IllegalArgumentException("insurance and id must not be null");
        }
        String key = insurance.getInsuranceId().trim();
        return insurances.putIfAbsent(key, insurance) == null;
    }

    public Insurance get(String id) {
        return id == null ? null : insurances.get(id.trim());
    }

    public boolean checkExist(String id) {
        return id != null && insurances.containsKey(id.trim());
    }

    public boolean existsByPlate(String plate) {
        if (plate == null) {
            return false;
        }
        String key = normalizePlate(plate);
        for (Insurance insurance : insurances.values()) {
            if (key.equals(normalizePlate(insurance.getLicensePlate()))) {
                return true;
            }
        }
        return false;
    }

    public Collection<Insurance> getAll() {
        return new ArrayList<Insurance>(insurances.values());
    }

    public void saveToFile() throws IOException {
        fileManager.save(filePath, insurances);
    }

    public boolean loadFromFile() throws IOException, ClassNotFoundException {
        Map<?, ?> rawMap = fileManager.load(filePath);
        if (rawMap == null) {
            return false;
        }
        Map<String, Insurance> loaded = new HashMap<String, Insurance>();
        for (Object value : rawMap.values()) {
            if (!(value instanceof Insurance)) {
                throw new IOException("Invalid insurance record in " + filePath);
            }
            Insurance insurance = (Insurance) value;
            if (insurance.getInsuranceId() == null) {
                throw new IOException("Insurance record has no id in " + filePath);
            }
            loaded.put(insurance.getInsuranceId().trim(), insurance);
        }
        insurances.clear();
        insurances.putAll(loaded);
        return true;
    }

    private static String normalizePlate(String plate) {
        return plate == null ? null : plate.trim().toUpperCase();
    }
}
