package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import java.util.List;

public interface EventSubscriptionService {
  void create(
      final List<String> events,
      final long userExtensionId
  );
  void create(
      final RestClient userClient,
      final List<String> events,
      final long userExtensionId
  );
}
