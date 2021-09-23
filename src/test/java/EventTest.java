
import com.company.events.concrete.Event;
import com.company.events.concrete.GlobalHandler;
import com.company.events.concrete.Listener;
import org.junit.jupiter.api.Test;

/**
 * @author H. Ardaki
 */
public class EventTest {
    @Test
    void shouldAssignListenerAdnInvokeEvent() {
        GlobalHandler.getInstance().addListener(new Listener<String>(
                "hi",
                stringEvent -> System.out.println(stringEvent.getData())
        ));
        GlobalHandler.getInstance().emit(new Event<>("hi", "bye"));
    }
}
