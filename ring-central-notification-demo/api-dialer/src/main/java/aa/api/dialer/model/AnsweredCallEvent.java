package aa.api.dialer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AnsweredCallEvent extends ApplicationEvent  {
  private final String userEmail;
  private final String callerPhoneNumber;

  public AnsweredCallEvent(Object source, String userEmail, String callerPhoneNumber) {
    super(source);
    this.userEmail = userEmail;
    this.callerPhoneNumber = callerPhoneNumber;
  }
}
