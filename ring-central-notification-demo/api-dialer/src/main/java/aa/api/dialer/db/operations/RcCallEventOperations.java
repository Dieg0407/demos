package aa.api.dialer.db.operations;

import aa.api.dialer.model.CallEvent;
import java.util.UUID;

public interface RcCallEventOperations {
  UUID create(CallEvent model);
}
