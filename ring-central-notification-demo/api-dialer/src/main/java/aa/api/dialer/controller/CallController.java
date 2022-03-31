package aa.api.dialer.controller;

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
public class CallController {

  @PostMapping(path = "/hook/{extensionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> hookPerExtension(
      @RequestHeader(name = "Validation-Token", required = false) String validation,
      @RequestBody(required = false) String payload,
      @PathVariable("extensionId") String extensionId
  ) {
    if (payload == null) {
      return ResponseEntity.ok()
          .header("Validation-Token", validation)
          .build();
    }
    return ResponseEntity.ok().build();
  }

}
