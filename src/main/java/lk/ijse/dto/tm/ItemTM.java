package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemTM {
    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;

}
