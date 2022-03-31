package aa.api.dialer.service.cli;

import com.ringcentral.RestClient;
import java.util.List;

public interface EventSubscriptionService {
  void create(
      final RestClient authenticatedClient,
      final List<String> events,
      final long userExtensionId
  );
}
