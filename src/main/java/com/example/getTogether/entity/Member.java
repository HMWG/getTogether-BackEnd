package com.example.getTogether.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;
}
