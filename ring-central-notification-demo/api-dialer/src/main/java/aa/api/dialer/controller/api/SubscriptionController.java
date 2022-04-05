package aa.api.dialer.controller.api;

import aa.api.dialer.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
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
  @CrossOrigin("${NGROK_DOMAIN}")
  public void createSubscription(@RequestHeader(Headers.RC_AUTH_HEADER) String authToken) {
    service.create(authToken);
  }

  @DeleteMapping("")
  @CrossOrigin("${NGROK_DOMAIN}")
  public void deleteSubscription(@RequestHeader(Headers.RC_AUTH_HEADER) String authToken) {

  }
}
