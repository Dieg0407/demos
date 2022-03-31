package aa.api.dialer.service;

import aa.api.dialer.model.UserInfo;
import aa.api.dialer.service.cli.ExtensionService;
import aa.api.dialer.service.factory.RestClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final ExtensionService extensionService;
  private final RestClientFactory factory;

  @Override
  public UserInfo whoAmI(String authToken) {
    final var client = factory.createClient(authToken);
    final var ext = extensionService.findLoggedInUserExtension(client);

    return UserInfo.builder()
        .email(ext.contact.email)
        .name(ext.name)
        .build();
  }
}
