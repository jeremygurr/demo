package demo.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This means that the class it annotates is guaranteed that it will never be altered, and that all of it's fields are also immutable.
 * Setters should still be provided which will return a new instance with the requested change applied.
 */
@Target(value={ElementType.PACKAGE,ElementType.TYPE})
public @interface Immutable {
}
