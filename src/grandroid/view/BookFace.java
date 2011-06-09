/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.util.SessionEvents.AuthListener;
import facepaper.api.FacebookAgent;
import grandroid.action.ContextAction;

/**
 *
 * @author Rovers
 */
public abstract class BookFace extends Face {

    /**
     * 
     */
    protected FacebookAgent agent;

    /**
     * 
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agent = new FacebookAgent(getAppID(), getFacebookIcon(), this);
    }

    /**
     * 
     * @param successAction
     * @param failAction
     * @param getUserData
     */
    public void loginFacebook(final ContextAction successAction, final ContextAction failAction, final boolean getUserData) {
        agent.login(new AuthListener()    {

            public void onAuthSucceed() {
                if (getUserData) {
                    agent.getProfile();
                }
                if (successAction != null) {
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

    /**
     * 
     */
    public void logoutFacebook() {
        agent.logout();
    }

    /**
     * 
     * @return
     */
    protected abstract String getAppID();

    /**
     * 
     * @return
     */
    protected abstract int getFacebookIcon();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        agent.authorizeCallback(requestCode, resultCode, data);
    }
}
