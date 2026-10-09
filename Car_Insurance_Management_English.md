# 1. Decomposition

**Objective:** Decompose the system into architectural layers and functional modules to apply the Separation of Concerns principle, where each component is responsible for a distinct group of functions.

## 1.1. Entry Point Layer – Program

- Contains the `main()` method and the `Scanner` object.
- Serves as the program's entry point.
- Initializes and coordinates the necessary system components.

## 1.2. Controller Layer

### CarController

Responsible for receiving user interactions and coordinating car-related functions:

- `addCar()` [1]
- `findCar()` [2]
- `updateCar()` [3]
- `deleteCar()` [4]
- `reportUninsured()` [7]
- `saveData()` [8]
- `loadData()` [9]

`CarController` uses `CarService` to perform the corresponding operations.

### InsuranceController

Responsible for receiving user interactions and coordinating insurance-related functions:

- `addInsurance()` [5]
- `listInsurances()` [6]
- `saveData()` [8]
- `loadData()` [9]

`InsuranceController` uses `InsuranceService` to perform the corresponding operations.

## 1.3. Service Layer

The Service Layer is responsible for handling business logic and coordinating the required repositories.

### CarService

Performs car-related business operations:

- Checks whether a car exists through `checkExist()`.
- Adds a car through `addCar()`.
- Searches for a car through `findCar()`.
- Updates car information through `updateCar()`.
- Deletes a car through `deleteCar()`.
- Checks whether a car is insured before deleting it.
- Retrieves the list of uninsured cars through `getUninsuredCars()`.
- Coordinates data-saving and data-loading operations.

`CarService` uses:

- `CarRepository` to manage car data.
- `InsuranceRepository` to check a car's insurance status.

### InsuranceService

Performs insurance-related business operations:

- Checks whether an insurance ID already exists through `existsId()`.
- Calculates insurance fees through `calculateFee()`.
- Adds an insurance policy through `addInsurance()`.
- Filters and sorts the list of insurance policies by year through `getByYear()`.
- Coordinates data-saving and data-loading operations.

`InsuranceService` uses:

- `InsuranceRepository` to manage insurance data.
- `CarService` to check and retrieve information related to cars.

## 1.4. Data Access and Entity Layer – Repository & Entity Layer

### Repository

`CarRepository` and `InsuranceRepository` are responsible for:

- Managing in-memory data using `Map`.
- Providing operations to retrieve and modify data.
- Saving data to files through `saveToFile()`.
- Reading data from files through `loadFromFile()`.

### Entity

`Car` and `Insurance` represent the system's main data entities.

### Enum

`DeleteResult` represents the result of a car-deletion operation with three states:

- `SUCCESS`
- `NOT_FOUND`
- `INSURED`

# 2. Pattern Recognition

**Objective:** Identify the architectural and design patterns, as well as the data organization approaches used in the system.

## 2.1. Controller–Service–Repository Pattern

The system applies the layered Controller–Service–Repository pattern:

- **Controller:** Responsible for interacting with users and coordinating functions.
- **Service:** Contains business logic and handles business constraints.
- **Repository:** Manages in-memory data and performs file persistence operations.

This separation distinguishes responsibilities among the presentation, business, and data layers while reducing dependencies between layers.

## 2.2. Repository Pattern

`CarRepository` and `InsuranceRepository` act as intermediary layers between the Service Layer and the data-storage mechanism.

Both repositories have a similar structure:

- Store data using `Map`.
- Have a `filePath` to specify the data-storage location.
- Provide the `saveToFile()` and `loadFromFile()` methods.

Using the Repository pattern means that the Service Layer does not need to depend directly on how data is stored.

## 2.3. Result/Status Enum

The system uses `DeleteResult` to represent the result of a car-deletion operation:

- `SUCCESS`
- `NOT_FOUND`
- `INSURED`

Using an enum instead of numeric or string values makes returned results clearer and easier to read, while reducing errors caused by using invalid values.

# 3. Abstraction

**Objective:** Model the necessary attributes and methods of each component based on the problem requirements and UML design.

## 3.1. Entity and Enum

### Car

**Attributes:**

