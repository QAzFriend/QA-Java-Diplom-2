package model;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order {

    private List<String> ingredients;

}

