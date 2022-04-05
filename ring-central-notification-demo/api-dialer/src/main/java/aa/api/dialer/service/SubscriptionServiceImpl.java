package aa.api.dialer.service;

import static java.lang.String.format;

import aa.api.dialer.service.cli.rc.EventSubscriptionService;
import aa.api.dialer.service.cli.rc.ExtensionService;
import aa.api.dialer.service.factory.RestClientFactory;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
  private final RestClientFactory factory;

  private final ExtensionService extensionService;
  private final EventSubscriptionService eventSubscriptionService;

  @Override
  public void create(String authorizationToken) {
    final var authenticatedClient = factory.createClient(authorizationToken);
    final var userExtension = extensionService.findLoggedInUserExtension(authenticatedClient);

    final var events = Arrays.asList(
        format("/restapi/v1.0/account/%s/extension/%s/telephony/sessions?direction=Inbound", userExtension.account.id, userExtension.id)
    );

    eventSubscriptionService.create(authenticatedClient, events, userExtension.id);
  }
}
