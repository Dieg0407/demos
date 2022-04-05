package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.definitions.GetExtensionInfoResponse;

public interface ExtensionService {
  GetExtensionInfoResponse findLoggedInUserExtension(RestClient authenticatedClient);

  GetExtensionInfoResponse findExtensionInfo(RestClient authenticatedClient, String extensionId);
}
