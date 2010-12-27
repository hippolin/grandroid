//基本使用方式
//                DSDialog.Builder customBuilder = new DSDialog.Builder(FrameShowWord.this);
//                customBuilder.setTitle("Custom title").setMessage("Custom body").setNegativeButton(new Action("Cancel") {
//
//                    @Override
//                    public boolean execute() {
//                        ((Dialog) this.args[0]).dismiss();
//                        return true;
//                    }
//                }).setPositiveButton(new Action("Confirm") {
//
//                    @Override
//                    public boolean execute() {
//                        ((Dialog) this.args[0]).dismiss();
//                        return true;
//                    }
//                });
//                Dialog dialog = customBuilder.create();
//                dialog.show();
package grandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import grandroid.action.Action;
import grandroid.util.LayoutMaker;

/**
 *
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to
 * create and use your own look & feel.
 *
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author antoine vianey
 *
 */
public class GDialog extends Dialog {

    public GDialog(Context context, int theme) {
        super(context, theme);
    }

    public GDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        //private View contentView;
        private Action positiveButtonAction,
                negativeButtonAction;
        private TextView tvTitle;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         * @param action
         * @return
         */
        public Builder setPositiveButton(Action action) {
            this.positiveButtonAction = action;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         * @param action
         * @return
         */
        public Builder setNegativeButton(Action action) {
            this.negativeButtonAction = action;
            return this;
        }

        public void beforeDialogContent(LayoutMaker maker) {
            maker.getMainLayout().setMinimumWidth(280);
            maker.getMainLayout().setBackgroundColor(Color.TRANSPARENT);
            LinearLayout layoutTitle = maker.addColLayout();
            layoutTitle.setBackgroundColor(Color.TRANSPARENT);
            layoutTitle.setBackgroundResource(maker.getResourceID("drawable/dialog_header"));
            tvTitle = (TextView) maker.add(maker.createTextView("Dialog"), maker.layWW(0));
            tvTitle.setPadding(8, 0, 8, 0);
            tvTitle.setBackgroundResource(maker.getResourceID("drawable/dialog_title"));
            tvTitle.setTextSize(18);
            tvTitle.setTextColor(Color.BLACK);
            tvTitle.setTypeface(null, Typeface.BOLD);
            maker.escape();
            
            LinearLayout layoutContent = maker.addColLayout();
            layoutContent.setBackgroundResource(maker.getResourceID("drawable/dialog_center"));
        }

        /**
         * Create the custom dialog
         * @param maker
         * @return  
         */
        public GDialog create(LayoutMaker maker) {
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            String uri = "style/GrandroidDialog";
            int resStyle = context.getResources().getIdentifier(uri, null, context.getPackageName());

            final GDialog dialog = new GDialog(context, resStyle);

            maker.escape();
            
            LinearLayout layoutBottom = maker.addRowLayout();
            layoutBottom.setBackgroundColor(Color.TRANSPARENT);
            layoutBottom.setBackgroundResource(maker.getResourceID("drawable/dialog_footer"));

            if (positiveButtonAction != null) {
                Button btn = maker.addButton(positiveButtonAction.getActionName());

                btn.setOnClickListener(new View.OnClickListener()   {

                    public void onClick(View v) {
                        positiveButtonAction.setArgs(dialog);
                        positiveButtonAction.setSrc(v);
                        positiveButtonAction.execute();
                    }
                });
            }
            // set the cancel button
            if (negativeButtonAction != null) {
                Button btn = maker.addButton(negativeButtonAction.getActionName());
                btn.setOnClickListener(new View.OnClickListener()   {

                    public void onClick(View v) {
                        negativeButtonAction.setArgs(dialog);
                        negativeButtonAction.setSrc(v);
                        negativeButtonAction.execute();
                    }
                });
            }
            if (title != null) {
                tvTitle.setText(title);
            }

            dialog.setContentView(maker.getMainLayout(), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }
}
