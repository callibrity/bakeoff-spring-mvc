package com.callibrity.bakeoff.api;

import com.callibrity.bakeoff.domain.Artist;
import com.callibrity.bakeoff.domain.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistResourceIT {

// ------------------------------ FIELDS ------------------------------

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

// -------------------------- OTHER METHODS --------------------------

    @Test
    void shouldCreateArtist() throws Exception {
        CreateArtistRequest request = new CreateArtistRequest("Prince", Genre.Rock);
        MvcResult result = mockMvc.perform(
                        post("/api/artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Artist artist = mapper.readValue(result.getResponse().getContentAsString(), Artist.class);

        mockMvc.perform(get("/api/artists/{id}", artist.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteArtist() throws Exception {
        CreateArtistRequest request = new CreateArtistRequest("Prince", Genre.Rock);
        MvcResult result = mockMvc.perform(
                        post("/api/artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Artist artist = mapper.readValue(result.getResponse().getContentAsString(), Artist.class);

        mockMvc.perform(get("/api/artists/{id}", artist.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/artists/{id}", artist.getId()))
                        .andExpect(status().isOk());

        mockMvc.perform(get("/api/artists/{id}", artist.getId()))
                .andExpect(status().isNotFound());
    }


}
