package com.trilogyed.gamestoreinvoicing.config.util.feign;

import com.trilogyed.gamestoreinvoicing.model.Console;
import com.trilogyed.gamestoreinvoicing.model.Game;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "game-store-catalog")
public interface GameStoreCatalogClient {
    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public Console getConsoleById(@PathVariable("id") long consoleId);
    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public Game getGameInfo(@PathVariable("id") long gameId);
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public TShirt getTShirt(@PathVariable("id") long tShirtId);

}
