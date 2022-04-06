package aa.api.dialer.controller.api;

import aa.api.dialer.service.hook.IncomingCallHookService;
import aa.api.dialer.service.hook.TrackingCallHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/call")
@RequiredArgsConstructor
public class CallController {
  final IncomingCallHookService incomingCallHookService;
  final TrackingCallHookService trackingCallHookService;

  @PostMapping(path = "/tracking-event", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> trackingCallHook(
      @RequestHeader(name = "Validation-Token", required = false) String validation,
      @RequestBody(required = false) String payload
  ) {
    if (payload == null) {
      return ResponseEntity.ok()
          .header("Validation-Token", validation)
          .build();
    }
    trackingCallHookService.handle(payload);
    return ResponseEntity.ok().build();
  }

  @PostMapping(path = "/incoming-event/{extensionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> incomingCallHook(
      @RequestHeader(name = "Validation-Token", required = false) String validation,
      @RequestBody(required = false) String payload,
      @PathVariable("extensionId") String extensionId
  ) {
    if (payload == null) {
      return ResponseEntity.ok()
          .header("Validation-Token", validation)
          .build();
    }
    incomingCallHookService.handle(payload, extensionId);
    return ResponseEntity.ok().build();
  }
}
