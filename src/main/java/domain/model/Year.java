package domain.model;

public enum Year {
    FRESHMAN(1), SOPHOMORE(2), JUNIOR(3), SENIOR(4);

    private final int year;

    Year(int year) {
        this.year = year;
    }
}
