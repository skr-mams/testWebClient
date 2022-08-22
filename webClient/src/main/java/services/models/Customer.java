package services.models;


import io.vertx.core.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Mario Manzanarez
 * @since 8/22/22
 */
public class Customer {
    private long idCustomer;
    private String personType;
    private String name;
    private String primaryLastName;
    private String secondaryLastName;
    private String company;
    private String gender;
    private String rfc;
    private LocalDate birthdate;
    private LocalDate constitutionDate;
    private String nationality;
    private String status;
    private Long groupId;
    private Long subGroupId;
    private Long subSubGroupId;
    private String modifiedByUser;
    private Boolean isdeleted;
    private String createUser;
    private String namePayment;
    private String primaryLastNamePayment;
    private String secondaryLastNamePayment;
    private LocalTime paymentScheduleStart;
    private LocalTime paymentScheduleEnd;
    private String commentsPayment;
    private Long idSeller;
    private String bucket;
    private Long idCatCategories;
    private String primaryColor;
    private String secondaryColor;
    private String imgKeyLogo;
    private Long paymentCompany;


    public Customer(JsonObject jsonObject) {
    }

    public Customer() {
    }

    public Long getPaymentCompany() {
        return paymentCompany;
    }

    public void setPaymentCompany(Long paymentCompany) {
        this.paymentCompany = paymentCompany;
    }

    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryLastName() {
        return primaryLastName;
    }

    public void setPrimaryLastName(String primaryLastName) {
        this.primaryLastName = primaryLastName;
    }

    public String getSecondaryLastName() {
        return secondaryLastName;
    }

    public void setSecondaryLastName(String secondaryLastName) {
        this.secondaryLastName = secondaryLastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getConstitutionDate() {
        return constitutionDate;
    }

    public void setConstitutionDate(LocalDate constitutionDate) {
        this.constitutionDate = constitutionDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(Long subGroupId) {
        this.subGroupId = subGroupId;
    }

    public Long getSubSubGroupId() {
        return subSubGroupId;
    }

    public void setSubSubGroupId(Long subSubGroupId) {
        this.subSubGroupId = subSubGroupId;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalTime getPaymentScheduleStart() {
        return paymentScheduleStart;
    }

    public void setPaymentScheduleStart(LocalTime paymentScheduleStart) {
        this.paymentScheduleStart = paymentScheduleStart;
    }

    public LocalTime getPaymentScheduleEnd() {
        return paymentScheduleEnd;
    }

    public void setPaymentScheduleEnd(LocalTime paymentScheduleEnd) {
        this.paymentScheduleEnd = paymentScheduleEnd;
    }

    public String getCommentsPayment() {
        return commentsPayment;
    }

    public void setCommentsPayment(String commentsPayment) {
        this.commentsPayment = commentsPayment;
    }

    public Long getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(Long idSeller) {
        this.idSeller = idSeller;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Long getIdCatCategories() {
        return idCatCategories;
    }

    public void setIdCatCategories(Long idCatCategories) {
        this.idCatCategories = idCatCategories;
    }


    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getImgKeyLogo() {
        return imgKeyLogo;
    }

    public void setImgKeyLogo(String imgKeyLogo) {
        this.imgKeyLogo = imgKeyLogo;
    }

    public String getNamePayment() {
        return namePayment;
    }

    public void setNamePayment(String namePayment) {
        this.namePayment = namePayment;
    }

    public String getPrimaryLastNamePayment() {
        return primaryLastNamePayment;
    }

    public void setPrimaryLastNamePayment(String primaryLastNamePayment) {
        this.primaryLastNamePayment = primaryLastNamePayment;
    }

    public String getSecondaryLastNamePayment() {
        return secondaryLastNamePayment;
    }

    public void setSecondaryLastNamePayment(String secondaryLastNamePayment) {
        this.secondaryLastNamePayment = secondaryLastNamePayment;
    }

    /**
     * @return nombre compuesto de un cliente
     * @author Mario Manzanarez
     */
    public String customCompanyName() {
        String name = null;
        switch (this.personType) {
            case "M":
                name = getCompany();
                break;
            case "F":
                name = getName() + " " + getPrimaryLastName() + " " + getSecondaryLastName();
                break;
        }
        return name;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "idCustomer=" + idCustomer +
                ", personType='" + personType + '\'' +
                ", name='" + name + '\'' +
                ", primaryLastName='" + primaryLastName + '\'' +
                ", secondaryLastName='" + secondaryLastName + '\'' +
                ", company='" + company + '\'' +
                ", gender='" + gender + '\'' +
                ", rfc='" + rfc + '\'' +
                ", birthdate=" + birthdate +
                ", constitutiondate=" + constitutionDate +
                ", nationality='" + nationality + '\'' +
                ", status='" + status + '\'' +
                ", groupId=" + groupId +
                ", subGroupId=" + subGroupId +
                ", subSubGroupId=" + subSubGroupId +
                ", modifiedByUser='" + modifiedByUser + '\'' +
                ", isdeleted=" + isdeleted +
                ", createUser='" + createUser + '\'' +
                ", namePayment='" + namePayment + '\'' +
                ", primaryLastNamePayment='" + primaryLastNamePayment + '\'' +
                ", secondaryLastNamePayment='" + secondaryLastNamePayment + '\'' +
                ", paymentScheduleStart=" + paymentScheduleStart +
                ", paymentScheduleEnd=" + paymentScheduleEnd +
                ", commentsPayment='" + commentsPayment + '\'' +
                ", idSeller=" + idSeller +
                ", bucket='" + bucket + '\'' +
                ", idCatCategories=" + idCatCategories +
                ", primaryColor='" + primaryColor + '\'' +
                ", secondaryColor='" + secondaryColor + '\'' +
                ", imgKeyLogo='" + imgKeyLogo + '\'' +
                ", paymentCompany=" + paymentCompany +
                '}';
    }
}
