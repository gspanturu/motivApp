package at.htl.motivapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import at.htl.motivapp.model.Task

class ListViewAdapter(
    context: Context?,
    resource: Int,
    objects: List<Task?>?
) : ArrayAdapter<Task?>(context!!, resource, objects!!) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (position == 0) {
            return LayoutInflater.from(this.context).inflate(R.layout.new_goal, parent, false)
        }
        convertView = LayoutInflater.from(this.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        val task = getItem(position)
        val title = convertView.findViewById<TextView>(android.R.id.text1)
        title.text = task!!.title
        return convertView
    }
}