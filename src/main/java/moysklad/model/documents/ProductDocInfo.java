package moysklad.model.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Embeddable
public class ProductDocInfo {

    @NotNull(message = "ID принимающего склада обязателен в документе поступления товаров")
    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", message = "Указанный ID склада не соотвествует формату")
    private String productID;

    @NotNull(message = "Количество товара обязательно в документе поступления")
    @DecimalMin(value = "0", message = "Недопустимо отрицательное количество товара в документе поступления")
    private BigDecimal quantity;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2, message = "Неверный формат закупочной цены")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1000000", message = "Превышена максимальная цена продажи")
    @Digits(integer = 6, fraction = 2, message = "Неверный формат цены продажи")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal sellingPrice;



    //getters-setters

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}

