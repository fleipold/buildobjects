package org.buildobjects.util;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.ClassesCombiner;
import org.buildobjects.artifacts.Sources;
import org.buildobjects.artifacts.SourcesCombiner;
import org.buildobjects.artifacts.resources.*;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 2:08:41 PM
 */
public class MixedUtils {
    public static List<Object> arrayToList(Object array) {
        int length = Array.getLength(array);
        List<Object> returnList = new ArrayList<Object>(length);
        for (int i = 0; i < length; i++) {
            returnList.add(Array.get(array, i));
        }
        return returnList;
    }

    public static List enumValues(Class <? extends Enum> clazz){
        try {
            Method valuesMethod = clazz.getMethod("values", new Class[]{});
            valuesMethod.setAccessible(true);
                Object array = valuesMethod.invoke(null);
                return arrayToList(array);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static String relativePath(File root, File descendant){
        return descendant.getAbsolutePath().replace(root.getAbsolutePath()+File.separator,"");

    }

    public static String relativePathCleaner(String path) {
        String cleanPath = path.replaceAll("//*","/");
        if (cleanPath.startsWith("/"))
            cleanPath = cleanPath.substring(1);
        if (cleanPath.endsWith("/"))
            cleanPath = cleanPath.substring(0, cleanPath.length()-1);
       return cleanPath;
    }

    public static <T> Set<T> set(T... elements){
        return  new HashSet<T>(Arrays.asList(elements));                
    }


    public static <T> List<T> list(T... elements){
            return  Arrays.asList(elements);                
    }

    public static Classes combine(Classes... elements){
        return new ClassesCombiner(elements);
    }

    public static Sources combine(Sources... elements){
        return new SourcesCombiner(elements);
    }


    public static Resources combine(Resources... elements){
        return new ResourcesCombiner(elements);
    }

    
    public static Resources prefix(Path prefix, Resources resources){
        return new ResourcesPrefixer(prefix, resources);
    }


}