- `licensePlate: String` – the car's identifying key.
- `owner: String` – the owner.
- `brand: String` – the vehicle brand.
- `value: long` – the car's value.
- `registrationDate: LocalDate` – the registration date.
- `registrationPlace: String` – the registration location.
- `vehicleType: int` – the vehicle type; accepts the values 5, 7, or 9.

**Methods:**

- Constructor.
- Getter/Setter.
- `toString()`.

### Insurance

**Attributes:**

- `insuranceId: String` – the identifying key of the insurance policy.
- `establishedDate: LocalDate` – the date the policy was established.
- `licensePlate: String` – the license plate of the insured car.
- `customerName: String` – the customer's name.
- `insurancePeriod: int` – the insurance period.
- `fee: long` – the insurance fee.

**Methods:**

- Constructor.
- Getter/Setter.
- `toString()`.

### DeleteResult

An enum used to represent the result of a car-deletion operation:

- `SUCCESS`
- `NOT_FOUND`
- `INSURED`

## 3.2. Repository

### CarRepository

**Attributes:**

- `cars: Map<String, Car>` – stores `Car` objects, using `licensePlate` as the key.
- `filePath: String` – the path to the storage file.

**Methods:**

- `addCar(): boolean`
- `updateCar()`
- `get(): Car`
- `checkExist(): boolean`
- `remove(): boolean`
- `getAll(): Collection`
- `saveToFile()`
- `loadFromFile()`

### InsuranceRepository

**Attributes:**

- `insurances: Map<String, Insurance>` – stores `Insurance` objects, using `insuranceId` as the key.
- `filePath: String` – the path to the storage file.

**Methods:**

- `addInsurance(): boolean`
- `get(id): Insurance`
- `checkExist(): boolean`
- `existsByPlate(plate): boolean`
- `getAll(): Collection`
- `saveToFile()`
- `loadFromFile()`

## 3.3. Service and Controller

### CarService

**Attributes:**

- `carRepository: CarRepository`
- `insuranceRepository: InsuranceRepository`

**Methods:**

- `checkExist(plate): boolean`
- `addCar(Car): boolean`
- `findCar(plate): Car`
- `updateCar(Car): boolean`
- `deleteCar(plate): DeleteResult`
- `getUninsuredCars(field, asc): List`
- `save()`
- `load()`

### InsuranceService

**Attributes:**

- `insuranceRepository: InsuranceRepository`
- `carService: CarService`

**Methods:**

- `checkExist(id): boolean`
- `calculateFee(carValue, period): long`
- `addInsurance(Insurance): boolean`
- `getByYear(year, field, asc): List`
- `save()`
- `load()`

### CarController

**Attribute:**

- `carService: CarService`

**Functions:**

- `addCar()`
- `findCar()`
- `updateCar()`
- `deleteCar()`
- `reportUninsured()`
- `saveData()`
- `loadData()`

### InsuranceController

**Attribute:**

- `insuranceService: InsuranceService`

**Functions:**

- `addInsurance()`
- `listInsurances()`
- `saveData()`
- `loadData()`

### Program

**Attribute:**

- `sc: Scanner`

**Method:**

- `main()`

# 4. Algorithm Design

## Function 1: Add Car Information

- **Input:** `licensePlate`, `owner`, `brand`, `value`, `registrationDate`, `registrationPlace`, `vehicleType`.
- **Step 1:** Enter `licensePlate` and call `checkExist()`. If it is a duplicate, display an error and ask the user to enter it again.
- **Step 2:** Enter and validate `owner` (2–35 characters), `brand` (not empty), and `value` (> 999).
- **Step 3:** Enter and validate `registrationDate` (`MM/dd/yyyy`), `registrationPlace` (not empty), and `vehicleType` (∈ {5, 7, 9}).
- **Step 4:** Create a `Car` object and call `CarService.addCar()` to save it to `CarRepository`.
- **Step 5:** Display `"Car added successfully."`
- **Step 6:** Ask whether to continue (Y/N). Choose Y to return to Step 1; choose N to return to the menu.

## Function 2: Find a Car

- **Input:** `licensePlate`.
- **Step 1:** Enter and normalize `licensePlate`.
- **Step 2:** Call `CarService.findCar(plate)`.
- **Step 3:** If the method returns `null`, display `"Unregistered vehicle"`.
- **Step 4:** If the car is found, display its detailed information.

