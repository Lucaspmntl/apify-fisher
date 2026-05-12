package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbProfileOut(

        String id,
        @JsonAlias("name") String username,
        // fullName -> Não disponibilizado
        @JsonAlias("intro") String biography,
        @JsonAlias("image") String profilePicUrl,
        @JsonAlias("url") String profileUrl
) {

    // O actor cleansyntax/facebook-profile-posts-scraper as vezes falha em retonar todos os dados, retornando uma lista
    // com o objetos vazios (""), o método verifica essa possibilidade
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.profileUrl == null || this.profileUrl.isBlank()) &&
                (this.biography == null || this.biography.isBlank()) &&
                (this.username == null || this.username.isBlank()) &&
                (this.profilePicUrl == null || this.profilePicUrl.isBlank());
    }
}
