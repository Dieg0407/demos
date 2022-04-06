package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.GetPresenceInfo;
import com.ringcentral.definitions.PresenceInfoRequest;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PresenceServiceImpl implements PresenceService {

  @Override
  public GetPresenceInfo getPresence(RestClient authenticatedClient) {
    try {
      return authenticatedClient.restapi().account()
          .extension()
          .presence()
          .get();
    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to fetch the presence of the user",
          e
      );
    }
  }

  @Override
  public void updatePresence(RestClient authenticatedClient, PresenceInfoRequest request) {
    try {
      authenticatedClient.restapi().account()
          .extension()
          .presence()
          .put(request);
    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to update the presence of the user",
          e
      );
    }
  }
}
