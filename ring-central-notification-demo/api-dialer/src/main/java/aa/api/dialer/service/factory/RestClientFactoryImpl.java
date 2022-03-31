package aa.api.dialer.service.factory;

import aa.api.dialer.conf.properties.AppProps;
import com.ringcentral.RestClient;
import com.ringcentral.definitions.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
