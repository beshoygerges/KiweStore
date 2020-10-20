package com.kiwie.store.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @JoinColumn(name = "userId",referencedColumnName = "id")
    @ManyToOne
    private User user;

    @Builder
    public Product(String name, Integer quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setProduct(this);
    }


}
