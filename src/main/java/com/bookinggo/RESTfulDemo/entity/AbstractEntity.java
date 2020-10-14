package com.bookinggo.RestfulDemo.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(exclude = {/*"createdBy",*/ "createdWhen", "modifiedWhen"})
public abstract class AbstractEntity implements Serializable {

/*    private static final String USER = System.getProperty("user.name");

    @Builder.Default
    @ToString.Exclude
    private String createdBy = USER;*/

    @CreationTimestamp
    @ToString.Exclude
    private LocalDateTime createdWhen;

    @ToString.Exclude
    private LocalDateTime modifiedWhen;

    @PreUpdate
    public void onUpdate() {
        modifiedWhen = LocalDateTime.now();
    }
}
