package com.project.ece150.scavenger;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final String[] itemname;
    private final Bitmap[] imgid;
    private final String[] details;
    private final int[] colors;

    public CustomListAdapter(Activity context, String[] itemname, String[] details, Bitmap[] imgid, int[] colors) {
        super(context, R.layout.list_item_layout, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.details = details;
        this.colors=colors;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_layout, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        if (imgid != null) {
            imageView.setImageBitmap(imgid[position]);
        }
        else{
            imageView.setImageResource(R.drawable.sad);
        }
        if (details != null) {
            extratxt.setText(details[position]);
        }
        if (colors!=null){
        rowView.getBackground().setColorFilter(colors[position], PorterDuff.Mode.SRC_IN);
        }

        return rowView;

    }
}
