/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.os.Bundle;
import com.facebook.util.SessionEvents.AuthListener;
import facepaper.api.FacebookAgent;
import grandroid.action.ContextAction;

/**
 *
 * @author Rovers
 */
public abstract class BookFace extends Face {

    protected FacebookAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agent = new FacebookAgent(getAppID(), getFacebookIcon(), this);
    }

    public void loginFacebook(final ContextAction successAction, final ContextAction failAction, final boolean getUserData) {
        agent.login(new AuthListener() {

            public void onAuthSucceed() {
                if (successAction != null) {
                    if (getUserData) {
                        agent.getProfile();
                    }
                    successAction.execute(BookFace.this);
                }
            }

            public void onAuthFail(String error) {
                if (failAction != null) {
                    failAction.execute(BookFace.this);
                }
            }
        });
    }

    public void logoutFacebook() {
        agent.logout();
    }
    protected abstract String getAppID();

    protected abstract int getFacebookIcon();
}
