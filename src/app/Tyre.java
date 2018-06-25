package app;

import java.util.Objects;

public class Tyre {

    private String tyreCode;
    private String tyreType;
    private String tyrePrice;

    public String getTyreCode() {
        return tyreCode;
    }

    public String getTyreType() {
        return tyreType;
    }

    public String getTyrePrice() {
        return tyrePrice;
    }

    public Tyre(String tyreCode, String tyreType, String tyrePrice) {
        this.tyreCode = tyreCode;
        this.tyreType = tyreType;
        this.tyrePrice = tyrePrice;
    }

    @Override
    public boolean equals(Object o) { // Tyres with same code or with same types are equals
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tyre tyre = (Tyre) o;

        return Objects.equals(tyreCode, tyre.tyreCode) ||
                Objects.equals(tyreType, tyre.tyreType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tyreCode, tyreType);
    }
}