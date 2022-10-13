package com.callibrity.bakeoff.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Artist {

// ------------------------------ FIELDS ------------------------------

    @Id
    @Setter(AccessLevel.PROTECTED)
    private String id = UUID.randomUUID().toString();

    private String name;
    private Genre genre;

}
