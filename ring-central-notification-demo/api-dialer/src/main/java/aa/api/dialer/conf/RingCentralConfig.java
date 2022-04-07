package aa.api.dialer.conf;

import aa.api.dialer.conf.properties.AppProps;
import aa.api.dialer.service.factory.RestClientFactory;
import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.GetTokenRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
public class RingCentralConfig {

  @Autowired
  RestClientFactory factory;

  @Bean("mainAccount")
  public RestClient mainAccount() {
    return factory.createMainClient();
  }

  @Slf4j
  @Component
  public static class Timer {
    @Qualifier("mainAccount")
    @Autowired
    private RestClient client;

    @Autowired
    private AppProps props;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void authenticate() {
      final var rc = props.getRingCentral();
      try {
        final var auth = new GetTokenRequest();
        auth.grant_type("refresh_token")
            .endpoint_id(client.token.endpoint_id)
            .refresh_token(client.token.refresh_token);

        client.authorize(auth);
      }
      catch (RestException | IOException e) {
        log.error("Failed to re authenticate", e);
      }
    }
  }
}
