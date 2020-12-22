package com.backbase.dbs.product.arrangement;

import com.backbase.buildingblocks.backend.security.accesscontrol.accesscontrol.AccessControlValidator;
import com.backbase.dbs.product.Configurations;
import com.backbase.dbs.product.balance.BalanceService;
import com.backbase.dbs.product.clients.AccessControlClient;
import com.backbase.dbs.product.clients.JwtContext;
import com.backbase.dbs.product.common.clients.AccountOutboundWrapper;
import com.backbase.dbs.product.repository.ArrangementJpaRepository;
import com.backbase.dbs.product.repository.ProductJpaRepository;
import com.backbase.dbs.product.repository.arrangement.ArrangementSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * Sample class extending getArrangementsByBusinessFunction operation.
 */
@Primary
@Service
public class ExtendedPermissionAwareArrangementService extends PermissionAwareArrangementService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtendedPermissionAwareArrangementService.class);
    private static final String[] LOCALES = Locale.getISOCountries();

    public ExtendedPermissionAwareArrangementService(
            Configurations configurations, ArrangementService arrangementService, JwtContext jwtContext,
            AccessControlClient accessControlClient, AccountOutboundWrapper accountOutboundWrapper,
            ArrangementUpdater arrangementUpdater, ArrangementJpaRepository arrangementJpaRepository,
            ProductJpaRepository productJpaRepository, BalanceService balanceService,
            AccessControlValidator accessControlValidator, DebitCardCommandMapper debitCardCommandMapper) {
        super(configurations, arrangementService, jwtContext, accessControlClient, accountOutboundWrapper,
                arrangementUpdater, arrangementJpaRepository, productJpaRepository,
                balanceService, accessControlValidator, debitCardCommandMapper);
        LOG.info("Extended service has been created");
    }

    /**
     * This is just an example.
     */
    @Override
    public GetArrangementsByBusinessFunctionResult getArrangementsByBusinessFunction(
        @NotNull ArrangementSearchParams arrangementSearchParams, Boolean withLatestBalances, String privilege,
        String[] businessFunction, String resourceName) {

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
