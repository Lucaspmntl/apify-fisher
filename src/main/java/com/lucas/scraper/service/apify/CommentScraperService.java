package com.lucas.scraper.service.apify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentScraperService {

    @Value("${apify.actor-name.comment-scraper}")
    String CommentActorId;

    @Autowired
    ApifyActorService apifyActorService;

    public Object getComments(String actorId){
        //TODO: Realizar refatoração, possívelmente cada actor precisa de um feign proprio, pois cada actor tem suas próprias configurações
    }
}
