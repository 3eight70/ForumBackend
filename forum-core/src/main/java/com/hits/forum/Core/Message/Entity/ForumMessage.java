package com.hits.forum.Core.Message.Entity;

import com.hits.forum.Core.File.Entity.File;
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
@Table(name = "messages")
public class ForumMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;

    @Column(nullable = false)
    private String authorLogin;

    @Column(nullable = false)
    @Size(min = 1, message = "Минимальная длина сообщения равна 1")
    private String content;

    @Column(nullable = false)
    private UUID themeId;

    @Column(nullable = false)
    private UUID categoryId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "message_file",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files;
}
