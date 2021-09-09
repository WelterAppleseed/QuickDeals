package com.example.quickdeals;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.quickdeals.daily.adapter_and_activity.ListActivity;
import com.example.quickdeals.daily.adapter_and_activity.ListAdapter;
import com.example.quickdeals.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addItem;
    private ListAdapter adapter;
    private LinearLayout itemLayout;
    private Dialog addItemDialog;
    private LayoutInflater inflater;
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(70));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_activity_layout);
        inflater = LayoutInflater.from(MainActivity.this);
        contentCreate();
    }
    private void contentCreate() {
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);
        addItem = (FloatingActionButton) findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_row_reminder);

                final EditText text = dialog.findViewById(R.id.item_title);
                final SwipeRevealLayout layout = (SwipeRevealLayout) LayoutInflater.from(dialog.getContext()).inflate(R.layout.reminder_layout, null, false);
                layout.setLayoutParams(params);
                layout.setPadding(Utils.dpToPx(10), 0, Utils.dpToPx(10), 0);
                FrameLayout textFrame = (FrameLayout) layout.getChildAt(1);
                final TextView itemTitle = (TextView) textFrame.getChildAt(0);
                layout.setLockDrag(true);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text.performClick();
                    }
                });
                Button button = dialog.findViewById(R.id.item_add_button);
                button.setText("OK");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout.setLockDrag(false);
                        itemTitle.setText(text.getText().toString());
                        itemLayout.addView(layout);
                        Space space = new Space(MainActivity.this);
                        space.setMinimumHeight(Utils.dpToPx(10));
                        itemLayout.addView(space);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void layoutTwoOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 2 clicked", Toast.LENGTH_SHORT).show();
    }

    public void layoutThreeOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 3 clicked", Toast.LENGTH_SHORT).show();
    }

    public void layoutFourOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 4 clicked", Toast.LENGTH_SHORT).show();
    }

    public void moreOnClick(View v) {
        Toast.makeText(MainActivity.this, "More clicked", Toast.LENGTH_SHORT).show();
    }

    public void deleteOnClick(View v) {
        Toast.makeText(MainActivity.this, "Delete clicked", Toast.LENGTH_SHORT).show();
    }

    public void archiveOnClick(View v) {
        Toast.makeText(MainActivity.this, "Archive clicked", Toast.LENGTH_SHORT).show();
    }

    public void helpOnClick(View v) {
        Toast.makeText(MainActivity.this, "Help clicked", Toast.LENGTH_SHORT).show();
    }

    public void searchOnClick(View v) {
        Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
    }

    public void starOnClick(View v) {
        Toast.makeText(MainActivity.this, "Star clicked", Toast.LENGTH_SHORT).show();
    }
}