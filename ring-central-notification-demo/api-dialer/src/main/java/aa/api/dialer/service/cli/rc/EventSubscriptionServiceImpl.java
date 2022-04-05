package aa.api.dialer.service.cli.rc;

import static java.lang.String.format;

import aa.api.dialer.conf.properties.AppProps;
import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.CreateSubscriptionRequest;
import com.ringcentral.definitions.ModifySubscriptionRequest;
import com.ringcentral.definitions.NotificationDeliveryModeRequest;
import com.ringcentral.definitions.SubscriptionInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSubscriptionServiceImpl implements EventSubscriptionService {
  private final AppProps properties;
  private final RestClient mainAccountClient;

  @Override
  public void create(RestClient userClient, List<String> events,
      long userExtensionId) {
    final var hookEndpoint = format("%s/%d", properties.getHookUrl(), userExtensionId);
    try {
      final var subscriptions = userClient.restapi()
          .subscription()
          .list()
          .records;

      final var affectedSubscriptions = Arrays.stream(subscriptions)
          .filter(sub -> isAffectedSubscription(sub, hookEndpoint))
          .collect(Collectors.toList());

      if (affectedSubscriptions.isEmpty())
        createNewSub(userClient, events, hookEndpoint);
      else
        refreshAffectedSubscriptions(userClient, events, affectedSubscriptions, hookEndpoint);

    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to fetch the extension information",
          e
      );
    }
  }

  @Override
  public void create(final List<String> events, final long userExtensionId) {
    create(mainAccountClient, events, userExtensionId);
  }

  private void refreshAffectedSubscriptions(
      final RestClient authenticatedClient,
      final List<String> events,
      final List<SubscriptionInfo> affectedSubscriptions,
      final String hookEndpoint
  ) throws RestException, IOException {

    // find if the sub with the same events
    final var validSubs = affectedSubscriptions.stream()
        .filter(sub -> {
          final var subEvents = Arrays.stream(sub.eventFilters).sorted().toArray(String[]::new);
          final var sortedEvents = events.stream().sorted().toArray(String[]::new);

          return Arrays.equals(subEvents, sortedEvents);
        })
        .collect(Collectors.toList());

    final var toDelete = new ArrayList<SubscriptionInfo>();

    // no sub matches the events
    if (validSubs.isEmpty()) {
      createNewSub(authenticatedClient, events, hookEndpoint);
      toDelete.addAll(affectedSubscriptions);
    }
    else {
      final var first = validSubs.get(0);
      authenticatedClient.restapi()
          .subscription(first.id)
          .put(new ModifySubscriptionRequest().expiresIn(properties.getRingCentral().getSubscriptionTtl()));

      log.info("Subscription with id {} refreshed", first.id);

      toDelete.addAll(
          affectedSubscriptions.stream()
              .filter(i -> !Objects.equals(i.id, first.id))
              .collect(Collectors.toList())
      );
    }

    for(var subToDelete : toDelete)
      authenticatedClient.restapi()
          .subscription(subToDelete.id)
          .delete();
  }

  private void createNewSub(final RestClient authenticatedClient, final List<String> events, final String hookEndpoint)
      throws RestException, IOException {

    final var request = new CreateSubscriptionRequest()
        .deliveryMode(
            new NotificationDeliveryModeRequest()
                .address(hookEndpoint)
                .transportType("WebHook")
        )
        .eventFilters(events.toArray(String[]::new))
        .expiresIn(properties.getRingCentral().getSubscriptionTtl());

    final var response = authenticatedClient.restapi()
        .subscription()
        .post(request);

    log.info("Subscription created: " + response.id);
  }

  private boolean isAffectedSubscription(SubscriptionInfo sub, String hookEndpoint) {
    if (sub.deliveryMode == null)
      return false;

    if (!"WebHook".equals(sub.deliveryMode.transportType))
      return false;

    return hookEndpoint.equals(sub.deliveryMode.address);
  }
}
