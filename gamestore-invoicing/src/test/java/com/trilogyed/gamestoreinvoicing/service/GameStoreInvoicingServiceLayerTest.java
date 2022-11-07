package com.trilogyed.gamestoreinvoicing.service;

import com.trilogyed.gamestoreinvoicing.config.util.feign.GameStoreCatalogClient;
import com.trilogyed.gamestoreinvoicing.model.*;
import com.trilogyed.gamestoreinvoicing.repository.InvoiceRepository;
import com.trilogyed.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.trilogyed.gamestoreinvoicing.repository.TaxRepository;
import com.trilogyed.gamestoreinvoicing.viewModel.InvoiceViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
@RunWith(SpringRunner.class)
public class GameStoreInvoicingServiceLayerTest {

    InvoiceRepository invoiceRepository;
    ProcessingFeeRepository processingFeeRepository;
    TaxRepository taxRepository;
    GameStoreInvoicingServiceLayer service;
    @MockBean
    GameStoreCatalogClient client;

    private Invoice invoice;

    private Game game;

    @Before
    public void setUp() throws Exception {
        setUpInvoiceRepositoryMock();
        setUpProcessingFeeRepositoryMock();
        setUpTaxRepositoryMock();
        setUpclientMock();

        service = new GameStoreInvoicingServiceLayer(invoiceRepository, processingFeeRepository, taxRepository, client);
    }

