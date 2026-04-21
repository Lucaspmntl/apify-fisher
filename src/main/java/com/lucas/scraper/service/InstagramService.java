package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.gateway.IgCommentsGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstagramService {

    private static final Logger log = LoggerFactory.getLogger(InstagramService.class);
    private final IgCommentsGateway commentsGateway;
    
    public InstagramService(IgCommentsGateway igCommentsGateway) {
        this.commentsGateway = igCommentsGateway;
    }


    public List<IgCommentsOut> getInstagramComments(String postUrl){
        IgCommentsIn input = new IgCommentsIn(List.of(postUrl), false, false, 1000);
        return commentsGateway.getComments(input);
    }


    // TODO: Verificar possibilidade de deixar filtros de comentários para a outra parte da aplicação
//    public List<IgCommentsOut> getInstagramFilteredComments(FisherIn input, String keyword){
//
//        if (!input.instagramUrl().contains("instagram")) {
//            log.warn("Comment Service: Requisição ignorada devido invalidade de URL");
//            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do Instagram.");
//        }
//
//        log.info("Comment Service: Iniciando coleta em: {}", input.instagramUrl());
//
//        List<IgCommentsOut> raw = getInstagramComments(new IgCommentsIn(
//                List.of(input.instagramUrl()),
//                false,
//                false,
//                1000));
//
//         List<IgCommentsOut> response = raw
//                .stream()
//                .filter(item -> item.text()
//                        .toLowerCase()
//                        .contains(keyword.toLowerCase()))
//                .toList();
//
//         log.info("Instagram Comment Service: Foram filtradas {} itens com a keywords \"{}\" no Instagram", response.size(), keyword);
//         return response;
//    }
}
