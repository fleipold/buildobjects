package org.buildobjects.util;

import org.buildobjects.tasks.Task;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * User: fleipold
 * Date: Oct 16, 2008
 * Time: 10:42:20 PM
 */
public class DelegatingProxy {

    public static <T> T create(Class<T> clazz, final DelegateProvider<T> delegateProvider, ClassLoader classLoader) {
     return (T)Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new InvocationHandler() {
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
             return method.invoke(delegateProvider.delegate(),args);
         }
     });
    }


     public static <T> T create(Class<T> clazz, final DelegateProvider<T> delegateProvider ) {
         return create(clazz,delegateProvider, clazz.getClassLoader());
    }


     public static <T> T guard(Class<T> clazz, final Task guard, final T actual ) {
         return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {

         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            guard.build();
            return method.invoke(actual, args);
         }
     });
    }



    public static <T> T createLazyProxy(Class<T> clazz, final ProxyFactory<T> delegateFactory, ClassLoader classLoader) {
     return (T)Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new InvocationHandler() {
         T delegate;

         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
             if (delegate ==null){
                 delegate = delegateFactory.createProxy();
             }
             return method.invoke(delegate,args);
         }
     });
    }


    public static <T> T createLazyProxy(Class<T> clazz, final ProxyFactory<T> proxyFactory) {
        return createLazyProxy(clazz, proxyFactory, clazz.getClassLoader());    
    }

}
