package org.retriever.server.dailypet.domain.member.entity;

import lombok.Getter;
import org.retriever.server.dailypet.domain.member.enums.RoleType;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    private String password;

    private String nickName;

    private String email;

    private RoleType roleType;
}
