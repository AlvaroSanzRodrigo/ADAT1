package Models;

public class Brand {
    private int idBrand;
    private String brandName;
    private String brandCountry;
    private int brandYearOfFundation;

    public Brand(int idBrand, String brandName, String brandCountry, int brandYearOfFundation) {
        this.idBrand = idBrand;
        this.brandName = brandName;
        this.brandCountry = brandCountry;
        this.brandYearOfFundation = brandYearOfFundation;
    }

    public Brand() {}

    public int getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCountry() {
        return brandCountry;
    }

    public void setBrandCountry(String brandCountry) {
        this.brandCountry = brandCountry;
    }

    public int getBrandYearOfFundation() {
        return brandYearOfFundation;
    }

    public void setBrandYearOfFundation(int brandYearOfFundation) {
        this.brandYearOfFundation = brandYearOfFundation;
    }
}
