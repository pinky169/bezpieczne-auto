package patryk.bezpieczneauto.Objects;


public class CarPart {

    // Nazwa nowej wstawionej części
    private String new_part;
    // Dodatkowe info
    private String additional_info;
    // Data wymiany
    private String date;
    // Cena nowej części
    private String price;
    // Ikonka dla części
    private int img_resource;

    public CarPart(String new_part, String additional_info, String date, String price, int img_resource) {
        this.new_part = new_part;
        this.additional_info = additional_info;
        this.date = date;
        this.price = price;
        this.img_resource = img_resource;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getNew_part() {
        return new_part;
    }

    public void setNew_part(String new_part) {
        this.new_part = new_part;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImg_resource() {
        return img_resource;
    }

    public void setImg_resource(int img_resource) {
        this.img_resource = img_resource;
    }
}
