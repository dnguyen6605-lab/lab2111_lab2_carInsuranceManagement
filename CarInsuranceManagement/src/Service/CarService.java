package Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import model.Car;
import Repository.CarRepository;
import Repository.InsuranceRepository;

public class CarService {
    private final CarRepository carRepository;
    private final InsuranceRepository insuranceRepository;

    public CarService(CarRepository carRepository, InsuranceRepository insuranceRepository) {
        this.carRepository = carRepository;
        this.insuranceRepository = insuranceRepository;
    }

    public boolean checkExist(String plate) {
        return carRepository.checkExist(plate);
    }

    public boolean addCar(Car car) {
        return carRepository.addCar(car);
    }

    public Car findCar(String plate) {
        return carRepository.get(plate);
    }

    public boolean updateCar(Car car) {
        if (car == null) {
            return false;
        }
        return carRepository.updateCar(car);
    }
//xóa xe vì có 3 kết quả nên sd enum thay vì boolean
    public DeleteResult deleteCar(String plate) {
        if (plate == null) {
            return DeleteResult.NOT_FOUND;
        }
        if (insuranceRepository.existsByPlate(plate)) {
            return DeleteResult.INSURED;
        }
        return carRepository.remove(plate) ? DeleteResult.SUCCESS : DeleteResult.NOT_FOUND;
    }
//so sánh theo field
    public List<Car> getUninsuredCars(int field, boolean asc) {
        List<Car> list = new ArrayList<Car>();
        for (Car car : carRepository.getAll()) {
            if (!insuranceRepository.existsByPlate(car.getLisencePlate())) {
                list.add(car);
            }
        }
        Comparator<Car> cmp = comparatorForField(field);
        if (!asc) {
            cmp = cmp.reversed();
        }
        list.sort(cmp);
        return list;
    }
// method comparator tạo field để so sánh car vì kh dc xử lí trong switch case

    private Comparator<Car> comparatorForField(int field) {
        if (field == 1) {
            return Comparator.comparing(Car::getLisencePlate);
        } else if (field == 2) {
            return Comparator.comparing(Car::getOwner, String.CASE_INSENSITIVE_ORDER);
        } else if (field == 3) {
            return Comparator.comparing(Car::getRegistrationDate);
        } else if (field == 4) {
            return Comparator.comparingInt(Car::getVehicleType);
        }
        throw new IllegalArgumentException("Unsupported sort field: " + field);
    }

    public Collection<Car> getAllCars() {
        return carRepository.getAll();
    }

    public void save() throws IOException {
        carRepository.saveToFile();
    }

    public boolean load() throws IOException, ClassNotFoundException {
        return carRepository.loadFromFile();
    }
}
