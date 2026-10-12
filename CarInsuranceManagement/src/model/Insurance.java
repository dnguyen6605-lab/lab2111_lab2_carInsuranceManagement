
package model;
 
import java.io.Serializable;
import java.time.LocalDate;

public class Insurance implements Serializable{
    private static final long serialVersionUID = 1L;
    private String insuranceId;
    private LocalDate establishedDate;
    private String licensePlate;
    private String customerName;
    private int insurancePeriod;
    private long fee;
    
    //contructor

    public Insurance() {
    }

    public Insurance(String insuranceId, LocalDate establishedDate, 
            String licensePlate, String customerName, int insurancePeriod, long fee) {
        this.insuranceId = insuranceId;
        this.establishedDate = establishedDate;
        this.licensePlate = licensePlate;
        this.customerName = customerName;
        this.insurancePeriod = insurancePeriod;
        this.fee = fee;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getInsurancePeriod() {
        return insurancePeriod;
    }

    public void setInsurancePeriod(int insurancePeriod) {
        this.insurancePeriod = insurancePeriod;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
    private static final java.time.format.DateTimeFormatter DATE_FORMAT =
            java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public String toString() {
        return String.format(
                "Insurance ID: %s%nEstablished date: %s%nLicense plate: %s%nCustomer name: %s%nInsurance period: %d months%nInsurance fee: $ %,d",
                insuranceId,
                (establishedDate != null ? establishedDate.format(DATE_FORMAT) : "N/A"),
                licensePlate,
                customerName,
                insurancePeriod,
                fee);
    }
}
