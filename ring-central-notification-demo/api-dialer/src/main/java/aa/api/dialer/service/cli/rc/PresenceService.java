package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.definitions.GetPresenceInfo;
import com.ringcentral.definitions.PresenceInfoRequest;

public interface PresenceService {
  GetPresenceInfo getPresence(RestClient authenticatedClient);

  void updatePresence(RestClient authenticatedClient, PresenceInfoRequest request);
}
