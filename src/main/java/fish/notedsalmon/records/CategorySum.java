package fish.notedsalmon.records;

import java.math.BigDecimal;

public record CategorySum(String categoryName, BigDecimal sum, int red, int green, int blue) {
    public CategorySum(String categoryName, BigDecimal sum, int red, int green, int blue) {
        this.categoryName = categoryName;
        this.sum = sum;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
