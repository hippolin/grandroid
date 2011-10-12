/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import grandroid.action.Action;
import grandroid.dialog.GDialog.DialogStyle;
import grandroid.util.LayoutMaker;
import grandroid.util.LayoutUtil;
import java.lang.reflect.Field;

/**
 *
 * @author Rovers
 */
public abstract class DialogMask {

    protected GDialog dialog;
    protected Context context;
    protected GDialog.Builder builder;

    public DialogMask(Context context) {
        this.context = context;
        builder = new GDialog.Builder(context);
    }

    public abstract boolean setupMask(Context context, GDialog.Builder builder, LayoutMaker maker) throws Exception;

    public <T extends View> T setButtonEvent(T view, final Action action) {
        view.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                action.setSrc(view);
                action.execute();
            }
        });
        return view;
    }

    public void onDismiss(DialogInterface dialogInterface) {
    }

    public void onCancel(DialogInterface dialogInterface) {
    }

    public GDialog getDialog() {
        return dialog;
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void show() {
        show(DialogStyle.Grandroid);
    }

    public void show(DialogStyle style) {
        try {
            if (style == DialogStyle.Android) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                LinearLayout ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                LayoutMaker maker = new LayoutMaker(context, ll);
                setupMask(context, builder, maker);
                b.setTitle(builder.getTitle());
                b.setView(maker.getMainLayout());
                if (builder.getPositiveButtonAction() != null) {
                    b.setPositiveButton(builder.getPositiveButtonAction().getActionName(), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dia, int arg1) {
                            if (builder.getPositiveButtonAction().execute()) {
                                try {
                                    Field field = dia.getClass().getSuperclass().getDeclaredField(
                                            "mShowing");
                                    field.setAccessible(true);
                                    //   将mShowing变量设为false，表示对话框已关闭 
                                    field.set(dia, true);
                                } catch (Exception e) {
                                }
                                dia.dismiss();
                            } else {
                                try {
                                    Field field = dia.getClass().getSuperclass().getDeclaredField(
                                            "mShowing");
                                    field.setAccessible(true);
                                    //   将mShowing变量设为false，表示对话框已关闭 
                                    field.set(dia, false);
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
                }
                if (builder.getNegativeButtonAction() != null) {
                    b.setNegativeButton(builder.getNegativeButtonAction().getActionName(), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dia, int arg1) {
                            if (builder.getNegativeButtonAction().execute()) {
                                try {
                                    Field field = dia.getClass().getSuperclass().getDeclaredField(
                                            "mShowing");
                                    field.setAccessible(true);
                                    //   将mShowing变量设为false，表示对话框已关闭 
                                    field.set(dia, true);
                                } catch (Exception e) {
                                }
                                dia.cancel();
                            }
                        }
                    });
                }
                final Dialog d = b.create();
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    public void onDismiss(DialogInterface dialogInterface) {
                        DialogMask.this.onDismiss(dialogInterface);
                    }
                });
                d.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialogInterface) {
                        DialogMask.this.onCancel(dialogInterface);
                    }
                });
                d.show();
            } else {
                LinearLayout ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                LayoutMaker maker = new LayoutMaker(context, ll);

                builder.beforeDialogContent(maker, style);

                setupMask(context, builder, maker);

                dialog = builder.create(maker, style);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    public void onDismiss(DialogInterface dialogInterface) {
                        DialogMask.this.onDismiss(dialogInterface);
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialogInterface) {
                        DialogMask.this.onCancel(dialogInterface);
                    }
                });
                dialog.show();
            }
        } catch (Exception ex) {
            Log.e(DialogMask.class.getName(), null, ex);
        }
    }

//    public void showAsDialog() {
//        try {
//        } catch (Exception ex) {
//            Log.e(DialogMask.class.getName(), null, ex);
//        }
//    }
    protected LinearLayout.LayoutParams getMaxSizeLayoutParams() {
        return getMaxSizeLayoutParams(0);
    }

    protected LinearLayout.LayoutParams getMaxSizeLayoutParams(int minus) {
        LayoutUtil lu = new LayoutUtil((Activity) context);
        return new LinearLayout.LayoutParams(lu.getWidth() - 60, lu.getHeight() - 80 - minus);
    }
}
