package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.futurecollars.invoicing.model.Invoice;

@CrossOrigin
@RequestMapping(value = "invoices", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"invoices"})
public interface InvoiceApi {

    @GetMapping
    @ApiOperation("Get all invoices")
    List<Invoice> getAll();

    @PostMapping
    @ApiOperation("Add new invoice to system")
    long add(@RequestBody Invoice invoice);

    @GetMapping(value = "/{id}")
    @ApiOperation("Get invoice by id")
    ResponseEntity<Invoice> getById(@PathVariable int id);

    @DeleteMapping("/{id}")
    @ApiOperation("Delete invoice with given id")
    ResponseEntity<?> deleteById(@PathVariable int id);

    @PutMapping("/{id}")
    @ApiOperation("Update invoice with given id")
    ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice updatedInvoice);
}
