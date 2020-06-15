package com.backbase.presentation.productsummary.extension.simple;

import com.backbase.buildingblocks.backend.communication.extension.annotations.BehaviorExtension;
import com.backbase.buildingblocks.backend.communication.extension.annotations.PostHook;
import com.backbase.buildingblocks.backend.communication.extension.annotations.PreHook;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.presentation.productsummary.rest.spec.v2.productsummary.ArrangementsByBusinessFunctionGetResponseBody;
import com.backbase.presentation.productsummary.util.PagedList;
import java.util.Locale;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example of how to provide a behavior extension using annotations.
 */
@BehaviorExtension(
    // The name parameter is used as a configuration key to enable/disable this specific extension.
    // For example, "backbase.behavior-extensions.enrich-location-information.enabled=false".
    // (Extensions are enabled by default.)
    name = "enrich-location-information",
    // The routeId parameter is the value returned by the getRouteId() method of the target SimpleExtensibleRouteBuilder
    // and is typically exposed as a constant by that route builder.  E.g.:
    routeId = "ListArrangementsByContextRoute"
)
public class ExampleBehaviorExtension {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleBehaviorExtension.class);
    private static final String[] LOCALES = Locale.getISOCountries();

    @PreHook
    public void examplePreHook(Object body, Exchange exchange) {
        LOG.info("Pre hook");
    }

    @PostHook
    public void enrichLocationInformation(InternalRequest<PagedList<ArrangementsByBusinessFunctionGetResponseBody>> body) {
        LOG.info("Post hook");
        updateHolderLocationInfo(body.getData());
    }

    /**
     * Updates holder location info with random country.
     */
    private void updateHolderLocationInfo(PagedList<ArrangementsByBusinessFunctionGetResponseBody> result) {
        result.getList().forEach(arrangement -> {
            String countryName = new Locale("en",
                LOCALES[(int) (Math.random() * LOCALES.length)]).getDisplayCountry();
            arrangement.setAccountHolderCountry(countryName);
            arrangement.setAccountHolderStreetName("123 Fake Street");
        });
    }

}
