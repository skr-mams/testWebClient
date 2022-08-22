package services.reactorUnit;

import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.adapter.DynamicRequest;
import services.adapter.ResponseClientHelper;
import services.businessUnit.CustomerUnit;
import services.models.httpMethods.Success;

/**
 * @author Mario Manzanarez
 * @since 8/22/22
 */
public class CustomerReactor {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerReactor.class);

    public static void getCustomer(RoutingContext ctx) {
        var idCustomer = DynamicRequest.getIdToReactiveEntity(ctx);
        CustomerUnit.getCustomer(idCustomer)
                .onFailure(fail -> ResponseClientHelper.responseFailureDynamicObject(ctx, fail, "No se pudo recuperar el cliente"))
                .onSuccess(customer -> ResponseClientHelper
                        .responseDynamicObject(ctx, Success.OK, "Información del cliente", customer));
    }
}
