package com.first.moviecatalogservice.resources;

import com.first.moviecatalogservice.models.CatalogItem;
import com.first.moviecatalogservice.models.Movie;
import com.first.moviecatalogservice.models.Rating;
import com.first.moviecatalogservice.models.UserRating;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private  RestTemplate restTemplate;

    //@Autowired
    //private DiscoveryClient discoveryClient;

   /* @Autowired
    private WebClient.Builder webClientBuilder;*/

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
       UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId,
                UserRating.class);
        /*List<Rating> ratings = Arrays.asList(
                new Rating("1234",4),
                new Rating("5678", 3)
        );*/
       return ratings.getUserRating().stream().map(rating -> {
          /*
          Movie movie =  webClientBuilder.build().get()
                   .uri("http://localhost:8081/movies/" + rating.getMovieId()).retrieve()
                   .bodyToMono(Movie.class)
                   .block();*/
           //For each movie Id , call movie info service and get details
           Movie movie =  restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
           //put them all together
           return new CatalogItem(movie.getName(),"Test",rating.getRating());
           }).collect(Collectors.toList());

    }

}
