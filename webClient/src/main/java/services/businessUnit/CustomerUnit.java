package services.businessUnit;

import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.models.Customer;
import services.sources.webclient.send.CustomerClient;

/**
 * @author Mario Manzanarez
 * @since 8/22/22
 */
public class CustomerUnit {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerUnit.class);

    public static Future<Customer> getCustomer(long idCustomer) {
        return Future.future(promise -> CustomerClient.getCustomer(idCustomer)
                .onFailure(promise::fail)
                .onSuccess(customer -> {
                    switch (customer.getPersonType()) {
                        case "M":
                            customer.setCompany("Compañía");
                            break;
                        case "F":
                            customer.setName("Nombre");
                            break;
                    }
                    promise.complete(customer);
                })

        );
    }
}
