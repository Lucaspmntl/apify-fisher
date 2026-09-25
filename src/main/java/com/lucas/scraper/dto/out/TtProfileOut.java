package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucas.scraper.utils.ValidatablePayload;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TtProfileOut(

        String id,
        @JsonAlias("name") String username,
        @JsonAlias("nickName") String fullName,
        @JsonAlias("signature") String biography,
        @JsonAlias("avatar") String profilePicUrl,
        String profileUrl,
        @JsonAlias("privateAccount") Boolean isPrivate

) implements ValidatablePayload {

    @Override
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.username == null || this.username.isBlank()) &&
                (this.fullName == null || this.fullName.isBlank()) &&
                (this.biography == null || this.biography.isBlank()) &&
                (this.profilePicUrl == null || this.profilePicUrl.isBlank()) &&
                (this.profileUrl == null || this.profileUrl.isBlank());
    }
}
