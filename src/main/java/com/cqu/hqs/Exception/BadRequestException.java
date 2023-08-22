package com.cqu.hqs.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Deependra Karki
 */
@Setter
@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

    private String message;
}
