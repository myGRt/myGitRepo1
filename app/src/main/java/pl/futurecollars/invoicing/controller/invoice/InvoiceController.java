package pl.futurecollars.invoicing.controller.invoice;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@AllArgsConstructor
@RestController
@RequestMapping("invoices")
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAll();
    }

    @Override
    public int addInvoice(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        return invoiceService.getById(id)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return invoiceService.delete(id)
                .map(invoice -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Invoice updatedInvoice) {
        return invoiceService.update(id, updatedInvoice)
                .map(invoice -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
