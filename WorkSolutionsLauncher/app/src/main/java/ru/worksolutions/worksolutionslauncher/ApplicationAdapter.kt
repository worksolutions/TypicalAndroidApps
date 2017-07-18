package ru.worksolutions.worksolutionslauncher

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ApplicationAdapter(private val view: IViewWithApps) : RecyclerView.Adapter<ApplicationAdapter.AppViewHolder>() {

    private val apps: MutableList<AppModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_icon_text, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]

        holder.icon.setImageDrawable(app.icon)
        holder.label.text = app.label
        holder.itemView.setOnClickListener {
            view.onAppSelect(app)
        }
    }

    override fun getItemCount(): Int = apps.size

    fun setData(apps: List<AppModel>) {
        this.apps.clear()
        this.apps.addAll(apps)
        notifyDataSetChanged()
    }

    class AppViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.icon)
        val label: TextView = v.findViewById(R.id.label)
    }
}