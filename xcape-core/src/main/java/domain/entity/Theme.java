package domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@Builder @NoArgsConstructor
public class Theme {
    private String name;

    private int difficulty;
}
