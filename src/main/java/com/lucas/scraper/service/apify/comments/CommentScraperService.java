package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.FbCommentsIn;
import com.lucas.scraper.dto.in.IgCommentsIn;
import com.lucas.scraper.dto.in.TtCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.TtCommentsOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentScraperService {

    private static final Logger log = LoggerFactory.getLogger(CommentScraperService.class);
    private final IgCommentsGateway igcommentsGateway;
    private final FbCommentsGateway fbCommentsGateway;
    private final TtCommentsGateway ttCommentsGateway;
    public CommentScraperService(IgCommentsGateway igCommentsGateway,
                                 FbCommentsGateway fbCommentsGateway,
                                 TtCommentsGateway ttCommentsGateway) {
        this.igcommentsGateway = igCommentsGateway;
        this.fbCommentsGateway = fbCommentsGateway;
        this.ttCommentsGateway = ttCommentsGateway;
    }


    public List<IgCommentsOut> getInstagramComments(IgCommentsIn input){
        return igcommentsGateway.getComments(input);
    }

    public List<TtCommentsOut> getTiktokComments(TtCommentsIn input){
        return ttCommentsGateway.getComments(input);
    }

    public List<FbCommentsOut> getFacebookComments(FbCommentsIn input){
        return fbCommentsGateway.getComments(input);
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
