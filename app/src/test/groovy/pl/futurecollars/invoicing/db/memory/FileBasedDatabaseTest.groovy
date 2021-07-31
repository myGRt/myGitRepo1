package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService
import java.nio.file.Files
import static pl.futurecollars.invoicing.TestHelpers.invoice

class FileBasedDatabaseTest extends DatabaseTest{

    def dbPath

    @Override
    Database getDatabaseInstance() {

        def fileService = new FileService()
        def idPath = File.createTempFile('ids', '.txt').toPath()
        def idService = new IdService(idPath, fileService)

        dbPath = File.createTempFile('invoices', '.txt').toPath()
        return new FileBasedDatabase(dbPath, idService, fileService, new JsonService())
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(invoice(4))

        then:
        1 == Files.readAllLines(dbPath).size()

        when:
        db.save(invoice(5))

        then:
        2 == Files.readAllLines(dbPath).size()
    }
}
