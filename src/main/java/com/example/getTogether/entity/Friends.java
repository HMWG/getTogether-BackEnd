package com.example.getTogether.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="friends")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friends extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toFriend", nullable = false)
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromFriend", nullable = false)
    private Member fromMember;
}
