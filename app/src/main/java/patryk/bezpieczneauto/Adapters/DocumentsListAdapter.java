package patryk.bezpieczneauto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import patryk.bezpieczneauto.Objects.Document;
import patryk.bezpieczneauto.R;

public class DocumentsListAdapter extends ArrayAdapter<Document> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Document> list;

    public DocumentsListAdapter(Context context, int layoutResourceId, ArrayList<Document> list) {
        super(context, layoutResourceId, list);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.list = list;
    }

    static class DocumentViewHolder
    {
        TextView carName;
        TextView docInfo;
        TextView docAdditionalInfo;
        TextView docDate;
        TextView docExpiryDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final DocumentViewHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DocumentViewHolder();
            holder.carName = row.findViewById(R.id.document_car_name);
            holder.docInfo = row.findViewById(R.id.document_info);
            holder.docAdditionalInfo = row.findViewById(R.id.document_additional_info);
            holder.docDate = row.findViewById(R.id.document_date);
            holder.docExpiryDate = row.findViewById(R.id.document_expiry_date);

            row.setTag(holder);
        }
        else
        {
            holder = (DocumentViewHolder) row.getTag();
        }

        Document document = getItem(position);
        holder.carName.setText(document.getAuto());
        holder.docInfo.setText(document.getPolicy());
        holder.docAdditionalInfo.setText(document.getAdditionalInfo());
        holder.docDate.setText(String.format("Od: %s", document.getDate()));
        holder.docExpiryDate.setText(String.format("Do: %s", document.getExpiryDate()));

        return row;
    }
}