package com.challenge3.challenge3.compass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "comment", schema = "blog")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment{

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "body")
    private String body;

}
