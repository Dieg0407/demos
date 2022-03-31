package aa.api.dialer.service.factory;

import com.ringcentral.RestClient;

public interface RestClientFactory {
  RestClient createClient(String authenticationToken);

  RestClient createMainClient();
}
