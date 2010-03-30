/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.action;

import android.view.View;

/**
 *
 * @author Rovers
 */
public class Action {

    protected String actionName;
    protected View src;
    protected String[] args;

    public Action() {
    }

    public Action(String actionName) {
        this.actionName = actionName;
    }

    public Action(View src) {
        this.src = src;
    }

    public View getSrc() {
        return src;
    }

    public Action setSrc(View src) {
        this.src = src;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public Action setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public Action setArgs(String ... args) {
        this.args = args;
        return this;
    }

    public boolean execute() {

        return true;
    }
}
