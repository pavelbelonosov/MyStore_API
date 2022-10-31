package moysklad.model.reports;

import java.math.BigDecimal;

public class AllProducts {

    private String article;
    private String name;
    private BigDecimal lastPurchasePrice;
    private BigDecimal lastSellingPrice;

    public AllProducts(String article, String name, BigDecimal lastPurchasePrice,
                       BigDecimal lastSellingPrice) {
        this.article = article;
        this.name = name;
        this.lastPurchasePrice = lastPurchasePrice;
        this.lastSellingPrice = lastSellingPrice;

    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
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

}
