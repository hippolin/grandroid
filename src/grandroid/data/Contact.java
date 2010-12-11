/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.data;

import grandroid.database.Table;

/**
 *
 * @author Rovers
 */
@Table("Contact")
public class Contact {

    protected Integer _id;
    protected String name;
    protected String email;
    protected boolean member;
    protected boolean selected;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
