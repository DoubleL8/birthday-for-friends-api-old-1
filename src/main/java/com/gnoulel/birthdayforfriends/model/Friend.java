package com.gnoulel.birthdayforfriends.model;

import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthdate;
    private GenderEnum gender;
    private String email;
    private String phone;
    private String note;

}
