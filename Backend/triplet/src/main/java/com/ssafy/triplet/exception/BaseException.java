package com.ssafy.triplet.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
        private final ErrorCode errorCode;

        public BaseException(ErrorCode errorCode){
            this.errorCode = errorCode;
        }
}
