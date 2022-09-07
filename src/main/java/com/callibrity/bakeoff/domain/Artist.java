package com.callibrity.bakeoff.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Artist {

// ------------------------------ FIELDS ------------------------------

    @Id
    @Setter(AccessLevel.PROTECTED)
    private String id = UUID.randomUUID().toString();

    private String name;
    private Genre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id.equals(artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
