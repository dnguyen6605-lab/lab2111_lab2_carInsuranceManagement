package Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Car;
import Utils.FileManager;

public class CarRepository {
    private final Map<String, Car> cars = new HashMap<String, Car>();
    private final String filePath = "carInfo.dat";
    private final FileManager fileManager;

    public CarRepository(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public boolean addCar(Car car) {
        if (car == null || car.getLisencePlate()== null) {
            throw new IllegalArgumentException("car and license plate must not be null");
        }
        String key = normalizePlate(car.getLisencePlate());
        return cars.putIfAbsent(key, car) == null;
    }

    public boolean updateCar(Car car) {
        if (car == null || car.getLisencePlate() == null) {
            return false;
        }
        String key = normalizePlate(car.getLisencePlate());
        return cars.replace(key, car) != null;
    }

    public Car get(String plate) {
        return plate == null ? null : cars.get(normalizePlate(plate));
    }

    public boolean checkExist(String plate) {
        return plate != null && cars.containsKey(normalizePlate(plate));
    }

    public boolean remove(String plate) {
        return cars.remove(normalizePlate(plate)) != null;
    }

    public Collection<Car> getAll() {
        return new ArrayList<Car>(cars.values());
    }

    public void saveToFile() throws IOException {
        fileManager.save(filePath, cars);
    }

    public boolean loadFromFile() throws IOException, ClassNotFoundException {
        Map<?, ?> rawMap = fileManager.load(filePath);
        if (rawMap == null) {
            return false;
        }
        Map<String, Car> loaded = new HashMap<String, Car>();
        for (Object value : rawMap.values()) {
            if (!(value instanceof Car)) {
                throw new IOException("Invalid car record in " + filePath);
            }
            Car car = (Car) value;
            if (car.getLisencePlate() == null) {
                throw new IOException("Car record has no license plate in " + filePath);
            }
            loaded.put(normalizePlate(car.getLisencePlate()), car);
        }
        cars.clear();
        cars.putAll(loaded);
        return true;
    }

    private static String normalizePlate(String plate) {
        return plate == null ? null : plate.trim().toUpperCase();
    }
}
