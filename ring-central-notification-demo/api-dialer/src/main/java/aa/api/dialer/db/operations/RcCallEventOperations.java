package aa.api.dialer.db.operations;

import aa.api.dialer.model.CallEvent;
import java.util.List;
import java.util.UUID;

public interface RcCallEventOperations {
  UUID create(CallEvent model);
  List<CallEvent> findAllBySessionId(String sessionId);
}
