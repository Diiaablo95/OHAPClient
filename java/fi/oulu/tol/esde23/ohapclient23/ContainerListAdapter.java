package fi.oulu.tol.esde23.ohapclient23;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.opimobi.ohap.Container;
import com.opimobi.ohap.Device;
import com.opimobi.ohap.Item;

import org.w3c.dom.Text;

public class ContainerListAdapter implements ListAdapter {

    //Private class
    private static class ViewHolder {
        public TextView textView_type;
        public TextView textView_id;
        public TextView textView_name;
        public TextView textView_description;
        public TextView textView_items_count;
        public TextView textView_listening;

        public ViewHolder(TextView type, TextView id, TextView name, TextView description, TextView count, TextView listening) {
            this.textView_type = type;
            this.textView_id = id;
            this.textView_name = name;
            this.textView_description = description;
            this.textView_items_count = count;
            this.textView_listening = listening;
        }
    }

    //Instance variables
    private Container container;

    //Constructor
    public ContainerListAdapter(Container container) {
        this.container = container;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return this.container.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return this.container.getItemByIndex(position);
    }

    @Override
    public long getItemId(int position) {
        return this.container.getItemByIndex(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //If first time, all initialisations are made and viewHolder is saved in the view as tag
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView is the view to return for each row
            convertView = inflater.inflate(R.layout.row_item, parent, false);

            TextView textView_type = (TextView) convertView.findViewById(R.id.textView_type);
            TextView textView_id = (TextView) convertView.findViewById(R.id.textView_id);
            TextView textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            TextView textView_description = (TextView) convertView.findViewById(R.id.textView_description);
            TextView textView_items_count = (TextView) convertView.findViewById(R.id.textView_items_count);
            TextView textView_listening = (TextView) convertView.findViewById(R.id.textView_listening);

            holder = new ViewHolder(textView_type, textView_id, textView_name, textView_description, textView_items_count, textView_listening);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item itemSelected = container.getItemByIndex(position);

        if (itemSelected instanceof Container) {
            holder.textView_type.setText("CONTAINER");
            holder.textView_items_count.setText(String.format("%d", ((Container) itemSelected).getItemCount()));
            holder.textView_listening.setText(String.format("%b", ((Container) itemSelected).isListening()));
        } else if (itemSelected instanceof Device) {
            LinearLayout container_details_layout = (LinearLayout) convertView.findViewById(R.id.container_details_layout);
            container_details_layout.setVisibility(View.GONE);
            holder.textView_type.setText("DEVICE");
        }

        holder.textView_id.setText(String.format("%d", itemSelected.getId()));
        holder.textView_description.setText(itemSelected.getDescription());
        holder.textView_name.setText(itemSelected.getName());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