    //Testing Invoice Operations...
    @Test
    public void shouldCreateFindInvoice() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test
    public void shouldFindAllInvoices(){
        InvoiceViewModel savedInvoice1 = new InvoiceViewModel();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        InvoiceViewModel savedInvoice2 = new InvoiceViewModel();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        InvoiceViewModel savedInvoice3 = new InvoiceViewModel();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<InvoiceViewModel> currInvoices = new ArrayList<>();
        currInvoices.add(savedInvoice1);
        currInvoices.add(savedInvoice2);
        currInvoices.add(savedInvoice3);

        List<InvoiceViewModel> foundAllInvoices = service.getAllInvoices();

        assertEquals(currInvoices.size(), foundAllInvoices.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadState() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NY");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(99);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadItemType() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("Bad Item Type");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithNoInventory() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(6);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidItem() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("nothing");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidQuantity() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(0);

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenCreateInvoiceInvalidInvoiceMV() {

        InvoiceViewModel invoiceViewModel = null;

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }

    //Testing Console Operations...
    @Test
    public void shouldCreateGetConsole() {

        Console console = new Console();
        console.setModel("Playstation");
        console.setManufacturer("Sony");
        console.setMemoryAmount("120gb");
        console.setProcessor("Intel I7-9750H");
        console.setPrice(new BigDecimal("299.99"));
        console.setQuantity(4);
        console = client.createConsole(console);

        Console console1 = client.getConsoleById(console.getId());
        assertEquals(console, console1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateConsoleWithNullViewModel() {

        Console console = new Console();

        console = null;
        console = client.createConsole(console);
    }

    @Test
    public void shouldUpdateConsole() {
        Console console2 = new Console();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = client.createConsole(console2);

        console2.setQuantity(6);
        console2.setPrice(new BigDecimal(289.99));

        client.updateConsole(console2);

        verify(consoleRepository, times(2)).save(any(Console.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateConsoleWithNullModelView() {
        Console console2 = null;

        client.updateConsole(console2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateConsoleWithBadId() {
        Console console2 = new Console();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = client.createConsole(console2);

        console2.setQuantity(6);
        console2.setPrice(new BigDecimal(289.99));

        //change Id to an invalid one.
        console2.setId(console2.getId()+1);

        client.updateConsole(console2);
    }

    @Test
    public void shouldDeleteConsole() {

        Console console2 = new Console();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = client.createConsole(console2);

        client.deleteConsole(console2.getId());

        verify(consoleRepository).deleteById(console2.getId());
    }

    @Test
    public void shouldFindConsoleByManufacturer() {
        List<Console> cvmList = new ArrayList<>();

        Console console2 = new Console();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);

        console2 = client.createConsole(console2);
        cvmList.add(console2);

        Console console3 = new Console();
        console3.setModel("Xbox");
        console3.setManufacturer("Sony");
        console3.setMemoryAmount("256gb");
        console3.setProcessor("Intel I7-9750H");
        console3.setPrice(new BigDecimal("399.99"));
        console3.setQuantity(4);

        console3 = client.createConsole(console3);
        cvmList.add(console3);

        List<Console> cvmFromService = client.getConsoleByManufacturer("Sony");

        assertEquals(cvmList, cvmFromService);
    }

    @Test
    public void shouldFindAllConsoles() throws Exception{
        List<Console> cvmList = new ArrayList<>();

        Console console1 = new Console();
        console1.setModel("Playstation");
        console1.setManufacturer("Sony");
        console1.setMemoryAmount("120gb");
        console1.setProcessor("Intel I7-9750H");
        console1.setPrice(new BigDecimal("299.99"));
        console1.setQuantity(4);

        console1 = client.createConsole(console1);
        cvmList.add(console1);

        Console console2 = new Console();
        console2.setModel("Xbox");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("256gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("399.99"));
        console2.setQuantity(4);

        console2 = client.createConsole(console2);
        cvmList.add(console2);

        Console console3 = new Console();
        console3.setModel("PS III");
        console3.setManufacturer("Sony");
        console3.setMemoryAmount("512Gb");
        console3.setProcessor("AMD I7-9750A");
        console3.setPrice(new BigDecimal("199.99"));
        console3.setQuantity(40);

        console3 = client.createConsole(console3);
        cvmList.add(console3);

        List<Console> cvmFromService = client.getAllConsoles();

        assertEquals(cvmList.size(), cvmFromService.size());
    }

    //Testing TShirt operations...
    @Test
    public void shouldCreateFindTShirt() {
        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);

        TShirt tShirtFromService = client.getTShirt(tShirt.getId());

        assertEquals(tShirt, tShirtFromService);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFaileWhenCreateTShirtWithNullViewModel() {
        TShirt tShirt = null;
        tShirt = client.createTShirt(tShirt);
    }

    @Test
    public void shouldUpdateTShirt() {

        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);

        tShirt.setQuantity(3);
        tShirt.setPrice(new BigDecimal("18.99"));

        client.updateTShirt(tShirt);

        verify(tShirtRepository, times(2)).save(any(TShirt.class));

    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTShirtWithNullViewModel() {

        TShirt tShirt = null;
        client.updateTShirt(tShirt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTShirtWithBadId() {

        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);

        tShirt.setQuantity(3);
        tShirt.setPrice(new BigDecimal("18.99"));

        tShirt.setId(tShirt.getId()+1);
        client.updateTShirt(tShirt);
    }

    @Test
    public void shouldDeleteTShirt() {

        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);

        client.deleteTShirt(tShirt.getId());

        verify(tShirtRepository).deleteById(any(Long.class));

    }

    @Test
    public void shouldFindTShirtByColor() {
        List<TShirt> tvmList = new ArrayList<>();

        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);
        tvmList.add(tShirt);

        TShirt tShirtExtra2 = new TShirt();
        tShirtExtra2.setSize("Large");
        tShirtExtra2.setColor("Blue");
        tShirtExtra2.setDescription("long sleeve");
        tShirtExtra2.setPrice(new BigDecimal("30.99"));
        tShirtExtra2.setQuantity(8);
        tShirtExtra2 = client.createTShirt(tShirtExtra2);
        tvmList.add(tShirtExtra2);

        List<TShirt> tvmFromclient = client.getTShirtByColor("Blue");

        assertEquals(tvmList, tvmFromService);

    }

    @Test
    public void shouldFindTShirtBySize() {
        List<TShirt> tvmList = new ArrayList<>();

        TShirt tShirt = new TShirt();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = client.createTShirt(tShirt);
        tvmList.add(tShirt);

        TShirt tShirtExtra3 = new TShirt();
        tShirtExtra3.setSize("Medium");
        tShirtExtra3.setColor("orange");
        tShirtExtra3.setDescription("sleeveless");
        tShirtExtra3.setPrice(new BigDecimal("15.99"));
        tShirtExtra3.setQuantity(3);
        tShirtExtra3 = client.createTShirt(tShirtExtra3);
        tvmList.add(tShirtExtra3);

        List<TShirt> tvmFromService = client.getTShirtBySize("Medium");

        assertEquals(tvmList, tvmFromService);

    }

    @Test
    public void shouldFindAllTShirts() {
        List<TShirt> tvmList = new ArrayList<>();

        TShirt newTShirt1 = new TShirt();
        newTShirt1.setSize("Medium");
        newTShirt1.setColor("Blue");
        newTShirt1.setDescription("V-Neck");
        newTShirt1.setPrice(new BigDecimal("19.99"));
        newTShirt1.setQuantity(5);

        newTShirt1 = client.createTShirt(newTShirt1);
        tvmList.add(newTShirt1);

        TShirt newTShirt2 = new TShirt();
        newTShirt2.setSize("Large");
        newTShirt2.setColor("Blue");
        newTShirt2.setDescription("long sleeve");
        newTShirt2.setPrice(new BigDecimal("30.99"));
        newTShirt2.setQuantity(8);

        newTShirt2 = client.createTShirt(newTShirt2);
        tvmList.add(newTShirt2);

        TShirt newTShirt3 = new TShirt();
        newTShirt3.setSize("Medium");
        newTShirt3.setColor("orange");
        newTShirt3.setDescription("sleeveless");
        newTShirt3.setPrice(new BigDecimal("15.99"));
        newTShirt3.setQuantity(3);

        newTShirt3 = client.createTShirt(newTShirt3);
        tvmList.add(newTShirt3);

        List<TShirt> tvmFromService = client.getAllTShirts();

        assertEquals(tvmList, tvmFromService);
    }

    //Testing Game operations...
    @Test
    public void shouldCreateFindGame() {

        Game gameViewModel = new Game();
        gameViewModel.setTitle("Halo");
        gameViewModel.setEsrbRating("E10+");
        gameViewModel.setDescription("Puzzles and Math");
        gameViewModel.setPrice(new BigDecimal("23.99"));
        gameViewModel.setStudio("Xbox Game Studios");
        gameViewModel.setQuantity(5);
        gameViewModel = client.createGame(gameViewModel);

        Game gameViewModel2 = client.getGameInfo(32);
        assertEquals(gameViewModel, gameViewModel2);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenCreateGameNullTitle() {

        Game gameViewModel = new Game();
        gameViewModel.setTitle(null);
        gameViewModel.setEsrbRating("E10+");
        gameViewModel.setDescription("Puzzles and Math");
        gameViewModel.setPrice(new BigDecimal("23.99"));
        gameViewModel.setStudio("Xbox Game Studios");
        gameViewModel.setQuantity(5);
        gameViewModel = client.createGame(gameViewModel);

        Game gameViewModel2 = client.getGameInfo(32);

        assertEquals(gameViewModel, gameViewModel2);
    }

    @Test
    public void shouldUpdateGame() {

        Game game = new Game();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = client.createGame(game);

        game.setPrice(new BigDecimal("20.99"));
        game.setQuantity(3);
        client.updateGame(game);

        verify(gameRepository, times(2)).save(any(Game.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateGameInvalidId() {

        Game game = new Game();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = client.createGame(game);

        game.setPrice(new BigDecimal("20.99"));
        game.setQuantity(3);

        //set game id to invalid id...
        game.setId(game.getId()+1);
        client.updateGame(game);

        System.out.println(game);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateGameNullViewModel() {
        client.updateGame(null);
    }

    @Test
    public void shouldDeleteGame() {

        Game game = new Game();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = client.createGame(game);

        client.deleteGame(game.getId());

        verify(gameRepository).deleteById(any(Long.class));

    }

    @Test
    public void shouldFindGameByEsrb() {

        List<Game> gamesByEsrb = new ArrayList<>();

        Game game1 = new Game();
        game1.setTitle("Halo");
        game1.setEsrbRating("E10+");
        game1.setDescription("Puzzles and Math");
        game1.setPrice(new BigDecimal("23.99"));
        game1.setStudio("Xbox Game Studios");
        game1.setQuantity(5);
        game1 = client.createGame(game1);
        gamesByEsrb.add(game1);

        Game gameExtra = new Game();
        gameExtra.setTitle("Tetris");
        gameExtra.setEsrbRating("E10+");
        gameExtra.setDescription("block puzzle game");
        gameExtra.setPrice(new BigDecimal("10.99"));
        gameExtra.setStudio("Dolby Studios");
        gameExtra.setQuantity(9);
        gameExtra = client.createGame(gameExtra);
        gamesByEsrb.add(gameExtra);

        List<Game> gvmFromService = client.getGameByEsrb("E10+");

        assertEquals(gamesByEsrb, gvmFromService);

        //Test Esrb with no games...
        gvmFromService = client.getGameByEsrb("E18+");
        assertEquals(gvmFromService.size(),0);

    }

    @Test
    public void shouldFindGameByTitle() {
        List<Game> gvmList = new ArrayList<>();

        Game game = new Game();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = client.createGame(game);
        gvmList.add(game);

        Game game2 = new Game();
        game2.setTitle("Fort Lines");
        game2.setEsrbRating("M");
        game2.setDescription("Zombie shooter game");
        game2.setPrice(new BigDecimal("37.99"));
        game2.setStudio("Dolby Studios");
        game2.setQuantity(3);
        game2 = client.createGame(game2);
        gvmList.add(game2);

        List<Game> gvmFromService = client.getGameByTitle("Halo");

        //Test title with no games...
        gvmFromService = client.getGameByTitle("Shalo");
        assertEquals(gvmFromService.size(),0);
    }

    @Test
    public void shouldFindGameByStudio() {
        List<Game> gvmList = new ArrayList<>();

        Game gameExtra2 = new Game();
        gameExtra2.setTitle("Tetris");
        gameExtra2.setEsrbRating("E10+");
        gameExtra2.setDescription("block puzzle game");
        gameExtra2.setPrice(new BigDecimal("10.99"));
        gameExtra2.setStudio("Dolby Studios");
        gameExtra2.setQuantity(9);
        gameExtra2 = client.createGame(gameExtra2);
        gvmList.add(gameExtra2);

        Game gameExtra3 = new Game();
        gameExtra3.setTitle("Fort Lines");
        gameExtra3.setEsrbRating("M");
        gameExtra3.setDescription("Zombie shooter game");
        gameExtra3.setPrice(new BigDecimal("37.99"));
        gameExtra3.setStudio("Dolby Studios");
        gameExtra3.setQuantity(3);
        gameExtra3 = client.createGame(gameExtra3);
        gvmList.add(gameExtra3);

        List<Game> gvmFromService = client.getGameByStudio("Dolby Studios");
        assertEquals(gvmList, gvmFromService);

        //Test title with no games...
        gvmFromService = client.getGameByStudio("EA");
        assertEquals(gvmFromService.size(),0);
    }

    @Test
    public void shouldFindAllGames() {
        List<Game> gvmList = new ArrayList<>();

        Game newGame1 = new Game();
        newGame1.setTitle("Halo");
        newGame1.setEsrbRating("E10+");
        newGame1.setDescription("Puzzles and Math");
        newGame1.setPrice(new BigDecimal("23.99"));
        newGame1.setStudio("Xbox Game Studios");
        newGame1.setQuantity(5);

        newGame1 = client.createGame(newGame1);
        gvmList.add(newGame1);

        Game newGame2 = new Game();
        newGame2.setTitle("Tetris");
        newGame2.setEsrbRating("E10+");
        newGame2.setDescription("block puzzle game");
        newGame2.setPrice(new BigDecimal("10.99"));
        newGame2.setStudio("Dolby Studios");
        newGame2.setQuantity(9);

        newGame2 = client.createGame(newGame2);
        gvmList.add(newGame2);

        Game newGame3 = new Game();
        newGame3.setTitle("Fort Lines");
        newGame3.setEsrbRating("M");
        newGame3.setDescription("Zombie shooter game");
        newGame3.setPrice(new BigDecimal("37.99"));
        newGame3.setStudio("Dolby Studios");
        newGame3.setQuantity(3);
        newGame3 = client.createGame(newGame3);
        gvmList.add(newGame3);

        List<Game> gvmFromService = client.getAllGames();
        assertEquals(gvmList, gvmFromService);

    }

    //DAO Mocks...
    private void setUpInvoiceRepositoryMock() {
        invoiceRepository = mock(InvoiceRepository.class);

        Invoice invoice = new Invoice();
        invoice.setName("John Jake");
        invoice.setStreet("street");
        invoice.setCity("Charlotte");
        invoice.setState("NC");
        invoice.setZipcode("83749");
        invoice.setItemType("T-Shirt");
        invoice.setItemId(54);
        invoice.setUnitPrice(new BigDecimal("19.99"));
        invoice.setQuantity(2);
        invoice.setSubtotal(new BigDecimal("39.98"));
        invoice.setTax(new BigDecimal("2"));
        invoice.setProcessingFee(new BigDecimal("1.98"));
        invoice.setTotal(new BigDecimal("43.96"));

        Invoice invoice1 = new Invoice();
        invoice1.setId(20);
        invoice1.setName("John Jake");
        invoice1.setStreet("street");
        invoice1.setCity("Charlotte");
        invoice1.setState("NC");
        invoice1.setZipcode("83749");
        invoice1.setItemType("T-Shirt");
        invoice1.setItemId(54);
        invoice1.setUnitPrice(new BigDecimal("19.99"));
        invoice1.setQuantity(2);
        invoice1.setSubtotal(new BigDecimal("39.98"));
        invoice1.setTax(new BigDecimal("2"));
        invoice1.setProcessingFee(new BigDecimal("1.98"));
        invoice1.setTotal(new BigDecimal("43.96"));

        doReturn(invoice1).when(invoiceRepository).save(invoice);
        doReturn(Optional.of(invoice1)).when(invoiceRepository).findById(20L);

        //Get All...
        Invoice savedInvoice1 = new Invoice();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        Invoice savedInvoice2 = new Invoice();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        Invoice savedInvoice3 = new Invoice();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<Invoice> allList = new ArrayList<>();
        allList.add(savedInvoice1);
        allList.add(savedInvoice2);
        allList.add(savedInvoice3);

        doReturn(allList).when(invoiceRepository).findAll();
    }

    private void setUpProcessingFeeRepositoryMock() {

        processingFeeRepository = mock(ProcessingFeeRepository.class);

        ProcessingFee processingFee = new ProcessingFee();
        processingFee.setFee(new BigDecimal("1.98"));
        processingFee.setProductType("T-Shirt");

        doReturn(Optional.of(processingFee)).when(processingFeeRepository).findById("T-Shirt");

    }

    private void setUpTaxRepositoryMock() {
        taxRepository = mock(TaxRepository.class);

        Tax taxNC = new Tax();
        taxNC.setRate(new BigDecimal(".05"));
        taxNC.setState("NC");

        Tax taxNY = new Tax();
        taxNY.setRate(BigDecimal.ZERO);
        taxNY.setState("NY");

        doReturn(Optional.of(taxNC)).when(taxRepository).findById("NC");
        doReturn(Optional.of(taxNY)).when(taxRepository).findById("NY");

    }
    private void setUpclientMock() {
        game = new Game();
        game.setTitle("Fun Game");
        game.setEsrbRating("Everyone");
        game.setPrice(new BigDecimal(10.00));
        game.setStudio("New Studio");
        game.setQuantity(10);

        doReturn(game).when(client).createGame(game);
        game = client.createGame(game);
        when(client.getGameInfo(game.getId())).thenReturn(game);

    }


}