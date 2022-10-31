package com.trilogyed.gamestoreinvoicing.config.util.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "game-store-catalog")
public interface GameStoreCatalogClient {
    @RequestMapping(value = "/game", method = RequestMethod.GET)
//    public String getRandomQuote();
    public List<GameViewModel> getAllGames();
}
