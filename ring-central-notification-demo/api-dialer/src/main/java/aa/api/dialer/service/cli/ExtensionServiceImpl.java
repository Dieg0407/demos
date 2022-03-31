package aa.api.dialer.service.cli;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.GetExtensionInfoResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExtensionServiceImpl implements ExtensionService {

  @Override
  public GetExtensionInfoResponse findLoggedInUserExtension(RestClient authenticatedClient) {
    try {
      return authenticatedClient.restapi().account()
          .extension("~")
          .get();
    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to fetch the extension information",
          e
      );
    }
  }
}