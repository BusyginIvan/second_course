package rest.json;

import validation.InArray;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PointData {
    @DecimalMax(value = "5", message = "x не может быть больше 5.")
    @DecimalMin(value = "-5", message = "x не может быть меньше -5.")
    @NotNull(message = "Требуется числовое значение x.")
    private float x;
    @DecimalMax(value = "5", message = "y не может быть больше 5.")
    @DecimalMin(value = "-5", message = "y не может быть меньше -5.")
    @NotNull(message = "Требуется числовое значение y.")
    private float y;
    @InArray(array = { 1, 2, 3 }, message = "Недопустимое значение r. Ожидается 1, 2 или 3.")
    @NotNull(message = "Требуется числовое значение r.")
    private int r;
}
