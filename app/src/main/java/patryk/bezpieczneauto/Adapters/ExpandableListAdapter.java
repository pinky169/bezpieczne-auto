package patryk.bezpieczneauto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import patryk.bezpieczneauto.Database.DBHelper;
import patryk.bezpieczneauto.Interfaces.mDialogInterface;
import patryk.bezpieczneauto.Objects.Car;
import patryk.bezpieczneauto.Objects.CarPart;
import patryk.bezpieczneauto.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Car> cars;
    private HashMap<Car, List<CarPart>> carParts;
    private Context context;
    private int groupLayoutResourceId;
    private int childLayoutResourceId;
    private mDialogInterface dialogInterface;

    public ExpandableListAdapter(Context context, mDialogInterface dialogInterface, int groupLayoutResourceId, int childLayoutResourceId, ArrayList<Car> cars, HashMap<Car, List<CarPart>> carParts) {
        this.cars = cars;
        this.carParts = carParts;
        this.context = context;
        this.groupLayoutResourceId = groupLayoutResourceId;
        this.childLayoutResourceId = childLayoutResourceId;
        this.dialogInterface = dialogInterface;
    }

    static class CarViewHolder
    {
        ImageView carIcon;
        TextView carText;
        TextView carSubInfo;
        TextView carDate;
        ImageView carIsMainIcon;
        ImageView carEditIcon;
    }

    static class PartsViewHolder
    {
        ImageView part_icon_resource;
        TextView partName;
        TextView partAdditionalInfo;
        TextView partDate;
        TextView partPrice;
    }


    @Override
    public int getGroupCount() {
        return cars.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return carParts.get(cars.get(groupPosition)).size();
    }

    @Override
    public Car getGroup(int groupPosition) {
        return cars.get(groupPosition);
    }

    @Override
    public CarPart getChild(int groupPosition, int childPosition) {
        return carParts.get(cars.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup parent) {

        View row = view;
        final CarViewHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(groupLayoutResourceId, parent, false);

            holder = new CarViewHolder();
            holder.carIcon = row.findViewById(R.id.item_icon);
            holder.carText = row.findViewById(R.id.car_name);
            holder.carSubInfo = row.findViewById(R.id.car_sub_info);
            holder.carDate = row.findViewById(R.id.date);
            holder.carIsMainIcon = row.findViewById(R.id.is_main_car_icon);
            holder.carEditIcon = row.findViewById(R.id.edit_icon);
            holder.carEditIcon.setOnClickListener(myClickListener);

            row.setTag(holder);

        } else {
            holder = (CarViewHolder) row.getTag();
        }

        holder.carEditIcon.setTag(groupPosition);

        Car car = getGroup(groupPosition);
        holder.carIcon.setImageResource(car.getImg_resource());
        holder.carText.setText(car.getMarka());
        holder.carSubInfo.setText(String.format("%s", car.getModel()));
        holder.carDate.setText(String.format("(%s)", car.getRok_produkcji()));

        DBHelper dbHelper = new DBHelper(context);
        if(dbHelper.isMainCar(groupPosition)) {
            holder.carIsMainIcon.setVisibility(View.VISIBLE);
            holder.carIsMainIcon.setImageResource(R.drawable.ic_check_circle);
            TooltipCompat.setTooltipText(holder.carIsMainIcon, context.getResources().getString(R.string.domyslne_auto_tooltip_text));
        } else {
            holder.carIsMainIcon.setVisibility(View.GONE);
        }

        return row;
    }

    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag() + 1;
            dialogInterface.editCarDialog(position);
        }
    };

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup parent) {

        View row = view;
        PartsViewHolder partsDataHolder;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(childLayoutResourceId, null);

            partsDataHolder = new PartsViewHolder();
            partsDataHolder.part_icon_resource = row.findViewById(R.id.part_icon);
            partsDataHolder.partName = row.findViewById(R.id.part_name);
            partsDataHolder.partAdditionalInfo = row.findViewById(R.id.additional_info);
            partsDataHolder.partDate = row.findViewById(R.id.part_date);
            partsDataHolder.partPrice = row.findViewById(R.id.part_price);

            row.setTag(partsDataHolder);

        } else {
            partsDataHolder = (PartsViewHolder) row.getTag();
        }

        CarPart carPart = getChild(groupPosition, childPosition);
        partsDataHolder.part_icon_resource.setImageResource(carPart.getImg_resource());
        partsDataHolder.partName.setText(carPart.getNew_part());
        partsDataHolder.partAdditionalInfo.setText(carPart.getAdditional_info());
        partsDataHolder.partDate.setText(String.format(context.getResources().getString(R.string.part_date_adapter_placeholder), carPart.getDate()));
        partsDataHolder.partPrice.setText(String.format(context.getResources().getString(R.string.part_price_adapter_placeholder), carPart.getPrice()));

        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
