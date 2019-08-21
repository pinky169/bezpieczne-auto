package patryk.bezpieczneauto.Objects;

public class Document {

    private String auto;
    private String info;
    private String additionalInfo;
    private String date;
    private String expiryDate;

    public Document(String auto, String info, String additionalInfo, String date, String expiryDate) {
        this.auto = auto;
        this.info = info;
        this.additionalInfo = additionalInfo;
        this.date = date;
        this.expiryDate = expiryDate;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
