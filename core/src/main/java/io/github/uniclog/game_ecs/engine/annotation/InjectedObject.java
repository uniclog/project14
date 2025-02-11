package io.github.uniclog.game_ecs.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, указывающая, что поле должно быть инъектировано
 * в процессе выполнения.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InjectedObject {
    Class<?> value() default Object.class;
}
