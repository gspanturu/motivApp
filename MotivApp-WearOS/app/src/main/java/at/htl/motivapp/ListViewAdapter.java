package at.htl.motivapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import at.htl.motivapp.model.Task;

public class ListViewAdapter extends ArrayAdapter<Task> {


    public ListViewAdapter( Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0){
            return LayoutInflater.from(this.getContext()).inflate(R.layout.new_goal, parent, false);
        }
        convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        Task task = getItem(position);
        TextView title = convertView.findViewById(android.R.id.text1);
        title.setText(task.getTitle());

        return convertView;
    }
}
