package com.callibrity.bakeoff.api;

import com.callibrity.bakeoff.domain.Genre;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateArtistRequest(@NotEmpty String name, @NotNull Genre genre) {
}
