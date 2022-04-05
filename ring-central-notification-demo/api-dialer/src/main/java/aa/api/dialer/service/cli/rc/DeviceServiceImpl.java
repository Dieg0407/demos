package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.ExtensionDeviceResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DeviceServiceImpl implements DeviceService {

  @Override
  public List<ExtensionDeviceResponse> getExtensionDevices(RestClient authenticatedClient) {
    try {
      return Arrays.stream(
          authenticatedClient
              .restapi()
              .account()
              .extension()
              .device()
              .get()
              .records
          ).collect(Collectors.toList());
    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to fetch the user devices",
          e
      );
    }
  }
}
