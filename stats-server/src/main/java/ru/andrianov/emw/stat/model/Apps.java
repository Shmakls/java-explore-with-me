package ru.andrianov.emw.stat.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "apps", schema = "public")
@Data
public class Apps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String app;

    @OneToMany
    @JoinColumn(name = "app")
    private List<EndpointHit> endpointHits;

}
