package patryk.bezpieczneauto.Objects;

import patryk.bezpieczneauto.R;

public enum ModelObject {

    RED(android.R.color.holo_red_dark, R.layout.viewpager_przeglady),
    BLUE(android.R.color.holo_blue_dark, R.layout.viewpager_ubezpieczenia);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}