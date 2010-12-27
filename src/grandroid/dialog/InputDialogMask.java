/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.content.Context;
import android.widget.EditText;
import grandroid.action.Action;
import grandroid.dialog.GDialog.Builder;
import grandroid.util.LayoutMaker;

/**
 *
 * @author Rovers
 */
public abstract class InputDialogMask extends DialogMask {

    protected String defaultText;
    protected String hintText;
    protected String captionText;
    protected String titleText;

    public InputDialogMask(Context context, String titleText, String defaultText, String hintText, String captionText) {
        super(context);
        this.titleText = titleText;
        this.hintText = hintText;
        this.captionText = captionText;
        this.defaultText = defaultText == null ? "" : defaultText;
    }

    public abstract boolean executeAction(String inputText);

    @Override
    public boolean setupMask(Context context, Builder builder, LayoutMaker maker) throws Exception {
        builder.setTitle(titleText);
        if (captionText != null && captionText.length() > 0) {
            maker.addTextView(captionText);
        }
        final EditText et = maker.addEditText(defaultText);
        if (hintText != null && hintText.length() > 0) {
            et.setHint(hintText);
        }
        builder.setPositiveButton(new Action(context.getString(maker.getResourceID("string/grand_btn_yes"))) {

            @Override
            public boolean execute() {
                if (executeAction(et.getText().toString())) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        builder.setNegativeButton(new Action(context.getString(maker.getResourceID("string/grand_btn_cancel"))) {

            @Override
            public boolean execute() {
                dialog.dismiss();
                return true;
            }
        });
        return true;
    }
}
