package aa.api.dialer.controller.api;

import aa.api.dialer.model.UserInfo;
import aa.api.dialer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-info")
@RequiredArgsConstructor
public class UserInfoController {
  private final UserService service;

  @GetMapping(path = "")
  public UserInfo whoAmI(@RequestHeader("x-rc-auth-token") String authToken) {
    return service.whoAmI(authToken);
  }
}
