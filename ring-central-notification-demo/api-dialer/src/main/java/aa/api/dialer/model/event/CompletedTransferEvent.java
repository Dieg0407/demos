package aa.api.dialer.model.event;

import aa.api.dialer.model.CallEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CompletedTransferEvent extends ApplicationEvent {
  private final CallEvent event;

  public CompletedTransferEvent(Object source, CallEvent event) {
    super(source);
    this.event = event;
  }
}
