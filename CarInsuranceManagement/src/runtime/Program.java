package runtime;

import Controller.CarController;
import Controller.InsuranceController;
import java.util.Scanner;
import Repository.CarRepository;
import Repository.InsuranceRepository;
import Service.CarService;
import Service.InsuranceService;
import Utils.FileManager;
import Utils.Inputter;
import Utils.Menu;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inputter inputter = new Inputter(sc);
        FileManager fileManager = new FileManager();

        // Repository
        CarRepository carRepo = new CarRepository(fileManager);
        InsuranceRepository insRepo = new InsuranceRepository(fileManager);

        // Service
        CarService carService = new CarService(carRepo, insRepo);
        InsuranceService insService = new InsuranceService(insRepo, carRepo);

        // Controller
        CarController carController = new CarController(carService, inputter);
        InsuranceController insColtroller = new InsuranceController(insService, inputter);

        // Menu
        Menu menu = new Menu("CAR INSURANCE MANAGEMENT SYSTEM",inputter);
        menu.addNewOption("Add car information");
        menu.addNewOption("Find a car");
        menu.addNewOption("Update a car");
        menu.addNewOption("Delete a car");
        menu.addNewOption("Add an insurance statement");
        menu.addNewOption("List of insurance statements");
        menu.addNewOption("Report on uninsured cars");
        menu.addNewOption("Save data");
        menu.addNewOption("Load data");
        menu.addNewOption("Quit");
        
        while (true) {
            menu.show();
            switch (menu.getchoice()) {
                case 1:
                    carController.addCar();
                    break;
                case 2:
                    carController.findCar();
                    break;
                case 3:
                    carController.updateCar();
                    break;
                case 4:
                    carController.deleteCar();
                    break;
                case 5:
                    insColtroller.addInsurance();
                    break;
                case 6:
                    insColtroller.listInsurances();
                    break;
                case 7:
                    carController.reportUninsured();
                    break;
                case 8:
                    carController.saveData();
                    insColtroller.saveData();
                    break;
                case 9:
                    carController.loadData();
                    insColtroller.loadData();
                    break;
                case 10:
                    if (inputter.getYesNo("Are you sure you want to quit?")) {
                        carController.saveData();
                        insColtroller.saveData();
                        System.out.println("Goodbye!");
                        return;
                    }
                    break;
                    
            }
        }
    }
}
// quăng choose accesding và menu chọn field trong controller qua inputter