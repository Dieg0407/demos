package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.definitions.ExtensionDeviceResponse;
import java.util.List;

public interface DeviceService {
  List<ExtensionDeviceResponse> getExtensionDevices(RestClient authenticatedClient);
}
