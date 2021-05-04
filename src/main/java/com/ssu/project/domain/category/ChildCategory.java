package com.ssu.project.domain.category;

import com.ssu.project.domain.item.Item;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class ChildCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 500)
    private String name;

    @OneToMany(mappedBy = "childCategory", cascade={CascadeType.ALL})
    private List<Item> items = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private ParentCategory parentCategory;
}
