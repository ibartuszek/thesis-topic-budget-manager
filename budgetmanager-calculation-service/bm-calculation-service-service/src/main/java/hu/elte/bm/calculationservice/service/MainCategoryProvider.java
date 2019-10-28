package hu.elte.bm.calculationservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class MainCategoryProvider {

    private final RestTemplate restTemplate;

    @Value("${bm.transactionservice.maincategories.findall}")
    private String findMainCategoriesUrl;

    public MainCategoryProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MainCategory> provide(final TransactionType type, final Long userId) {
        String url = createUrl(type, userId);
        ResponseEntity<MainCategory[]> responseEntity = restTemplate.getForEntity(url, MainCategory[].class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? List.of(Objects.requireNonNull(responseEntity.getBody())) : Collections.emptyList();
    }

    private String createUrl(final TransactionType type, final Long userId) {
        return UriComponentsBuilder.fromHttpUrl(findMainCategoriesUrl)
                .queryParam("type", type)
                .queryParam("userId", userId)
                .toUriString();
    }

}
