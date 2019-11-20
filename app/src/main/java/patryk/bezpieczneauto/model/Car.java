package patryk.bezpieczneauto.model;

public class Car {

    private String marka;
    private String model;
    private String rok_produkcji;
    private String pojemnosc;
    private String moc;
    private int img_resource;
    private int isMainCar; // 0 - false, 1 - true

    public Car(String marka, String model, String rok_produkcji, String pojemnosc, String moc, int img_resource, int isMainCar) {
        this.marka = marka;
        this.model = model;
        this.rok_produkcji = rok_produkcji;
        this.pojemnosc = pojemnosc;
        this.moc = moc;
        this.img_resource = img_resource;
        this.isMainCar = isMainCar;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRok_produkcji() {
        return rok_produkcji;
    }

    public void setRok_produkcji(String rok_produkcji) {
        this.rok_produkcji = rok_produkcji;
    }

    public String getPojemnosc() {
        return pojemnosc;
    }

    public void setPojemnosc(String pojemnosc) {
        this.pojemnosc = pojemnosc;
    }

    public String getMoc() {
        return moc;
    }

    public void setMoc(String moc) {
        this.moc = moc;
    }

    public int isMainCar() {
        return isMainCar;
    }

    public void setMainCar(int mainCar) {
        isMainCar = mainCar;
    }

    public int getImg_resource() {
        return img_resource;
    }

    public void setImg_resource(int img_resource) {
        this.img_resource = img_resource;
    }
}
