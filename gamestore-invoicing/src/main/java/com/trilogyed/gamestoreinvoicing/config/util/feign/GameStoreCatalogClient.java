package com.trilogyed.gamestoreinvoicing.config.util.feign;

import com.trilogyed.gamestoreinvoicing.model.Console;
import com.trilogyed.gamestoreinvoicing.model.Game;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "game-store-catalog")
public interface GameStoreCatalogClient {
    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public Console getConsoleById(long id);
    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public Game getGameInfo(long id);
    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public TShirt getTShirt(long id);

}
