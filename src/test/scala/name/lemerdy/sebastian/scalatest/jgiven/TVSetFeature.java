package name.lemerdy.sebastian.scalatest.jgiven;

import org.scalatest.TagAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@TagAnnotation
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface TVSetFeature {
}
