package com.example.nioaioserver.netty.action;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotated;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface AnnoteAction {
    String value() default "";
}
