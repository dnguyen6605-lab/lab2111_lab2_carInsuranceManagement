package Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import model.Car;
import model.Insurance;
import Repository.InsuranceRepository;
import Repository.CarRepository;

public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final CarRepository carRepo;

    public InsuranceService(InsuranceRepository insuranceRepository, CarRepository carRepo) {
        this.insuranceRepository = insuranceRepository;
        this.carRepo = carRepo;
    }

    public boolean checkExist(String id) {
        return insuranceRepository.checkExist(id);
    }

    public boolean checkCarExist(String plate) {
        return carRepo.checkExist(plate);
    }
    // phải private để tránh bị thay đổi công thức tính fee
    private long calculateFee(long carValue, int period) {
        if (carValue < 0) {
            throw new IllegalArgumentException("carValue must not be negative");
        }
        if (period == 12) {
            return carValue * 25 / 100;
        } else if (period == 24) {
            return carValue * 40 / 100;
        } else if (period == 36) {
            return carValue * 45 / 100;
        }
        throw new IllegalArgumentException("Unsupported insurance period: " + period);
    }

    public boolean addInsurance(Insurance insurance) {
        if (insurance == null) {
            throw new IllegalArgumentException("insurance must not be null");
        }
        Car car = carRepo.get(insurance.getLicensePlate());
        if (car == null || insuranceRepository.checkExist(insurance.getInsuranceId())) {
            return false;
        }
        insurance.setFee(calculateFee(car.getValue(), insurance.getInsurancePeriod()));
        return insuranceRepository.addInsurance(insurance);
    }

    public List<Insurance> getByYear(int year, int field, boolean asc) {
        List<Insurance> list = new ArrayList<Insurance>();
        for (Insurance insurance : insuranceRepository.getAll()) {
            if (insurance.getEstablishedDate() != null
                    && insurance.getEstablishedDate().getYear() == year) {
                list.add(insurance);
            }
        }
        Comparator<Insurance> cmp = comparatorForField(field);
        if (!asc) {
            cmp = cmp.reversed();
        }
        list.sort(cmp);
        return list;
    }

    private Comparator<Insurance> comparatorForField(int field) {
        if (field == 1) {
            return Comparator.comparing(Insurance::getInsuranceId);
        } else if (field == 2) {
            return Comparator.comparing(Insurance::getEstablishedDate);
        } else if (field == 3) {
            return Comparator.comparing(Insurance::getLicensePlate);
        } else if (field == 4) {
            return Comparator.comparingInt(Insurance::getInsurancePeriod);
        }
        throw new IllegalArgumentException("Unsupported sort field: " + field);
    }

    public Collection<Insurance> getAllInsurances() {
        return insuranceRepository.getAll();
    }

    public void save() throws IOException {
        insuranceRepository.saveToFile();
    }

    public boolean load() throws IOException, ClassNotFoundException {
        return insuranceRepository.loadFromFile();
    }
}
