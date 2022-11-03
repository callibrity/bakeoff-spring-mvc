package com.callibrity.bakeoff.api;

import com.callibrity.bakeoff.domain.Artist;
import com.callibrity.bakeoff.domain.ArtistRepository;
import com.callibrity.bakeoff.domain.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ArtistResourceTest {

    @Mock
    private ArtistRepository mockRepository;

    @Captor
    private ArgumentCaptor<Artist> artistCaptor;

    @Test
    void shouldCreateArtist() {
        CreateArtistRequest request = new CreateArtistRequest("Prince", Genre.Rock);
        ArtistResource resource = new ArtistResource(mockRepository);
        Artist result = resource.createArtist(request);

        verify(mockRepository).save(artistCaptor.capture());

        Artist artist = artistCaptor.getValue();
        assertThat(artist.getName()).isEqualTo("Prince");
        assertThat(artist.getGenre()).isEqualTo(Genre.Rock);
        assertThat(artist).isSameAs(result);
    }

    @Test
    void shouldUpdateArtist() {
        final Artist original = new Artist();
        original.setName("Garth Brooks");
        original.setGenre(Genre.Country);

        when(mockRepository.findById(original.getId())).thenReturn(Optional.of(original));

        ArtistResource resource = new ArtistResource(mockRepository);

        UpdateArtistRequest request = new UpdateArtistRequest("Prince", Genre.Rock);
        resource.updateArtist(original.getId(), request);

        verify(mockRepository).save(artistCaptor.capture());

        Artist updated = artistCaptor.getValue();
        assertThat(updated.getName()).isEqualTo("Prince");
        assertThat(updated.getGenre()).isEqualTo(Genre.Rock);
    }

    @Test
    void shouldNotUpdateArtistWhenNotFound() {

        when(mockRepository.findById("12345")).thenReturn(Optional.empty());

        ArtistResource resource = new ArtistResource(mockRepository);

        UpdateArtistRequest request = new UpdateArtistRequest("Prince", Genre.Rock);

        assertThatThrownBy(() -> resource.updateArtist("12345", request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldRetrieveArtist() {
        final Artist artist = new Artist();
        artist.setName("Garth Brooks");
        artist.setGenre(Genre.Country);
        when(mockRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        ArtistResource resource = new ArtistResource(mockRepository);

        assertThat(resource.getArtistById(artist.getId())).isSameAs(artist);
    }


    @Test
    void shouldNotRetrieveArtistWhenNotFound() {

        when(mockRepository.findById("12345")).thenReturn(Optional.empty());
        ArtistResource resource = new ArtistResource(mockRepository);


        assertThatThrownBy(() -> resource.getArtistById("12345"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(mockRepository).findById("12345");
        verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void shouldListArtists() {
        final Artist garth = new Artist();
        garth.setName("Garth Brooks");
        garth.setGenre(Genre.Country);

        final Artist prince = new Artist();
        prince.setName("Prince");
        prince.setGenre(Genre.Rock);

        final Artist madonna = new Artist();
        madonna.setName("Prince");
        madonna.setGenre(Genre.Pop);

        when(mockRepository.findAll()).thenReturn(List.of(garth, prince, madonna));

        ArtistResource resource = new ArtistResource(mockRepository);

        assertThat(resource.listArtists()).containsExactly(garth, prince, madonna);

        verify(mockRepository).findAll();
        verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void shouldDeleteArtist() {
        ArtistResource resource = new ArtistResource(mockRepository);
        resource.deleteArtist("12345");
        verify(mockRepository).deleteById("12345");
        verifyNoMoreInteractions(mockRepository);
    }
}