package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {

    private String name;
    private String description;
    private String price;
    private String tax;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o){
        if(! (o instanceof ItemDTO)){
          return false;
        }
        return StringUtils.equals(this.name, ((ItemDTO) o).name) &&
            StringUtils.equals(this.description, ((ItemDTO) o).description) &&
            StringUtils.equals(this.price, ((ItemDTO) o).price) &&
            StringUtils.equals(this.tax, ((ItemDTO) o).tax);
    }

}
