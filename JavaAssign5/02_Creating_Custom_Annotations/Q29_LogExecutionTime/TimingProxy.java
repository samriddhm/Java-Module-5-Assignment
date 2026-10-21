import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Q29: wraps any object in a JDK dynamic proxy. Every call goes through invoke();
 * if the REAL method has @LogExecutionTime, the call is timed and logged.
 *
 * This is the same idea Spring AOP uses for @Transactional, @Cacheable, @Timed, etc.
 */
public final class TimingProxy implements InvocationHandler {

    private final Object target;

    private TimingProxy(Object target) {
        this.target = target;
    }

    /** Creates a proxy that looks like the given interface but adds timing. */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Class<T> iface) {
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                new TimingProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method interfaceMethod, Object[] args) throws Throwable {
        // The annotation is on the IMPLEMENTATION's method, so look that method up.
        Method realMethod = target.getClass().getMethod(interfaceMethod.getName(),
                interfaceMethod.getParameterTypes());
        LogExecutionTime timing = realMethod.getAnnotation(LogExecutionTime.class);

        if (timing == null) {
            return call(interfaceMethod, args);            // no annotation -> just call it
        }

        String label = timing.value().isEmpty() ? realMethod.getName() : timing.value();
        long start = System.nanoTime();
        try {
            return call(interfaceMethod, args);
        } finally {                                         // logs even if the method throws
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[TIMER] " + label + " took " + ms + " ms");
            if (ms > timing.warnAboveMs()) {
                System.out.println("[TIMER] WARNING: " + label + " is slower than the "
                        + timing.warnAboveMs() + " ms limit!");
            }
        }
    }

    private Object call(Method m, Object[] args) throws Throwable {
        try {
            return m.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();                             // rethrow the method's real exception
        }
    }
}
