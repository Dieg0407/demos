package aa.api.dialer.controller.api;

import aa.api.dialer.model.UserInfo;
import aa.api.dialer.model.UserInfo.UserPresence;
import aa.api.dialer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-info")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping(path = "")
  @CrossOrigin("${NGROK_DOMAIN}")
  public UserInfo whoAmI(@RequestHeader(Headers.RC_AUTH_HEADER) String authToken) {
    return service.whoAmI(authToken);
  }

  @PatchMapping
  @CrossOrigin("${NGROK_DOMAIN}")
  public void updatePresence(
      @RequestHeader(Headers.RC_AUTH_HEADER) String authToken,
      @RequestBody UserPresence presence
  ) {
    service.updatePresence(authToken, presence);
  }
}
