package com.backbase.dbs.product.arrangement;

import com.backbase.buildingblocks.backend.security.accesscontrol.accesscontrol.AccessControlValidator;
import com.backbase.dbs.product.Configurations;
import com.backbase.dbs.product.ProductKindStorage;
import com.backbase.dbs.product.balance.BalanceService;
import com.backbase.dbs.product.clients.AccessControlClient;
import com.backbase.dbs.product.clients.JwtContext;
import com.backbase.dbs.product.common.clients.AccountOutboundWrapper;
import com.backbase.dbs.product.repository.ArrangementJpaRepository;
import com.backbase.dbs.product.repository.ProductJpaRepository;
import com.backbase.dbs.product.repository.arrangement.ArrangementSearchParams;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Sample class extending getArrangementsByBusinessFunction operation.
 */
@Primary
@Service
@Slf4j
public class ExtendedPermissionAwareArrangementService extends PermissionAwareArrangementService {

    private static final String[] LOCALES = Locale.getISOCountries();

    public ExtendedPermissionAwareArrangementService(Configurations configurations, ProductKindStorage kindStorage,
        ArrangementService arrangementService, JwtContext jwtContext, AccessControlClient accessControlClient,
        AccountOutboundWrapper accountOutboundWrapper, ArrangementUpdater arrangementUpdater,
        ArrangementJpaRepository arrangementJpaRepository, ProductJpaRepository productJpaRepository,
        BalanceService balanceService, AccessControlValidator accessControlValidator,
        DebitCardCommandMapper debitCardCommandMapper) {
        super(configurations, kindStorage, arrangementService, jwtContext, accessControlClient, accountOutboundWrapper,
            arrangementUpdater, arrangementJpaRepository, productJpaRepository, balanceService, accessControlValidator,
            debitCardCommandMapper);
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

        final var result = super.getArrangementsByBusinessFunction(
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
            final var countryName = new Locale("en",
                LOCALES[(int) (Math.random() * LOCALES.length)]).getDisplayCountry();

            arrangement.setAccountHolderCountry(countryName);
            arrangement.setAccountHolderStreetName("123 Fake Street");

            LOG.info("Update holder location updated to {} {}",
                arrangement.getAccountHolderCountry(), arrangement.getAccountHolderStreetName());
        });
    }
}
