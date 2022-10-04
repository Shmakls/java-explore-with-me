package ru.andrianov.emw.categories.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "categories", schema = "public")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


}
