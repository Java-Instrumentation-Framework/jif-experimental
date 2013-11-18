/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionListenerFor
{
    String source();
}