package pl.futurecollars.invoicing.db.memory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

    private final Path invoicesPath;
    private final IdService idService;
    private final FileService fileService;
    private final JsonService jsonService;

    @Override
    public int save(Invoice invoice) {
        try {
            invoice.setId(idService.getNextId());
            fileService.appendLineToFile(invoicesPath, jsonService.objectToJson(invoice));
            return invoice.getId();
        } catch (IOException exception) {
            throw new RuntimeException("Database failed to save invoice", exception);
        }
    }

    @Override
    public Optional<Invoice> getById(int id) {
        try {
            return fileService.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> containsId(line, id))
                    .map(line -> jsonService.stringToObject(line, Invoice.class))
                    .findFirst();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to get invoice with id: " + id, exception);
        }
    }

    @Override
    public List<Invoice> getAll() {
        try {
            return fileService.readAllLines(invoicesPath)
                    .stream()
                    .map(line -> jsonService.stringToObject(line, Invoice.class))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read invoices from file", exception);
        }
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        Optional<Invoice> toUpdate = getById(id);

        updatedInvoice.setId(id);

        try {
            List<String> updatedInvoicesList = fileService.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());
            updatedInvoicesList.add(jsonService.objectToJson(updatedInvoice));

            fileService.writeLinesToFile(invoicesPath, updatedInvoicesList);
            return toUpdate;

        } catch (IOException exception) {
            throw new RuntimeException("Failed to update invoice with id: " + id, exception);
        }
    }

    @Override
    public Optional<Invoice> delete(int id) {
        Optional<Invoice> toDelete = getById(id);
        try {
            List<String> updatedInvoicesList = fileService.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());

            fileService.writeLinesToFile(invoicesPath, updatedInvoicesList);
            return toDelete;

        } catch (IOException exception) {
            throw new RuntimeException("Failed to delete invoice with id: " + id, exception);
        }
    }

    private boolean containsId(String line, int id) {
        return line.contains("\"id\":" + id + ",");
    }
}
