package org.retriever.server.dailypet.domain.member;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    private String password;

    private String nickName;

    private String email;

    private RoleType roleType;
}
