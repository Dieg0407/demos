package aa.api.dialer.controller;

import aa.api.dialer.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
  private final SubscriptionService service;

  @PostMapping("")
  public void createSubscription(@RequestHeader("x-rc-auth-token") String authToken) {
    service.create(authToken);
  }

  @DeleteMapping("")
  public void deleteSubscription(@RequestHeader("x-rc-auth-token") String authToken) {

  }
}
