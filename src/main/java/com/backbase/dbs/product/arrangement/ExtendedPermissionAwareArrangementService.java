package com.backbase.dbs.product.arrangement;

import com.backbase.buildingblocks.backend.security.accesscontrol.accesscontrol.AccessControlValidator;
import com.backbase.dbs.product.balance.BalanceService;
import com.backbase.dbs.product.common.Configurations;
import com.backbase.dbs.product.common.clients.AccessGroupClient;
import com.backbase.dbs.product.common.clients.AccountIntegrationClient;
import com.backbase.dbs.product.common.clients.LegalEntityClient;
import com.backbase.dbs.product.common.clients.UserClient;
import com.backbase.dbs.product.repository.ArrangementJpaRepository;
import com.backbase.dbs.product.repository.ProductJpaRepository;
import com.backbase.dbs.product.repository.arrangement.ArrangementSearchParams;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Sample class extending getArrangementsByBusinessFunction operation.
 */
@Primary
@Service
public class ExtendedPermissionAwareArrangementService extends PermissionAwareArrangementService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtendedPermissionAwareArrangementService.class);
    private static final String[] LOCALES = Locale.getISOCountries();

    public ExtendedPermissionAwareArrangementService(Configurations configurations, ArrangementService arrangementService,
        UserClient userClient, AccessGroupClient accessGroupClient,
        AccountIntegrationClient accountIntegrationClient,
        ArrangementUpdater arrangementUpdater,
        ArrangementJpaRepository arrangementJpaRepository,
        LegalEntityClient legalEntityClient, ProductJpaRepository productJpaRepository,
        BalanceService balanceService,
        AccessControlValidator accessControlValidator) {
        super(configurations, arrangementService, userClient, accessGroupClient, accountIntegrationClient, arrangementUpdater,
            arrangementJpaRepository, legalEntityClient, productJpaRepository, balanceService, accessControlValidator);
        LOG.info("Extended service has been created");
    }

    /**
     * This is just an example.
     */
    @Override
    public GetArrangementsByBusinessFunctionResult getArrangementsByBusinessFunction(
        @NotNull ArrangementSearchParams arrangementSearchParams, Boolean withLatestBalances, String privilege,
        String businessFunction, String resourceName) {

        LOG.info("Pre hook");

        final GetArrangementsByBusinessFunctionResult result = super.getArrangementsByBusinessFunction(
            arrangementSearchParams, withLatestBalances, privilege, businessFunction, resourceName);

        LOG.info("Post hook");

        updateHolderLocationInfo(result);

        return result;
    }

    /**
     * Updates holder location info with random country.
     */
    private void updateHolderLocationInfo(GetArrangementsByBusinessFunctionResult result) {
        result.getArrangements().forEach(arrangement -> {
            final String countryName = new Locale("en",
                LOCALES[(int) (Math.random() * LOCALES.length)]).getDisplayCountry();

            arrangement.setAccountHolderCountry(countryName);
            arrangement.setAccountHolderStreetName("123 Fake Street");
            LOG.info("Update holder location updated to {} {}",
                arrangement.getAccountHolderCountry(), arrangement.getAccountHolderStreetName());
        });
    }
}
