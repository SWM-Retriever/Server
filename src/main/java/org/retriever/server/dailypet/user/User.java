package org.retriever.server.dailypet.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    private String password;

    private String nickName;

    private RoleType roleType;
}
