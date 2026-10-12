package Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Insurance;
import Service.InsuranceService;
import Utils.Inputter;
import Utils.Menu;
import model.Car;

public class InsuranceController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern(Inputter.DATE_PATTERN);

    private final InsuranceService insuranceService;
    private final Inputter inputter;

    public InsuranceController(InsuranceService insuranceService, Inputter inputter) {
        this.insuranceService = insuranceService;
        this.inputter = inputter;
    }
 
    public void addInsurance() {
        while (true) {
            String id = readUniqueInsuranceId();
            LocalDate date = inputter.getDate(
                    "Established date (" + Inputter.DATE_PATTERN + "): ", Inputter.DATE_PATTERN);
            String plate = readRegisteredPlate();
            String customerName = inputter.getNonEmptyString("Customer name: ");
            int period = inputter.getOneOf("Insurance period (12, 24 or 36 months): ",
                    12, 24, 36);
            Insurance insurance = new Insurance(id, date, plate, customerName, period, 0);
            if (insuranceService.addInsurance(insurance)) {
                System.out.printf("Insurance added successfully. Fee: $ %,d%n",
                        insurance.getFee());
            } else {
                System.out.println("Unable to add insurance. "
                        + " Verify that the car exists and the ID is unique.");
            }
            if (!inputter.getYesNo("Add another insurance statement?")) {
                break;
            }
        }
    }
    public void listInsurances() {
        int year = inputter.getAnInteger("Year: ", 1, 9999);
        Menu insuranceMenu = new Menu("Sort insurance statements by:",inputter);
        insuranceMenu.addNewOption("Insurance ID");
        insuranceMenu.addNewOption("Established date");
        insuranceMenu.addNewOption("License plate");
        insuranceMenu.addNewOption("Insurance period");
        insuranceMenu.show();
        int field = insuranceMenu.getchoice();
        boolean asc = chooseAscending();
        List<Insurance> list = insuranceService.getByYear(year, field, asc);
        printInsuranceReport(year, field, asc, list);
    }

    public void saveData() {
        try {
            insuranceService.save();
            System.out.println("Insurance data saved successfully.");
        } catch (IOException ex) {
            System.out.println("Save failed: " + ex.getMessage());
        }
    }

    public void loadData() {
        try {
            boolean loaded = insuranceService.load();
            if (!loaded) {
                System.out.println("No saved insurance data found.");
            } else {
                System.out.println("Insurance data loaded successfully.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Load failed: " + ex.getMessage());
        }
    }

    private String readUniqueInsuranceId() {
        while (true) {
            String id = inputter.getNonEmptyString("Insurance ID: ");
            if (!insuranceService.checkExist(id)) {
                return id;
            }
            System.out.println("That insurance ID already exists.");
        }
    }

    private String readRegisteredPlate() {
        while (true) {
            String plate = inputter.getNonEmptyString("License plate: ")
                    .toUpperCase();
            if (insuranceService.checkCarExist(plate)) {
                return plate;
            }
            System.out.println("Unregistered vehicle. Enter a registered license plate.");
        }
    }

    private void printInsuranceReport(int year, int field, boolean asc,
            List<Insurance> insurances) {
        System.out.println("\nReport: INSURANCE STATEMENTS");
        System.out.printf("From: 01/01/%04d To: 12/31/%04d%n", year, year);
        System.out.println("Sorted by: " + insuranceFieldLabel(field));
        System.out.println("Sort type: " + (asc ? "ASC" : "DESC"));
        if (insurances.isEmpty()) {
            System.out.println("\nNo insurance statements in " + year);
            return;
        }
        System.out.printf("%-4s | %-13s | %-16s | %-14s | %-20s | %-16s | %s%n",
                "No.", "Insurance Id", "Established Date", "License plate",
                "Customer", "Insurance period", "Insurance fees");
        int number = 1;
        for (Insurance insurance : insurances) {
            System.out.printf("%-4d | %-13s | %-16s | %-14s | %-20s | %-16d | $ %,d%n",
                    number++, insurance.getInsuranceId(),
                    insurance.getEstablishedDate().format(DATE_FORMAT),
                    insurance.getLicensePlate(), insurance.getCustomerName(),
                    insurance.getInsurancePeriod(), insurance.getFee());
        }
    }

    private static String insuranceFieldLabel(int field) {
        switch (field) {
            case 1: return "Insurance ID";
            case 2: return "Established Date";
            case 3: return "License plate";
            default: return "Insurance period";
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
