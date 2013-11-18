/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.DynProxy;

/**
 *
 * @author GBH
 */
public class DelegatedTo {

    private Object target;

    public DelegatedTo() {
        new DelegatedTo(this);

    }

    public DelegatedTo(Object target) {
        this.target = target;

    }
}