## Function 3: Update a Car

- **Input:** `licensePlate` and the new information to be updated.
- **Step 1:** Enter `licensePlate` and call `findCar()`. If the car is not found, display `"Unregistered vehicle"` and stop.
- **Step 2:** Enter the new information (press Enter to keep the existing value). Validate any newly entered values.
- **Step 3:** Update the `Car` object's attributes and call `CarService.updateCar()`.
- **Step 4:** Display `"Update successful"`.

## Function 4: Delete a Car

- **Input:** `licensePlate`, `confirm` (Y/N).
- **Step 1:** Enter `licensePlate` and check whether the car exists.
- **Step 2:** Ask for confirmation: `"Delete this car? (Y/N)"`. If N is selected, display `"Delete cancelled."`
- **Step 3:** If Y is selected, call `CarService.deleteCar(plate)` and receive a `DeleteResult`:
  - `NOT_FOUND`: display `"Unregistered vehicle"`.
  - `INSURED`: display `"Cannot delete: the car is already insured"`.
  - `SUCCESS`: display `"Delete successful"`.

## Function 5: Add an Insurance Policy

- **Input:** `insuranceId`, `establishedDate`, `licensePlate`, `customerName`, `insurancePeriod`.
- **Step 1:** Enter `insuranceId` and check that it is not duplicated (`existsId`).
- **Step 2:** Enter `establishedDate` (`MM/dd/yyyy`).
- **Step 3:** Enter `licensePlate` and check that the car exists in `CarService`.
- **Step 4:** Enter `customerName` (not empty) and `insurancePeriod` (∈ {12, 24, 36}).
- **Step 5:** Call `calculateFee()` to calculate the fee: 12 months = 25%, 24 months = 40%, and 36 months = 45% of the car's value.
- **Step 6:** Create an `Insurance` object and save it through `InsuranceService.addInsurance()`.
- **Step 7:** Display `"Insurance added successfully. Fee: $..."` and ask whether to continue (Y/N).

## Function 6: List Insurance Policies by Year

- **Input:** `year`, `field` (1: ID, 2: Establishment Date, 3: License Plate, 4: Period), `order` (ASC/DESC).
- **Step 1:** Enter a valid year.
- **Step 2:** Select the field and sort order.
- **Step 3:** Call `InsuranceService.getByYear(year, field, asc)` to filter and sort the records.
- **Step 4:** Display the list in tabular form, including the fees.

## Function 7: Report Uninsured Cars

- **Input:** `field` (1: License Plate, 2: Owner, 3: Registration Date, 4: Vehicle Type), `order` (ASC/DESC).
- **Step 1:** Select the field and sort order.
- **Step 2:** Call `CarService.getUninsuredCars(field, asc)` to filter cars that do not appear in `InsuranceRepository` and sort them.
- **Step 3:** Display a detailed report in tabular form.

## Function 8: Save Data to Files

- **Input:** The current data in RAM.
- **Step 1:** Execute `CarRepository.saveToFile()` to write data to `carInfo.dat`.
- **Step 2:** Execute `InsuranceRepository.saveToFile()` to write data to `insurances.dat`.
- **Step 3:** Report that saving succeeded, or print detailed error information if an I/O exception occurs.

## Function 9: Load Data from Files

- **Input:** The files `carInfo.dat` and `insurances.dat`.
- **Step 1:** Call `CarRepository.loadFromFile()` to load data into the `CarRepository` map.
- **Step 2:** Call `InsuranceRepository.loadFromFile()` to load data into the `InsuranceRepository` map.
- **Step 3:** Report that loading succeeded, or indicate that a file could not be found or a read error occurred.

## Function 10: Quit the Program

- **Input:** `confirm` (Y/N).
- **Step 1:** Ask `"Are you sure you want to quit? (Y/N)"`. If N is selected, return to the main menu.
- **Step 2:** If Y is selected, automatically call the Save Data function (`saveToFile()`) to ensure that the latest changes are written to disk.
- **Step 3:** End the main loop, print `"Goodbye!"`, and exit the application.
