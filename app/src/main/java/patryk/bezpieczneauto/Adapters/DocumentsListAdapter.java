package patryk.bezpieczneauto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import patryk.bezpieczneauto.Objects.Document;
import patryk.bezpieczneauto.R;

public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListAdapter.DocumentViewHolder> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Document> list;
    private OnDocumentListener onDocumentListener;

    public DocumentsListAdapter(Context context, int layoutResourceId, OnDocumentListener onDocumentListener, ArrayList<Document> list) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.onDocumentListener = onDocumentListener;
        this.list = list;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutResourceId, parent, false);
        return new DocumentViewHolder(view, onDocumentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {

        Document document = list.get(position);
        holder.carName.setText(document.getAuto());
        holder.docInfo.setText(document.getInfo());
        holder.docAdditionalInfo.setText(document.getAdditionalInfo());
        holder.docDate.setText(String.format("Od: %s", document.getDate()));
        holder.docExpiryDate.setText(String.format("Do: %s", document.getExpiryDate()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnDocumentListener {
        void onDocumentClick(int position);
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView carName;
        TextView docInfo;
        TextView docAdditionalInfo;
        TextView docDate;
        TextView docExpiryDate;
        OnDocumentListener onDocumentListener;

        public DocumentViewHolder(View view, OnDocumentListener onDocumentListener) {
            super(view);

            carName = view.findViewById(R.id.document_car_name);
            docInfo = view.findViewById(R.id.insurance_policy);
            docAdditionalInfo = view.findViewById(R.id.insurance_additional_info);
            docDate = view.findViewById(R.id.insurance_date);
            docExpiryDate = view.findViewById(R.id.insurance_expiry_date);
            this.onDocumentListener = onDocumentListener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onDocumentListener.onDocumentClick(getAdapterPosition());
        }
    }
}