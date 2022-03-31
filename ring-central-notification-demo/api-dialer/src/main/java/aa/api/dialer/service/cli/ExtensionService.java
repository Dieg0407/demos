package aa.api.dialer.service.cli;

import com.ringcentral.RestClient;
import com.ringcentral.definitions.GetExtensionInfoResponse;

public interface ExtensionService {
  GetExtensionInfoResponse findLoggedInUserExtension(RestClient authenticatedClient);
}
