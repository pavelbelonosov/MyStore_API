package moysklad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Store {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty(message = "Наименование склада не может быть пустым")
    @NotBlank(message = "Наименование склада не может состоять из пробелов")
    @Size(min = 1, max = 25, message = "Допустимое наименование склада от 1-25 символов")
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Position> positions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return this.id == store.id;
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

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
