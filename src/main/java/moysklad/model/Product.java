package moysklad.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty(message = "Артикул не может быть пустым")
    @NotBlank(message = "Артикул не может состоять из пробелов")
    @Size(min = 1, max = 100, message = "Допустимая длина артикула от 1-100 символов")
    private String article;

    @NotEmpty(message = "Наименование товара не может быть пустым")
    @NotBlank(message = "Наименование товара не может состоять из пробелов")
    @Size(min = 3, max = 100, message = "Допустимое наименование товара от 3-100 символов")
    private String name;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2, message = "Неверный формат закупочной цены")
    @Column(name = "last_purchase_price")
    private BigDecimal lastPurchasePrice = new BigDecimal("0.00");

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1000000", message = "Превышена максимальная цена продажи")
    @Digits(integer = 6, fraction = 2, message = "Неверный формат цены продажи")
    @Column(name = "last_selling_price")
    private BigDecimal lastSellingPrice = new BigDecimal("0.00");

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Position> positions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    //getters-setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLastPurchasePrice() {
        return lastPurchasePrice;
    }

    public void setLastPurchasePrice(BigDecimal lastPurchasePrice) {
        this.lastPurchasePrice = lastPurchasePrice;
    }

    public BigDecimal getLastSellingPrice() {
        return lastSellingPrice;
    }

    public void setLastSellingPrice(BigDecimal lastSellingPrice) {
        this.lastSellingPrice = lastSellingPrice;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
