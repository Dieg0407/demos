package aa.api.dialer.service.factory;

import aa.api.dialer.conf.properties.AppProps;
import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.TokenInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RestClientFactoryImpl implements RestClientFactory {
  private final AppProps properties;

  @Override
  public RestClient createClient(String authenticationToken) {
    final var client = new RestClient("", "", properties.getRingCentral().getUrl());
    client.token = new TokenInfo().access_token(authenticationToken);

    return client;
  }

  @Override
  public RestClient createMainClient() {
    final var rc = properties.getRingCentral();
    final var client = new RestClient(
        rc.getMainAccount().getClientId(),
        rc.getMainAccount().getClientSecret(),
        rc.getUrl()
    );

    try {
      if (rc.getMainAccount().getJwt() != null)
        client.authorize(rc.getMainAccount().getJwt());
      else
        client.authorize(rc.getMainAccount().getUsername(), rc.getMainAccount().getExtension(), rc.getMainAccount().getPassword());

      return client;
    } catch (IOException | RestException e) {
      final var message = "Login failed to RingCentral with stored credentials for tha main account";
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED,
          message,
          e
      );
    }
  }
}
