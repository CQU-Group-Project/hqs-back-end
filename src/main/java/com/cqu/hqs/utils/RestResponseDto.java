
package com.cqu.hqs.utils;

import java.util.Optional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deependra Karki
 */
@Data
public class RestResponseDto {
        private String message;
    private Object detail;

    public static ResponseEntity<?> success(Object detail) {
        RestResponseDto dto = new RestResponseDto();
        dto.setDetail(detail);
        dto.setMessage(AppConstant.SUCCESSFUL);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    public static ResponseEntity<?> fail(HttpStatus httpStatus, Optional<String> message) {
        RestResponseDto dto = new RestResponseDto();
        dto.setMessage(message.orElse(null));
        return new ResponseEntity<>(dto, httpStatus);
    }
}
