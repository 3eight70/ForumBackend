package com.hits.forum.Core.Category.Entity;

import com.hits.forum.Core.Theme.Entity.ForumTheme;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class ForumCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;

    @Column(nullable = false)
    private String authorLogin;

    @Column(nullable = false)
    @Size(min = 5, message = "Минимальная длина названия категории равна 5")
    private String categoryName;

    private UUID parentId;

    @OneToMany(mappedBy = "categoryId", cascade = CascadeType.ALL)
    private List<ForumTheme> themes;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    private List<ForumCategory> childCategories;
}
