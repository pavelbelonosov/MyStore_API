package moysklad.model.reports;

import java.math.BigDecimal;

public class ProductStock {

    private String productArticle;
    private String productName;
    private BigDecimal stock;

    public ProductStock(String productArticle, String productName, BigDecimal stock) {
        this.productName = productName;
        this.productArticle = productArticle;
        this.stock = stock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductArticle() {
        return productArticle;
    }

    public void setProductArticle(String productArticle) {
        this.productArticle = productArticle;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }
}
