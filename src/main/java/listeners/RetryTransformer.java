package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Automatically attaches RetryAnalyzer to ALL test methods.
 * No need to add retryAnalyzer = RetryAnalyzer.class to every @Test.
 * <p>
 * This is an IAnnotationTransformer — it modifies annotations at runtime
 * BEFORE tests execute. TestNG calls transform() for every @Test it finds.
 */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }

}
