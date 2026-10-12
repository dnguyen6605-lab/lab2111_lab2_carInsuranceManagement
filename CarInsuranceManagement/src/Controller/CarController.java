package Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Car;
import Service.CarService;
import Service.DeleteResult;
import Utils.Inputter;
import Utils.Menu;

public class CarController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern(Inputter.DATE_PATTERN);

    private final CarService carService;
    private final Inputter inputter;

    public CarController(CarService carService, Inputter inputter) {
        this.carService = carService;
        this.inputter = inputter;
    }

    public void addCar() {
        while (true) {
            String plate = readUniquePlate();
            String owner = inputter.getString("Owner (2-35 characters): ", 2, 35);
            String brand = inputter.getNonEmptyString("Brand: ");
            long value = inputter.getLong("Vehicle value (> 999): ", 1000);
            LocalDate date = inputter.getDate(
                "Registration date (" + Inputter.DATE_PATTERN + "): ", Inputter.DATE_PATTERN);
            String place = inputter.getNonEmptyString("Registration place: ");
            int type = inputter.getOneOf("Vehicle type (5, 7 or 9 seats): ", 5, 7, 9);
            Car car = new Car(plate, owner, brand, value, date, place, type);
            carService.addCar(car);
            System.out.println("Car added successfully.");
            if (!inputter.getYesNo("Add another car?")) {
                break;
            }
        }
    }

    public void findCar() {
        String plate = inputter.getNonEmptyString("License plate: ")
                .toUpperCase();
        Car car = carService.findCar(plate);
        if (car == null) {
            System.out.println("Unregistered vehicle");
        } else {
            printCar(car);
        }
    }

    public void updateCar() {
        String plate = inputter.getNonEmptyString("License plate: ")
                .toUpperCase();
        Car current = carService.findCar(plate);
        if (current == null) {
            System.out.println("Unregistered vehicle");
            return;
        }
        System.out.println("Press Enter to keep each current value.");
        String owner = inputter.getOptionalString("Owner", current.getOwner(), 2, 35);
        String brand = inputter.getOptionalNonEmptyString("Brand", current.getBrand());
        long value = inputter.getOptionalLong("Vehicle value", current.getValue(), 1000);
        LocalDate registrationDate = inputter.getOptionalDate(
                "Registration date (" + Inputter.DATE_PATTERN + ")",
                current.getRegistrationDate(), Inputter.DATE_PATTERN);
        String registrationPlace = inputter.getOptionalNonEmptyString(
                "Registration place", current.getRegistrationPlace());
        int vehicleType = inputter.getOptionalOneOf(
                "Vehicle type (5, 7 or 9)", current.getVehicleType(), 5, 7, 9);
        Car updated = new Car(current.getLisencePlate(), owner, brand, value,
                registrationDate, registrationPlace, vehicleType);
        System.out.println(carService.updateCar(updated)
                ? "Update successful" : "Update failed");
    }

    public void deleteCar() {
        String plate = inputter.getNonEmptyString("License plate: ")
                .toUpperCase();
        Car car = carService.findCar(plate);
        if (car == null) {
            System.out.println("Unregistered vehicle");
            return;
        }
        printCar(car);
        if (!inputter.getYesNo("Delete this car?")) {
            System.out.println("Delete cancelled.");
            return;
        }
        DeleteResult result = carService.deleteCar(plate);
        switch (result) {
            case SUCCESS:
                System.out.println("Delete successful");
                break;
            case NOT_FOUND:
                System.out.println("Unregistered vehicle");
                break;
            case INSURED:
                System.out.println("Cannot delete: the car is already insured");
                break;
            default:
                throw new IllegalStateException("Unhandled delete result: " + result);
        }
    }

    public void reportUninsured() {
            Menu uninsuredMenu = new Menu("Sort uninsured cars by:",inputter);
            uninsuredMenu.addNewOption("License plate");
            uninsuredMenu.addNewOption("Owner");
            uninsuredMenu.addNewOption("Registration date");
            uninsuredMenu.addNewOption("Vehicle type");
            uninsuredMenu.show();
            int field = uninsuredMenu.getchoice();
            boolean asc = chooseAscending();
            List<Car> cars = carService.getUninsuredCars(field, asc);
            printUninsuredReport(field, asc, cars);
        }
    public void saveData() {
        try {
            carService.save();
            System.out.println("Car data saved successfully.");
        } catch (IOException ex) {
            System.out.println("Save failed: " + ex.getMessage());
        }
    }

    public void loadData() {
        try {
            boolean loaded = carService.load();
            if (!loaded) {
                System.out.println("No saved car data found.");
            } else {
                System.out.println("Car data loaded successfully.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Load failed: " + ex.getMessage());
        }
    }
    //check dup  + cấm null
    private String readUniquePlate() {
        while (true) {
            String plate = inputter.getNonEmptyString("License plate: ")
                    .toUpperCase();
            if (!carService.checkExist(plate)) {
                return plate;
            }
            System.out.println("That license plate is already registered.");
        }
    }
    // show infor
    private void printCar(Car car) {
        System.out.println(car);
    }
    //Show uninsurance
    private void printUninsuredReport(int field, boolean asc, List<Car> cars) {
        System.out.println("\nReport: UNINSURED CARS");
        System.out.println("Sorted by: " + carFieldLabel(field));
        System.out.println("Sort type: " + (asc ? "ASC" : "DESC"));
        if (cars.isEmpty()) {
            System.out.println("\nNo uninsured cars.");
            return;
        }
        System.out.printf("%-4s | %-14s | %-17s | %-20s | %-15s | %-12s | %s%n",
                "No.", "License plate", "Registration Date", "Vehicle Owner",
                "Brand", "Vehicle type", "Value");
        int number = 1;
        for (Car car : cars) {
            System.out.printf("%-4d | %-14s | %-17s | %-20s | %-15s | %-12d | $ %,d%n",
                    number++, car.getLisencePlate(),
                    car.getRegistrationDate().format(DATE_FORMAT), car.getOwner(),
                    car.getBrand(), car.getVehicleType(), car.getValue());
        }
    }

    private static String carFieldLabel(int field) {
        switch (field) {
            case 1: return "License plate";
            case 2: return "Owner";
            case 3: return "Registration date";
            default: return "Vehicle type";
        }
    }

    private boolean chooseAscending() {
        while (true) {
            String choice = inputter.readLine("Sort type (ASC/DESC): ").trim();
            if ("ASC".equalsIgnoreCase(choice)) {
                return true;
            }
            if ("DESC".equalsIgnoreCase(choice)) {
                return false;
            }
            System.out.println("Enter ASC or DESC.");
        }
    }
}
