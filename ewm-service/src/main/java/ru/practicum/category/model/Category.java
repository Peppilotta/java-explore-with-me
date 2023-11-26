package ru.practicum.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(name = "cat_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: name. Error: must not be blank. Value: blank")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Event> events;

    @Override
    public String toString() {
        return "Category{id=" + id + ", name=" + name + "}";
    }
}