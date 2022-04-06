package aa.api.dialer.db.oprations;

import aa.api.dialer.model.CallEvent;
import java.util.UUID;

public interface RcCallEventOperations {
  UUID create(CallEvent model);
}
