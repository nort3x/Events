
import com.company.events.basics.WeakHandler;
import com.company.events.concrete.Event;
import com.company.events.concrete.GlobalHandler;
import com.company.events.concrete.Listener;
import com.company.events.concrete.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author H. Ardaki
 */
public class EventTest {


    WeakHandler h = GlobalHandler.getInstance();

    @BeforeEach
    void clearGlobalHandler(){
        h.clearAllListeners();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000})
    void shouldAssignListenerAdnInvokeByEvent(int numberOfListeners) throws InterruptedException {


        CountDownLatch cld = new CountDownLatch(numberOfListeners);

        // listeners are sharing a single event
        IntStream.range(0, numberOfListeners)
                .forEach(num -> h.addListener(
                                new Listener<AtomicInteger>(
                                        EventTest.class,
                                        event -> {
                                            event.getData().incrementAndGet();
                                            cld.countDown();
                                        }
                                )
                        )
                );

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>(EventTest.class, number));

        cld.await();

        h.clearAllListeners();

        Assertions.assertEquals(number.get(), numberOfListeners);
    }


    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners are being GCed if token is not preserved")
    void shouldRemoveOrphanedListener() throws InterruptedException {

        int numberOfListeners = 1000;
        IntStream.range(0, numberOfListeners)
                .forEach(num -> h.addListener(
                                new Listener<AtomicInteger>(
                                        EventTest.class,
                                        event -> {
                                            event.getData().incrementAndGet();
                                        }
                                )
                        )
                );

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>(EventTest.class, number));

        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertTrue(h.getNumberOfListeners() < 1000);

    }

    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners are being saved if token is preserved")
    void shouldKeepListenersWhichHasAliveToken() throws InterruptedException {

        int numberOfListeners = 1000;
        List<Token<AtomicInteger>> tokens = IntStream.range(0, numberOfListeners)
                .mapToObj(num -> h.addListener(
                                new Listener<AtomicInteger>(
                                        EventTest.class,
                                        event -> {
                                            event.getData().incrementAndGet();
                                        }
                                )
                        )
                ).collect(Collectors.toList());

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>(EventTest.class, number));

        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertEquals(1000, h.getNumberOfListeners());

    }


    private void registListeners(Object obj,int numberOfListeners){
        IntStream.range(0, numberOfListeners)
                .forEach(num -> h.addListener(
                                new Listener<AtomicInteger>(
                                        obj,
                                        event -> {
                                            event.getData().incrementAndGet();
                                        }
                                )
                        )
                );
    }

    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners-container (targetObject) are being GCed if targetObject is not preserved")
    void shouldRemoveListenerListIfOrphanedTarget() throws InterruptedException {

        Object dumbObj = new Object();
        int numberOfListeners = 1000;

        registListeners(dumbObj,numberOfListeners);

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>(dumbObj, number));
        dumbObj = null;

        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertTrue(h.getNumberOfListeners() < 1000);
        Assertions.assertEquals(0, h.getNumberOfTargets());

    }

    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners-container (targetObject) are being GCed if targetObject is preserved")
    void shouldKeepListIfTargetisActive() throws InterruptedException {

        Object dumbObj = new Object();
        int numberOfListeners = 1000;

        registListeners(dumbObj,numberOfListeners);

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>(dumbObj, number));
//        dumbObj = null;

        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertTrue(h.getNumberOfListeners() < 1000);
        Assertions.assertEquals(1, h.getNumberOfTargets());

    }


    // testing with strings



    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners-container (targetObject) are being GCed if targetObject is not preserved if its String")
    void shouldRemoveListenerListIfOrphanedTargetString() throws InterruptedException {

//        Object dumbObj = new Object();
        int numberOfListeners = 1000;

        registListeners("dumbObj",numberOfListeners);

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>("dumbObj", number));
//        dumbObj = null;

        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertTrue(h.getNumberOfListeners() < 1000);
        Assertions.assertEquals(0, h.getNumberOfTargets());

    }

    @Test
    @DisplayName("loose method! might fail easily!!! testing whether listeners-container (targetObject) are being GCed if targetObject is preserved if String")
    void shouldKeepListIfTargetIsActiveString() throws InterruptedException {


        int numberOfListeners = 1000;

        registListeners("dumbObj",numberOfListeners);

        AtomicInteger number = new AtomicInteger();
        h.emit(new Event<>("dumbObj", number));


        System.out.println(h.getNumberOfListeners());
        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());

        System.gc();
        Thread.sleep(100);
        System.out.println(h.getNumberOfListeners());


        System.out.println(h.getNumberOfListeners());
        Assertions.assertTrue(h.getNumberOfListeners() < 1000);
        Assertions.assertEquals(1, h.getNumberOfTargets());

    }

}
