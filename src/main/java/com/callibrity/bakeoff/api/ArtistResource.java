package com.callibrity.bakeoff.api;

import com.callibrity.bakeoff.domain.Artist;
import com.callibrity.bakeoff.domain.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/artists", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class ArtistResource {

    private final ArtistRepository repository;

    @GetMapping
    public List<Artist> listArtists() {
        final List<Artist> artists = new LinkedList<>();
        repository.findAll().forEach(artists::add);
        return artists;
    }

    @PostMapping
    public Artist createArtist(@Valid @RequestBody CreateArtistRequest request) {
        final Artist artist = new Artist();
        artist.setName(request.name());
        artist.setGenre(request.genre());
        repository.save(artist);
        return artist;
    }

    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable("id") String id) {
        return findArtistById(id);
    }

    private Artist findArtistById(String id) {
        return repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Artist updateArtist(@PathVariable("id") String id, @Valid @RequestBody UpdateArtistRequest request) {
        Artist existing = findArtistById(id);
        existing.setName(request.name());
        existing.setGenre(request.genre());
        repository.save(existing);
        return existing;
    }
}
