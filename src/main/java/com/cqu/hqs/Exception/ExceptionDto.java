package com.cqu.hqs.Exception;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Deependra Karki
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDto {
    public ExceptionDto(String message) {
        this.message = message;
    }

    public ExceptionDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    private String errorCode;
    private String message;
}