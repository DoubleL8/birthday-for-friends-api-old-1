package com.gnoulel.birthdayforfriends.entity;

import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import com.gnoulel.birthdayforfriends.repository.customquery.FriendCustomQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * The core functionality here involves mapping result sets from database SQL statements into Java objects.
 * Every SqlResultSetMapping annotation requires only one property, name.
 * However, without one of the member types, nothing will be mapped.
 * The member types are ColumnResult, ConstructorResult, and EntityResult.
 */

@SqlResultSetMappings({
        @SqlResultSetMapping(name = "FriendMappings.getFriendsBelongToUser", classes = {
                @ConstructorResult(targetClass = Friend.class, columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "birthdate", type = LocalDate.class),
                        @ColumnResult(name = "gender", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "note", type = String.class),
                }),

        })
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Friend.isEmailExistedBelongToUser",
                query = FriendCustomQuery.IS_EMAIL_EXISTED_BELONG_TO_USER),
        @NamedNativeQuery(
                name = "Friend.isPhoneExistedBelongToUser",
                query = FriendCustomQuery.IS_PHONE_EXISTED_BELONG_TO_USER),
})
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    public Friend(Long id, String name, LocalDate birthdate, String gender,
                  String email, String phone, String note) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = GenderEnum.of(gender);
        this.email = email;
        this.phone = phone;
        this.note = note;
    }
}
