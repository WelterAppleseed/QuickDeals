package com.example.quickdeals.daily.actions;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ReminderActions {
    private static final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private Activity activity;
    private static int iconNumber;
    public ReminderActions(Context context) {
         activity = (Activity) context;
    }

    public static void setIconNumber(int iconNumber) {
        ReminderActions.iconNumber = iconNumber;
    }

    /*public final ConstraintLayout createContainer(final Context context) {
        ConstraintLayout constParent = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.reminder_layout, null, false);
                params.setMargins(Utils.dpToPx(10), Utils.dpToPx(10), Utils.dpToPx(10), Utils.dpToPx(10));
                constParent.setLayoutParams(params);
                final SwipeLayout layout = (SwipeLayout) constParent.findViewById(R.id.swipe_layout);
                final TextView deleteText = (TextView) layout.findViewById(R.id.right_delete);
                layout.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
                    @Override
                    public void onOpen(int direction, boolean isContinuous) {
                        deleteText.setClickable(true);
                        deleteText.setEnabled(true);
                    }

                    @Override
                    public void onClose() {
                        deleteText.setClickable(false);
                        deleteText.setEnabled(false);
                    }
                });
        return constParent;
    }*/
   /* public final void createContainer(final Context context, String title, String desc, int year, int month, int day, int hours, int minutes, boolean repeat, boolean alarm, ReminderDao dao) {
        ReminderEntity.addToDatabase(new ReminderData(title, desc, year, iconNumber, month, day, hours, minutes, repeat, alarm), dao);
    }*/
}
