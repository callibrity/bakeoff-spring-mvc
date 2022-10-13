package com.callibrity.bakeoff.api;

import com.callibrity.bakeoff.domain.Artist;
import com.callibrity.bakeoff.domain.ArtistRepository;
import com.callibrity.bakeoff.domain.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ArtistResource.class)
class ArtistResourceTest {

    @MockBean
    private ArtistRepository mockRepository;

    @Autowired
    private MockMvc mockMvc;


    @Captor
    private ArgumentCaptor<Artist> artistCaptor;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldCreateArtist() throws Exception {
        CreateArtistRequest request = new CreateArtistRequest("Prince", Genre.Rock);
        mockMvc.perform(
                        post("/api/artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(mockRepository).save(artistCaptor.capture());

        Artist artist = artistCaptor.getValue();
        assertThat(artist.getName()).isEqualTo("Prince");
        assertThat(artist.getGenre()).isEqualTo(Genre.Rock);
    }

    @Test
    void shouldUpdateArtist() throws Exception {
        final Artist original = new Artist();
        original.setName("Garth Brooks");
        original.setGenre(Genre.Country);

        when(mockRepository.findById(original.getId())).thenReturn(Optional.of(original));

        UpdateArtistRequest request = new UpdateArtistRequest("Prince", Genre.Rock);
        mockMvc.perform(
                        put("/api/artists/{id}", original.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(mockRepository).save(artistCaptor.capture());

        Artist updated = artistCaptor.getValue();
        assertThat(updated.getName()).isEqualTo("Prince");
        assertThat(updated.getGenre()).isEqualTo(Genre.Rock);
    }

    @Test
    void shouldNotUpdateArtistWhenNotFound() throws Exception {

        when(mockRepository.findById("12345")).thenReturn(Optional.empty());

        UpdateArtistRequest request = new UpdateArtistRequest("Prince", Genre.Rock);
        mockMvc.perform(
                        put("/api/artists/{id}", "12345")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRetrieveArtist() throws Exception {
        final Artist artist = new Artist();
        artist.setName("Garth Brooks");
        artist.setGenre(Genre.Country);
        when(mockRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        mockMvc.perform(get("/api/artists/{id}", artist.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId()))
                .andExpect(jsonPath("$.name").value("Garth Brooks"))
                .andExpect(jsonPath("$.genre").value("Country"));

    }

    @Test
    void shouldNotRetrieveArtistWhenNotFound() throws Exception {

        when(mockRepository.findById("12345")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artists/{id}", "12345").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldListArtists() throws Exception {
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

        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    void shouldDeleteArtist() throws Exception {

        mockMvc.perform(delete("/api/artists/{id}", "12345")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepository).deleteById("12345");
    }
}