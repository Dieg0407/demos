package aa.api.dialer.conf.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProps {
  private String hookUrl;
  private RingCentralProps ringCentral;

  @Data
  public static class RingCentralProps {
    private String url;
    private long subscriptionTtl;
    private RingCentralMainAccount mainAccount;
  }

  @Data
  public static class RingCentralMainAccount {
    String clientId;
    String clientSecret;
    String jwt;
    String username;
    String password;
    String extension;
  }
}
