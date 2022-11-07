package com.trilogyed.gamestoreinvoicing.config.util.feign;

import com.trilogyed.gamestoreinvoicing.model.Console;
import com.trilogyed.gamestoreinvoicing.model.Game;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@FeignClient(name = "game-store-catalog")
public interface GameStoreCatalogClient {

    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public Console getConsoleById(@PathVariable("id") long consoleId);
    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public Game getGameInfo(@PathVariable("id") long gameId);
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public TShirt getTShirt(@PathVariable("id") long tShirtId);
    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public Game createGame(@RequestBody @Valid Game gameViewModel);
    @RequestMapping(value = "/console", method = RequestMethod.POST)
    public Console createConsole(@RequestBody @Valid Console consoleViewModel);
    @RequestMapping(value = "/tshirt", method = RequestMethod.POST)
    public TShirt createTShirt(@RequestBody @Valid TShirt tShirtViewModel);
    @RequestMapping(value = "/console/{id}", method = RequestMethod.PUT)
    public Console updateConsole(@PathVariable long consoleId);
    @RequestMapping(value = "/game/{id}", method = RequestMethod.PUT)
    public Game updateGame(@PathVariable long gameId);
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.PUT)
    public TShirt updateTShirt(@PathVariable long tShirtId);



}
