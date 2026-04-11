package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.request.ApifyCommentsRequestDTO;
import com.lucas.scraper.dto.response.ApifyCommentsResponseDTO;
import com.lucas.scraper.dto.response.startRun.ApifyRunResponse;
import com.lucas.scraper.service.apify.ApifyGenericFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CommentsScraperGateway {

    @Value("${apify.actor-id.comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private ApifyGenericFeign apifyClient;

    public CommentsScraperGateway(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    public List<ApifyCommentsResponseDTO> getComments(ApifyCommentsRequestDTO input) {
        ApifyRunResponse run = apifyClient.startRun(actorId, input, "Bearer " + token);

        if (!polling(run.data().runId()))
            throw new RuntimeException("Polling failed");

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);

        // Transforma os dados brutos em um objeto ApifyCommentsResponseDTO
        return rawComments.stream().map(item -> new ApifyCommentsResponseDTO(
                (String) item.get("id"),
                (String) item.get("text"),
                (String) item.get("ownerUsername"),
                (String) item.get("ownerProfilePicUrl"),
                (String) item.get("timestamp"),
                (Integer) item.get("likesCount"),
                (Integer) item.get("repliesCount")

                //(List<repliesDTO>) item.get("replies")
        )).toList();

    }

    private boolean polling(String runId){

        String status;

        do{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            status = apifyClient.getRunDetails(runId, "Bearer " + token).data().status();

            // TODO: Tratar excessões como: FAILED ou TIMED-OUT
            // Verificar as possibilidades em: https://docs.apify.com/api/v2/actor-run-get

        } while(!status.equalsIgnoreCase("SUCCEEDED"));

        return true;
    }
}
