package com.kiwie.store.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String path;

    @JoinColumn(name = "productId",referencedColumnName = "id")
    @ManyToOne
    private Product product;

    public Image(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
