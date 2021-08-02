package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@AutoConfigureMockMvc
@SpringBootTest
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def "empty array is returned when no invoices were added"() {

        expect:
        true
    }
    def "empty array is returned when no invoices were created"() {
        expect:
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
    }

    def "empty array is returned when no invoices were created2"() {
        expect:
        mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == "[]"
    }

    def "empty array is returned when no invoices were added2"() {
        expect:
        getAllInvoices() == []
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.stringToObject(response, Invoice[])
    }


}
