package moysklad.model.documents;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Sale {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty(message = "Код поступления не может быть пустым")
    @NotBlank(message = "Код поступления  не может состоять из пробелов")
    @Size(min = 1, max = 100, message = "Допустимая длина кода от 1-100 символов")
    @Column(unique = true)
    private String code;

    @NotNull
    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
            message = "Указанный ID склада не соотвествует формату")
    private String storeID;

    @ElementCollection
    @NotNull(message = "Необходим список товарных позиций")
    private List<ProductDocInfo> positions = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date created;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }



    //getters-setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public List<ProductDocInfo> getPositions() {
        return positions;
    }

    public void setPositions(List<ProductDocInfo> positions) {
        this.positions = positions;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
