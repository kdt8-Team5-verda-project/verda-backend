package com.verda.BE.login.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FundPost")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(mappedBy = "ChatRoomEntity")
    private long fmId;
    private String email;
    private String name;
    private String age;
    private String file;
    private String record;
    private String location;
    private String number;


    //test 용 text
    @Builder
    public FundEntity(long fmId, String email, String name, String age, String file, String record, String location,
                      String number) {
        this.fmId = fmId;
        this.email = email;
        this.name = name;
        this.age = age;
        this.file = file;
        this.record = record;
        this.location = location;
        this.number = number;
    }
}
