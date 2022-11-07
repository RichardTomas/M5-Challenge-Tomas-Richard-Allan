package com.trilogyed.gamestoreinvoicing.repository;

import com.trilogyed.gamestoreinvoicing.config.util.feign.GameStoreCatalogClient;
import com.trilogyed.gamestoreinvoicing.model.Invoice;
import com.trilogyed.gamestoreinvoicing.model.ProcessingFee;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import com.trilogyed.gamestoreinvoicing.model.Tax;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class InvoiceRepositoryTest {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    TaxRepository taxRepository;
    @Autowired
    ProcessingFeeRepository processingFeeRepository;

    @Before
    public void setUp() throws Exception {

        invoiceRepository.deleteAll();
        processingFeeRepository.deleteAll();

        ProcessingFee tShirtProcessingFee = new ProcessingFee();
        tShirtProcessingFee.setProductType("T-Shirts");
        tShirtProcessingFee.setFee(new BigDecimal("1.98"));

        ProcessingFee consoleProcessingFee = new ProcessingFee();
        consoleProcessingFee.setProductType("Consoles");
        consoleProcessingFee.setFee(new BigDecimal("14.99"));

        ProcessingFee gameProcessingFee = new ProcessingFee();
        gameProcessingFee.setProductType("Games");
        gameProcessingFee.setFee(new BigDecimal("1.49"));

        processingFeeRepository.save(tShirtProcessingFee);
        processingFeeRepository.save(consoleProcessingFee);
        processingFeeRepository.save(gameProcessingFee);
    }

    @Test
    public void shouldAddFindDeleteInvoice() {
        // Code referenced from example in class November 3, 2022
        // Build an invoice
        Invoice invoice = new Invoice();
        invoice.setName("Joe Black");
        invoice.setStreet("123 Main St");
        invoice.setCity("any City");
        invoice.setState("NY");
        invoice.setState("NY");
        invoice.setItemType("T-Shirts");
        invoice.setItemId(1);
        invoice.setUnitPrice(new BigDecimal(14.95));
        invoice.setQuantity(2);
        invoice.setTax(new BigDecimal("0.06"));
        invoice.setProcessingFee(new BigDecimal("1.99"));
        invoice.setTotal(new BigDecimal("30.00"));

        // save to database
        invoice = invoiceRepository.save(invoice);

        // get it back out of the database
        Invoice invoice2 = invoiceRepository.findById(invoice.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(invoice, invoice2);

        // delete it
        invoiceRepository.deleteById(invoice.getId());

        // go try to get it again
        Optional<Invoice> invoice3 = invoiceRepository.findById(invoice.getId());

        // confirm that it's gone
        assertEquals(false, invoice3.isPresent());
    }

    @Test
    public void shouldFindByName() {

        Invoice invoice = new Invoice();
        invoice.setName("Joe Black");
        invoice.setStreet("123 Main St");
        invoice.setCity("any City");
        invoice.setState("NY");
        invoice.setState("NY");
        invoice.setItemType("T-Shirts");
        invoice.setItemId(1);
        invoice.setUnitPrice(new BigDecimal(14.95));
        invoice.setQuantity(2);
        invoice.setTax(new BigDecimal("0.06"));
        invoice.setProcessingFee(new BigDecimal("1.99"));
        invoice.setTotal(new BigDecimal("30.00"));

        //Act
        invoice = invoiceRepository.save(invoice);

        List<Invoice> foundNoinvoice = invoiceRepository.findByName("invalidValue");

        List<Invoice> foundOneinvoice = invoiceRepository.findByName(invoice.getName());

        //Assert
        assertEquals(foundOneinvoice.size(),1);

        //Assert
        assertEquals(foundNoinvoice.size(),0);
    }
}